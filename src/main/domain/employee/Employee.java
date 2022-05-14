package main.domain.employee;


import main.domain.insurance.*;
import main.domain.insurance.inputDto.*;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class Employee {

	private int id;
	private String name;
	private String phone;
	private Department department;
	private Position position;


//	public Contract m_Contract;
//	public Insurance m_Insurance;
//	public Accident m_Accident;

	public Employee(){
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

	public String toString(){
		String value = "";
		value += this.getId()+" "+this.getName()+" "+this.getPhone()+" "+this.getDepartment()+" "+this.getPosition();
		return value;
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
					.setGuarantee(guaranteeList);
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
					.setGuarantee(guaranteeList);
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
					.setGuarantee(guaranteeList);
			return insurance;
		}
		return null;
	}

	public void calculatePremium(Insurance insurance){
		// Not Used...?
	}

	public int calcPurePremiumMethod(Insurance insurance, long damageAmount, long countContract, long businessExpense, double profitMargin){
		int purePremium = (int) (damageAmount / countContract);
		int riskCost = (int) (businessExpense / countContract);
		int premium = (int) ((purePremium + riskCost) / (1 - profitMargin));
		return premium;
	}

	public Object[] calcLossRatioMethod(Insurance insurance, int lossRatio, int expectedBusinessRatio, int curTotalPremium){
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

	public void readContract(){

	}

	public void underwriting(){

	}

}