package main.domain.viewUtils.viewlogic;

import main.domain.viewUtils.ViewLogic;

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
    @Override
    public void showMenu() {
        createMenu("고객메뉴", "보험가입", "보험료납입", "사고접수", "보상금청구", "기타등등");
    }

    @Override
    public void work(String command) {

    }
}
