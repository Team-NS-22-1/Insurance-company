package main.domain.accident.accDocFile;

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
public class AccDocFileListImpl implements AccDocFileList{

    private static Map<Integer, AccDocFile> accDocFilelist = new HashMap<>();
    private static int id;

    @Override
    public void create(AccDocFile accDocFile) {
        accDocFile.setId(++id);
        accDocFilelist.put(accDocFile.getId(), accDocFile);
    }

    @Override
    public AccDocFile read(int id) {
        AccDocFile accDocFile = accDocFilelist.get(id);
        if(accDocFile==null)
            throw new IllegalArgumentException(id+"에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return accDocFile;
    }

    @Override
    public boolean delete(int id) {
        AccDocFile accDocFile = accDocFilelist.remove(id);
        if(accDocFile==null)
            throw new IllegalArgumentException(id+"에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return true;
    }
    @Override
    public List<AccDocFile> readAllByAccidentId(int accidentId) {
        List<AccDocFile> list = new ArrayList<>(accDocFilelist.values());
        List<AccDocFile> collect = list.stream().filter(ac -> ac.getAccidentId() == accidentId)
                .collect(Collectors.toList());
        if(collect.size()==0)
            throw new IllegalArgumentException(accidentId+"에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return collect;
    }

}
