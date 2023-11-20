package com.allur.sybaseservice.controllers;

import com.allur.sybaseservice.entity.StockOTKEntity;
import com.allur.sybaseservice.entity.requestsdto.DateDTO;
import com.allur.sybaseservice.services.SybaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sybase")
public class SybaseController {
    private final SybaseService sybaseService;

    @GetMapping("/all")
    public Collection<StockOTKEntity> get(@RequestParam Long dateFrom, @RequestParam Long dateTo){
        return sybaseService.getResultStockAndOTK(dateFrom, dateTo);
    }
}
