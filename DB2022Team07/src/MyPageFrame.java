import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;

public class MyPageFrame extends JFrame {
	private JTextField name_field = new JTextField(25);
	private JTextField phone_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);

	private JTextField edit_passwd = new JTextField(5);
	private JTextField edit_height = new JTextField(5);
	private JTextField edit_weight = new JTextField(5);

	private JLabel t1 = new JLabel();
	private JLabel t2 = new JLabel();
	private JLabel complete_label = new JLabel();
	private JButton bmi_menu = new JButton("BMI Calculation"); // BMI 怨꾩궛 踰꾪듉
	private JButton period_menu = new JButton("Membership Expiration Date Inquiry"); // �쉶�썝沅� 留뚮즺 �궇吏� 議고쉶 踰꾪듉
	private JButton edit_menu = new JButton("Edit Member Information");
	private JButton btn = new JButton("OK"); // �궗�슜�옄 �젙蹂� �젣異� 踰꾪듉
	private JButton edit_btn = new JButton("OK");

	private JLabel edit_label1 = new JLabel("password");
	private JLabel edit_label2 = new JLabel(" height");
	private JLabel edit_label3 = new JLabel(" weight");

	private JPanel edit_panel = new JPanel();
	Vector<String> end_date_list = new Vector<>();
	String height = "", weight = "", password = "";
	String phone, passwd;

	public MyPageFrame() {
		setTitle("My Page");
		height = "";
		weight = "";
		phone = "";
		end_date_list.clear();
		t1.setText("");
		bmi_menu.setVisible(false);
		period_menu.setVisible(false);
		edit_menu.setVisible(false);

		// Container�뿉 �븘�뱶 異붽�
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "My Page<br />"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Identity Verification *</body></html>"));
		contentPane.add(new JLabel("   name    "));
		contentPane.add(name_field);
		contentPane.add(new JLabel("   phone   "));
		contentPane.add(phone_field);
		contentPane.add(new JLabel("password"));
		contentPane.add(passwd_field);
		contentPane.add(btn);
		contentPane.add(t1);
		contentPane.add(bmi_menu);
		contentPane.add(period_menu);
		contentPane.add(edit_menu);
		contentPane.add(t2);

		edit_panel.setLayout(new FlowLayout());
		edit_panel.add(edit_label1);
		edit_panel.add(edit_passwd);
		edit_panel.add(edit_label2);
		edit_panel.add(edit_height);
		edit_panel.add(edit_label3);
		edit_panel.add(edit_weight);

		edit_panel.setVisible(false);

		contentPane.add(edit_panel);
		contentPane.add(edit_btn);
		contentPane.add(complete_label);

		edit_btn.setVisible(false);
		complete_label.setVisible(false);

		setSize(350, 700);
		setVisible(true);

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bmi_menu.setVisible(false);
				period_menu.setVisible(false);
				edit_menu.setVisible(false);
				t2.setText("");
				edit_panel.setVisible(false);
				edit_btn.setVisible(false);
				String name = name_field.getText().trim() == null ? "" : name_field.getText().trim();
				phone = phone_field.getText().trim() == null ? "" : phone_field.getText().replaceAll("-", "").trim();
				passwd = passwd_field.getText().trim() == null ? "" : passwd_field.getText().trim();
				try (Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
						DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
					// �엯�젰�맂 �궗�슜�옄 �젙蹂대�� 諛뷀깢�쑝濡� �쉶�썝 �젙蹂� 遺덈윭�삤湲�
					PreparedStatement pStmt = conn.prepareStatement(
							"select membership, height, weight, phone, end_date "
							+ "from DB2022_members join DB2022_period using(member_id) "
							+ "where DB2022_members.name=? and phone=? and password=?;");
					pStmt.setString(1, name);
					pStmt.setString(2, phone);
					pStmt.setString(3, passwd);
					ResultSet rset = pStmt.executeQuery();

					// �빐�떦 �쉶�썝�씠 議댁옱�븯吏� �븡�쑝硫� �뿉�윭 硫붿떆吏� �쓣�슦湲�
					if (!rset.next()) {
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Invalid member information.<br/>" + "Please re-enter your member information.<br/>"
								+ "----------------------------------------------------------------------------------"
								+ "</body></html>");
					} else {
						// 議댁옱�븯�뒗 �쉶�썝�씠硫� �궎, 紐몃Т寃�, 留뚮즺 �궇吏� �젙蹂� 蹂댁뿬二쇨린
						height = rset.getString("height");
						weight = rset.getString("weight");
						phone = rset.getString("phone");
						do {
							end_date_list.add(rset.getString("membership")+": "+rset.getString("end_date"));
						} while (rset.next());
						bmi_menu.setVisible(true);
						period_menu.setVisible(true);
						edit_menu.setVisible(true);
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Please choose the service<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
						bmi_menu.setVisible(true);
						period_menu.setVisible(true);
					}

				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle);
				}
			}
		});

		// BMI 怨꾩궛 踰꾪듉 �겢由� �떆, BMI 怨꾩궛�븯�뒗 �씠踰ㅽ듃 異붽�
		bmi_menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				edit_panel.setVisible(false);
				edit_btn.setVisible(false);
				complete_label.setVisible(false);

				int height_num = Integer.parseInt(height);
				int weight_num = Integer.parseInt(weight);

				int BMI = weight_num / ((height_num / 100) * (height_num / 100));
				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "BMI: " + BMI + "<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}

		});

		// �쉶�썝沅� 留뚮즺 �궇吏� 議고쉶 踰꾪듉 �겢由� �떆, 留뚮즺 �궇吏� 蹂댁뿬二쇰뒗 �씠踰ㅽ듃 異붽�
		period_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				edit_panel.setVisible(false);
				edit_btn.setVisible(false);
				complete_label.setVisible(false);

				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "[ Membership Expiration Date ]<br/>");
				while(!end_date_list.isEmpty()) {
					t2.setText(t2.getText()+end_date_list.lastElement()+"<br/>");
					end_date_list.remove(end_date_list.size()-1);
				}
				t2.setText(t2.getText()
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}

		});
		// �쉶�썝 �젙蹂� �닔�젙 �씠踰ㅽ듃 泥섎━
		edit_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Edit Member Information<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");

				edit_panel.setVisible(true);
				edit_btn.setVisible(true);
				complete_label.setVisible(true);

				edit_passwd.setText(passwd);
				edit_height.setText(height);
				edit_weight.setText(weight); // �븘�뱶�뿉 �엯�젰�븳 媛� 諛쏆븘�� ���옣

				// �겢由� �떆 �쉶�썝 �젙蹂� �뾽�뜲�씠�듃
				edit_btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String new_passwd = edit_passwd.getText().trim();
						String new_height = edit_height.getText().trim();
						String new_weight = edit_weight.getText().trim();

						// 鍮꾨�踰덊샇 4�옄由� �븘�땲硫� �삤瑜� 硫붿꽭吏� 異쒕젰, �뾽�뜲�씠�듃 X
						if (new_passwd.length() != 4) {
							complete_label.setText("<html><body style='text-align:center;'>"
									+ "----------------------------------------------------------------------------------<br/>"
									+ "Password should be 4 digits long.<br/>"
									+ "----------------------------------------------------------------------------------<br/>"
									+ "</body></html>");
						}
						// 鍮꾨�踰덊샇 4�옄由ъ씠硫� �젙蹂� �뾽�뜲�씠�듃
						else {
							try (Connection conn = DriverManager.getConnection(
									"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
									DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
								PreparedStatement pStmt = conn.prepareStatement(
										"update DB2022_members set password=?, height=?, weight=? where phone=?;");
								pStmt.setString(1, new_passwd);
								pStmt.setString(2, new_height);
								pStmt.setString(3, new_weight);
								pStmt.setString(4, phone);
								pStmt.executeUpdate();

							} catch (SQLException sqle) {
								System.out.println("SQLException: " + sqle);
							}
							// �뾽�뜲�씠�듃 �셿猷� �썑 �꽦怨� 硫붿꽭吏� 異쒕젰
							complete_label.setText("<html><body style='text-align:center;'>"
									+ "----------------------------------------------------------------------------------<br/>"
									+ "Edit Completed.<br />"
									+ "----------------------------------------------------------------------------------<br/>"
									+ "</body></html>");
						}

					}

				});

			}

		});
	}
}
