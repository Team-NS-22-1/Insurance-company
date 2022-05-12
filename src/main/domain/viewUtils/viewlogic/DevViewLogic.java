package main.domain.viewUtils.viewlogic;

import main.domain.contract.BuildingType;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.employee.Position;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.domain.insurance.inputDto.*;
import main.domain.viewUtils.ViewLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static main.domain.utility.MessageUtil.createMenu;

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
    private EmployeeListImpl employeeList = new EmployeeListImpl();
    private InsuranceListImpl insuranceList = new InsuranceListImpl();
    private Employee employee = new Employee();

    public DevViewLogic(){
        employee.setId(1)
                .setName("황승호")
                .setDepartment(Department.DEV)
                .setPhone("010-1234-5678")
                .setPosition(Position.DEPTMANAGER);
        employeeList.create(employee);
    }

    @Override
    public void showMenu() {
        createMenu("개발팀 메뉴", "보험 개발", "판매인가 등록");
    }

    @Override
    public void work(String command) {
        try {
            switch (command){
                case "1" -> this.menuDevelop(this.menuInsuranceType());
                case "2" -> {}
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private InsuranceType menuInsuranceType() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        createMenu("<< 보험 종류 선택 >>", "건강보험", "자동차보험", "화재보험");
        int insType = Integer.parseInt(br.readLine());
        return switch (insType){
            case 1 -> InsuranceType.HEALTH;
            case 2 -> InsuranceType.CAR;
            case 3 -> InsuranceType.FIRE;
            default -> null;
        };
    }

    private void menuDevelop(InsuranceType type) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Insurance insurance = employeeList.read(1).develop(
                formInputDefaultInfo(br),
                // return Basic Info of Insurance
                formInputTypeInfo(br, type),
                // return Type Info of Insurance (Type is Health, Car, Fire)
                formInputGuaranteeInfo(br)
                // return Guarantee Info of Insurance (Guarantee Info is list)
        );

        int premium = formCalculatePremium(br, isCalcPremium(br), insurance);
        if(premium < 0) return;

        formRegisterInsurance(br, insurance, premium);

        System.out.println(employee.readMyInsurance(insuranceList));
    }

    private void formRegisterInsurance(BufferedReader br, Insurance insurance, int premium) throws IOException {
        boolean forWhile = true;
        while(forWhile){
            System.out.println("<< 보험을 저장하시겠습니까? >>");
            System.out.println("1. 예\t2. 아니오");
            switch (Integer.parseInt(br.readLine())){
                case 1 -> {
                    if(employee.registerInsurance(insuranceList, insurance, premium))
                        System.out.println("정상적으로 보험이 저장되었습니다!");
                    else System.out.println("보험 저장에 실패하였습니다!");
                    forWhile = false;
                }
                case 2 -> {
                    System.out.println("정말 취소하시겠습니까?\n1. 예\t2. 아니오");
                    int isCancel = Integer.parseInt(br.readLine());
                    if(isCancel == 1)
                        forWhile = false;
                }
            }
        }
    }

    private DtoBasicInfo formInputDefaultInfo(BufferedReader br) {
        System.out.println("<< 보험 기본 정보 >>");
//		System.out.print("보험 이름: "); String name = br.readLine();
//		System.out.print("보험 설명: "); String description = br.readLine();
//		System.out.print("납입 기간(년): "); int paymentPeriod = Integer.parseInt(br.readLine());
//		System.out.print("만기 나이(세): "); int contractPeriod = Integer.parseInt(br.readLine());
        // test segment
        String name = "건강보험 1";
        String description = "건강보험 1의 설명입니다.";
        int paymentPeriod = 30;
        int contractPeriod = 80;
        System.out.println("보험이름: "+name+"\t보험설명: "+description+"\t납입기간: "+paymentPeriod+"\t만기나이: "+contractPeriod);
        // test segment
        return new DtoBasicInfo(name, description, paymentPeriod, contractPeriod);
    }

    private DtoTypeInfo formInputTypeInfo(BufferedReader br, InsuranceType type) throws IOException {
        if(type == InsuranceType.HEALTH){
            System.out.println("<< 건강 보험 정보 >>");
    //		System.out.print("보험 대상 나이: "); int targetAge = Integer.parseInt(br.readLine());
    //		System.out.print("보험 대상 성별(1.남자 2.여자): "); int sex = Integer.parseInt(br.readLine());
    //		System.out.print("위험부담 기준(개): "); int riskCriterion = Integer.parseInt(br.readLine());
    //		boolean targetSex = false;
    //		if(sex == 1) targetSex = true;
    //		else if(sex == 2) targetSex = false;
            // test segment
            int targetAge = 20;
            boolean targetSex = true;
            int riskCriterion = 4;
            System.out.println("대상나이: "+targetAge+"\t대상성별: "+targetSex+"\t위험부담 기준: "+riskCriterion);
            // test segment
            return new DtoHealth(targetAge, targetSex, riskCriterion);
        }
        else if(type == InsuranceType.CAR){
            System.out.println("<< 자동차 보험 정보 >>");
            //		System.out.print("보험 대상 나이: "); int targetAge = Integer.parseInt(br.readLine());
            //		System.out.print("차량가액 기준(원): "); long valueCriterion = (br.readLine());
            // test segment
            int targetAge = 20;
            long valueCriterion = 20000000;
            System.out.println("대상나이: "+targetAge+"\t차량가액 기준: "+valueCriterion);
            // test segment
            return new DtoCar(targetAge, valueCriterion);
        }
        else if(type == InsuranceType.FIRE){
            System.out.println("<< 화재 보험 정보 >>");
//            System.out.println("건물종류 선택: 1. 상업용 2. 산업용 3. 기관용 4. 거주용"); int selectType = Integer.parseInt(br.readLine());
//            BuildingType buildingType = null;
//            switch (selectType){
//                case 1 -> buildingType = BuildingType.COMMERCIAL;
//                case 2 -> buildingType = BuildingType.INDUSTRIAL;
//                case 3 -> buildingType = BuildingType.INSTITUTIONAL;
//                case 4 -> buildingType = BuildingType.RESIDENTIAL;
//                default -> System.out.println("잘못된 메뉴 선택");
//            }
//            System.out.print("담보금액: "); long collateralAmount = Long.parseLong(br.readLine());
            // test segment
            BuildingType buildingType = BuildingType.COMMERCIAL;
            long collateralAmount = 300000000;
            System.out.println("건물 종류: "+buildingType+"\t담보금액: "+collateralAmount);
            // test segment
            return new DtoFire(buildingType, collateralAmount);
        }
        return null;
    }

    private ArrayList<DtoGuarantee> formInputGuaranteeInfo(BufferedReader br){
        System.out.println("<< 보장 상세 내용 >>");
//		System.out.print("보장명: "); String gName = br.readLine();
//		System.out.print("보장 상세 내용: "); String gDescription = br.readLine();
        // test segment
        String gName = "보장명 01";
        String gDescription = "보장명 01의 설명입니다.";
        System.out.println("보장명: "+gName+"\t보장 설명: "+gDescription);
        // test segment
        ArrayList<DtoGuarantee> guaranteeListInfo = new ArrayList<>();
        guaranteeListInfo.add(new DtoGuarantee(gName, gDescription));
        return guaranteeListInfo;
    }

    private boolean isCalcPremium(BufferedReader br) throws IOException {
        boolean isCalcPremium = false;
        boolean forWhile = true;
        while(forWhile){
            System.out.println("보험료를 산출하시겠습니까? 1.예 2.아니오 "); int inputCalc = Integer.parseInt(br.readLine());
            if(inputCalc == 1) {
                isCalcPremium = true;
                forWhile = false;
            }
            else if(inputCalc == 2){
                System.out.println("정말 취소하시겠습니까?\n1. 예\t2. 아니오");
                int isCancel = Integer.parseInt(br.readLine());
                if(isCancel == 1)
                    forWhile = false;
            }
            else {
                System.out.println("잘못된 메뉴 선택");
            }
        }
        return isCalcPremium;
    }

    private int formCalculatePremium(BufferedReader br, boolean isCalcPremium, Insurance insurance) throws IOException {
        int premium = -1;

        if(!isCalcPremium) return premium;

        boolean forWhile = true;
        while(forWhile){
            System.out.println("<< 보험료 산출 방식 선택>>");
            System.out.println("1. 순보험료법 산출");
            System.out.println("2. 손해율법 산출");
            int choiceCalcMethod = Integer.parseInt(br.readLine());
            if(choiceCalcMethod == 1){
                premium = calcPurePremiumMethod(br, insurance);
                forWhile = false;
            }
            else if(choiceCalcMethod == 2){
                premium = calcLossRatioMethod(br, insurance);
                forWhile = false;
            }
            else {
                System.out.println("잘못된 메뉴 선택");
            }
        }
        return premium;
    }

    private int calcPurePremiumMethod(BufferedReader br, Insurance insurance) throws IOException {
        System.out.println("<< 순보험료법 산출 방식 >>");
        System.out.print("발생손해액: "); long damageAmount = Long.parseLong(br.readLine());
        System.out.print("계약건수: "); long countContract = Long.parseLong(br.readLine());
        System.out.print("사업비: "); long businessExpense = Long.parseLong(br.readLine());
        System.out.print("이익률: "); double profitMargin = Double.parseDouble(br.readLine());
        int premium = employee.calcPurePremiumMethod(insurance, damageAmount, countContract, businessExpense, profitMargin);
        System.out.printf("총보험료: %d(원)", premium);
        return premium;
    }

    private int calcLossRatioMethod(BufferedReader br, Insurance insurance) throws IOException {
        System.out.println("<< 손해율법 산출 방식 >>");
        System.out.print("실제손해율(%): "); int lossRatio = Integer.parseInt(br.readLine());
        System.out.print("예정사업비율(%): "); int expectedBusinessRatio = Integer.parseInt(br.readLine());
        System.out.print("현재총보험료(원): "); int curTotalPremium = Integer.parseInt(br.readLine());
        Object[] premium = employee.calcLossRatioMethod(insurance, lossRatio, expectedBusinessRatio, curTotalPremium);
        System.out.println("요율조정값: "+(Math.round((double) premium[0]*1000)/1000.0)*100+"%");
        System.out.println("조정보험료: "+premium[1]+"원");
        return (int) premium[1];
    }
}
