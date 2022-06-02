import java.sql.*;
import java.util.Scanner;

public class DB2022Team07 {
	static final String DBID = "DB2022Team07";
	static final String USERID = "root";
	static final String PASSWD = "wcdi9786@#";


	public static void Create() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			stmt.executeUpdate("create table DB2022_membership(\r\n"
					+ "	membership_id int,\r\n"
					+ "	name varchar(30) not null,\r\n"
					+ "	period int not null,\r\n"
					+ "	price int not null,\r\n"
					+ "\r\n"
					+ "primary key (membership_id),\r\n"
					+ "key idx_name (name)\r\n"
					+ ");");
			stmt.executeUpdate("create table DB2022_branches (\r\n"
					+ "	name varchar(20),\r\n"
					+ "	phone varchar(15) not null,\r\n"
					+ "	city varchar(10) not null,\r\n"
					+ "	state varchar(10) not null,\r\n"
					+ "	street_name varchar(30) not null,\r\n"
					+ "	street_number varchar(10) not null,\r\n"
					+ "	boss varchar(20) not null,\r\n"
					+ "\r\n"
					+ "	primary key (name)\r\n"
					+ "key idx_city (city)\r\n"
					+ ");");
			stmt.executeUpdate("create table DB2022_trainers (\r\n"
					+ "	trainer_id int,\r\n"
					+ "	name varchar(20) not null,\r\n"
					+ "	career_start_year int not null,\r\n"
					+ "	career_start_month int not null,\r\n"
					+ "	branch varchar(30) not null,\r\n"
					+ "\r\n"
					+ "	primary key (trainer_id),\r\n"
					+ "	foreign key (branch) references DB2022_branches(name)\r\n"
					+ " key idx_name (name)\r\n"
					+ ");");
			stmt.executeUpdate("create table DB2022_members(\r\n"
					+ "  member_id int,\r\n"
					+ "  name varchar(20) not null,\r\n"
					+ "  gender char(1) not null,\r\n"
					+ "  height int not null,\r\n"
					+ "  weight int not null,\r\n"
					+ "	phone char(11) not null,\r\n"
					+ "	password char(4) not null, \r\n"
					+ "	branch varchar(20) not null,\r\n"
					+ "	trainer int,\r\n"
					+ "\r\n"
					+ "  primary key (member_id),\r\n"
					+ "  foreign key (branch) references DB2022_branches(name),\r\n"
					+ "  foreign key (trainer) references DB2022_trainers(trainer_id)\r\n"
					+ " key idx_name (name)\r\n"
					+ ");");
			stmt.executeUpdate("create table DB2022_enroll (\r\n"
					+ "	member_id int,\r\n"
					+ "	enroll_date date not null,\r\n"
					+ "	start_date date,\r\n"
					+ "	membership int not null, -- membership은 table 이름과 동일하므로 그냥 membership_id\r\n"
					+ "\r\n"
					+ "	primary key (member_id, start_date), -- composite key\r\n"
					+ "	foreign key (membership) references DB2022_membership(membership_id),\r\n"
					+ "	foreign key (member_id) references DB2022_members(member_id)\r\n"
					+ ");");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void View() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			 	stmt.executeUpdate("create view DB2022_period as\r\n"
			 			+ "select enroll.member_id, members.name, start_date, date_add(enroll.start_date, interval membership.period day) as end_date\r\n"
			 			+ "from DB2022_enroll as enroll, DB2022_membership as membership, DB2022_members as members\r\n"
			 			+ "where enroll.member_id=members.member_id and enroll.membership=membership.membership_id;");
			 	stmt.executeUpdate("create view DB2022_career as\r\n"
			 			+ "select trainer_id, \r\n"
			 			+ "floor(((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()))/12) as career_year,\r\n"
			 			+ "mod((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()),12) as career_month\r\n"
			 			+ "from DB2022_trainers as trainers");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void DropDb() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			 	stmt.executeUpdate("create table DB2022_membership(\r\n"
			 			+ "	membership_id int,\r\n"
			 			+ "	name varchar(30) not null,\r\n"
			 			+ "	period int not null,\r\n"
			 			+ "	price int not null,\r\n"
			 			+ "\r\n"
			 			+ "	primary key (membership_id)\r\n"
			 			+ ");");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void InsertInitialData() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			 	stmt.executeUpdate("insert into DB2022_membership values\r\n"
			 			+ "    (1, '30일권', 30, 50000),\r\n"
			 			+ "    (2, '60일권', 60, 80000),\r\n"
			 			+ "    (3, '90일권', 90, 120000),\r\n"
			 			+ "    (4, '종강 기념 특별 회원권', 50, 50000),\r\n"
			 			+ "    (5, '개강 기념 특별 회원권', 100, 200000);\r\n"
			 			+ "select * from DB2022_membership;\r\n"
			 			+ "\r\n"
			 			+ "insert into DB2022_branches values\r\n"
			 			+ "    ('수지', '02-100-1234', '용인시', '수지구', '포은대로', '435', '박현지'),\r\n"
			 			+ "    ('노량진', '02-120-1000', '서울특별시', '동작구', '장승배기로', '161', '김지혜'),\r\n"
			 			+ "    ('염창', '02-570-4321', '서울특별시', '동작구', '마곡중앙로', '52', '최기준');\r\n"
			 			+ "select * from DB2022_branches;\r\n"
			 			+ "\r\n"
			 			+ "insert into DB2022_trainers values\r\n"
			 			+ "    (1, '김진해', 2017, 5, '수지'),\r\n"
			 			+ "    (2, '정완섭', 2022, 6, '수지'),\r\n"
			 			+ "    (3, '도실인', 2013, 11, '염창'),\r\n"
			 			+ "    (4, '채준기', 2010, 12, '노량진'),\r\n"
			 			+ "    (5, '김정연', 2020, 7, '노량진'),\r\n"
			 			+ "    (6, '이은혜', 2015, 9, '염창');\r\n"
			 			+ "select * from DB2022_trainers;\r\n"
			 			+ "\r\n"
			 			+ "insert into DB2022_members values\r\n"
			 			+ "    (1, '이나현', 'F', 160, 60, '01029302038', '2940', '수지', 1),\r\n"
			 			+ "    (2, '김선영', 'F', 170, 70, '01034062059', '1930', '노량진', 4),\r\n"
			 			+ "    (3, '정민정', 'F', 175, 75, '01034062059', '4021', '염창', 3),\r\n"
			 			+ "    (4, '김용연', 'F', 165, 65, '01034062059', '4920', '염창', null),\r\n"
			 			+ "    (5, '김도연', 'F', 155, 55, '01024568059', '6569', '노량진', 5),\r\n"
			 			+ "    (6, '김민우', 'M', 175, 92, '01002942038', '4496', '염창', 6),\r\n"
			 			+ "    (7, '최인호', 'M', 180, 68, '01089072769', '0123', '수지', null);\r\n"
			 			+ "select * from DB2022_members;\r\n"
			 			+ "\r\n"
			 			+ "insert into DB2022_enroll values\r\n"
			 			+ "    (3, '2021-04-19', '2022-05-30', 2),\r\n"
			 			+ "    (4, '2018-05-01', '2018-05-03', 4),\r\n"
			 			+ "    (2, '2022-05-10', '2022-05-23', 1),\r\n"
			 			+ "    (1, '2022-05-22', '2022-05-22', 3),\r\n"
			 			+ "    (5, '2022-05-23', '2022-06-06', 1);\r\n"
			 			+ "select * from DB2022_enroll;");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void Trainer_Change(String member_name, String phone) {
		Scanner s = new Scanner(System.in);
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			// 회원의 이름과 생년월일을 바탕으로 member_id 검색
			ResultSet info_set = stmt
					.executeQuery("select * from DB2022_members join DB2022_enroll using(member_id)\r\n"
							+ "where name='" + member_name + "' and phone='" + phone + "';\r\n");
			// 회원 정보가 잘못되었거나, 회원권을 등록한 회원DB에 회원이 존재하지 않는 경우
			if (!info_set.next()) {
				System.out.println("존재하지 않는 회원입니다.");
			} else {
				// 보안을 위해 password 확인
				System.out.println("본인확인을 위해 비밀번호를 입력해주세요.");
				String input_passwd = s.next();
				String passwd = info_set.getString("password");
				while (!input_passwd.equals(passwd)) {
					System.out.println("비밀번호가 잘못되었습니다. 다시 입력해주세요.");
					input_passwd = s.next();
				}
				;
				// 회원의 member_id와 지점 저장
				String member_id = info_set.getString("member_id");
				String member_branch = info_set.getString("branch");

				// 해당 회원의 지점에 있는 trainer 목록 검색
				ResultSet trainer_set = stmt
						.executeQuery("select *\r\n" + "from DB2022_trainers join DB2022_career using(trainer_id)\r\n"
								+ "where branch ='" + member_branch + "';\r\n");
				System.out.println(member_branch + "소속 트레이너 명단입니다.");
				while (trainer_set.next()) {
					System.out.println(trainer_set.getString("trainer_id") + "번: " + trainer_set.getString("name")
							+ ", 경력: " + trainer_set.getString("career_year") + "년 "
							+ trainer_set.getString("career_month") + "개월");
				}
				// 원하는 트레이너의 번호 입력
				System.out.println("원하는 트레이너의 번호를 입력해주세요.");
				String trainer_id = s.next();
				// 선택한 trainer로 변경 혹은 등록
				stmt.executeUpdate("update DB2022_members\r\n" + "set trainer ='" + trainer_id + "'\r\n"
						+ "where member_id='" + member_id + "';\r\n");
				System.out.println("트레이너가 변경되었습니다.");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
		s.close();

	}

	public static void Trainer_Move(String trainer, String branch) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			stmt.executeUpdate("update DB2022_trainers\r\n" + "set branch ='" + branch + "'\r\n" + "where trainer='"
					+ trainer + "';\r\n");
			System.out.println("지점 이동 처리되었습니다.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	public static void Trainer_Quit(String trainer) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {

			stmt.executeUpdate(
					"update DB2022_members\r\n" + "set trainer = null\r\n" + "where trainer='" + trainer + "';\r\n");
			stmt.executeUpdate("delete from DB2022_trainers\r\n" + "where trainer_id='" + trainer + "';\r\n");

			System.out.println("퇴사처리되었습니다.");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}

	public static void Monthly_Revenue(String year, String month) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBID, USERID, PASSWD);
				Statement stmt = conn.createStatement();) {
			ResultSet rset = stmt.executeQuery("select branch, sum(price) as 'revenue'\r\n"
					+ "from DB2022_enroll join DB2022_membership \r\n"
					+ "on DB2022_enroll.membership=DB2022_membership.membership_id\r\n"
					+ "join DB2022_members using(member_id)\r\n" + "where year(enroll_date)='" + year
					+ "' and month(enroll_Date)='" + month + "'\r\n" + "group by branch\r\n" + "order by branch; ");
			System.out.println(year + "년 " + month + "월의 " + "매출: ");
			while (rset.next()) {
				System.out.println(rset.getString("branch") + "지점: " + rset.getInt("revenue") + "원");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}

	}
}
