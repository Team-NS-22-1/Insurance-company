package application.viewlogic.dto.compDto;

import domain.accident.accDocFile.AccDocFile;
import domain.payment.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName :  application.viewlogic.dto
 * fileName : compDto
 * author :  규현
 * date : 2022-05-26
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-26                규현             최초 생성
 */
@Getter @Setter
public class AssessDamageResponseDto {

    private AccDocFile accDocFile;
    private Account account;

    @Builder
    public AssessDamageResponseDto(AccDocFile accDocFile, Account account) {
        this.accDocFile = accDocFile;
        this.account = account;
    }
}
