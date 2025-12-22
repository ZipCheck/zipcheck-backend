package com.ssafy.zipcheck.interests.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterestResponse {

    private Integer interestId;
    private Integer dealNo;

    // houseinfos
    private String aptSeq;
    private String aptName;
    private String jibun;
    private String roadNm;
    private Integer buildYear;
    private Double latitude;
    private Double longitude;

    // housedeals
    private String aptDong;
    private String floor;
    private Integer dealYear;
    private Integer dealMonth;
    private Integer dealDay;
    private Double excluUseAr;
    private String dealAmount;

    // UI 확장
    private LocalDateTime createdAt;
    private Boolean hasSticker;
}
