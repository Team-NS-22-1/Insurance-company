package insuranceCompany.application.viewlogic;

import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  insuranceCompany.application.viewlogic
 * fileName : CustomerViewLogicTest
 * author :  규현
 * date : 2022-06-01
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-01                규현             최초 생성
 */
class CustomerViewLogicTest {



    @BeforeEach
    void beforeEach() {

    }

    @Test
    void isValidateReportAccident() {
        assertEquals(isValidateReportAccident(AccidentType.CARACCIDENT,InsuranceType.CAR),true);
    }

    @Test
    void isValidateReportAccident_Exception() {
        assertFalse(isValidateReportAccident(AccidentType.CARACCIDENT,InsuranceType.HEALTH));
    }

    boolean isValidateReportAccident(AccidentType accidentType, InsuranceType insuranceType) {
        return switch (accidentType) {
            case CARACCIDENT, CARBREAKDOWN -> insuranceType == InsuranceType.CAR;
            case FIREACCIDENT -> insuranceType == InsuranceType.FIRE;
            case INJURYACCIDENT -> insuranceType == InsuranceType.HEALTH;
        };
    }

}