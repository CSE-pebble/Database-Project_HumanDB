import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
   static List profitList = new List();
   static List infoList = new List();
   static JPanel panel = new JPanel();
   static Container content;

   static boolean month = false;
   static boolean info = false;

   public MainFrame() {
      setTitle("Ewha Gym");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JButton branchBtn = new JButton("지점");
      JButton registerBtn = new JButton ("회원가입");
      JButton memberBtn = new JButton("마이 페이지");
      JButton trainerBtn = new JButton("트레이너 등록 / 변경");
      JButton employeeBtn = new JButton("직원 페이지");
      branchBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
        	 new BranchFrame();
         }
      });
      registerBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new RegisterFrame();
		}
    	  
      });
      memberBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Member.Expired_Member(content);
            Member.BMI_Calculator(content);
         }

      });
      trainerBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
           new TrainerFrame();
         }
      });
      employeeBtn.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new EmployeeFrame();
		}
    	  

      });
      content = getContentPane();
      content.setLayout(new FlowLayout());
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "안녕하세요!<br/>"
      		+ "Ewha Gym입니다.<br/>"
      		+ "원하시는 서비스를 선택해주세요.<br/>"
      		+ "</body></html>"));
      content.add(new JLabel("<html><body style='text-align:center;'>"
        		+ "----------------------------------------------------------------------------------<br/>"
        		+ "* 지점별 정보 조회 *<br/>"
        		+ "</body></html>"));
      content.add(branchBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* 회원 서비스 *<br/>"
      		+ "</body></html>"));
      content.add(registerBtn);
      content.add(memberBtn);
      content.add(trainerBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* 직원 서비스 *<br/>"
      		+ "</body></html>"));
      content.add(employeeBtn);
      
      setSize(350, 700);
      setVisible(true);
   }
}
