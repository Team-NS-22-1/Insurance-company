package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.contract.BuildingType;
import insuranceCompany.application.domain.contract.CarType;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.*;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.exception.InputNullDataException;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.exception.NoResultantException;
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
import static insuranceCompany.application.global.constant.CommonConstants.ONE;
import static insuranceCompany.application.global.constant.ContractViewLogicConstants.*;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndLogoutAndInput;

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
        return createMenuAndLogoutAndInput(SALES_MENU, SALES_MENU_ELEMENTS);
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                case ONE -> selectInsurance();
                case "" -> throw new InputNullDataException();
            }
        } catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        } catch (MyIllegalArgumentException | InputNullDataException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectInsurance() throws IOException {
        ArrayList<Insurance> insurances = employee.readInsurances();
        if(insurances.size() == 0)
            throw new NoResultantException();
        while (true) {
            System.out.println(CONTRACT_INSURANCE_LIST);
            System.out.printf(CONTRACT_INSURANCES_CATEGORY_FORMAT, CONTRACT_INSURANCE_ID, CONTRACT_INSURANCE_NAME, CONTRACT_INSURANCE_TYPE);
            System.out.println(CONTRACT_SHORT_DIVISION);
            for (Insurance insurance : insurances) {
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION)
                    System.out.printf(CONTRACT_INSURANCES_VALUE_FORMAT, insurance.getId(), insurance.getName(), insurance.printInsuranceType());
//                    System.out.println("보험상품 번호: " + insurance.getId() + " | 보험상품 이름: " + insurance.getName() +
//                            "   \t보험상품 종류: " + insurance.getInsuranceType());
            }

            try {
                int insuranceId = 0;
                System.out.println(SALES_SELECT_INSURANCE_ID);
                insuranceId = (int) br.verifyRead(CONTRACT_INPUT_INSURANCE_ID, insuranceId);
                if(insuranceId == 0) break;

                insurance = employee.readInsurance(insuranceId);
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println(CONTRACT_INSURANCE_DESCRIPTION + insurance.getDescription() + CONTRACT_INSURANCE_CONTRACT_PERIOD + insurance.getContractPeriod() +
                            CONTRACT_YEAR_PARAMETER + CONTRACT_INSURANCE_PAYMENT_PERIOD + insurance.getPaymentPeriod() +CONTRACT_YEAR_PARAMETER + CONTRACT_INSURANCE_GUARANTEE);
                    System.out.printf(CONTRACT_GUARANTEES_CATEGORY_FORMAT, "",CONTRACT_INSURANCE_GUARANTEE_DESCRIPTION, CONTRACT_INSURANCE_GUARANTEE_AMOUNT);
                    System.out.println(CONTRACT_LONG_DIVISION);
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.printf(CONTRACT_GUARANTEES_VALUE_FORMAT, guarantee.getName(), guarantee.getDescription(), guarantee.getGuaranteeAmount());
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
        System.out.println(SALES_INSURANCE_DATAIL);
        switch (insurance.getInsuranceType()) {
            case HEALTH -> {
                System.out.printf(CONTRACT_HEALTH_DETAIL_CATEGORY_FORMAT, SALES_TARGET_AGE, SALES_TARGET_SEX, SALES_RISK_CRITERTION, SALES__PREMIUM);
                System.out.println(CONTRACT_SHORT_DIVISION);
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.printf(CONTRACT_HEALTH_DETAIL_VALUE_FORMAT, ((HealthDetail) insuranceDetail).printTargetAge(),
                            ((HealthDetail) insuranceDetail).getTargetSex(), ((HealthDetail) insuranceDetail).getRiskCriterion(),
                            insuranceDetail.getPremium());
                }
            }
            case FIRE -> {
                System.out.printf(CONTRACT_FIRE_DETAIL_CATEGORY_FORMAT, SALES_BUIILDING_TYPE, SALES_COLLATERAL_AMOUNT_CRITERION, SALES__PREMIUM);
                System.out.println(CONTRACT_SHORT_DIVISION);
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.printf(CONTRACT_FIRE_DETAIL_VALUE_FORMAT, ((FireDetail) insuranceDetail).printBuildingType(), ((FireDetail) insuranceDetail).printCollateralAmountCriterion(), insuranceDetail.getPremium());
                }
            }
            case CAR -> {
                System.out.printf(CONTRACT_CAR_DETAIL_CATEGORY_FORMAT, SALES_TARGET_AGE, SALES_VALUE_CRITERION, SALES__PREMIUM);
                System.out.println(CONTRACT_SHORT_DIVISION);
                for (InsuranceDetail insuranceDetail : insurance.getInsuranceDetailList()) {
                    System.out.printf(CONTRACT_CAR_DETAIL_VALUE_FORMAT, ((CarDetail) insuranceDetail).printTargetAge(),
                            (( CarDetail) insuranceDetail).printValueCriterion(), insuranceDetail.getPremium());
                }
            }
        }

        ContractDto contractDto =  switch (insurance.getInsuranceType()) {
            case HEALTH -> planHealthInsurance();
            case FIRE -> planFireInsurance();
            case CAR -> planCarInsurance();
        };

        int choice = br.verifyCategory(SALES_PROGRESS_CONTRACT, 2);
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
            case 2 -> System.out.println(SALES_CANCEL);
        }
    }

    private ContractDto planHealthInsurance() {
        int riskCount = 0, targetAge = 0;
        boolean targetSex, isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease;

        targetAge = (int) br.verifyRead(SALES_TARGET_AGE_QUERY, targetAge);
        targetSex =  br.verifyCategory(SALES_TARGET_SEX_QUERY, 2) == 1;
        isDrinking = br.verifyCategory(SALES_IS_DRINKING_QUERY, 2) == 1;
        if(isDrinking) riskCount++;
        isSmoking = br.verifyCategory(SALES_IS_SMOKING_QUERY, 2) == 1;
        if(isSmoking) riskCount++;
        isDriving = br.verifyCategory(SALES_IS_DRIVING_QUERY, 2) == 1;
        if(isDriving) riskCount++;
        isDangerActivity = br.verifyCategory(SALES_IS_DANGER_ACTIVITY_QUERY, 2) == 1;
        if(isDangerActivity) riskCount++;
        isTakingDrug = br.verifyCategory(SALES_IS_TAKING_DRUG_QUERY, 2) == 1;
        if(isTakingDrug) riskCount++;
        isHavingDisease = br.verifyCategory(SALES_IS_HAVING_DISEASE_QUERY, 2) == 1;
        if (isHavingDisease) riskCount++;

        int premium = employee.planHealthInsurance(targetAge, targetSex, riskCount, insurance);
        System.out.println(premiumInquiry(premium));

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

        buildingType = switch (br.verifyCategory(SALES_BUILDING_TYPE_QUERY, 4)) {
            case 1 -> COMMERCIAL;
            case 2 -> INDUSTRIAL;
            case 3 -> INSTITUTIONAL;
            case 4 -> RESIDENTIAL;
            default -> throw new IllegalStateException();
        };
        collateralAmount = (Long) br.verifyRead(SALES_COLLATRAL_AMOUNT_QUERY, collateralAmount);

        int premium = employee.planFireInsurance(buildingType, collateralAmount, insurance);
        System.out.println(premiumInquiry(premium));

        FireContractDto fireContractDto = new FireContractDto();
        fireContractDto.setBuildingType(buildingType)
                .setCollateralAmount(collateralAmount)
                .setPremium(premium);
        return fireContractDto;
    }

    private ContractDto planCarInsurance() {
        int targetAge = 0;
        Long value = 0L;

        targetAge = (int) br.verifyRead(SALES_TARGET_AGE_QUERY, targetAge);
        value = (Long) br.verifyRead(SALES_VALUE_QUERY, value);

        int premium = employee.planCarInsurance(targetAge, value, insurance);
        System.out.println(premiumInquiry(premium));

        CarContractDto carContractDto = new CarContractDto();
        carContractDto.setValue(value)
                    .setPremium(premium);
        return carContractDto;
    }

    private CustomerDto inputCustomerInfo() {
        int customerId = 0;
        CustomerDto customerDto = null;

        int choice = br.verifyCategory(SALES_IS_CONTRACTED_CUSTOMER, 2);
        switch (choice) {
            // 등록 고객
            case 1 -> {
                customerId = (int) br.verifyRead(SALES_INPUT_CUSTOMER_ID, customerId);
                customer = employee.readCustomer(customerId);
            }
            // 미등록 고객
            case 2 -> {
                String name = "", ssn = "", phone = "", address = "", email = "", job = "";
                System.out.println(SALES_INPUT_CUSTOMER_INFO);

                name = (String) br.verifySpecificRead(SALES_CUSTOMER_NAME_QUERY, name, "name");
                ssn = (String) br.verifySpecificRead(SALES_SSN_QUERY, ssn, "ssn");
                phone = (String) br.verifySpecificRead(SALES_PHONE_QUERY, phone, "phone");
                address = (String) br.verifyRead(SALES_ADDRESS_QUERY, address);
                email = (String) br.verifySpecificRead(SALES_EMAIL_QUERY, email, "email");
                job = (String) br.verifyRead(SALES_JOB_QUERY, job);

                customerDto = new CustomerDto(name, ssn, phone, address, email, job);
            }
        }
        return customerDto;
    }

    private ContractDto inputHealthInfo(ContractDto contractDto) {
        String diseaseDetail = "";
        int height = 0, weight = 0;
        System.out.println(SALES_INPUT_HEALTH_INFO);

        height = (int) br.verifyRead(SALES_HEIGHT_QUERY, height);
        weight = (int) br.verifyRead(SALES_WEGHIT_QUERY, weight);
        if (((HealthContractDto) contractDto).isHavingDisease())
            diseaseDetail = (String) br.verifyRead(SALES_DISEASE_DETAIL_QUERY, diseaseDetail);

        ((HealthContractDto) contractDto).setHeight(height)
                                        .setWeight(weight)
                                        .setDiseaseDetail(diseaseDetail)
                                        .setEmployeeId(employee.getId());
        return contractDto;
    }

    private ContractDto inputFireInfo(ContractDto contractDto) {
        int buildingArea = 0;
        boolean isSelfOwned, isActualResidence;

        buildingArea = (int) br.verifyRead(SALES_BUILDING_AREA_QUERY, buildingArea);
        isSelfOwned = br.verifyCategory(SALES_IS_SELF_OWNED_QUERY, 2) == 1;
        isActualResidence = br.verifyCategory(SALES_IS_ACTUAL_RESIDENCE_QUERY, 2) == 1;

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

        carNo = (String) br.verifySpecificRead(SALES_CAR_NO_QUERY, carNo, "carNo");
        carType = switch (br.verifyCategory(SALES_CAR_TYPE_QUERY, 7)) {
            case 1 -> URBAN;
            case 2 -> SUBCOMPACT;
            case 3 -> COMPACT;
            case 4 -> MIDSIZE;
            case 5 -> LARGESIZE;
            case 6 -> FULLSIZE;
            case 7 -> SPORTS;
            default -> throw new IllegalStateException();
        };
        modelName = (String) br.verifyRead(SALES_MADEL_NAME_QUERY, modelName);
        modelYear = (int) br.verifyRead(SALES_MODEL_YEAR_QUERY, modelYear);

        ((CarContractDto) contractDto).setCarNo(carNo)
                                    .setCarType(carType)
                                    .setModelName(modelName)
                                    .setModelYear(modelYear)
                                    .setEmployeeId(employee.getId());
        return  contractDto;
    }

    private void concludeContract(CustomerDto customerDto, ContractDto contractDto) {
        int choice = br.verifyCategory(SALES_CONCLUDE_CONTRACT, 2);
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
                System.out.println(SALES_CONCLUDE);
            }
            case 2 -> System.out.println(SALES_CANCEL);
        }
    }

    private UserDto signUp() {
        String userId = "", password = "";
        userId = (String) br.verifyRead(CONTRACT_USER_ID_QUERY, userId);
        password = (String) br.verifyRead(CONTRACT_USER_PASSWORD_QUERY, password);
        return new UserDto(userId, password, customer.getId());
    }
}
