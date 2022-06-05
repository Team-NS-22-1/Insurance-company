package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.contract.BuildingType;
import insuranceCompany.application.domain.contract.CarType;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.*;
import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.User;
import insuranceCompany.application.viewlogic.dto.UserDto.UserDto;
import insuranceCompany.application.viewlogic.dto.contractDto.CarContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.ContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.FireContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.HealthContractDto;
import insuranceCompany.application.viewlogic.dto.customerDto.CustomerDto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static insuranceCompany.application.domain.contract.BuildingType.*;
import static insuranceCompany.application.domain.contract.CarType.*;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndLogout;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : SalesViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class SalesViewLogic implements ViewLogic {
    private MyBufferedReader br;
    private Employee employee;
    private Customer customer;
    private Insurance insurance;

    public SalesViewLogic(Employee employee) {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employee = employee;
    }

    @Override
    public String showMenu() {
        return createMenuAndLogout("<<영업팀 메뉴>>", "보험상품설계");
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                // 보험상품설계
                case "1" -> selectInsurance();
                case "" -> throw new InputNullDataException();
                default -> throw new InputInvalidMenuException();
            }
        } catch(InputException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        }
    }

    public void selectInsurance() throws IOException {
        ArrayList<Insurance> insurances = employee.readInsurances();
        if(insurances.size() == 0)
            throw new NoResultantException();
        while (true) {
            System.out.println("<< 보험상품목록 >>");
            for (Insurance insurance : insurances) {
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION)
                    System.out.println("보험상품 번호: " + insurance.getId() + " | 보험상품 이름: " + insurance.getName() + "   \t보험상품 종류: " + insurance.getInsuranceType());
            }

            try {
                int insuranceId = 0;
                System.out.println("설계할 보험상품의 번호를 입력하세요. \t(0 : 뒤로가기)");
                insuranceId = (int) br.verifyRead("보험상품 번호: ", insuranceId);
                if(insuranceId == 0) break;

                insurance = employee.readInsurance(insuranceId);
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println("<< 상품안내 >>\n" + insurance.getDescription() + "\n계약기간: " + insurance.getContractPeriod() +
                            "년  납입기간: " + insurance.getPaymentPeriod() +"년\n<< 보장내역 >>");
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.println(guarantee);
                    }
                    ProgressContract();
                }
                else {
                    throw new MyIllegalArgumentException("ERROR:: ID["+ insuranceId + "]에 해당하는 보험 정보가 존재하지 않습니다.");
                }
            } catch (InputException | MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            }
        }
    }

    private void ProgressContract() {
        switch (insurance.getInsuranceType()) {
            case HEALTH -> {
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.println("<< 계약조건 >>\n나이: " + ((HealthDetail) insuranceDetail).getTargetAge() + "\n성별: " + ((HealthDetail) insuranceDetail).getTargetSex() +
                            "\n위험도 기준: " + ((HealthDetail) insuranceDetail).getRiskCriterion() + "\n보험료: " + insuranceDetail.getPremium());
                }
            }
            case FIRE -> {
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.println("<< 계약조건 >>\n건물종류: " + ((FireDetail) insuranceDetail).getTargetBuildingType() +
                            "\n담보금액: " + ((FireDetail) insuranceDetail).getCollateralAmountCriterion() + "\n보험료: " + insuranceDetail.getPremium());
                }
            }
            case CAR -> {
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.println("<< 계약조건 >>\n나이: " + ((CarDetail) insuranceDetail).getTargetAge() +
                            "\n차량가액: " + (( CarDetail) insuranceDetail).getValueCriterion() + "\n보험료: " + insuranceDetail.getPremium());
                }
            }
        }

        ContractDto contractDto =  switch (insurance.getInsuranceType()) {
            case HEALTH -> planHealthInsurance();
            case FIRE -> planFireInsurance();
            case CAR -> planCarInsurance();
        };

        int choice = br.verifyCategory("보험계약을 진행하시겠습니까?\n1. 계약\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                CustomerDto customerDto = inputCustomerInfo();
                contractDto = switch (insurance.getInsuranceType()) {
                    case HEALTH -> inputHealthInfo(contractDto);
                    case FIRE -> inputFireInfo(contractDto);
                    case CAR -> inputCarInfo(contractDto);
                };
                concludeContract(customerDto, contractDto);
            }
            case 2 -> System.out.println("계약이 취소되었습니다.");
        }
    }

    private ContractDto planHealthInsurance() {
        int riskCount = 0, targetAge = 0;
        boolean targetSex, isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease;

        targetAge = (int) br.verifyRead("고객님의 나이: ", targetAge);
        targetSex =  br.verifyCategory("고객님의 성별 \n1. 남  2. 여\n", 2) == 1;
        isDrinking = br.verifyCategory("음주 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if(isDrinking) riskCount++;
        isSmoking = br.verifyCategory("흡연 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if(isSmoking) riskCount++;
        isDriving = br.verifyCategory("운전 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if(isDriving) riskCount++;
        isDangerActivity = br.verifyCategory("위험 취미 활동 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if(isDangerActivity) riskCount++;
        isTakingDrug = br.verifyCategory("약물 복용 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if(isTakingDrug) riskCount++;
        isHavingDisease = br.verifyCategory("질병 이력 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        if (isHavingDisease) riskCount++;

        int premium = employee.planHealthInsurance(targetAge, targetSex, riskCount, insurance);
        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        HealthContractDto healthContractDto = new HealthContractDto();
        healthContractDto.setDrinking(isDrinking)
                .setSmoking(isSmoking)
                .setDriving(isDriving)
                .setDangerActivity(isDangerActivity)
                .setTakingDrug(isTakingDrug)
                .setHavingDisease(isHavingDisease)
                .setPremium(premium);
        return healthContractDto;
    }

    private ContractDto planFireInsurance() {
        BuildingType buildingType;
        Long collateralAmount = 0L;

        buildingType = switch (br.verifyCategory("건물종류를 선택해주세요.\n1. 상업용\n2. 산업용\n3. 기관용\n4. 거주용\n", 4)) {
            case 1 -> COMMERCIAL;
            case 2 -> INDUSTRIAL;
            case 3 -> INSTITUTIONAL;
            case 4 -> RESIDENTIAL;
            default -> throw new IllegalStateException();
        };
        collateralAmount = (Long) br.verifyRead("담보금액: (단워: 원): ", collateralAmount);

        int premium = employee.planFireInsurance(buildingType, collateralAmount, insurance);
        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        FireContractDto fireContractDto = new FireContractDto();
        fireContractDto.setBuildingType(buildingType)
                .setCollateralAmount(collateralAmount)
                .setPremium(premium);
        return fireContractDto;
    }

    private ContractDto planCarInsurance() {
        int targetAge = 0;
        Long value = 0L;

        targetAge = (int) br.verifyRead("고객님의 나이: ", targetAge);
        value = (Long) br.verifyRead("차량가액 (단위: 원): ", value);

        int premium = employee.planCarInsurance(targetAge, value, insurance);
        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        CarContractDto carContractDto = new CarContractDto();
        carContractDto.setValue(value)
                    .setPremium(premium);
        return carContractDto;
    }

    private CustomerDto inputCustomerInfo() {
        int customerId = 0;
        CustomerDto customerDto = null;

        int choice = br.verifyCategory("등록된 회원입니까?\n1. 예\n2. 아니요\n", 2);
        switch (choice) {
            // 등록 고객
            case 1 -> {
                customerId = (int) br.verifyRead("고객 ID를 입력해주세요.\n고객 ID: ", customerId);
                customer = employee.readCustomer(customerId);
            }
            // 미등록 고객
            case 2 -> {
                String name = "", ssn = "", phone = "", address = "", email = "", job = "";
                System.out.println("<<고객님의 개인정보를 입력해주세요.>>");

                name = (String) br.verifySpecificRead("이름: ", name, "name");
                ssn = (String) br.verifySpecificRead("주민번호 (______-*******): ", ssn, "ssn");
                phone = (String) br.verifySpecificRead("연락처 (0__-____-____): ", phone, "phone");
                address = (String) br.verifyRead("주소: ", address);
                email = (String) br.verifySpecificRead("이메일 (_____@_____.___): ", email, "email");
                job = (String) br.verifyRead("직업: ", job);

                customerDto = new CustomerDto(name, ssn, phone, address, email, job);
            }
        }
        return customerDto;
    }

    private ContractDto inputHealthInfo(ContractDto contractDto) {
        String diseaseDetail = "";
        int height = 0, weight = 0;

        height = (int) br.verifyRead("키 (단위: cm): ", height);
        weight = (int) br.verifyRead("몸무게 (단위: kg): ", weight);
        if (((HealthContractDto) contractDto).isHavingDisease())
            diseaseDetail = (String) br.verifyRead("질병에 대한 상세 내용를 입력해주세요.\n", diseaseDetail);

        ((HealthContractDto) contractDto).setHeight(height)
                                        .setWeight(weight)
                                        .setDiseaseDetail(diseaseDetail)
                                        .setEmployeeId(employee.getId());
        return contractDto;
    }

    private ContractDto inputFireInfo(ContractDto contractDto) {
        int buildingArea = 0;
        boolean isSelfOwned, isActualResidence;

        buildingArea = (int) br.verifyRead("건물면적 (단위: m^2): ", buildingArea);
        isSelfOwned = br.verifyCategory("자가 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        isActualResidence = br.verifyCategory("실거주 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;

        ((FireContractDto) contractDto).setBuildingArea(buildingArea)
                                        .setSelfOwned(isSelfOwned)
                                        .setActualResidence(isActualResidence)
                                        .setEmployeeId(employee.getId());
        return contractDto;
    }

    private ContractDto inputCarInfo(ContractDto contractDto) {
        CarType carType;
        String modelName = "", carNo = "";
        int modelYear = 0;

        carNo = (String) br.verifySpecificRead("차량번호: ", carNo, "carNo");
        carType = switch (br.verifyCategory("차종을 선택해주세요.\n1. 경형\n2. 소형\n3. 준중형\n4. 중형\n5. 준대형\n6. 대형\n7. 스포츠카\n", 7)) {
            case 1 -> URBAN;
            case 2 -> SUBCOMPACT;
            case 3 -> COMPACT;
            case 4 -> MIDSIZE;
            case 5 -> LARGESIZE;
            case 6 -> FULLSIZE;
            case 7 -> SPORTS;
            default -> throw new IllegalStateException();
        };
        modelName = (String) br.verifyRead("모델 이름: ", modelName);
        modelYear = (int) br.verifyRead("차량 연식 (단위: 년): ", modelYear);

        ((CarContractDto) contractDto).setCarNo(carNo)
                                    .setCarType(carType)
                                    .setModelName(modelName)
                                    .setModelYear(modelYear)
                                    .setEmployeeId(employee.getId());
        return  contractDto;
    }

    private void concludeContract(CustomerDto customerDto, ContractDto contractDto) {
        int choice = br.verifyCategory("보험 계약을을 체결하시겠습니까?\n1. 계약체결\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                User user = null;
                if (customer.getId() == 0) {
                    customer = employee.registerCustomer(customerDto);
                    UserDto userDto = signUp();
                    user = employee.registerUser(userDto);
                }
                Contract contract = employee.registerContract(customer, contractDto, insurance);
                System.out.println(customer);
                if(user != null)
                    System.out.println(user);
                System.out.println(contract);
                System.out.println("계약을 체결하였습니다.");
            }
            case 2 -> System.out.println("계약을 취소되었습니다.");
        }
    }

    private UserDto signUp() {
        String userId = "", password = "";
        userId = (String) br.verifyRead("아이디: ", userId);
        password = (String) br.verifyRead("비밀번호: ", password);
        return new UserDto(userId, password, customer.getId());
    }
}
