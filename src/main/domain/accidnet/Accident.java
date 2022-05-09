package main.domain.accidnet;

import java.util.List;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public class Accident {

	private List<AccDocFile> accDocFileList;
	//private enum accidentType;
	private int customerId;
	//private Date dateOfAccident;
	//private Date dateOfReport;
	private int employeeId;
	private int id;
	private int lossReserves;
	public AccDocFile m_AccDocFile;

	public Accident(){

	}

	public void finalize() throws Throwable {

	}

}