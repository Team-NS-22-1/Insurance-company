package insuranceCompany.application.viewlogic.dto.contractDto;

import insuranceCompany.application.domain.contract.ConditionOfUw;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.insurance.InsuranceType;
import lombok.Data;

/**
 * packageName :  insuranceCompany.application.viewlogic.dto.contractDto
 * fileName : ContractwithTypeDto
 * author :  규현
 * date : 2022-06-01
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-01                규현             최초 생성
 */
@Data
public class ContractwithTypeDto {

    private ConditionOfUw conditionOfUw;
    private int customerId;
    private int employeeId;
    private int id;
    private int insuranceId;
    private boolean isPublishStock;
    private int paymentId;
    private int premium;
    private String reasonOfUw;
    private InsuranceType insuranceType;

    public ContractwithTypeDto(Contract contract) {
        this.id = contract.getId();
        this.customerId = contract.getId();
        this.employeeId = contract.getEmployeeId();
        premium = contract.getPremium();
        reasonOfUw = contract.getReasonOfUw();
        paymentId = contract.getPaymentId();
        isPublishStock = contract.isPublishStock();
        conditionOfUw = contract.getConditionOfUw();
        insuranceId = contract.getInsuranceId();
    }
}
