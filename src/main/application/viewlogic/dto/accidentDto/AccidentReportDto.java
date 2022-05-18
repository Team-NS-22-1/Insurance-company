package main.application.viewlogic.dto.accidentDto;

import main.domain.accident.AccidentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName :  main.application.viewlogic.dto.accidentDto
 * fileName : AccidentReportDto
 * author :  규현
 * date : 2022-05-18
 * description : 공통된 사고 접수에 필요한 정보를 받는 클래스
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class AccidentReportDto {

    // 공통 정보
    private AccidentType accidentType;
    private int customerId;
    private LocalDateTime dateOfAccident;
    private LocalDateTime dateOfReport;

    // 차량, 화재 공통
    private String placeAddress;
    
    //차량 공통
    private String carNo;
    
    // 차 사고
    private String opposingDriverPhone;
    private boolean isRequestOnSite; // 현장 출동 요청

    // 차 고장
    private String symptom;
    
    //상해
    private String injurySite; // 부상 부위

    public AccidentType getAccidentType() {
        return accidentType;
    }

    public AccidentReportDto setAccidentType(AccidentType accidentType) {
        this.accidentType = accidentType;
        return this;
    }

    public int getCustomerId() {
        return customerId;
    }

    public AccidentReportDto setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public LocalDateTime getDateOfAccident() {
        return dateOfAccident;
    }

    public AccidentReportDto setDateOfAccident(LocalDateTime dateOfAccident) {
        this.dateOfAccident = dateOfAccident;
        return this;
    }

    public LocalDateTime getDateOfReport() {
        return dateOfReport;
    }

    public AccidentReportDto setDateOfReport(LocalDateTime dateOfReport) {
        this.dateOfReport = dateOfReport;
        return this;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public AccidentReportDto setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
        return this;
    }

    public String getCarNo() {
        return carNo;
    }

    public AccidentReportDto setCarNo(String carNo) {
        this.carNo = carNo;
        return this;
    }

    public String getOpposingDriverPhone() {
        return opposingDriverPhone;
    }

    public AccidentReportDto setOpposingDriverPhone(String opposingDriverPhone) {
        this.opposingDriverPhone = opposingDriverPhone;
        return this;
    }

    public boolean isRequestOnSite() {
        return isRequestOnSite;
    }

    public AccidentReportDto setRequestOnSite(boolean requestOnSite) {
        isRequestOnSite = requestOnSite;
        return this;
    }

    public String getSymptom() {
        return symptom;
    }

    public AccidentReportDto setSymptom(String symptom) {
        this.symptom = symptom;
        return this;
    }

    public String getInjurySite() {
        return injurySite;
    }

    public AccidentReportDto setInjurySite(String injurySite) {
        this.injurySite = injurySite;
        return this;
    }
}
