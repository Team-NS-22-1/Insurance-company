package domain.employee;


import application.viewlogic.dto.compDto.AccountRequestDto;
import application.viewlogic.dto.compDto.AssessDamageResponseDto;
import application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import domain.accident.Accident;
import domain.accident.AccidentType;
import domain.accident.CarAccident;
import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocType;
import dao.ContractDao;
import dao.CustomerDao;
import dao.InsuranceDao;
import domain.contract.*;
import domain.customer.Customer;
import domain.insurance.*;
import domain.insurance.inputDto.*;
import domain.payment.Account;
import exception.InputException.InputInvalidDataException;
import utility.DocUtil;
import utility.FileDialogUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

	public void develop(InsuranceType type, DtoBasicInfo basicInfo, ArrayList<DtoGuarantee> guaranteeInfoList, ArrayList<DtoTypeInfo> typeInfoList){
		Insurance insurance = new Insurance();
		insurance.setName(basicInfo.getName())
				.setDescription(basicInfo.getDescription())
				.setContractPeriod(basicInfo.getContractPeriod())
				.setPaymentPeriod(basicInfo.getPaymentPeriod())
				.setGuaranteeList(developGuarantee(guaranteeInfoList))
				.setDevInfo(developDevInfo())
				.setSalesAuthFile(new SalesAuthFile());
		switch (type) {
			case HEALTH -> developHealth(insurance, typeInfoList);
			case CAR -> developCar(insurance, typeInfoList);
			case FIRE -> developFire(insurance, typeInfoList);
		}
		new InsuranceDao().create(insurance);
	}

	private ArrayList<Guarantee> developGuarantee(ArrayList<DtoGuarantee> guaranteeInfoList) {
		ArrayList<Guarantee> guaranteeList = new ArrayList<>();
		for(int i=0; i<guaranteeInfoList.size(); i++) {
			guaranteeList.add(new Guarantee().setName(guaranteeInfoList.get(i).getName())
					.setDescription(guaranteeInfoList.get(i).getDescription())
					.setGuaranteeAmount(guaranteeInfoList.get(i).getGuaranteeAmount())
			);
		}
		return guaranteeList;
	}

	private DevInfo developDevInfo() {
		return new DevInfo().setEmployeeId(this.id)
				.setDevDate(LocalDate.now())
				.setSalesAuthState(SalesAuthState.WAIT);
	}

	private Insurance developHealth(Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.HEALTH);
		ArrayList<InsuranceDetail> insuranceDetails = new ArrayList<>();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			HealthDetail healthDetail = new HealthDetail();
			healthDetail.setTargetAge(((DtoHealth) dtoTypeInfo).getTargetAge())
					.setTargetSex(((DtoHealth) dtoTypeInfo).isTargetSex())
					.setRiskCriterion(((DtoHealth) dtoTypeInfo).getRiskCriterion())
					.setPremium(dtoTypeInfo.getPremium());
			insuranceDetails.add(healthDetail);
		}
		return insurance.setInsuranceDetailList(insuranceDetails);
	}

	private Insurance developCar(Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.CAR);
		ArrayList<InsuranceDetail> insuranceDetails = new ArrayList<>();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			CarDetail carDetail = new CarDetail();
			carDetail.setTargetAge(((DtoCar) dtoTypeInfo).getTargetAge())
					.setValueCriterion(((DtoCar) dtoTypeInfo).getValueCriterion())
					.setPremium(dtoTypeInfo.getPremium());
			insuranceDetails.add(carDetail);
		}
		return insurance.setInsuranceDetailList(insuranceDetails);
	}

	private Insurance developFire(Insurance insurance, ArrayList<DtoTypeInfo> typeInfoList) {
		insurance.setInsuranceType(InsuranceType.FIRE);
		ArrayList<InsuranceDetail> insuranceDetails = new ArrayList<>();
		for(DtoTypeInfo dtoTypeInfo : typeInfoList) {
			FireDetail fireDetail = new FireDetail();
			fireDetail.setTargetBuildingType(((DtoFire) dtoTypeInfo).getBuildingType())
					.setCollateralAmountCriterion(((DtoFire) dtoTypeInfo).getCollateralAmount())
					.setPremium(dtoTypeInfo.getPremium());
			insuranceDetails.add(fireDetail);
		}
		return insurance.setInsuranceDetailList(insuranceDetails);
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

		if(targetAge < 10) weightRatio *= 1.02;
		else if(targetAge < 20) weightRatio *= 1.06;
		else if(targetAge < 30) weightRatio *= 1.1;
		else if(targetAge < 40) weightRatio *= 1.14;
		else if(targetAge < 50) weightRatio *= 1.18;
		else if(targetAge < 60) weightRatio *= 1.22;
		else if(targetAge < 80) weightRatio *= 1.26;
		else if(targetAge < 100) weightRatio *= 1.3;
		else weightRatio *= 1.34;

		weightRatio = (targetSex) ? weightRatio * 1.2 : weightRatio * 1.1;

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

	public void modifySalesAuthState(Insurance insurance, SalesAuthState modify) {
		insurance.getDevInfo().setSalesAuthState(modify);
		new InsuranceDao().updateBySalesAuthState(insurance);
	}

	private boolean checkSalesAuthState(Insurance insurance) {
		SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
		if(salesAuthFile.getProdDeclaration()!=null && salesAuthFile.getFssOfficialDoc()!=null && salesAuthFile.getIsoVerification()!=null && salesAuthFile.getSrActuaryVerification()!=null)
			return true;
		return false;
	}

	public int registerAuthProdDeclaration(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getProdDeclaration()!=null) return 0;
		return uploadProd(insurance);
	}

	public int registerAuthProdDeclaration(Insurance insurance, String nullValue) throws IOException {
		insurance.getSalesAuthFile().setProdDeclaration(nullValue);
		return uploadProd(insurance);
	}

	private int uploadProd(Insurance insurance) throws IOException {
		String dirInsurance = insurance.getId() + ". " + insurance.getName();
		String savePath = FileDialogUtil.upload(dirInsurance);
		if(savePath == null) return -1;
		insurance.getSalesAuthFile().setProdDeclaration(savePath)
				.setModifiedProd(LocalDateTime.now());
		new InsuranceDao().updateByProd(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthSrActuaryVerification(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getSrActuaryVerification()!=null) return 0;
		return uploadSrActuary(insurance);
	}

	public int registerAuthSrActuaryVerification(Insurance insurance, String nullValue) throws IOException {
		insurance.getSalesAuthFile().setSrActuaryVerification(nullValue);
		return uploadSrActuary(insurance);
	}

	private int uploadSrActuary(Insurance insurance) throws IOException {
		String dirInsurance = insurance.getId() + ". " + insurance.getName();
		String savePath = FileDialogUtil.upload(dirInsurance);
		if(savePath == null) return -1;
		insurance.getSalesAuthFile().setSrActuaryVerification(savePath)
				.setModifiedSrActuary(LocalDateTime.now());
		new InsuranceDao().updateBySrActuary(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthIsoVerification(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getIsoVerification()!=null) return 0;
		return uploadIso(insurance);
	}

	public int registerAuthIsoVerification(Insurance insurance, String nullValue) throws IOException {
		insurance.getSalesAuthFile().setIsoVerification(nullValue);
		return uploadIso(insurance);
	}

	private int uploadIso(Insurance insurance) throws IOException {
		String dirInsurance = insurance.getId() + ". " + insurance.getName();
		String savePath = FileDialogUtil.upload(dirInsurance);
		if(savePath == null) return -1;
		insurance.getSalesAuthFile().setIsoVerification(savePath)
				.setModifiedIso(LocalDateTime.now());
		new InsuranceDao().updateByIso(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthFssOfficialDoc(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getFssOfficialDoc()!=null) return 0;
		return uploadFss(insurance);
	}

	public int registerAuthFssOfficialDoc(Insurance insurance, String nullValue) throws IOException {
		insurance.getSalesAuthFile().setFssOfficialDoc(nullValue);
		return uploadFss(insurance);
	}

	private int uploadFss(Insurance insurance) throws IOException {
		String dirInsurance = insurance.getId() + ". " + insurance.getName();
		String savePath = FileDialogUtil.upload(dirInsurance);
		if(savePath == null) return -1;
		insurance.getSalesAuthFile().setFssOfficialDoc(savePath)
				.setModifiedFss(LocalDateTime.now());
		new InsuranceDao().updateByFss(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}
	public AssessDamageResponseDto assessDamage(Accident accident, AccountRequestDto accountRequestDto){
		return AssessDamageResponseDto.builder().accDocFile(uploadLossAssessment(accident))
				.account(new Account().setBankType(accountRequestDto.getBankType()).setAccountNo(accountRequestDto.getAccountNo()))
				.build();
	}

	private AccDocFile uploadLossAssessment(Accident accident) {
		DocUtil instance = DocUtil.getInstance();
		String dir = "./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()+"/"+AccDocType.LOSSASSESSMENT.getDesc()+".hwp";
		String fileDir = instance.upload(dir);
		if (fileDir == null) {
			return null;
		}
		AccDocFile accDocFile = new AccDocFile();
		accDocFile.setFileAddress(fileDir)
				.setAccidentId(accident.getId())
				.setType(AccDocType.LOSSASSESSMENT);
		accident.getAccDocFileList().put(AccDocType.LOSSASSESSMENT,accDocFile);
		return accDocFile;
	}

	public void assessDamage(){

	}

	public void investigateDamage(InvestigateDamageRequestDto dto, Accident accident){
		accident.setLossReserves(dto.getLossReserves());
		if(accident.getAccidentType() == AccidentType.CARACCIDENT)
			((CarAccident)accident).setErrorRate(dto.getErrorRate());

	}

	public HealthContract concludeHealthContract(int insuranceId, int premium, boolean isDrinking, boolean isSmoking,
												 boolean isDriving, boolean isDangerActivity, boolean isTakingDrug, boolean isHavingDisease){
		HealthContract healthContract = new HealthContract();
		healthContract.setDrinking(isDrinking)
						.setSmoking(isSmoking)
						.setDriving(isDriving)
						.setDangerActivity(isDangerActivity)
						.setTakingDrug(isTakingDrug)
						.setHavingDisease(isHavingDisease)
						.setInsuranceId(insuranceId)
						.setPremium(premium);
		return healthContract;
	}

	public FireContract concludeFireContract(int insuranceId, int premium, BuildingType buildingType, Long collateralAmount){
		FireContract fireContract = new FireContract();
		fireContract.setBuildingType(buildingType)
					.setCollateralAmount(collateralAmount)
					.setInsuranceId(insuranceId)
					.setPremium(premium);		;
		return fireContract;
	}

	public CarContract concludeCarContract(int insuranceId, int premium, Long value){
		CarContract carContract = new CarContract();
		carContract.setValue(value)
				.setInsuranceId(insuranceId)
				.setPremium(premium)
		;
		return carContract;
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

	public HealthContract inputHealthInfo(int height, int weight, boolean isDrinking, boolean isSmoking, boolean isDriving, boolean isDangerActivity,
									Boolean isTakingDrug, boolean isHavingDisease, String diseaseDetail, int insuranceId, int premium){
		HealthContract healthContract = new HealthContract();
		healthContract.setHeight(height)
						.setWeight(weight)
						.setDrinking(isDrinking)
						.setSmoking(isSmoking)
						.setDriving(isDriving)
						.setDangerActivity(isDangerActivity)
						.setTakingDrug(isTakingDrug)
						.setHavingDisease(isHavingDisease)
						.setDiseaseDetail(diseaseDetail)
						.setInsuranceId(insuranceId)
						.setPremium(premium);
		return healthContract;
	}

	public HealthContract inputHealthInfo(HealthContract healthContract, int height, int weight, String diseaseDetail){
		healthContract.setHeight(height)
						.setWeight(weight)
						.setDiseaseDetail(diseaseDetail);
		return healthContract;
	}

	public FireContract inputFireInfo(BuildingType buildingType, int buildingArea, Long collateralAmount, boolean isSelfOwned, boolean isActualResidence, int insuranceId, int premium){
		FireContract fireContract = new FireContract();
		fireContract.setBuildingType(buildingType)
				 	.setBuildingArea(buildingArea)
					.setCollateralAmount(collateralAmount)
					.setSelfOwned(isSelfOwned)
					.setActualResidence(isActualResidence)
					.setInsuranceId(insuranceId)
					.setPremium(premium);
		return fireContract;
	}

	public FireContract inputFireInfo(FireContract fireContract, int buildingArea, boolean isSelfOwned, boolean isActualResidence){
		fireContract.setBuildingArea(buildingArea)
				.setSelfOwned(isSelfOwned)
				.setActualResidence(isActualResidence);
		return fireContract;
	}

	public CarContract inputCarInfo(String carNo, CarType carType, String modelName, int modelYear, Long value, int insuranceId, int premium) {
		CarContract carContract = new CarContract();
		carContract.setCarNo(carNo)
					.setCarType(carType)
					.setModelName(modelName)
					.setModelYear(modelYear)
					.setValue(value)
					.setInsuranceId(insuranceId)
					.setPremium(premium);
		return carContract;
	}

	public CarContract inputCarInfo(CarContract carContract, String carNo, CarType carType, String modelName, int modelYear) {
		carContract.setCarNo(carNo)
					.setCarType(carType)
					.setModelName(modelName)
					.setModelYear(modelYear);
		return carContract;
	}


	public void registerContract(Customer customer, Contract contract, Employee employee){
		if (customer.getId() == 0) {
			CustomerDao customerDao = new CustomerDao();
			customerDao.create(customer);
		}
		contract.setCustomerId(customer.getId())
				.setConditionOfUw(ConditionOfUw.WAIT);
		if (employee.getId() != 0)
			contract.setEmployeeId(employee.getId());
		ContractDao contractDao = new ContractDao();
		contractDao.create(contract);
	}

	public int planHealthInsurance(int targetAge, boolean targetSex, boolean riskPremiumCriterion){
		if(targetAge > 100) targetAge = 100;
		else if(targetAge > 80) targetAge = 80;
		else if(targetAge > 60) targetAge = 60;
		else if(targetAge > 50) targetAge = 50;
		else if(targetAge > 40) targetAge = 40;
		else if(targetAge > 30) targetAge = 30;
		else if(targetAge > 20) targetAge = 20;
		else if(targetAge > 10) targetAge = 10;
		else targetAge = 0;

		InsuranceDao insuranceDao = new InsuranceDao();
		int premium = insuranceDao.readHealthPremium(targetAge, targetSex, riskPremiumCriterion);
		return premium;
	}

	public int planFireInsurance(BuildingType buildingType, Long collateralAmount){
		Long collateralAmountCriterion;
		if(collateralAmount > 5000000000L) collateralAmountCriterion = 5000000000L;
		else if(collateralAmount > 1000000000L) collateralAmountCriterion = 1000000000L;
		else if(collateralAmount > 500000000L) collateralAmountCriterion = 500000000L;
		else if(collateralAmount > 100000000L) collateralAmountCriterion = 100000000L;
		else collateralAmountCriterion = 0L;;

		InsuranceDao insuranceDao = new InsuranceDao();
		int premium = insuranceDao.readFirePremium(buildingType, collateralAmountCriterion);
		return premium;
	}

	public int planCarInsurance(int targetAge, Long value){
		if(targetAge > 100) targetAge = 100;
		else if(targetAge > 80) targetAge = 80;
		else if(targetAge > 60) targetAge = 60;
		else if(targetAge > 50) targetAge = 50;
		else if(targetAge > 40) targetAge = 40;
		else if(targetAge > 30) targetAge = 30;
		else if(targetAge > 20) targetAge = 20;
		else targetAge = 0;

		Long valueCriterion;
		if(value > 150000000L) valueCriterion = 150000000L;
		else if(value > 90000000L) valueCriterion = 90000000L;
		else if(value > 70000000L) valueCriterion = 70000000L;
		else if(value > 50000000L) valueCriterion = 50000000L;
		else if(value > 30000000L) valueCriterion = 30000000L;
		else if(value > 10000000L) valueCriterion = 10000000L;
		else valueCriterion = 0L;

		InsuranceDao insuranceDao = new InsuranceDao();
		int premium = insuranceDao.readCarPremium(targetAge, valueCriterion);
		return premium;
	}

	public void readAccident(){

	}

	public List<Contract> readContract(InsuranceType insuranceType){
		ContractDao contractDao = new ContractDao();
		return contractDao.readAllByInsuranceType(insuranceType);
	}

	public void underwriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw){
		ContractDao contractDao = new ContractDao();
		Contract contract = contractDao.read(contractId);
		contract.setReasonOfUw(reasonOfUw);
		contract.setConditionOfUw(conditionOfUw);
		contract.setPublishStock(true);
		contractDao.update(contract);
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