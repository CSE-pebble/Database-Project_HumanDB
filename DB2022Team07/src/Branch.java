import java.sql.*;

public class Branch {
	
	public static void Monthly_Revenue(Connection conn, Statement stmt, String year, String month) {
		try {
			ResultSet rset = stmt.executeQuery("select branch, sum(price) as 'revenue'\r\n"
					+ "from DB2022_enroll join DB2022_membership \r\n"
					+ "on DB2022_enroll.membership=DB2022_membership.membership_id\r\n"
					+ "join DB2022_members using(member_id)\r\n" + "where year(enroll_date)='" + year
					+ "' and month(enroll_Date)='" + month + "'\r\n" + "group by branch\r\n" + "order by branch; ");
			System.out.println(year + "�� " + month + "���� " + "����: ");
			while (rset.next()) {
				System.out.println(rset.getString("branch") + "����: " + rset.getInt("revenue") + "��");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
	
	public static void Branch_Info(Connection conn, Statement stmt) {
		try {			
			/* ������ ȸ�� �� */
			ResultSet rset = stmt.executeQuery("select branch, count(*) from DB2022_members group by branch;");
			while (rset.next()) {
				System.out.println("������ ȸ�� �� : " + rset.getString("branch") + " " + rset.getInt(2));
			}
			System.out.println("\n");
			
			/* ������ Ʈ���̳� �� */
			ResultSet rset2 = stmt.executeQuery("select branch, count(*) from DB2022_trainers group by branch;");
			while (rset2.next()) {
				System.out.println("������ Ʈ���̳� �� : " + rset2.getString("branch") + " " + rset2.getInt(2));
			}
			System.out.println("\n");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}


}
