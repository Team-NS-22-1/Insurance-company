package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIOException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.Login;
import insuranceCompany.application.login.UserType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static insuranceCompany.application.global.constant.LoginViewLogicConstants.*;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndExitQuery;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuOnlyExitQuery;

public class LoginViewLogic implements ViewLogic {

    private MyBufferedReader br;

    public LoginViewLogic() {
        br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String showMenu() {
        return createMenuOnlyExitQuery(MENU_TITLE_LOGIN_VIEW_LOGIC, MENU_ELEMENTS_LOGIN_VIEW_LOGIC);
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                case "1" -> menuCustomerLogin();
                case "2" -> menuEmployeeLogin();
            }
        }
        catch (IOException e) {
            throw new MyIOException();
        }
    }

    private void menuCustomerLogin() throws IOException {
        Scanner sc = new Scanner(System.in);
        loop: while(true) {
            switch (br.verifyMenu(createMenuAndExitQuery(MENU_TITLE_LOGIN_CUSTOMER, MENU_ELEMENTS_LOGIN_CUSTOMER), 2)) {
                case 1 -> {
                    Customer customer = new Login().loginCustomer();
                    if (customer == null) break;
                    System.out.printf(MSG_WELCOME_CUSTOMER, customer.getName());
                    while (customer != null) {
                        CustomerViewLogic customerViewLogic = new CustomerViewLogic(customer);
                        String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(),4));
                        customer = isLogoutCustomer(customer, command);
                        customerViewLogic.work(command);
                    }
                }
                case 2 -> {
                    while(true){
                        CustomerViewLogic customerViewLogic = new CustomerViewLogic();
                        customerViewLogic.showMenu();
                        String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(), 1));
                        if(command.equals("0")) break;
                        customerViewLogic.work(command);
                    }
                }
                default -> {
                    break loop;
                }
            }
        }
    }

    private void menuEmployeeLogin() throws IOException {
        Scanner sc = new Scanner(System.in);
        Employee employee = new Login().loginEmployee();
        if(employee==null) return;
        System.out.printf(MSG_WELCOME_EMPLOYEE, employee.getDepartment().name(), employee.getPosition().name(), employee.getName());
        while(employee!=null) {
            switch (employee.getDepartment()) {
                case ADMIN -> {
                    while(true) {
                        AdminViewLogic adminViewLogic = new AdminViewLogic(employee);
                        adminViewLogic.showMenu();
                        int userType = Integer.parseInt(sc.nextLine());
                        employee = isLogoutEmployee(employee, String.valueOf(userType));
                        if (employee == null) break;
                        UserType[] values = UserType.values();
                        UserType type = values[userType - 1];
                        while (employee != null) {
                            ViewLogic viewLogic = adminViewLogic.getMap().get(type);
                            viewLogic.showMenu();
                            String command = sc.nextLine();
                            employee = isLogoutAdmin(employee, command);
                            viewLogic.work(command);
                        }
                    }
                }
                case DEV -> {
                    DevelopViewLogic developViewLogic = new DevelopViewLogic(employee);
                    String command = String.valueOf(br.verifyMenu(developViewLogic.showMenu(), 2));
                    employee = isLogoutEmployee(employee, command);
                    developViewLogic.work(command);
                }
                case UW -> {
                    UnderwritingViewLogic underwritingViewLogic = new UnderwritingViewLogic(employee);
                    String command = String.valueOf(br.verifyMenu(underwritingViewLogic.showMenu(), 1));
                    employee = isLogoutEmployee(employee, command);
                    underwritingViewLogic.work(command);
                }
                case COMP -> {
                    CompensationViewLogic compensationViewLogic = new CompensationViewLogic(employee);
                    ;
                    String command = String.valueOf(br.verifyMenu(compensationViewLogic.showMenu(), 3));
                    employee = isLogoutEmployee(employee, command);
                    compensationViewLogic.work(command);
                }
                case SALES -> {
                    SalesViewLogic salesViewLogic = new SalesViewLogic(employee);
                    String command = String.valueOf(br.verifyMenu(salesViewLogic.showMenu(), 1));
                    employee = isLogoutEmployee(employee, command);
                    salesViewLogic.work(command);
                }
            }
        }
    }

    private Customer isLogoutCustomer(Customer customer, String command) {
        if (checkLogoutOrExit(command)) {
            customer = null;
            System.out.println(SUCCESS_LOGOUT);
        }
        return customer;
    }

    private Employee isLogoutEmployee(Employee employee, String command) {
        if (checkLogoutOrExit(command)) {
            employee = null;
            System.out.println(SUCCESS_LOGOUT);
        }
        return employee;
    }

    private Employee isLogoutAdmin(Employee employee, String command) {
        if (checkLogoutOrExit(command))
            employee = null;
        return employee;
    }

    private boolean checkLogoutOrExit(String command) {
        if (command.equals("0"))
            return true;
        if (command.equalsIgnoreCase("EXIT"))
            throw new MyCloseSequence();
        return false;
    }


}
