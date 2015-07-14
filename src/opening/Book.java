package opening;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gameLogic.Board;
import gameLogic.Move;

public class Book {
	
	public static List <Entry> entries = new ArrayList<Entry>();
	private static Statement statement;
	public static boolean database;

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
	
	public static void exportEntries () {
		try {
			ResultSet set = statement.executeQuery("select * from openings;");
			PrintWriter out = new PrintWriter (new FileWriter (new File("./Resources/openings.txt"),true));
			while (set.next()) {
				StringBuilder str = new StringBuilder(set.getString("fen"));
				str.append(",");
				str.append(set.getInt("origin"));
				str.append(",");
				str.append(set.getInt("destination"));
				out.println(str);
			}
			out.close();
			
		} catch (Exception e) {
			System.err.println("Failed to export");
		}

	}
	
	public static void initBook (boolean database) throws Exception {
		Book.database = database;
		if (database) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/openings";

			Connection con = DriverManager.getConnection (url, "root", "us1");
			statement = con.createStatement();
			
			ResultSet set = statement.executeQuery ("select * from openings;");
			while (set.next()) {
				entries.add(new Entry(set.getString("fen"), set.getInt("origin"), set.getInt("destination")));
			}
		} else {
			BufferedReader br = new BufferedReader (new FileReader("./Resources/openings.txt"));
			String line;
			while ((line=br.readLine())!=null) {
				String [] arr = line.split(",");
				entries.add(new Entry(arr[0], Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
			}
			br.close();
			
		}

	}

	public static void addEntry(Board board, Move m) {
		if (database) {
			String fen = board.getFEN();
			int from = m.getFrom();
			int to = m.getTo();
			entries.add(new Entry(fen, from, to));
			try {
				statement.execute("insert into openings values ('" + fen + "', " + from + ", " + to + ");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			PrintWriter out = null;
			try {
				out = new PrintWriter (new FileWriter (new File("./Resources/openings.txt"),true));
			} catch (IOException e) {
				e.printStackTrace();
			}
			StringBuilder str = new StringBuilder(board.getFEN());
			str.append(",");
			str.append(m.getFrom());
			str.append(",");
			str.append(m.getTo());
			out.println(str);
			out.close();
			
		}

	}

}
