create table customer(
customer_id int primary key not null auto_increment,
name varchar(10),
job varchar(20),
ssn varchar(14),
phone varchar(20),
email varchar(30),
address varchar(200)
);

create table employee(
employee_id int primary key not null auto_increment,
name varchar(10),
phone varchar(20),
department varchar(15),
position varchar(20)
);

create table payment(
payment_id int primary key not null auto_increment,
pay_type varchar(10),
customer_id int,
foreign key (customer_id) references customer(customer_id) on delete cascade
);

create table card(
payment_id int primary key not null,
card_no varchar(20),
card_type varchar(10),
cvc_no varchar(5),
expiry_date date,
foreign key (payment_id) references payment(payment_id) on delete cascade
);

create table account(
payment_id int primary key not null,
account_no varchar(20),
bank_type varchar(10),
foreign key (payment_id) references payment(payment_id) on delete cascade
);

create table complain(
complain_id int primary key not null auto_increment,
reason varchar(2000),
customer_id int,
foreign key (customer_id) references customer(customer_id) on delete cascade
);

create table accident(
accident_id int primary key not null auto_increment,
accident_type varchar(20),
employee_id int,
customer_id int,
loss_reserves bigint,
date_of_accident datetime,
date_of_report datetime
);

create table car_accident(
accident_id int primary key not null,
car_no varchar(15),
place_address varchar(100),
opposing_driver_phone varchar(13),
is_request_on_site boolean,
error_rate int,
foreign key (accident_id) references accident (accident_id) on delete cascade
);

create table car_breakdown(
accident_id int primary key not null,
car_no varchar(15),
place_address varchar(100),
symptom varchar(1000),
foreign key (accident_id) references accident (accident_id) on delete cascade
);

create table fire_accident(
accident_id int primary key not null,
place_address varchar(100),
foreign key (accident_id) references accident (accident_id) on delete cascade
);

create table injury_accident(
accident_id int primary key not null,
injury_site varchar(100),
foreign key (accident_id) references accident (accident_id) on delete cascade
);

create table accident_document_file(
accident_document_file_id int not null primary key auto_increment,
type varchar(50),
file_address varchar(5000),
accident_id int,
last_modified_date datetime,
foreign key(accident_id) references accident(accident_id) on delete cascade
);

create table contract(
contract_id int not null primary key auto_increment,
reason_of_uw varchar(2000),
payment_id int,
insurance_id int,
customer_id int,
employee_id int,
premium int,
is_publish_stock boolean,
condition_of_uw varchar(15)
);

create table health_contract(
contract_id int not null primary key,
height int,
weight int,
is_danger_activity boolean,
is_drinking boolean,
is_smoking boolean,
is_taking_drug boolean,
disease_detail varchar(255),
is_having_disease boolean,
is_driving boolean,
foreign key (contract_id) references contract(contract_id) on delete cascade
);

create table car_contract(
contract_id int not null primary key,
car_no varchar(15),
car_type varchar(15),
model_year int,
name varchar(20),
owner varchar(20),
value int,
foreign key (contract_id) references contract(contract_id) on delete cascade
);

create table fire_contract(
contract_id int not null primary key,
building_area int,
building_type varchar(20),
collateral_amount bigint,
is_actual_residence boolean,
is_self_owned boolean,
foreign key (contract_id) references contract(contract_id) on delete cascade
);

create table insurance(
insurance_id int primary key not null auto_increment,
name varchar(20),
description varchar(2000),
contract_period int,
payment_period int,
insurance_type varchar(10)
);

create table insurance_detail(
insurance_detail_id int primary key not null auto_increment,
premium int,
insurance_id int,
foreign key (insurance_id) references insurance(insurance_id) on delete cascade
);

create table health_detail(
health_detail_id int primary key not null,
target_age int,
target_sex boolean,
risk_criterion boolean,
foreign key (health_detail_id) references insurance_detail(insurance_detail_id) on delete cascade
);

create table car_detail(
car_detail_id int primary key not null,
target_age int,
value_criterion bigint,
foreign key (car_detail_id) references insurance_detail(insurance_detail_id) on delete cascade
);

create table fire_detail(
fire_detail_id int primary key not null,
target_building_type varchar(20),
collateral_amount_criterion bigint,
foreign key (fire_detail_id) references insurance_detail(insurance_detail_id) on delete cascade
);

create table guarantee(
guarantee_id int primary key not null auto_increment,
insurance_id int,
name varchar(20),
description varchar(100),
amount bigint,
foreign key(insurance_id) references insurance(insurance_id) on delete cascade
);

create table develop_info(
insurance_id int primary key not null,
employee_id int,
develop_date date,
sales_authorization_state varchar(10),
sales_start_date date,
foreign key(insurance_id) references insurance(insurance_id) on delete cascade
);

create table sales_authorization_file(
insurance_id int primary key not null,
fss_official_doc varchar(50),
modified_fss datetime,
iso_verification varchar(50),
modified_iso datetime,
prod_declaration varchar(50),
modified_prod datetime,
sr_actuary_verification varchar(50),
modified_sr_actuary datetime,
foreign key(insurance_id) references insurance(insurance_id) on delete cascade
);

create table user (
id int primary key not null auto_increment,
user_id varchar(255) not null,
password varchar(255) not null,
role_id int not null
);