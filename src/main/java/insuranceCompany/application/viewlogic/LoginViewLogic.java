package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.Login;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndClose;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuOnlyExit;

public class LoginViewLogic implements ViewLogic {

    private MyBufferedReader br;

    public LoginViewLogic() {
        br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void showMenu() {
        createMenuOnlyExit("<< INSURANCE-COMPANY >>", "고객", "직원");
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                case "1" -> {
                    menuCustomerLogin();
                }
                case "2" -> {
                    menuEmployeeLogin();
                }
            }
        }
        catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        }
    }

    private void menuCustomerLogin() throws IOException {
        Scanner sc = new Scanner(System.in);
        createMenuAndClose("<< 고객 >>", "회원", "비회원");
        switch (br.verifyMenu("", 2)) {
            case 1 -> {
                Login login = new Login();
                Customer customer = login.loginCustomer();
                while(customer != null) {
                    CustomerViewLogic customerViewLogic = new CustomerViewLogic(customer);
                    customerViewLogic.showMenu();
                    String command = sc.nextLine();
                    customerViewLogic.work(command);
                }
            }
            case 2 -> {
                GuestViewLogic guestViewLogic = new GuestViewLogic();
                guestViewLogic.showMenu();
                String command = sc.nextLine();
                guestViewLogic.work(command);
            }
        }

    }

    private void menuEmployeeLogin() throws IOException {
        Scanner sc = new Scanner(System.in);
        Login login = new Login();
        Employee employee = login.loginEmployee();
        System.out.println("LOGIN:: " + "부서[" + employee.getDepartment().name() + "] 직책[" + employee.getPosition().name() + "] " + employee.getName() + "\n");
        while(employee!=null) {
            switch (employee.getDepartment()) {
                case ADMIN -> {

                }
                case DEV -> {
                    DevelopViewLogic developViewLogic = new DevelopViewLogic(employee);
                    developViewLogic.showMenu();
                    String command = sc.nextLine();
                    employee = isLogout(employee, command);
                    developViewLogic.work(command);
                }
                case UW -> {
                    UnderwritingViewLogic underwritingViewLogic = new UnderwritingViewLogic(employee);
                    underwritingViewLogic.showMenu();
                    String command = sc.nextLine();
                    employee = isLogout(employee, command);
                    underwritingViewLogic.work(command);
                }
                case COMP -> {
                    CompensationViewLogic compensationViewLogic = new CompensationViewLogic(employee);
                    compensationViewLogic.showMenu();
                    String command = sc.nextLine();
                    employee = isLogout(employee, command);
                    compensationViewLogic.work(command);
                }
                case SALES -> {
                    SalesViewLogic salesViewLogic = new SalesViewLogic(employee);
                    salesViewLogic.showMenu();
                    String command = sc.nextLine();
                    employee = isLogout(employee, command);
                    salesViewLogic.work(command);
                }
            }
        }
    }

    private Employee isLogout(Employee employee, String command) {
        if (checkLogoutOrExit(command)) {
            employee = null;
            System.out.println("정상적으로 로그아웃되었습니다!\n");
        }
        return employee;
    }

    private boolean checkLogoutOrExit(String command) {
        if (command.equals("0")) {
            return true;
        }
        if (command.equalsIgnoreCase("EXIT")) {
            throw new MyCloseSequence();
        }
        return false;
    }


}
