package main.domain.accident;

import java.time.LocalDate;
import java.util.ArrayList;


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

	public ArrayList<AccDocFile> getAccDocFileList() {
		return accDocFileList;
	}

	public Accident setAccDocFileList(ArrayList<AccDocFile> accDocFileList) {
		this.accDocFileList = accDocFileList;
		return this;
	}

	public AccidentType getAccidentType() {
		return accidentType;
	}

	public Accident setAccidentType(AccidentType accidentType) {
		this.accidentType = accidentType;
		return this;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Accident setCustomerId(int customerId) {
		this.customerId = customerId;
		return this;
	}

	public LocalDate getDateOfAccident() {
		return dateOfAccident;
	}

	public Accident setDateOfAccident(LocalDate dateOfAccident) {
		this.dateOfAccident = dateOfAccident;
		return this;
	}

	public LocalDate getDateOfReport() {
		return dateOfReport;
	}

	public Accident setDateOfReport(LocalDate dateOfReport) {
		this.dateOfReport = dateOfReport;
		return this;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public Accident setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	public int getId() {
		return id;
	}

	public Accident setId(int id) {
		this.id = id;
		return this;
	}

	public int getLossReserves() {
		return lossReserves;
	}

	public Accident setLossReserves(int lossReserves) {
		this.lossReserves = lossReserves;
		return this;
	}

	public AccDocFile getM_AccDocFile() {
		return m_AccDocFile;
	}

	public Accident setM_AccDocFile(AccDocFile m_AccDocFile) {
		this.m_AccDocFile = m_AccDocFile;
		return this;
	}
}