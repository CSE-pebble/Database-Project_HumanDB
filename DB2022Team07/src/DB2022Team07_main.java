import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public class DB2022Team07_main extends JFrame {
    static Frame frame = new Frame("���� ������");

	public static void main(String args[]) {
		frame.setLayout(new FlowLayout());	        
        frame.setSize(700,700);
		
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        JButton branchBtn=new JButton("����");
	    frame.add(branchBtn);
	    JButton memberBtn=new JButton("ȸ��");
	    frame.add(memberBtn);
	    JButton trainerBtn=new JButton("Ʈ���̳�");
	    frame.add(trainerBtn);
	    
	    branchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Branch.Monthly_Revenue(frame);
				Branch.Branch_Info(frame);
			}
	    	
	    });
	    
	    memberBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Member.Expired_Member(frame);
				Member.BMI_Calculator(frame);
			}
	    	
	    });

		frame.setVisible(true);
	}
}
