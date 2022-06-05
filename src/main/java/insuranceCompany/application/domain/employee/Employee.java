package insuranceCompany.application.domain.employee;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.dao.user.UserDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFile;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.insurance.*;
import insuranceCompany.application.domain.payment.Account;
import insuranceCompany.application.global.exception.InputInvalidDataException;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.exception.MyNotExistContractException;
import insuranceCompany.application.global.exception.NoResultantException;
import insuranceCompany.application.global.utility.FileDialogUtil;
import insuranceCompany.application.login.User;
import insuranceCompany.application.viewlogic.dto.UserDto.UserDto;
import insuranceCompany.application.viewlogic.dto.compDto.AccountRequestDto;
import insuranceCompany.application.viewlogic.dto.compDto.AssessDamageResponseDto;
import insuranceCompany.application.viewlogic.dto.compDto.InvestigateDamageRequestDto;
import insuranceCompany.application.viewlogic.dto.contractDto.CarContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.ContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.FireContractDto;
import insuranceCompany.application.viewlogic.dto.contractDto.HealthContractDto;
import insuranceCompany.application.viewlogic.dto.customerDto.CustomerDto;
import insuranceCompany.application.viewlogic.dto.insuranceDto.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static insuranceCompany.application.global.constant.DevelopViewLogicConstants.EXCEPTION_NO_RESULT_LIST;
import static insuranceCompany.application.global.utility.CriterionSetUtil.*;

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
		long purePremium = damageAmount / countContract;
		long riskCost = businessExpense / countContract;
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
		boolean riskCriterion = dtoHealth.getRiskCriterion();

		switch (targetAge) {
			case 100 -> weightRatio *= 1.34;
			case 80 -> weightRatio *= 1.3;
			case 60 -> weightRatio *= 1.26;
			case 50 -> weightRatio *= 1.22;
			case 40 -> weightRatio *= 1.18;
			case 30 -> weightRatio *= 1.14;
			case 20 -> weightRatio *= 1.1;
			default -> weightRatio *= 1.06;
		}

		weightRatio = (targetSex) ? weightRatio * 1.2 : weightRatio * 1.1;

		weightRatio = (riskCriterion) ? weightRatio * 1.4 : weightRatio;

		return (int) (stPremium * weightRatio);
	}

	private int calcCarPremium(int stPremium, DtoCar dtoCar) {
		double weightRatio = 1.0;
		int targetAge = dtoCar.getTargetAge();
		long valueCriterion = dtoCar.getValueCriterion();

		switch (targetAge) {
			case 100 -> weightRatio *= 1.4;
			case 80 -> weightRatio *= 1.35;
			case 60 -> weightRatio *= 1.2;
			case 50 -> weightRatio *= 1.15;
			case 40 -> weightRatio *= 1.15;
			case 30 -> weightRatio *= 1.2;
			case 20 -> weightRatio *= 1.35;
			default -> weightRatio *= 1.4;
		}

		if(valueCriterion == 150000000L) weightRatio *= 2.2;
		else if(valueCriterion == 90000000L) weightRatio *= 1.8;
		else if(valueCriterion == 70000000L) weightRatio *= 1.6;
		else if(valueCriterion == 50000000L) weightRatio *= 1.4;
		else if(valueCriterion == 30000000L) weightRatio *= 1.3;
		else if(valueCriterion == 10000000L) weightRatio *= 1.15;
		else weightRatio *= 1.05;

		return (int) (stPremium * weightRatio);
	}

	private int calcFirePremium(int stPremium, DtoFire dtoFire) {
		double weightRatio = 1.0;
		BuildingType targetBuildingType = dtoFire.getBuildingType();
		long collateralAmountCriterion = dtoFire.getCollateralAmount();

		switch (targetBuildingType){
			case COMMERCIAL -> weightRatio *= 1.3;
			case INDUSTRIAL -> weightRatio *= 1.25;
			case INSTITUTIONAL -> weightRatio *= 1.15;
			case RESIDENTIAL -> weightRatio *= 1.1;
		}

		if(collateralAmountCriterion == 5000000000L) weightRatio *= 1.1;
		else if(collateralAmountCriterion == 1000000000L) weightRatio *= 1.15;
		else if(collateralAmountCriterion == 500000000L) weightRatio *= 1.2;
		else if(collateralAmountCriterion == 100000000L) weightRatio *= 1.25;
		else weightRatio *= 1.3;

		return (int) (stPremium * weightRatio);
	}

	public void modifySalesAuthState(Insurance insurance, SalesAuthorizationState modify) {
		insurance.getDevInfo().setSalesAuthorizationState(modify);
		if(modify == SalesAuthorizationState.PERMISSION){
			insurance.getDevInfo().setSalesStartDate(LocalDate.now());
		}
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

		createOrUpdateFile(accident, lossAssessment);
		return lossAssessment;
	}

	private void createOrUpdateFile(Accident accident, AccidentDocumentFile accidentDocFile) {
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
			accidentDocumentFileDao.create(accidentDocFile);
		}
	}

	private AccidentDocumentFile uploadDocfile(Accident accident, String dir,AccDocType accDocType) {
		String fileDir = FileDialogUtil.uploadAccidentDocumentFile(dir);
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
			AccidentDocumentFile accidentDocumentFile = uploadDocfile(accident, dir, AccDocType.INVESTIGATEACCIDENT);
			createOrUpdateFile(accident,accidentDocumentFile);
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

	public ArrayList<Insurance> readInsurances() {
		InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
		return insuranceDao.readAll();
	}

	public Insurance readInsurance(int insuranceId) {
		InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
		return insuranceDao.read(insuranceId);
	}

	public Customer readCustomer(int customerId) {
		CustomerDaoImpl customerDao = new CustomerDaoImpl();
		return customerDao.read(customerId);
	}

	public int planHealthInsurance(int targetAge, boolean targetSex, int riskCount, Insurance insurance){
		int premium = 0;
		targetAge = setTargetAge(targetAge);
		boolean riskCriterion = setRiskCriterion(riskCount);

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			HealthDetail healthDetail = (HealthDetail) insuranceDetail;
			if (healthDetail.getTargetAge() == targetAge && healthDetail.isTargetSex() == targetSex && (healthDetail.isRiskCriterion()) == riskCriterion) {
				premium = healthDetail.getPremium();
				break;
			}
		}
		if (premium == 0)
			throw new NoResultantException();
		return premium;
	}

	public int planFireInsurance(BuildingType buildingType, Long collateralAmount, Insurance insurance){
		int premium = 0;
		Long collateralAmountCriterion = setCollateralAmountCriterion(collateralAmount);

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			FireDetail fireDetail = (FireDetail) insuranceDetail;
			if (fireDetail.getTargetBuildingType() == buildingType && fireDetail.getCollateralAmountCriterion() == collateralAmountCriterion) {
				premium = fireDetail.getPremium();
				break;
			}
		}
		if (premium == 0)
			throw new NoResultantException();
		return premium;
	}

	public int planCarInsurance(int targetAge, Long value, Insurance insurance){
		int premium = 0;
		targetAge = setTargetAge(targetAge);
		Long valueCriterion = setValueCriterion(value);

		ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
		for (InsuranceDetail insuranceDetail : insuranceDetails) {
			CarDetail carDetail = (CarDetail) insuranceDetail;
			if (carDetail.getTargetAge() == targetAge && carDetail.getValueCriterion() == valueCriterion) {
				premium = carDetail.getPremium();
				break;
			}
		}
		if (premium == 0)
			throw new NoResultantException();
		return premium;
	}

	public Customer registerCustomer(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setName(customerDto.getName())
				.setSsn(customerDto.getSsn())
				.setAddress(customer.getAddress())
				.setPhone(customerDto.getPhone())
				.setEmail(customerDto.getEmail())
				.setJob(customer.getJob());
		CustomerDaoImpl customerDao = new CustomerDaoImpl();
		customerDao.create(customer);
		return customer;
	}

	public User registerUser(UserDto userDto) {
		User user = new User();
		user.setUserId(userDto.getUserId())
				.setPassword(userDto.getPassword())
				.setRoleId(userDto.getRoleId());
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.create(user);
		return user;
	}

	public Contract registerContract(Customer customer, ContractDto contractDto, Insurance insurance) {
		Contract contract = null;
		switch (insurance.getInsuranceType()){
			case HEALTH -> {
				HealthContract healthContract = new HealthContract();

				healthContract.setHeight(((HealthContractDto) contractDto).getHeight())
						.setWeight(((HealthContractDto) contractDto).getWeight())
						.setDrinking(((HealthContractDto) contractDto).isDrinking())
						.setSmoking(((HealthContractDto) contractDto).isSmoking())
						.setDriving(((HealthContractDto) contractDto).isDriving())
						.setDangerActivity(((HealthContractDto) contractDto).isDangerActivity())
						.setHavingDisease(((HealthContractDto) contractDto).isHavingDisease())
						.setTakingDrug(((HealthContractDto) contractDto).isTakingDrug())
						.setDiseaseDetail(((HealthContractDto) contractDto).getDiseaseDetail())
						.setInsuranceId(insurance.getId())
						.setCustomerId(customer.getId())
						.setEmployeeId(contractDto.getEmployeeId())
						.setPremium(contractDto.getPremium())
						.setConditionOfUw(ConditionOfUw.WAIT);

				ContractDaoImpl contractDao = new ContractDaoImpl();
				contractDao.create(healthContract);
				contract = healthContract;
			}
			case FIRE -> {
				FireContract fireContract = new FireContract();

				fireContract.setBuildingArea(((FireContractDto) contractDto).getBuildingArea())
						.setBuildingType(((FireContractDto) contractDto).getBuildingType())
						.setCollateralAmount(((FireContractDto) contractDto).getCollateralAmount())
						.setActualResidence(((FireContractDto) contractDto).isActualResidence())
						.setSelfOwned(((FireContractDto) contractDto).isSelfOwned())
						.setInsuranceId(insurance.getId())
						.setCustomerId(customer.getId())
						.setEmployeeId(contractDto.getEmployeeId())
						.setPremium(contractDto.getPremium())
						.setConditionOfUw(ConditionOfUw.WAIT);

				ContractDaoImpl contractDao = new ContractDaoImpl();
				contractDao.create(fireContract);
				contract = fireContract;
			}
			case CAR -> {
				CarContract carContract = new CarContract();

				carContract.setCarNo(((CarContractDto) contractDto).getCarNo())
						.setCarType(((CarContractDto) contractDto).getCarType())
						.setModelName(((CarContractDto) contractDto).getModelName())
						.setModelYear(((CarContractDto) contractDto).getModelYear())
						.setValue(((CarContractDto) contractDto).getValue())
						.setInsuranceId(insurance.getId())
						.setCustomerId(customer.getId())
						.setEmployeeId(contractDto.getEmployeeId())
						.setPremium(contractDto.getPremium())
						.setConditionOfUw(ConditionOfUw.WAIT);

				ContractDaoImpl contractDao = new ContractDaoImpl();
				contractDao.create(carContract);
				contract = carContract;
			}
		}
		return contract;
	}

	public List<Accident> readAccident(){
		AccidentDao accidentDao = new AccidentDaoImpl();
		return accidentDao.readAllByEmployeeId(this.getId());
	}

	public Contract readContract(int contractId, InsuranceType insuranceType) {
		if (contractId < 1) throw new InputInvalidDataException();
		ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
		Contract contract = contractDaoImpl.read(contractId);

		//if (contract == null) throw new MyNotExistContractException();
		InsuranceDaoImpl insuranceDaoImpl = new InsuranceDaoImpl();
		Insurance insurance = insuranceDaoImpl.read(contract.getInsuranceId());

		if (!insurance.getInsuranceType().equals(insuranceType)) throw new MyNotExistContractException();

		return contract;
	}

	public void underwriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw){
		ContractDaoImpl readContractDaoImpl = new ContractDaoImpl();
		Contract contract = readContractDaoImpl.read(contractId);
		contract.setReasonOfUw(reasonOfUw);
		contract.setConditionOfUw(conditionOfUw);

		if (!conditionOfUw.getName().equals(ConditionOfUw.RE_AUDIT.getName())) contract.setPublishStock(true);

		ContractDaoImpl updateContractDaoImpl = new ContractDaoImpl();
		updateContractDaoImpl.update(contract);
	}

	public ArrayList<Insurance> readMyInsuranceList() {
		return new InsuranceDaoImpl().readByEmployeeId(this.id);
	}

	public Insurance readMyInsurance(int insuranceId) {
		Insurance insurance = new InsuranceDaoImpl().read(insuranceId);
		if (insurance.getDevInfo().getEmployeeId() != this.id) {
			throw new MyIllegalArgumentException(EXCEPTION_NO_RESULT_LIST);
		}
		return insurance;
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