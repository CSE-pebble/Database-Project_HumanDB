import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class MainFrame extends JFrame {
   static List profitList = new List();
   static List infoList = new List();
   static JPanel panel = new JPanel();
   static Container content;

   static boolean month = false;
   static boolean info = false;

   public MainFrame() {
      setTitle("Trainer ���� ���");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      content = getContentPane();
      content.setLayout(new FlowLayout());

      JButton branchBtn = new JButton("����");
      JButton memberBtn = new JButton("ȸ��");
      JButton trainerBtn = new JButton("Ʈ���̳�");
      branchBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Branch.Monthly_Revenue(content);
            Branch.Branch_Info(content);
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
            //TrainerFrame T_frame = new TrainerFrame();
         }
      });
      content.add(branchBtn);
      content.add(memberBtn);
      content.add(trainerBtn);
      
      setSize(250, 700);
      setVisible(true);
   }
}