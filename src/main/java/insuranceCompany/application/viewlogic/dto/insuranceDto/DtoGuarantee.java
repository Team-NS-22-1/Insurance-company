package insuranceCompany.application.viewlogic.dto.insuranceDto;

public class DtoGuarantee {

    private String name;

    private String description;

    private Long guaranteeAmount;

    public DtoGuarantee(String name, String description, Long guaranteeAmount) {
        this.name = name;
        this.description = description;
        this.guaranteeAmount = guaranteeAmount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getGuaranteeAmount() { return guaranteeAmount; }
}
