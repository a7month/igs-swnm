package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.postgresql.PGConnection;
import org.postgresql.fastpath.Fastpath;

public class NotificationTest {
	public static void main(String args[]) throws Exception {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://10.45.7.141:5432/igs";
		Properties props = new Properties();
		props.setProperty("user", "igs");
		props.setProperty("password", "igs");

		// Create two distinct connections, one for the notifier
		// and another for the listener to show the communication
		// works across connections although this example would
		// work fine with just one connection.
		Connection lConn = DriverManager.getConnection(url,props);
		Connection nConn = DriverManager.getConnection(url,props);
		
		Fastpath fp = ((PGConnection)lConn).getFastpathAPI();
		System.out.println("fastpath: "+fp.toString());

		// Create two threads, one to issue notifications and
		// the other to receive them.
		Listener listener = new Listener(lConn);
		Notifier notifier = new Notifier(nConn);
		listener.start();
		notifier.start();
	}
}
