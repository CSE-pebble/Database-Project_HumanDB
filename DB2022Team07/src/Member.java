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
			panel.add(new JLabel("------ȸ���� ���� ȸ�� ����------"));
			panel.add(expiredList);
			JButton show = new JButton("����");
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
							String str = (rset.getString("name") + " ȸ�� " + date + " ~ " + rset.getString("end_date"));

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
			panel.add(new JLabel("------BMI ��ȸ------"));
			panel.add(new JLabel("�̸��� �Է��ϼ���"));
			JTextField name_field = new JTextField(10);
			panel.add(name_field);
			panel.add(new JLabel("��ȭ��ȣ�� �Է��ϼ���"));
			JTextField phone_field = new JTextField(10);
			panel.add(phone_field);
			panel.add(new JLabel("��й�ȣ�� �Է��ϼ���"));
			JTextField passwd_field = new JTextField(10);
			panel.add(passwd_field);
			JButton calc = new JButton("BMI ��ȸ");
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
						// ȸ���� �̸��� ��ȭ��ȣ�� �������� member_id �˻�
						PreparedStatement pStmt = conn.prepareStatement(
								"select * from DB2022_members join DB2022_enroll using(member_id) where name=? and phone=?;");
						pStmt.setString(1, name);
						pStmt.setString(2, phone);
						ResultSet info_set = pStmt.executeQuery();
						// ȸ�� ������ �߸��Ǿ��ų�, ȸ������ ����� ȸ��DB�� ȸ���� �������� �ʴ� ���
						if (!info_set.next()) {
							BMI.setText("�������� �ʴ� ȸ���Դϴ�.");
						} else {
							// ������ ���� password Ȯ��
							if (!passwd.equals(info_set.getString("password"))) {
								BMI.setText("��й�ȣ�� �߸��Ǿ����ϴ�.");
							} else {
								pStmt = conn.prepareStatement("select name, (weight/((height/100)*(height/100))) as score from DB2022_members as members where members.name=?;");
								pStmt.setString(1, name);
								ResultSet rset = pStmt.executeQuery();
								rset.next();
								BMI.setText("<html><body style='text-align:center;'>BMI ������<br /> " + rset.getString("score")+ "�Դϴ�.<br /></body></html>");
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