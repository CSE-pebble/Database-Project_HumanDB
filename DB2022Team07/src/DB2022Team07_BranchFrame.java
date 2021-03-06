import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 지점 관련 GUI 
public class DB2022Team07_BranchFrame extends JFrame {
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

   public DB2022Team07_BranchFrame() {
      setTitle("Branch");
      profit.setText("");
      member_num.setText("");
      trainer_num.setText("");
      year_field.setText("");
      month_field.setText("");

      Container content = getContentPane();
      content.setLayout(new FlowLayout());
      // 지점 관련 GUI
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* Monthly Sales  per Branch *<br/>" + "Please enter the year and month.<br/>"
            + "</body></html>"));
      content.add(new JLabel("    Year     "));
      content.add(year_field);
      content.add(new JLabel("    Month   "));
      content.add(month_field);
      content.add(revenue_btn);
      content.add(profit);
      // 지점별 월별 매출 조회 GUI 추가
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* Number of Members per Branch *<br/>"
            + "</body></html>"));
      content.add(member_btn);
      content.add(member_num);
      // 지점별 트레이너 수 조회 GUI 추가
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* Number of Trainers per Branch*<br/>"
            + "</body></html>"));
      content.add(trainer_btn);
      content.add(trainer_num);
      // 지점별 회원수 1위 트레이너 조회 GUI 추가
      content.add(new JLabel("<html><body style='text-align:center;'>"
            + "----------------------------------------------------------------------------------<br/>"
            + "* TOP 1 Trainer per Branch *<br/>"
            + "</body></html>"));
      content.add(top1_btn);
      content.add(top1_label);

      // 버튼에 월별 매출 계산 이벤트 추가
      revenue_btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Monthly_Revenue(); // 월별 매출 계산하기
         }
      });
   // 버튼에 지점별 회원 수 보여주는 이벤트 추가
      member_btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
                  DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
               ResultSet rset = stmt
                           .executeQuery("select branch, count(*) from DB2022_members group by branch;");
               member_num.setText("<html><body style='text-align:center;'>");
                     while (rset.next()) {
                        String str = (rset.getString("branch") + ": " + rset.getInt(2)+"<br/>");
                        member_num.setText(member_num.getText()+str);
                     }
                     member_num.setText(member_num.getText()
                           + "----------------------------------------------------------------------------------<br/></body></html>");
            }catch(SQLException sqle) {
               System.out.println("SQL Exception: "+sqle);
            }
         }
         
      });
      
      // 버튼에 지점별 트레이너 수 보여주는 이벤트 추가
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
                        String str = (rset.getString("branch") + ": " + rset.getInt(2)+"<br/>");
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
      
      // 버튼에 지점별 누적 회원 수 1위 트레이너 보여주는 이벤트 추가
      top1_btn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
                   DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();){
                ResultSet rset = stmt.executeQuery("with A as "
                            	  + "(select trainer, DB2022_trainers.branch, DB2022_trainers.name, count(trainer) as totalMemb "
                            	  + "from DB2022_trainers, DB2022_members use index(idx_memb_trainer)"
                                  + "where DB2022_members.trainer = DB2022_trainers.trainer_id group by trainer), \r\n"
                                  + "B as "
                                  + "(select branch, max(totalMemb) as topMemb "
                                  + "from A group by branch) "
                                  + "select A.branch, name, topMemb "
                                  + "from A,B "
                                  + "where A.branch = B.branch and totalMemb = topMemb order by branch;");
                top1_label.setText("<html><body style='text-align:center;'>");
                      while (rset.next()) {
                         String str = (rset.getString("branch") + ": " + rset.getString("name")+ " trainer (" + rset.getInt(3)+ " members)" );
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

   // 지점별 월별 매출을 계산하는 함수
   void Monthly_Revenue() {
	   // 연도와 월 입력 값 가져오기
      String tempYear = year_field.getText().trim();
      String tempMonth = month_field.getText().trim();
      int year = -1, month = -1;
      try {
         year = Integer.parseInt(tempYear);
         month = Integer.parseInt(tempMonth);
      } catch (NumberFormatException numException) {
    	  // 입력 형식이 맞지 않으면 에러 메시지 띄우기
         profit.setText("<html><body style='text-align:center;'>"
               + "----------------------------------------------------------------------------------<br/>"
               + "You have entered incorrectly.<br/>" + "Please re-enter the correct year and month.<br/>"
               + "----------------------------------------------------------------------------------<br/>"
               + "</body></html>");
      }
      // 올바른 값을 입력했으면 월별 매출 계산하기
      if (year != -1 && month != -1) {
         try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID,
               DB2022Team07_main.USERID, DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
            PreparedStatement pStmt = conn.prepareStatement(
                  "select branch, sum(price) as 'revenue' " + "from DB2022_enroll use index(idx_enroll_membership)join DB2022_membership "
                        + "on DB2022_enroll.membership=DB2022_membership.membership_id "
                        + "join DB2022_members using(member_id) "
                        + "where year(enroll_date)=? and month(enroll_date)=? " + "group by branch "
                        + "order by branch;");
            pStmt.setString(1, tempYear);
            pStmt.setString(2, tempMonth);
            ResultSet rset = pStmt.executeQuery();
            // 월별 매출 보여주기
            if (rset.next()) {
               String info;
               profit.setText("<html><body style='text-align:center;'>"
                     + "----------------------------------------------------------------------------------<br/>");
               do {
                  info = rset.getString("branch") + ": " + rset.getInt("revenue") + " won<br/>";
                  profit.setText(profit.getText() + info);
               } while (rset.next());
               profit.setText(profit.getText() + "</body></html>");
            } else {
            	// 해당 날짜에 매출 정보가 없으면 정보가 없다는 메시지 띄우기
               profit.setText("<html><body style='text-align:center;'>"
                     + "----------------------------------------------------------------------------------<br/>"
                     + "There are no members registered in the year and month..<br/>"
                     + "----------------------------------------------------------------------------------<br/>"
                     + "</body></html>");
            }

         } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle);
         }
      }
   }

}
