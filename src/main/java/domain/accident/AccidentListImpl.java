package domain.accident;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:56
 */
public class AccidentListImpl implements AccidentList {

	private static Map<Integer,Accident> accidentList = new HashMap<>();
	private static int id;


	@Override
	public void create(Accident accident) {
		accident.setId(++id);
		accidentList.put(accident.getId(),accident);
	}

	@Override
	public Accident read(int id) {
		Accident accident = accidentList.get(id);

		if (accident == null) {
			throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 정보가 존재하지 않습니다.");
		}
		return accident;
	}

	@Override
	public boolean update(int id) {
		return false;
	}

	@Override
	public boolean delete(int id) {
		if (accidentList.containsKey(id)) {
			accidentList.remove(id);
			return true;
		}
		throw new IllegalArgumentException("사고 아이디["+id+"]에 해당하는 사고 정보가 존재하지 않습니다.");
	}

	@Override
	public List<Accident> readAllByCustomerId(int customerId) {
		List<Accident> list = new ArrayList<>(accidentList.values());
		List<Accident> collect = list.stream().filter(a -> a.getCustomerId() == customerId)
				.collect(Collectors.toList());
		if(collect.size()==0)
			throw new IllegalArgumentException("고객 아이디 ["+customerId+"]에 해당하는 사고 정보가 존재하지 않습니다.");
		return collect;
	}

	@Override
	public List<Accident> readAllByEmployeeId(int employeeId) {
		List<Accident> list = new ArrayList<>(accidentList.values());
		return list.stream().filter(a -> a.getEmployeeId()==employeeId)
				.collect(Collectors.toList());
	}

	@Override
	public void updateLossReserve(Accident accident) {

	}

	@Override
	public void updateLossReserveAndErrorRate(Accident accident) {

	}
}