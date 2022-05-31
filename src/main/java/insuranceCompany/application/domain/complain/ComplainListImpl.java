package insuranceCompany.application.domain.complain;

import insuranceCompany.application.global.exception.MyIllegalArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName :  domain.complain
 * fileName : ComplainListImpl
 * author :  규현
 * date : 2022-05-23
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-23                규현             최초 생성
 */
public class ComplainListImpl implements ComplainList{
    private static Map<Integer, Complain> complainlist = new HashMap<>();
    private static int id;

    @Override
    public List<Complain> readAllByCustomerId(int customerId) {
        return null;
    }

    @Override
    public void create(Complain complain) {
        complain.setId(++id);
        complainlist.put(complain.getId(), complain);
    }

    @Override
    public Complain read(int id) {
        Complain complain = complainlist.get(id);
        if (complain == null)
            throw new MyIllegalArgumentException(id + "에 해당하는 민원정보가 존재하지 않습니다.");
        return complain;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        Complain remove = complainlist.remove(id);
        if (remove == null)
            throw new MyIllegalArgumentException(id + "에 해당하는 민원정보가 존재하지 않습니다.");
        return true;
    }
}
