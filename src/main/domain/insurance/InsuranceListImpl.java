package main.domain.insurance;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:02
 */
public class InsuranceListImpl implements InsuranceList {

	private ArrayList<Insurance> insuranceList = new ArrayList<Insurance>();

	public InsuranceListImpl(){
	}

	@Override
	public boolean create(Insurance insurance) {
		if(this.insuranceList.add(insurance)) return true;
		else return false;
	}

	@Override
	public Insurance read(int id) {
		for(Insurance insurance : this.insuranceList)
			if(insurance.getId() == id)
				return insurance;
		return null;
	}

	public ArrayList<Insurance> readByEid(int eid){
		ArrayList<Insurance> eInsuranceList = new ArrayList<>();
		for(Insurance insurance : this.insuranceList){
			if(insurance.getDevInfo().getEmployeeId() == eid)
				eInsuranceList.add(insurance);
		}
		return eInsuranceList;
	}

	@Override
	public boolean delete(int id) {
		boolean delete = false;
		Iterator it = this.insuranceList.iterator();
		while(it.hasNext()){
			Insurance insurance = (Insurance) it.next();
			if(insurance.getId() == id) {
				it.remove();
				delete = true;
				break;
			}
		}
		return delete;
	}


//	public boolean create(Insurance insurance){
//		if(this.insuranceList.add(insurance)) return true;
//		return false;
//	}
//
//	public boolean delete(String insuranceId){
//		if(this.insuranceList.remove(insuranceId)) return true;
//		return false;
//	}
//
//	public Insurance read(String insuranceId){
//		for(Insurance insurance : this.insuranceList)
//			if(insurance.getId() == Integer.parseInt(insuranceId))
//				return insurance;
//		return null;
//	}

//	public void update(){}

}