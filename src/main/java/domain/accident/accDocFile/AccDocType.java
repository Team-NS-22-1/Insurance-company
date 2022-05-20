package domain.accident.accDocFile;

/**
 * packageName :  domain.accident.accDocFile
 * fileName : AccDocType
 * author :  규현
 * date : 2022-05-20
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-20                규현             최초 생성
 */
public enum AccDocType {
    CLAIMCOMP("보험금 청구 서류"), MEDICALCERTIFICATION("진단서"), CONFIRMADMISSIONDISCHARGE("입퇴원 확인서"),
    PICTUREOFSITE("사고현장 사진"), REPAIRESTIMATE("수리비 견적서"), REPAIRRECEIPT("수리비 영수증"),
    CARACCIDENTFACTCONFIRMATION("교통사고 사실확인원"), PAYMENTRESOLUTION("자동차보험금 지급결의서");


    private String desc;
    private AccDocType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
