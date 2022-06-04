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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet.ColorAttribute;

public class TrainerFrame extends JFrame {
	private JTextField name_field = new JTextField(25);
	private JTextField phone_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);
	private JTextField trainer_field = new JTextField(4);
	private JButton close = new JButton("close");
	private JButton btn = new JButton("OK");
	private JLabel t1 = new JLabel();
	private JLabel t2 = new JLabel();
	private JLabel t3 = new JLabel();
	private JLabel t4 = new JLabel();
	private Vector<Integer> id = new Vector<Integer>();
	private String member_id="";

	public TrainerFrame() {
		setTitle("Trainer");
		trainer_field.setVisible(false);
		close.setVisible(false);
		id.clear();
		name_field.setText("");
		phone_field.setText("");
		passwd_field.setText("");
		trainer_field.setText("");
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim()==null?"":name_field.getText().trim();
				String phone = phone_field.getText().trim()==null?"":phone_field.getText().replaceAll("-","").trim();
				String passwd = passwd_field.getText().trim()==null?"":passwd_field.getText().trim();
				Trainer_Change(name, phone, passwd);
			}
		});
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name_field.setText("");
				phone_field.setText("");
				passwd_field.setText("");
				trainer_field.setText("");
				
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t4.setText("");
				
				member_id = null;
				
				dispose();
			}
		});
		trainer_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainer_id = trainer_field.getText().trim()==null?"":trainer_field.getText().trim();
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
						t4.setText("<html><body style='text-align:center;'>"
								+ "Ʈ���̳ʰ� ��ϵǾ����ϴ�.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
						close.setVisible(true);
					} catch (SQLException sqle) {
						System.out.println("SQL Exception: " + sqle);
					}
				}
				else {
					t4.setText("<html><body style='text-align:center;'>"
							+ "�߸��� Ʈ���̳� ��ȣ�Դϴ�.<br />"
							+ "�ٽ� �Է����ּ���.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				}
			}
		});
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "PT ��û / Ʈ���̳� ����<br />"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* ���� Ȯ�� *</body></html>"));
		contentPane.add(new JLabel("   �̸�    "));
		contentPane.add(name_field);
		contentPane.add(new JLabel("��ȭ��ȣ"));
		contentPane.add(phone_field);
		contentPane.add(new JLabel("��й�ȣ"));
		contentPane.add(passwd_field);
		contentPane.add(btn);
		contentPane.add(t1);
		contentPane.add(t2);
		contentPane.add(t3);
		contentPane.add(trainer_field);
		contentPane.add(t4);
		contentPane.add(close);

		setSize(350, 500);
		setVisible(true);
	}

	void Trainer_Change(String member_name, String phone, String passwd) {
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
				t1.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "�������� �ʴ� ȸ���Դϴ�.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			} else {
				// ������ ���� password Ȯ��
				if (!passwd.equals(info_set.getString("password"))) {
					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "��й�ȣ�� �߸��Ǿ����ϴ�.<br/>"
							+ "----------------------------------------------------------------------------------</body></html>");
				} else {
					// ȸ���� member_id�� ���� ����
					member_id = info_set.getString("member_id");
					String member_branch = info_set.getString("branch");

					// �ش� ȸ���� ������ �ִ� trainer ��� �˻�
					pStmt = conn.prepareStatement(
							"select * from DB2022_trainers join DB2022_career using(trainer_id) where branch =?;\r\n");
					pStmt.setString(1, member_branch);
					ResultSet trainer_set = pStmt.executeQuery();

					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "ȸ������ ��� ������<br /> "
							+ "�Ҽӵ� Ʈ���̳� ����Դϴ�."
							+ "</body></html>");
					t2.setText("<html><body style='text-align:center;'>");
					while (trainer_set.next()) {
						id.add(trainer_set.getInt("trainer_id"));
						t2.setText(t2.getText() + trainer_set.getString("trainer_id") + "��: "
								+ trainer_set.getString("name") + ", ���: " + trainer_set.getString("career_year") + "�� "
								+ trainer_set.getString("career_month") + "����<br />");

					}
					t2.setText(t2.getText() + "----------------------------------------------------------------------------------</body></html>");
					// ���ϴ� Ʈ���̳��� ��ȣ �Է�
					t3.setText("<html><body style='text-align:center;'>���ϴ� Ʈ���̳��� ��ȣ</body></html>");
					trainer_field.setVisible(true);
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}