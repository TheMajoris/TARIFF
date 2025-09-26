package com.cs203.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.service.NationaltariffLinesService;

@RestController
@RequestMapping("/tariffs")
public class NationalTariffLinesController {
    
    @Autowired
    NationaltariffLinesService nationaltariffLinesService;

    @PostMapping
    @PutMapping("/{id}")
    @DeleteMapping("/{id}")

}
