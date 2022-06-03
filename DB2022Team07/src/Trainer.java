import java.awt.*;
import java.sql.*;
import java.util.Scanner;

import javax.swing.*;

public class Trainer {
	// 트레이너 지점 이동 함수
	public static void Trainer_Move(Connection conn, Statement stmt, String trainer, String branch) {
		try {
			// 해당 트레이너가 맡은 회원의 trainer -> null
			PreparedStatement pStmt = conn
					.prepareStatement("update DB2022_members set trainer = null where trainer=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();
			// 해당 트레이너의 지점 정보 변경
			pStmt = conn.prepareStatement("update DB2022_trainers set branch =? where trainer=?;");
			pStmt.setString(1, branch);
			pStmt.setString(2, trainer);
			pStmt.executeUpdate();

			System.out.println("지점 이동 처리되었습니다.");

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
			System.out.println("퇴사처리되었습니다.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
