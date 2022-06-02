import java.sql.*;

public class DB2022Team07_main {
	static final String DBID = "DB2022Team07";
	static final String USERID = "root";
	static final String PASSWD = "wcdi9786@#";
	public static void main(String args[]) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			Initialize.Create(conn, stmt);
			Initialize.View(conn, stmt);
			Initialize.InsertInitialData(conn, stmt);
			
			
			
		}catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
}
