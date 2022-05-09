package main.domain.insurance;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public interface InsuaranceList {

	public boolean add(Insuarance insuarance); // create
	public boolean delete(int insuranceId); // delete
	public Insuarance get(int insuranceId); // read
	//public void update();
	

}