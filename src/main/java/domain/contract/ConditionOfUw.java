package domain.contract;

import domain.insurance.SalesAuthState;

/**
 * 작성자 : 김규현
 * 작성일 : 22/05/10
 * 내용 : 보험사 어플리케이션에 나타날 유저 타입에 대해서 작성.
 * 해당 값을 통해서 ViewLogic에 차이를 둠.
 */
public enum ConditionOfUw {
    APPROVAL("인수 승인"),
    RE_AUDIT("인수 보류"),
    REFUSE("인수 거절"),
    WAIT("인수 심사 대기");

    String name;
    ConditionOfUw(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

//    public static ConditionOfUw getConditionOf(String name) {
//        for (ConditionOfUw conditionOfUw : values()) {
//            if (conditionOfUw.getName().equals(name)) return conditionOfUw;
//        }
//        return null;
//    }
}
