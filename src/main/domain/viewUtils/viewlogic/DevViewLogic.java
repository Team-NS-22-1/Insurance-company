package main.domain.viewUtils.viewlogic;

import main.domain.viewUtils.ViewLogic;

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
    @Override
    public void showMenu() {
        createMenu("개발팀메뉴", "보험개발", "보험료산출");
    }

    @Override
    public void work(String command) {

    }
}
