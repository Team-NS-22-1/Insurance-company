package main.domain.accident;


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
		//TODO accident search실패 시, exception 뭘로 할지 생각하기 - PaymentlistImpl에도 같은 것으로 설정ㅊ할지 말지 고민해야 함. 아마 customerId랑은 다르게 해야 할 듯.
		if (accident == null) {

		}
		return accident;
	}

	@Override
	public boolean delete(int id) {
		if (accidentList.containsKey(id)) {
			accidentList.remove(id);
			return true;
		}
		//TODO 마찬가지로 exception 정해주기.
		throw new IllegalArgumentException();
	}

	@Override
	public List<Accident> readAllByCustomerId(int customerId) {
		List<Accident> list = new ArrayList<>(accidentList.values());
		return list.stream().filter(a -> a.getCustomerId()==customerId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Accident> readAllByEmployeeId(int employeeId) {
		List<Accident> list = new ArrayList<>(accidentList.values());
		return list.stream().filter(a -> a.getEmployeeId()==employeeId)
				.collect(Collectors.toList());
	}
}