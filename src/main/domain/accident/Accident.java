package main.domain.accident;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public class Accident {

	private ArrayList<AccDocFile> accDocFileList;
	private AccidentType accidentType;
	private int customerId;
	private LocalDate dateOfAccident;
	private LocalDate dateOfReport;
	private int employeeId;
	private int id;
	private int lossReserves;
	public AccDocFile m_AccDocFile;

	public Accident(){

	}

}