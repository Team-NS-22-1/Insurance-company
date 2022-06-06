package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIOException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.Login;

import java.io.IOException;
import java.io.InputStreamReader;

import static insuranceCompany.application.global.constant.CommonConstants.*;
import static insuranceCompany.application.global.constant.CompensationViewLogicConstants.MENU_ELEMENTS_COMP_VIEW_LOGIC;
import static insuranceCompany.application.global.constant.ContractConstants.MENU_ELEMENT_GUEST_VIEW_LOGIC;
import static insuranceCompany.application.global.constant.CustomerViewLogicConstants.MENU_ELEMENTS_CUSTOMER_VIEW_LOGIC;
import static insuranceCompany.application.global.constant.DevelopViewLogicConstants.MENU_ELEMENTS_DEV_VIEW_LOGIC;
import static insuranceCompany.application.global.constant.LoginViewLogicConstants.*;
import static insuranceCompany.application.global.utility.MenuUtil.createMenuAndClose;
import static insuranceCompany.application.global.utility.MenuUtil.createMenuOnlyExitQuery;

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
                case ONE -> menuCustomerLogin();
                case TWO -> menuEmployeeLogin();
            }
        }
        catch (IOException e) {
            throw new MyIOException();
        }
    }

    private void menuCustomerLogin() throws IOException {
        loop: while(true) {
            switch (br.verifyMenu(createMenuAndClose(MENU_TITLE_LOGIN_CUSTOMER, MENU_ELEMENTS_LOGIN_CUSTOMER), MENU_ELEMENTS_LOGIN_CUSTOMER.length)) {
                case 1 -> {
                    Customer customer = new Login().loginCustomer();
                    if (customer == null) break;
                    System.out.printf(MSG_WELCOME_CUSTOMER, customer.getName());
                    while (customer != null) {
                        CustomerViewLogic customerViewLogic = new CustomerViewLogic(customer);
                        String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(), MENU_ELEMENTS_CUSTOMER_VIEW_LOGIC.length));
                        customer = isLogoutCustomer(customer, command);
                        customerViewLogic.work(command);
                    }
                }
                case 2 -> {
                    while(true){
                        CustomerViewLogic customerViewLogic = new CustomerViewLogic();
                        customerViewLogic.showMenu();
                        String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(), MENU_ELEMENT_GUEST_VIEW_LOGIC.length));
                        if(command.equals(ZERO)) break;
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
        Employee employee = new Login().loginEmployee();
        if(employee==null) return;
        System.out.printf(MSG_WELCOME_EMPLOYEE, employee.getDepartment().name(), employee.getPosition().name(), employee.getName());
        while(employee!=null) {
            switch (employee.getDepartment()) {
                case DEV -> {
                    DevelopViewLogic developViewLogic = new DevelopViewLogic(employee);
                    String command = String.valueOf(br.verifyMenu(developViewLogic.showMenu(), MENU_ELEMENTS_DEV_VIEW_LOGIC.length));
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
                    String command = String.valueOf(br.verifyMenu(compensationViewLogic.showMenu(), MENU_ELEMENTS_COMP_VIEW_LOGIC.length));
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

    private boolean checkLogoutOrExit(String command) {
        if (command.equals(ZERO))
            return true;
        if (command.equalsIgnoreCase(EXIT))
            throw new MyCloseSequence();
        return false;
    }


}
