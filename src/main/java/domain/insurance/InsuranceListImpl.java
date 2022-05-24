package domain.insurance;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:02
 */
public class InsuranceListImpl implements InsuranceList {

	private static HashMap<Integer, Insurance> insuranceList = new HashMap<>();

	private static int id = 0;

	public InsuranceListImpl(){
	}

	@Override
	public void create(Insurance insurance) {
		this.insuranceList.put(insurance.setId(++id).getId(), insurance);
	}

	@Override
	public Insurance read(int id) {
		Insurance insurance = this.insuranceList.get(id);
		if(insurance != null) return insurance;
		else return null;
	}

	@Override
	public boolean update(int id) {
		return false;
	}

	public ArrayList<Insurance> readByEid(int eid){
		ArrayList<Insurance> insuranceArrayList = new ArrayList<>(this.insuranceList.values());
		ArrayList<Insurance> eInsuranceList = new ArrayList<>();
		for(Insurance insurance : insuranceArrayList){
			if(insurance.getDevInfo().getEmployeeId() == eid)
				eInsuranceList.add(insurance);
		}
		return eInsuranceList;
	}

	public ArrayList<Insurance> readAll() {
		ArrayList<Insurance> insuranceArrayList = new ArrayList<>(this.insuranceList.values());
		return insuranceArrayList;
	}

	// 임시
	public int readPremium(int id) {
		Insurance insurance = this.insuranceList.get(id);
		if(insurance != null) return insurance.premium;
		else return 0;
	}
	//

	@Override
	public boolean delete(int id) {
		Insurance insurance = this.insuranceList.remove(id);
		if(insurance != null) return true;
		else return false;
	}


//	public void update(){}

}