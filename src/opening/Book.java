package opening;

import gameLogic.Board;
import gameLogic.Move;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Book {
	
	public static List <Entry> entries = new ArrayList<Entry>();
	private static Statement statement;

	public static List<Move> getMoves (Board board) {
		List<Move> moves = new ArrayList<Move>();
		for (Entry e: entries) {
			if (board.equals(e.getBoard().getFEN())) {
				moves.add(e.getMove());
			}
		}
		return moves;
	}
	
	public static Move getMove(Board board) {
		List<Move> moves = getMoves(board);
		if (moves.size()!=0) {
			return moves.get((int)Math.floor(Math.random()*moves.size()));
		}
		return null;
	}
	
	public static void initBook () throws Exception {
		// Load Sun's jdbc-odbc driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://localhost:3306/openings";

		// Now attempt to create a database connection
		Connection con = DriverManager.getConnection (url, "root", "us1");
		statement = con.createStatement();
		
		ResultSet set = statement.executeQuery ("select * from openings;");
		while (set.next()) {
			entries.add(new Entry(set.getString("fen"), set.getInt("origin"), set.getInt("destination")));
		}
	}

	public static void addEntry(Board board, Move m) {
		String fen = board.getFEN();
		int from = m.getFrom();
		int to = m.getTo();
		entries.add(new Entry(fen, from, to));
		try {
			statement.execute("insert into openings values ('" + fen + "', " + from + ", " + to + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
