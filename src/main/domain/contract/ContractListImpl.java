package main.domain.contract;


import main.domain.insurance.InsuranceType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public class ContractListImpl implements ContractList {

	private static Map<Integer, Contract> contractList = new HashMap<>();
	private static int idSequence;


	public ContractListImpl(){

	}

	@Override
	public boolean create(Contract contract) {
		contract.setId(++idSequence);
		contractList.put(contract.getId(), contract);
		return true;
	}

	@Override
	public Contract read(int id) {
		Contract contract = contractList.get(id);
		if(contract != null)
			return contract;
		return null;
	}

	@Override
	public boolean delete(int id) {
		Contract remove = contractList.remove(id);
		return remove!=null;
	}

	public boolean update(int id, Contract contract) {
		this.delete(id);
		contractList.put(id, contract);
		return true;
	}

	public static Map<Integer, Contract> getContractList() {
		return contractList;
	}


}