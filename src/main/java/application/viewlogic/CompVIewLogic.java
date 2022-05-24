package application.viewlogic;

import application.ViewLogic;
import domain.accident.*;
import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocFileListImpl;
import domain.customer.Customer;
import domain.customer.CustomerList;
import domain.customer.CustomerListImpl;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeList;
import domain.employee.EmployeeListImpl;
import exception.InputException;
import exception.MyIllegalArgumentException;
import utility.CustomMyBufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static utility.MessageUtil.createMenu;


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

    public CompVIewLogic(EmployeeListImpl employeeList, AccidentListImpl accidentList, AccDocFileListImpl accDocFileList, CustomerList customerList) {
        this.employeeList = employeeList;
        this.accidentList = accidentList;
        this.accDocFileList = accDocFileList;
        this.customerList = customerList;
        this.br = new CustomMyBufferedReader(new InputStreamReader(System.in));
    }



    @Override
    public void showMenu() {
       createMenu("보상팀 메뉴", "사고목록조회","손해조사","손해사정");
    }

    @Override
    public void work(String command) {


        switch (command) {
            case "1" :
                break;
            case "2":
                break;
            case"3":
                break;
            default:
                throw new MyIllegalArgumentException();


        }
    }

    private void investigateDamage() {
        loginCompEmployee();
        selectAccident();

    }

    private void selectAccident() {
        List<Accident> accidents = this.accidentList.readAllByEmployeeId(this.employee.getId());
        while (true) {
            System.out.println("<< 사고를 선택하세요. >>");
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
            showAccidentDetail(accident);


        }
    }

    private void showAccidentDetail(Accident accident) {
        Customer customer = customerList.read(accident.getCustomerId());
        List<AccDocFile> accDocFiles = accDocFileList.readAllByAccidentId(accident.getId());

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

    private void loginCompEmployee() {
        while(true){
            try {
                System.out.println("<< 직원을 선택하세요. >>");
                List<Employee> employeeArrayList = this.employeeList.readAllCompEmployee();
                for(Employee employee : employeeArrayList){
                    System.out.println(employee.print());
                }
                System.out.println("---------------------------------");
                int employeeId = br.verifyMenu("직원 ID: ", employeeArrayList.size());
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
