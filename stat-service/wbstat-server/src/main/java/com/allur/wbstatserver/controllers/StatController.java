package com.allur.wbstatserver.controllers;

import com.allur.wbstatserver.model.dto.SybaseDTO;
import com.allur.wbstatserver.model.dto.requestsDTO.SybaseRequestBody;
import com.allur.wbstatserver.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stat")
public class StatController {
    private final StatService service;

    @GetMapping ("/all")
    public List<SybaseDTO> get(
            @RequestParam(name = "dateFrom") Long dateFrom,
            @RequestParam(name = "dateTo") Long dateTo
    ){
        SybaseRequestBody sybaseRequestBody = new SybaseRequestBody(dateFrom, dateTo);
        return service.getEntities(sybaseRequestBody);
    }
}
