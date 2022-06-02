import java.sql.*;
import java.time.LocalDate;

public class Member {
		   public static void Expired_Member(Connection conn, Statement stmt)
		   {
		      try{
		         LocalDate now = LocalDate.now();
		         /*PreparedStatement pStmt=conn.prepareStatement(
		        		 "create view DB2022_period as\r\n"
		        		 + "select enroll.member_id, members.name, start_date, date_add(enroll.start_date, interval membership.period day) as end_date\r\n"
			             + "from DB2022_enroll as enroll, DB2022_membership as membership, DB2022_members as members\r\n"
		        		 + "where enroll.member_id=members.member_id and enroll.membership=membership.membership_id;"
		        		 );
		         pStmt.executeUpdate();*/
		         
		         ResultSet rset = stmt.executeQuery(
		               "select member_id, name, start_date, end_date\r\n"
		               + "from DB2022_period as period\r\n"
		               + "where period.end_date < DATE(NOW());"
		               );
		         

		         System.out.println(now+" 만료 회원 리스트");
		         while(rset.next()) {
		        	String date=rset.getString("start_date");
		            System.out.println(rset.getString("name")+" 회원 "+date+" ~ "+rset.getString("end_date"));
			         PreparedStatement pStmt2=conn.prepareStatement(
				    		 "delete from DB2022_enroll\r\n"
				    		 + "where member_id="+rset.getInt("member_id")+" and start_date='"+date+"';"
				    		 );
				     pStmt2.executeUpdate();
		         }
		         
		      }catch(SQLException sqle) {
		         System.out.println("SQLException: "+sqle);
		      }

		   }
		   public static void BMI_Calculator(Connection conn, Statement stmt, String member_name)
		   {
		      try{
		         ResultSet rset = stmt.executeQuery(
		               "select name, (weight/((height/100)*(height/100))) as score\r\n"
		            		   +"from DB2022_members as members\r\n"
		            		   +"where members.name='"+member_name+"';"
		               );
		         System.out.println("회원별 BMI 점수");
		         while(rset.next()) {
		            System.out.println(rset.getString("name")+" 회원 점수 "+rset.getString("score"));
		         }
		         
		      }catch(SQLException sqle) {
		         System.out.println("SQLException: "+sqle);
		      }

		   }
	
}
