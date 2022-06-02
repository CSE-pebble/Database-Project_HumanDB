import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class LoginFrame extends JFrame {
	private JFrame frame;
	
	public LoginFrame() {
		frame = new JFrame();
		
		Font titleFont = new Font("맑은 고딕", Font.PLAIN, 24);
		Font bodyFont = new Font("맑은 고딕", Font.PLAIN, 15);
		
		// 제목
		JLabel titleLb = new JLabel("로그인");
		titleLb.setFont(titleFont);
		
		JPanel titlePn = new JPanel();
		titlePn.setSize(100, 30);
		titlePn.add(titleLb);
		
		// 이름 필드 생성
		JLabel nameLb = new JLabel("이름:");
		nameLb.setSize(80, 30);
		nameLb.setFont(bodyFont);
		
		JTextField nameTf = new JTextField(10);
		nameTf.setSize(100, 30);
		nameTf.setFont(bodyFont);
		
		// 전화번호 필드 생성
		JLabel phoneLb = new JLabel("전화번호:");
		phoneLb.setFont(bodyFont);
		
		JTextField phoneTf = new JTextField(10);
		phoneTf.setSize(100, 30);
		phoneTf.setFont(bodyFont);
		
		// 비밀번호 필드 생성
		JLabel passwordLb = new JLabel("비밀번호:");
		passwordLb.setFont(bodyFont);
		
		JTextField passwordTf = new JTextField(10);
		passwordTf.setFont(bodyFont);
		
		// 필드 패널 생성
		JPanel fieldPn = new JPanel();
		fieldPn.setSize(180, 100);
		fieldPn.setLayout(new GridLayout(3,2));
		fieldPn.add(nameLb); fieldPn.add(nameTf);
		fieldPn.add(phoneLb); fieldPn.add(phoneTf);
		fieldPn.add(passwordLb); fieldPn.add(passwordTf);
		
		// 로그인 버튼 생성
		JButton loginBtn = new JButton("로그인");
		loginBtn.setSize(160, 30);
		loginBtn.setFont(bodyFont);
		
		JPanel loginBtnPn = new JPanel();
		loginBtnPn.add(loginBtn);
		
		// 로그인 버튼 이벤트 추가
		loginBtn.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {       
				String name = nameTf.getText();
				String phone = phoneTf.getText();
				String password = passwordTf.getText();
            }
		});   
		
		frame.setTitle("로그인");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		
		frame.add(titlePn, BorderLayout.NORTH);
		frame.add(fieldPn, BorderLayout.CENTER);
		frame.add(loginBtnPn, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new LoginFrame();
	}
}

