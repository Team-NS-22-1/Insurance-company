package main.domain.employee;


import main.domain.contract.ConditionOfUw;
import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.insurance.*;
import main.domain.insurance.inputDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오후 4:38:59
 */
public class Employee {

	private int id;
	private String name;
	private String phone;
	private Department department;
	private Position position;

	public Employee(){
	}

	@Override
	public String toString() {
		return this.id+"'"+this.name+"'"+this.phone+"'"+this.department.getName()+"'"+this.position.getName();
	}

	public int getId() {
		return id;
	}

	public Employee setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Employee setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Employee setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public Department getDepartment() {
		return department;
	}

	public Employee setDepartment(Department department) {
		this.department = department;
		return this;
	}

	public Position getPosition() {
		return position;
	}

	public Employee setPosition(Position position) {
		this.position = position;
		return this;
	}

	public Insurance develop(DtoBasicInfo basicInfo, DtoTypeInfo typeInfo, ArrayList<DtoGuarantee> guaranteeListInfo) {
		ArrayList<Guarantee> guaranteeList = new ArrayList<>();
		for(int i=0; i<guaranteeListInfo.size(); i++)
			guaranteeList.add(
					new Guarantee(guaranteeListInfo.get(i).getName(), guaranteeListInfo.get(i).getDescription())
			);
		if(typeInfo instanceof DtoHealth){
			HealthInsurance insurance = new HealthInsurance();
			insurance.setTargetAge(((DtoHealth) typeInfo).getTargetAge())
					.setTargetSex(((DtoHealth) typeInfo).isTargetSex())
					.setRiskPremiumCriterion(((DtoHealth) typeInfo).getRiskCriterion())
					.setName(basicInfo.getName())
					.setDescription(basicInfo.getDescription())
					.setPaymentPeriod(basicInfo.getPaymentPeriod())
					.setContractPeriod(basicInfo.getContractPeriod())
					.setGuarantee(guaranteeList)
					.setInsuranceType(InsuranceType.HEALTH);
			return insurance;
		}
		else if(typeInfo instanceof DtoCar){
			CarInsurance insurance = new CarInsurance();
			insurance.setTargetAge(((DtoCar) typeInfo).getTargetAge())
					.setValueCriterion(((DtoCar) typeInfo).getTargetAge())
					.setName(basicInfo.getName())
					.setDescription(basicInfo.getDescription())
					.setPaymentPeriod(basicInfo.getPaymentPeriod())
					.setContractPeriod(basicInfo.getContractPeriod())
					.setGuarantee(guaranteeList)
					.setInsuranceType(InsuranceType.CAR);;
			return insurance;
		}
		else if(typeInfo instanceof DtoFire){
			FireInsurance insurance = new FireInsurance();
			insurance.setBuildingType(((DtoFire) typeInfo).getBuildingType())
					.setCollateralAmount(((DtoFire) typeInfo).getCollateralAmount())
					.setName(basicInfo.getName())
					.setDescription(basicInfo.getDescription())
					.setPaymentPeriod(basicInfo.getPaymentPeriod())
					.setContractPeriod(basicInfo.getContractPeriod())
					.setGuarantee(guaranteeList)
					.setInsuranceType(InsuranceType.FIRE);;
			return insurance;
		}
		return null;
	}

	public void calculatePremium(Insurance insurance){
		// Not Used...?
	}

	public int calcPurePremiumMethod(long damageAmount, long countContract, long businessExpense, double profitMargin){
		int purePremium = (int) (damageAmount / countContract);
		int riskCost = (int) (businessExpense / countContract);
		int premium = (int) ((purePremium + riskCost) / (1 - profitMargin));
		return premium;
	}

	public Object[] calcLossRatioMethod(int lossRatio, int expectedBusinessRatio, int curTotalPremium){
		double adjustedRate = (double) (lossRatio - (100 - expectedBusinessRatio)) / (100 - expectedBusinessRatio);
		int premium = (int) (curTotalPremium + (curTotalPremium * adjustedRate));
		return new Object[]{ adjustedRate, premium };
	}

	public boolean registerInsurance(InsuranceListImpl insuranceList, Insurance insurance, int premium){
		insurance.setPremium(premium)
				.setDevInfo(new DevInfo().setEmployeeId(this.id)
										.setDevDate(LocalDate.now())
										.setSalesAuthState(SalesAuthState.WAIT)
										.setSalesStartDate(null)
				);
		return insuranceList.create(insurance);
	}

	public String readMyInsurance(InsuranceListImpl insuranceList){
		ArrayList<Insurance> eInsuranceList = insuranceList.readByEid(this.id);
		String value = "";
		for(Insurance insurance : eInsuranceList)
			value += insurance.toString()+"\n";
		return value;
	}

	public void registerAuthInfo(){

	}

	public void assessDamage(){

	}

	public void concludeContract(){

	}

	public void investigateDamage(){

	}

	public void planInsurance(){

	}

	public void readAccident(){

	}

	public Map<Integer, Contract> readContract(InsuranceType insuranceType){
		Map<Integer, Contract> contractList = new HashMap<>();

		for (Contract contract : ContractListImpl.getContractList().values()) {

			switch (insuranceType) {

				case HEALTH:
					if (contract.getHealthInfo() != null && (contract.getConditionOfUw() == ConditionOfUw.WAIT || contract.getConditionOfUw() == ConditionOfUw.RE_AUDIT))
						contractList.put(contract.getId(), contract);
					break;
				case CAR:
					if (contract.getCarInfo() != null && (contract.getConditionOfUw() == ConditionOfUw.WAIT || contract.getConditionOfUw() == ConditionOfUw.RE_AUDIT))
						contractList.put(contract.getId(), contract);
					break;
				case FIRE:
					if (contract.getBuildingInfo() != null && (contract.getConditionOfUw() == ConditionOfUw.WAIT || contract.getConditionOfUw() == ConditionOfUw.RE_AUDIT))
						contractList.put(contract.getId(), contract);
					break;
			}


		}

		return contractList;
	}

	public void underwriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw){
		ContractListImpl contractList = new ContractListImpl();
		Contract contract = contractList.read(contractId);
		contract.setReasonOfUw(reasonOfUw);
		contract.setConditionOfUw(conditionOfUw);
		contract.setPublishStock(true);

	}

}