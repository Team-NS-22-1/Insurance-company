package dao;

import insuranceCompany.application.dao.accident.ComplainDaoImpl;
import insuranceCompany.application.domain.accident.complain.Complain;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  dao
 * fileName : ComplainDaoTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class ComplainDaoImplTest {

    ComplainDaoImpl dao = new ComplainDaoImpl();

    @Test
    void create() {
        Complain complain = Complain.builder().reason("시발 이유없음")
                .customerId(19).build();
        dao.create(complain);

        Complain read = dao.read(complain.getId());

        System.out.println(read);
        assertEquals(complain.getId(),read.getId());
    }

    @Test
    void read() {

        Complain read = dao.read(2);
        System.out.println(read);
        assertEquals(2,read.getId());

    }
    @Test
    void read_exception() {
        assertThrows(MyIllegalArgumentException.class, () -> dao.read(10));
    }

    @Test
    void readAllByCustomerId() {
        List<Complain> complains = dao.readAllByCustomerId(19);
        for (Complain complain : complains) {
            System.out.println(complain);
        }
        assertEquals(complains.size(),5);
    }
}