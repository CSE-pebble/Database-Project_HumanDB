import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class MyPageFrame extends JFrame{
   private JTextField name_field = new JTextField(25);
   private JTextField phone_field = new JTextField(25);
   private JTextField passwd_field = new JTextField(25);
   
   private JTextField edit_passwd = new JTextField(5);
   private JTextField edit_height = new JTextField(5);
   private JTextField edit_weight = new JTextField(5);
   
   private JLabel t1 = new JLabel();
   private JLabel t2 = new JLabel();
   private JLabel complete_label=new JLabel();
   private JButton bmi_menu = new JButton("BMI Calculation");
   private JButton period_menu = new JButton("Membership Expiration Date Inquiry");
   private JButton edit_menu = new JButton("Edit Member Information");
   private JButton btn = new JButton("OK");
   private JButton edit_btn = new JButton("OK");
   
   private JLabel edit_label1 = new JLabel("password");
   private JLabel edit_label2 = new JLabel(" height");
   private JLabel edit_label3 = new JLabel(" weight");
   
   private JPanel edit_panel=new JPanel();
   
   String height="",weight="",end_date="", password="";
   String phone, passwd;
   
   public MyPageFrame() {
      setTitle("My Page");
      height="";
      weight = "";
      phone="";
      end_date="";
      t1.setText("");
      bmi_menu.setVisible(false);
      period_menu.setVisible(false);
      edit_menu.setVisible(false);
      
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
      
      setSize(350,700);
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
            String name = name_field.getText().trim()==null?"":name_field.getText().trim();
            phone = phone_field.getText().trim()==null?"":phone_field.getText().replaceAll("-","").trim();
            passwd = passwd_field.getText().trim()==null?"":passwd_field.getText().trim();
            try (Connection conn = DriverManager.getConnection(
                  "jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
                  DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
               PreparedStatement pStmt = conn.prepareStatement(
                     "select height, weight, phone, end_date from DB2022_members join DB2022_period where DB2022_members.name=? and phone=? and password=?;");
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
                  phone = rset.getString("phone");
                  end_date = rset.getString("end_date");
                  bmi_menu.setVisible(true);
                  period_menu.setVisible(true);
                  edit_menu.setVisible(true);
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
            edit_panel.setVisible(false);
            edit_btn.setVisible(false);
            complete_label.setVisible(false);


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
            edit_panel.setVisible(false);
            edit_btn.setVisible(false);
            complete_label.setVisible(false);


            t2.setText("<html><body style='text-align:center;'>"
                  + "----------------------------------------------------------------------------------<br/>"
                  + "Membership Expiration Date: "+end_date+"<br/>"
                  + "----------------------------------------------------------------------------------<br/>"
                  + "</body></html>");
         }
         
      });
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
            edit_weight.setText(weight);
                        
            edit_btn.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  String new_passwd = edit_passwd.getText().trim();
                  String new_height = edit_height.getText().trim();
                  String new_weight = edit_weight.getText().trim();
                  
                  if (new_passwd.length() != 4) {
                     complete_label.setText("<html><body style='text-align:center;'>"
                           + "----------------------------------------------------------------------------------<br/>"
                           + "Password should be 4 digits long.<br/>"
                           + "----------------------------------------------------------------------------------<br/>"
                           + "</body></html>");
                  }
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