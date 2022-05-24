package application.viewlogic;

import application.viewlogic.dto.accidentDto.AccidentReportDto;
import domain.accident.*;
import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocFileListImpl;
import domain.accident.accDocFile.AccDocType;
import domain.complain.Complain;
import domain.complain.ComplainList;
import domain.complain.ComplainListImpl;
import domain.contract.Contract;
import domain.contract.ContractList;
import domain.contract.ContractListImpl;
import domain.customer.Customer;
import domain.customer.CustomerList;
import domain.customer.CustomerListImpl;
import domain.employee.Employee;
import domain.employee.EmployeeList;
import domain.employee.EmployeeListImpl;
import domain.insurance.Insurance;
import domain.insurance.InsuranceList;
import domain.insurance.InsuranceListImpl;
import domain.payment.*;
import application.ViewLogic;
import exception.*;
import outerSystem.CarAccidentService;
import utility.CustomMyBufferedReader;
import utility.DocUtil;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static utility.CompAssignUtil.assignCompEmployee;
import static utility.CustomerInfoFormatUtil.isCarNo;
import static utility.CustomerInfoFormatUtil.isPhone;
import static utility.DocUtil.isExist;
import static utility.MessageUtil.*;
import static utility.FormatUtil.*;

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

    private ContractList contractList;
    private InsuranceList insuranceList;
    private CustomerList customerList;
    private PaymentList paymentList;
    private AccidentList accidentList;
    private AccDocFileList accDocFileList;
    private EmployeeList employeeList;
    private ComplainList complainList;
    private Customer customer;
    private Scanner sc;
    private CustomMyBufferedReader br;

    public CustomerViewLogic() {
    }

    public CustomerViewLogic(CustomerList customerList, ContractList contractList, InsuranceList insuranceList, PaymentList paymentList, AccidentList accidentList, AccDocFileList accDocFileList, EmployeeList employeeList, ComplainList complainList) {
        this.br = new CustomMyBufferedReader(new InputStreamReader(System.in));
        this.sc = new Scanner(System.in);
        this.contractList = contractList;
        this.insuranceList = insuranceList;
        this.customerList = customerList;
        this.paymentList = paymentList;
        this.accidentList = accidentList;
        this.accDocFileList = accDocFileList;
        this.employeeList = employeeList;
        this.complainList = complainList;
    }
    @Override
    public void showMenu() {
        createMenuAndExit("<<고객메뉴>>", "보험가입", "보험료납입", "사고접수", "보상금청구");
    }

    @Override
    public void work(String command) {
        switch (command) {
            case "2" :
                payPremiumButton();
                break;
            case "3":
                reportAccident();
                break;
            case "4":
                claimCompensation();
                break;
            default:
                throw new MyIllegalArgumentException();
        }

    }

    private void claimCompensation() {
        if (!login()) return;
        while (true) {
            try {
                Accident accident = selectAccident();
                if (accident == null)
                    return;
                showRequiredDocFile(accident);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

        }
    }

    private void showRequiredDocFile(Accident accident) {
        AccidentType accidentType = accident.getAccidentType();
        switch (accidentType) {
            case CARACCIDENT -> showCarAccidentDoc(accident);
            case FIREACCIDENT -> showFireAccidentDoc(accident);
            case INJURYACCIDENT ->showInjuryAccidentDoc(accident);
            case CARBREAKDOWN -> throw new IllegalArgumentException("자동차 고장은 보상금 청구가 되지 않습니다.");
        }
    }
    private void showCommonAccidentDoc(Accident accident) {

        submitDocFile(accident,AccDocType.CLAIMCOMP);

        System.out.println("계좌 번호를 입력해주세요");
        Account compAccount = createCompAccount();
        if (compAccount != null) {
            accident.setAccount(compAccount);
        }
    }

    private Account createCompAccount() {
        Account account = null;
        loop : while (true) {

            System.out.println("계좌 추가하기");
            System.out.println("은행사 선택하기");
            BankType bankType = selectBankType();
            if(bankType==null)
                break ;
            while (true) {
                try {
                    System.out.println("계좌 번호 입력하기 : (예시 -> " + bankType.getFormat() + ")");
                    System.out.println("0. 취소하기");
                    String command = sc.next();
                    if (command.equals("0")) {
                        continue loop;
                    }
                    String accountNo = checkAccountFormat(bankType,command);
                    account = new Account();
                    account.setBankType(bankType)
                            .setAccountNo(accountNo);
                    break loop;
                } catch (MyInadequateFormatException e) {
                    System.out.println("정확한 값을 입력해주세요");
                }
            }
        }
        return account;
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
                // TODO isExist라면 update를 해줘야 하는데..
                uploadMedicalCertification = (String) br.verifyRead(accDocType.getDesc()+"를 제출하시겠습니까?(Y/N)",uploadMedicalCertification);
                if (uploadMedicalCertification.equals("Y")) {
                    AccDocFile accDocFile = customer.claimCompensation(accident, new AccDocFile().setAccidentId(accident.getId())
                            .setType(accDocType));
                    if (accDocFile == null) {
                        System.out.println(accDocType.getDesc() + "의 제출을 취소하셨습니다.");
                        break;
                    }
                    accDocFileList.create(accDocFile);
                    break;
                } else if (uploadMedicalCertification.equals("N")) {
                    break;
                }
            } catch (MyFileException e) {
                System.out.println("파일 다운로드에 이상이 생겼습니다.");
            }
        }

    }

    private void submitDocFile(Accident accident, AccDocType accDocType) {
        System.out.println(accDocType.getDesc()+"를 제출해주세요");

        while (true) {
            try {
                String medicalCertification = "";
                medicalCertification = (String) br.verifyRead(accDocType.getDesc()+" 양식을 다운로드 받겠습니까?(Y/N)", medicalCertification);
                if (medicalCertification.equals("Y")) {
                    DocUtil instance = DocUtil.getInstance();
                    instance.download(accDocType);
                    break;
                } else if (medicalCertification.equals("N")) {
                    break;
                }
            } catch (MyFileException e) {
                System.out.println("파일 다운로드에 이상이 생겼습니다.");
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
        if (submitted && accident.getAccount() != null) {
            connectCompEmployee(accident);
        } else {
            System.out.println("추후에 미제출한 정보들을 제출해주세요.");
        }
    }

    private void connectCompEmployee(Accident accident) {

        Employee compEmployee = assignCompEmployee(employeeList, accidentList);
        System.out.println(compEmployee.print());


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead("보상처리담당자를 변경하실 수 있습니다. 하시겠습니까?(Y/N)",rtVal);
            if (rtVal.equals("Y")) {
                String reasons = "";
                reasons=(String)br.verifyRead("변경 사유를 입력해주세요",reasons);
                Complain complain = this.customer.changeCompEmp(reasons);
                complainList.create(complain);
                compEmployee = assignCompEmployee(employeeList, accidentList);
                System.out.println(compEmployee.print());
                System.out.println("보상처리담당자 변경이 완료되었습니다.");
                break;
            }else if(rtVal.equals("N")){
                break;
            }
        }
        accident.setEmployeeId(compEmployee.getId());
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
        Map<AccDocType, AccDocFile> accDocFileList = accident.getAccDocFileList();
        for (AccDocType accDocType : accDocTypes) {
            if (!accDocFileList.containsKey(accDocType)) {
                return false;
            }
        }
        return true;
    }


    private Accident selectAccident() {
        Accident retAccident = null;
        while (true) {
            List<Accident> accidents = accidentList.readAllByCustomerId(customer.getId());
            for (Accident accident : accidents) {
                accident.printForCustomer();
            }
            try {
                int accidentId = 0;
                System.out.println("0. 취소하기");
                System.out.println("exit. 종료하기");
                 accidentId = (int) br.verifyRead("사고 ID 입력 : ", accidentId);

                if (accidentId == 0) {
                    break;
                }

                 retAccident = accidentList.read(accidentId);
                 break;
            } catch (InputException | IllegalArgumentException e) {
                System.out.println("정확한 값을 입력해 주세요");
            }
        }
        return retAccident;
    }

    private void reportAccident()  {
        if (!login()) return;
        while (true) {
            try {
                AccidentType accidentType = selectAccidentType();
                if (accidentType == null)
                    return;
                inputAccidentInfo(accidentType);
                break;
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void inputAccidentInfo(AccidentType selectAccidentType) {

        AccidentReportDto accidentReportDto = inputDetailAccidentInfo(inputCommonAccidentInfo(selectAccidentType));
        Accident accident = customer.reportAccident(accidentReportDto);
        accidentList.create(accident);

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
         placeAddress= (String) br.verifyRead("사고 장소 : ", placeAddress);
         return accidentReportDto.setPlaceAddress(placeAddress);
    }

    private AccidentReportDto inputCarNo(AccidentReportDto accidentReportDto) {
        String carNo = "";
        while (true) {
            carNo= (String) br.verifyRead("차 번호 (ex : __-**_-**** (_ : 한글, * : 숫자)) : ", carNo);
            if(isCarNo(carNo))
                break;
            System.out.println("양식에 맞게 입력해주세요.");
        }
        return accidentReportDto.setCarNo(carNo);
    }

    private AccidentReportDto inputCarAccident(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String opposingDriverPhone = "";
        while (true) {
            opposingDriverPhone= (String) br.verifyRead("상대방 연락처 : ", opposingDriverPhone);
            if(isPhone(opposingDriverPhone))
                break;
            System.out.println("양식에 맞게 입력해주세요.");
        }
        accidentReportDto.setOpposingDriverPhone(opposingDriverPhone);


        String isRequestOnSite = "";
        while (true) {
            isRequestOnSite= (String) br.verifyRead("현장 출동 요청을 하시겠습니까? (Y/N) : ", isRequestOnSite);
            isRequestOnSite = isRequestOnSite.toUpperCase();
            if(isRequestOnSite.equals("Y")||isRequestOnSite.equals("N"))
                break;
        }
        boolean request = false;
        if (isRequestOnSite.equals("Y")) {
            request = true;
        }

        return accidentReportDto.setRequestOnSite(request);
    }

    private AccidentReportDto inputCarBreakdown(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String symptom = "";
        symptom= (String) br.verifyRead("고장 증상 : ", symptom);
        return accidentReportDto.setSymptom(symptom);
    }

    private AccidentReportDto inputInjuryAccidentInfo(AccidentReportDto accidentReportDto) {
        String injurySite = "";

        injurySite = (String)br.verifyRead("부상 부위 : ",injurySite);
        return accidentReportDto.setInjurySite(injurySite);
    }

    private AccidentReportDto inputFireAccidentInfo(AccidentReportDto accidentReportDto) {
        return inputPlaceAddress(accidentReportDto);
    }

    private AccidentReportDto inputCommonAccidentInfo(AccidentType selectAccidentType) {


        int year = 0; int month = 0; int day = 0;int  hour = 0; int min = 0;
        System.out.println("<< 사고 접수 정보 >> (exit: 시스템 종료)");
        System.out.println("사고 일시를 입력해주세요");

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
            year = (int) br.verifyRead("연도 (예시 : 20xx 4자리 전부 입력.): ", year);
            if(isYear(Integer.toString(year)))
                break;
            System.out.println("정확한 값을 입력해주세요.");
        }
        return year;
    }

    private int vadliateMonth(int month) {
        while (true) {
            month = (int) br.verifyRead("월 : ", month);
            if(isMonth(month))
                break;
            System.out.println("정확한 값을 입력해주세요.");
        }
        return month;
    }

    private int validateDay(int day) {
        while (true) {
            day = (int) br.verifyRead("일 : ", day);
            if(isDay(day))
                break;
            System.out.println("정확한 값을 입력해주세요.");
        }
        return day;
    }

    private int validateHour(int hour) {
        while (true) {
            hour = (int) br.verifyRead("시 : ", hour);
            if(isHour(hour))
                break;
            System.out.println("정확한 값을 입력해주세요.");
        }
        return hour;
    }

    private int validateMinute(int min) {
        while (true) {
            min = (int) br.verifyRead("분 : ", min);
            if(isMinute(min))
                break;
            System.out.println("정확한 값을 입력해주세요.");
        }
        return min;
    }

    private String dateFormatter(int time) {
        String date = Integer.toString(time);
        if (time < 10) {
            date = "0"+time;
        }
        return date;
    }

    private AccidentType selectAccidentType() {
            int insType = 0;
            createMenuAndClose("<< 사고 종류 선택 >>", "자동차 사고", "자동차 고장", "상해 사고", "화재 사고");
            insType = br.verifyMenu("", 4);


            return switch (insType) {
                case 1 -> AccidentType.CARACCIDENT;
                case 2 -> AccidentType.CARBREAKDOWN;
                case 3 -> AccidentType.INJURYACCIDENT;
                case 4 -> AccidentType.FIREACCIDENT;
                case 0 -> null;
                default -> throw new IllegalStateException("Unexpected value: " + insType);
            };

    }

    // customer ID를 입력하여 customerViewLogic에서 진행되는 작업에서 사용되는 고객 정보를 불러온다.
    public void login(int customerId) {
        this.customer  = customerList.read(customerId);
        try {
            List<Payment> payments = paymentList.findAllByCustomerId(customerId);
            this.customer.setPaymentList((ArrayList<Payment>) payments);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("결제 수단을 추가한 후, 계약에 결제 수단을 등록해주세요.");
        }
    }

    // 보험료 납입 버튼을 클릭했을 경우, 그 이후 작업들에 대해서 보여준다
    // 이후 진행될 작업으로 보험료를 납입할 계약을 선택하고, 해당 계약으로 즉시 결제를 할지, 계약에 기존에 등록된 결제수단을 등록할지,
    // 고객에게 새로운 결제 수단을 추가할지 정할 수 있다.
    private void payPremiumButton() {
        if (!login()) return;


        while (true) {
            Contract contract = selectContract();
            if (contract == null) {
                System.out.println("취소하였습니다.");
                return;
            }
            loop : while (true) {
                createMenu("결제 선택","결제하기","결제수단등록하기","결제수단추가하기");
                System.out.println("0. 취소하기");
                System.out.println("exit. 종료하기");
                String next = sc.next();
                switch (next) {
                    case "1" :
                        payLogic(contract);
                        break;
                    case "2" :
                        setPaymentOnContract(contract);
                        break;
                    case"3":
                        addnewPayment();
                        break;
                    case "0" :
                        break loop;
                    case "exit" :
                        throw new MyCloseSequence();
                    default:
                        System.out.println("입력 값을 다시 확인해주세요");
                }
            }
        }
    }

    private boolean login() {
        while (true) {
            try {
                System.out.println("고객 ID 입력 : ");
                System.out.println("0. 취소하기");
                String id = sc.next();
                if (id.equals("0")) {
                    return false;
                }
                login(Integer.parseInt(id));
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException| NumberFormatException e) {
                System.out.println("올바른 값을 입력해주세요");
            }
        }
        return true;
    }

    // 고객이 보험료 납입 버튼을 클릭한 이후 사용할 계약을 선택하는 기능이다.
    // 계약의 ID를 입력하는 것으로 이후 작업이 진행될 계약 객체를 선택한다.
    private Contract selectContract(){
        Contract contract = null;
        List<Contract> contracts = contractList.findAllByCustomerId(this.customer.getId());
        while (true) {
            try {
                System.out.println("가입된 계약 목록");
                for (Contract con : contracts) {
                    showContractInfoForPay(con);
                }
                System.out.println("0. 취소하기");
                String key = sc.next();
                if (key.equals("0"))
                    break;
                contract = contractList.read(Integer.parseInt(key));
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("제대로 된 ID를 입력해주세요");
            }
        }
        return contract;
    }

    // 보험료 납부를 위한 계약 정보를 출력하는 기능
    public void showContractInfoForPay(Contract contract) {
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        StringBuilder sb = new StringBuilder();
        contract.setPremium(insurance.getPremium());
        sb.append("[ID]").append(" : ").append(contract.getId())
                .append(" 이름 : ").append(insurance.getName()).append(" 보험료 : ").append(insurance.getPremium())
                .append("\n");
        System.out.println(sb.toString());
    }


    // 계약을 선택한 이후 즉시 결제를 시도하는 기능.
    // 해당 계약에 결제 수단이 등록되지 않았다면 결제 수단 등록을 진행한다.
    private void payLogic(Contract contract) {
        if (contract.getPayment() == null) {
            System.out.println("해당 계약에 대해 결제 수단 정보가 없습니다. 설정해주세요.");
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
            System.out.println("등록된 결제 수단이 없습니다. 먼저 결제수단을 새로 추가해주세요");
            addnewPayment();
            return;
        }
        while (true) {
            try {
                for (Payment payment : paymentList) {
                    System.out.println(payment);
                }
                System.out.println("0 : 취소하기");
                System.out.println("exit : 시스템 종료");
                String key = sc.next();
                key = key.toUpperCase();
                if (key.equals("0"))
                    return;
                if(key.equals("exit"))
                    throw new MyCloseSequence();
                Payment payment = this.paymentList.read(Integer.parseInt(key));
                this.customer.registerPayment(contract, payment);
                break;
            } catch (NumberFormatException e) {
                System.out.println("정확한 형식의 값을 입력해주세요.");
            } catch (IllegalArgumentException| MyIllegalArgumentException e ) {
                System.out.println(e.getMessage());
            }
        }
    }
    // 고객에게 새로운 결제수단을 추가하는 기능. 카드와 계좌의 정보를 추가할 수 있다.
    public void addnewPayment() {
        loop :while (true) {
            createMenu("결제수단추가하기","카드추가하기","계좌추가하기");
            System.out.println("0. 취소하기");
            System.out.println("exit : 종료하기");
            switch (sc.next()) {
                case "1" :
                    createCard();
                    break;
                case "2":
                    createAccount();
                    break;
                case "0":
                    break loop;
                case "exit" :
                    throw new MyCloseSequence();
                default:
                    System.out.println("정확한 값을 입력해주세요.");
            }
        }
    }
    

    // 결제수단 중 카드를 새로 추가하는 기능
    private void createCard() {
        PaymentDto card = new PaymentDto();
        while (true) {
            try {
                System.out.println("카드 등록하기");
                System.out.println("카드사 선택");
                CardType cardType = selectCardType();
                if(cardType==null)
                    return;

                while (true) {
                    try {
                        System.out.println("카드 번호 : (예시 : ****-****-****-****) {4자리 숫자와 - 입력}");
                        String cardNo = validateCardNoFormat(sc.next());
                        System.out.println("CVC : (예시 : *** {3자리 숫자})");
                        String cvc = validateCVCFormat(sc.next());
                        System.out.println("만료일");
                        System.out.print("월 : ");
                        int month = validateMonthFormat(sc.nextInt());
                        System.out.print("년 : (예시 : ****) {4개 숫자 입력 && 202* ~ 203* 까지의 값 입력}");
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
                        System.out.println("정확한 값을 입력해주세요");
                    }
                }


                while (true) {
                    System.out.println("카드 정보를 등록하시겠습니까? (Y/N)");
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals("N")) {
                        System.out.println("결제 수단 등록을 취소하셨습니다.");
                        return;
                    } else if (result.equals("Y"))
                        break;
                    else
                        System.out.println("Y 혹은 N을 입력해주세요");
                }
                break;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        Payment payment = customer.createPayment(card);
        paymentList.create(payment);
        customer.addPayment(payment);
        System.out.println("결제 수단이 추가되었습니다.");

    }

    // 카드 결제 수단 추가 중 카드사를 선택하는 기능
    private CardType selectCardType() {
        CardType[] values = CardType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("0. 취소하기");
        System.out.println("카드사 번호 : ");
        String key = sc.next();
        if(key.equals("0"))
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
        String mm = month < 10 ? "0"+month : String.valueOf(month);
        String date = "01/"+mm+"/"+year;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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
                System.out.println("계좌 추가하기");
                System.out.println("은행사 선택하기");
                BankType bankType = selectBankType();
                if(bankType==null)
                    return;
                while (true) {
                    try {
                        System.out.println("계좌 번호 입력하기 : (예시 -> " + bankType.getFormat() + ")");
                        System.out.println("0. 취소하기");
                        String command = sc.next();
                        if (command.equals("0")) {
                            continue loop;
                        }
                        String accountNo = checkAccountFormat(bankType,command);


                        account.setBankType(bankType)
                                .setAccountNo(accountNo)
                                .setCustomerId(this.customer.getId())
                                .setPayType(PayType.ACCOUNT);
                        break;
                    } catch (MyInadequateFormatException e) {
                        System.out.println("정확한 값을 입력해주세요");
                    }
                }

                while (true) {
                    System.out.println("계좌 정보를 등록하시겠습니까? (Y/N)");
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals("N")) {
                        System.out.println("결제 수단 등록을 취소하셨습니다.");
                        return;
                    } else if (result.equals("Y"))
                        break;
                    else
                        System.out.println("Y 혹은 N을 입력해주세요");
                }

                break;
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException| MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        Payment payment = customer.createPayment(account);
        paymentList.create(payment);
        customer.addPayment(payment);
        System.out.println("결제 수단이 추가되었습니다.");
    }

    // 계좌 결제 수단 추가 과정에서 은행을 선택하는 기능
    private BankType selectBankType() {
        BankType[] values = BankType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("0. 취소하기");
        System.out.println("은행 번호 : ");
        String key = sc.next();
        
        if(key.equals("0"))
            return null;


        return values[Integer.parseInt(key)-1];
    }

    // 계좌 결제 수단 추가 과정에서 은행사에 따라서 계좌 번호를 검증하는 기능
    private String checkAccountFormat(BankType bankType, String accountNo) {
        boolean result = false;
        switch (bankType) {
            case KB :
                result = isKB(accountNo);
                break;
            case NH:
                result = isNH(accountNo);
                break;
            case KAKAOBANK:
                result = isKakaoBank(accountNo);
                break;
            case SINHAN:
                result = isSinhan(accountNo);
                break;
            case WOORI:
                result = isWoori(accountNo);
                break;
            case IBK:
                result = isIBK(accountNo);
                break;
            case HANA:
                result = isHana(accountNo);
                break;
            case CITY:
                result = isCity(accountNo);
                break;
            case SAEMAUL:
                result = isSaemaul(accountNo);
                break;
        }
        if (!result)
            throw new MyInadequateFormatException("정확한 형식의 값을 입력해주세요");

        return accountNo;

    }



    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }


}
