package com.ssafy.zipcheck.listings.dto;

import lombok.Data;

@Data
public class ListingResponse {

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
    private Double excluUseAr;
    private String dealAmount;

    // 찜 여부
    private Boolean isFavorite;
}
