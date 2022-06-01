package dao;

import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.domain.accident.accDocFile.AccDocFile;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  dao
 * fileName : AccDocFileDaoTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class AccidentDocumentFileDaoTest {

    AccidentDocumentFileDao dao = new AccidentDocumentFileDao();

    @Test
    void create() {
        AccDocFile accDocFile = new AccDocFile();
        accDocFile.setFileAddress("testAddress");
        accDocFile.setType(AccDocType.CLAIMCOMP);
        accDocFile.setAccidentId(5);
        dao.create(accDocFile);

        dao = new AccidentDocumentFileDao();
        AccDocFile read = dao.read(accDocFile.getId());
        System.out.println(read);
        assertEquals(accDocFile.getId(),read.getId());
        assertNotNull(read.getFileAddress());
    }

    @Test
    void read() {
    }

    @Test
    void read_exception() {
        assertThrows(IllegalArgumentException.class, () -> dao.read(1010));
    }

    @Test
    void update() {
        AccDocFile accDocFile = new AccDocFile();
//        accDocFile.setFileAddress("testAddress");
        accDocFile.setType(AccDocType.CLAIMCOMP);
//        accDocFile.setAccidentId(22);
        dao.create(accDocFile);

        dao.update(accDocFile.getId());

        AccDocFile read = dao.read(accDocFile.getId());
        System.out.println(read);
        System.out.println(accDocFile.getLastModifedDate());
        System.out.println(read.getLastModifedDate());
        assertNotEquals(accDocFile.getLastModifedDate(),read.getLastModifedDate());
    }

    @Test
    void readAllByAccidentId() {
        List<AccDocFile> accDocFileList = dao.readAllByAccidentId(22);
        for (AccDocFile accDocFile : accDocFileList) {
            System.out.println(accDocFile);
        }
        assertEquals(accDocFileList.size(),3);
    }
}