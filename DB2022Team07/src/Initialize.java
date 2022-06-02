import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Initialize {
	public static void Create(Connection conn, Statement stmt) {
		try{
			//stmt.executeUpdate("use "+ DBID+";");
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
					+ "	primary key (name),\r\n"
					+ " key idx_city (city)\r\n"
					+ ");");
			stmt.executeUpdate("create table DB2022_trainers (\r\n"
					+ "	trainer_id int,\r\n"
					+ "	name varchar(20) not null,\r\n"
					+ "	career_start_year int not null,\r\n"
					+ "	career_start_month int not null,\r\n"
					+ "	branch varchar(30) not null,\r\n"
					+ "\r\n"
					+ "	primary key (trainer_id),\r\n"
					+ "	foreign key (branch) references DB2022_branches(name),\r\n"
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
					+ "  foreign key (trainer) references DB2022_trainers(trainer_id),\r\n"
					+ "key idx_name (name)"
					+ ");");
			stmt.executeUpdate("create table DB2022_enroll (\r\n"
					+ "	member_id int,\r\n"
					+ "	enroll_date date not null,\r\n"
					+ "	start_date date,\r\n"
					+ "	membership int not null,\r\n"
					+ "\r\n"
					+ "	primary key (member_id, start_date), \r\n"
					+ "	foreign key (membership) references DB2022_membership(membership_id),\r\n"
					+ "	foreign key (member_id) references DB2022_members(member_id)\r\n"
					+ ");");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void View(Connection conn, Statement stmt) {
		try {
				// ȸ���� ���� �Ⱓ view
			 	stmt.executeUpdate("create view DB2022_period as\r\n"
			 			+ "select enroll.member_id, members.name, start_date, date_add(enroll.start_date, interval membership.period day) as end_date\r\n"
			 			+ "from DB2022_enroll as enroll, DB2022_membership as membership, DB2022_members as members\r\n"
			 			+ "where enroll.member_id=members.member_id and enroll.membership=membership.membership_id;");
			 	// Ʈ���̳� ��� view
			 	stmt.executeUpdate("create view DB2022_career as\r\n"
			 			+ "select trainer_id, \r\n"
			 			+ "floor(((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()))/12) as career_year,\r\n"
			 			+ "mod((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()),12) as career_month\r\n"
			 			+ "from DB2022_trainers as trainers");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void DropDb(Connection conn, Statement stmt) {
		try {
			 	stmt.executeUpdate("drop table DB2022_enroll;\r\n"
			 			+ "drop table DB2022_members;\r\n"
			 			+ "drop table DB2022_trainers;\r\n"
			 			+ "drop table DB2022_branches;\r\n"
			 			+ "drop table DB2022_membership;");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
	public static void InsertInitialData(Connection conn, Statement stmt) {
		try {
			 	stmt.executeUpdate("insert into DB2022_membership values\r\n"
			 			+ "    (1, '30�ϱ�', 30, 50000),\r\n"
			 			+ "    (2, '60�ϱ�', 60, 80000),\r\n"
			 			+ "    (3, '90�ϱ�', 90, 120000),\r\n"
			 			+ "    (4, '���� ��� Ư�� ȸ����', 50, 50000),\r\n"
			 			+ "    (5, '���� ��� Ư�� ȸ����', 100, 200000);\r\n");
			 	stmt.executeUpdate("insert into DB2022_branches values\r\n"
			 			+ "    ('����', '02-100-1234', '���ν�', '������', '�������', '435', '������'),\r\n"
			 			+ "    ('�뷮��', '02-120-1000', '����Ư����', '���۱�', '��¹���', '161', '������'),\r\n"
			 			+ "    ('��â', '02-570-4321', '����Ư����', '���۱�', '�����߾ӷ�', '52', '�ֱ���');\r\n");
			 	stmt.executeUpdate("insert into DB2022_trainers values\r\n"
			 			+ "    (1, '������', 2017, 5, '����'),\r\n"
			 			+ "    (2, '���ϼ�', 2022, 6, '����'),\r\n"
			 			+ "    (3, '������', 2013, 11, '��â'),\r\n"
			 			+ "    (4, 'ä�ر�', 2010, 12, '�뷮��'),\r\n"
			 			+ "    (5, '������', 2020, 7, '�뷮��'),\r\n"
			 			+ "    (6, '������', 2015, 9, '��â');\r\n");
			 	stmt.executeUpdate("insert into DB2022_members values\r\n"
			 			+ "    (1, '�̳���', 'F', 160, 60, '01029302038', '2940', '����', 1),\r\n"
			 			+ "    (2, '�輱��', 'F', 170, 70, '01034062059', '1930', '�뷮��', 4),\r\n"
			 			+ "    (3, '������', 'F', 175, 75, '01034062059', '4021', '��â', 3),\r\n"
			 			+ "    (4, '��뿬', 'F', 165, 65, '01034062059', '4920', '��â', null),\r\n"
			 			+ "    (5, '�赵��', 'F', 155, 55, '01024568059', '6569', '�뷮��', 5),\r\n"
			 			+ "    (6, '��ο�', 'M', 175, 92, '01002942038', '4496', '��â', 6),\r\n"
			 			+ "    (7, '����ȣ', 'M', 180, 68, '01089072769', '0123', '����', null);\r\n");
			 	stmt.executeUpdate("insert into DB2022_enroll values\r\n"
			 			+ "    (3, '2021-04-19', '2022-05-30', 2),\r\n"
			 			+ "    (4, '2018-05-01', '2018-05-03', 4),\r\n"
			 			+ "    (2, '2022-05-10', '2022-05-23', 1),\r\n"
			 			+ "    (1, '2022-05-22', '2022-05-22', 3),\r\n"
			 			+ "    (5, '2022-05-23', '2022-06-06', 1);\r\n");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle);
		}
	}
}
