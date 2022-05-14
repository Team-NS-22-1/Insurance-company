package main.domain.insurance;

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

	public ArrayList<Insurance> readByEid(int eid){
		ArrayList<Insurance> insuranceArrayList = new ArrayList<>(this.insuranceList.values());
		ArrayList<Insurance> eInsuranceList = new ArrayList<>();
		for(Insurance insurance : insuranceArrayList){
			if(insurance.getDevInfo().getEmployeeId() == eid)
				eInsuranceList.add(insurance);
		}
		return eInsuranceList;
	}

	@Override
	public boolean delete(int id) {
		Insurance insurance = this.insuranceList.remove(id);
		if(insurance != null) return true;
		else return false;
	}

	// 나중에 수정
	public boolean add(Insurance insurance) {
		insurance.setId(++id);
		insuranceList.put(insurance.getId(), insurance);
		return true;
	}


//	public void update(){}

}