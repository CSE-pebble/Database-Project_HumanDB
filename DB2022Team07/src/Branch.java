import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Branch extends JFrame{
	static final String DBID = "DB2022Team07";
	static final String USERID = "root";
	static final String PASSWD = "db2022team07";

    static List profitList = new List();
    static List infoList = new List();
    static JPanel panel = new JPanel();
	
	public static void Monthly_Revenue(Frame frame) {        
		panel.add(new JLabel("년도"));
		JTextField year=new JTextField(5);
		panel.add(year);
		panel.add(new JLabel("월"));
		JTextField month=new JTextField(5);
		panel.add(month);
		
        JButton showProfitBtn=new JButton("조회");
        panel.add(showProfitBtn);
        
	    showProfitBtn.addActionListener(new ActionListener() {
	    	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String tempYear=year.getText();
				System.out.println(tempYear);	

				String tempMonth=month.getText();
				System.out.println(tempMonth);	
				
				try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
						Statement stmt = conn.createStatement();){
					ResultSet rset = stmt.executeQuery("select branch, sum(price) as 'revenue'\r\n"
							+ "from DB2022_enroll join DB2022_membership \r\n"
							+ "on DB2022_enroll.membership=DB2022_membership.membership_id\r\n"
							+ "join DB2022_members using(member_id)\r\n" + "where year(enroll_date)='" + tempYear
							+ "' and month(enroll_Date)='" + tempMonth + "'\r\n" + "group by branch\r\n" + "order by branch; ");
					System.out.println(year + "년 " + month + "월의 " + "매출: ");
				
					while (rset.next()) {
						System.out.println(rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
						String str=(rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
						profitList.add(str); 
					}

				}catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle);
				}}
	    	
	    });
	    panel.add(profitList);
		
		frame.add(panel);
		
	}
		
	
	public static void Branch_Info(Frame frame) {
	      JButton showInfoBtn=new JButton("조회");
	      panel.add(showInfoBtn);
	      		      	
	      showInfoBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
						Statement stmt = conn.createStatement();){			
					/* 지점별 회원 수 */
					ResultSet rset = stmt.executeQuery("select branch, count(*) from DB2022_members group by branch;");
					while (rset.next()) {
						System.out.println("지점별 회원 수 : " + rset.getString("branch") + " " + rset.getInt(2));
						String str=("지점별 회원 수 : " + rset.getString("branch") + " " + rset.getInt(2));
						infoList.add(str); 
					}
					System.out.println("\n");
					
					/* 지점별 트레이너 수 */
					ResultSet rset2 = stmt.executeQuery("select branch, count(*) from DB2022_trainers group by branch;");
					while (rset2.next()) {
						System.out.println("지점별 트레이너 수 : " + rset2.getString("branch") + " " + rset2.getInt(2));
						String str=("지점별 트레이너 수 : " + rset2.getString("branch") + " " + rset2.getInt(2));
						infoList.add(str); 
					}
					System.out.println("\n");

				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle);
				}
				
			}
	    	  
	      });
	      panel.add(infoList);
	      
		frame.add(panel);		
	}
	
}
