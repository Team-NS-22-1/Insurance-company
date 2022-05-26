package domain.insurance;


import ifs.CrudInterface;

import java.util.ArrayList;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:01
 */
public interface InsuranceList extends CrudInterface<Insurance> {
    ArrayList<Insurance> readAll();
//    int readPremium(int id);
    ArrayList<Insurance> readByEid(int eid);
}