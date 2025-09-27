package com.cs203.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.NationalTariffLinesDto;
import com.cs203.core.service.NationaltariffLinesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tariffs")
public class NationalTariffLinesController {
    
    @Autowired
    NationaltariffLinesService nationaltariffLinesService;

    @PostMapping
    public ResponseEntity<NationalTariffLinesDto> createNationalTariffLine(
        @Valid @RequestBody CreateNationalTariffLinesDto createDto) {
        return new ResponseEntity<>(nationaltariffLinesService.createNationalTariffLines(createDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
public ResponseEntity<GenericResponse<NationalTariffLinesDto>> updateNationalTariffLine(
    @Valid @RequestBody NationalTariffLinesDto dto, @PathVariable Long id) {
    GenericResponse<NationalTariffLinesDto> response = nationaltariffLinesService.updateNationalTariffLines(dto, id);
    return new ResponseEntity<>(response, response.getStatus());
}



    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteNationalTariffLine(@PathVariable Long id) {
        GenericResponse<Void> response = nationaltariffLinesService.deleteNationalTariffLines(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
