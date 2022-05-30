package uwDao;

import dao.ContractDao;
import domain.contract.Contract;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * packageName :  uwDao
 * fileName : ContractDaoTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class ContractDaoTest {

    ContractDao dao = new ContractDao();
    @Test
    void updateTest() throws SQLException {
        Contract con = new Contract();
        con.setPremium(10000);
        con.setCustomerId(22)
                .setEmployeeId(2)
                .setInsuranceId(3);


            dao.create(con);




    }

}