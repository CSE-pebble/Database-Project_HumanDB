-- create database DB2022Team07;
use DB2022Team07 ;

/* -- 테이블 생성 -- */

/* 편의상 주석에서는 테이블 이름을 DB2022 없이 작성하겠습니다. */

/* 
< membership 테이블 >
1. 테이블 용도 설명 
: 헬스장에서 구매할 수 있는 여러 종류의 회원권에 대한 정보를 담고 있다.

2. Attribute 설명
- membership_id : 각 회원권별 고유 번호
- name : 30일권, 60일권 등의 실제 헬스장에서 부르는 회원권 이름 
- period : 회원권 기간 
- price : 회원권 가격 

3. key 설명
- primary key : 회원권별 고유 번호인 'membership_id'로 지정
 */
create table DB2022_membership(
	membership_id int,
	name varchar(30) not null,
	period int not null,
	price int not null,

	primary key (membership_id)
);

/* 
< branches 테이블 >
1. 테이블 용도 설명 
: 헬스장은 여러 지점을 가지고 있다. branches 테이블은 각 지점별 정보를 담고있다.

2. Attribute 설명
- name : 지점의 이름 
- phone : 지점의 전화번호
- city : 지점의 '시' 단위 위치 
- state : 지점의 '구' 단위 위치 
- street_name : 지점의 주소. '포은대로' 등의 도로명 주소가 들어간다.
- street_number : 지점의 주소. '435' 등의 도로명 주소가 들어간다.
- boss : 지점의 관리자(매니저) 이름

3. key 설명
- primary key : 지점의 이름은 지점별로 모두 다르므로 'name'으로 지정
 */
create table DB2022_branches (
	name varchar(20),
	phone varchar(15) not null,
	city varchar(10) not null,
	state varchar(10) not null,
	street_name varchar(30) not null,
	street_number varchar(10) not null,
	boss varchar(20) not null,

	primary key (name)
);

/* 
< trainers 테이블 >
1. 테이블 용도 설명 
: 헬스장에 속한 트레이너들의 정보를 담고있다. 

2. Attribute 설명
- trainer_id : 각 트레이너별 고유 번호 
- name : 실제 트레이너의 이름 
- career_start_year : 트레이너 경력 시작 연도
- career_start_month : 트레이너 경력 시작 월
- branch : 트레이너가 속한 지점 이름
- password : 본인 정보 조회/수정을 위한 트레이너 개인별 패스워드

3. key 설명
- foreign key : 'branch'는 지점 이름에 관한 정보이므로, 지점에 관한 상위 테이블인 branches의 'name'에서 참조한다.
- primary key : 트레이너별 고유 번호인 'trainer_id'로 지정
 */
create table DB2022_trainers (
	trainer_id int,
	name varchar(20) not null,
	career_start_year int not null,
	career_start_month int not null,
	branch varchar(30) not null,
  password varchar(4) not null,

	primary key (trainer_id),
	foreign key (branch) references DB2022_branches(name)
);

/* 
< members 테이블 >
1. 테이블 용도 설명 
: 헬스장에 등록 중인/등록했던 회원들의 정보를 담고 있다. 

2. Attribute 설명
- member_id : 각 회원별 고유 번호
- name : 실제 회원의 이름
- gender : 회원의 성별. F/M으로 구분한다.
- height : 회원의 키
- weight : 회원의 몸무게
- phone : 회원의 전화번호
- password : 본인 정보 조회/수정을 위한 회원 개인별 패스워드
- branch : 회원이 속한 지점 이름
- trainer : 해당 회원을 담당하는 트레이너의 고유 번호. 만약 트레이너가 퇴사를 하게 되면 담당 트레이너가 사라지므로 null 값이 들어갈 수도 있다.

3. key 설명
- foreign key : 
  'branch'는 지점 이름에 관한 정보이므로, 지점에 관한 상위 테이블인 branches의 'name'에서 참조한다.
  'trainer'는 트레이너의 고유 번호에 관한 정보이므로, 트레이너에 대한 상위 테이블인 trainers의 'trainer_id'애서 참조한다.
- primary key : 회원별 고유 번호인 'member_id'로 지정

 */
create table DB2022_members(
  member_id int,
  name varchar(20) not null,
  gender char(1) not null,
  height int not null,
  weight int not null,
	phone char(11) not null,
	password varchar(4) not null, 
	branch varchar(20) not null,
	trainer int,

  primary key (member_id),
  foreign key (branch) references DB2022_branches(name),
  foreign key (trainer) references DB2022_trainers(trainer_id)
);

/* 
< enroll 테이블 >
1. 테이블 용도 설명 
: 회원권이 유효한 회원들(활동회원들)의 정보를 담고 있다. 

2. Attribute 설명
- member_id : 각 회원별 고유 번호
- enroll_date : 회원권 구매 날짜
- start_date : 회원권 시작 날짜. 회원이 회원권을 구매한 후 운동을 시작하기로 지정하는 날짜이다. 이때부터 회원권의 유효기간이 카운트된다. 회원권을 사놓고 시작하지 않을 수 있으므로 null 값이 들어갈 수도 있다.
- membership : 회원이 이용하고 있는 회원권 고유 번호

3. key 설명
- foreign key : 
  'membership'은 회원권 고유 번호에 관한 정보이므로, 회원권에 관한 상위 테이블인 membership의 'membership_id'에서 참조한다.
  'member_id'는 회원의 고유 번호에 관한 정보이므로, 회원에 대한 상위 테이블인 members의 'member_id'애서 참조한다.
- primary key : member_id만으로는 모든 tuple을 구별할 수 없다. 한 회원이 여러개의 회원권을 구매할 수 있기 때문이다. 따라서 member_id에 start_date까지 포함해준다. 한 회원이 여러 회원권을 구매한다고 해도 여러개의 회원권을 동시에 시작할 수는 없기 때문이다.
 */
create table DB2022_enroll (
	member_id int,
	enroll_date date not null,
	start_date date,
	membership int not null,

	primary key (member_id, start_date), -- composite key
	foreign key (membership) references DB2022_membership(membership_id),
	foreign key (member_id) references DB2022_members(member_id)
);




/* -- 데이터 삽입 -- */

insert into DB2022_membership values
    (1, '30일권', 30, 50000),
    (2, '60일권', 60, 80000),
    (3, '90일권', 90, 120000),
    (4, '종강 기념 특별 회원권', 50, 50000),
    (5, '개강 기념 특별 회원권', 100, 200000);
select * from DB2022_membership;

insert into DB2022_branches values
    ('수지', '02-100-1234', '용인시', '수지구', '포은대로', '435', '박현지'),
    ('노량진', '02-120-1000', '서울특별시', '동작구', '장승배기로', '161', '김지혜'),
    ('염창', '02-570-4321', '서울특별시', '동작구', '마곡중앙로', '52', '최기준');
select * from DB2022_branches;

insert into DB2022_trainers values
    (1, '김진해', 2017, 5, '수지', '30q0'),
    (2, '정완섭', 2022, 6, '수지', '5ab0'),
    (3, '도실인', 2013, 11, '염창', '3068'),
    (4, '채준기', 2010, 12, '노량진', 'Ai39'),
    (5, '김정연', 2020, 7, '노량진', '1c59'),
    (6, '이은혜', 2015, 9, '염창', '7e4r');
select * from DB2022_trainers;

insert into DB2022_members values
    (1, '이나현', 'F', 160, 60, '01029302038', '2av0', '수지', 1),
    (2, '김선영', 'F', 170, 70, '01034062059', '1930', '노량진', 4),
    (3, '정민정', 'F', 175, 75, '01059202038', 'AB21', '염창', 3),
    (4, '김용연', 'F', 165, 65, '01090794634', '49pD', '염창', null),
    (5, '김도연', 'F', 155, 55, '01024568059', '6569', '노량진', 5),
    (6, '김민우', 'M', 175, 92, '01002942038', 'an96', '염창', 6),
    (7, '최인호', 'M', 180, 68, '01089072769', '0qi3', '수지', null),
    (8, '이진호', 'M', 184, 74, '01082937912', '1298', '노량진', 5),
    (9, '권아영', 'F', 163, 51, '01049817291', '59S2', '노량진', 5),
    (10, '성지윤', 'F', 153, 48, '01059197662', 'va12', '노량진', 4),
    (11, '강지훈', 'M', 171, 93, '01098781613', '09iS', '수지', 1);
select * from DB2022_members;

insert into DB2022_enroll values
    (3, '2021-04-19', '2022-05-30', 2),
    (4, '2018-05-01', '2018-05-03', 4),
    (2, '2022-05-10', '2022-05-23', 1),
		(3, '2021-05-20', '2022-08-01', 2),
    (1, '2022-05-22', '2022-05-22', 3),
		(1, '2022-05-22', '2022-09-01', 1),
    (5, '2022-05-23', '2022-06-06', 1);
select * from DB2022_enroll;


/* -- 뷰 생성 -- */

/* 회원의 회원권 기간 */
create view DB2022_period as
select enroll.member_id, members.name, start_date, date_add(enroll.start_date, interval membership.period day) as end_date, membership.name as membership
from DB2022_enroll as enroll, DB2022_membership as membership, DB2022_members as members
where enroll.member_id=members.member_id and enroll.membership=membership.membership_id;

/* 트레이너 경력 */
create view DB2022_career as
select trainer_id, 
floor(((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()))/12) as career_year,
mod((year(now())-trainers.career_start_year)*12-trainers.career_start_month+month(now()),12) as career_month
from DB2022_trainers as trainers;


/* -- 인덱스 생성 -- */

/* 트레이너들 지점별로 인덱싱 */
create index idx_trainer_branch
on DB2022_trainers(branch);
show index from DB2022_trainers;

/* 회원들 트레이너별로 인덱싱 */
create index idx_memb_trainer
on DB2022_members(trainer);
show index from DB2022_members;

/* 활동 회원들 회원권으로 인덱싱 */
create index idx_enroll_membership
on DB2022_enroll(membership);
show index from DB2022_enroll;

/* 회원들 번호별로 인덱싱 */
create unique index idx_memb_phone
on DB2022_members(phone);
show index from DB2022_members;