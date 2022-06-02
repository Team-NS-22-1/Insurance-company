package insuranceCompany.application.viewlogic;

import insuranceCompany.application.login.UserType;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIOException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.Login;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static insuranceCompany.application.global.utility.MessageUtil.*;

public class LoginViewLogic implements ViewLogic {

    private MyBufferedReader br;

    public LoginViewLogic() {
        br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String showMenu() {
        return createMenuOnlyExitQuery("<< INSURANCE-COMPANY >>", "고객", "직원");
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
            switch (br.verifyMenu(createMenuAndExitQuery("<< 고객 >>", "회원", "비회원"), 2)) {
                case 1 -> {
                    Customer customer = new Login().loginCustomer();
                    if (customer == null) break;
                    System.out.println("어서오세요! " + customer.getName() + " 고객님.\n");
                    while (customer != null) {
                        CustomerViewLogic customerViewLogic = new CustomerViewLogic(customer);
                        String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(),4));
                        customer = isLogoutCustomer(customer, command);
                        customerViewLogic.work(command);
                    }
                }
                case 2 -> {
                    CustomerViewLogic customerViewLogic = new CustomerViewLogic();
                    customerViewLogic.showMenu();
                    String command = String.valueOf(br.verifyMenu(customerViewLogic.showMenu(), 1));
                    customerViewLogic.work(command);
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
        System.out.println("LOGIN:: " + "부서[" + employee.getDepartment().name() + "] 직책[" + employee.getPosition().name() + "] " + employee.getName() + "\n");
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
            System.out.println("정상적으로 로그아웃되었습니다!\n");
        }
        return customer;
    }

    private Employee isLogoutEmployee(Employee employee, String command) {
        if (checkLogoutOrExit(command)) {
            employee = null;
            System.out.println("정상적으로 로그아웃되었습니다!\n");
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
