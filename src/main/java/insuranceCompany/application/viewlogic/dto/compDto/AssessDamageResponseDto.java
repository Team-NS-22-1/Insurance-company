package insuranceCompany.application.viewlogic.dto.compDto;

import insuranceCompany.application.domain.accident.accidentDocumentFile.AccidentDocumentFile;
import insuranceCompany.application.domain.customer.payment.Account;
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

    private AccidentDocumentFile accidentDocumentFile;
    private Account account;

    @Builder
    public AssessDamageResponseDto(AccidentDocumentFile accidentDocumentFile, Account account) {
        this.accidentDocumentFile = accidentDocumentFile;
        this.account = account;
    }
}
