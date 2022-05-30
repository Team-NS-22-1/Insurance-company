package application;

import application.viewlogic.*;
import dao.*;
import domain.accident.AccidentList;
import domain.accident.accDocFile.AccDocFileList;
import domain.complain.ComplainList;
import domain.contract.ContractList;
import domain.contract.ContractListImpl;
import domain.customer.CustomerList;
import domain.employee.EmployeeList;
import domain.employee.EmployeeListImpl;
import domain.insurance.InsuranceDetailList;
import domain.insurance.InsuranceDetailListImpl;
import domain.insurance.InsuranceList;
import domain.insurance.InsuranceListImpl;
import domain.payment.PaymentList;
import exception.MyCloseSequence;
import exception.MyIllegalArgumentException;
import test.TestData;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static utility.MessageUtil.createMenu;


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
        CustomerList customerList = new CustomerDao();
        EmployeeList employeeList = new EmployeeListImpl();
        InsuranceList insuranceList = new InsuranceListImpl();
        InsuranceDetailList insuranceDetailList = new InsuranceDetailListImpl();
        ContractList contractList = new ContractListImpl();
        PaymentList paymentList = new PaymentDao();
        AccidentList accidentList = new AccidentDao();
        AccDocFileList accDocFileList = new AccDocFileDao();
        ComplainList complainList = new ComplainDao();

        // 테스트 더미 데이터 생성
        new TestData();

        map.put(UserType.GUEST,new GuestViewLogic());
        map.put(UserType.CUSTOMER, new CustomerViewLogic( customerList, contractList, insuranceList, paymentList,accidentList,accDocFileList, employeeList,complainList));
        map.put(UserType.SALES, new SalesViewLogic());
        map.put(UserType.DEV, new DevViewLogic(employeeList, insuranceList, insuranceDetailList));
        map.put(UserType.UW, new UWViewLogic(employeeList, customerList, insuranceList, contractList));
        map.put(UserType.COMP, new CompVIewLogic(employeeList,accidentList,accDocFileList,customerList));
    }


    public void run() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            try {
                createMenu("<<유저 타입>>", "보험가입희망자", "고객", "영업팀", "언더라이팅팀", "개발팀", "보상팀");
                System.out.println("0. 종료하기");
                int userType = Integer.parseInt(sc.nextLine());
                UserType[] values = UserType.values();

                if (userType == 0) {
                    throw new MyCloseSequence();
                }
                UserType type = values[userType - 1];
                while (true) {
                    ViewLogic viewLogic = map.get(type);
                    viewLogic.showMenu();
                    String command = sc.nextLine();
                    if (command.equals("0"))
                        break;
                    if (command.equalsIgnoreCase("EXIT")) {
                        throw new MyCloseSequence();
                    }
                    viewLogic.work(command);
                }
            } catch (ArrayIndexOutOfBoundsException | InputMismatchException | MyIllegalArgumentException | NullPointerException e) {
                System.out.println("정확한 값을 입력해주세요.");
            } catch (MyCloseSequence e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 메뉴를 입력해주세요");
            }
        }
    }
}
