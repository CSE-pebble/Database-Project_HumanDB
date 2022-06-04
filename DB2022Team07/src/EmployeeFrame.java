import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class EmployeeFrame extends JFrame {
	private JTextField name_field = new JTextField(25);
	private JTextField branch_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);
	private JTextField new_branch_field = new JTextField(10);
	private JTextField new_pwd_field = new JTextField(10);
	private JButton identify_btn = new JButton("Search");
	private JButton quit_menu = new JButton("Resignation");
	private JButton move_menu = new JButton("Transfer Branch");
	private JButton pwd_menu = new JButton("Change Password");
	private JButton delete_menu = new JButton("Delete Expired Member");

	private JButton close = new JButton("Close");
	private JLabel t1 = new JLabel();
	private JLabel t2 = new JLabel();
	private JLabel t3 = new JLabel();
	private JLabel t4 = new JLabel();
	private JLabel t5 = new JLabel();
	private String trainer_id = "", trainer_branch = "";
	private Vector<String> branch_list = new Vector<String>();
	private Vector<String> delete_list = new Vector<String>();

	public EmployeeFrame() {
		setTitle("Employee");
		quit_menu.setVisible(false);
		move_menu.setVisible(false);
		pwd_menu.setVisible(false);
		new_branch_field.setVisible(false);
		new_pwd_field.setVisible(false);
		close.setVisible(false);
		branch_list.clear();
		delete_list.clear();
		name_field.setText("");
		branch_field.setText("");
		passwd_field.setText("");
		new_branch_field.setText("");
		new_pwd_field.setText("");

		identify_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim() == null ? "" : name_field.getText().trim();
				String branch = branch_field.getText().trim() == null ? "" : branch_field.getText().trim();
				String passwd = passwd_field.getText().trim() == null ? "" : passwd_field.getText().trim();

				try (Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
						DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
					PreparedStatement pStmt = conn.prepareStatement(
							"select trainer_id, branch from DB2022_trainers where name=? and branch=? and password=?;");
					pStmt.setString(1, name);
					pStmt.setString(2, branch);
					pStmt.setString(3, passwd);
					ResultSet rset = pStmt.executeQuery();
					if (!rset.next()) {
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Wrong trainer Information!<br/>" + "Please enter it Again.<br/>"
								+ "----------------------------------------------------------------------------------"
								+ "</body></html>");
					} else {
						trainer_id = rset.getString(1);
						trainer_branch = rset.getString(2);

						quit_menu.setVisible(true);
						move_menu.setVisible(true);
						pwd_menu.setVisible(true);
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Please select the service.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					}

				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle);
				}

			}

		});
		quit_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t2.setText("");
				t3.setText("");
				t4.setText("");
				t5.setText("");
				new_branch_field.setVisible(false);
				new_pwd_field.setVisible(false);
				Trainer_Quit(trainer_id);
			}

		});
		move_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t2.setText("");
				t3.setText("");
				t4.setText("");
				t5.setText("");
				new_branch_field.setText("");
				new_pwd_field.setVisible(false);
				close.setVisible(false);

				try (Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
						DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
					// �씠�룞 媛��뒫�븳 吏��젏 由ъ뒪�듃 異쒕젰
					PreparedStatement pStmt = conn
							.prepareStatement("select name from DB2022_branches where name != ?;");
					pStmt.setString(1, trainer_branch);
					ResultSet rset = pStmt.executeQuery();
					t2.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "[ List of transferable branches ]<br/>");
					while (rset.next()) {
						branch_list.add(rset.getString("name"));
						t2.setText(t2.getText() + rset.getString("name") + "<br/>");
					}
				} catch (SQLException sqle) {
					System.out.println("SQL Exception: " + sqle);
				}

				t2.setText(t2.getText() + "</body></html>");
				t3.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Please choose the branch: " + "</body></html>");
				new_branch_field.setVisible(true);
			}
		});
		pwd_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t3.setText("");
				t4.setText("");
				t5.setText("");
				new_pwd_field.setText("");
				new_branch_field.setVisible(false);
				close.setVisible(false);

				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "New password :" + "</body></html>");
				new_pwd_field.setVisible(true);
			}

		});
		delete_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				delete_member();
			}

		});
		new_branch_field.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (new_branch_field.getText().trim() != null)
					Trainer_Move(trainer_id, new_branch_field.getText().trim());
			}

		});
		new_pwd_field.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (new_pwd_field.getText().trim() != null) {
					if (new_pwd_field.getText().trim().length() != 4) {
						t3.setText("<html><body style='text-align:center;'>" + "Wrong password.<br/>" + "Please enter it again.<br/>"
								+ "----------------------------------------------------------------------------------"
								+ "</body></html>");
					} else {
						change_pwd(new_pwd_field.getText().trim());
					}
				}

			}

		});
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name_field.setText("");
				branch_field.setText("");
				passwd_field.setText("");
				new_branch_field.setText("");
				new_pwd_field.setText("");
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t4.setText("");

				trainer_id = "";
				trainer_branch = "";
				dispose();
			}
		});
		Container content = getContentPane();
		content.setLayout(new FlowLayout());
		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Trainer Menu *" + "</body></html>"));
		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Hereby Certify *<br/>" + "</body></html>"));
		content.add(new JLabel("    Name    "));
		content.add(name_field);
		content.add(new JLabel("   Branch  "));
		content.add(branch_field);
		content.add(new JLabel(" Password "));
		content.add(passwd_field);
		content.add(identify_btn);
		content.add(t1);
		content.add(quit_menu);
		content.add(move_menu);
		content.add(pwd_menu);
		content.add(t2);
		content.add(new_pwd_field);
		content.add(t3);
		content.add(new_branch_field);
		content.add(t4);
		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Administrator Menu *<br/>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "</body></html>"));
		content.add(delete_menu);
		content.add(t5);
		content.add(close);
		setSize(350, 800);
		setVisible(true);
	}

	void Trainer_Quit(String trainer) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			PreparedStatement pStmt = conn
					.prepareStatement("update DB2022_members set trainer = null where trainer=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();

			pStmt = conn.prepareStatement("delete from DB2022_trainers where trainer_id=?;");
			pStmt.setString(1, trainer);
			pStmt.executeUpdate();

			t2.setText("<html><body style='text-align:center;'>"
					+ "----------------------------------------------------------------------------------<br/>"
					+ "Resignation has been processed.<br/>"
					+ "----------------------------------------------------------------------------------<br/>"
					+ "</body></html>");
			close.setVisible(true);
			move_menu.setEnabled(false);
			pwd_menu.setEnabled(false);

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	void Trainer_Move(String trainer, String branch) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			if (branch_list.contains(branch)) {
				// 占쌔댐옙 트占쏙옙占싱너곤옙 占쏙옙占쏙옙 회占쏙옙占쏙옙 trainer -> null
				PreparedStatement pStmt = conn
						.prepareStatement("update DB2022_members set trainer = null where trainer=?;");
				pStmt.setString(1, trainer);
				pStmt.executeUpdate();
				// 占쌔댐옙 트占쏙옙占싱놂옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
				pStmt = conn.prepareStatement("update DB2022_trainers set branch =? where trainer_id=?;");
				pStmt.setString(1, branch);
				pStmt.setString(2, trainer);
				pStmt.executeUpdate();
				t4.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Branch is changed.<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
				trainer_branch = branch;
				close.setVisible(true);
			} else {
				t4.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Wrong branch name!<br/>" + "Please re-enter the branch name.<br />"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	void change_pwd(String pwd) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			PreparedStatement pStmt = conn
					.prepareStatement("" + "update DB2022_trainers set password=? where trainer_id=?");
			pStmt.setString(1, pwd);
			pStmt.setString(2, trainer_id);
			pStmt.executeUpdate();

			t3.setText("<html><body style='text-align:center;'>"
					+ "----------------------------------------------------------------------------------<br/>"
					+ "Password is changed.<br/>"
					+ "----------------------------------------------------------------------------------<br/>"
					+ "</body></html>");
			close.setVisible(true);
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}

	void delete_member() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			ResultSet rset = stmt
					.executeQuery("select name,member_id from DB2022_period where timestampdiff(day,end_date,curdate())>0;");
			if (!rset.next()) {
				t5.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "There is no expired member.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			} else {
				t5.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>");
				do {
					t5.setText(t5.getText() + rset.getString("name") + "占쏙옙<br/>");
					PreparedStatement pStmt = conn.prepareStatement("delete from DB2022_enroll where member_id=?");
					pStmt.setString(1, rset.getString("member_id"));
					pStmt.executeUpdate();
				}while(rset.next());
				t5.setText(t5.getText() + "Membership Information is Deleted.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			}
			close.setVisible(true);
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
}
