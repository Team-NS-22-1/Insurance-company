package application.viewlogic.dto.compDto;

import domain.accident.AccidentType;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName :  application.viewlogic.dto.compDto
 * fileName : InvestigateDamageRequestDto
 * author :  규현
 * date : 2022-05-27
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-27                규현             최초 생성
 */
@Getter
@Setter
public class InvestigateDamageRequestDto {

    private int errorRate;
    private long lossReserves;
    private AccidentType accidentType;

}
