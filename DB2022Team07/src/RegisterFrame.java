import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RegisterFrame extends JFrame{
	static JTextField name_field = new JTextField(25);
	static JTextField phone_field = new JTextField(22);
	static JTextField password_field = new JTextField(22);
	static JTextField gender_field = new JTextField(7);
	static JTextField height_field = new JTextField(7);
	static JTextField weight_field = new JTextField(7);
	static JTextField branch_field = new JTextField(22);
	static JLabel label = new JLabel("필수 정보를 입력하세요");
	static JLabel register = new JLabel("회원가입이 안료되었습니다");

	public RegisterFrame() {
		setTitle("회원가입");
		Container content = getContentPane();
		content.setLayout(new FlowLayout());

		name_field.setText("");
		phone_field.setText("");
		password_field.setText("");
		gender_field.setText("");
		height_field.setText("");
		weight_field.setText("");
		branch_field.setText("");

		content.add(label);
		label.setVisible(false);

		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* 회원가입*<br />"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "</body></html>"));

		content.add(new JLabel("이름"));
		content.add(name_field);

		content.add(new JLabel("전화번호"));
		content.add(phone_field);

		content.add(new JLabel("4자리 비밀번호"));
		content.add(password_field);

		content.add(new JLabel("성별"));
		content.add(gender_field);

		content.add(new JLabel("키"));
		content.add(height_field);

		content.add(new JLabel("몸무게"));
		content.add(weight_field);

		content.add(new JLabel("지점"));
		content.add(branch_field);

		JButton btn = new JButton("회원가입");
		content.add(btn);

		content.add(register);
		register.setVisible(false);

		setSize(350, 500);
		setVisible(true);

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name = name_field.getText().trim();
				String phone = phone_field.getText().trim();
				String passwd = password_field.getText().trim();
				String gender = gender_field.getText().trim();
				String height = height_field.getText().trim();
				String weight = weight_field.getText().trim();
				String branch = branch_field.getText().trim();

				if(!name.isEmpty() && !phone.isEmpty() && !passwd.isEmpty() && !gender.isEmpty() && !height.isEmpty() && !weight.isEmpty() && !branch.isEmpty()) {
					label.setVisible(false);
					Register(name, phone, passwd, gender, height, weight, branch);
				}
				else
					label.setVisible(true);
			}
		});

	}

	static void Register(String name, String phone, String passwd, String gender, String height, String weight, String branch) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			// 회원의 이름과 전화번호를 바탕으로 member_id 검색
			PreparedStatement pStmt = conn.prepareStatement("insert into DB2022_members values(?,?,?,?,?,?,?,?,?)");
			ResultSet rSet=stmt.executeQuery("select count(*) as num from DB2022_members;");

			if(rSet.next()) {
				int n=rSet.getInt("num")+1;
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
				register.setVisible(true);
				System.out.println("회원가입이 완료되었습니다.");
			}


		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}