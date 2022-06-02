package insuranceCompany.application.dao.accident;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.complain.Complain;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName :  dao
 * fileName : ComplainDao
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
public class ComplainDaoImpl extends Dao implements ComplainDao {
    public ComplainDaoImpl() {
        super.connect();
    }

    @Override
    public void create(Complain complain) {
        String query = "insert into complain (reason, customer_id) values ('%s', %d)";
        String formattedQuery = String.format(query,complain.getReason(),complain.getCustomerId());
        int id = super.create(formattedQuery);
        complain.setId(id);
        close();
    }

    // Not Use
    @Override
    public Complain read(int id) {
        String query = "select * from complain where complain_id = %d";
        String formattedQuery = String.format(query,id);
        ResultSet rs = super.read(formattedQuery);
        Complain complain = null;
        try {
            if (rs.next()) {
                complain = getComplain(id, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        if (complain == null)
            throw new MyIllegalArgumentException(id + "에 해당하는 민원정보가 존재하지 않습니다.");
            return complain;
    }

    private Complain getComplain(int id, ResultSet rs) throws SQLException {
        Complain complain = Complain.builder().customerId(rs.getInt("customer_id"))
                .reason(rs.getString("reason"))
                .build();
        complain.setId(id);
        return complain;
    }

    @Override
    public List<Complain> readAllByCustomerId(int customerId) {
        String query = "select * from complain where customer_id = %d";
        String formattedQuery = String.format(query,customerId);
        System.out.println(formattedQuery);
        ResultSet rs = super.read(formattedQuery);
        List<Complain> complains = new ArrayList<>();
            try {
                while (rs.next()) {
                    Complain complain = null;
                    complain = getComplain(rs.getInt("complain_id"),rs);
                    complains.add(complain);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                close();
            }


        if(complains.size() == 0)
            throw new IllegalArgumentException("해당 ID로 조회되는 민원이 존재하지 않습니다.");
        return  complains;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
