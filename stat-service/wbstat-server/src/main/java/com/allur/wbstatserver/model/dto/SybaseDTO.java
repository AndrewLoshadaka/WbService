package com.allur.wbstatserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SybaseDTO {
    private int countOtk;
    private int countStock;
    private int countRoute;
    private String size;
    private String wbDesign;
    private String prGroup;
    private String tpStream;
}
