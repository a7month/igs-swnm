package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Notifier extends Thread {
	private Connection conn;

	public Notifier(Connection conn) {
		this.conn = conn;
	}

	public void run() {
		while (true) {
			try {
				Statement stmt = conn.createStatement();
				stmt.execute("NOTIFY mymessage");
				stmt.close();
				Thread.sleep(2000);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
