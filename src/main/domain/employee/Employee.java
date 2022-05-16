package main.domain.employee;


import main.domain.contract.BuildingType;
import main.domain.contract.ConditionOfUw;
import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.insurance.*;
import main.domain.insurance.inputDto.*;
import main.exception.InputException;
import main.exception.InputException.InputInvalidDataException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

		Insurance insurance=null;
		if(typeInfo instanceof DtoHealth){
			insurance = new HealthInsurance();
			((HealthInsurance) insurance).setTargetAge(((DtoHealth) typeInfo).getTargetAge())
					.setTargetSex(((DtoHealth) typeInfo).isTargetSex())
					.setRiskPremiumCriterion(((DtoHealth) typeInfo).getRiskCriterion())
					.setInsuranceType(InsuranceType.HEALTH);
		}
		else if(typeInfo instanceof DtoCar){
			insurance = new CarInsurance();
			((CarInsurance)insurance).setTargetAge(((DtoCar) typeInfo).getTargetAge())
					.setValueCriterion(((DtoCar) typeInfo).getTargetAge())
					.setInsuranceType(InsuranceType.CAR);;
		}
		else if(typeInfo instanceof DtoFire){
			insurance = new FireInsurance();
			((FireInsurance)insurance).setBuildingType(((DtoFire) typeInfo).getBuildingType())
					.setCollateralAmount(((DtoFire) typeInfo).getCollateralAmount())
					.setInsuranceType(InsuranceType.FIRE);;
		}
		insurance.setName(basicInfo.getName())
				.setDescription(basicInfo.getDescription())
				.setPaymentPeriod(basicInfo.getPaymentPeriod())
				.setContractPeriod(basicInfo.getContractPeriod())
				.setGuarantee(guaranteeList);
		return insurance;
	}

	public int calcPurePremiumMethod(long damageAmount, long countContract, long businessExpense, double profitMargin){
		if(damageAmount <=0 || countContract <=0 || businessExpense <=0 || profitMargin <= 0 || profitMargin>=100)
			throw new InputInvalidDataException();

		damageAmount *= 10000;
		businessExpense *= 10000;
		profitMargin /= 100;
		int purePremium = (int) (damageAmount / countContract);
		int riskCost = (int) (businessExpense / countContract);
		int premium = (int) ((purePremium + riskCost) / (1 - profitMargin));
		return premium;
	}

	public Object[] calcLossRatioMethod(double lossRatio, double expectedBusinessRatio, int curTotalPremium){
		if (expectedBusinessRatio >= 100 || lossRatio <=0 || curTotalPremium <= 0) {
			throw new InputInvalidDataException();
		}
		lossRatio /= 100;
		expectedBusinessRatio /= 100;
		double adjustedRate = (lossRatio - (1 - expectedBusinessRatio)) / (1 - expectedBusinessRatio);
		int premium = (int) (curTotalPremium + (curTotalPremium * adjustedRate));
		return new Object[]{ adjustedRate, premium };
	}

	public void registerInsurance(InsuranceListImpl insuranceList, Insurance insurance, int premium){
		insurance.setPremium(premium)
				.setDevInfo(new DevInfo().setEmployeeId(this.id)
										.setDevDate(LocalDate.now())
										.setSalesAuthState(SalesAuthState.WAIT)
				);
		insuranceList.create(insurance);
	}

	public void registerAuthInfo(){

	}

	public void assessDamage(){

	}

	public void concludeContract(){

	}

	public void investigateDamage(){

	}

	public int planHealthInsurance(int targetAge, boolean targetSex, boolean riskPremiumCriterion){
		return 0;
	}

	public int planFireInsurance(BuildingType buildingType, int collateralAmount){
		return 0;
	}

	public int planCarInsurance(int targetAge, int value){
		return 0;
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

	public String print() {
		return "직원 정보 {" +
				"직원ID: " + id +
				", 이름: '" + name + '\'' +
				", 연락처: '" + phone + '\'' +
				", 부서: " + department.getName() +
				", 직책: " + position.getName() +
				'}';
	}
}