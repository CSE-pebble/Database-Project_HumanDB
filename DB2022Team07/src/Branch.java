import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class Branch extends JFrame {
   static List profitList = new List();
   static List infoList = new List();
   static JPanel panel = new JPanel();

   static boolean flag = false;
   static boolean flag2 = false;

   public static void Monthly_Revenue(Container content) {
	  JTextField year = new JTextField(10);
	  JTextField month = new JTextField(10);
      if (!flag) {
         flag = true;
         panel.add(new JLabel("------월별 매출 조회------"));
         panel.add(new JLabel("년도"));
         panel.add(year);
         panel.add(new JLabel("월"));
         panel.add(month);
         panel.add(profitList);
         JButton showProfitBtn = new JButton("조회");
         panel.add(showProfitBtn);

         showProfitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               String tempYear = year.getText().trim();
               String tempMonth = month.getText().trim();

               try (Connection conn = DriverManager.getConnection(
                           "jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
                           DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
            	  profitList.removeAll();
                  ResultSet rset = stmt.executeQuery("select branch, sum(price) as 'revenue'\r\n"
                        + "from DB2022_enroll join DB2022_membership \r\n"
                        + "on DB2022_enroll.membership=DB2022_membership.membership_id\r\n"
                        + "join DB2022_members using(member_id)\r\n" + "where year(enroll_date)='" + tempYear
                        + "' and month(enroll_Date)='" + tempMonth + "'\r\n" + "group by branch\r\n"
                        + "order by branch; ");
                  System.out.println(year + "년 " + month + "월의 " + "매출: ");

                  while (rset.next()) {
                     System.out.println(rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
                     String str = (rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
                     profitList.add(str);
                  }

               } catch (SQLException sqle) {
                  System.out.println("SQLException: " + sqle);
               }
            }

         });

         panel.setPreferredSize(new DimensionUIResource(150, 700));
         content.add(panel);
         content.setVisible(true);
      }
      else {
    	  year.setText("");
    	  month.setText("");
    	  profitList.removeAll();
    	  infoList.removeAll();
      }
      panel.setVisible(panel.isVisible() ? false : true);
   }

   public static void Branch_Info(Container content) {
      if (!flag2) {
    	 flag2 = true;
         panel.add(new JLabel("------지점별 정보 조회------"));
         panel.add(infoList);
         JButton showInfoBtn = new JButton("조회");
         panel.add(showInfoBtn);

         showInfoBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               try (Connection conn = DriverManager.getConnection(
                           "jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
                           DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
            	   infoList.removeAll();
                  /* 지점별 회원 수 */
                  ResultSet rset = stmt
                        .executeQuery("select branch, count(*) from DB2022_members group by branch;");
                  infoList.add("<지점별 회원 수>");
                  while (rset.next()) {
                     String str = (rset.getString("branch") + " " + rset.getInt(2));
                     infoList.add(str);
                  }

                  /* 지점별 트레이너 수 */
                  infoList.add("<지점별 트레이너 수>");
                  ResultSet rset2 = stmt
                        .executeQuery("select branch, count(*) from DB2022_trainers group by branch;");
                  while (rset2.next()) {
                     String str = (rset2.getString("branch") + " " + rset2.getInt(2));
                     infoList.add(str);
                  }

               } catch (SQLException sqle) {
                  System.out.println("SQLException: " + sqle);
               }

            }

         });
         content.add(panel);
         content.setVisible(true);
      }

   }

}