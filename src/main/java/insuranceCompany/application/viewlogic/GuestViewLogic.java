package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Guarantee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.utility.CriterionSetUtil;
import insuranceCompany.application.global.utility.InputValidation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static insuranceCompany.application.domain.contract.BuildingType.*;
import static insuranceCompany.application.domain.contract.CarType.*;
import static insuranceCompany.application.global.utility.MessageUtil.createMenu;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndClose;
import static insuranceCompany.application.global.utility.TargetInfoCalculator.targetAgeCalculator;
import static insuranceCompany.application.global.utility.TargetInfoCalculator.targetSexCalculator;

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
    String command;
    private Scanner sc;
    private InputValidation input;
    private CriterionSetUtil csu;

    private HealthContract healthContract;
    private FireContract fireContract;
    private CarContract carContract;
    private Customer customer;
    private Insurance insurance;

    private Employee employee = new Employee();

    public GuestViewLogic() {
        this.sc = new Scanner(System.in);
        this.input = new InputValidation();
        this.csu = new CriterionSetUtil();
    }

    @Override
    public void showMenu() {
        createMenuAndClose("보험가입희망자메뉴", "보험가입");
    }

    @Override
    public void work(String command) {

        try {
            switch (command) {
                case "1":
                    selectInsurance();
                    break;
                case "":
                    throw new InputException.InputNullDataException();
                default:
                    throw new InputException.InvalidMenuException();
            }
        } catch (InputException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectInsurance() throws SQLException {
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
                System.out.println("가입할 보험상품의 번호를 입력하세요. \t(0: 뒤로가기)");
                command = sc.nextLine();
                if (command.equals("0")) {
                    break;
                }
                if (command.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                insuranceDao = new InsuranceDaoImpl();
                insurance = insuranceDao.read(Integer.parseInt(command));
                if (insurance != null && insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println("<< 상품안내 >>\n" + insurance.getDescription() + "\n<< 보장내역 >>");
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.println(guarantee);
                    }
                    decideSigning();
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

    private void decideSigning() {
        while (true) {
            try {
                createMenu("해당 보험상품을 가입하시겠습니까?", "가입", "취소");
                command = sc.nextLine();

                switch (command) {
                    // 가입
                    case "1":
                        inputCustomerInfo();
                        break;
                    // 취소
                    case "2":
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void inputCustomerInfo() {
        String question;

        question = "이름을 입력해주세요.";
        String name = input.validateDistinctFormat(question, 1);

        question = "주민번호를 입력해주세요. (______-*******)";
        String ssn = input.validateDistinctFormat(question, 2);

        question = "연락처를 입력해주세요. (0__-____-____)";
        String phone = input.validateDistinctFormat(question, 3);

        question = "주소를 입력해주세요.";
        String address = input.validateStringFormat(question);

        question = "이메일을 입력해주세요. (_____@_____.___)";
        String email = input.validateDistinctFormat(question, 4);

        question = "직업을 입력해주세요.";
        String job = input.validateStringFormat(question);

        customer = employee.inputCustomerInfo(name, ssn, phone, address, email, job);

        switch (insurance.getInsuranceType()) {
            case HEALTH:
                inputHealthInfo();
                break;
            case FIRE:
                inputFireInfo();
                break;
            case CAR:
                inputCarInfo();
                break;
        }
    }

    private void inputHealthInfo() {
        int riskCount = 0;
        String question;
        String diseaseDetail;

        question = "키를 입력해주세요. (단위: cm)";
        int height = input.validateIntFormat(question);

        question = "몸무게를 입력해주세요. (단위: kg)";
        int weight = input.validateIntFormat(question);

        question = "음주 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isDrinking = input.validateBooleanFormat(question);
        if (isDrinking)
            riskCount++;

        question = "흡연 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isSmoking = input.validateBooleanFormat(question);
        if (isSmoking)
            riskCount++;

        question = "운전 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isDriving = input.validateBooleanFormat(question);
        if (isDriving)
            riskCount++;

        question = "위험 취미 활동 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isDangerActivity = input.validateBooleanFormat(question);
        if (isDangerActivity)
            riskCount++;

        question = "약물 복용 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isTakingDrug = input.validateBooleanFormat(question);
        if (isTakingDrug)
            riskCount++;

        question = "질병 이력 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isHavingDisease = input.validateBooleanFormat(question);
        if (isHavingDisease)
            riskCount++;

        if (isHavingDisease) {
            question = "질병에 대한 상세 내용를 입력해주세요.";
            diseaseDetail = input.validateStringFormat(question);
        } else
            diseaseDetail = null;

        int age = targetAgeCalculator(customer.getSsn());
        boolean sex = targetSexCalculator(customer.getSsn());

        int premium = employee.planHealthInsurance(csu.setTargetAge(age), sex, csu.setRiskCriterion(riskCount), insurance);

        healthContract = employee.inputHealthInfo(height, weight, isDrinking, isSmoking, isDriving, isDangerActivity,
                                                isTakingDrug, isHavingDisease, diseaseDetail, insurance.getId(), premium);
        signContract(healthContract);
    }

    private void inputFireInfo() {
        String question;
        BuildingType buildingType;

        while(true) {
            try {
                createMenu("건물종류를 입력해주세요.","주거용", "상업용", "산업용", "공업용");
                command  = sc.nextLine();
                switch (command) {
                    case "1":
                        buildingType = RESIDENTIAL;
                        break;
                    case "2":
                        buildingType = COMMERCIAL;
                        break;
                    case "3":
                        buildingType = INDUSTRIAL;
                        break;
                    case "4":
                        buildingType = INSTITUTIONAL;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                break;
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }

        question = "건물면적을 입력해주세요. (단위: m^2 )";
        int buildingArea = input.validateIntFormat(question);

        question = "담보 금액을 입력해주세요. (단워: 원)";
        Long collateralAmount = input.validateLongFormat(question);

        question = "자가 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isSelfOwned = input.validateBooleanFormat(question);

        question = "실거주 여부를 입력해주세요. \n1. 예  2. 아니요";
        boolean isActualResidence = input.validateBooleanFormat(question);

        int premium = employee.planFireInsurance(buildingType, csu.setCollateralAmountCriterion(collateralAmount), insurance);

        fireContract = employee.inputFireInfo(buildingType, buildingArea, collateralAmount, isSelfOwned, isActualResidence, insurance.getId(), premium);
        signContract(fireContract);
    }

    private void inputCarInfo() {
        String question;
        CarType carType;

        question = "차량번호를 입력해주세요.";
        String carNo = input.validateDistinctFormat(question, 5);

        while(true) {
            try {
                createMenu("차종을 입력해주세요.", "경형", "소형", "준중형", "중형", "준대형", "대형", "스포츠카");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        carType = URBAN;
                        break;
                    case "2":
                        carType = SUBCOMPACT;
                        break;
                    case "3":
                        carType = COMPACT;
                        break;
                    case "4":
                        carType = MIDSIZE;
                        break;
                    case "5":
                        carType = LARGESIZE;
                        break;
                    case "6":
                        carType = FULLSIZE;
                        break;
                    case "7":
                        carType = SPORTS;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                break;
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }

        question = "모델이름을 입력해주세요.";
        String modelName = input.validateStringFormat(question);

        question = "차량연식을 입력해주세요. (단위: 년)";
        int modelYear = input.validateIntFormat(question);

        question = "차량가액을 입력해주세요. (단위: 년)";
        Long value = input.validateLongFormat(question);

        int age = targetAgeCalculator(customer.getSsn());
        int premium = employee.planCarInsurance(csu.setTargetAge(age), csu.setValueCriterion(value), insurance);

        carContract = employee.inputCarInfo(carNo, carType, modelName, modelYear, value, insurance.getId(), premium);
        signContract(carContract);
    }

    private void signContract(Contract contract) {
        System.out.println("조회된 귀하의 보험료는 " + contract.getPremium() + "원 입니다.");
        while (true) {
            try {
                createMenu("보험가입을 신청하시겠습니까?", "가입", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        employee.registerContract(customer , contract, employee);
                        System.out.println(customer);
                        System.out.println(contract);
                        System.out.println("가입이 완료되었습니다.");
                        break;
                    case "2":
                        System.out.println("가입이 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
