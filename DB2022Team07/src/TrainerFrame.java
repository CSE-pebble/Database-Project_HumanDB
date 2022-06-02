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
		setTitle("Trainer ���� ���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>pt ���/ Ʈ���̳� ����<br />���� Ȯ��</body></html>"));
		contentPane.add(new JLabel("* �̸�"));
		contentPane.add(name_field);
		contentPane.add(new JLabel("* ��ȭ��ȣ"));
		contentPane.add(phone_field);
		contentPane.add(new JLabel("* ��й�ȣ"));
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
				// ������ trainer�� ���� Ȥ�� ���
				if (id.contains(Integer.parseInt(trainer_id))) {
					try (Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
							DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
						PreparedStatement pStmt = conn
								.prepareStatement("update DB2022_members set trainer =? where member_id=?;");
						pStmt.setString(1, trainer_id);
						pStmt.setString(2, member_id);
						pStmt.executeUpdate();
						t4.setText("<html><body style='text-align:center;'>Ʈ���̳ʰ� ����Ǿ����ϴ�.</body></html>");
					} catch (SQLException sqle) {
						System.out.println("SQL Exception: " + sqle);
					}
				}
				else {
					t4.setText("<html><body style='text-align:center;'>�߸��� Ʈ���̳� �����Դϴ�. �ٽ� �Է����ּ���.</body></html>");
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
			// ȸ���� �̸��� ��ȭ��ȣ�� �������� member_id �˻�
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from DB2022_members join DB2022_enroll using(member_id) where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			// ȸ�� ������ �߸��Ǿ��ų�, ȸ������ ����� ȸ��DB�� ȸ���� �������� �ʴ� ���
			if (!info_set.next()) {
				t1.setText("�������� �ʴ� ȸ���Դϴ�.");
			} else {
				// ������ ���� password Ȯ��
				if (!passwd.equals(info_set.getString("password"))) {
					t1.setText("��й�ȣ�� �߸��Ǿ����ϴ�.");
				} else {
					// ȸ���� member_id�� ���� ����
					member_id = info_set.getString("member_id");
					String member_branch = info_set.getString("branch");

					// �ش� ȸ���� ������ �ִ� trainer ��� �˻�
					pStmt = conn.prepareStatement(
							"select * from DB2022_trainers join DB2022_career using(trainer_id) where branch =?;\r\n");
					pStmt.setString(1, member_branch);
					ResultSet trainer_set = pStmt.executeQuery();

					t1.setText("�Ҽ� Ʈ���̳� ����Դϴ�.");
					while (trainer_set.next()) {
						id.add(trainer_set.getInt("trainer_id"));
						t2.setText(t2.getText() + trainer_set.getString("trainer_id") + "��: "
								+ trainer_set.getString("name") + ", ���: " + trainer_set.getString("career_year") + "�� "
								+ trainer_set.getString("career_month") + "����<br />");

					}
					t2.setText(t2.getText() + "</body></html>");
					// ���ϴ� Ʈ���̳��� ��ȣ �Է�
					t3.setText("<html><body style='text-align:center;'>���ϴ� Ʈ���̳��� ��ȣ�� �Է����ּ���.</body></html>");
					trainer_field.setVisible(true);
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}
}