package com.ssafy.zipcheck.deals.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MapDealResponse {

    private int no;
    private String aptSeq;
    private String aptDong;
    private String floor;
    private int dealYear;
    private int dealMonth;
    private int dealDay;
    private BigDecimal excluUseAr;
    private String dealAmount;
    private String aptName;
    private String jibun;
    private String roadNm;
    private String buildYear;
    private String latitude;
    private String longitude;

    //  찜 여부 추가
    private Boolean isFavorite;
}
