package main.domain.viewUtils.viewlogic;

import main.domain.viewUtils.ViewLogic;

import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : UWViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class UWViewLogic implements ViewLogic {
    @Override
    public void showMenu() {
        createMenu("언더라이팅팀메뉴", "인수심사한다", "돌아간다");
    }

    @Override
    public void work(String command) {

        switch (command) {
            // 인수심사한다
            case "1":
                selectInsuranceType();
                selectCustomer();
                selectCommand();
                break;
            // 돌아간다
            case "2":
                break;

        }
    }

    public void selectInsuranceType() {
        createMenu("보험 선택","건강보험", "자동차보험", "화재보험");

        Scanner sc = new Scanner(System.in);
        String insuranceType = sc.next();

        switch (insuranceType) {

            case "1":
                System.out.println("건강보험 고객 리스트");
                break;
            case "2":
                System.out.println("자동차보험 고객 리스트");
                break;
            case "3":
                System.out.println("화재보험 고객 리스트");
                break;
        }

    }

    public void selectCustomer() {
        System.out.println("고객ID를 입력하세요.");

        Scanner sc = new Scanner(System.in);
        Long customerId = sc.nextLong();

        System.out.println("고객ID: " + customerId + "인 고객 정보");
    }

    public void selectCommand() {

    }




}
