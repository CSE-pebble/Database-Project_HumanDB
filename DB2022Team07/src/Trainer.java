import java.awt.*;
import java.sql.*;
import java.util.Scanner;

import javax.swing.*;

public class Trainer {
	public static void Trainer_Change(Container contentPane, String member_name, String phone,String passwd) {
		Scanner s = new Scanner(System.in);
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
			// ȸ���� �̸��� ��ȭ��ȣ�� �������� member_id �˻�
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from DB2022_members join DB2022_enroll using(member_id) where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			// ȸ�� ������ �߸��Ǿ��ų�, ȸ������ ����� ȸ��DB�� ȸ���� �������� �ʴ� ���
			if (!info_set.next()) {
				System.out.println("�������� �ʴ� ȸ���Դϴ�.");
			} else {
				// ������ ���� password Ȯ��
				if (!passwd.equals(info_set.getString("password"))) {
					System.out.println("��й�ȣ�� �߸��Ǿ����ϴ�.");
				} else {
					// ȸ���� member_id�� ���� ����
					String member_id = info_set.getString("member_id");
					String member_branch = info_set.getString("branch");

					// �ش� ȸ���� ������ �ִ� trainer ��� �˻�
					pStmt = conn.prepareStatement("select * from DB2022_trainers join DB2022_career using(trainer_id) where branch =?;\r\n");
					pStmt.setString(1, member_branch);
					ResultSet trainer_set = pStmt.executeQuery();

					contentPane.add(new JLabel("�Ҽ� Ʈ���̳� ����Դϴ�."));
					while (trainer_set.next()) {
						System.out.printf("%s��: %s, ���: %s�� %s����\n", trainer_set.getString("trainer_id"),
								trainer_set.getString("name"), trainer_set.getString("career_year"),
								trainer_set.getString("career_month"));
					}
					// ���ϴ� Ʈ���̳��� ��ȣ �Է�
					System.out.println("���ϴ� Ʈ���̳��� ��ȣ�� �Է����ּ���.");
					String trainer_id = s.next();
					// ������ trainer�� ���� Ȥ�� ���
					pStmt = conn.prepareStatement("update DB2022_members set trainer =? where member_id=?;");
					pStmt.setString(1, trainer_id);
					pStmt.setString(2, member_id);
					pStmt.executeUpdate();
					System.out.println("Ʈ���̳ʰ� ����Ǿ����ϴ�.");
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}

	// Ʈ���̳� ���� �̵� �Լ�
	public static void Trainer_Move(Connection conn, Statement stmt, String trainer, String branch) {
		try {
			// �ش� Ʈ���̳ʰ� ���� ȸ���� trainer -> null
			PreparedStatement pStmt = conn
					.prepareStatement("update DB2022_members set trainer = null where trainer=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();
			// �ش� Ʈ���̳��� ���� ���� ����
			pStmt = conn.prepareStatement("update DB2022_trainers set branch =? where trainer=?;");
			pStmt.setString(1, branch);
			pStmt.setString(2, trainer);
			pStmt.executeUpdate();

			System.out.println("���� �̵� ó���Ǿ����ϴ�.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	public static void Trainer_Quit(Connection conn, Statement stmt, String trainer) {
		try {

			PreparedStatement pStmt = conn
					.prepareStatement("update DB2022_members set trainer = null where trainer=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();

			pStmt = conn.prepareStatement("delete from DB2022_trainers where trainer_id=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();
			System.out.println("���ó���Ǿ����ϴ�.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
