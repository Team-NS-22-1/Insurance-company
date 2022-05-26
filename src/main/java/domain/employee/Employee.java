package domain.employee;


import domain.contract.BuildingType;
import domain.contract.ConditionOfUw;
import domain.contract.Contract;
import domain.contract.ContractListImpl;
import domain.insurance.*;
import domain.insurance.inputDto.*;
import exception.InputException.InputInvalidDataException;
import utility.FileDialogUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

	public Insurance develop(InsuranceList insuranceList, InsuranceDetailList insuranceDetailList, InsuranceType type, DtoBasicInfo basicInfo, ArrayList<DtoGuarantee> guaranteeInfoList, ArrayList<DtoTypeInfo> typeInfoList){
		ArrayList<Guarantee> guaranteeList = new ArrayList<>();
		for(int i=0; i<guaranteeInfoList.size(); i++)
			guaranteeList.add(
					new Guarantee(guaranteeInfoList.get(i).getName(), guaranteeInfoList.get(i).getDescription(), guaranteeInfoList.get(i).getGuaranteeAmount())
			);
		Insurance insurance = new Insurance();
		insurance.setName(basicInfo.getName())
				.setDescription(basicInfo.getDescription())
				.setContractPeriod(basicInfo.getContractPeriod())
				.setPaymentPeriod(basicInfo.getPaymentPeriod())
				.setGuarantee(guaranteeList)
				.setDevInfo(new DevInfo().setEmployeeId(this.id)
						.setDevDate(LocalDate.now())
						.setSalesAuthState(SalesAuthState.WAIT)
				)
				.setSalesAuthFile(new SalesAuthFile());
		insuranceList.create(insurance);
		return switch (type) {
			case HEALTH -> developHealth(insuranceDetailList, insurance, typeInfoList);
			case CAR -> developCar(insuranceDetailList, insurance, typeInfoList);
			case FIRE -> developFire(insuranceDetailList, insurance, typeInfoList);
		};
	}

	private Insurance developHealth(InsuranceDetailList insuranceDetailList, Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.HEALTH);
		int insuranceId = insurance.getId();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			HealthDetail healthDetail = new HealthDetail();
			healthDetail.setTargetAge(((DtoHealth) dtoTypeInfo).getTargetAge())
					.setTargetSex(((DtoHealth) dtoTypeInfo).isTargetSex())
					.setRiskCriterion(((DtoHealth) dtoTypeInfo).getRiskCriterion())
					.setPremium(dtoTypeInfo.getPremium())
					.setInsuranceId(insuranceId);
			insuranceDetailList.create(healthDetail);
		}
		return insurance;
	}

	private Insurance developCar(InsuranceDetailList insuranceDetailList, Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.CAR);
		int insuranceId = insurance.getId();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			CarDetail carDetail = new CarDetail();
			carDetail.setTargetAge(((DtoCar) dtoTypeInfo).getTargetAge())
					.setValueCriterion(((DtoCar) dtoTypeInfo).getValueCriterion())
					.setPremium(dtoTypeInfo.getPremium())
					.setInsuranceId(insuranceId);
			insuranceDetailList.create(carDetail);
		}
		return insurance;
	}

	private Insurance developFire(InsuranceDetailList insuranceDetailList, Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.FIRE);
		int insuranceId = insurance.getId();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			FireDetail fireDetail = new FireDetail();
			fireDetail.setTargetBuildingType(((DtoFire) dtoTypeInfo).getBuildingType())
					.setCollateralAmountCriterion(((DtoFire) dtoTypeInfo).getCollateralAmount())
					.setPremium(dtoTypeInfo.getPremium())
					.setInsuranceId(insuranceId);
			insuranceDetailList.create(fireDetail);
		}
		return insurance;
	}

	public int calcStandardPremium(long damageAmount, long countContract, long businessExpense, double profitMargin){
		if(damageAmount <=0 || countContract <=0 || businessExpense <=0 || profitMargin <= 0 || profitMargin>=100)
			throw new InputInvalidDataException();

		damageAmount *= 10000;
		businessExpense *= 10000;
		profitMargin /= 100;
		int purePremium = (int) (damageAmount / countContract);
		int riskCost = (int) (businessExpense / countContract);
		int stPremium = (int) ((purePremium + riskCost) / (1 - profitMargin));
		return stPremium;
	}

	public int calcSpecificPremium(int stPremium, DtoTypeInfo dtoTypeInfo) {
		if(dtoTypeInfo instanceof DtoHealth)
			return calcHealthPremium(stPremium, (DtoHealth) dtoTypeInfo);
		else if(dtoTypeInfo instanceof DtoCar)
			return calcCarPremium(stPremium, (DtoCar) dtoTypeInfo);
		else if(dtoTypeInfo instanceof DtoFire)
			return calcFirePremium(stPremium, (DtoFire) dtoTypeInfo);

		return -1;
	}

	private int calcHealthPremium(int stPremium, DtoHealth dtoHealth) {
		double weightRatio = 1.0;
		int targetAge = dtoHealth.getTargetAge();
		boolean targetSex = dtoHealth.isTargetSex();
		int riskCriterion = dtoHealth.getRiskCriterion();

		// 나이 가중치
		if(targetAge < 10) weightRatio *= 1.02;
		else if(targetAge < 20) weightRatio *= 1.06;
		else if(targetAge < 30) weightRatio *= 1.1;
		else if(targetAge < 40) weightRatio *= 1.14;
		else if(targetAge < 50) weightRatio *= 1.18;
		else if(targetAge < 60) weightRatio *= 1.22;
		else if(targetAge < 80) weightRatio *= 1.26;
		else if(targetAge < 100) weightRatio *= 1.3;
		else weightRatio *= 1.34;

		// 성별 가중치
		weightRatio = (targetSex) ? weightRatio * 1.2 : weightRatio * 1.1;

		// 위험부담 기준 가중치
		weightRatio = (riskCriterion > 3) ? weightRatio * 1.4 : weightRatio;

		return (int) (stPremium * weightRatio);
	}

	private int calcCarPremium(int stPremium, DtoCar dtoCar) {
		double weightRatio = 1.0;
		int targetAge = dtoCar.getTargetAge();
		long valueCriterion = dtoCar.getValueCriterion();

		if(targetAge < 20) weightRatio *= 1.4;
		else if(targetAge < 30) weightRatio *= 1.35;
		else if(targetAge < 40) weightRatio *= 1.2;
		else if(targetAge < 50) weightRatio *= 1.15;
		else if(targetAge < 60) weightRatio *= 1.15;
		else if(targetAge < 80) weightRatio *= 1.2;
		else if(targetAge < 100) weightRatio *= 1.35;
		else weightRatio *= 1.4;

		if(valueCriterion < 10000000L) weightRatio *= 1.05;
		else if(valueCriterion < 30000000L) weightRatio *= 1.15;
		else if(valueCriterion < 50000000L) weightRatio *= 1.3;
		else if(valueCriterion < 70000000L) weightRatio *= 1.4;
		else if(valueCriterion < 90000000L) weightRatio *= 1.6;
		else if(valueCriterion < 150000000L) weightRatio *= 1.8;
		else weightRatio *= 2.2;

		return (int) (stPremium * weightRatio);
	}

	private int calcFirePremium(int stPremium, DtoFire dtoFire) {
		double weightRatio = 1.0;
		BuildingType targetBuildingType = dtoFire.getBuildingType();
		long collateralAmountCriterion = dtoFire.getCollateralAmount();

		if(targetBuildingType == BuildingType.COMMERCIAL) weightRatio *= 1.3;
		else if(targetBuildingType == BuildingType.INDUSTRIAL) weightRatio *= 1.25;
		else if(targetBuildingType == BuildingType.INSTITUTIONAL) weightRatio *= 1.15;
		else if(targetBuildingType == BuildingType.RESIDENTIAL) weightRatio *= 1.1;

		if(collateralAmountCriterion < 100000000L) weightRatio *= 1.3;
		else if(collateralAmountCriterion < 500000000L) weightRatio *= 1.25;
		else if(collateralAmountCriterion < 1000000000L) weightRatio *= 1.2;
		else if(collateralAmountCriterion < 5000000000L) weightRatio *= 1.15;
		else weightRatio *= 1.1;

		return (int) (stPremium * weightRatio);
	}

	public int registerAuthProdDeclaration(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getDirProdDeclaration()!=null) {
			return -1;
		}
		String dirInsurance = insurance.getId() + ". " + insurance.getName();
		String savePath = FileDialogUtil.upload(dirInsurance);
		if(savePath == null) return 0;
		insurance.getSalesAuthFile().setDirProdDeclaration(savePath);
		return 1;
	}

	public void registerAuthSrActuaryVerification(Insurance insurance) {
	}

	public void registerAuthISOVerification(Insurance insurance) {
	}

	public void registerAuthFSSOfficialDoc(Insurance insurance) {
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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return getId() == employee.getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}