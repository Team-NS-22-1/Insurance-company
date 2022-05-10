package main.domain.viewUtils;

/**
 * 작성자 : 김규현
 * 작성일 : 22/05/10
 * 내용 : 사용자 별 보여주는 화면을 다르게 구성하기 위해서 제작.
 *        해당 interface를 구현한 객체들로 view와 logic을 구성한다.
 */
public interface ViewLogic {

    void showMenu();
    void work(String command);
}
