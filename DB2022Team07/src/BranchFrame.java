import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BranchFrame extends JFrame {
   private JTextField year_field = new JTextField(25);
   private JTextField month_field = new JTextField(25);
   private JLabel profit = new JLabel();
   private JLabel member_num = new JLabel();
   private JLabel trainer_num = new JLabel();
   private JButton revenue_btn = new JButton("search");
   private JButton member_btn = new JButton("search");
   private JButton trainer_btn = new JButton("search");
   private JButton top1_btn = new JButton("search");
   private JLabel top1_label = new JLabel();

   public BranchFrame() {
      setTitle("Branch");
      profit.setText("");
      member_num.setText("");
      trainer_num.setText("");
      year_field.setText("");
      month_field.setText("");

      Container content = getContentPane();
      content.setLayout(new FlowLayout());
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* 지점별 월별 매출 조회 *<br/>" + "조회할 연도와 월을 입력하세요.<br/>"
            + "----------------------------------------------------------------------------------<br/>"
            + "</body></html>"));
      content.add(new JLabel("   연도  "));
      content.add(year_field);
      content.add(new JLabel("   월     "));
      content.add(month_field);
      content.add(revenue_btn);
      content.add(profit);
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* 지점별 회원 수 조회 *<br/>"
            + "----------------------------------------------------------------------------------<br/>"
            + "</body></html>"));
      content.add(member_btn);
      content.add(member_num);
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* 지점별 트레이너 수 조회 *<br/>"
            + "----------------------------------------------------------------------------------<br/>"
            + "</body></html>"));
      content.add(trainer_btn);
      content.add(trainer_num);
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* 지점별 회원 수 1위 트레이너 조회 *<br/>"
            + "----------------------------------------------------------------------------------<br/>"
            + "</body></html>"));
      content.add(top1_btn);
      content.add(top1_label);

      revenue_btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            Monthly_Revenue();
         }
      });
      member_btn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
                  DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
               ResultSet rset = stmt
                           .executeQuery("select branch, count(*) from DB2022_members group by branch;");
               member_num.setText("<html><body style='text-align:center;'>");
                     while (rset.next()) {
                        String str = (rset.getString("branch") + " " + rset.getInt(2)+"<br/>");
                        member_num.setText(member_num.getText()+str);
                     }
                     member_num.setText(member_num.getText()
                           + "----------------------------------------------------------------------------------<br/></body></html>");
            }catch(SQLException sqle) {
               System.out.println("SQL Exception: "+sqle);
            }
         }
         
      });
      trainer_btn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
                  DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
               ResultSet rset = stmt
                           .executeQuery("select branch, count(*) from DB2022_trainers group by branch;");
               trainer_num.setText("<html><body style='text-align:center;'>");
                     while (rset.next()) {
                        String str = (rset.getString("branch") + " " + rset.getInt(2)+"<br/>");
                        trainer_num.setText(trainer_num.getText()+str);
                     }
                     trainer_num.setText(trainer_num.getText()
                           + "----------------------------------------------------------------------------------<br/>"
                           + "</body></html>");
            }catch(SQLException sqle) {
               System.out.println("SQL Exception: "+sqle);
            }
         }

      });
      
      top1_btn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             // TODO Auto-generated method stub
             try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
                   DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
                ResultSet rset = stmt
                            .executeQuery("with A as (select trainer, DB2022_trainers.branch, DB2022_trainers.name, count(trainer) as 총회원수 from DB2022_trainers, DB2022_members "
                                  + "where DB2022_members.branch = DB2022_trainers.branch and DB2022_members.trainer = DB2022_trainers.trainer_id group by trainer), \r\n"
                                  + "B as (select branch, max(총회원수) as 트레이너별최고회원수 from A group by branch) \r\n"
                                  + "select A.branch, name, 트레이너별최고회원수 from A,B where A.branch = B.branch and 총회원수 = 트레이너별최고회원수 order by branch; \r\n"
                                  + "\r\n"
                                  + "");
                top1_label.setText("<html><body style='text-align:center;'>");
                      while (rset.next()) {
                         String str = (rset.getString("branch") + "점 " + rset.getString("name")+ " 트레이너 " + rset.getInt(3)+ "명으로 1등입니다." );
                         top1_label.setText(top1_label.getText() + str+"<br/>");
                         System.out.println(str);
                      }
                      top1_label.setText(top1_label.getText()+"----------------------------------------------------------------------------------<br/></body></html>");
             } catch(SQLException sqle) {
                System.out.println("SQL Exception: "+sqle);
             }
          }
       });

      
      setSize(350,800);
      setVisible(true);
   }

   void Monthly_Revenue() {
      String tempYear = year_field.getText().trim();
      String tempMonth = month_field.getText().trim();
      int year = -1, month = -1;
      try {
         year = Integer.parseInt(tempYear);
         month = Integer.parseInt(tempMonth);
      } catch (NumberFormatException numException) {
         profit.setText("<html><body style='text-align:center;'>"
               + "----------------------------------------------------------------------------------<br/>"
               + "잘못 입력하셨습니다.<br/>" + "정확한 연도와 월을 다시 입력하세요.<br/>"
               + "----------------------------------------------------------------------------------<br/>"
               + "</body></html>");
      }
      if (year != -1 && month != -1) {
         try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
               DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
            PreparedStatement pStmt = conn.prepareStatement(
                  "select branch, sum(price) as 'revenue' " + "from DB2022_enroll join DB2022_membership "
                        + "on DB2022_enroll.membership=DB2022_membership.membership_id "
                        + "join DB2022_members using(member_id) "
                        + "where year(enroll_date)=? and month(enroll_date)=? " + "group by branch "
                        + "order by branch;");
            pStmt.setString(1, tempYear);
            pStmt.setString(2, tempMonth);
            ResultSet rset = pStmt.executeQuery();
            if (rset.next()) {
               String info;
               profit.setText("<html><body style='text-align:center;'>"
                     + "----------------------------------------------------------------------------------<br/>");
               do {
                  info = rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원<br/>";
                  profit.setText(profit.getText() + info);
               } while (rset.next());
               profit.setText(profit.getText() + "</body></html>");
            } else {
               profit.setText("<html><body style='text-align:center;'>"
                     + "----------------------------------------------------------------------------------<br/>"
                     + "해당 연도와 월에 등록된 회원이 없습니요.<br/>"
                     + "----------------------------------------------------------------------------------<br/>"
                     + "</body></html>");
            }

         } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle);
         }
      }
   }

}
