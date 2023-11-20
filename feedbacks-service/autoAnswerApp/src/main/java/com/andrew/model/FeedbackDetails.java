package com.andrew.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackDetails {
    private String id;
    private String productValuation;
    private String productName;
    private String brandName;
    private String supplier;
}
