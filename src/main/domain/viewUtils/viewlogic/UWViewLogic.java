package main.domain.viewUtils.viewlogic;

import main.domain.viewUtils.ViewLogic;

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
        createMenu("언더라이팅팀메뉴", "인수심사한다");
    }

    @Override
    public void work(String command) {

    }
}
