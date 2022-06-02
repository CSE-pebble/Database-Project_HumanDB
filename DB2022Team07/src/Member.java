import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.time.LocalDate;

public class Member extends JFrame{
	static final String DBID = "DB2022Team07";
	static final String USERID = "root";
	static final String PASSWD = "db2022team07";
	
    static List BMI = new List();
    static List expiredList = new List();
    static JPanel panel = new JPanel();
    
		public static void Expired_Member(Frame frame){			
		    JButton show=new JButton("조회");
		    panel.add(show);
	        
		    show.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					try(Connection conn1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
						Statement stmt1 = conn1.createStatement();){
						LocalDate now = LocalDate.now();
			         
						ResultSet rset = stmt1.executeQuery(
			               "select member_id, name, start_date, end_date\r\n"
			               + "from DB2022_period as period\r\n"
			               + "where period.end_date < DATE(NOW());"
			               );
			         
						System.out.println(now+" 만료 회원 리스트");
										
						while(rset.next()) {
							String date=rset.getString("start_date");
							System.out.println(rset.getString("name")+" 회원 "+date+" ~ "+rset.getString("end_date"));
							String str=(rset.getString("name")+" 회원 "+date+" ~ "+rset.getString("end_date"));
							
							expiredList.add(str);
							
							PreparedStatement pStmt2=conn1.prepareStatement(
					    		 "delete from DB2022_enroll\r\n"
					    		 + "where member_id="+rset.getInt("member_id")+" and start_date='"+date+"';"
					    		 );
							//pStmt2.executeUpdate();
						}
							         
				      	}catch(SQLException sqle) {
				      		System.out.println("SQLException: "+sqle);
				      	}

				}
		    	
		    });
		    
			panel.add(expiredList);
			
			frame.add(panel);
			frame.setVisible(true);

		  }
		  public static void BMI_Calculator(Frame frame)
		  {		        
			  panel.add(new JLabel("이름을 입력하세요"));
			  JTextField name=new JTextField(10);
		      panel.add(name);
		      JButton calc=new JButton("조회");
		      panel.add(calc);
		      		      	
		      calc.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String temp=name.getText();
					System.out.println(temp);	
					try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
							Statement stmt = conn.createStatement();){
							ResultSet rset = stmt.executeQuery(
				              "select name, (weight/((height/100)*(height/100))) as score\r\n"
				           		   +"from DB2022_members as members\r\n"
				           		   +"where members.name='"+temp+"';"
				              );
					        System.out.println("회원별 BMI 점수");
					        while(rset.next()) {
					           System.out.println(rset.getString("name")+" 회원 점수 "+rset.getString("score"));
					           String str=(rset.getString("name")+" 회원 점수 "+rset.getString("score"));
					           BMI.add(str); 
					        }
							         
				     }catch(SQLException sqle) {
					        System.out.println("SQLException: "+sqle);
				     }}
		      });
			  		        
			panel.add(BMI);
			
			frame.add(panel);
			frame.setVisible(true);
		  }
}

