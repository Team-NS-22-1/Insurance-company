package main.domain.employee;


import main.domain.contract.ConditionOfUw;
import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.insurance.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

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

	public Insurance develop(InsuranceType type, Object[] defaultInfo, Object[] typeInfo, ArrayList<String[]> guaranteeListInfo) {
		if(type == InsuranceType.HEALTH){
			// 이런 Object 배열을 이용하는 하드코딩? 이외의 방법은 없을까요??
			HealthInsurance insurance = new HealthInsurance();
			insurance.setTargetAge((Integer) typeInfo[0])
					.setTargetSex((Boolean) typeInfo[1])
					.setRiskPremiumCriterion((Integer) typeInfo[2])
			// id를 여기서 세팅해야 하는지, DB의 Auto Increment를 사용할지 결정?
					.setId(0)
					.setName((String) defaultInfo[0])
					.setDescription((String) defaultInfo[1])
					.setPaymentPeriod((Integer) defaultInfo[2])
					.setContractPeriod((Integer) defaultInfo[3]);
			ArrayList<Guarantee> guaranteeList = new ArrayList<>();
			for(int i=0; i<guaranteeListInfo.size(); i++)
				guaranteeList.add(
						new Guarantee(guaranteeListInfo.get(i)[0], guaranteeListInfo.get(i)[1])
				);
			insurance.setGuarantee(guaranteeList);
			return insurance;
		}
		else if(type == InsuranceType.CAR){

		}
		else if(type == InsuranceType.FIRE){

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