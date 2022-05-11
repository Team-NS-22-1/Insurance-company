package main;

import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.employee.Position;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestMain {

	public static void main(String[] args) throws IOException {
		EmployeeListImpl employeeList = new EmployeeListImpl();
		InsuranceListImpl insuranceList = new InsuranceListImpl();

		testEmployeeCrud(employeeList);
		testDevelopInsurance(employeeList, insuranceList);
	}

	static void testEmployeeCrud(EmployeeListImpl employeeList){
		//create
		Employee employee = new Employee();
		employee.setId(1)
				.setName("황승호")
				.setDepartment(Department.DEV)
				.setPhone("010-1234-5678")
				.setPosition(Position.DEPTMANAGER);
		System.out.println("CREATE: "+employeeList.create(employee));
		//read
		System.out.println("READ id(1): "+employeeList.read(1));
		//delete
//		System.out.println("DELETE id(1): "+employeeList.delete(1));
//		System.out.println("CHECK DELETE: "+employeeList.read(1));
	}

	static void testDevelopInsurance(EmployeeListImpl employeeList, InsuranceListImpl insuranceList) throws IOException {
		Employee employee = employeeList.read(1);
		//Develop Health Insurance
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
		Object[] defaultInfo = { name, description, paymentPeriod, contractPeriod };

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
		Object[] typeInfo = { targetAge, targetSex, riskCriterion };

		//  + while문을 이용하여 더 추가할지 사용자가 선택하여 반복수행 후 추가 안함 선택 시 탈출
		System.out.println("<< 보장 상세 내용 >>");
//		System.out.print("보장명: "); String gName = br.readLine();
//		System.out.print("보장 상세 내용: "); String gDescription = br.readLine();
		// test segment
		String gName = "건강보험 1 보장명 01";
		String gDescription = "건강보험 1 보장명 01의 설명입니다.";
		String[] guarantee = { gName, gDescription };
		System.out.println("보장명: "+gName+"\t보장 설명: "+gDescription);
		// test segment
		ArrayList<String[]> guaranteeListInfo = new ArrayList<>();
		guaranteeListInfo.add(guarantee);
		Insurance insurance = employee.develop(InsuranceType.HEALTH, defaultInfo, typeInfo, guaranteeListInfo);

		// 보험료 산출
		System.out.println("보험료를 산출하시겠습니까? 1.예 2.아니오 "); int inputCalc = Integer.parseInt(br.readLine());
		int premium = -1;
		if(inputCalc == 1)
			premium = testCalculatePremium(employee, insurance);
		else if(inputCalc == 2){
			System.out.println("!! 정말 취소하시겠습니까?\n1. 예\t2. 아니오");
			// ...
		}
		else {
			// ...
		}

		// 보험 저장
		System.out.println("<< 보험을 저장하시겠습니까? >>");
		System.out.println("1. 예\t2. 아니오");
		int inputSave = Integer.parseInt(br.readLine());
		if(inputSave == 1) {
			if(employee.registerInsurance(insuranceList, insurance, premium))
				System.out.println("정상적으로 보험이 저장되었습니다!");
			else System.out.println("보험 저장에 실패하였습니다!");
		}
		else if(inputSave == 2){
			System.out.println("!! 정말 취소하시겠습니까?\n1. 예\t2. 아니오");
			// ...
		}
		else{
			// ...
		}
		// test segment
		System.out.println("<< 직원 "+employee.getName()+"의 개발목록 >>");
		System.out.println(employee.readMyInsurance(insuranceList));
	}

	static int testCalculatePremium(Employee employee, Insurance insurance) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<< 보험료 산출 방식 선택>>");
		System.out.println("1. 순보험료법 산출");
		System.out.println("2. 손해율법 산출");
		int choiceCalcMethod = Integer.parseInt(br.readLine());
		int premium = -1;
		if(choiceCalcMethod == 1)
			premium = testCalcPurePremiumMethod(employee, insurance);
		else if(choiceCalcMethod == 2)
			premium = testCalcLossRatioMethod(employee, insurance);
		else {
			// ...
		}
		return premium;
	}

	static int testCalcPurePremiumMethod(Employee employee, Insurance insurance) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<< 순보험료법 산출 방식 >>");
		System.out.print("발생손해액: "); long damageAmount = Long.parseLong(br.readLine());
		System.out.print("계약건수: "); long countContract = Long.parseLong(br.readLine());
		System.out.print("사업비: "); long businessExpense = Long.parseLong(br.readLine());
		System.out.print("이익률: "); double profitMargin = Double.parseDouble(br.readLine());
		int premium = employee.calcPurePremiumMethod(insurance, damageAmount, countContract, businessExpense, profitMargin);
		System.out.printf("총보험료: %d(원)", premium);
		return premium;
	}

	static int testCalcLossRatioMethod(Employee employee, Insurance insurance) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<< 손해율법 산출 방식 >>");
		System.out.print("실제손해율(%): "); int lossRatio = Integer.parseInt(br.readLine());
		System.out.print("예정사업비율(%): "); int expectedBusinessRatio = Integer.parseInt(br.readLine());
		System.out.print("현재총보험료(원): "); int curTotalPremium = Integer.parseInt(br.readLine());
		Object[] premium = employee.calcLossRatioMethod(insurance, lossRatio, expectedBusinessRatio, curTotalPremium);
		System.out.println("요율조정값: "+(Math.round((double) premium[0]*1000)/1000.0)*100+"%");
		System.out.println("조정보험료: "+premium[1]+"원");
		return (int) premium[1];
	}

	static void testRegisterAuthInfo(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<< 판매 인가 등록 >>");
		// ... 보험 개발 리스트 출력 & 보험 선택

	}

}
