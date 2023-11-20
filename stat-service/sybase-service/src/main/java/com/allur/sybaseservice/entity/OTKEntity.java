package com.allur.sybaseservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTKEntity {
    private String WBDesign;
    private String nmId;
    private int countProduct;
    private int countRoute;
    private String size;
}
