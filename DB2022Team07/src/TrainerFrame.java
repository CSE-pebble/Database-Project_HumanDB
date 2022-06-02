import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TrainerFrame extends JFrame {
	private static JTextField name_field = new JTextField(20);
	private static JTextField phone_field = new JTextField(11);
	private static JTextField passwd_field = new JTextField(4);
	private static JLabel t1 = new JLabel();
	private static JLabel t2 = new JLabel("<html><body style='text-align:center;'>");
	private static JLabel t3 = new JLabel();
	private static JLabel t4 = new JLabel();
	private static JTextField trainer_field = new JTextField(4);
	private static Vector<Integer> id = new Vector<Integer>();
	private static String member_id;

	public TrainerFrame() {
		setTitle("Trainer 관련 기능");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>pt 등록/ 트레이너 변경<br />본인 확인</body></html>"));
		contentPane.add(new JLabel("* 이름"));
		contentPane.add(name_field);
		contentPane.add(new JLabel("* 전화번호"));
		contentPane.add(phone_field);
		contentPane.add(new JLabel("* 비밀번호"));
		contentPane.add(passwd_field);

		JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim();
				String phone = phone_field.getText().trim();
				String passwd = passwd_field.getText().trim();
				Trainer_Change(name, phone, passwd);
			}
		});
		contentPane.add(btn);
		contentPane.add(t1);
		contentPane.add(t2);
		contentPane.add(t3);
		contentPane.add(trainer_field);
		trainer_field.setVisible(false);
		trainer_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainer_id = trainer_field.getText().trim();
				// 선택한 trainer로 변경 혹은 등록
				if (id.contains(Integer.parseInt(trainer_id))) {
					try (Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
							DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
						PreparedStatement pStmt = conn
								.prepareStatement("update DB2022_members set trainer =? where member_id=?;");
						pStmt.setString(1, trainer_id);
						pStmt.setString(2, member_id);
						pStmt.executeUpdate();
						t4.setText("<html><body style='text-align:center;'>트레이너가 변경되었습니다.</body></html>");
					} catch (SQLException sqle) {
						System.out.println("SQL Exception: " + sqle);
					}
				}
				else {
					t4.setText("<html><body style='text-align:center;'>잘못된 트레이너 정보입니다. 다시 입력해주세요.</body></html>");
				}
			}
		});
		contentPane.add(t4);
		setSize(300, 300);
		setVisible(true);
	}

	static void Trainer_Change(String member_name, String phone, String passwd) {
		Scanner s = new Scanner(System.in);
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			// 회원의 이름과 전화번호를 바탕으로 member_id 검색
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from DB2022_members join DB2022_enroll using(member_id) where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			// 회원 정보가 잘못되었거나, 회원권을 등록한 회원DB에 회원이 존재하지 않는 경우
			if (!info_set.next()) {
				t1.setText("존재하지 않는 회원입니다.");
			} else {
				// 보안을 위해 password 확인
				if (!passwd.equals(info_set.getString("password"))) {
					t1.setText("비밀번호가 잘못되었습니다.");
				} else {
					// 회원의 member_id와 지점 저장
					member_id = info_set.getString("member_id");
					String member_branch = info_set.getString("branch");

					// 해당 회원의 지점에 있는 trainer 목록 검색
					pStmt = conn.prepareStatement(
							"select * from DB2022_trainers join DB2022_career using(trainer_id) where branch =?;\r\n");
					pStmt.setString(1, member_branch);
					ResultSet trainer_set = pStmt.executeQuery();

					t1.setText("소속 트레이너 명단입니다.");
					while (trainer_set.next()) {
						id.add(trainer_set.getInt("trainer_id"));
						t2.setText(t2.getText() + trainer_set.getString("trainer_id") + "번: "
								+ trainer_set.getString("name") + ", 경력: " + trainer_set.getString("career_year") + "년 "
								+ trainer_set.getString("career_month") + "개월<br />");

					}
					t2.setText(t2.getText() + "</body></html>");
					// 원하는 트레이너의 번호 입력
					t3.setText("<html><body style='text-align:center;'>원하는 트레이너의 번호를 입력해주세요.</body></html>");
					trainer_field.setVisible(true);
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}
}