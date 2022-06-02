import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class DB2022Team07_main {
	static final String DBID = "DB2022Team07";
	static final String USERID = "root";
	static final String PASSWD = "wcdi9786@#";

	public static void main(String args[]) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			// Initialize.Create(conn, stmt);
			// Initialize.View(conn, stmt);
			// Initialize.InsertInitialData(conn, stmt);
			TrainerFrame frame = new TrainerFrame();
		} catch (SQLException sqle) {
			System.out.println("SQL Exception: " + sqle);
		}

	}
}

