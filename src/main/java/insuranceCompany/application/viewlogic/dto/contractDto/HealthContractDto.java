package insuranceCompany.application.viewlogic.dto.contractDto;

public class HealthContractDto extends ContractDto {
    private int height;
    private int weight;
    private boolean isDrinking;
    private boolean isSmoking;
    private boolean isDriving;
    private boolean isDangerActivity;
    private boolean isHavingDisease;
    private boolean isTakingDrug;
    private String diseaseDetail;

    public HealthContractDto () {
    }

    public HealthContractDto (int height, int weight, boolean isDrinking, boolean isSmoking, boolean isDriving, boolean isDangerActivity,
                              boolean isHavingDisease, boolean isTakingDrug, String diseaseDetail){
        this.height = height;
        this.weight = weight;
        this.isDrinking = isDrinking;
        this.isSmoking = isSmoking;
        this.isDriving = isDriving;
        this.isDangerActivity = isDangerActivity;
        this.isHavingDisease = isHavingDisease;
        this.isTakingDrug = isTakingDrug;
        this.diseaseDetail =  diseaseDetail;
    }

    public int getHeight() {
        return height;
    }

    public HealthContractDto setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public HealthContractDto setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public boolean isDrinking() {
        return isDrinking;
    }

    public HealthContractDto setDrinking(boolean drinking) {
        isDrinking = drinking;
        return this;
    }

    public boolean isSmoking() {
        return isSmoking;
    }

    public HealthContractDto setSmoking(boolean smoking) {
        isSmoking = smoking;
        return this;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public HealthContractDto setDriving(boolean driving) {
        isDriving = driving;
        return this;
    }

    public boolean isDangerActivity() {
        return isDangerActivity;
    }

    public HealthContractDto setDangerActivity(boolean dangerActivity) {
        isDangerActivity = dangerActivity;
        return this;
    }

    public boolean isHavingDisease() {
        return isHavingDisease;
    }

    public HealthContractDto setHavingDisease(boolean havingDisease) {
        isHavingDisease = havingDisease;
        return this;
    }

    public boolean isTakingDrug() {
        return isTakingDrug;
    }

    public HealthContractDto setTakingDrug(boolean takingDrug) {
        isTakingDrug = takingDrug;
        return this;
    }

    public String getDiseaseDetail() {
        return diseaseDetail;
    }

    public HealthContractDto setDiseaseDetail(String diseaseDetail) {
        this.diseaseDetail = diseaseDetail;
        return this;
    }
}
