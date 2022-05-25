package application.viewlogic;

import application.ViewLogic;
import domain.accident.*;
import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocFileListImpl;
import domain.accident.accDocFile.AccDocType;
import domain.customer.Customer;
import domain.customer.CustomerList;
import domain.customer.CustomerListImpl;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeList;
import domain.employee.EmployeeListImpl;
import exception.InputException;
import exception.MyIllegalArgumentException;
import outerSystem.Bank;
import utility.CustomMyBufferedReader;
import utility.DocUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static utility.FormatUtil.isErrorRate;
import static utility.MessageUtil.createMenu;
import static utility.MessageUtil.createMenuAndExit;


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
public class CompVIewLogic implements ViewLogic {


    private final EmployeeList employeeList;
    private final AccidentList accidentList;
    private final AccDocFileList accDocFileList;
    private final CustomerList customerList;
    private CustomMyBufferedReader br;
    private Employee employee;

    public CompVIewLogic(EmployeeList employeeList, AccidentList accidentList, AccDocFileList accDocFileList, CustomerList customerList) {
        this.employeeList = employeeList;
        this.accidentList = accidentList;
        this.accDocFileList = accDocFileList;
        this.customerList = customerList;
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
                break;
            default:
                throw new MyIllegalArgumentException();


        }
    }

    private void investigateDamage() {
        System.out.println("CompVIewLogic.investigateDamage 시작");
        loginCompEmployee();

        Accident accident = selectAccident();
        if(accident == null)
            return;

        showAccidentDetail(accident);
        List<AccDocFile> accDocFiles = accDocFileList.readAllByAccidentId(accident.getId());
        //다운로드 하기.

        downloadAccDocFile(accident, accDocFiles);
        System.out.println("다운로드 종료");
        if(accident.getAccidentType() == AccidentType.CARACCIDENT)
        inputErrorRate((CarAccident) accident, accident.getAccidentType());
        // 지급 준비금 입력.
        inputLossReserve(accident);

        //TODO accident update하기.
//        accidentList.update()

        // TODO 손해사정으로 넘어가기.
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

    private Accident selectAccident() {
        System.out.println("CompVIewLogic.selectAccident");
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
            Accident accident = this.accidentList.read(accidentId);
            if (accident.getEmployeeId() != this.employee.getId()) {
                System.out.println("현재 직원에게 배당된 사건이 아닙니다. 정확한 값을 입력해주세요.");
                continue;
            }
            return accident;
        }
    }

    private void showAccidentDetail(Accident accident) {
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
//            default ->
        }

    }

    private void assessDamage() {
        loginCompEmployee();
        Accident accident = selectAccident();
        if(accident == null)
            return;

        List<AccDocFile> accDocFiles = accDocFileList.readAllByAccidentId(accident.getId());
        //다운로드 하기.
        downloadAccDocFile(accident, accDocFiles);

        AccDocFile accDocFile = this.employee.assessDamage(accident);
        accDocFileList.create(accDocFile);
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
        Bank.sendCompensation(accident.getAccount(),compensation);

        // 다운로드할지 물음.


    }

    private void inputLossReserve(Accident accident) {
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

    private void inputErrorRate(CarAccident accident, AccidentType accidentType) {
        if (accidentType == AccidentType.CARACCIDENT) {
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
    }

    private void downloadAccDocFile(Accident accident, List<AccDocFile> accDocFiles) {
        DocUtil instance = DocUtil.getInstance();
        for (AccDocFile accDocFile : accDocFiles) {
            while (true) {
                String query = accDocFile.getType().getDesc()+"를 다운로드 하시겠습니까? (Y/N) (0.취소하기)";
                String result = "";
                result = (String) br.verifyRead(query,result);
                if (result.equals("Y")) {
                    instance.download(accident, accDocFile.getType());
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
                List<Employee> employeeArrayList = this.employeeList.readAllCompEmployee();
                for(Employee employee : employeeArrayList){
                    System.out.println(employee.print());
                }
                System.out.println("---------------------------------");
                int employeeId = 0;
                employeeId = (int) br.verifyRead("직원 ID: ", employeeId);
                if(employeeId == 0) break;
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
