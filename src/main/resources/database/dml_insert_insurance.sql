INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('건강보험 1','건강보험 1 설명입니다.',80,40,'HEALTH');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (1, '건강보험 보장 1', '건강보험 보장 1 설명입니다.', 1000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (1, '건강보험 보장 2', '건강보험 보장 2 설명입니다.', 20000000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (1, 1, '2022-05-31', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (38904, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (1, 20, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (36958, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (2, 30, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (41733, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (3, 40, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (43148, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (4, 50, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (29178, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (5, 60, 0, 0);
INSERT INTO sales_authorization_file (insurance_id) VALUES (1);

INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('자동차보험 1','자동차보험 1 설명입니다.',60,40,'CAR');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (2, '자동차보험 보장 1', '자동차보험 보장 1 설명입니다.', 100000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (2, '자동차보험 보장 2', '자동차보험 보장 2 설명입니다.', 2000000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (2, '자동차보험 보장 3', '자동차보험 보장 3 설명입니다.', 4000000000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (2, 1, '2022-05-31', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (60750, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (6, 20, 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (66000, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (7, 30, 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (63249, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (8, 40, 1000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (63249, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (9, 50, 5000000000);
INSERT INTO sales_authorization_file (insurance_id) VALUES (2);

INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('화재보험 1','화재보험 1 설명입니다.',50,20,'FIRE');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (3, '화재보험 보장 1', '화재보험 보장 1 설명입니다.', 100000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (3, '화재보험 보장 2', '화재보험 보장 2 설명입니다.', 2000000000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (3, 5, '2022-05-31', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (2247142, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (10, 'COMMERCIAL', 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (2160713, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (11, 'INDUSTRIAL', 5000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1987856, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (12, 'INSTITUTIONAL', 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1901427, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (13, 'RESIDENTIAL', 100000000);
INSERT INTO sales_authorization_file (insurance_id) VALUES (3);

update develop_info set sales_authorization_state = 'PERMISSION' , sales_start_date = sysdate();

