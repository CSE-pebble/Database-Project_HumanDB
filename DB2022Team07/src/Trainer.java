import java.awt.*;
import java.sql.*;
import java.util.Scanner;

import javax.swing.*;

public class Trainer {
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
