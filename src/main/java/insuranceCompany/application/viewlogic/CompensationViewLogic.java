package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDao;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.domain.accident.*;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFile;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.payment.BankType;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.exception.MyInadequateFormatException;
import insuranceCompany.application.global.exception.MyInvalidAccessException;
import insuranceCompany.application.global.exception.NoResultantException;
import insuranceCompany.application.global.utility.DocUtil;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.viewlogic.dto.compDto.AccountRequestDto;
import insuranceCompany.application.viewlogic.dto.compDto.AssessDamageResponseDto;
import insuranceCompany.application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import insuranceCompany.outerSystem.Bank;

import java.io.InputStreamReader;
import java.util.List;

import static insuranceCompany.application.global.utility.BankUtil.checkAccountFormat;
import static insuranceCompany.application.global.utility.BankUtil.selectBankType;
import static insuranceCompany.application.global.utility.FormatUtil.isErrorRate;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndLogout;


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

    private AccidentDao accidentDao;
    private AccidentDocumentFileDao accidentDocumentFileDao;
    private CustomerDao customerList;
    private MyBufferedReader br;
    private Employee employee;

    public CompensationViewLogic() {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    public CompensationViewLogic(Employee employee) {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employee = employee;
    }

    @Override
    public String showMenu() {
       return createMenuAndLogout("보상팀 메뉴", "사고목록조회","손해조사","손해사정");
    }

    @Override
    public void work(String command) {

        try {

            switch (command) {
                case "1":
                    showAccidentList();
                    break;
                case "2":
                    investigateDamage();
                    break;
                case "3":
                    assessDamage();
                    break;
                case "0" :
                    break;
                default:
                    throw new MyIllegalArgumentException();
            }
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void investigateDamage() {
        Accident accident = selectAccident();
        if(accident == null)
            return;
        showAccidentDetail(accident);

        downloadAccDocFile(accident);
        System.out.println("다운로드 종료");

        InvestigateDamageRequestDto dto = new InvestigateDamageRequestDto();
        dto.setAccidentType(accident.getAccidentType());

        if(accident.getAccidentType() == AccidentType.CARACCIDENT)
        inputErrorRate(dto); // 과실비율 입력
        inputLossReserve(dto); // 지급 준비금 입력.
        if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
            System.out.println("사고 조사 보고서를 제출해주세요");
        }
        employee.investigateDamage(dto,accident);


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead("손해 사정을 진행하시겠습니까? (Y/N)",rtVal);
            if (rtVal.equals("Y")) {
                assessDamage();
                break;
            } else if (rtVal.equals("N")) {
                break;
            }
        }

    }

    private void showAccidentList() {
        List<Accident> accidents = getAccidentList();
        if (accidents == null) return;
        showAccidentInfo(accidents);
    }

    private Accident selectAccident() {
        List<Accident> accidents = getAccidentList();
        if (accidents == null) return null;
        while (true) {
            try {
                System.out.println("<< 사고를 선택하세요. >>");
                showAccidentInfo(accidents);
                System.out.println("---------------------------------");
                int accidentId = 0;
                accidentId = (int) br.verifyRead("사고 ID : ", accidentId);
                accidentDao = new AccidentDaoImpl();
                Accident accident = this.accidentDao.read(accidentId);
                if (accident.getEmployeeId() != this.employee.getId()) {
                    throw new MyInvalidAccessException("리스트에 있는 아이디를 입력해주세요.");
                }
                accidentDocumentFileDao = new AccidentDocumentFileDaoImpl();
                List<AccidentDocumentFile> accidentDocumentFiles = accidentDocumentFileDao.readAllByAccidentId(accidentId);
                for (AccidentDocumentFile accidentDocumentFile : accidentDocumentFiles) {
                    accident.getAccDocFileList().put(accidentDocumentFile.getType(), accidentDocumentFile);
                }
                return accident;
            } catch (MyInvalidAccessException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    private List<Accident> getAccidentList() {
        List<Accident> accidents = null;
        try {
            accidents = employee.readAccident();
        } catch (NoResultantException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return accidents;
    }

    private void showAccidentInfo(List<Accident> accidents) {
        for (Accident accident : accidents) {
            accident.printForComEmployee();
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
        Accident accident = selectAccident();
        if(accident == null) // 배정된 사고가 없을 떄 null. 이거도 exception인가???
            return;
        // 사고조사보고서, 지급준비금, 과실비율이 입력 값이 없으면 돌아가야해.
        isValidAccident(accident);
        //다운로드 하기.

        downloadAccDocFile(accident);
        AccountRequestDto compAccount = createCompAccount();
        System.out.println("손해사정서를 업로드해주세요.");
        AssessDamageResponseDto assessDamageResponseDto = this.employee.assessDamage(accident,compAccount);

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
        accidentDao = new AccidentDaoImpl();
        accidentDao.delete(accident.getId());
        DocUtil.deleteDir(accident); // 폴더 삭제
    }

    private void isValidAccident(Accident accident) {
        if (accident.getLossReserves() == 0 ) {
            throw new MyInvalidAccessException("지급 준비금이 입력되지 않은 사고이기 떄문에 손해 사정이 불가능합니다.");
        }
        if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
            throw new MyInvalidAccessException("사고 조사 보고서가 업로드되지 않은 사고이기 때문에 손해 사정이 불가능합니다.");
        }
        if (accident instanceof CarAccident) {
            if (((CarAccident) accident).getErrorRate() == 0) {
                throw new MyInvalidAccessException("과실 비율이 입력되지 않은 사고이기 때문에 손해 사정이 불가능합니다.");
            }
        }
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

    private void downloadAccDocFile(Accident accident) {
        DocUtil instance = DocUtil.getInstance();
        for (AccidentDocumentFile accidentDocumentFile : accident.getAccDocFileList().values()) {
            label:
            while (true) {
                String query = accidentDocumentFile.getType().getDesc()+"를 다운로드 하시겠습니까? (Y/N) (0.취소하기)";
                String result = "";
                result = (String) br.verifyRead(query,result);
                switch (result) {
                    case "Y":
                        instance.download(accidentDocumentFile.getFileAddress());
                        break label;
                    case "N":
                        break label;
                    case "0":
                        return;
                }
            }
        }
    }
}
