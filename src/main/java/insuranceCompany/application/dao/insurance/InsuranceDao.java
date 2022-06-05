package insuranceCompany.application.dao.insurance;


import insuranceCompany.application.dao.CrudInterface;
import insuranceCompany.application.domain.insurance.Insurance;

import java.util.ArrayList;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:01
 */
public interface InsuranceDao extends CrudInterface<Insurance> {
    ArrayList<Insurance> readAll();
    ArrayList<Insurance> readByEmployeeId(int eid);
    void updateByFss(Insurance insurance);
    void updateByIso(Insurance insurance);
    void updateByProd(Insurance insurance);
    void updateBySrActuary(Insurance insurance);
    void updateBySalesAuthState(Insurance insurance);
}