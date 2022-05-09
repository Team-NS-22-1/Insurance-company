package main.domain.insurance;

import java.util.ArrayList;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class InsuaranceListImpl implements InsuaranceList {

	private ArrayList<Insuarance> insuaranceList = new ArrayList<Insuarance>();
	public Insuarance m_Insuarance;
	
	@Override
	public boolean add(Insuarance insuarance) {
		if (this.insuaranceList.add(insuarance)) return true;
		else return false;
	}
	@Override
	public boolean delete(int insuranceId) {
		if (this.insuaranceList.remove(insuranceId) != null) return true;
		else return false;
	}
	@Override
	public Insuarance get(int insuranceId) {
		
		for (Insuarance insuarance : this.insuaranceList) {
			
			if (insuarance.getId() == insuranceId) {
				return insuarance;
			}
		}
		return null;
	}
	



}