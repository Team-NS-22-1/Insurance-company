package main.domain.contract;


import main.exception.MyIllegalArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public class ContractListImpl implements ContractList {


	private static Map<Integer, Contract> contractList = new HashMap<>();
	private static int idSequence;

	@Override
	public boolean create(Contract contract) {
		contract.setId(++idSequence);
		contractList.put(contract.getId(), contract);
		return true;
	}

	@Override
	public Contract read(int id) {
		Contract contract = contractList.get(id);
		if (contract != null)
			return contract;
		throw new MyIllegalArgumentException(id + "에 맞는 계약정보가 존재하지 않습니다.");
	}

	@Override
	public boolean delete(int id) {
		Contract contract = contractList.remove(id);
		if (contract != null)
			return true;
		throw new MyIllegalArgumentException(id + "에 맞는 계약정보가 존재하지 않습니다.");
	}


	@Override
	public List<Contract> findAllByCustomerId(int customerId) {
		List<Contract> contracts = contractList.values()
				.stream()
				.filter(c -> c.getCustomerId() == customerId)
				.collect(Collectors.toList());

		if (contracts.size() == 0)
			throw new MyIllegalArgumentException("해당 ID로 검색되는 계약이 존재하지 않습니다");
		return contracts;

	}
}