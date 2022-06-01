package dao;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.InjuryAccident;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  dao
 * fileName : AccidentDaoTest
 * author :  규현
 * date : 2022-05-28
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-28                규현             최초 생성
 */
class AccidentDaoTest {

    Dao dao = new Dao();
    AccidentDao ad = new AccidentDao();

    @Test
    void ad_test() {
        InjuryAccident accident = new InjuryAccident();
        accident.setInjurySite("asdasd")
                .setCustomerId(1)
                .setEmployeeId(1)
                .setLossReserves(123)
                .setDateOfAccident(LocalDateTime.now())
                .setDateOfReport(LocalDateTime.now())
                .setAccidentType(AccidentType.INJURYACCIDENT);
        ad = new AccidentDao();
        ad.create(accident);

        ad = new AccidentDao();
        Accident read = ad.read(accident.getId());
        read.printForCustomer();
    }

    @Test
    void create() {

        InjuryAccident accident = new InjuryAccident();
        accident.setInjurySite("asdasd")
                .setCustomerId(1)
                .setEmployeeId(1)
                .setLossReserves(123)
                .setDateOfAccident(LocalDateTime.now())
                .setDateOfReport(LocalDateTime.now())
                .setAccidentType(AccidentType.INJURYACCIDENT);



        String query = "insert into accident (accident_type, employee_id, customer_id, loss_reserves, date_of_accident, date_of_report) values ('%s', '%d','%d','%d','%s', '%s')";
        String formattedQuery =  String.format(query, accident.getAccidentType().name(), accident.getEmployeeId(), accident.getCustomerId(), accident.getLossReserves(), accident.getDateOfAccident().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ,accident.getDateOfReport().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        int id = dao.create(formattedQuery);
        accident.setId(id);

        String detail_query = "";
        String detailFormat = "";


                detail_query = "insert into injury_accident (accident_id, injury_site) values ('%d', '%s')";
                detailFormat = String.format(detail_query, accident.getId(), ((InjuryAccident)accident).getInjurySite());
        System.out.println(formattedQuery);
        System.out.println(detailFormat);

        int i = dao.create(detailFormat);

    }

    @Test
    void read() throws SQLException {
        InjuryAccident accident = new InjuryAccident();
        accident.setInjurySite("asdasd")
                .setCustomerId(1)
                .setEmployeeId(1)
                .setLossReserves(123)
                .setDateOfAccident(LocalDateTime.now())
                .setDateOfReport(LocalDateTime.now())
                .setAccidentType(AccidentType.INJURYACCIDENT);


        int id = 13;
        String query = "select * from accident join injury_accident on accident.accident_id = injury_accident.accident_id where accident.accident_id = 13";
        ResultSet rs = dao.read(query);
        Accident ac = null;
        if (rs.next()) {

            AccidentType accident_type = AccidentType.valueOf(rs.getString("accident_type").toUpperCase());
            switch (accident_type) {
                case INJURYACCIDENT -> {
                    ac = new InjuryAccident();
                    ((InjuryAccident)ac).setInjurySite(rs.getString("injury_site"));
                }
            }
            Timestamp date_of_accident = rs.getTimestamp("date_of_accident");

            ac.setId(rs.getInt("accident_id"))
                    .setCustomerId(rs.getInt("customer_id"))
                    .setEmployeeId(rs.getInt("employee_id"))
                    .setAccidentType(accident_type)
                    .setDateOfAccident(date_of_accident.toLocalDateTime())
                    .setDateOfReport(rs.getTimestamp("date_of_report").toLocalDateTime());

            ac.printForCustomer();
            ac.printForComEmployee();


        }
    }

    @Test
    void read2() throws SQLException {
        Accident read = ad.read(3);
        read.printForComEmployee();
        read.printForComEmployee();

//        assertEquals(read.getId(),14);
    }
    @Test
    void readAllByCustomerId() {
        List<Accident> accidentList = ad.readAllByCustomerId(1);
        for (Accident accident : accidentList) {
            accident.printForCustomer();
            accident.printForComEmployee();
        }

        assertEquals(accidentList.size(),3);
    }
    @Test
    void readAllByCustomerId_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,()->ad.readAllByCustomerId(3));
    }

    @Test
    void readAllByEmployeeId() {
        List<Accident> accidentList = ad.readAllByEmployeeId(1);
        for (Accident accident : accidentList) {
            accident.printForCustomer();
            accident.printForComEmployee();
        }

        assertEquals(accidentList.size(),3);
    }

    @Test
    void readAllByEmployeeId_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,()->ad.readAllByEmployeeId(3));
    }

    @Test
    void updateLossReserve() {
        Accident read = ad.read(14);
        read.setLossReserves(100000);
        ad.updateLossReserve(read);

        Accident updated = ad.read(14);
        updated.printForCustomer();
        assertEquals(updated.getLossReserves(),100000);
    }

    @Test
    void updateLossReserveAndErrorRate() {
        CarAccident car = new CarAccident();
        car.setCarNo("test")
                .setPlaceAddress("terst").setOpposingDriverPhone("asd")
                .setRequestOnSite(true)
                .setAccidentType(AccidentType.CARACCIDENT)
                .setEmployeeId(2)
                .setCustomerId(2)
                .setDateOfAccident(LocalDateTime.now())
                .setDateOfReport(LocalDateTime.now());


        ad.create(car);

        Accident read = ad.read(car.getId());
        ((CarAccident)read).setErrorRate(90);
        read.setLossReserves(1000);
        ad.updateLossReserveAndErrorRate(read);

        Accident updated = ad.read(read.getId());
        assertEquals(updated.getLossReserves(),1000);
        assertEquals(((CarAccident)updated).getErrorRate(),90);
    }

    @Test
    void delete() {
//        CarAccident car = new CarAccident();
//        car.setCarNo("test")
//                .setPlaceAddress("terst").setOpposingDriverPhone("asd")
//                .setRequestOnSite(true)
//                .setAccidentType(AccidentType.CARACCIDENT)
//                .setEmployeeId(2)
//                .setCustomerId(2)
//                .setDateOfAccident(LocalDateTime.now())
//                .setDateOfReport(LocalDateTime.now());
//
//
//        ad.create(car);

        ad.delete(2);


    }

}