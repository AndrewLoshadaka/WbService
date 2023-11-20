package com.allur.sybaseservice.entity.requestsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateDTO {
    private Long dateFrom;
    private Long dateTo;
}
