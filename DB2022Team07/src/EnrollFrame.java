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

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EnrollFrame extends JFrame {
	
	LocalDate now = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	String formatedNow = now.format(formatter);
	java.sql.Date today = java.sql.Date.valueOf(formatedNow);
	
	private JTextField name_field = new JTextField(25);
	private JTextField phone_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);
	
	private JTextField membership_field = new JTextField(4);
	private JTextField startDate_field = new JTextField(8);
	
	private JButton close = new JButton("close");
	private JButton btn = new JButton("OK");
	private JLabel t1 = new JLabel();
	private JLabel t2 = new JLabel();
	private JLabel t3 = new JLabel();
	private JLabel t3_1 = new JLabel();
	private JLabel t4 = new JLabel();
	private Vector<Integer> id = new Vector<Integer>();
	private String member_id="";

	public EnrollFrame() {
		setTitle("Enroll Membership");
		
		membership_field.setVisible(false);
		startDate_field.setVisible(false);
		close.setVisible(false);
		
		id.clear();
		
		name_field.setText("");
		phone_field.setText("");
		passwd_field.setText("");
		membership_field.setText("");
		startDate_field.setText("");
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim()==null?"":name_field.getText().trim();
				String phone = phone_field.getText().trim()==null?"":phone_field.getText().replaceAll("-","").trim();
				String passwd = passwd_field.getText().trim()==null?"":passwd_field.getText().trim();
				BuyMembership(name, phone, passwd);
			}
		});
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name_field.setText("");
				phone_field.setText("");
				passwd_field.setText("");
				membership_field.setText("");
				
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t3_1.setText("");
				t4.setText("");
				
				member_id = null;
				
				dispose();
			}
		});
		membership_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String membership_id = membership_field.getText().trim()==null?"":membership_field.getText().trim();
				String start_date_inserted = startDate_field.getText();
				java.sql.Date start_date = java.sql.Date.valueOf(start_date_inserted);
				
				// 조회되지 않는 회원권인 경우 
				if (! id.contains(Integer.parseInt(membership_id))) {
					t4.setText("<html><body style='text-align:center;'>"
							+ "Wrong Membership number!<br />"
							+ "Enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				}
				// 시작날짜가 등록날짜(오늘) 이전인 경우 
				else if (start_date.before(today)) {
					t4.setText("<html><body style='text-align:center;'>"
							+ "Start Date Must be After Today(" + formatedNow + "). <br/>" 
							+ "Enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				}
				else {
					try (Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
							DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
						PreparedStatement pStmt = conn
								.prepareStatement("insert into db2022_enroll values(?,?,?,?);");
						pStmt.setString(1, member_id);
						pStmt.setDate(2, today);
						pStmt.setDate(3, start_date);
						pStmt.setString(4, membership_id);

						pStmt.executeUpdate();
						t4.setText("<html><body style='text-align:center;'>"
								+ "Membership Registration Completed.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
						close.setVisible(true);
					} catch (SQLException sqle) {
						System.out.println("SQL Exception: " + sqle);
					}
				}
			}
		});
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "Buy Membership<br />"
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
		contentPane.add(membership_field);
		contentPane.add(t3_1);
		contentPane.add(startDate_field);
		contentPane.add(t4);
		contentPane.add(close);

		setSize(350, 500);
		setVisible(true);
	}

	void BuyMembership(String member_name, String phone, String passwd) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from DB2022_members where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			
			if (!info_set.next()) {
				t1.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Member does not exist.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			} else {
				
				if (!passwd.equals(info_set.getString("password"))) {
					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Wrong password!<br/>"
							+ "----------------------------------------------------------------------------------</body></html>");
				} else {
					
					member_id = info_set.getString("member_id");

					pStmt = conn.prepareStatement("select * from DB2022_membership;\r\n");
					
					ResultSet membership_set = pStmt.executeQuery();

					t1.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "This is a list of Memberships Available.<br /> "
							+ "</body></html>");
					t2.setText("<html><body style='text-align:center;'>");
					
					while (membership_set.next()) {
						id.add(membership_set.getInt("membership_id"));
						t2.setText(t2.getText() + "[" + membership_set.getString("membership_id")+"]" + " "
								+ membership_set.getString("name") + ", Period: " + membership_set.getString("period") + " price "
								+ membership_set.getString("price") + " won<br />");

					}
					t2.setText(t2.getText() + "----------------------------------------------------------------------------------</body></html>");
					
					t3.setText("<html><body style='text-align:center;'>Please choose Membership number: </body></html>");
					t3_1.setText("<html><body style='text-align:center;'>When will you start using this Membership?: </body></html>");

					membership_field.setVisible(true);
					startDate_field.setVisible(true);
				}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}