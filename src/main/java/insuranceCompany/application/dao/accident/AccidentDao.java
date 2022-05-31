package insuranceCompany.application.dao.accident;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.accident.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName :  domain.accident
 * fileName : JDBCAccidentListImpl
 * author :  규현
 * date : 2022-05-24
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-24                규현             최초 생성
 */
public class AccidentDao extends Dao implements AccidentList {

    public AccidentDao() {
        super.connect();
    }

    @Override
    public void create(Accident accident) {

        String query = "insert into accident (accident_type, employee_id, customer_id, loss_reserves, date_of_accident, date_of_report) values ('%s', '%d','%d','%d','%s', '%s')";
        String formattedQuery =  String.format(query, accident.getAccidentType().name(), accident.getEmployeeId(), accident.getCustomerId(), accident.getLossReserves(), accident.getDateOfAccident().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ,accident.getDateOfReport().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        int id = super.create(formattedQuery);
        accident.setId(id);

        String detail_query = "";
        String detailFormat = "";

        switch (accident.getAccidentType()) {
            case CARACCIDENT -> {detail_query = "insert into car_accident (accident_id, car_no, place_address, opposing_driver_phone, is_request_on_site, error_rate) values ('%d', '%s', '%s', '%s', '%d', '%d')";
                detailFormat = String.format(detail_query,accident.getId(),((CarAccident)accident).getCarNo(),((CarAccident)accident).getPlaceAddress(), ((CarAccident)accident).getOpposingDriverPhone(), ((CarAccident)accident).isRequestOnSite() ? 1 : 0
                ,((CarAccident)accident).getErrorRate());
            }
            case CARBREAKDOWN -> {detail_query = "insert into car_breakdown (accident_id, car_no, place_address, symptom) values ('%d', '%s', '%s','%s')";
            detailFormat = String.format(detailFormat, accident.getId(), ((CarBreakdown)accident).getCarNo(), ((CarBreakdown)accident).getPlaceAddress(), ((CarBreakdown)accident).getSymptom());
            }
            case FIREACCIDENT -> {detail_query = "insert into fire_accident (accident_id, place_address) values ('%d', '%s')";
            detailFormat = String.format(detail_query, accident.getId(), ((FireAccident)accident).getPlaceAddress());
            }
            case INJURYACCIDENT -> {detail_query = "insert into injury_accident (accident_id, injury_site) values ('%d', '%s')";
            detailFormat = String.format(detail_query, accident.getId(), ((InjuryAccident)accident).getInjurySite());
            }
        }

        super.create(detailFormat);
        close();
    }

    @Override
    public Accident read(int id) {

        String query = "select * from accident where accident_id = %d";
        String formatted = String.format(query,id);

        Accident ac = null;
        ResultSet rs = super.read(formatted);
        try {
            if (rs.next()) {
                AccidentType accident_type = AccidentType.valueOf(rs.getString("accident_type").toUpperCase());
                switch (accident_type) {
                    case CARACCIDENT ->{
                        String detailQuery = "select * from car_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new CarAccident();
                        if (detailRs.next()) {
                            ((CarAccident) ac).setCarNo(detailRs.getString("car_no"))
                                    .setPlaceAddress(detailRs.getString("place_address"))
                                    .setOpposingDriverPhone(detailRs.getString("opposing_driver_phone"))
                                    .setRequestOnSite(detailRs.getInt("is_request_on_site") == 1 ? true : false)
                                    .setErrorRate(detailRs.getInt("error_rate"));

                        }
                    }
                    case CARBREAKDOWN -> {
                        String detailQuery = "select * from car_breakdown where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new CarBreakdown();
                        if (detailRs.next()) {
                            ((CarBreakdown) ac).setCarNo(detailRs.getString("car_no"))
                                    .setPlaceAddress(detailRs.getString("place_address"))
                                    .setSymptom(detailRs.getString("symptom"));
                        }
                    }
                    case INJURYACCIDENT -> {
                        String detailQuery = "select * from injury_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new InjuryAccident();
                        if (detailRs.next()) {
                            ((InjuryAccident) ac).setInjurySite(detailRs.getString("injury_site"));
                        }
                    }
                    case FIREACCIDENT -> {
                        String detailQuery = "select * from fire_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new FireAccident();
                        if (detailRs.next()) {
                            ((FireAccident) ac).setPlaceAddress(detailRs.getString("place_address"));
                        }
                    }
                }
                ac.setId(rs.getInt("accident_id"))
                        .setCustomerId(rs.getInt("customer_id"))
                        .setEmployeeId(rs.getInt("employee_id"))
                        .setAccidentType(accident_type)
                        .setDateOfAccident(rs.getTimestamp("date_of_accident").toLocalDateTime())
                        .setDateOfReport(rs.getTimestamp("date_of_report").toLocalDateTime())
                        .setLossReserves(rs.getInt("loss_reserves"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        if (ac == null) {
            throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 정보가 존재하지 않습니다.");
        }
        return ac;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public List<Accident> readAllByCustomerId(int customerId) {
        String query = "select * from accident where customer_Id = %d";
        String formatted = String.format(query,customerId);
        List<Accident> accidents = getAccidents(formatted);
        close();
        if(accidents.size() == 0)
        throw new IllegalArgumentException("고객 아이디 ["+customerId+"]에 해당하는 사고 정보가 존재하지 않습니다.");
        return  accidents;
    }

    @Override
    public List<Accident> readAllByEmployeeId(int employeeId) {
        String query = "select * from accident where employee_Id = %d";
        String formatted = String.format(query,employeeId);

        List<Accident> accidents = getAccidents(formatted);
        close();
//        if(accidents.size()==0)
//            throw new IllegalArgumentException("보상팀 아이디 ["+employeeId+"]에 해당하는 사고 정보가 존재하지 않습니다.");
        return accidents;
    }

    private List<Accident> getAccidents(String formatted) {
        ResultSet rs = super.read(formatted);
        List<Accident> accidentList = new ArrayList<>();

        try {
            while (rs.next()) {
                Accident ac = null;
                int id = rs.getInt("accident_id");
                AccidentType accident_type = AccidentType.valueOf(rs.getString("accident_type").toUpperCase());
                switch (accident_type) {
                    case CARACCIDENT ->{
                        String detailQuery = "select * from car_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new CarAccident();
                        if (detailRs.next()) {
                            ((CarAccident) ac).setCarNo(detailRs.getString("car_no"))
                                    .setPlaceAddress(detailRs.getString("place_address"))
                                    .setOpposingDriverPhone(detailRs.getString("opposiong_driver_phone"))
                                    .setRequestOnSite(detailRs.getInt("is_request_on_site") == 1 ? true : false)
                                    .setErrorRate(detailRs.getInt("error_rate"));

                        }
                    }
                    case CARBREAKDOWN -> {
                        String detailQuery = "select * from car_breakdown where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new CarBreakdown();
                        if (detailRs.next()) {
                            ((CarBreakdown) ac).setCarNo(detailRs.getString("car_no"))
                                    .setPlaceAddress(detailRs.getString("place_address"))
                                    .setSymptom(detailRs.getString("symptom"));
                        }
                    }
                    case INJURYACCIDENT -> {
                        String detailQuery = "select * from injury_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new InjuryAccident();
                        if (detailRs.next()) {
                            ((InjuryAccident) ac).setInjurySite(detailRs.getString("injury_site"));
                        }
                    }
                    case FIREACCIDENT -> {
                        String detailQuery = "select * from fire_accident where accident_id = %d";
                        String detailFormatted = String.format(detailQuery, id);
                        ResultSet detailRs = super.read(detailFormatted);
                        ac = new FireAccident();
                        if (detailRs.next()) {
                            ((FireAccident) ac).setPlaceAddress(detailRs.getString("place_address"));
                        }
                    }
                }
                ac.setId(rs.getInt("accident_id"))
                        .setCustomerId(rs.getInt("customer_id"))
                        .setEmployeeId(rs.getInt("employee_id"))
                        .setAccidentType(accident_type)
                        .setDateOfAccident(rs.getTimestamp("date_of_accident").toLocalDateTime())
                        .setDateOfReport(rs.getTimestamp("date_of_report").toLocalDateTime())
                        .setLossReserves(rs.getInt("loss_reserves"));
                accidentList.add(ac);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return accidentList;
    }

    @Override
    public void updateLossReserve(Accident accident) {
        String query = "update accident set loss_reserves = %d where accident_id = %d";
        String formatted = String.format(query,accident.getLossReserves(),accident.getId());
        super.update(formatted);
        close();
    }

    @Override
    public void updateLossReserveAndErrorRate(Accident accident) {
        String query = "update accident set loss_reserves = %d where accident_id = %d";
        String formatted = String.format(query,accident.getLossReserves(),accident.getId());
        super.update(formatted);

        String detailQuery = "update car_accident set error_rate = %d where accident_id = %d";
        String detailFormatted = String.format(detailQuery,((CarAccident)accident).getErrorRate(),accident.getId());
        System.out.println(detailFormatted);
        super.update(detailFormatted);
        close();
    }

    @Override
    public void updateCompEmployeeId(Accident accident) {
        String query = "update accident set employee_id = %d where accident_id = %d";
        String formatted = String.format(query,accident.getEmployeeId(),accident.getId());
        super.update(formatted);
        close();
    }


    @Override
    public boolean delete(int id) {
        String query = "delete from accident where accident_id = %d";
        String formattedQuery = String.format(query,id);
        boolean result = super.delete(formattedQuery);
        close();
        if(!result)
            throw new IllegalArgumentException("사고 아이디["+id+"]에 해당하는 사고 정보가 존재하지 않습니다.");

        return true;
    }

}
