import javax.swing.*;
import java.sql.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class BranchFrame extends JFrame {
	private JTextField year_field = new JTextField(25);
	private JTextField month_field = new JTextField(25);
	private JLabel profit = new JLabel();
	private JLabel t1 = new JLabel();
	private JButton revenue_btn = new JButton("조회");
	Calendar now = getInstance();

	public BranchFrame() {
		setTitle("Branch");
		profit.setText("");
		Container content = getContentPane();
		content.setLayout(new FlowLayout());
		content.add(new JLabel("<html><body style='text-align:center;'>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "* 지점별 월별 매출 조회 *<br/>" + "조회할 연도와 월을 입력하세요.<br/>"
				+ "----------------------------------------------------------------------------------<br/>"
				+ "</body></html>"));
		content.add(new JLabel("   연도  "));
		content.add(year_field);
		content.add(new JLabel("   월     "));
		content.add(month_field);
		content.add(revenue_btn);
		content.add(profit);

		revenue_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String tempYear = year_field.getText().trim();
				String tempMonth = month_field.getText().trim();
				
				try (Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + DB2022Team07_main.DBID, DB2022Team07_main.USERID,
						DB2022Team07_main.PASSWD); Statement stmt = conn.createStatement();) {
					PreparedStatement pStmt = conn.prepareStatement(
									"select branch, sum(price) as 'revenue' " 
									+ "from DB2022_enroll join DB2022_membership "
									+ "on DB2022_enroll.membership=DB2022_membership.membership_id "
									+ "join DB2022_members using(member_id) "
									+ "where year(enroll_date)=? and month(enroll_date)=? " + "group by branch "
									+ "order by branch;");
					pStmt.setString(1, tempMonth);
					pStmt.setString(2, tempYear);
					ResultSet rset = pStmt.executeQuery();
					// 연도와 월을 잘못 입력한 경우
					if (!rset.next()) {
						profit.setText("<html><body style='text-align:center;'>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "잘못 입력하셨습니다.<br/>" + "정확한 연도와 월을 다시 입력하세요.<br/>"
								+ "----------------------------------------------------------------------------------<br/>"
								+ "</body></html>");
					} 
					else {
						String info;
						profit.setText("<html><body style='text-align:center;'>");
						while (rset.next()) {
							info = (rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원<br/>");
							profit.setText(profit.getText()+info);
						}
						profit.setText("</body></html>");
					}

				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle);
				}
			}

		});

		setSize(350, 500);
		setVisible(true);
	}

	private Calendar getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
