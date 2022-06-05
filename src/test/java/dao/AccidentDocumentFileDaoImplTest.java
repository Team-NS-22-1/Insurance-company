package dao;

import insuranceCompany.application.domain.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.domain.accident.accDocFile.AccidentDocumentFile;
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
class AccidentDocumentFileDaoImplTest {

    AccidentDocumentFileDaoImpl dao = new AccidentDocumentFileDaoImpl();

    @Test
    void create() {
        AccidentDocumentFile accidentDocumentFile = new AccidentDocumentFile();
        accidentDocumentFile.setFileAddress("testAddress");
        accidentDocumentFile.setType(AccDocType.CLAIMCOMP);
        accidentDocumentFile.setAccidentId(5);
        dao.create(accidentDocumentFile);

        dao = new AccidentDocumentFileDaoImpl();
        AccidentDocumentFile read = dao.read(accidentDocumentFile.getId());
        System.out.println(read);
        assertEquals(accidentDocumentFile.getId(),read.getId());
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
        AccidentDocumentFile accidentDocumentFile = new AccidentDocumentFile();
//        accDocFile.setFileAddress("testAddress");
        accidentDocumentFile.setType(AccDocType.CLAIMCOMP);
//        accDocFile.setAccidentId(22);
        dao.create(accidentDocumentFile);

        dao.update(accidentDocumentFile.getId());

        AccidentDocumentFile read = dao.read(accidentDocumentFile.getId());
        System.out.println(read);
        System.out.println(accidentDocumentFile.getLastModifedDate());
        System.out.println(read.getLastModifedDate());
        assertNotEquals(accidentDocumentFile.getLastModifedDate(),read.getLastModifedDate());
    }

    @Test
    void readAllByAccidentId() {
        List<AccidentDocumentFile> accidentDocumentFileList = dao.readAllByAccidentId(22);
        for (AccidentDocumentFile accidentDocumentFile : accidentDocumentFileList) {
            System.out.println(accidentDocumentFile);
        }
        assertEquals(accidentDocumentFileList.size(),3);
    }
}