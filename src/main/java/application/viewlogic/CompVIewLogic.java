package application.viewlogic;

import application.ViewLogic;

import static utility.MessageUtil.createMenu;


/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CompVIewLogic
 * author :  규현
 * date : 2022-05-10
 * description : ViewLogic의 구현클래스, 보상팀의 업무 목록과
 * 그것을 실행시키는 기능이 있다.
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class CompVIewLogic implements ViewLogic {
    @Override
    public void showMenu() {
       createMenu("보상팀 메뉴", "사고목록조회","손해조사","손해사정");
    }

    @Override
    public void work(String command) {

    }
}
