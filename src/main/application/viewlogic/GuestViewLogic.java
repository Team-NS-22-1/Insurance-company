package main.application.viewlogic;

import main.application.ViewLogic;
import main.domain.contract.*;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.SalesAuthState;

import java.util.Scanner;

import static main.domain.contract.BuildingType.*;
import static main.domain.contract.CarType.*;
import static main.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : GuestViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class GuestViewLogic implements ViewLogic {
    int command;
    Scanner sc = new Scanner(System.in);

    private InsuranceListImpl insuranceList = new InsuranceListImpl();
    private ContractListImpl contractList = new ContractListImpl();
    private CustomerListImpl customerList = new CustomerListImpl();

    public GuestViewLogic(InsuranceListImpl insuranceList, ContractListImpl contractList, CustomerListImpl customerList) {
    }

    @Override
    public void showMenu() {
        createMenu("보험가입희망자메뉴", "보험가입", "뒤로가기");
    }

    @Override
    public void work(String command) {
        boolean isLoop = true;

        while (isLoop) {

                switch (command) {
                    // 보험가입
                    case "1":
                        selectInsurance();
                    // 뒤로
                    case "2":
                        isLoop = false;
                        break;
                }
        }
    }

    private void selectInsurance() {
        boolean isLoop = true;

        while(isLoop) {
            for (Insurance insurance : insuranceList.readAll()) {
                System.out.println("보험코드: " + insurance.getId() + "\t보험이름: " + insurance.getName() + "\t보험종류: " + insurance.getInsuranceType());
            }

            System.out.println("가입할 보험상품의 보험코드를 입력하세요.");
            command =sc.nextInt();
            Insurance insurance = insuranceList.read(command);

            // System.out.println(insurance.getId());

                if (insurance == null) {
                    System.out.println("선택된 보험상품이 없습니다.");
                    break;
                } else if (insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION) {
                    System.out.println("보험설명: " + insurance.getDescription() + "\n보장내역: " + insurance.getGuarantee());
                    createMenu("해당 보험상품을 가입하시겠습니까?","가입", "취소");
                    command = sc.nextInt();

                    switch (command) {
                        // 가입
                        case 1:
                            isLoop = false;
                            signContract(insurance.getId());
                            break;
                        // 취소
                        case 2:
                            isLoop = false;
                            break;
                        default:
                            System.out.println("잘못된 입력입니다.");
                            break;
                    }

                } else {
                    System.out.println("해당 상품은 준비중입니다.");
                    break;
                }
        }

    }

    private void signContract(int insuranceId) {

        Customer customer = new Customer();
        Contract contract = new Contract();

        System.out.println("이름을 입력해주세요.");
        String name = sc.next();

        System.out.println("주민번호를 입력해주세요.");
        String ssn = sc.next();

        System.out.println("연락처를 입력해주세요.");
        String phone = sc.next();

        System.out.println("주소를 입력해주세요.");
        String address = sc.next();

        System.out.println("이메일을 입력해주세요.");
        String email = sc.next();

        System.out.println("직업을 입력해주세요.");
        String job = sc.next();

        customer.setName(name)
                .setSsn(ssn)
                .setPhone(phone)
                .setAddress(address)
                .setEmail(email)
                .setJob(job);

        switch (insuranceList.read(insuranceId).getInsuranceType()) {
            case HEALTH:
                int count = 0;
                String diseaseDetail;

                System.out.println("키를 입력해주세요.");
                int height = sc.nextInt();

                System.out.println("몸무게를 입력해주세요.");
                int weight = sc.nextInt();

                createMenu("음주 여부를 입력해주세요.","예", "아니요");
                command = sc.nextInt();
                boolean isDrinking = false;
                if(command == 1) {
                    isDrinking = true;
                    count++;
                }

                createMenu("흡연 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isSmoking = false;
                if(command == 1) {
                    isSmoking = true;
                    count++;
                }

                createMenu("운전 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isDriving = false;
                if(command == 1) {
                    isDriving = true;
                    count++;
                }

                createMenu("위험 취미 활동 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isDangerousActivity = false;
                if(command == 1) {
                    isDangerousActivity = true;
                    count++;
                }

                createMenu("약물 복용 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isTakingDrug = false;
                if(command == 1) {
                    isTakingDrug = true;
                    count++;
                }

                createMenu("질병 이력 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isHavingDisease = false;
                if(command == 1) {
                    isHavingDisease = true;
                    count++;
                }

                if (isHavingDisease) {
                    System.out.println("질병에 대한 상세 내용를 입력해주세요.");
                    diseaseDetail = sc.next();
                } else {
                    diseaseDetail = null;
                }

                contract.setHealthInfo(new HealthInfo().setHeight(height)
                        .setWeight(weight)
                        .setDrinking(isDrinking)
                        .setSmoking(isSmoking)
                        .setDriving(isDriving)
                        .setDangerousActivity(isDangerousActivity)
                        .setTakingDrug(isTakingDrug)
                        .setHavingDisease(isHavingDisease)
                        .setDiseaseDetail(diseaseDetail)
                );

                signUp(insuranceId, contract, customer);

                break;

            case FIRE:
                BuildingType buildingType = null;
                createMenu("건물종류를 입력해주세요.","주거용", "상업용", "산업용", "공업용");
                command  = sc.nextInt();
                switch (command) {
                    case 1:
                        buildingType = RESIDENTIAL;
                        break;
                    case 2:
                        buildingType = COMMERCIAL;
                        break;
                    case 3:
                        buildingType = INDUSTRIAL;
                        break;
                    case 4:
                        buildingType = INSTITUTIONAL;
                        break;
                }

                System.out.println("건물면적을 입력해주세요.");
                int buildingArea = sc.nextInt();

                System.out.println("담보 금액을 입력해주세요.");
                int collateralAmount = sc.nextInt();

                createMenu("자가 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isSelfOwned = command == 1;

                createMenu("실거주 여부를 입력해주세요","예", "아니요");
                command = sc.nextInt();
                boolean isActualResidence = command == 1;

                contract.setBuildingInfo(new BuildingInfo().setBuildingType(buildingType)
                        .setBuildingArea(buildingArea)
                        .setCollateralAmount(collateralAmount)
                        .setSelfOwned(isSelfOwned)
                        .setActualResidence(isActualResidence)
                );

                signUp(insuranceId, contract, customer);

                break;

            case CAR:
                System.out.println("차량번호를 입력해주세요.");
                String carNo  = sc.next();

                CarType carType = null;
                createMenu("차종을 입력해주세요.","경형", "소형", "준중형", "중형", "준대형", "대형", "스포츠카");
                command  = sc.nextInt();
                switch (command) {
                    case 1:
                        carType = URBAN;
                        break;
                    case 2:
                        carType = SUBCOMPACT;
                        break;
                    case 3:
                        carType = COMPACT;
                        break;
                    case 4:
                        carType = MIDSIZE;
                        break;
                    case 5:
                        carType = LARGESIZE;
                        break;
                    case 6:
                        carType = FULLSIZE;
                        break;
                    case 7:
                        carType = SPORTS;
                        break;
                }

                System.out.println("모델이름을 입력해주세요.");
                String modelName = sc.next();

                System.out.println("차량연식을 입력해주세요.");
                int modelYear = sc.nextInt();

                System.out.println("차량가액을 입력해주세요.");
                int value = sc.nextInt();

                contract.setCarInfo(new CarInfo().setCarNo(carNo)
                        .setCarType(carType)
                        .setModelName(modelName)
                        .setModelYear(modelYear)
                        .setValue(value)
                );

                signUp(insuranceId, contract, customer);

                break;
        }
    }

    private void signUp(int insuranceId, Contract contract, Customer customer) {

        int premium = insuranceList.readPremium(insuranceId);
        System.out.println("조회된 귀하의 보험료는: " + premium + "원입니다.");
        createMenu("보험가입을 신청하시겠습니까?","가입", "취소");
        command = sc.nextInt();
        switch (command) {
            case 1:
                customerList.create(customer);
                contract.setPremium(premium)
                        .setCustomerId(customer.getId())
                        .setInsuranceId(insuranceId)
                        .setConditionOfUw(ConditionOfUw.WAIT);
                contractList.create(contract);
                System.out.println(customerList.read(customer.getId()));
                System.out.println(contractList.read(contract.getId()));
                System.out.println("가입이 완료되었습니다.");
                break;
            case 2:
                System.out.println("가입이 취소되었습니다.");
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
}
