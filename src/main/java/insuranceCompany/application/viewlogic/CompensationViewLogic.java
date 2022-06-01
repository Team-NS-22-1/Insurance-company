package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.domain.accident.*;
import insuranceCompany.application.viewlogic.dto.compDto.AccountRequestDto;
import insuranceCompany.application.viewlogic.dto.compDto.AssessDamageResponseDto;
import insuranceCompany.application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.domain.accident.accDocFile.AccDocFile;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFileList;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.dao.customer.CustomerDao;
import insuranceCompany.application.domain.employee.Department;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.payment.BankType;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.exception.MyInadequateFormatException;
import insuranceCompany.outerSystem.Bank;
import insuranceCompany.application.global.utility.CustomMyBufferedReader;
import insuranceCompany.application.global.utility.DocUtil;

import java.io.InputStreamReader;
import java.util.List;

import static insuranceCompany.application.global.utility.BankUtil.checkAccountFormat;
import static insuranceCompany.application.global.utility.BankUtil.selectBankType;
import static insuranceCompany.application.global.utility.FormatUtil.isErrorRate;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndExit;


/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CompVIewLogic
 * author :  규현
 * date : 2022-05-10
 * description : ViewLogic의 구현클래스, 보상팀의 업무 목록과
 * 그것을 실행시키는 기능이 있다.
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class CompensationViewLogic implements ViewLogic {

    private AccidentList accidentList;
    private AccidentDocumentFileList accidentDocumentFileList;
    private CustomerDao customerList;
    private EmployeeDao employeeList;
    private CustomMyBufferedReader br;
    private Employee employee;

    public CompensationViewLogic() {
        this.br = new CustomMyBufferedReader(new InputStreamReader(System.in));
    }



    @Override
    public void showMenu() {
       createMenuAndExit("보상팀 메뉴", "사고목록조회","손해조사","손해사정");
    }

    @Override
    public void work(String command) {


        switch (command) {
            case "1" :
                break;
            case "2":
                investigateDamage();
                break;
            case"3":
                assessDamage();
                break;
            default:
                throw new MyIllegalArgumentException();


        }
    }

    private void investigateDamage() {

        loginCompEmployee();

        Accident accident = selectAccident();
        if(accident == null)
            return;

        showAccidentDetail(accident);
        accidentDocumentFileList = new AccidentDocumentFileDao();
        List<AccDocFile> accDocFiles = accidentDocumentFileList.readAllByAccidentId(accident.getId());
        //다운로드 하기.

        downloadAccDocFile(accident, accDocFiles);
        System.out.println("다운로드 종료");
        // investigateDamageaccidentRequestDto 에 지급준비금, 혹은 손해율 가져가서 accident에 넣어서 뱉어줘야겠다.

        InvestigateDamageRequestDto dto = new InvestigateDamageRequestDto();
        dto.setAccidentType(accident.getAccidentType());

        if(accident.getAccidentType() == AccidentType.CARACCIDENT)
        inputErrorRate(dto);
        // 지급 준비금 입력.
        inputLossReserve(dto);

        employee.investigateDamage(dto,accident);
        accidentList = new AccidentDao();
        if(accident.getAccidentType() == AccidentType.CARACCIDENT)
            accidentList.updateLossReserveAndErrorRate(accident);
        else
            accidentList.updateLossReserve(accident);


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead("손해 사정을 진행하시겠습니까? (Y/N)",rtVal);
            if (rtVal.equals("Y")) {
                assessDamagewithoutLogin();
                break;
            } else if (rtVal.equals("N")) {
                break;
            }
        }

    }

    private Accident selectAccident() {

        accidentList = new AccidentDao();
        List<Accident> accidents = this.accidentList.readAllByEmployeeId(this.employee.getId());
        if (accidents.size() == 0) {
            System.out.println("현재 배당된 사고가 없습니다.");
            return null;
        }
        while (true) {
            System.out.println("<< 사고를 선택하세요. >>");
            System.out.println(accidents.size());
            for (Accident accident : accidents) {
                accident.printForComEmployee();
            }
            System.out.println("---------------------------------");
            int accidentId = 0;
            accidentId = (int)br.verifyRead("사고 ID : ", accidentId);
            accidentList = new AccidentDao();
            Accident accident = this.accidentList.read(accidentId);
            if (accident.getEmployeeId() != this.employee.getId()) {
                System.out.println("현재 직원에게 배당된 사건이 아닙니다. 정확한 값을 입력해주세요.");
                continue;
            }
            return accident;
        }
    }

    private void showAccidentDetail(Accident accident) {
        customerList = new CustomerDaoImpl();
        Customer customer = customerList.read(accident.getCustomerId());


        accident.printForComEmployee();
        System.out.println("접수자 명 : " + customer.getName());
        AccidentType accidentType = accident.getAccidentType();
        switch (accidentType) {
            case CARACCIDENT -> {
                System.out.println("차량 번호 : " + ((CarAccident)accident).getCarNo());
                System.out.println("상대방 차주 연락처 : " + ((CarAccident)accident).getOpposingDriverPhone());
                System.out.println("사고 주소 : " + ((CarAccident)accident).getPlaceAddress());
                break;
            }
            case INJURYACCIDENT -> {
                System.out.println("부상 부위 : " + ((InjuryAccident)accident).getInjurySite());
                break;
            }
            case FIREACCIDENT -> {
                System.out.println("사고 주소 : " + ((FireAccident)accident).getPlaceAddress());
                break;
            }
        }

    }

    private void assessDamage() {
        loginCompEmployee();
        assessDamagewithoutLogin();
    }

    private void assessDamagewithoutLogin() {
        Accident accident = selectAccident();
        if(accident == null)
            return;
        accidentDocumentFileList = new AccidentDocumentFileDao();
        List<AccDocFile> accDocFiles = accidentDocumentFileList.readAllByAccidentId(accident.getId());
        //다운로드 하기.

        downloadAccDocFile(accident, accDocFiles);
        AccountRequestDto compAccount = createCompAccount();
        System.out.println("손해사정서를 업로드해주세요.");
        AssessDamageResponseDto assessDamageResponseDto = this.employee.assessDamage(accident,compAccount);
        boolean isExist = false;
        int lossId = 0;
        for (AccDocFile accDocFile : accDocFiles) {
            if (accDocFile.getType() == AccDocType.LOSSASSESSMENT) {
                lossId = accDocFile.getId();
                isExist = true;
            }
        }
        accidentDocumentFileList = new AccidentDocumentFileDao();
        if (isExist) {
            accidentDocumentFileList.update(lossId);
        } else {
            accidentDocumentFileList.create(assessDamageResponseDto.getAccDocFile());
        }
        long lossReserves = accident.getLossReserves();
        long compensation = 0L;
        compensation = (long) br.verifyRead("지급할 보상금을 입력해주세요.", compensation);

        if (compensation > lossReserves * 1.5) {
            System.out.println("손해 사정서가 반려되었습니다.");
            return;
        }

        if (accident.getAccidentType() == AccidentType.CARACCIDENT) {
            int errorRate = 0;
            errorRate = ((CarAccident)accident).getErrorRate();
            compensation = compensation * (errorRate/100);

            if (compensation == 0) {
                System.out.println("고객 과실이 0이기 때문에 보상금을 지급하지 않습니다.");
                return;
            }
        }
        Bank.sendCompensation(assessDamageResponseDto.getAccount(),compensation);
        accidentList = new AccidentDao();
        accidentList.delete(accident.getId());
        DocUtil.deleteDir(accident); // 폴더 삭제
    }

    private AccountRequestDto createCompAccount() {
        AccountRequestDto account = null;
        loop : while (true) {

            System.out.println("계좌 추가하기");
            System.out.println("은행사 선택하기");
            BankType bankType = selectBankType(br);
            if(bankType==null)
                break ;
            while (true) {
                try {
                    StringBuilder query = new StringBuilder();
                    query.append("계좌 번호 입력하기 : (예시 -> ").append(bankType.getFormat()).append(")\n")
                            .append("0. 취소하기\n");

                    String command = "";
                    command = (String) br.verifyRead(query.toString(),command);
                    if (command.equals("0")) {
                        continue loop;
                    }
                    String accountNo = checkAccountFormat(bankType,command);
                    account = AccountRequestDto.builder().bankType(bankType)
                            .accountNo(accountNo)
                            .build();
                    break loop;
                } catch (MyInadequateFormatException e) {
                    System.out.println("정확한 값을 입력해주세요");
                }
            }
        }
        return account;
    }



    private void inputLossReserve(InvestigateDamageRequestDto accident) {
        while (true) {
            long loss_reserve = -1;
            loss_reserve = (long) br.verifyRead("지급 준비금을 입력해주세요 ",loss_reserve);
            if (loss_reserve< 0 ) {
                System.out.println("정확한 값을 입력해주세요");
                continue;
            }
            accident.setLossReserves(loss_reserve);
            break;
        }
    }

    private void inputErrorRate(InvestigateDamageRequestDto accident) {

            while (true) {
                int errorRate = -1;
                errorRate = (int) br.verifyRead("과실비율을 입력해주세요 (0~100)",errorRate);
                if (isErrorRate(errorRate)) {
                    accident.setErrorRate(errorRate);
                    break;
                } else {
                    System.out.println("범위에 맞게 입력해주세요.");
                }
            }

    }

    private void downloadAccDocFile(Accident accident, List<AccDocFile> accDocFiles) {
        DocUtil instance = DocUtil.getInstance();
        for (AccDocFile accDocFile : accDocFiles) {
            while (true) {
                String query = accDocFile.getType().getDesc()+"를 다운로드 하시겠습니까? (Y/N) (0.취소하기)";
                String result = "";
                result = (String) br.verifyRead(query,result);
                if (result.equals("Y")) {
                    instance.download(accDocFile.getFileAddress());
                    break;
                } else if (result.equals("N")) {
                    break;
                } else if (result.equals("0")) {
                    return;
                }
            }
        }
    }

    private void loginCompEmployee() {
        while(true){
            try {
                System.out.println("<< 직원을 선택하세요. >>");
                employeeList = new EmployeeDao();
                List<Employee> employeeArrayList = this.employeeList.readAllCompEmployee();
                for(Employee employee : employeeArrayList){
                    System.out.println(employee.print());
                }
                System.out.println("---------------------------------");
                int employeeId = 0;
                employeeId = (int) br.verifyRead("직원 ID: ", employeeId);
                if(employeeId == 0) break;
                this.employeeList = new EmployeeDao();
                this.employee = this.employeeList.read(employeeId);
                if(this.employee.getDepartment() != Department.COMP){
                    System.out.println("해당 직원은 보상팀 직원이 아닙니다!");
                    continue;
                }
                break;
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }




}
