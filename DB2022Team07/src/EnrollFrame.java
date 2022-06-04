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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EnrollFrame extends JFrame {
	
	private Vector<Integer> id = new Vector<Integer>(); // 회원권 아이디를 담은 벡터
	private String member_id="";
		
	// 현재 날짜 받아와서 포매팅 
	LocalDate now = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	String formatedNow = now.format(formatter);
	java.sql.Date today = java.sql.Date.valueOf(formatedNow);
	
	
	// 로그인 요소 
	private JTextField name_field = new JTextField(25);
	private JTextField phone_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);
	
	private JLabel sign_in_result = new JLabel();
	private JLabel membership_search_result = new JLabel();
	
	private JButton signInBtn = new JButton("Sign In");

	
	// 회원권 등록 요소 
	private JLabel membership_number_insert = new JLabel();
	private JTextField membership_field = new JTextField(4);
	
	private JLabel start_date_insert = new JLabel();
	private JLabel year = new JLabel("Year");
	private JLabel month = new JLabel("Month");
	private JLabel day = new JLabel("Day");
	private JTextField year_field = new JTextField(4);
	private JTextField month_field = new JTextField(2);
	private JTextField day_field = new JTextField(2);
	
	private JButton enrollBtn = new JButton("Enroll");
	private JLabel membership_enroll_result = new JLabel();


	
	public EnrollFrame() {
		setTitle("Enroll Membership");
		
		showEnrollField(false);
		
		id.clear();
		
		// 로그인 및 회원권 조회 
		signInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText();
				String phone = phone_field.getText();
				String passwd = passwd_field.getText();
				
				signIn(name, phone, passwd);
			}
		});
		
		// 회원권 등록 
		enrollBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String membership_id = membership_field.getText().trim()==null?"":membership_field.getText().trim();
				String start_date_inserted = year_field.getText() + "-" + month_field.getText() + "-" + day_field.getText();
				java.sql.Date start_date = java.sql.Date.valueOf(start_date_inserted);
				
				enrollMembership(start_date, membership_id);

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
		contentPane.add(signInBtn);
		contentPane.add(sign_in_result);
		
		contentPane.add(membership_search_result);
		contentPane.add(membership_number_insert);
		contentPane.add(membership_field);
		contentPane.add(start_date_insert);
		contentPane.add(year);
		contentPane.add(year_field);
		contentPane.add(month);
		contentPane.add(month_field);
		contentPane.add(day);
		contentPane.add(day_field);
		contentPane.add(membership_enroll_result);
		contentPane.add(new JLabel("<html><body style='text-align:center;'>----------------------------------------------------------------------------------<br/></body></html>"));
		contentPane.add(enrollBtn);
		
		setSize(350, 700);
		setVisible(true);
	}
	
	// 넘겨 받은 사용자 정보가 유효한지 판단한다.
	void signIn(String member_name, String phone, String passwd) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			
			PreparedStatement pStmt = conn.prepareStatement("select * from DB2022_members where name=? and phone=?;");
			pStmt.setString(1, member_name);
			pStmt.setString(2, phone);
			ResultSet info_set = pStmt.executeQuery();
			
			// 회원가입이 되지 않은 멤버인 경우 
			if (!info_set.next()) {
				sign_in_result.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Member does not exist.<br/>"
						+ "----------------------------------------------------------------------------------"
						+ "</body></html>");
			} else {
				
				// 비밀번호가 일치하지 않은 경우 
				if (!passwd.equals(info_set.getString("password"))) {
					sign_in_result.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Wrong password!<br/>"
							+ "----------------------------------------------------------------------------------</body></html>");
				} else {
					// 정상 로그인
					member_id = info_set.getString("member_id");

					pStmt = conn.prepareStatement("select * from DB2022_membership;\r\n");
					
					ResultSet membership_set = pStmt.executeQuery();

					sign_in_result.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "This is a list of Memberships Available.<br /> "
							+ "</body></html>");
					membership_search_result.setText("<html><body style='text-align:center;'>");
					
					while (membership_set.next()) {
						id.add(membership_set.getInt("membership_id"));
						membership_search_result.setText(membership_search_result.getText() + "[" + membership_set.getString("membership_id")+"]" + " "
								+ membership_set.getString("name") + ", Period: " + membership_set.getString("period") + " price "
								+ membership_set.getString("price") + " won<br />");

					}
					
					// 로그인 이후 화면 변환
					membership_search_result.setText(membership_search_result.getText() + "----------------------------------------------------------------------------------</body></html>");					
					membership_number_insert.setText("<html><body style='text-align:center;'>Please choose Membership number: </body></html>");
					start_date_insert.setText("<html><body style='text-align:center;'> When will you start to use this Membership? ----- </body></html>");
					
					showEnrollField(true);
					}
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
	
	void enrollMembership(java.sql.Date start_date, String membership_id) {
		// 조회되지 않는 회원권인 경우 
		if (! id.contains(Integer.parseInt(membership_id))) {
			membership_enroll_result.setText("<html><body style='text-align:center;'>"
					+ "Wrong Membership number!<br />"
					+ "Enter it again.<br/>"
					+ "----------------------------------------------------------------------------------<br/>"
					+ "</body></html>");
		}
		// 시작날짜가 등록날짜(오늘) 이전인 경우 
		else if (start_date.before(today)) {
			membership_enroll_result.setText("<html><body style='text-align:center;'>"
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
				membership_enroll_result.setText("<html><body style='text-align:center;'>"
						+ "Membership Registration Completed.<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			} catch (SQLException sqle) {
				membership_enroll_result.setText("<html><body style='text-align:center;'>"
						+ "You have already enrolled Membership.<br/>"
						+ "Go To 'MyPage' and Check your Membership.<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}
		}
	}
	
	void showEnrollField (Boolean b) {
		membership_field.setVisible(b);
		year.setVisible(b);
		month.setVisible(b);
		day.setVisible(b);
		year_field.setVisible(b);
		month_field.setVisible(b);
		day_field.setVisible(b);
		enrollBtn.setVisible(b);
	}
}
