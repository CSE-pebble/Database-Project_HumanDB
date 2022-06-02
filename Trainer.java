import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Trainer {
	public static void Trainer_Change(Connection conn, Statement stmt, String member_name, String phone) {
		Scanner s = new Scanner(System.in);
		try {
			// ȸ���� �̸��� ��������� �������� member_id �˻�
			ResultSet info_set = stmt
					.executeQuery("select * from DB2022_members join DB2022_enroll using(member_id)\r\n"
							+ "where name='" + member_name + "' and phone='" + phone + "';\r\n");
			// ȸ�� ������ �߸��Ǿ��ų�, ȸ������ ����� ȸ��DB�� ȸ���� �������� �ʴ� ���
			if (!info_set.next()) {
				System.out.println("�������� �ʴ� ȸ���Դϴ�.");
			} else {
				// ������ ���� password Ȯ��
				System.out.println("����Ȯ���� ���� ��й�ȣ�� �Է����ּ���.");
				String input_passwd = s.next();
				String passwd = info_set.getString("password");
				while (!input_passwd.equals(passwd)) {
					System.out.println("��й�ȣ�� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.");
					input_passwd = s.next();
				}
				;
				// ȸ���� member_id�� ���� ����
				String member_id = info_set.getString("member_id");
				String member_branch = info_set.getString("branch");

				// �ش� ȸ���� ������ �ִ� trainer ��� �˻�
				ResultSet trainer_set = stmt
						.executeQuery("select *\r\n" + "from DB2022_trainers join DB2022_career using(trainer_id)\r\n"
								+ "where branch ='" + member_branch + "';\r\n");
				System.out.println(member_branch + "�Ҽ� Ʈ���̳� ����Դϴ�.");
				while (trainer_set.next()) {
					System.out.println(trainer_set.getString("trainer_id") + "��: " + trainer_set.getString("name")
							+ ", ���: " + trainer_set.getString("career_year") + "�� "
							+ trainer_set.getString("career_month") + "����");
				}
				// ���ϴ� Ʈ���̳��� ��ȣ �Է�
				System.out.println("���ϴ� Ʈ���̳��� ��ȣ�� �Է����ּ���.");
				String trainer_id = s.next();
				// ������ trainer�� ���� Ȥ�� ���
				stmt.executeUpdate("update DB2022_members\r\n" + "set trainer ='" + trainer_id + "'\r\n"
						+ "where member_id='" + member_id + "';\r\n");
				System.out.println("Ʈ���̳ʰ� ����Ǿ����ϴ�.");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}

	public static void Trainer_Move(Connection conn, Statement stmt,String trainer, String branch) {
		try {
			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			stmt.executeUpdate("update DB2022_trainers\r\n" + "set branch ='" + branch + "'\r\n" + "where trainer='"
					+ trainer + "';\r\n");
			System.out.println("���� �̵� ó���Ǿ����ϴ�.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	public static void Trainer_Quit(Connection conn, Statement stmt, String trainer) {
		try {

			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			stmt.executeUpdate("delete from DB2022_trainers\r\n" + "where trainer_id='" + trainer + "';\r\n");

			System.out.println("���ó���Ǿ����ϴ�.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
