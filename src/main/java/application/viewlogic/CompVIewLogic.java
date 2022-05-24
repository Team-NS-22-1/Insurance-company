package application.viewlogic;

import application.ViewLogic;
import domain.accident.AccidentList;
import domain.accident.AccidentListImpl;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocFileListImpl;
import domain.customer.CustomerList;
import domain.customer.CustomerListImpl;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeList;
import domain.employee.EmployeeListImpl;
import exception.InputException;
import exception.MyIllegalArgumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

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

    public CompVIewLogic(EmployeeListImpl employeeList, AccidentListImpl accidentList, AccDocFileListImpl accDocFileList, CustomerListImpl customerList) {
        this.employeeList = employeeList;
        this.accidentList = accidentList;
        this.accDocFileList = accDocFileList;
        this.customerList = customerList;
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

//    private void testInitEmployee() throws IOException {
//        while(true){
//            try {
//                System.out.println("<< 직원을 선택하세요. >>");
//                ArrayList<Employee> employeeArrayList = this.employeeList.readAll();
//                for(Employee employee : employeeArrayList){
//                    System.out.println(employee.print());
//                }
//                System.out.println("---------------------------------");
//                int employeeId = br.verifyMenu("직원 ID: ", employeeArrayList.size());
//                if(employeeId == 0) break;
//                this.employee = this.employeeList.read(employeeId);
//                if(this.employee.getDepartment() != Department.DEV){
//                    System.out.println("해당 직원은 개발팀 직원이 아닙니다!");
//                    continue;
//                }
//                break;
//            }
//            catch (InputException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }


}
