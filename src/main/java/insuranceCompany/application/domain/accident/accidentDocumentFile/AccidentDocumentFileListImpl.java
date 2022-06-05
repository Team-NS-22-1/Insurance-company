package insuranceCompany.application.domain.accident.accidentDocumentFile;

import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName :  main.domain.accident.accDocFile
 * fileName : AccDocFileListImpl
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class AccidentDocumentFileListImpl implements AccidentDocumentFileDao {

    private static Map<Integer, AccidentDocumentFile> accDocFilelist = new HashMap<>();
    private static int id;

    @Override
    public void create(AccidentDocumentFile accidentDocumentFile) {
        accidentDocumentFile.setId(++id);
        accDocFilelist.put(accidentDocumentFile.getId(), accidentDocumentFile);
    }

    @Override
    public AccidentDocumentFile read(int id) {
        AccidentDocumentFile accidentDocumentFile = accDocFilelist.get(id);
        if(accidentDocumentFile ==null)
            throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return accidentDocumentFile;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        AccidentDocumentFile accidentDocumentFile = accDocFilelist.remove(id);
        if(accidentDocumentFile ==null)
            throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return true;
    }
    @Override
    public List<AccidentDocumentFile> readAllByAccidentId(int accidentId) {
        List<AccidentDocumentFile> list = new ArrayList<>(accDocFilelist.values());
        List<AccidentDocumentFile> collect = list.stream().filter(ac -> ac.getAccidentId() == accidentId)
                .collect(Collectors.toList());
        if(collect.size()==0)
            throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return collect;
    }

}
