package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDao;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.domain.accident.*;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccDocType;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccidentDocumentFile;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.customer.payment.BankType;
import insuranceCompany.application.global.constant.CompensationViewLogicConstants;
import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.FileDialogUtil;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.viewlogic.dto.compDto.AccountRequestDto;
import insuranceCompany.application.viewlogic.dto.compDto.AssessDamageResponseDto;
import insuranceCompany.application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import insuranceCompany.outerSystem.Bank;

import java.io.InputStreamReader;
import java.util.List;

import static insuranceCompany.application.global.constant.CommonConstants.*;
import static insuranceCompany.application.global.constant.CompensationViewLogicConstants.*;
import static insuranceCompany.application.global.constant.CustomerViewLogicConstants.*;
import static insuranceCompany.application.global.constant.ExceptionConstants.*;
import static insuranceCompany.application.global.utility.BankUtil.checkAccountFormat;
import static insuranceCompany.application.global.utility.BankUtil.selectBankType;
import static insuranceCompany.application.global.utility.FormatUtil.isErrorRate;
import static insuranceCompany.application.global.utility.MenuUtil.createMenuAndLogout;


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
       return createMenuAndLogout(COMPENSATION_MENU_HEAD, MENU_ELEMENTS_COMP_VIEW_LOGIC);
    }

    @Override
    public void work(String command) {

        try {

            switch (command) {
                case ONE:
                    showAccidentList();
                    break;
                case TWO:
                    investigateDamage();
                    break;
                case THREE:
                    assessDamage();
                    break;
                case ZERO:
                    break;
                default:
                    throw new InputInvalidMenuException(INPUT_INVALID_MENU_EXCEPTION);
            }
        } catch (InputInvalidMenuException e) {
            System.out.println(e.getMessage());
        }
    }

    private void investigateDamage() {
        Accident accident = selectAccident();
        if(accident == null)
            return;
        showAccidentDetail(accident);

        downloadAccDocFile(accident);
        System.out.println(FINISH_DOWNLOAD);

        InvestigateDamageRequestDto dto = new InvestigateDamageRequestDto();
        dto.setAccidentType(accident.getAccidentType());

        if(accident.getAccidentType() == AccidentType.CARACCIDENT)
            inputErrorRate(dto); // 과실비율 입력
        inputLossReserve(dto); // 지급 준비금 입력.
        if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
            System.out.println(INVESTIGATE_ACCIDENT_QUERY);
        }
        employee.investigateDamage(dto,accident);


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead(ASSESS_DAMAGE_QUERY,rtVal);
            if (rtVal.equals(YES)) {
                assessDamage();
                break;
            } else if (rtVal.equals(NO)) {
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
                System.out.println(SELECT_ACCIDENT);
                showAccidentInfo(accidents);
                System.out.println(LIST_LINE);
                int accidentId = 0;
                accidentId = (int) br.verifyRead(ACCIDENT_ID_QUERY, accidentId);
                if (accidentId == 0) {
                    return null;
                }
                accidentDao = new AccidentDaoImpl();
                Accident accident = this.accidentDao.read(accidentId);
                if (accident.getEmployeeId() != this.employee.getId()) {
                    throw new MyIllegalArgumentException(INPUT_DATA_ON_LIST);
                }
                accidentDocumentFileDao = new AccidentDocumentFileDaoImpl();
                List<AccidentDocumentFile> accidentDocumentFiles = accidentDocumentFileDao.readAllByAccidentId(accidentId);
                for (AccidentDocumentFile accidentDocumentFile : accidentDocumentFiles) {
                    accident.getAccDocFileList().put(accidentDocumentFile.getType(), accidentDocumentFile);
                }
                return accident;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<Accident> getAccidentList() {
        List<Accident> accidents = null;
        try {
            accidents = employee.readAccident();
        } catch (NoResultantException e) {
            System.out.println(e.getMessage());
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
        System.out.println(SHOW_CUSTOMER_NAME + customer.getName());
        AccidentType accidentType = accident.getAccidentType();
        switch (accidentType) {
            case CARACCIDENT -> {
                System.out.println(SHOW_CAR_NO + ((CarAccident)accident).getCarNo());
                System.out.println(SHOW_OPPOSING_PHONE + ((CarAccident)accident).getOpposingDriverPhone());
                System.out.println(SHOW_PLACE_ADDRESS + ((CarAccident)accident).getPlaceAddress());
            }
            case INJURYACCIDENT ->
                System.out.println(SHOW_INJURY_SITE + ((InjuryAccident)accident).getInjurySite());

            case FIREACCIDENT ->
                System.out.println(SHOW_PLACE_ADDRESS + ((FireAccident)accident).getPlaceAddress());

        }

    }

    private void assessDamage() {
        Accident accident = null;
        AccountRequestDto compAccount = null;
        while (true) {
            try {
                accident = selectAccident();
                if (accident == null) // 배정된 사고가 없을 떄 null. 이거도 exception인가???
                    return;
                isValidAccident(accident);
                downloadAccDocFile(accident);
                compAccount = createCompAccount();
                break;

            } catch (MyInvalidAccessException e) {
                System.out.println(e.getMessage());
            }

        }
        //다운로드 하기.


        System.out.println(UPLOAD_ASSESS_DAMAGE);
        AssessDamageResponseDto assessDamageResponseDto = null;
        try {
            assessDamageResponseDto  = this.employee.assessDamage(accident, compAccount);
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
            return;
        }

        long lossReserves = accident.getLossReserves();
        long compensation = 0L;
        compensation = (long) br.verifyRead(getInputCompensation(lossReserves), compensation);
        if (compensation > lossReserves * 1.5) {
            System.out.println(REJECT_ASSESS_DAMAGE);
            return;
        }

        if (accident.getAccidentType() == AccidentType.CARACCIDENT) {
            int errorRate = 0;
            errorRate = ((CarAccident)accident).getErrorRate();
            compensation = (long) (compensation * (((double)errorRate/100)));
            if (compensation == 0) {
                System.out.println(NO_ERROR);
                return;
            }
        }
        Bank.sendCompensation(assessDamageResponseDto.getAccount(),compensation);
        try {
            accidentDao = new AccidentDaoImpl();
            accidentDao.delete(accident.getId());
            FileDialogUtil.deleteDir(accident); // 폴더 삭제
        } catch (MyIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void isValidAccident(Accident accident) {
        if (accident.getLossReserves() == 0 ) {
            throw new MyInvalidAccessException(LOSS_RESERVE_EXCEPTION);
        }
        if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
            throw new MyInvalidAccessException(INVESTIGATE_ACCIDENT_EXCEPTION);
        }
        if (accident instanceof CarAccident car && car.getErrorRate() == 0) {
                throw new MyInvalidAccessException(ERROR_RATE_EXCEPTION);
        }
    }

    private AccountRequestDto createCompAccount() {
        AccountRequestDto account = null;
        loop : while (true) {

            System.out.println(REGISTER_ACCOUNT);
            System.out.println(SELECT_BANK);
            BankType bankType = selectBankType(br);
            if(bankType==null)
               throw new MyInvalidAccessException(INPUT_ACCOUNT);
            while (true) {
                try {
                    StringBuilder query = new StringBuilder();
                    query.append(showAccountNoEX(bankType.getFormat())).append(LINE_SEPARATOR).append(INPUT);
                    String command = "";
                    command = (String) br.verifyRead(query.toString(),command);
                    if (command.equals(ZERO)) {
                        continue loop;
                    }
                    String accountNo = checkAccountFormat(bankType,command);
                    account = AccountRequestDto.builder().bankType(bankType)
                            .accountNo(accountNo)
                            .build();
                    break loop;
                } catch (MyInadequateFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return account;
    }



    private void inputLossReserve(InvestigateDamageRequestDto accident) {
        while (true) {
            try {
                long loss_reserve = -1;
                loss_reserve = (long) br.verifyRead(LOSS_RESERVE_QUERY, loss_reserve);
                if (loss_reserve < 0) {
                    throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
                }
                accident.setLossReserves(loss_reserve);
                break;
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void inputErrorRate(InvestigateDamageRequestDto accident) {

            while (true) {
                try{
                    int errorRate = -1;
                    errorRate = (int) br.verifyRead(ERROR_RATE_QUERY,errorRate);
                    if (isErrorRate(errorRate)) {
                        accident.setErrorRate(errorRate);
                        break;
                    } else {
                        throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
                    }
                }catch (MyInadequateFormatException e) {
                    System.out.println(e.getMessage());
                }
            }

    }

    private void downloadAccDocFile(Accident accident) {
        for (AccidentDocumentFile accidentDocumentFile : accident.getAccDocFileList().values()) {
            label:
            while (true) {
                String query = CompensationViewLogicConstants.getDownloadDocExQuery(accidentDocumentFile.getType().getDesc());
                String result = "";
                result = (String) br.verifyRead(query,result);
                switch (result) {
                    case YES:
                        FileDialogUtil.download(accidentDocumentFile.getFileAddress());
                        break label;
                    case NO:
                        break label;
                    case ZERO:
                        return;
                }
            }
        }
    }
}
