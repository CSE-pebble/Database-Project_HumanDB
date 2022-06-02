import java.sql.*;
import java.time.LocalDate;

import com.mysql.cj.protocol.Resultset;

public class Member {
	// 회원 등록 함수
	public static void new_Member(Connection conn, Statement stmt, String name, String gender, int height, int weight, String phone, String password, String branch) {
		try {
			ResultSet rset =  stmt.executeQuery("select max(member_id)+1 from DB2022_members;");
			rset.next();
			int new_member_id = rset.getInt(1);
			PreparedStatement pStmt = conn.prepareStatement(
					"insert into DB2022_members values(?,?,?,?,?,?,?,?,null);");
			pStmt.setInt(1, new_member_id);
			pStmt.setString(2, name);
			pStmt.setString(3, gender);
			pStmt.setInt(4, height);
			pStmt.setInt(5, weight);
			pStmt.setString(6, phone);
			pStmt.setString(7, password);
			pStmt.setString(8, branch);
			pStmt.executeUpdate();
			
		}catch(SQLException sqle) {
			System.out.println("SQLException: "+sqle);
		}
	}
	// 회원권 등록 함수
	public static void enroll(Connection conn, Statement stmt, int member_id, String enroll_date, String start_date, int membership) {
		try {
			PreparedStatement pStmt = conn.prepareStatement(
					"insert into DB2022_enroll values(?,?,?,?)");
			pStmt.setInt(1, member_id);
			pStmt.setString(2, enroll_date);
			pStmt.setString(3, start_date);
			pStmt.setInt(4, membership);
			pStmt.executeUpdate();
		}catch(SQLException sqle) {
			System.out.println("SQLException: "+sqle);
		}
	}
	// 만료 회원 삭제 함수
	public static void Expired_Member(Connection conn, Statement stmt) {
		try {
			LocalDate now = LocalDate.now();

			ResultSet rset = stmt.executeQuery("select member_id, name, start_date, end_date\r\n"
					+ "from DB2022_period as period\r\n" 
					+ "where period.end_date < DATE(NOW());");

			System.out.println(now + " 만료 회원 리스트");
			while (rset.next()) {
				String date = rset.getString("start_date");
				System.out.println(rset.getString("name") + " 회원 " + date + " ~ " + rset.getString("end_date"));
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
	// BMI 계산 함수
	public static void BMI_Calculator(Connection conn, Statement stmt, String member_name) {
		try {
			ResultSet rset = stmt.executeQuery("select name, (weight/((height/100)*(height/100))) as score\r\n"
					+ "from DB2022_members as members\r\n" 
					+ "where members.name='" + member_name + "';");
			System.out.println("회원별 BMI 점수");
			while (rset.next()) {
				System.out.println(rset.getString("name") + " 회원 점수 " + rset.getString("score"));
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}

}
