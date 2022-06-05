import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DB2022Team07_MainFrame extends JFrame {
   static List profitList = new List();
   static List infoList = new List();
   static Container content;

   static boolean month = false;
   static boolean info = false;

   public DB2022Team07_MainFrame() {
      setTitle("Ewha Gym");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
   // 硫붾돱 踰꾪듉 �깮�꽦
      JButton branchBtn = new JButton("Branch Information");
      JButton registerBtn = new JButton ("Sign Up");
      JButton memberBtn = new JButton("My Page");
      JButton trainerBtn = new JButton("PT Application / Change Trainer");
      JButton employeeBtn = new JButton("Staff Page");
      JButton enrollBtn = new JButton("Buy Membership");
      
	// 媛� 硫붾돱 踰꾪듉�쓣 �겢由��븯硫� �빐�떦 �봽�젅�엫�쓣 �깮�꽦�븳�떎.
      branchBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
        	 new DB2022Team07_BranchFrame();
         }
      });
      registerBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new DB2022Team07_RegisterFrame();
		}
    	  
      });
      memberBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new DB2022Team07_MyPageFrame();
         }

      });
      trainerBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
           new DB2022Team07_TrainerFrame();
         }
      });
      enrollBtn.addActionListener(new ActionListener() {
    	  @Override
    	  public void actionPerformed(ActionEvent e) {
    		  new DB2022Team07_EnrollFrame();
    	  }
      });
      employeeBtn.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new DB2022Team07_EmployeeFrame();
		}
      });

   // Container�뿉 �뙣�꼸 異붽��븯湲�
      content = getContentPane();
      content.setLayout(new FlowLayout());
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "Hello!<br/>"
      		+ "Welcome to Ewha Gym.<br/>"
      		+ "Please choose the service.<br/>"
      		+ "</body></html>"));
      content.add(new JLabel("<html><body style='text-align:center;'>"
        		+ "----------------------------------------------------------------------------------<br/>"
        		+ "* Branch Information Inquiry *<br/>"
        		+ "</body></html>"));
      content.add(branchBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* Member Service *<br/>"
      		+ "</body></html>"));
      content.add(registerBtn);
      content.add(memberBtn);
      content.add(enrollBtn);
      content.add(trainerBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* Staff Service *<br/>"
      		+ "</body></html>"));
      content.add(employeeBtn);
      
      setSize(350, 700);
      setVisible(true);
   }
}
