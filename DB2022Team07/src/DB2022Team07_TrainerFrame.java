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

public class DB2022Team07_TrainerFrame extends JFrame {
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

	public DB2022Team07_TrainerFrame() {
		setTitle("Trainer");
		trainer_field.setVisible(false);
		close.setVisible(false);
		id.clear();
		name_field.setText("");
		phone_field.setText("");
		passwd_field.setText("");
		trainer_field.setText("");
		
		// 버튼 클릭 시, 사용자가 입력한 회원 정보를 빋아온다.
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim()==null?"":name_field.getText().trim();
				String phone = phone_field.getText().trim()==null?"":phone_field.getText().replaceAll("-","").trim();
				String passwd = passwd_field.getText().trim()==null?"":passwd_field.getText().trim();
				Trainer_Change(name, phone, passwd);
			}
		});
		
		// 닫기 버튼 클릭 시, 필드 값을 초기화한다.
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
		
		// 버튼 클릭 시, PT 신청/트레이너 변경하기
		trainer_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainer_id = trainer_field.getText().trim()==null?"":trainer_field.getText().trim();
				// 현재 등록된 트레이너이면,
				// 회원의 trainer 값에 트레이너 id를 추가한다.
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
								+ "Trainer Registration Completed.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
						close.setVisible(true);
					} catch (SQLException sqle) {
						System.out.println("SQL Exception: " + sqle);
					}
				}
				else {
					// 존재하는 트레이너 정보가 아니면 에러 메시지 띄우기
					t4.setText("<html><body style='text-align:center;'>"
							+ "Wrong trainer number!<br />"
							+ "Enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				}
			}
		});
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "Enroll PT / Change Trainer<br />"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Identity Verification *</body></html>"));
		contentPane.add(new JLabel("    Name    "));
		contentPane.add(name_field);
		contentPane.add(new JLabel("Telephone"));
		contentPane.add(phone_field);
		contentPane.add(new JLabel(" Password "));
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
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from DB2022_members use index(idx_memb_phone) join DB2022_enroll using(member_id) where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			// 입력 값에 맞는 회원이 없으면 없다고 메시지 띄우기
			if (!info_set.next()) {
				t1.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Member does not exist.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			} else {
				// 비밀번호를 틀리면 에러 메시지 띄우기
				if (!passwd.equals(info_set.getString("password"))) {
					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Wrong password!<br/>"
							+ "----------------------------------------------------------------------------------</body></html>");
				} else {
					// 현재 회원인 사용자이면
					member_id = info_set.getString("member_id");
					String member_branch = info_set.getString("branch");

					// 신청할 수 있는 트레이너 목록 보여주기
					pStmt = conn.prepareStatement(
							"select * from DB2022_trainers join DB2022_career using(trainer_id) where branch =?;\r\n");
					pStmt.setString(1, member_branch);
					ResultSet trainer_set = pStmt.executeQuery();

					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "This is a list of trainers<br /> "
							+ "belonging to your branch."
							+ "</body></html>");
					t2.setText("<html><body style='text-align:center;'>");
					while (trainer_set.next()) {
						id.add(trainer_set.getInt("trainer_id"));
						t2.setText(t2.getText() + "[" + trainer_set.getString("trainer_id")+"]" + " "
								+ trainer_set.getString("name") + ", Career: " + trainer_set.getString("career_year") + " year "
								+ trainer_set.getString("career_month") + " months<br />");

					}
					t2.setText(t2.getText() + "----------------------------------------------------------------------------------</body></html>");
					t3.setText("<html><body style='text-align:center;'>Please choose trainer number: </body></html>");
					trainer_field.setVisible(true);
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
