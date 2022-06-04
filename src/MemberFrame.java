import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.time.LocalDate;

public class MemberFrame extends JFrame{
   static final String DBID = "db2022team07";
   static final String USERID = "db2022team07";
   static final String PASSWD = "db2022team07";
   static final String URL = "jdbc:mysql://localhost:3306/" + DBID;
   
    List BMI = new List();
    List expiredList = new List(); // 만료된 회원 목록
    JFrame frame = new JFrame(); // Member frame 생성
    
    // 제목, 본문 폰트 설정
	Font titleFont = new Font("맑은 고딕", Font.PLAIN, 24);
	Font bodyFont = new Font("맑은 고딕", Font.PLAIN, 15);
	
	 public MemberFrame() {
 		Expired_Member(); // Expired member panel 생성
         BMI_Calculator(); // BMI calculator panel 생성
         
         frame.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent windowEvent) {
                 System.exit(0);
             }
         });
        frame.setTitle("회원 정보");
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.setSize(550, 200);
 		frame.setLayout(new GridLayout(1, 2));
 		frame.setVisible(true);
 	}
	
    // 만료된 회원 패널 생성
      public void Expired_Member() {
    	// 제목
  		JLabel titleLb = new JLabel("만료된 회원 목록");
  		titleLb.setFont(titleFont);
  		
  		// 조회 버튼
		JButton showBtn = new JButton("조회");
		showBtn.setSize(50, 30);
		showBtn.setFont(bodyFont);
		
		// 조회 이벤트 추가
  		showBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try(Connection conn1 = DriverManager.getConnection(URL, USERID, PASSWD);
                  Statement stmt1 = conn1.createStatement();){
                  LocalDate now = LocalDate.now(); 
                  ResultSet rset = stmt1.executeQuery(
                        "select member_id, name, start_date, end_date\r\n"
                        + "from DB2022_period as period\r\n"
                        + "where period.end_date < DATE(NOW());"
                        );
                  
                  System.out.println(now + " 만료 회원 리스트");
                              
                  while(rset.next()) {
                     String date = rset.getString("start_date");
                     System.out.println(rset.getString("name")+" 회원 "+ date +" ~ "+ rset.getString("end_date"));
                     String str=(rset.getString("name")+" 회원 "+date+" ~ "+rset.getString("end_date"));
                     
                     expiredList.add(str);
                     
                     PreparedStatement pStmt2 = conn1.prepareStatement(
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
  		
			// title panel 생성
			JPanel titlePn = new JPanel();
	  		titlePn.setSize(150, 30);
	  		titlePn.add(titleLb);
	  		titlePn.add(showBtn);
	  		
	  		// list panel 생성
	  		JPanel listPn = new JPanel();
	  		listPn.setLayout(new GridLayout(1,1));
	  		listPn.setSize(150, 150);
	  		listPn.add(expiredList);
	  		
	  		JPanel expiredPn = new JPanel(new BorderLayout());
	  		expiredPn.setSize(250, 200);
	  		expiredPn.add(titlePn, BorderLayout.NORTH);
	  		expiredPn.add(listPn, BorderLayout.CENTER);
	  		
	  		// frame에 expired panel 추가
	        frame.add(expiredPn);
        }
      
      // BMI 정보 패널 생성
        public void BMI_Calculator()
        {              
        	// 제목
      		JLabel titleLb = new JLabel("BMI 정보 조회");
      		titleLb.setFont(titleFont);
      		
      		// title panel 생성
			JPanel titlePn = new JPanel();
	  		titlePn.add(titleLb);
      		
	         // 입력 필드 생성
	         JLabel nameLb = new JLabel("이름:");
	         nameLb.setFont(bodyFont);
	         
	         JTextField nameTf = new JTextField(10);
	         nameTf.setFont(bodyFont);
	      		
	         // 입력 버튼
    		JButton inputBtn = new JButton("입력");
    		inputBtn.setSize(100, 10);
    		inputBtn.setFont(bodyFont);
    		
    		// 입력 버튼 이벤트 추가
    		inputBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   String temp = nameTf.getText();
                   System.out.println(temp);   
                   try(Connection conn = DriverManager.getConnection(URL, USERID, PASSWD);
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
    		
		// 필드 패널 생성
		JPanel fieldPn = new JPanel();
		fieldPn.setLayout(new FlowLayout());
		fieldPn.add(nameLb);
		fieldPn.add(nameTf);
		fieldPn.add(inputBtn);
      		
         // 목록 패널 생성
         JPanel listPn = new JPanel();
         listPn.setLayout(new GridLayout(1, 1));
         listPn.add(BMI);
         
         // BMI 패널 생성
         JPanel BMIPn = new JPanel();
         BMIPn.setLayout(new GridLayout(3,1));
         BMIPn.add(titlePn);
         BMIPn.add(fieldPn);
         BMIPn.add(listPn);
                        
         frame.add(BMIPn);
        }

      public static void main(String args[]){
    	  new MemberFrame();
      }
}