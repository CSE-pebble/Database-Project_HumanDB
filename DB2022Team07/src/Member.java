import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import java.time.LocalDate;

public class Member extends JFrame {
	static JLabel BMI = new JLabel();
	static List expiredList = new List();
	static JPanel panel = new JPanel();

	static boolean expire = false;
	static boolean bmi = false;

	public static void Expired_Member(Container content) {
		if (!expire) {
			expire = true;
			panel.add(new JLabel("------회원권 만료 회원 삭제------"));
			panel.add(expiredList);
			JButton show = new JButton("삭제");
			panel.add(show);

			show.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					try (Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
							DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
						expiredList.removeAll();
						LocalDate now = LocalDate.now();

						ResultSet rset = stmt.executeQuery("select member_id, name, start_date, end_date\r\n"
								+ "from DB2022_period as period\r\n" + "where period.end_date < DATE(NOW());");

						while (rset.next()) {
							String date = rset.getString("start_date");
							String str = (rset.getString("name") + " 회원 " + date + " ~ " + rset.getString("end_date"));

							expiredList.add(str);

							PreparedStatement pStmt2 = conn
									.prepareStatement("delete from DB2022_enroll\r\n" + "where member_id="
											+ rset.getInt("member_id") + " and start_date='" + date + "';");
							// pStmt2.executeUpdate();
						}

					} catch (SQLException sqle) {
						System.out.println("SQLException: " + sqle);
					}

				}

			});
			
			panel.setPreferredSize(new DimensionUIResource(200, 400));
			content.add(panel);
			content.setVisible(true);
		}
		panel.setVisible(panel.isVisible() ? false : true);

	}

	public static void BMI_Calculator(Container content) {
		if (!bmi) {
			bmi = true;
			panel.add(new JLabel("------BMI 조회------"));
			panel.add(new JLabel("이름을 입력하세요"));
			JTextField name_field = new JTextField(10);
			panel.add(name_field);
			panel.add(new JLabel("전화번호을 입력하세요"));
			JTextField phone_field = new JTextField(10);
			panel.add(phone_field);
			panel.add(new JLabel("비밀번호를 입력하세요"));
			JTextField passwd_field = new JTextField(10);
			panel.add(passwd_field);
			JButton calc = new JButton("BMI 조회");
			panel.add(calc);

			calc.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String name = name_field.getText().trim();
					String phone = phone_field.getText().trim();
					String passwd = passwd_field.getText().trim();
					try (Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
							DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
						// 회원의 이름과 전화번호를 바탕으로 member_id 검색
						PreparedStatement pStmt = conn.prepareStatement(
								"select * from DB2022_members join DB2022_enroll using(member_id) where name=? and phone=?;");
						pStmt.setString(1, name);
						pStmt.setString(2, phone);
						ResultSet info_set = pStmt.executeQuery();
						// 회원 정보가 잘못되었거나, 회원권을 등록한 회원DB에 회원이 존재하지 않는 경우
						if (!info_set.next()) {
							BMI.setText("존재하지 않는 회원입니다.");
						} else {
							// 보안을 위해 password 확인
							if (!passwd.equals(info_set.getString("password"))) {
								BMI.setText("비밀번호가 잘못되었습니다.");
							} else {
								pStmt = conn.prepareStatement("select name, (weight/((height/100)*(height/100))) as score from DB2022_members as members where members.name=?;");
								pStmt.setString(1, name);
								ResultSet rset = pStmt.executeQuery();
								rset.next();
								BMI.setText("<html><body style='text-align:center;'>BMI 점수는<br /> " + rset.getString("score")+ "입니다.<br /></body></html>");
							}

						}
					} catch (SQLException sqle) {
						System.out.println("SQLException: " + sqle);
					}
				}
			});

			panel.add(BMI);

			content.add(panel);
			content.setVisible(true);
		}
	}
}