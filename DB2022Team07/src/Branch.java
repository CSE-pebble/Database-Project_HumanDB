import java.sql.*;

public class Branch {
	
	public static void Monthly_Revenue(Connection conn, Statement stmt, String year, String month) {
		try {
			PreparedStatement pStmt = conn.prepareStatement("select branch, sum(price) as 'revenue'\r\n"
					+ "from DB2022_enroll join DB2022_membership \r\n"
					+ "on DB2022_enroll.membership=DB2022_membership.membership_id\r\n join DB2022_members using(member_id)\r\n" 
					+ "where year(enroll_date)=? and month(enroll_Date)=?\r\n" 
					+ "group by branch\r\n" 
					+ "order by branch; ");
			pStmt.setString(1, year);
			pStmt.setString(2, month);
			ResultSet rset = pStmt.executeQuery();
			System.out.println(year + "년 " + month + "월의 " + "매출: ");
			while (rset.next()) {
				System.out.println(rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
	
	public static void Branch_Info(Connection conn, Statement stmt) {
		try {			
			/* 지점별 회원 수 */
			ResultSet rset = stmt.executeQuery("select branch, count(*) from DB2022_members group by branch;");
			while (rset.next()) {
				System.out.println("지점별 회원 수 : " + rset.getString("branch") + " " + rset.getInt(2));
			}
			System.out.println("\n");
			
			/* 지점별 트레이너 수 */
			ResultSet rset2 = stmt.executeQuery("select branch, count(*) from DB2022_trainers group by branch;");
			while (rset2.next()) {
				System.out.println("지점별 트레이너 수 : " + rset2.getString("branch") + " " + rset2.getInt(2));
			}
			System.out.println("\n");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}


}
