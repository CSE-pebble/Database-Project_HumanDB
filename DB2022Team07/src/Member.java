import java.sql.*;
import java.time.LocalDate;

public class Member {
	// ���� ȸ�� ���� �Լ�
	public static void Expired_Member(Connection conn, Statement stmt) {
		try {
			LocalDate now = LocalDate.now();

			ResultSet rset = stmt.executeQuery("select member_id, name, start_date, end_date\r\n"
					+ "from DB2022_period as period\r\n" 
					+ "where period.end_date < DATE(NOW());");

			System.out.println(now + " ���� ȸ�� ����Ʈ");
			while (rset.next()) {
				String date = rset.getString("start_date");
				System.out.println(rset.getString("name") + " ȸ�� " + date + " ~ " + rset.getString("end_date"));
				PreparedStatement pStmt2 = conn.prepareStatement("delete from DB2022_enroll\r\n" 
				+ "where member_id="
						+ rset.getInt("member_id") 
						+ " and start_date='" + date + "';");
				pStmt2.executeUpdate();
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
	// BMI ��� �Լ�
	public static void BMI_Calculator(Connection conn, Statement stmt, String member_name) {
		try {
			ResultSet rset = stmt.executeQuery("select name, (weight/((height/100)*(height/100))) as score\r\n"
					+ "from DB2022_members as members\r\n" 
					+ "where members.name='" + member_name + "';");
			System.out.println("ȸ���� BMI ����");
			while (rset.next()) {
				System.out.println(rset.getString("name") + " ȸ�� ���� " + rset.getString("score"));
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}

}
