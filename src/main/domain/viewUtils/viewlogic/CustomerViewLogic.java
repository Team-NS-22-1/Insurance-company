package main.domain.viewUtils.viewlogic;

import main.domain.contract.Contract;
import main.domain.contract.ContractList;
import main.domain.contract.ContractListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceList;
import main.domain.insurance.InsuranceListImpl;
import main.domain.payment.Payment;
import main.domain.viewUtils.ViewLogic;

import java.util.List;
import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CustomerViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class CustomerViewLogic implements ViewLogic {

    private ContractList contractList;
    private InsuranceList insuranceList;
    private Scanner sc;
    public CustomerViewLogic() {
        this.sc = new Scanner(System.in);
        this.contractList = new ContractListImpl();
        this.insuranceList = new InsuranceListImpl();
    }

    @Override
    public void showMenu() {
        createMenu("고객메뉴", "보험가입", "보험료납입", "사고접수", "보상금청구", "기타등등");
    }

    @Override
    public void work(String command) {

        switch (command) {
            case "2" :
                System.out.println("고객 ID 입력 : ");
                showContract(sc.nextInt());
                break;
        }

    }

    public void showContract(int customerId) {
        List<Contract> contracts = contractList.findAllByCustomerId(customerId);
        for (Contract contract : contracts) {

            StringBuilder sb = new StringBuilder();
            sb.append("ID:").append(contract.getId())
                    .append(" ").append("이름:").append(insuranceList.read(contract.getInsuranceId()).getName())
                    .append(" ").append("가격:").append(contract.getPremium()).append("\n");
            System.out.println(sb.toString());
        }
        int contractId = sc.nextInt();
        Contract contract = contractList.read(contractId);
        payLogic(contract);

    }

    private void payLogic(Contract contract) {
        if (contract.getPayment() == null) {
            setPaymentOnContract();
        }else{
            pay(contract);
        }
    }

    private void setPaymentOnContract() {

    }

    private void pay(Contract contract) {
        System.out.println(contract.getPremium()+ "원 결제하였습니다.");
    }

    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }


}
