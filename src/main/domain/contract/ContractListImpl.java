package main.domain.contract;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public class ContractListImpl implements ContractList {

	private HashMap<Integer, Contract> contractMap = new HashMap<>();

	public ContractListImpl(){

	}

	@Override
	public boolean create(Contract contract) {



		return false;
	}

	@Override
	public Contract read(int id) {
		return null;
	}

	@Override
	public boolean delete(int id) {
		return false;
	}
}