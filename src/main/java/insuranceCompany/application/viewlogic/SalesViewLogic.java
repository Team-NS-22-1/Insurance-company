package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Guarantee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.User;

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
    private HealthContract healthContract;
    private FireContract fireContract;
    private CarContract carContract;
    private Employee employee;
    private Customer customer;
    private Insurance insurance;

    public SalesViewLogic() {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    public SalesViewLogic(Employee employee) {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employee = employee;
    }

    @Override
    public void showMenu() {
        createMenuAndLogout("<<영업팀 메뉴>>", "보험상품설계");
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                // 보험상품설계
                case "1" -> {
                    selectInsurance();
                }
                case "" -> throw new InputException.InputNullDataException();
                default -> throw new InputException.InputInvalidMenuException();
            }
        } catch(InputException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        }
    }

    public void selectInsurance() throws IOException {
        InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
        ArrayList<Insurance> insurances = insuranceDao.readAll();
        if(insurances.size() == 0)
            throw new InputException.NoResultantException();
        while (true) {
            System.out.println("<< 보험상품목록 >>");
            for (Insurance insurance : insurances) {
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION)
                    System.out.println("상품번호: " + insurance.getId() + " | 보험이름: " + insurance.getName() + "   \t보험종류: " + insurance.getInsuranceType());
            }

            try {
                System.out.println("설계할 보험상품의 번호를 입력하세요. \t(0 : 뒤로가기)");
                int insuranceId = br.verifyMenu("보험상품 번호: ", insurances.size());
                if (insuranceId == 0) break;

                insuranceDao = new InsuranceDaoImpl();
                insurance = insuranceDao.read(insuranceId);
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println("<< 상품안내 >>\n" + insurance.getDescription() + "\n<< 보장내역 >>");
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.println(guarantee);
                    }
                    switch (insurance.getInsuranceType()) {
                        case HEALTH -> planHealthInsurance();
                        case FIRE -> planFireInsurance();
                        case CAR -> planCarInsurance();
                    }
                }
                else {
                    throw new InputException.NoResultantException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            }
        }
    }

    private void planHealthInsurance() throws IOException {
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

        int premium = employee.inquireHealthPremium(targetAge, targetSex, riskCount, insurance);
        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        int choice = br.verifyCategory("보험계약을 진행하시겠습니까?\n1. 계약\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                healthContract = employee.planHealthInsurance(insurance.getId(), premium, isDrinking, isSmoking,
                                                                isDriving, isDangerActivity, isTakingDrug, isHavingDisease);
                inputCustomerInfo();
            }
            case 2 -> System.out.println("계약이 취소되었습니다.");
        }
    }

    private void planFireInsurance() throws IOException {
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

        int premium = employee.inquireFirePremium(buildingType, collateralAmount, insurance);
        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        int choice = br.verifyCategory("보험계약을 진행하시겠습니까?\n1. 계약\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                fireContract = employee.planFireInsurance(insurance.getId(), premium, buildingType, collateralAmount);
                inputCustomerInfo();
            }
            case 2 -> System.out.println("계약이 취소되었습니다.");
        }
    }

    private void planCarInsurance() throws IOException {
        int targetAge = 0;
        Long value = 0L;

        targetAge = (int) br.verifyRead("고객님의 나이: ", targetAge);
        value = (Long) br.verifyRead("차량가액 (단위: 원): ", value);

        int premium = employee.inquireCarPremium(targetAge, value, insurance);
        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        int choice = br.verifyCategory("보험계약을 진행하시겠습니까?\n1. 계약\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                carContract = employee.planCarInsurance(insurance.getId(), premium, value);
                inputCustomerInfo();
            }
            case 2 -> System.out.println("계약이 취소되었습니다.");
        }
    }

    private void inputCustomerInfo() throws IOException {
        int customerId = 0;

        int choice = br.verifyCategory("등록된 회원입니까?\n1. 예\n2. 아니요\n", 2);
        switch (choice) {
            // 등록 고객
            case 1 -> {
                customerId = (int) br.verifyRead("고객 ID를 입력해주세요.\n고객 ID: ", customerId);

                CustomerDaoImpl customerDao = new CustomerDaoImpl();
                customer = customerDao.read(customerId);
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

                customer = employee.inputCustomerInfo(name, ssn, phone, address, email, job);
            }
        }

        switch (insurance.getInsuranceType()) {
            case HEALTH -> inputHealthInfo();
            case FIRE -> inputFireInfo();
            case CAR -> inputCarInfo();
        }
    }

    private void inputHealthInfo() throws IOException {
        String diseaseDetail = "";
        int height = 0, weight = 0;

        height = (int) br.verifyRead("키 (단위: cm): ", height);
        weight = (int) br.verifyRead("몸무게 (단위: kg): ", weight);
        if (healthContract.isHavingDisease())
            diseaseDetail = (String) br.verifyRead("질병에 대한 상세 내용를 입력해주세요.\n", diseaseDetail);

        healthContract = employee.inputHealthInfo(healthContract, height, weight, diseaseDetail);
        concludeContract(healthContract);
    }

    private void inputFireInfo() throws IOException {
        int buildingArea = 0;
        boolean isSelfOwned, isActualResidence;

        buildingArea = (int) br.verifyRead("건물면적 (단위: m^2): ", buildingArea);
        isSelfOwned = br.verifyCategory("자가 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        isActualResidence = br.verifyCategory("실거주 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;

        fireContract = employee.inputFireInfo(fireContract, buildingArea, isSelfOwned, isActualResidence);
        concludeContract(fireContract);
    }

    private void inputCarInfo() throws IOException {
        CarType carType;
        String modelName = null, carNo = null;
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

        carContract = employee.inputCarInfo(carContract, carNo, carType, modelName, modelYear);
        concludeContract(carContract);
    }

    private void concludeContract(Contract contract) throws IOException {
        int choice = br.verifyCategory("보험 계약을을 체결하시겠습니까?\n1. 계약체결\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                User user = null;
                if (customer.getId() == 0) {
                    user = signUp();
                }
                employee.registerContract(customer, contract, user, employee);
                System.out.println(customer);
                System.out.println(contract);
                System.out.println("계약을 체결하였습니다.");
            }
            case 2 -> System.out.println("계약을 취소되었습니다.");
        }
    }

    private User signUp() throws IOException {
        String userId = "", password = "";
        userId = (String) br.verifyRead("고객이 지정한 아이디: ", userId);
        password = (String) br.verifyRead("고객이 지정한 비밀번호: ", password);
        User user = employee.createUserAccount(userId, password);
        return user;
    }
}
