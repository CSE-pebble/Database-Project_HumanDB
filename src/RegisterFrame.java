import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;

public class RegisterFrame extends JFrame {
	JTextField name_field = new JTextField(20);
	JTextField phone_field = new JTextField(20);
	JTextField password_field = new JTextField(20);
	JTextField gender_field = new JTextField(20);
	JTextField height_field = new JTextField(20);
	JTextField weight_field = new JTextField(20);
	JTextField branch_field = new JTextField(20);
	JLabel branch_list = new JLabel();
	JLabel label = new JLabel();
	JLabel register = new JLabel("회원가입이 안료되었습니다");
	Vector<String> branch_vec = new Vector<String>();

	public RegisterFrame() {
		setTitle("회원가입");
		Container content = getContentPane();
		content.setLayout(new FlowLayout());
		
		// 필드 값 초기화
		name_field.setText("");
		phone_field.setText("");
		password_field.setText("");
		gender_field.setText("");
		height_field.setText("");
		weight_field.setText("");
		branch_field.setText("");
		label.setText("");
		branch_list.setText("");
		branch_vec.clear();

		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* 회원 가입 *<br/>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "아래는 지점 정보입니다.<br />" + "고객님의 지점을 올바르게 입력해주세요.<br/>" + "</body></html>"));
		branch_list.setText("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>");
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			ResultSet rset = stmt.executeQuery("select name from DB2022_branches;");
			while (rset.next()) {
				branch_vec.add(rset.getString(1));
				branch_list.setText(branch_list.getText() + rset.getString(1) + "<br/>");
			}
			branch_list.setText(branch_list.getText()
					+ "----------------------------------------------------------------------------------<br/>"
					+ "</body></html>");
		} catch (SQLException sqle) {
			System.out.println("SQL Exception: " + sqle);
		}
		content.add(branch_list);
		content.add(new JLabel("      이름         "));
		content.add(name_field);

		content.add(new JLabel(" 휴대폰 번호  "));
		content.add(phone_field);

		content.add(new JLabel("    비밀번호    "));
		content.add(password_field);

		content.add(new JLabel("    성별(F/M)    "));
		content.add(gender_field);

		content.add(new JLabel("         키          "));
		content.add(height_field);

		content.add(new JLabel("      몸무게      "));
		content.add(weight_field);

		content.add(new JLabel("      지점         "));
		content.add(branch_field);

		JButton btn = new JButton("회원가입");
		content.add(btn);
		content.add(label);

		setSize(350, 800);
		setVisible(true);

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name = name_field.getText().trim();
				String phone = phone_field.getText().replaceAll("-", "").trim();
				String passwd = password_field.getText().trim();
				String gender = gender_field.getText().trim();
				String height = height_field.getText().trim();
				String weight = weight_field.getText().trim();
				String branch = branch_field.getText().trim();

				try {
					int phone_num = Integer.parseInt(phone);
				} catch (NumberFormatException numException) {
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "잘못된 전화번호 형식입니다.<br/>" + "다시 입력해주세요.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				} finally {

					if (name.isEmpty() || phone.isEmpty() || passwd.isEmpty() || gender.isEmpty() || height.isEmpty()
							|| weight.isEmpty() || branch.isEmpty()) {
						label.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "필수 정보를 입력하세요<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					} else if (passwd.length() != 4) {
						label.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "비밀번호는 4자리입니다.<br/>" + "다시 입력해주세요.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					} else if (!gender.equals("F") && !gender.equals("M")) {
						label.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "성별은 F와 M으로<br/>" + "입력해주세요.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					} else if (!branch_vec.contains(branch)) {
						label.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "잘못된 지점 이름입니다.<br/>" + "다시 입력해주세요.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					} else
						Register(name, phone, passwd, gender, height, weight, branch);
				
				}
			}
		});

	}

	void Register(String name, String phone, String passwd, String gender, String height, String weight,
			String branch) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			// 회원의 이름과 전화번호를 바탕으로 member_id 검색
			PreparedStatement pStmt = conn.prepareStatement("insert into DB2022_members values(?,?,?,?,?,?,?,?,?)");
			ResultSet rSet = stmt.executeQuery("select count(*) as num from DB2022_members;");

			if (rSet.next()) {
				int n = rSet.getInt("num") + 1;
				System.out.println(n);
				pStmt.setInt(1, n);
				pStmt.setString(2, name);
				pStmt.setString(3, gender);
				pStmt.setInt(4, Integer.parseInt(height));
				pStmt.setInt(5, Integer.parseInt(weight));
				pStmt.setString(6, phone);
				pStmt.setString(7, passwd);
				pStmt.setString(8, branch);
				pStmt.setString(9, null);
				pStmt.executeUpdate();
				label.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "회원가입이 완료되었습니다.<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}