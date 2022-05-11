package main.domain.accident;


import java.util.ArrayList;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:56
 */
public class AccidentListImpl implements AccidentList {

	private ArrayList<Accident> accidentList = new ArrayList<>();

	public AccidentListImpl(){

	}

	@Override
	public boolean create(Accident accident) {
		return false;
	}

	@Override
	public Accident read(int id) {
		return null;
	}

	@Override
	public boolean delete(int id) {
		return false;
	}
}