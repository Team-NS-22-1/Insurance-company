package main.viewUtils.viewlogic;

import main.viewUtils.ViewLogic;

import static main.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : SalesViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class SalesViewLogic implements ViewLogic {
    @Override
    public void showMenu() {
        createMenu("영업팀 메뉴", "보험상품설계");
    }

    @Override
    public void work(String command) {

    }
}
