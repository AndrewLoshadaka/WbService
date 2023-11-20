package com.allur.wbstatserver.model.dto.requestsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SybaseRequestBody {
    private long dateFrom;
    private long dateTo;
}
