import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Trainer {
	public static void Trainer_Change(Connection conn, Statement stmt, String member_name, String phone) {
		Scanner s = new Scanner(System.in);
		try {
			// 회원의 이름과 생년월일을 바탕으로 member_id 검색
			ResultSet info_set = stmt
					.executeQuery("select * from DB2022_members join DB2022_enroll using(member_id)\r\n"
							+ "where name='" + member_name + "' and phone='" + phone + "';\r\n");
			// 회원 정보가 잘못되었거나, 회원권을 등록한 회원DB에 회원이 존재하지 않는 경우
			if (!info_set.next()) {
				System.out.println("존재하지 않는 회원입니다.");
			} else {
				// 보안을 위해 password 확인
				System.out.println("본인확인을 위해 비밀번호를 입력해주세요.");
				String input_passwd = s.next();
				String passwd = info_set.getString("password");
				while (!input_passwd.equals(passwd)) {
					System.out.println("비밀번호가 잘못되었습니다. 다시 입력해주세요.");
					input_passwd = s.next();
				}
				;
				// 회원의 member_id와 지점 저장
				String member_id = info_set.getString("member_id");
				String member_branch = info_set.getString("branch");

				// 해당 회원의 지점에 있는 trainer 목록 검색
				ResultSet trainer_set = stmt
						.executeQuery("select *\r\n" + "from DB2022_trainers join DB2022_career using(trainer_id)\r\n"
								+ "where branch ='" + member_branch + "';\r\n");
				System.out.println(member_branch + "소속 트레이너 명단입니다.");
				while (trainer_set.next()) {
					System.out.println(trainer_set.getString("trainer_id") + "번: " + trainer_set.getString("name")
							+ ", 경력: " + trainer_set.getString("career_year") + "년 "
							+ trainer_set.getString("career_month") + "개월");
				}
				// 원하는 트레이너의 번호 입력
				System.out.println("원하는 트레이너의 번호를 입력해주세요.");
				String trainer_id = s.next();
				// 선택한 trainer로 변경 혹은 등록
				stmt.executeUpdate("update DB2022_members\r\n" + "set trainer ='" + trainer_id + "'\r\n"
						+ "where member_id='" + member_id + "';\r\n");
				System.out.println("트레이너가 변경되었습니다.");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}

	public static void Trainer_Move(Connection conn, Statement stmt,String trainer, String branch) {
		try {
			conn.setAutoCommit(false);
			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			conn.commit();
			stmt.executeUpdate("update DB2022_trainers\r\n" + "set branch ='" + branch + "'\r\n" + "where trainer='"
					+ trainer + "';\r\n");
			System.out.println("지점 이동 처리되었습니다.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	public static void Trainer_Quit(Connection conn, Statement stmt, String trainer) {
		try {

			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			stmt.executeUpdate("delete from DB2022_trainers\r\n" + "where trainer_id='" + trainer + "';\r\n");

			System.out.println("퇴사처리되었습니다.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
