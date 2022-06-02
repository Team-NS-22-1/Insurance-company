package insuranceCompany.application.domain.employee;


import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.insurance.*;
import insuranceCompany.application.domain.payment.Account;
import insuranceCompany.application.global.exception.InputInvalidDataException;
import insuranceCompany.application.viewlogic.dto.compDto.AccountRequestDto;
import insuranceCompany.application.viewlogic.dto.compDto.AssessDamageResponseDto;
import insuranceCompany.application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFile;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.global.utility.DocUtil;
import insuranceCompany.application.global.utility.FileDialogUtil;
import insuranceCompany.application.viewlogic.dto.insuranceDto.*;

import java.io.IOException;
import java.sql.SQLException;
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
				.setSalesAuthFile(new SalesAuthorizationFile());
		switch (type) {
			case HEALTH -> developHealth(insurance, typeInfoList);
			case CAR -> developCar(insurance, typeInfoList);
			case FIRE -> developFire(insurance, typeInfoList);
		}
		new InsuranceDaoImpl().create(insurance);
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

	private DevelopInfo developDevInfo() {
		return new DevelopInfo().setEmployeeId(this.id)
				.setDevelopDate(LocalDate.now())
				.setSalesAuthorizationState(SalesAuthorizationState.WAIT);
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

	public void modifySalesAuthState(Insurance insurance, SalesAuthorizationState modify) {
		insurance.getDevInfo().setSalesAuthorizationState(modify);
		new InsuranceDaoImpl().updateBySalesAuthState(insurance);
	}

	private boolean checkSalesAuthState(Insurance insurance) {
		SalesAuthorizationFile salesAuthFile = insurance.getSalesAuthFile();
		return (salesAuthFile.getProdDeclaration()!=null) && (salesAuthFile.getFssOfficialDoc()!=null)
				&& (salesAuthFile.getIsoVerification()!=null) && (salesAuthFile.getSrActuaryVerification()!=null);
	}

	public int registerAuthProdDeclaration(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getProdDeclaration()!=null) return 0;
		else return uploadProd(insurance);
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
		new InsuranceDaoImpl().updateByProd(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthSrActuaryVerification(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getSrActuaryVerification()!=null) return 0;
		else return uploadSrActuary(insurance);
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
		new InsuranceDaoImpl().updateBySrActuary(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthIsoVerification(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getIsoVerification()!=null) return 0;
		else return uploadIso(insurance);
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
		new InsuranceDaoImpl().updateByIso(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}

	public int registerAuthFssOfficialDoc(Insurance insurance) throws IOException {
		if(insurance.getSalesAuthFile().getFssOfficialDoc()!=null) return 0;
		else return uploadFss(insurance);
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
		new InsuranceDaoImpl().updateByFss(insurance);
		if(checkSalesAuthState(insurance)) return 2;
		return 1;
	}
	public AssessDamageResponseDto assessDamage(Accident accident, AccountRequestDto accountRequestDto){
		return AssessDamageResponseDto.builder().accidentDocumentFile(uploadLossAssessment(accident))
				.account(new Account().setBankType(accountRequestDto.getBankType()).setAccountNo(accountRequestDto.getAccountNo()))
				.build();
	}

	private AccidentDocumentFile uploadLossAssessment(Accident accident) {



		String dir = "./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()+"/"+AccDocType.LOSSASSESSMENT.getDesc()+".hwp";
		AccidentDocumentFile lossAssessment = uploadDocfile(accident, dir, AccDocType.LOSSASSESSMENT);

		boolean isExist = false;
		int lossId = 0;
		for (AccidentDocumentFile accidentDocumentFile : accident.getAccDocFileList().values()) {
			if (accidentDocumentFile.getType() == AccDocType.LOSSASSESSMENT) {
				lossId = accidentDocumentFile.getId();
				isExist = true;
			}
		}
		AccidentDocumentFileDao accidentDocumentFileDao = new AccidentDocumentFileDaoImpl();
		if (isExist) {
			accidentDocumentFileDao.update(lossId);
		} else {
			accidentDocumentFileDao.create(lossAssessment);
		}
		return lossAssessment;
	}

	private AccidentDocumentFile uploadDocfile(Accident accident, String dir,AccDocType accDocType) {
		DocUtil instance = DocUtil.getInstance();
		String fileDir = instance.upload(dir);
		if (fileDir == null) {
			return null;
		}
		AccidentDocumentFile accidentDocumentFile = new AccidentDocumentFile();
		accidentDocumentFile.setFileAddress(fileDir)
				.setAccidentId(accident.getId())
				.setType(accDocType);
		accident.getAccDocFileList().put(accDocType, accidentDocumentFile);
		return accidentDocumentFile;
	}

	public void investigateDamage(InvestigateDamageRequestDto dto, Accident accident){
		if (!accident.getAccDocFileList().containsKey(AccDocType.INVESTIGATEACCIDENT)) {
			String dir = "./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()+"/"+AccDocType.INVESTIGATEACCIDENT.getDesc()+".hwp";
			uploadDocfile(accident,dir,AccDocType.INVESTIGATEACCIDENT);
		}


		accident.setLossReserves(dto.getLossReserves());
		if(accident.getAccidentType() == AccidentType.CARACCIDENT)
			((CarAccident)accident).setErrorRate(dto.getErrorRate());

		AccidentDao accidentDao = new AccidentDaoImpl();
		if(accident.getAccidentType() == AccidentType.CARACCIDENT)
			accidentDao.updateLossReserveAndErrorRate(accident);
		else
			accidentDao.updateLossReserve(accident);

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


	public void registerContract(Customer customer, Contract contract, Employee employee) throws SQLException {
		if (customer.getId() == 0) {
			CustomerDaoImpl customerDao = new CustomerDaoImpl();
			customerDao.create(customer);
		}
		contract.setCustomerId(customer.getId())
				.setConditionOfUw(ConditionOfUw.WAIT);
		if (employee.getId() != 0)
			contract.setEmployeeId(employee.getId());
		ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
		contractDaoImpl.create(contract);
	}

	public int planHealthInsurance(int targetAge, boolean targetSex, boolean riskPremiumCriterion, Insurance insurance){
		int premium = 0;

		if(targetAge > 100) targetAge = 100;
		else if(targetAge > 80) targetAge = 80;
		else if(targetAge > 60) targetAge = 60;
		else if(targetAge > 50) targetAge = 50;
		else if(targetAge > 40) targetAge = 40;
		else if(targetAge > 30) targetAge = 30;
		else if(targetAge > 20) targetAge = 20;
		else if(targetAge > 10) targetAge = 10;
		else targetAge = 0;

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();

		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			HealthDetail healthDetail = (HealthDetail) insuranceDetail;
			if (healthDetail.getTargetAge() == targetAge && healthDetail.isTargetSex() == targetSex && (healthDetail.getRiskPremiumCriterion() > 3) == riskPremiumCriterion) {
				premium = healthDetail.getPremium();
				break;
			}
		}
//		InsuranceDao insuranceDao = new InsuranceDao();
//		int premium = insuranceDao.readHealthPremium(targetAge, targetSex, riskPremiumCriterion);
		return premium;
	}

	public int planFireInsurance(BuildingType buildingType, Long collateralAmount, Insurance insurance){
		int premium = 0;

		Long collateralAmountCriterion;
		if(collateralAmount > 5000000000L) collateralAmountCriterion = 5000000000L;
		else if(collateralAmount > 1000000000L) collateralAmountCriterion = 1000000000L;
		else if(collateralAmount > 500000000L) collateralAmountCriterion = 500000000L;
		else if(collateralAmount > 100000000L) collateralAmountCriterion = 100000000L;
		else collateralAmountCriterion = 0L;;

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();

		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			FireDetail fireDetail = (FireDetail) insuranceDetail;
			if (fireDetail.getTargetBuildingType() == buildingType && fireDetail.getCollateralAmountCriterion() == collateralAmountCriterion) {
				premium = fireDetail.getPremium();
				break;
			}
		}

//		InsuranceDao insuranceDao = new InsuranceDao();
//		int premium = insuranceDao.readFirePremium(buildingType, collateralAmountCriterion);
		return premium;
	}

	public int planCarInsurance(int targetAge, Long value, Insurance insurance){
		int premium = 0;

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

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();

		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			CarDetail carDetail = (CarDetail) insuranceDetail;
			if (carDetail.getTargetAge() == targetAge && carDetail.getValueCriterion() == valueCriterion) {
				premium = carDetail.getPremium();
				break;
			}
		}

//		InsuranceDao insuranceDao = new InsuranceDao();
//		int premium = insuranceDao.readCarPremium(targetAge, valueCriterion);
		return premium;
	}

	public List<Accident> readAccident(){
		AccidentDao accidentDao = new AccidentDaoImpl();
		return accidentDao.readAllByEmployeeId(this.getId());
	}

	public List<Contract> readContract(InsuranceType insuranceType){
		ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
		return contractDaoImpl.readAllByInsuranceType(insuranceType);
	}

	public void underwriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw){
		ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
		Contract contract = contractDaoImpl.read(contractId);
		contract.setReasonOfUw(reasonOfUw);
		contract.setConditionOfUw(conditionOfUw);
		contract.setPublishStock(true);

		ContractDaoImpl contractDaoImpl1 = new ContractDaoImpl();
		contractDaoImpl1.update(contract);
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