package application.viewlogic.dto.compDto;

import domain.payment.BankType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName :  application.viewlogic.dto.compDto
 * fileName : AccountRequestDto
 * author :  규현
 * date : 2022-05-27
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-27                규현             최초 생성
 */
@Getter @Setter
public class AccountRequestDto {
    private BankType bankType;
    private String accountNo;

    @Builder
    public AccountRequestDto(BankType bankType, String accountNo) {
        this.bankType = bankType;
        this.accountNo = accountNo;
    }
}
