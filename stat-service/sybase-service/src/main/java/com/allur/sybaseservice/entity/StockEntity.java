package com.allur.sybaseservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity {
    private String size;
    private String wbDesign;
    private String nmId;
    private int count;
}
