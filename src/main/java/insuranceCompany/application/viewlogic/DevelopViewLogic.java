package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.BuildingType;
import insuranceCompany.application.domain.employee.Department;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.exception.MyFileException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.viewlogic.dto.insuranceDto.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndClose;


/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : DevViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class DevelopViewLogic implements ViewLogic {

    private Employee employee;

    private MyBufferedReader br;

    public DevelopViewLogic() {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void showMenu() {
        createMenuAndClose("개발팀 메뉴", "보험 개발", "판매인가 등록");
    }

    @Override
    public void work(String command) {
        try {
            switch (command){
                case "1" -> {
                    initEmployee();
                    if(employee!=null){
                        showInsuranceByEmployee();
                        this.menuDevelop(this.menuInsuranceType());
                    }
                }
                case "2" -> {
                    initEmployee();
                    if(employee!=null){
                        this.menuSalesAuthFile(showInsuranceByEmployeeAndSelect());
                    }
                    return;
                }
            }
        }
        catch (IllegalStateException e){
            System.out.println(e.getMessage());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initEmployee() throws IOException {
        while(true) {
            try {
                System.out.println("<< 직원을 선택하세요. >>");
                ArrayList<Employee> devEmployees = new EmployeeDao().readAllDev();
                for(Employee employee : devEmployees) {
                    System.out.println(employee.print());
                }
                System.out.println("---------------------------------");
                int eid = 0;
                eid = (int) br.verifyRead("직원 ID: ", eid);
                if(eid == 0) return;
                this.employee = new EmployeeDao().read(eid);
                if(this.employee != null) {
                    if (this.employee.getDepartment() == Department.DEV)
                        break;
                    else
                        System.out.println("개발팀 직원을 선택하세요!");
                }
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private ArrayList<Insurance> showInsuranceByEmployee() {
        System.out.println("<< "+employee.getName()+" 보험 개발 리스트 >>");
        ArrayList<Insurance> insuranceArrayList = new InsuranceDaoImpl().readByEmployeeId(employee.getId());
        if(insuranceArrayList.size() == 0) {
            System.out.println("--------------NONE---------------");
        }
        else{
            for(Insurance insurance : insuranceArrayList){
                System.out.println(insurance.print());
            }
            System.out.println("---------------------------------");
        }
        return insuranceArrayList;
    }

    private Insurance showInsuranceByEmployeeAndSelect() throws IOException {
        ArrayList<Insurance> insuranceArrayList = showInsuranceByEmployee();
        System.out.println("<< 파일을 추가할 보험을 선택하세요. >>");
        int insuranceId = br.verifyMenu("보험 ID: ", insuranceArrayList.size());
        Insurance insurance = new InsuranceDaoImpl().read(insuranceId);
        System.out.println(insurance.print());
        return insurance;
    }

    private InsuranceType menuInsuranceType() throws IOException {
        int insType = 0;
        createMenuAndClose("<< 보험 종류 선택 >>", "건강보험", "자동차보험", "화재보험");
        insType = br.verifyMenu("",3);
        return switch (insType){
            case 1 -> InsuranceType.HEALTH;
            case 2 -> InsuranceType.CAR;
            case 3 -> InsuranceType.FIRE;
            case 0 -> null;
            default -> throw new IllegalStateException("Unexpected value: " + insType);
        };
    }

    private void menuDevelop(InsuranceType type) throws IOException {
        if (type == null) return;

        DtoBasicInfo dtoBasicInfo = formInputBasicInfo();
        ArrayList<DtoGuarantee> dtoGuarantees = formInputGuaranteeInfo();
        int stPremium = formCalculatePremium(isCalcPremium());
        if(stPremium < 0) return;

        ArrayList<DtoTypeInfo> dtoTypeInfo = formInputTypeInfo(type, stPremium);

        formRegisterInsurance(type, dtoBasicInfo, dtoGuarantees,  dtoTypeInfo);
    }

    private DtoBasicInfo formInputBasicInfo() throws IOException {
        String name = "", description = "";
        int paymentPeriod = 0, contractPeriod = 0;
        System.out.println("<< 보험 기본 정보 >> (exit: 시스템 종료)");
        name = (String) br.verifyRead("보험 이름: ", name);
        description = (String) br.verifyRead("보험 설명: ", description);
        paymentPeriod = (int) br.verifyRead("납입 기간(년): ", paymentPeriod);
        contractPeriod = (int) br.verifyRead("만기 나이(세): ", contractPeriod);
        System.out.println("보험이름: "+name+"\t보험설명: "+description+"\t납입기간: "+paymentPeriod+"\t만기나이: "+contractPeriod);
        return new DtoBasicInfo(name, description, paymentPeriod, contractPeriod);
    }

    private ArrayList<DtoTypeInfo> formInputTypeInfo(InsuranceType type, int stPremium) throws IOException {
        return switch (type) {
            case HEALTH -> formDtoHealth(stPremium);
            case CAR -> formDtoCar(stPremium);
            case FIRE -> formDtoFire(stPremium);
            default -> null;
        };
    }

    private ArrayList<DtoTypeInfo> formDtoHealth(int stPremium) throws IOException {
        boolean isAddHealth = true;
        ArrayList<DtoTypeInfo> healthListInfo = new ArrayList<>();
        while(isAddHealth){
            int targetAge = 0, premium = -1, riskCriterion = -1;
            boolean targetSex;
            System.out.println("<< 건강 보험 정보 >> (exit: 시스템 종료)");
            targetAge = (int) br.verifyRead("보험 대상 나이: ", targetAge);
            targetSex = br.verifyMenu("보험 대상 성별 (1.남자 2.여자): ", 2) == 1;
            while(true) {
                riskCriterion = (int) br.verifyRead("위험부담 기준(개): ", riskCriterion);
                if(riskCriterion > 6){
                    System.out.println("위험부담 기준은 6개 이하여야 합니다.");
                }
                else break;
            }
            System.out.println("대상나이: "+targetAge+"\t대상성별: "+targetSex+"\t위험부담 기준: "+riskCriterion);
            DtoHealth dtoHealth = new DtoHealth(targetAge, targetSex, riskCriterion);

            premium = employee.calcSpecificPremium(stPremium, dtoHealth);
            System.out.printf("보험료: %d(원)\n", premium);
            healthListInfo.add(dtoHealth.setPremium(premium));

            switch (br.verifyMenu("<< 건강 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 2 -> isAddHealth = false;
            }
        }
        return healthListInfo;
    }

    private ArrayList<DtoTypeInfo> formDtoCar(int stPremium) throws IOException {
        boolean isAddCar = true;
        ArrayList<DtoTypeInfo> carListInfo = new ArrayList<>();
        while(isAddCar) {
            int targetAge = 0, premium = -1;
            long valueCriterion = -1;
            System.out.println("<< 자동차 보험 정보 >> (exit: 시스템 종료)");
            targetAge = (int) br.verifyRead("보험 대상 나이: ", targetAge);
            valueCriterion = (long) br.verifyRead("차량가액 기준(원): ", valueCriterion);
            System.out.println("대상나이: "+targetAge+"\t차량가액 기준: "+valueCriterion);
            DtoCar dtoCar = new DtoCar(targetAge, valueCriterion);

            premium = employee.calcSpecificPremium(stPremium, dtoCar);
            System.out.printf("보험료: %d(원)\n", premium);
            carListInfo.add(dtoCar.setPremium(premium));

            switch (br.verifyMenu("<< 자동차 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 2 -> isAddCar = false;
            }
        }
        return carListInfo;
    }

    private ArrayList<DtoTypeInfo> formDtoFire(int stPremium) throws IOException {
        boolean isAddFire = true;
        ArrayList<DtoTypeInfo> fireListInfo = new ArrayList<>();
        while(isAddFire){
            BuildingType buildingType = null;
            long collateralAmount = -1;
            int premium = -1;
            System.out.println("<< 화재 보험 정보 >> (exit: 시스템 종료)");
            System.out.println();
            switch (br.verifyMenu("건물종류 선택: 1. 상업용 2. 산업용 3. 기관용 4. 거주용\n", 4)){
                case 1 -> buildingType = BuildingType.COMMERCIAL;
                case 2 -> buildingType = BuildingType.INDUSTRIAL;
                case 3 -> buildingType = BuildingType.INSTITUTIONAL;
                case 4 -> buildingType = BuildingType.RESIDENTIAL;
            }
            collateralAmount = (long) br.verifyRead("담보금액: ", collateralAmount);
            System.out.println("건물 종류: "+buildingType+"\t담보금액: "+collateralAmount);
            DtoFire dtoFire = new DtoFire(buildingType, collateralAmount);

            premium = employee.calcSpecificPremium(stPremium, dtoFire);
            System.out.printf("보험료: %d(원)\n", premium);
            fireListInfo.add(dtoFire.setPremium(premium));

            switch (br.verifyMenu("<< 화재 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 2 -> isAddFire = false;
            }
        }
        return fireListInfo;
    }

    private ArrayList<DtoGuarantee> formInputGuaranteeInfo() throws IOException {
        boolean isAddGuarantee = true;
        ArrayList<DtoGuarantee> guaranteeListInfo = new ArrayList<>();
        while(isAddGuarantee) {
            String gName = "", gDescription = "";
            long gAmount = 0;
            System.out.println("<< 보장 상세 내용 >> (exit: 시스템 종료)");
            gName = (String) br.verifyRead("보장명: ", gName);
            gDescription = (String) br.verifyRead("보장 상세 내용: ", gDescription);
            gAmount = (Long) br.verifyRead("보장금액: ", gAmount);
            System.out.println("보장명: "+gName+"\t보장 설명: "+gDescription+"\t보장금액: "+gAmount);
            guaranteeListInfo.add(new DtoGuarantee(gName, gDescription, gAmount));
            switch (br.verifyMenu("<< 보장을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 2 -> isAddGuarantee = false;
            }
        }
        return guaranteeListInfo;
    }

    private boolean isCalcPremium() throws IOException {
        boolean isCalcPremium = false;
        boolean forWhile = true;
        while(forWhile) {
            switch (br.verifyMenu("<< 보험료를 산출하시겠습니까? >>\n1.예 2.아니오\n",2)) {
                case 1 -> {
                    isCalcPremium = true;
                    forWhile = false;
                }
                case 2 -> {
                    switch (br.verifyMenu("<< 정말 취소하시겠습니까? >>\n1. 예 2. 아니오\n", 2)) {
                        case 1 -> forWhile = false;
                    }
                }
            }
        }
        return isCalcPremium;
    }

    private int formCalculatePremium(boolean isCalcPremium) throws IOException {
        if(!isCalcPremium) return -1;
        return calcStandardPremium();
    }

    private int calcStandardPremium() throws IOException {
        boolean forWhile = true;
        int stPremium= -1;
        while(forWhile) {
            long damageAmount = 0, countContract = 0, businessExpense = 0;
            double profitMargin = 0;
            System.out.println("<< 기준 보험료 산출 >> (exit: 시스템 종료)");
            damageAmount = (long) br.verifyRead("발생손해액(단위:만원): ", damageAmount);
            countContract = (long) br.verifyRead("계약건수(건): ", countContract);
            businessExpense = (long) br.verifyRead("사업비(단위:만원): ", businessExpense);
            profitMargin = (Double) br.verifyRead("이익률(1-99%): ", profitMargin);
            stPremium = employee.calcStandardPremium(damageAmount, countContract, businessExpense, profitMargin);
            System.out.printf("기준 보험료: %d(원)\n", stPremium);
            switch (br.verifyMenu("<< 산출된 기준 보험료로 확정하시겠습니까? >>\n1.예 2. 아니오\n", 2)){
                case 1 -> forWhile = false;
            }
        }
        return stPremium;
    }

    private void formRegisterInsurance(InsuranceType type, DtoBasicInfo dtoBasicInfo, ArrayList<DtoGuarantee> dtoGuaranteeList, ArrayList<DtoTypeInfo> dtoTypeInfoList) throws IOException {
        boolean forWhile = true;
        while(forWhile){
            switch (br.verifyMenu("<< 보험을 저장하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 1 -> {
                    employee.develop(type, dtoBasicInfo, dtoGuaranteeList, dtoTypeInfoList);
                    System.out.println("정상적으로 보험이 저장되었습니다!");
                    forWhile = false;
                }
                case 2 -> {
                    switch (br.verifyMenu("<< 정말 취소하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                        case 1 -> forWhile = false;
                    }
                }
            }
        }
    }

    private void menuSalesAuthFile(Insurance insurance) throws IOException {
        try {
            loop : while(true) {
                System.out.println("<< 해당 보험의 추가할 파일을 선택하세요. >>");
                String query = "1. 보험상품신고서\n"
                        + "2. 선임계리사 검증기초서류\n"
                        + "3. 보험요율산출기관 검증확인서\n"
                        + "4. 금융감독원 판매인가서\n"
                        + "0. 이전 메뉴로 돌아가기\n";
                switch (br.verifyMenu(query, 5)){
                    // 보험상품신고서
                    case 1 -> {
                        switch (employee.registerAuthProdDeclaration(insurance)) {
                            // 파일 업로드 성공
                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                            // 파일 업로드 취소
                            case -1 -> { return; }
                            // 파일 업로드 변경
                            case 0 -> {
                                switch (br.verifyMenu("<< 이미 파일이 존재합니다! 변경하시겠습니까? >>\n\"1. 예 2. 아니오\n", 2)){
                                    case 1 -> {
                                        switch (employee.registerAuthProdDeclaration(insurance, null)) {
                                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                                            case -1 -> { return; }
                                        }
                                    }
                                }
                            }
                            // 판매상태 변경
                            case 2 -> {
                                switch (br.verifyMenu("<< 모든 인가파일이 등록되었습니다! 판매상태를 변경해주세요.\n1. 허가 2. 불허", 2)){
                                    case 1 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.PERMISSION);
                                    case 2 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.DISALLOWANCE);
                                }
                            }
                        }
                    }
                    case 2 -> {
                        switch (employee.registerAuthSrActuaryVerification(insurance)) {
                            // 파일 업로드 성공
                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                            // 파일 업로드 취소
                            case -1 -> { return; }
                            // 파일 업로드 변경
                            case 0 -> {
                                switch (br.verifyMenu("<< 이미 파일이 존재합니다! 변경하시겠습니까? >>\n\"1. 예 2. 아니오\n", 2)){
                                    case 1 -> {
                                        switch (employee.registerAuthSrActuaryVerification(insurance, null)) {
                                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                                            case -1 -> { return; }
                                        }
                                    }
                                }
                            }
                            // 판매상태 변경
                            case 2 -> {
                                switch (br.verifyMenu("<< 모든 인가파일이 등록되었습니다! 판매상태를 변경해주세요.\n1. 허가 2. 불허", 2)){
                                    case 1 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.PERMISSION);
                                    case 2 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.DISALLOWANCE);
                                }
                            }
                        }
                    }
                    case 3 -> {
                        switch (employee.registerAuthIsoVerification(insurance)) {
                            // 파일 업로드 성공
                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                            // 파일 업로드 취소
                            case -1 -> { return; }
                            // 파일 업로드 변경
                            case 0 -> {
                                switch (br.verifyMenu("<< 이미 파일이 존재합니다! 변경하시겠습니까? >>\n\"1. 예 2. 아니오\n", 2)){
                                    case 1 -> {
                                        switch (employee.registerAuthIsoVerification(insurance, null)) {
                                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                                            case -1 -> { return; }
                                        }
                                    }
                                }
                            }
                            // 판매상태 변경
                            case 2 -> {
                                switch (br.verifyMenu("<< 모든 인가파일이 등록되었습니다! 판매상태를 변경해주세요.\n1. 허가 2. 불허", 2)){
                                    case 1 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.PERMISSION);
                                    case 2 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.DISALLOWANCE);
                                }
                            }
                        }
                    }
                    case 4 -> {
                        switch (employee.registerAuthFssOfficialDoc(insurance)) {
                            // 파일 업로드 성공
                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                            // 파일 업로드 취소
                            case -1 -> { return; }
                            // 파일 업로드 변경
                            case 0 -> {
                                switch (br.verifyMenu("<< 이미 파일이 존재합니다! 변경하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                                    case 1 -> {
                                        switch (employee.registerAuthFssOfficialDoc(insurance, null)) {
                                            case 1 -> System.out.println("정상적으로 업로드되었습니다!");
                                            case -1 -> { return; }
                                        }
                                    }
                                }
                            }
                            // 판매상태 변경
                            case 2 -> {
                                switch (br.verifyMenu("<< 모든 인가파일이 등록되었습니다! 판매상태를 변경해주세요.\n1. 허가 2. 불허\n", 2)){
                                    case 1 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.PERMISSION);
                                    case 2 -> employee.modifySalesAuthState(insurance, SalesAuthorizationState.DISALLOWANCE);
                                }
                            }
                        }
                    }
                    case 0 -> {
                        break loop;
                    }
                }
            }
        }
        catch (MyFileException e) {
            System.out.println(e.getMessage());
        }
    }
}
