INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('건강보험 1','건강보험 1 설명',80,40,'HEALTH');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (1, '건강보험 보장 1', '건강보험 보장 1 설명입니다', 1000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (1, '건강보험 보장 2', '건강보험 보장 2 설명입니다', 2100000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (1, 1, '2022-06-05', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (38904, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (1, 0, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (35662, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (2, 0, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (35662, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (3, 0, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (40318, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (4, 0, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (36958, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (5, 20, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (41733, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (6, 20, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (38255, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (7, 20, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (43148, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (8, 20, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (39552, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (9, 30, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (44562, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (10, 30, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (40849, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (11, 30, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (37489, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (12, 30, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (34365, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (13, 40, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (45977, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (14, 40, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (42146, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (15, 40, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (37489, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (16, 40, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (34365, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (17, 50, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (38094, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (18, 50, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (35266, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (19, 50, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (40138, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (20, 50, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (36598, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (21, 60, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (41337, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (22, 60, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (38205, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (23, 60, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (43109, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (24, 60, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (39255, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (25, 80, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (44503, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (26, 80, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (40409, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (27, 80, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (37432, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (28, 80, 1, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (34256, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (29, 100, 0, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (45821, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (30, 100, 0, 1);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (42049, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (31, 100, 1, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (37321, 1);
INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (32, 100, 1, 1);
INSERT INTO sales_authorization_file (insurance_id) VALUES (1);





INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('자동차 보험 1','자동차 보험 1 설명입니다',80,40,'CAR');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (2, '자동차 보험 1 보장', '자동차 보험 1 보장 설명입니다', 1000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (2, '자동차 보험 2 보장', '자동차 보험 2 보장 설명입니다', 2000000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (2, 1, '2022-06-05', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (101709, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (33, 20, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (113682, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (34, 20, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (117017, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (35, 20, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (117017, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (36, 20, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (127893, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (37, 20, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (127893, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (38, 20, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (156314, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (39, 20, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (72630, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (40, 30, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (82104, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (41, 30, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (82104, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (42, 30, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (88420, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (43, 30, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (101051, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (44, 30, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (113682, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (45, 30, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (138945, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (46, 30, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (69604, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (47, 40, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (78683, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (48, 40, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (78683, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (49, 40, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (84735, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (50, 40, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (96841, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (51, 40, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (108946, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (52, 40, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (133156, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (53, 40, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (69604, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (54, 50, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (78683, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (55, 50, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (69604, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (56, 50, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (84735, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (57, 50, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (96841, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (58, 50, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (108946, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (59, 50, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (133156, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (60, 50, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (76630, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (61, 60, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (82104, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (62, 60, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (82104, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (63, 60, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (88420, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (64, 60, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (101051, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (65, 60, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (113682, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (66, 60, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (138945, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (67, 60, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (84735, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (68, 80, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (92367, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (69, 80, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (92367, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (70, 80, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (99472, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (71, 80, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (113682, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (72, 80, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (127893, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (73, 80, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (156314, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (74, 80, 150000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (95735, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (75, 100, 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (99786, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (76, 100, 10000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (99786, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (77, 100, 30000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (103156, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (78, 100, 50000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (117893, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (79, 100, 70000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (132630, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (80, 100, 90000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (162103, 2);
INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (81, 100, 150000000);
INSERT INTO sales_authorization_file (insurance_id) VALUES (2);

INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('화재보험 1','화재보험 1 설명입니다',80,40,'FIRE');
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (3, '화재보험 1 보장 1', '화재보험 1 보장 1 설명입니다', 10000000000);
INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (3, '화재보험 1 보장 2', '화재보험 1 보장 2 설명입니다', 2000000000);
INSERT INTO develop_info (insurance_id, employee_id, develop_date, sales_authorization_state) VALUES (3, 1, '2022-06-05', 'WAIT');
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1951576, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (82, 'COMMERCIAL', 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1881577, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (83, 'COMMERCIAL', 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1806314, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (84, 'COMMERCIAL', 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1731051, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (85, 'COMMERCIAL', 1000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1655788, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (86, 'COMMERCIAL', 5000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1889200, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (87, 'INDUSTRIAL', 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1809209, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (88, 'INDUSTRIAL', 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1736841, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (89, 'INDUSTRIAL', 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1664472, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (90, 'INDUSTRIAL', 1000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1592104, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (91, 'INDUSTRIAL', 5000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1724478, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (92, 'INSTITUTIONAL', 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1664472, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (93, 'INSTITUTIONAL', 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1597893, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (94, 'INSTITUTIONAL', 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1531314, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (95, 'INSTITUTIONAL', 1000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1464735, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (96, 'INSTITUTIONAL', 5000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1652104, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (97, 'RESIDENTIAL', 0);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1592104, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (98, 'RESIDENTIAL', 100000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1528420, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (99, 'RESIDENTIAL', 500000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1464735, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (100, 'RESIDENTIAL', 1000000000);
INSERT INTO insurance_detail (premium, insurance_id) VALUES (1401051, 3);
INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (101, 'RESIDENTIAL', 5000000000);

INSERT INTO sales_authorization_file (insurance_id) VALUES (3);

update develop_info set sales_authorization_state = 'PERMISSION' , sales_start_date = sysdate();