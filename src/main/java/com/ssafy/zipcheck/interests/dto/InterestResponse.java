package com.ssafy.zipcheck.interests.dto;

import lombok.Data;

@Data
public class InterestResponse {
    // From interests table
    private Long interestId;

    // From houseinfos table
    private String aptSeq;
    private String aptName;
    private String jibun;
    private String roadNm;
    private Integer buildYear;
    private Double latitude;
    private Double longitude;

    // From housedeals table
    private Long no; // deal number
    private String aptDong;
    private String floor;
    private Integer dealYear;
    private Integer dealMonth;
    private Integer dealDay;
    private Double excluUseAr;
    private String dealAmount;
}
