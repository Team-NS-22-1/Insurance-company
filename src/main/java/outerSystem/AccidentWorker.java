package outerSystem;

/**
 * packageName :  main.outerSystem
 * fileName : AccidentServer
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class AccidentWorker {

    private String name;
    private String phoneNum;

    public AccidentWorker() {
    }

    public AccidentWorker(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public AccidentWorker setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public AccidentWorker setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    @Override
    public String toString() {
        return "\n<<출동업체직원정보>>\n"+"이름 : " + name + "\n연락처 : " + phoneNum+"\n";
    }
}
