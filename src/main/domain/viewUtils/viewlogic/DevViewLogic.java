package main.domain.viewUtils.viewlogic;

import main.domain.contract.BuildingType;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.domain.insurance.inputDto.*;
import main.domain.utility.MyBufferedReader;
import main.exception.InputException;
import main.domain.viewUtils.ViewLogic;
import main.login.Login;

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
    private EmployeeListImpl employeeList;

    private InsuranceListImpl insuranceList;

    private Employee employee = new Employee();

    // Test Login
    private Login login;

    {
        try {
            login = new Login();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DevViewLogic() {
    }

    @Override
    public void showMenu() {
        createMenu("개발팀 메뉴", "보험 개발", "판매인가 등록", "로그인");
    }

    @Override
    public void work(String command) {
        try {
            switch (command){
                case "1" -> this.menuDevelop(this.menuInsuranceType());
                case "2" -> {}
                case "3" -> login.loginMenu();
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private InsuranceType menuInsuranceType() throws IOException {
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(System.in));
        boolean forWhile = true;
        int insType = 0;
        while(forWhile){
            createMenu("<< 보험 종류 선택 >>", "건강보험", "자동차보험", "화재보험");
            try {
                insType = br.verifyMenu(3);
                forWhile = false;
            }
            catch (InputException.InvalidMenuException |
                   InputException.InputNullDataException e){
                System.out.println(e.getMessage());
            }
        }
        return switch (insType){
            case 1 -> InsuranceType.HEALTH;
            case 2 -> InsuranceType.CAR;
            case 3 -> InsuranceType.FIRE;
            default -> null;
        };
    }

    private void menuDevelop(InsuranceType type) throws IOException {
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(System.in));

        Insurance insurance = employeeList.read(1).develop(
                formInputBasicInfo(br), // return Basic Info of Insurance
                formInputTypeInfo(br, type), // return Type Info of Insurance (Type is Health, Car, Fire)
                formInputGuaranteeInfo(br) // return Guarantee Info of Insurance (Guarantee Info is list)
        );

        int premium = formCalculatePremium(br, isCalcPremium(br));
        if(premium < 0) return;

        formRegisterInsurance(br, insurance, premium);

        System.out.println(employee.readMyInsurance(insuranceList));
    }

    private DtoBasicInfo formInputBasicInfo(MyBufferedReader br) throws IOException{
        boolean forWhile = true;
        String name = "", description = "";
        int paymentPeriod = 0, contractPeriod = 0;
        while(forWhile){
            System.out.println("<< 보험 기본 정보 >>");
            try{
                System.out.print("보험 이름: "); name = (String) br.verifyRead(name);
                System.out.print("보험 설명: "); description = (String) br.verifyRead(description);
                System.out.print("납입 기간(년): "); paymentPeriod = (int) br.verifyRead(paymentPeriod);
                System.out.print("만기 나이(세): "); contractPeriod = (int) br.verifyRead(contractPeriod);
                forWhile = false;
            }
            catch (InputException e){
                System.out.println(e.getMessage());
            }
//            catch (TestInputException.ExitMenuException e){
//                System.out.println(e.getMessage());
//                // ... 이게 프로그램 종료냐? 아니면 메뉴 종료냐? 결정 바람 
//                // ... 프로그램 종료면 throws, 메뉴 종료면 catch
//            }
        }
        System.out.println("보험이름: "+name+"\t보험설명: "+description+"\t\t납입기간: "+paymentPeriod+"\t만기나이: "+contractPeriod);
        return new DtoBasicInfo(name, description, paymentPeriod, contractPeriod);
    }

    private DtoTypeInfo formInputTypeInfo(MyBufferedReader br, InsuranceType type) throws IOException {
        boolean forWhile = true;
        if(type == InsuranceType.HEALTH){
            int targetAge = 0, sex = 0, riskCriterion = -1;
            boolean targetSex = false;
            while(forWhile){
                try {
                    System.out.println("<< 건강 보험 정보 >>");
                    System.out.print("보험 대상 나이: "); targetAge = (int) br.verifyRead(targetAge);
                    System.out.print("보험 대상 성별 (1.남자 2.여자): "); sex = br.verifyMenu(2);
                    System.out.print("위험부담 기준(개): "); riskCriterion = (int) br.verifyRead(riskCriterion);
                    if(sex == 1) targetSex = true;
                    forWhile = false;
                }
                catch (InputException.InputNullDataException |
                        InputException.InputInvalidDataException |
                        InputException.InvalidMenuException e){
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("대상나이: "+targetAge+"\t대상성별: "+targetSex+"\t위험부담 기준: "+riskCriterion);
            return new DtoHealth(targetAge, targetSex, riskCriterion);
        }
        else if(type == InsuranceType.CAR){
            int targetAge = 0;
            long valueCriterion = -1;
            while(forWhile){
                try {
                    System.out.println("<< 자동차 보험 정보 >>");
                    System.out.print("보험 대상 나이: "); targetAge = (int) br.verifyRead(targetAge);
                    System.out.print("차량가액 기준(원): "); valueCriterion = (long) br.verifyRead(valueCriterion);
                    forWhile = false;
                }
                catch (InputException.InputNullDataException |
                       InputException.InputInvalidDataException e){
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("대상나이: "+targetAge+"\t차량가액 기준: "+valueCriterion);
            return new DtoCar(targetAge, valueCriterion);
        }
        else if(type == InsuranceType.FIRE){
            BuildingType buildingType = null;
            long collateralAmount = -1;
            while(forWhile){
                try {
                    System.out.println("<< 화재 보험 정보 >>");
                    System.out.println("건물종류 선택: 1. 상업용 2. 산업용 3. 기관용 4. 거주용");
                    switch (br.verifyMenu(4)){
                        case 1 -> buildingType = BuildingType.COMMERCIAL;
                        case 2 -> buildingType = BuildingType.INDUSTRIAL;
                        case 3 -> buildingType = BuildingType.INSTITUTIONAL;
                        case 4 -> buildingType = BuildingType.RESIDENTIAL;
                    }
                    System.out.print("담보금액: "); collateralAmount = (long) br.verifyRead(collateralAmount);
                    forWhile = false;
                }
                catch (InputException.InputNullDataException |
                       InputException.InputInvalidDataException |
                       InputException.InvalidMenuException e){
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("건물 종류: "+buildingType+"\t담보금액: "+collateralAmount);
            return new DtoFire(buildingType, collateralAmount);
        }
        return null;
    }

    private ArrayList<DtoGuarantee> formInputGuaranteeInfo(MyBufferedReader br) throws IOException{
        // 계속 추가할건지 분기
        // 오류 시 반복
        boolean isAdd = true, forWhile = true;
        ArrayList<DtoGuarantee> guaranteeListInfo = new ArrayList<>();
        while(forWhile){
                try {
                    String gName = "", gDescription = "";
                    while(isAdd) {
                            System.out.println("<< 보장 상세 내용 >>");
                            System.out.print("보장명: "); gName = (String) br.verifyRead(gName);
                            System.out.print("보장 상세 내용: "); gDescription = (String) br.verifyRead(gDescription);
                            System.out.println("보장명: "+gName+"\t보장 설명: "+gDescription);
                            guaranteeListInfo.add(new DtoGuarantee(gName, gDescription));

                            // 아래 메뉴에서 캐치되면 다시 이쪽으로 돌아오게끔 해라
                            System.out.println("보장을 더 추가하시겠습니까? 1. 예 2. 아니오");
                            try{
                                if(br.verifyMenu(2) == 2)
                                    isAdd = false;
                            }
                            catch (InputException.InvalidMenuException e){
                                System.out.println(e.getMessage());
                            }
                        }
                    forWhile = false;
                }
                catch (InputException.InputNullDataException |
                       InputException.InputInvalidDataException e){
                    System.out.println(e.getMessage());
                }
        }
        return guaranteeListInfo;
    }

    private boolean isCalcPremium(MyBufferedReader br) throws IOException {
        boolean isCalcPremium = false;
        boolean forWhile = true;
        while(forWhile) {
            System.out.println("보험료를 산출하시겠습니까? 1.예 2.아니오 ");
            try {
                switch (br.verifyMenu(2)) {
                    case 1 -> {
                        isCalcPremium = true;
                        forWhile = false;
                    }
                    case 2 -> {
                        System.out.println("정말 취소하시겠습니까?\n1. 예\t2. 아니오");
                        switch (br.verifyMenu(2)) {
                            case 1 -> forWhile = false;
                        }
                    }
                }
            }
            catch (InputException.InvalidMenuException e){
                System.out.println(e.getMessage());
            }
        }
        return isCalcPremium;
    }

    private int formCalculatePremium(MyBufferedReader br, boolean isCalcPremium) throws IOException {
        int premium = -1;

        if(!isCalcPremium) return premium;

        boolean forWhile = true;
        while(forWhile){
            try {
                System.out.println("<< 보험료 산출 방식 선택>>");
                System.out.println("1. 순보험료법 산출");
                System.out.println("2. 손해율법 산출");
                switch (br.verifyMenu(2)){
                    case 1 -> {
                        premium = calcPurePremiumMethod(br);
                        forWhile = false;
                    }
                    case 2 -> {
                        premium = calcLossRatioMethod(br);
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

    private int calcPurePremiumMethod(MyBufferedReader br) throws IOException {
        boolean forWhile = true;
        int premium= -1;
        while(forWhile) {
            long damageAmount = 0, countContract = 0, businessExpense = 0;
            double profitMargin = 0.0;
            try {
                System.out.println("<< 순보험료법 산출 방식 >>");
                System.out.print("발생손해액: "); damageAmount = (long) br.verifyRead(damageAmount);
                System.out.print("계약건수: "); countContract = (long) br.verifyRead(countContract);
                System.out.print("사업비: "); businessExpense = (long) br.verifyRead(businessExpense);
                System.out.print("이익률: "); profitMargin = (double) br.verifyRead(profitMargin);
                premium = employee.calcPurePremiumMethod(damageAmount, countContract, businessExpense, profitMargin);
                System.out.printf("총보험료: %d(원)", premium);
                forWhile = false;
            }
            catch (InputException.InputNullDataException |
                    InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }
        return premium;
    }

    private int calcLossRatioMethod(MyBufferedReader br) throws IOException {
        boolean forWhile = true;
        Object[] premium = null;
        while(forWhile){
            int lossRatio = 0, expectedBusinessRatio = 0, curTotalPremium = 0;
            try {
                System.out.println("<< 손해율법 산출 방식 >>");
                System.out.print("실제손해율(%): "); lossRatio = (int) br.verifyRead(lossRatio);
                System.out.print("예정사업비율(%): "); expectedBusinessRatio = (int) br.verifyRead(expectedBusinessRatio);
                System.out.print("현재총보험료(원): "); curTotalPremium = (int) br.verifyRead(curTotalPremium);
                premium = employee.calcLossRatioMethod(lossRatio, expectedBusinessRatio, curTotalPremium);
                System.out.println("요율조정값: "+(Math.round((double) premium[0]*1000)/1000.0)*100+"%");
                System.out.println("조정보험료: "+premium[1]+"원");
                forWhile = false;
            }
            catch (InputException.InputNullDataException |
                   InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }
        return (int) premium[1];
    }


    private void formRegisterInsurance(MyBufferedReader br, Insurance insurance, int premium) throws IOException {
        boolean forWhile = true;
        while(forWhile){
            try {
                System.out.println("<< 보험을 저장하시겠습니까? >>");
                System.out.println("1. 예\t2. 아니오");
                switch (br.verifyMenu(2)){
                    case 1 -> {
                        employee.registerInsurance(insuranceList, insurance, premium);
                        System.out.println("정상적으로 보험이 저장되었습니다!");
                        forWhile = false;
                    }
                    case 2 -> {
                        System.out.println("정말 취소하시겠습니까?\n1. 예\t2. 아니오");
                        switch (br.verifyMenu(2)){
                            case 1 -> forWhile = false;
                        }
                    }
                }
            }
            catch (InputException.InvalidMenuException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
