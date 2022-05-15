package main.application;

import main.TestDevData;
import main.TestData;
import main.application.viewlogic.*;
import main.domain.contract.ContractListImpl;
import main.domain.customer.CustomerListImpl;
import main.domain.employee.EmployeeListImpl;
import main.domain.insurance.InsuranceListImpl;
import main.domain.payment.PaymentListImpl;
import main.application.viewlogic.*;
import main.exception.ReturnMenuException;
import main.exception.SystemExitException;
import main.utility.MessageUtil;
import main.exception.MyCloseSequence;
import main.exception.MyIllegalArgumentException;


import java.util.*;

import static main.utility.MessageUtil.createMenu;

/**
 * packageName :  main.application
 * fileName : Application
 * author :  규현
 * date : 2022-05-10
 * description : 어플리케이션 시작 클래스, 해당 클래스에서 유저 타입을 입력하는 것으로
 *               각자 개발한 ViewLogic을 수행하여 기능을 작동한다.
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class Application {
    private Map<UserType, ViewLogic> map = new HashMap<>();

    public Application() {
        TestData t = new TestData();
        CustomerListImpl customerList = new CustomerListImpl();
        EmployeeListImpl employeeList = new EmployeeListImpl();
        InsuranceListImpl insuranceList = new InsuranceListImpl();
        ContractListImpl contractList = new ContractListImpl();
        PaymentListImpl paymentList = new PaymentListImpl();

        // 테스트 더미 데이터 생성
        new TestDevData(employeeList).createEmployee();


        map.put(UserType.GUEST,new GuestViewLogic(insuranceList, contractList, customerList));
        map.put(UserType.CUSTOMER, new CustomerViewLogic(customerList, contractList, insuranceList, paymentList));
        map.put(UserType.SALES, new SalesViewLogic(insuranceList, contractList, customerList, employeeList));
        map.put(UserType.DEV, new DevViewLogic(employeeList, insuranceList));
        map.put(UserType.UW, new UWViewLogic(employeeList, customerList, insuranceList, contractList));
        map.put(UserType.COMP, new CompVIewLogic());
    }


    public void run() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            try {
                createMenu("<<유저 타입>>", "보험가입희망자", "고객", "영업팀", "언더라이팅팀", "개발팀", "보상팀");
                System.out.println("0. 종료하기");
                int userType = sc.nextInt();
                UserType[] values = UserType.values();

                if (userType == 0) {
                    throw new MyCloseSequence();
                }
                UserType type = values[userType - 1];
                while (true) {
                    ViewLogic viewLogic = map.get(type);
                    viewLogic.showMenu();
                    System.out.println("0 : 취소하기");
                    System.out.println("exit : 시스템 종료");
                    String command = sc.next();
                    if (command.equals("0"))
                        break;
                    if (command.equals("exit"))
                        throw new MyCloseSequence();
                    viewLogic.work(command);
                }
            } catch (ArrayIndexOutOfBoundsException | InputMismatchException | MyIllegalArgumentException e) {
                System.out.println("정확한 값을 입력해주세요.");
            } catch (MyCloseSequence e) {
                System.out.println("시스템을 종료합니다.");
                System.exit(0);
            }
        }
    }

//    private void close(Object )
}
