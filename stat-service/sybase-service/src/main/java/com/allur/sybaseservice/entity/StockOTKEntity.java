package com.allur.sybaseservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOTKEntity {
    private String nmId;
    private String size;
    private String wbDesign;
    private int countOTK;
    private int countStock;
    private int countRoute;
}
