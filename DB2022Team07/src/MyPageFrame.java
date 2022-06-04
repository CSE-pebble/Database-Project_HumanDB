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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MyPageFrame extends JFrame{
	private JTextField name_field = new JTextField(25);
	private JTextField phone_field = new JTextField(25);
	private JTextField passwd_field = new JTextField(25);
	private JLabel t1 = new JLabel();
	private JLabel t2 = new JLabel();
	private JButton bmi_menu = new JButton("BMI Calculation");
	private JButton period_menu = new JButton("Membership Expiration Date Inquiry");
	private JButton btn = new JButton("OK");
	
	String height="",weight="",end_date="";
	
	public MyPageFrame() {
		setTitle("My Page");
		height="";
		weight = "";
		end_date="";
		t1.setText("");
		bmi_menu.setVisible(false);
		period_menu.setVisible(false);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "My Page<br />"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* Hereby Certify *</body></html>"));
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
		contentPane.add(t2);
		setSize(350,500);
		setVisible(true);
		
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = name_field.getText().trim()==null?"":name_field.getText().trim();
				String phone = phone_field.getText().trim()==null?"":phone_field.getText().replaceAll("-","").trim();
				String passwd = passwd_field.getText().trim()==null?"":passwd_field.getText().trim();
				try (Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
						DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
					PreparedStatement pStmt = conn.prepareStatement(
							"select height, weight,end_date from DB2022_members join DB2022_period where DB2022_members.name=? and phone=? and password=?;");
					pStmt.setString(1, name);
					pStmt.setString(2, phone);
					pStmt.setString(3, passwd);
					ResultSet rset = pStmt.executeQuery();
					if (!rset.next()) {
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Invalid member information.<br/>" + "Please re-enter your member information.<br/>"
								+ "----------------------------------------------------------------------------------"
								+ "</body></html>");
					} else {
						height= rset.getString("height");
						weight = rset.getString("weight");
						end_date = rset.getString("end_date");
						bmi_menu.setVisible(true);
						period_menu.setVisible(true);
						t1.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "Please choose the service.<br/>"
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
		bmi_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int height_num = Integer.parseInt(height);
				int weight_num = Integer.parseInt(weight);
				
				int BMI = weight_num/((height_num/100)*(height_num/100));
				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "BMI: "+BMI+"<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
			}
			
		});
		period_menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t2.setText("<html><body style='text-align:center;'>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "Membership Expiration Date: "+end_date+"<br/>"
						+ "----------------------------------------------------------------------------------<br/>"
						+ "</body></html>");
				
			}
			
		});
	}
}
