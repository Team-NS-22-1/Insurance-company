package main.domain.viewUtils;

/**
 * 작성자 : 김규현
 * 작성일 : 22/05/10
 * 내용 : 사용자 별 보여주는 화면을 다르게 구성하기 위해서 제작.
 *        해당 interface를 구현한 객체들로 view와 logic을 구성한다.
 *        showMenu에는 보여줄 목록을 작성하고 work에는 application에서 받은 String 값을 통해
 *        이어질 로직을 작성한다.
 */
public interface ViewLogic {

    void showMenu();
    void work(String command);
}
