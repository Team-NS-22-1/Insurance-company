package main.domain.employee;


import main.domain.contract.*;
import main.domain.contract.*;
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

	// TODO 단위 만원
	public int calcPurePremiumMethod(long damageAmount, long countContract, long businessExpense, int profitMargin){
		if(damageAmount <=0 || countContract <=0 || businessExpense <=0 || profitMargin <= 0 || profitMargin>=100)
			throw new InputInvalidDataException();
		int purePremium = (int) (damageAmount / countContract);
		int riskCost = (int) (businessExpense / countContract);
		int premium = (purePremium + riskCost) / (100 - profitMargin);
		return premium;
	}

	public Object[] calcLossRatioMethod(int lossRatio, int expectedBusinessRatio, int curTotalPremium){
		if (expectedBusinessRatio >= 100 || lossRatio <=0 || curTotalPremium <= 0) {
			throw new InputInvalidDataException();
		}
		double adjustedRate = (double) (lossRatio - (100 - expectedBusinessRatio)) / (100 - expectedBusinessRatio);
		int premium = (int) (curTotalPremium + (curTotalPremium * adjustedRate));
		return new Object[]{ adjustedRate, premium };
	}

	public void registerInsurance(InsuranceListImpl insuranceList, Insurance insurance, int premium){
		insurance.setPremium(premium)
				.setDevInfo(new DevInfo().setEmployeeId(this.id)
										.setDevDate(LocalDate.now())
										.setSalesAuthState(SalesAuthState.PERMISSION)
										.setSalesStartDate(null)
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

	public Customer inputCustomerInfo(String name, String ssn, String phone, String address, String email, String job) {
		Customer customer = new Customer();
		customer.setName(name)
				.setSsn(ssn)
				.setPhone(phone)
				.setAddress(address)
				.setEmail(email)
				.setJob(job);
		return customer;
	};

	public Contract inputHealthInfo(int height, int weight, boolean isDrinking, boolean isSmoking, boolean isDriving,
									boolean isDangerActivity, Boolean isTakingDrug, boolean isHavingDisease, String diseaseDetail){
		Contract contract = new Contract();
		contract.setHealthInfo(new HealthInfo().setHeight(height)
				.setWeight(weight)
				.setDrinking(isDrinking)
				.setSmoking(isSmoking)
				.setDriving(isDriving)
				.setDangerActivity(isDangerActivity)
				.setTakingDrug(isTakingDrug)
				.setHavingDisease(isHavingDisease)
				.setDiseaseDetail(diseaseDetail));
		return contract;
	}

	public Contract inputFireInfo(BuildingType buildingType, int buildingArea, int collateralAmount, boolean isSelfOwned, boolean isActualResidence){
		Contract contract = new Contract();
		contract.setBuildingInfo(new BuildingInfo().setBuildingType(buildingType)
				.setBuildingArea(buildingArea)
				.setCollateralAmount(collateralAmount)
				.setSelfOwned(isSelfOwned)
				.setActualResidence(isActualResidence)
		);
		return contract;
	}

	public Contract inputCarInfo(String carNo, CarType carType, String modelName, int modelYear, int value) {
		Contract contract = new Contract();
		contract.setCarInfo(new CarInfo().setCarNo(carNo)
				.setCarType(carType)
				.setModelName(modelName)
				.setModelYear(modelYear)
				.setValue(value));
		return contract;
	}

	public void registerContract(CustomerList customerList, ContractList contractList , Customer customer, Contract contract, int premium){

		customerList.create(customer);
		contract.setPremium(premium)
				.setCustomerId(customer.getId())
				.setConditionOfUw(ConditionOfUw.WAIT);
		contractList.create(contract);
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