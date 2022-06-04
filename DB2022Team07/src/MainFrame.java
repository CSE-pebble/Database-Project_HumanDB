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

      JButton branchBtn = new JButton("����");
      JButton registerBtn = new JButton ("ȸ������");
      JButton memberBtn = new JButton("���� ������");
      JButton trainerBtn = new JButton("Ʈ���̳� ��� / ����");
      JButton employeeBtn = new JButton("���� ������");
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
      		+ "�ȳ��ϼ���!<br/>"
      		+ "Ewha Gym�Դϴ�.<br/>"
      		+ "���Ͻô� ���񽺸� �������ּ���.<br/>"
      		+ "</body></html>"));
      content.add(new JLabel("<html><body style='text-align:center;'>"
        		+ "----------------------------------------------------------------------------------<br/>"
        		+ "* ������ ���� ��ȸ *<br/>"
        		+ "</body></html>"));
      content.add(branchBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* ȸ�� ���� *<br/>"
      		+ "</body></html>"));
      content.add(registerBtn);
      content.add(memberBtn);
      content.add(trainerBtn);
      content.add(new JLabel("<html><body style='text-align:center;'>"
      		+ "----------------------------------------------------------------------------------<br/>"
      		+ "* ���� ���� *<br/>"
      		+ "</body></html>"));
      content.add(employeeBtn);
      
      setSize(350, 700);
      setVisible(true);
   }
}
