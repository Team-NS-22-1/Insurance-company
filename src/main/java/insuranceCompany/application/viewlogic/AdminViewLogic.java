package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.login.UserType;

import java.util.HashMap;
import java.util.Map;

import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndLogout;

public class AdminViewLogic implements ViewLogic {

    private Map<UserType, ViewLogic> map = new HashMap<>();

    public AdminViewLogic(Employee employee) {
        Customer customer = new CustomerDaoImpl().read(employee.getId());
        map.put(UserType.GUEST,new GuestViewLogic());
        map.put(UserType.CUSTOMER, new CustomerViewLogic(customer));
        map.put(UserType.SALES, new SalesViewLogic(employee));
        map.put(UserType.DEV, new DevelopViewLogic(employee));
        map.put(UserType.UW, new UnderwritingViewLogic(employee));
        map.put(UserType.COMP, new CompensationViewLogic(employee));
    }

    public Map<UserType, ViewLogic> getMap() {
        return map;
    }

    @Override
    public String showMenu() {
        return createMenuAndLogout("<< 관리자 메뉴 >>", "보험가입희망자", "고객", "영업팀", "언더라이팅팀", "개발팀", "보상팀");
    }

    @Override
    public void work(String command) {
    }
}
