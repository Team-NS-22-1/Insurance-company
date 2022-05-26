package domain.insurance;

import java.util.ArrayList;
import java.util.HashMap;

public class InsuranceDetailListImpl implements InsuranceDetailList {

    private static HashMap<Integer, InsuranceDetail> insuranceDetailList = new HashMap<>();

    private static int id = 0;

    @Override
    public void create(InsuranceDetail insuranceDetail) {
        insuranceDetailList.put(insuranceDetail.setId(++id).getId(), insuranceDetail);
    }

    @Override
    public InsuranceDetail read(int id) {
        InsuranceDetail insuranceDetail = insuranceDetailList.get(id);
        if(insuranceDetail != null) return insuranceDetail;
        else return null;
    }

    public ArrayList<InsuranceDetail> readByInsuranceId(int insuranceId){
        ArrayList<InsuranceDetail> returnList = new ArrayList<>();
        for(InsuranceDetail insuranceDetail : insuranceDetailList.values()){
            if(insuranceDetail.getInsuranceId() == insuranceId)
                returnList.add(insuranceDetail);
        }
        return returnList;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        InsuranceDetail insuranceDetail = insuranceDetailList.remove(id);
        return insuranceDetail != null;
    }
}
