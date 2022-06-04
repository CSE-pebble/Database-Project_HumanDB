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
	JLabel register = new JLabel("Register completed");
	Vector<String> branch_vec = new Vector<String>();

	// �쉶�썝媛��엯 GUI瑜� �깮�꽦�븳�떎.
	// �엯�젰媛믪씠 �쑀�슚�븳吏� �뙋�떒�븯怨� �쉶�썝媛��엯�쓣 吏꾪뻾�븳�떎.
	public RegisterFrame() {
		setTitle("Sign Up");
		Container content = getContentPane();
		content.setLayout(new FlowLayout());
		
		// �븘�뱶 媛� 珥덇린�솕
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
				+ "* Sign Up *<br/>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "Here is branch name info.<br />" + "Please enter a branch from the list below. <br/>" + "</body></html>"));
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
		content.add(new JLabel("       Name        "));
		content.add(name_field);

		content.add(new JLabel("       Phone       "));
		content.add(phone_field);

		content.add(new JLabel("     Password    "));
		content.add(password_field);

		content.add(new JLabel("   Gender(F/M) "));
		content.add(gender_field);

		content.add(new JLabel("         Height      "));
		content.add(height_field);

		content.add(new JLabel("        Weight      "));
		content.add(weight_field);

		content.add(new JLabel("       Branch      "));
		content.add(branch_field);

		JButton btn = new JButton("Sign Up");
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
				
				//�쟾�솕踰덊샇 �쑀�슚�꽦 寃��궗
				boolean phone_flag=true;
				
				if(phone.length()!=11) phone_flag=false;
				else {
					for(int i=0; i<phone.length(); i++) {
						char tmp=phone.charAt(i);
						if (!Character.isDigit(tmp)) phone_flag=false;
					}
				}
				
				// �쉶�썝 媛��엯 �떆 湲곗엯�븳 �젙蹂� �옒紐삳릺硫� �뿉�윭 硫붿떆吏� �쓣�슦湲�

				// 鍮� 媛믪쓣 �꽆湲곕㈃ �뿉�윭 硫붿떆吏� �쓣�슦湲�
				if (name.isEmpty() || phone.isEmpty() || passwd.isEmpty() || gender.isEmpty() || height.isEmpty()
						|| weight.isEmpty() || branch.isEmpty()) {
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Please enter required information<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				} else if (!phone_flag) {
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Invalid phone number.<br/>" + "Please enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");	
				} else if (passwd.length() != 4) {
					// 鍮꾨�踰덊샇媛� 4�옄由ш� �븘�땲硫� �뿉�윭 硫붿떆吏� �쓣�슦湲�
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "The password is four digits.<br/>" + "Please enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				} else if (!gender.equals("F") && !gender.equals("M")) {
					// �꽦蹂꾩씠 F/M �쇅�쓽 媛믪씠硫� �뿉�윭 硫붿떆吏� �쓣�슦湲�
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Gender must be either F or M.<br/>" + "Please enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				} else if (!branch_vec.contains(branch)) {
					// �뾾�뒗 吏��젏�쓣 �엯�젰�븯硫� �뿉�윭 硫붿떆吏� �쓣�슦湲�
					label.setText("<html><body style='text-align:center;'>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "Invalid branch name.<br/>" + "Please enter it again.<br/>"
							+ "----------------------------------------------------------------------------------<br/>"
							+ "</body></html>");
				} else
					Register(name, phone, passwd, gender, height, weight, branch);
				
			}
		});

	}

	// �쑀�슚�븳 �궗�슜�옄 �젙蹂대�� DB�뿉 異붽��븯怨� �쉶�썝媛��엯�쓣 �셿猷뚰븳�떎.
	void Register(String name, String phone, String passwd, String gender, String height, String weight,
			String branch) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
				DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
			// member table�뿉 tuple瑜� �븯�굹 �궫�엯�븳�떎
			PreparedStatement pStmt = conn.prepareStatement("insert into DB2022_members values(?,?,?,?,?,?,?,?,?)");
			ResultSet rSet = stmt.executeQuery("select count(*) as num from DB2022_members;");

			if (rSet.next()) {
				int n = rSet.getInt("num") + 1; // 珥� �쉶�썝 �닔�뿉 1�쓣 �뜑�븯�뿬 member_id濡� 吏��젙�븳�떎.
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
				// �쉶�썝媛��엯 �셿猷� 硫붿떆吏� �쓣�슦湲�
				label.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Your registration is completed<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
