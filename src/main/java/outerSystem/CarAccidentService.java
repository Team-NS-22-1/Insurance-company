package outerSystem;

import domain.accident.Accident;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName :  main.outerSystem
 * fileName : CarAccidentService
 * author :  규현
 * date : 2022-05-18
 * description : 현장 출동 업체
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class CarAccidentService {

    private static List<AccidentWorker> servers = new ArrayList<>();

    private static void init() {
        servers.add(new AccidentWorker("직원1","010-1111-1111"));
        servers.add(new AccidentWorker("직원2","010-1221-1111"));
        servers.add(new AccidentWorker("직원3","010-1133-1111"));
        servers.add(new AccidentWorker("직원4","010-1111-4411"));
    }

    public static void connectWorker() {
        init();
        int index = (int) (Math.random()*servers.size());
        AccidentWorker accidentWorker = servers.get(index);
        System.out.println(accidentWorker);

    }

}
