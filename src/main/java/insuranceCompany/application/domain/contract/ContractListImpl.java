package insuranceCompany.application.domain.contract;


import insuranceCompany.application.dao.contract.ContractDao;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.viewlogic.dto.contractDto.ContractwithTypeDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public class ContractListImpl implements ContractDao {


	private static Map<Integer, Contract> contractList = new HashMap<>();
	private static int idSequence;

	public ContractListImpl(){

	}

	@Override
	public void create(Contract contract) {
		contractList.put(contract.setId(++idSequence).getId(), contract);
	}

	@Override
	public Contract read(int id) {
		Contract contract = contractList.get(id);
		if (contract != null)
			return contract;
		throw new MyIllegalArgumentException(id + "에 맞는 계약정보가 존재하지 않습니다.");
	}

	@Override
	public boolean update(int id) {
		return false;
	}

	@Override
	public boolean delete(int id) {
		Contract contract = contractList.remove(id);
		if (contract != null)
			return true;
		throw new MyIllegalArgumentException(id + "에 맞는 계약정보가 존재하지 않습니다.");
	}


	@Override
	public List<ContractwithTypeDto> findAllContractWithTypeByCustomerId(int customerId) {
		return null;
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

	@Override
	public void updatePayment(int contractId, int paymentId) {

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