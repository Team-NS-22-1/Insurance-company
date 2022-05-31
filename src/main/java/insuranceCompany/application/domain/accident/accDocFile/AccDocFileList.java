package insuranceCompany.application.domain.accident.accDocFile;

import insuranceCompany.application.dao.CrudInterface;

import java.util.List;

/**
 * packageName :  main.domain.accident.accDocFile
 * fileName : AccDocFileList
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public interface AccDocFileList extends CrudInterface<AccDocFile> {

    List<AccDocFile> readAllByAccidentId(int accidentId);
}
