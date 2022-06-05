package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccDocType;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccidentDocumentFile;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.global.exception.MyInvalidAccessException;
import insuranceCompany.application.global.exception.NoResultantException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static insuranceCompany.application.global.constant.CompensationViewLogicConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CompensationViewLogicTest {



    @Test
    void exception_test_loss_reserve() {
        CarAccident car = new CarAccident();
        try {
            isValidAccident(car);
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
        }
        assertThrows(MyInvalidAccessException.class, () -> isValidAccident(car));
    }
    @Test
    void exception_test_acc_doc_file() {
        CarAccident car = new CarAccident();
        car.setLossReserves(1000L);
        try {
            isValidAccident(car);
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
        }
        assertThrows(MyInvalidAccessException.class, () -> isValidAccident(car));
    }

    @Test
    void exception_test_error_rate() {
        CarAccident car = new CarAccident();
        car.setLossReserves(1000L);
        car.getAccDocFileList().put(AccDocType.INVESTIGATEACCIDENT, new AccidentDocumentFile());
        try {
            isValidAccident(car);
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
        }
        assertThrows(MyInvalidAccessException.class, () -> isValidAccident(car));
    }

    @Test
    void getAccidentList_test() {
//        getAccidentList()
        assertNull(getAccidentList());
    }

    private List<Accident> getAccidentList() {
        List<Accident> accidents = null;
        try {
            EmployeeDao dao = new EmployeeDaoImpl();
            Employee employee = dao.read(3);
            accidents = employee.readAccident();
        } catch (NoResultantException e) {
            System.out.println(e.getMessage());
        }
        return accidents;
    }


    private void isValidAccident(Accident accident) {
        if (accident.getLossReserves() == 0 ) {
            throw new MyInvalidAccessException(LOSS_RESERVE_EXCEPTION);
        }
        if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
            throw new MyInvalidAccessException(INVESTIGATE_ACCIDENT_EXCEPTION);
        }
        if (accident instanceof CarAccident car && car.getErrorRate() == 0) {
            throw new MyInvalidAccessException(ERROR_RATE_EXCEPTION);
        }
    }

}