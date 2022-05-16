package main.application.viewlogic;

import main.domain.contract.BuildingType;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.domain.insurance.inputDto.*;
import main.exception.MyCloseSequence;
import main.utility.MyBufferedReader;
import main.exception.InputException;
import main.application.ViewLogic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static main.utility.MessageUtil.createMenuAndClose;


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
public class DevViewLogic implements ViewLogic {

    private EmployeeListImpl employeeList;
    private InsuranceListImpl insuranceList;

    private Employee employee;

    private MyBufferedReader br;

    public DevViewLogic() {}

    public DevViewLogic(EmployeeListImpl employeeList, InsuranceListImpl insuranceList) {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employeeList = employeeList;
        this.insuranceList = insuranceList;
    }

    @Override
    public void showMenu() {
        createMenuAndClose("개발팀 메뉴", "보험 개발", "판매인가 등록");
    }

    @Override
    public void work(String command) throws MyCloseSequence {
        try {
            switch (command){
                case "1" -> {
                    testInitEmployee();
                    showInsuranceByEmployee();
                    if(employee!=null)
                        this.menuDevelop(this.menuInsuranceType());
                }
                case "2" -> {
                    System.out.println("메뉴 준비중입니다.");
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

    private void testInitEmployee() throws IOException {
        while(true){
            try {
                System.out.println("<< 직원을 선택하세요. >>");
                ArrayList<Employee> employeeArrayList = this.employeeList.readAll();
                for(Employee employee : employeeArrayList){
                    System.out.println(employee.print());
                }
                System.out.println("---------------------------------");
                int employeeId = br.verifyMenu("직원 ID: ", employeeArrayList.size());
                if(employeeId == 0) break;
                this.employee = this.employeeList.read(employeeId);
                if(this.employee.getDepartment() != Department.DEV){
                    System.out.println("해당 직원은 개발팀 직원이 아닙니다!");
                    continue;
                }
                break;
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showInsuranceByEmployee() {
        if(employee == null) return;

        System.out.println("<< "+employee.getName()+" 직원 보험 개발 리스트 >>");
        ArrayList<Insurance> insuranceArrayList = insuranceList.readByEid(employee.getId());
        if(insuranceArrayList.size() == 0) {
            System.out.println("--------------NONE---------------");
            return;
        }
        for(Insurance insurance : insuranceArrayList){
            System.out.println(insurance.print());
        }
        System.out.println("---------------------------------");
    }

    private InsuranceType menuInsuranceType() throws IOException, MyCloseSequence {
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

    private void menuDevelop(InsuranceType type) throws IOException, MyCloseSequence {
        if (type == null) return;
        Insurance insurance = employee.develop(
                formInputBasicInfo(), // return Basic Info of Insurance
                formInputTypeInfo(type), // return Type Info of Insurance (Type is Health, Car, Fire)
                formInputGuaranteeInfo() // return Guarantee Info of Insurance (Guarantee Info is list)
        );
        int premium = formCalculatePremium(isCalcPremium());
        if(premium < 0) return;
        formRegisterInsurance(insurance, premium);
    }

    private DtoBasicInfo formInputBasicInfo() throws IOException, MyCloseSequence{
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

    private DtoTypeInfo formInputTypeInfo(InsuranceType type) throws IOException, MyCloseSequence {
        return switch (type) {
            case HEALTH -> formDtoHealth();
            case CAR -> formDtoCar();
            case FIRE -> formDtoFire();
            default -> null;
        };
    }

    private DtoHealth formDtoHealth() throws IOException {
        int targetAge = 0, riskCriterion = -1;
        boolean targetSex;
        System.out.println("<< 건강 보험 정보 >> (exit: 시스템 종료)");
        targetAge = (int) br.verifyRead("보험 대상 나이: ", targetAge);
        targetSex = br.verifyMenu("보험 대상 성별 (1.남자 2.여자): ", 2) == 1;
        riskCriterion = (int) br.verifyRead("위험부담 기준(개): ", riskCriterion);
        System.out.println("대상나이: "+targetAge+"\t대상성별: "+targetSex+"\t위험부담 기준: "+riskCriterion);
        return new DtoHealth(targetAge, targetSex, riskCriterion);
    }

    private DtoCar formDtoCar() throws IOException {
        int targetAge = 0;
        long valueCriterion = -1;
        System.out.println("<< 자동차 보험 정보 >> (exit: 시스템 종료)");
        targetAge = (int) br.verifyRead("보험 대상 나이: ", targetAge);
        valueCriterion = (long) br.verifyRead("차량가액 기준(원): ", valueCriterion);
        System.out.println("대상나이: "+targetAge+"\t차량가액 기준: "+valueCriterion);
        return new DtoCar(targetAge, valueCriterion);
    }

    private DtoFire formDtoFire() throws IOException {
        BuildingType buildingType = null;
        long collateralAmount = -1;
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
        return new DtoFire(buildingType, collateralAmount);
    }

    private ArrayList<DtoGuarantee> formInputGuaranteeInfo() throws IOException, MyCloseSequence {
        boolean isAddGuarantee = true;
        ArrayList<DtoGuarantee> guaranteeListInfo = new ArrayList<>();
        String gName = "", gDescription = "";
        while(isAddGuarantee) {
            System.out.println("<< 보장 상세 내용 >> (exit: 시스템 종료)");
            gName = (String) br.verifyRead("보장명: ", gName);
            gDescription = (String) br.verifyRead("보장 상세 내용: ", gDescription);
            System.out.println("보장명: "+gName+"\t보장 설명: "+gDescription);
            guaranteeListInfo.add(new DtoGuarantee(gName, gDescription));
            switch (br.verifyMenu("<< 보장을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 2 -> isAddGuarantee = false;
            }
        }
        return guaranteeListInfo;
    }

    private boolean isCalcPremium() throws IOException, MyCloseSequence {
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

    private int formCalculatePremium(boolean isCalcPremium) throws IOException, MyCloseSequence {
        int premium = -1;

        if(!isCalcPremium) return premium;

        boolean forWhile = true;
        while(forWhile){
            try {
                String query = "<< 보험료 산출 방식 선택>> (exit: 시스템 종료)\n"
                        + "1. 순보험료법 산출\n"
                        + "2. 손해율법 산출\n";
                switch (br.verifyMenu(query, 2)){
                    case 1 -> {
                        premium = calcPurePremiumMethod();
                        forWhile = false;
                    }
                    case 2 -> {
                        premium = calcLossRatioMethod();
                        forWhile = false;
                    }
                }
            }
            catch (InputException.InvalidMenuException e) {
                System.out.println(e.getMessage());
            }
        }
        return premium;
    }

    private int calcPurePremiumMethod() throws IOException, MyCloseSequence {
        boolean forWhile = true;
        int premium= -1;
        while(forWhile) {
            long damageAmount = 0, countContract = 0, businessExpense = 0;
            double profitMargin = 0;
            System.out.println("<< 순보험료법 산출 방식 >> (exit: 시스템 종료)");
            damageAmount = (long) br.verifyRead("발생손해액(단위:만원): ", damageAmount);
            countContract = (long) br.verifyRead("계약건수(건): ", countContract);
            businessExpense = (long) br.verifyRead("사업비(단위:만원): ", businessExpense);
            profitMargin = (Double) br.verifyRead("이익률(1-99%): ", profitMargin);
            premium = employee.calcPurePremiumMethod(damageAmount, countContract, businessExpense, profitMargin);
            System.out.printf("총보험료: %d(원)\n", premium);
            switch (br.verifyMenu("<< 산출된 보험료로 확정하시겠습니까? >>\n1.예 2. 아니오\n", 2)){
                case 1 -> forWhile = false;
            }
        }
        return premium;
    }

    private int calcLossRatioMethod() throws IOException, MyCloseSequence {
        boolean forWhile = true;
        Object[] premium = null;
        while(forWhile){
            double lossRatio = 0, expectedBusinessRatio = 0;
            int curTotalPremium = 0;
            System.out.println("<< 손해율법 산출 방식 >> (exit: 시스템 종료)");
            lossRatio = (Double) br.verifyRead("실제손해율(1-99%): ", lossRatio);
            expectedBusinessRatio = (Double) br.verifyRead("예정사업비율(1-99%): ", expectedBusinessRatio);
            curTotalPremium = (int) br.verifyRead("현재총보험료(원): ", curTotalPremium);
            premium = employee.calcLossRatioMethod(lossRatio, expectedBusinessRatio, curTotalPremium);
            System.out.println("요율조정값: "+(Math.round((double) premium[0]*1000)/1000.0)*100+"%");
            System.out.println("조정보험료: "+premium[1]+"원");
            System.out.println();
            switch (br.verifyMenu("<< 산출된 보험료로 확정하시겠습니까? >>\n1.예 2. 아니오\n", 2)){
                case 1 -> forWhile = false;
            }
        }
        return (int) premium[1];
    }

    private void formRegisterInsurance(Insurance insurance, int premium) throws IOException, MyCloseSequence {
        boolean forWhile = true;
        while(forWhile){
            switch (br.verifyMenu("<< 보험을 저장하시겠습니까? >>\n1. 예 2. 아니오\n", 2)){
                case 1 -> {
                    employee.registerInsurance(insuranceList, insurance, premium);
                    System.out.println("정상적으로 보험이 저장되었습니다!");
                    System.out.println("개발 보험 정보");
                    System.out.println(insurance.print()+"\n");
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
}
