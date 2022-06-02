package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.dao.contract.ContractDao;
import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDao;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFile;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Guarantee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;
import insuranceCompany.application.domain.payment.*;
import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.CustomMyBufferedReader;
import insuranceCompany.application.global.utility.DocUtil;
import insuranceCompany.application.login.User;
import insuranceCompany.application.viewlogic.dto.accidentDto.AccidentReportDto;
import insuranceCompany.application.viewlogic.dto.contractDto.ContractwithTypeDto;
import insuranceCompany.outerSystem.CarAccidentService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static insuranceCompany.application.domain.contract.BuildingType.*;
import static insuranceCompany.application.domain.contract.CarType.*;

import static insuranceCompany.application.global.constant.CustomerViewLogicConstants.*;
import static insuranceCompany.application.global.constant.ExceptionConstants.*;
import static insuranceCompany.application.global.utility.BankUtil.checkAccountFormat;
import static insuranceCompany.application.global.utility.BankUtil.selectBankType;
import static insuranceCompany.application.global.utility.CompAssignUtil.assignCompEmployee;
import static insuranceCompany.application.global.utility.CustomerInfoFormatUtil.isCarNo;
import static insuranceCompany.application.global.utility.CustomerInfoFormatUtil.isPhone;
import static insuranceCompany.application.global.utility.DocUtil.isExist;
import static insuranceCompany.application.global.utility.FormatUtil.*;
import static insuranceCompany.application.global.utility.MessageUtil.*;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CustomerViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class CustomerViewLogic implements ViewLogic {

    private ContractDao contractList;
    private InsuranceDao insuranceList;
    private AccidentDao accidentDao;
    private AccidentDocumentFileDao accidentDocumentFileDao;

    private Customer customer;
    private HealthContract healthContract;
    private FireContract fireContract;
    private CarContract carContract;
    private Scanner sc;
    private CustomMyBufferedReader br;
    private Insurance insurance;

    public CustomerViewLogic() {
        this.br = new CustomMyBufferedReader(new InputStreamReader(System.in));
        this.sc = new Scanner(System.in);
        this.customer = new Customer();
    }

    public CustomerViewLogic(Customer customer) {
        this.br = new CustomMyBufferedReader(new InputStreamReader(System.in));
        this.sc = new Scanner(System.in);
        this.customer = customer;
        setPayment();
    }

    @Override
    public void showMenu() {
        if (customer.getId() == 0)
            createMenuAndExit(CUSTOMERMENU, SIGNININSURANCE);
        else
            createMenuAndLogout(CUSTOMERMENU, SIGNININSURANCE, PAYPREMIUM, REPORTACCIDENT, CLAIMCOMPENSATION);
    }

    @Override
    public void work(String command) {
        try {
            if (customer.getId() == 0) {
                switch (command) {
                    case "1" -> selectInsurance();
                    case "" -> throw new InputNullDataException();
                }
            } else {
                switch (command) {
                    case "1" -> selectInsurance();
                    case "2" -> payPremiumButton();
                    case "3" -> reportAccident();
                    case "4" -> claimCompensation();
                    case "" -> throw new InputNullDataException();
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        } catch (MyIllegalArgumentException | NoResultantException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectInsurance() throws IOException {
        InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
        ArrayList<Insurance> insurances = insuranceDao.readAll();
        if(insurances.size() == 0)
            throw new NoResultantException();
        while (true) {
            System.out.println("<< 보험상품목록 >>");
            for (Insurance insurance : insurances) {
                if (insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION)
                    System.out.println("상품번호: " + insurance.getId() + " | 보험이름: " + insurance.getName() + "   \t보험종류: " + insurance.getInsuranceType());
            }

            try {
                System.out.println("가입할 보험상품의 번호를 입력하세요. \t(0: 뒤로가기)");
                int insuranceId = br.verifyMenu("보험상품 번호: ", insurances.size());
                if (insuranceId == 0) break;

                insuranceDao = new InsuranceDaoImpl();
                insurance = insuranceDao.read(insuranceId);
                if (insurance != null && insurance.getDevInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println("<< 상품안내 >>\n" + insurance.getDescription() + "\n<< 보장내역 >>");
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.println(guarantee);
                    }
                    decideSigning();
                }
                else {
                    throw new NoResultantException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            }
        }
    }

    private void decideSigning() throws IOException {
        int choice = br.verifyCategory("해당 보험상품을 가입하시겠습니까?\n1. 가입\n2. 취소\n", 2);
        if (choice == 1) {
            if (customer.getId() == 0)
                inputCustomerInfo();
            else {
                switch (insurance.getInsuranceType()) {
                    case HEALTH -> inputHealthInfo();
                    case FIRE -> inputFireInfo();
                    case CAR -> inputCarInfo();
                }
            }
        }
    }

    private void inputCustomerInfo() throws IOException {
        String name = "", ssn = "", phone = "", address = "", email = "", job = "";
        System.out.println("<<고객님의 개인정보를 입력해주세요.>>");

        name = (String) br.verifySpecificRead("이름: ", name, "name");
        ssn = (String) br.verifySpecificRead("주민번호 (______-*******): ", ssn, "ssn");
        phone = (String) br.verifySpecificRead("연락처 (0__-____-____): ", phone, "phone");
        address = (String) br.verifyRead("주소: ", address);
        email = (String) br.verifySpecificRead("이메일 (_____@_____.___): ", email, "email");
        job = (String) br.verifyRead("직업: ", job);

        customer = customer.inputCustomerInfo(name, ssn, phone, address, email, job, customer);
        switch (insurance.getInsuranceType()) {
            case HEALTH -> inputHealthInfo();
            case FIRE -> inputFireInfo();
            case CAR -> inputCarInfo();
        }
    }

    private void inputHealthInfo() {
        int riskCount = 0, height = 0, weight = 0;
        String diseaseDetail = "";
        boolean isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease;
        System.out.println("<<고객님의 건강정보를 입력해주세요.>>");

        height = (int) br.verifyRead("키 (단위: cm): ", height);
        weight = (int) br.verifyRead("몸무게 (단위: kg): ", weight);
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
        if (isHavingDisease) {
            riskCount++;
            diseaseDetail = (String) br.verifyRead("질병에 대한 상세 내용를 입력해주세요.\n", diseaseDetail);
        }

        int premium = customer.inquireHealthPremium(customer.getSsn(), riskCount, insurance);
        healthContract = customer.inputHealthInfo(height, weight, isDrinking, isSmoking, isDriving, isDangerActivity,
                isTakingDrug, isHavingDisease, diseaseDetail, insurance.getId(), premium);
        signContract(healthContract);
    }

    private void inputFireInfo() {
        BuildingType buildingType;
        int buildingArea = 0;
        Long collateralAmount = 0L;
        boolean isSelfOwned, isActualResidence;

        buildingType = switch (br.verifyCategory("건물종류를 선택해주세요.\n1. 상업용\n 2. 산업용\n3. 기관용\n4. 거주용\n", 4)) {
            case 1 -> COMMERCIAL;
            case 2 -> INDUSTRIAL;
            case 3 -> INSTITUTIONAL;
            case 4 -> RESIDENTIAL;
            default -> throw new IllegalStateException();
        };
        buildingArea = (int) br.verifyRead("건물면적 (단위: m^2): ", buildingArea);
        collateralAmount = (Long) br.verifyRead("담보금액: (단워: 원): ", collateralAmount);
        isSelfOwned = br.verifyCategory("자가 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;
        isActualResidence = br.verifyCategory("실거주 여부를 입력해주세요. \n1. 예  2. 아니요\n", 2) == 1;

        int premium = customer.inquireFirePremium(buildingType, collateralAmount, insurance);
        fireContract = customer.inputFireInfo(buildingType, buildingArea, collateralAmount, isSelfOwned, isActualResidence, insurance.getId(), premium);
        signContract(fireContract);
    }

    private void inputCarInfo() throws IOException {
        CarType carType;
        String modelName = "", carNo = "";
        int modelYear = 0;
        Long value = 0L;

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
        value = (Long) br.verifyRead("차량가액 (단위: 원): ", value);

        int premium = customer.inquireCarPremium(customer.getSsn(), value, insurance);
        carContract = customer.inputCarInfo(carNo, carType, modelName, modelYear, value, insurance.getId(), premium);
        signContract(carContract);
    }

    private void signContract(Contract contract) {
        System.out.println("조회된 귀하의 보험료는 " + contract.getPremium() + "원 입니다.");

        int choice = br.verifyCategory("보험가입을 신청하시겠습니까?\n1. 가입\n2. 취소\n", 2);
        switch (choice) {
            case 1 -> {
                User user = null;
                if (customer.getId() == 0) {
                    user = signUp();
                }
                customer.registerContract(customer, contract, user);
                System.out.println(customer);
                System.out.println(contract);
                System.out.println("가입이 완료되었습니다.");
            }
            case 2 -> System.out.println("가입이 취소되었습니다.");
        }
    }

    private User signUp() {
        String userId = "", password = "";
        userId = (String) br.verifyRead("아이디: ", userId);
        password = (String) br.verifyRead("비밀번호: ", password);
        User user = customer.createAccount(userId, password);
        return user;
    }



    private void claimCompensation() {
            try {
                Accident accident = selectAccident();
                if (accident == null)
                    return;
                showRequiredDocFile(accident);
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
    }

    private void showRequiredDocFile(Accident accident) {
        AccidentType accidentType = accident.getAccidentType();
        switch (accidentType) {
            case CARACCIDENT -> showCarAccidentDoc(accident);
            case FIREACCIDENT -> showFireAccidentDoc(accident);
            case INJURYACCIDENT ->showInjuryAccidentDoc(accident);
            case CARBREAKDOWN -> throw new MyIllegalArgumentException(CARBREAKDOWNEXCEPTION);
        }
    }
    private void showCommonAccidentDoc(Accident accident) {

        submitDocFile(accident,AccDocType.CLAIMCOMP);
    }

    private void submitMedicalConfirmation(Accident accident) {
        submitDocFile(accident, AccDocType.MEDICALCERTIFICATION); // 진단서 제출
        submitDocFile(accident, AccDocType.CONFIRMADMISSIONDISCHARGE); // 입퇴원 확인서 제출
    }

    private void submitFile(Accident accident, AccDocType accDocType) {
        while (true) {
            try {
                String uploadMedicalCertification = "";
                isExist(accident,accDocType);
                uploadMedicalCertification = (String) br.verifyRead(getSubmitDocQuery(accDocType.getDesc()),uploadMedicalCertification);
                if (uploadMedicalCertification.equals(YES)) {
                    AccidentDocumentFile accidentDocumentFile = customer.claimCompensation(accident, new AccidentDocumentFile().setAccidentId(accident.getId())
                            .setType(accDocType));
                    if (accidentDocumentFile == null) {
                        System.out.println(getSubmitDocCancel(accDocType.getDesc()));
                        break;
                    }
                    break;
                } else if (uploadMedicalCertification.equals(NO)) {
                    break;
                }
            } catch (MyFileException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void submitDocFile(Accident accident, AccDocType accDocType) {
        System.out.println(getSubmitDoc(accDocType.getDesc()));

        while (true) {
            try {
                String medicalCertification = "";
                medicalCertification = (String) br.verifyRead(getDownloadDocExQuery(accDocType.getDesc()), medicalCertification);
                if (medicalCertification.equals(YES)) {
                    DocUtil instance = DocUtil.getInstance();
                    String dir = getExDirectory(accDocType.getDesc());
                    instance.download(dir);
                    break;
                } else if (medicalCertification.equals(NO)) {
                    break;
                }
            } catch (MyFileException e) {
                System.out.println(e.getMessage());
            }
        }
        submitFile(accident,accDocType);
    }

    private void showCarAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitMedicalConfirmation(accident);
        submitDocFile(accident,AccDocType.CARACCIDENTFACTCONFIRMATION); // 교통사고 사실 확인원
        submitDocFile(accident,AccDocType.PAYMENTRESOLUTION); // 자동차 보험금 지급 결의서

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.MEDICALCERTIFICATION, AccDocType.CONFIRMADMISSIONDISCHARGE
                , AccDocType.CARACCIDENTFACTCONFIRMATION, AccDocType.PAYMENTRESOLUTION);

        isFinishedClaimComp(accident, submitted);
    }

    private void isFinishedClaimComp(Accident accident, boolean submitted) {

        if (submitted) {
            connectCompEmployee(accident);
        } else {
            System.out.println("추후에 미제출한 정보들을 제출해주세요.");
        }
    }

    private void connectCompEmployee(Accident accident) {

        Employee compEmployee = assignCompEmployee();
        System.out.println(compEmployee.print());


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead(CHANGECOMPQUERY,rtVal);
            if (rtVal.equals(YES)) {
                String reasons = "";
                reasons=(String)br.verifyRead(INPUTCOMPLAIN,reasons);
                compEmployee = this.customer.changeCompEmp(reasons,compEmployee);
                System.out.println(compEmployee.print());
                System.out.println(SUCESSCHANGECOMPEMPLOYEE);
                break;
            }else if(rtVal.equals(NO)){
                break;
            }
        }
        accident.setEmployeeId(compEmployee.getId());
        accidentDao = new AccidentDaoImpl();
        accidentDao.updateCompEmployeeId(accident);
    }

    private void showFireAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitFile(accident, AccDocType.PICTUREOFSITE); // 사고현장사진
        submitDocFile(accident, AccDocType.REPAIRESTIMATE); // 수리비 견적서
        submitDocFile(accident,AccDocType.REPAIRRECEIPT); // 수리비 영수증

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.PICTUREOFSITE, AccDocType.REPAIRESTIMATE, AccDocType.REPAIRRECEIPT);
        isFinishedClaimComp(accident, submitted);
    }

    private void showInjuryAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitMedicalConfirmation(accident);

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.MEDICALCERTIFICATION, AccDocType.CONFIRMADMISSIONDISCHARGE);
        isFinishedClaimComp(accident, submitted);
    }

    private boolean isAllDocSubmitted(Accident accident, AccDocType ... accDocTypes) {
        Map<AccDocType, AccidentDocumentFile> accDocFileList = accident.getAccDocFileList();
        for (AccDocType accDocType : accDocTypes) {
            if (!accDocFileList.containsKey(accDocType)) {
                return false;
            }
        }
        return true;
    }


    private Accident selectAccident() {
        Accident retAccident = null;
        int accidentId = 0;
        while (true) {
            accidentDao = new AccidentDaoImpl();
            List<Accident> accidents = accidentDao.readAllByCustomerId(customer.getId());
            for (Accident accident : accidents) {
                accident.printForCustomer();
            }
            try {

                System.out.println(ZEROMESSAGE);
                System.out.println(EXITMESSAGE);
                accidentId = (int) br.verifyRead(INPUTACCIDENTID, accidentId);

                if (accidentId == 0) {
                    break;
                }
                accidentDao = new AccidentDaoImpl();
                retAccident = accidentDao.read(accidentId);
                if(retAccident.getCustomerId() != this.customer.getId())
                    throw new MyInvalidAccessException(INPUTDATEONLIST);
                break;
            } catch (InputException | MyIllegalArgumentException | MyInvalidAccessException e) {
                System.out.println(e.getMessage());
            }
        }
        if (accidentId != 0) {
            try{
                accidentDocumentFileDao = new AccidentDocumentFileDaoImpl();
                List<AccidentDocumentFile> files = accidentDocumentFileDao.readAllByAccidentId(retAccident.getId());
                for (AccidentDocumentFile file : files) {
                    retAccident.getAccDocFileList().put(file.getType(),file);
                }
            }catch (MyIllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
        return retAccident;
    }

    private void reportAccident()  {
        AccidentType accidentType = selectAccidentType();
        if (accidentType != null)
            inputAccidentInfo(accidentType);
    }

    private void inputAccidentInfo(AccidentType selectAccidentType) {

        AccidentReportDto accidentReportDto = inputDetailAccidentInfo(inputCommonAccidentInfo(selectAccidentType));
        Accident accident = customer.reportAccident(accidentReportDto);
        accident.printForCustomer();

        AccidentType accidentType = accident.getAccidentType();
        if (accidentType == AccidentType.CARACCIDENT) {
            if (((CarAccident) accident).isRequestOnSite()) {
                CarAccidentService.connectWorker();
            }
        } else if (accidentType == AccidentType.CARBREAKDOWN) {
            CarAccidentService.connectWorker();
        }

    }

    private AccidentReportDto inputDetailAccidentInfo(AccidentReportDto accidentReportDto) {
        AccidentType accidentType = accidentReportDto.getAccidentType();
        return switch (accidentType) {
            case INJURYACCIDENT ->inputInjuryAccidentInfo(accidentReportDto);
            case CARBREAKDOWN -> inputCarBreakdown(accidentReportDto);
            case CARACCIDENT -> inputCarAccident(accidentReportDto);
            case FIREACCIDENT -> inputFireAccidentInfo(accidentReportDto);
        };
    }

    private AccidentReportDto inputPlaceAddress(AccidentReportDto accidentReportDto) {
        String placeAddress = "";
        placeAddress= (String) br.verifyRead(ADDRESS, placeAddress);
        return accidentReportDto.setPlaceAddress(placeAddress);
    }

    private AccidentReportDto inputCarNo(AccidentReportDto accidentReportDto) {
        String carNo = "";
        while (true) {
            try {

                carNo = (String) br.verifyRead(CARNOEX, carNo);
                if (isCarNo(carNo))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return accidentReportDto.setCarNo(carNo);
    }

    private AccidentReportDto inputCarAccident(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String opposingDriverPhone = "";
        while (true) {
            try{

                opposingDriverPhone= (String) br.verifyRead(OPOSSINGPHONE, opposingDriverPhone);
                if(isPhone(opposingDriverPhone))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        accidentReportDto.setOpposingDriverPhone(opposingDriverPhone);


        String isRequestOnSite = "";
        while (true) {
            isRequestOnSite= (String) br.verifyRead(REQUESTONSITE, isRequestOnSite);
            isRequestOnSite = isRequestOnSite.toUpperCase();
            if(isRequestOnSite.equals(YES)||isRequestOnSite.equals(NO))
                break;
        }
        boolean request = false;
        if (isRequestOnSite.equals(YES)) {
            request = true;
        }

        return accidentReportDto.setRequestOnSite(request);
    }

    private AccidentReportDto inputCarBreakdown(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String symptom = "";
        symptom= (String) br.verifyRead(SYMPTOM, symptom);
        return accidentReportDto.setSymptom(symptom);
    }

    private AccidentReportDto inputInjuryAccidentInfo(AccidentReportDto accidentReportDto) {
        String injurySite = "";

        injurySite = (String)br.verifyRead(INJURYSITE,injurySite);
        return accidentReportDto.setInjurySite(injurySite);
    }

    private AccidentReportDto inputFireAccidentInfo(AccidentReportDto accidentReportDto) {
        return inputPlaceAddress(accidentReportDto);
    }

    private AccidentReportDto inputCommonAccidentInfo(AccidentType selectAccidentType) {

        int year = 0; int month = 0; int day = 0;int  hour = 0; int min = 0;
        System.out.println(REPORTACCIDENTINFO);
        System.out.println(INPUTACCIDENTDATE);

        year = validateYear(year);
        month = vadliateMonth(month);
        day = validateDay(day);

        hour = validateHour(hour);
        min = validateMinute(min);

        String M = dateFormatter(month);
        String d = dateFormatter(day);
        String h = dateFormatter(hour);
        String m = dateFormatter(min);

        String dateFormat = year+"/"+M+"/"+d+" "+h+":"+m;

        LocalDateTime accidentDate = LocalDateTime.parse(dateFormat, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

        return new AccidentReportDto().setAccidentType(selectAccidentType)
                .setDateOfAccident(accidentDate)
                .setDateOfReport(LocalDateTime.now());
    }

    private int validateYear(int year) {
        while (true) {
            try {
                year = (int) br.verifyRead(YEAREX, year);
                if (isYear(Integer.toString(year)))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return year;
    }

    private int vadliateMonth(int month) {
        while (true) {
            try{

                month = (int) br.verifyRead(MONTH, month);
                if(isMonth(month))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            }catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return month;
    }

    private int validateDay(int day) {
        while (true) {
            try {
                day = (int) br.verifyRead(DAY, day);
                if(isDay(day))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }

        }
        return day;
    }

    private int validateHour(int hour) {
        while (true) {
            try {

                hour = (int) br.verifyRead(HOUR, hour);
                if (isHour(hour))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return hour;
    }

    private int validateMinute(int min) {
        while (true) {
            try{

                min = (int) br.verifyRead(MINUTE, min);
                if(isMinute(min))
                    break;
                throw new MyInadequateFormatException(INPUTWRONGFORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return min;
    }

    private String dateFormatter(int time) {
        String date = Integer.toString(time);
        if (time < 10) {
            date = ZERO+time;
        }
        return date;
    }

    private AccidentType selectAccidentType() {
        contractList = new ContractDaoImpl();
        List<ContractwithTypeDto> contractTypes = contractList.findAllContractWithTypeByCustomerId(this.customer.getId());
        AccidentType accidentType = null;
            while (true) {
                try {
                    int insType = 0;
                    String query = createMenuAndClose(ACCIDENTMENU, CARACCIDENT, CARBREAKDOWN, INJURYACCIDENT, FIREACCIDENT);
                    insType = br.verifyMenu("", 4);

                    switch (insType) {
                        case 1 -> accidentType = AccidentType.CARACCIDENT;
                        case 2 -> accidentType = AccidentType.CARBREAKDOWN;
                        case 3 -> accidentType = AccidentType.INJURYACCIDENT;
                        case 4 -> accidentType = AccidentType.FIREACCIDENT;
                        case 0 -> accidentType = null;
                    }

                    if (accidentType == null)
                        break;

                    for (ContractwithTypeDto contractType : contractTypes) {
                        if(isValidateReportAccident(accidentType,contractType.getInsuranceType()))
                            return accidentType;
                    }
                    throw new MyInvalidAccessException(NOINSURANCEABOUNTACCIDENT);
                } catch (MyInvalidAccessException e) {
                    System.out.println(e.getMessage());
                }
            }
        return accidentType;
    }

    private boolean isValidateReportAccident(AccidentType accidentType, InsuranceType insuranceType) {
        return switch (accidentType) {
            case CARACCIDENT, CARBREAKDOWN -> insuranceType == InsuranceType.CAR;
            case FIREACCIDENT -> insuranceType == InsuranceType.FIRE;
            case INJURYACCIDENT -> insuranceType == InsuranceType.HEALTH;
        };
    }

    // customer ID를 입력하여 customerViewLogic에서 진행되는 작업에서 사용되는 고객 정보를 불러온다.
    public void setPayment() {
        try {
            customer.readPayments();
        } catch (MyIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 보험료 납입 버튼을 클릭했을 경우, 그 이후 작업들에 대해서 보여준다
    // 이후 진행될 작업으로 보험료를 납입할 계약을 선택하고, 해당 계약으로 즉시 결제를 할지, 계약에 기존에 등록된 결제수단을 등록할지,
    // 고객에게 새로운 결제 수단을 추가할지 정할 수 있다.
    private void payPremiumButton() {
        while (true) {
            Contract contract = selectContract();
            if (contract == null) {
                System.out.println(CANCEL);
                return;
            }
            loop : while (true) {
                try {
                    createMenu(PAYHEAD, DOPAY, SETPAMENT, ADDACCOUNTMENUHEAD);
                    System.out.println(ZEROMESSAGE);
                    System.out.println(EXITMESSAGE);
                    String next = sc.next();
                    switch (next) {
                        case "1":
                            payLogic(contract);
                            break;
                        case "2":
                            setPaymentOnContract(contract);
                            break;
                        case "3":
                            addNewPayment();
                            break;
                        case ZERO:
                            break loop;
                        case "exit":
                            throw new MyCloseSequence();
                        default:
                            throw new InputInvalidMenuException();
                    }
                } catch (InputInvalidMenuException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 고객이 보험료 납입 버튼을 클릭한 이후 사용할 계약을 선택하는 기능이다.
    // 계약의 ID를 입력하는 것으로 이후 작업이 진행될 계약 객체를 선택한다.
    private Contract selectContract(){
        Contract contract = null;
        List<Contract> contracts = customer.readContracts();
        while (true) {
            try {
                try {
                    System.out.println(CONTRACTLIST);
                    for (Contract con : contracts) {
                        showContractInfoForPay(con);
                    }
                    System.out.println(ZEROMESSAGE);
                    String key = sc.next();
                    if (key.equals(ZERO))
                        break;
                    contractList = new ContractDaoImpl();
                    contract = contractList.read(Integer.parseInt(key));
                    if (contract.getCustomerId() != this.customer.getId()) {
                        throw new MyInvalidAccessException(INPUTDATEONLIST);
                    }

                    break;
                } catch (MyIllegalArgumentException | MyInvalidAccessException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    throw new InputInvalidDataException(e);
                }
            } catch (InputInvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
        return contract;
    }

    // 보험료 납부를 위한 계약 정보를 출력하는 기능
    public void showContractInfoForPay(Contract contract) {

        insuranceList = new InsuranceDaoImpl();
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        StringBuilder sb = new StringBuilder();
        sb.append("[ID]").append(" : ").append(contract.getId())
                .append(" 이름 : ").append(insurance.getName()).append(" 보험료 : ").append(contract.getPremium())
                .append("\n");
        System.out.println(sb.toString());
    }


    // 계약을 선택한 이후 즉시 결제를 시도하는 기능.
    // 해당 계약에 결제 수단이 등록되지 않았다면 결제 수단 등록을 진행한다.
    private void payLogic(Contract contract) {
        if (contract.getPaymentId() == 0) {
            System.out.println(NOPAYMENTONCONTRACT);
            setPaymentOnContract(contract);
        }else{
            pay(contract);
        }
    }

    // 계약에 대해서 보험료를 납부하는 기능
    private void pay(Contract contract) {
        customer.pay(contract);
    }


    // 고객에게 등록된 결제 수단들을 불러온다.
    private void setPaymentOnContract(Contract contract) {
        ArrayList<Payment> paymentList = this.customer.getPaymentList();
        if (paymentList.size() == 0) {
            System.out.println(NOPAYMENTONCUSTOMER);
            addNewPayment();
            return;
        }
        while (true) {
            try{

                try {
                    for (Payment payment : paymentList) {
                        System.out.println(payment);
                    }
                    System.out.println(ZEROMESSAGE);
                    System.out.println(EXITMESSAGE);
                    String key = sc.next();
                    key = key.toUpperCase();
                    if (key.equals(ZERO))
                        return;
                    if(key.equals(EXIT))
                        throw new MyCloseSequence();
                    int paymentId = Integer.parseInt(key);
                    this.customer.registerPayment(contract, paymentId);
                    break;
                } catch (NumberFormatException e) {
                    throw new InputInvalidDataException(INPUTWRONGFORMAT, e);
                }
                } catch (MyIllegalArgumentException |InputInvalidDataException| MyInvalidAccessException  e ) {
                    System.out.println(e.getMessage());
                }
            }
        }

    // 고객에게 새로운 결제수단을 추가하는 기능. 카드와 계좌의 정보를 추가할 수 있다.
    public void addNewPayment() {
        loop :while (true) {
            try {

                createMenu(ADDACCOUNTMENUHEAD, REGISTERCARD, REGISTERACCOUNT);
                System.out.println(ZEROMESSAGE);
                System.out.println(EXITMESSAGE);
                switch (sc.next()) {
                    case "1":
                        createCard();
                        break;
                    case "2":
                        createAccount();
                        break;
                    case ZERO:
                        break loop;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InputInvalidMenuException();
                }
            } catch (InputInvalidMenuException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    // 결제수단 중 카드를 새로 추가하는 기능
    private void createCard() {
        PaymentDto card = new PaymentDto();
        while (true) {
            try {
                System.out.println(REGISTERCARD);
                System.out.println(SELECTCARDTYPE);
                CardType cardType = selectCardType();
                if(cardType==null)
                    return;

                while (true) {
                    try {
                        System.out.println(CARDNOEX);
                        String cardNo = validateCardNoFormat(sc.next());
                        System.out.println(CVCEX);
                        String cvc = validateCVCFormat(sc.next());
                        System.out.println(EXPIRYDATE);
                        System.out.print(MONTH);
                        int month = validateMonthFormat(sc.nextInt());
                        System.out.print(YEAREX);
                        int year = validateYearFormat(sc.nextInt());
                        LocalDate expireDate = createExpireDate(month, year);

                        card.setCardNo(cardNo)
                                .setCvcNo(cvc)
                                .setCardType(cardType)
                                .setExpiryDate(expireDate)
                                .setCustomerId(this.customer.getId())
                                .setPayType(PayType.CARD);
                        break;
                    } catch ( MyInadequateFormatException e) {
                        System.out.println(INPUTWRONGFORMAT);
                    }
                }


                while (true) {
                    System.out.println(REGISTERCARDINFO);
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals(NO)) {
                        System.out.println(CANCELREGISTERPAYMENT);
                        return;
                    } else if (result.equals(YES))
                        break;
                    else
                        throw new InputInvalidDataException();
                }
                break;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | MyInadequateFormatException | InputInvalidDataException e) {
                System.out.println(INPUTWRONGFORMAT);
            }
        }
        customer.addPayment(card);
        System.out.println(SUCEESSREGISTERPAYMENT);

    }

    // 카드 결제 수단 추가 중 카드사를 선택하는 기능
    private CardType selectCardType() {
        CardType[] values = CardType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println(ZEROMESSAGE);
        System.out.println(SELECTCARDTYPENO);
        String key = sc.next();
        if(key.equals(ZERO))
            return null;
        return values[Integer.parseInt(key)-1];
    }
    // 카드 결제 수단을 추가하는 과정에서 만료기간 중 연도를 형식에 맞게 입력했는지 검증하는 기능
    private int validateYearFormat(int year) {
        if(!isYear(Integer.toString(year)))
            throw new MyInadequateFormatException();
        return year;
    }
    // 카드 결제 수단을 추가하는 과정에서 만료기간 중 달를 형식에 맞게 입력했는지 검증하는 기능
    private int validateMonthFormat(int month) {
        if(!isMonth(month))
            throw new MyInadequateFormatException();
        return month;
    }

    // 카드 결제 수단을 추가하는 과정에서 입력한 달과 연을 통해서 저장하기 위한 LocalDate 객체를 생성하는 기능
    private LocalDate createExpireDate(int month, int year) {
        String mm = month < 10 ? ZERO+month : String.valueOf(month);
        String date = "01/"+mm+"/"+year;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATEFORMATE));
    }
    // 카드 결제 수단 추가 과정에서 카드 번호 형식에 대해서 검증하는 기능
    private String validateCardNoFormat(String cardNo) {
        if(!isCardNo(cardNo))
            throw new MyInadequateFormatException();
        return cardNo;
    }
    // 카드 결제 수단 추가 과정에서 CVC 번호 형식에 대해서 검증하는 기능
    private String validateCVCFormat(String cvc) {
        if(!isCVC(cvc))
            throw new MyInadequateFormatException();
        return cvc;
    }



    // 계좌 결제 수단을 추가하는 기능
    private void createAccount() {
        PaymentDto account = new PaymentDto();
        loop: while (true) {
            try{
                System.out.println(REGISTERACCOUNT);
                System.out.println(SELECTBANK);
                BankType bankType = selectBankType(br);
                if(bankType==null)
                    return;
                while (true) {
                    try {
                        System.out.println(showAccountNoEX(bankType.getFormat()));
                        System.out.println(ZEROMESSAGE);
                        String command = sc.next();
                        if (command.equals(ZERO)) {
                            continue loop;
                        }
                        String accountNo = checkAccountFormat(bankType,command);


                        account.setBankType(bankType)
                                .setAccountNo(accountNo)
                                .setCustomerId(this.customer.getId())
                                .setPayType(PayType.ACCOUNT);
                        break;
                    } catch (MyInadequateFormatException e) {
                        System.out.println(e.getMessage());
                    }
                }

                while (true) {
                    System.out.println(REGISTERACCOUNTINFO);
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals(YES)) {
                        System.out.println(CANCELREGISTERPAYMENT);
                        return;
                    } else if (result.equals(NO))
                        break;
                    else
                        throw new InputInvalidDataException();
                }

                break;
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException| MyInadequateFormatException e) {
                System.out.println(INPUTWRONGFORMAT);
            }
        }
        customer.addPayment(account);
        System.out.println(SUCEESSREGISTERPAYMENT);
    }

    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }
}
