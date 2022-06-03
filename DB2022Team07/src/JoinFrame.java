import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class JoinFrame extends JFrame {
	Font titleFont = new Font("맑은 고딕", Font.PLAIN, 24);
	Font bodyFont = new Font("맑은 고딕", Font.PLAIN, 15);
	
	public JoinFrame()  {
		// 제목
		JLabel title = new JLabel("회원가입");
		title.setFont(titleFont);
		title.setHorizontalAlignment(JLabel.CENTER);
		
		// 이름 필드
		JLabel nameLb = new JLabel("이름:");
		nameLb.setFont(bodyFont);
		JTextField nameTf = new JTextField(4);
		// 성별 필드
		JLabel genderLb = new JLabel("성별:");
		genderLb.setFont(bodyFont);
		JTextField genderTf = new JTextField(2);
		// 첫 번째 줄
		JPanel pn1 = new JPanel(new FlowLayout());
		pn1.add(nameLb);
		pn1.add(nameTf);
		pn1.add(genderLb);
		pn1.add(genderTf);
		
		// 키 필드
		JLabel heightLb = new JLabel("키:");
		heightLb.setFont(bodyFont);
		JTextField heightTf = new JTextField(4);
		// 몸무게 필드
		JLabel weightLb = new JLabel("몸무게:");
		weightLb.setFont(bodyFont);
		JTextField weightTf = new JTextField(3);
		// 지점 필드
		JLabel branchLb = new JLabel("지점:");
		branchLb.setFont(bodyFont);
		JTextField branchTf = new JTextField(4);
		// 두 번째 줄
		JPanel pn2 = new JPanel(new FlowLayout());
		pn2.add(heightLb);
		pn2.add(heightTf);
		pn2.add(weightLb);
		pn2.add(weightTf);
		pn2.add(branchLb);
		pn2.add(branchTf);
		
		// 전화번호 필드
		JLabel phoneLb = new JLabel("전화번호:");
		phoneLb.setFont(bodyFont);
		JTextField phoneTf = new JTextField(15);
		// 세 번째 줄
		JPanel pn3 = new JPanel();
		pn3.setLayout(new FlowLayout());
		pn3.add(phoneLb);
		pn3.add(phoneTf);
		
		// 비밀번호 필드
		JLabel passwordLb = new JLabel("비밀번호:");
		passwordLb.setFont(bodyFont);
		JTextField passwordTf = new JTextField(15);
		// 네 번째 줄
		JPanel pn4 = new JPanel();
		pn4.setLayout(new FlowLayout());
		pn4.add(passwordLb);
		pn4.add(passwordTf);
		
		// 전체 필드 패널 생성
		JPanel fieldPn = new JPanel();
		fieldPn.setLayout(new BoxLayout(fieldPn, BoxLayout.Y_AXIS));
		fieldPn.add(pn1);
		fieldPn.add(pn2);
		fieldPn.add(pn3);
		fieldPn.add(pn4);
		
		// 가입 버튼
		JButton joinBtn = new JButton("가입하기");
		joinBtn.setFont(bodyFont);
		
		// 가입 버튼 이벤트 추가
		joinBtn.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {       
				String nameValue = nameTf.getText();
				String genderValue = genderTf.getText();
				int heightValue = Integer.parseInt(heightTf.getText());
				int weightValue = Integer.parseInt(weightTf.getText());
				String phoneValue = phoneTf.getText();
				String passwordValue = passwordTf.getText();
				// ...
            }
		});  
		
		// 컨테이너 생성
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(title, BorderLayout.NORTH);
		container.add(fieldPn, BorderLayout.CENTER);
		container.add(joinBtn, BorderLayout.SOUTH);
		setTitle("회원가입");
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}


	public static void main(String[] args) {
		new JoinFrame();

	}

}
