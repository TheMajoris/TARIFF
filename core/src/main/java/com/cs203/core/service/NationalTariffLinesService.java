package com.cs203.core.service;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.NationalTariffLinesDto;

public interface NationalTariffLinesService {

    public NationalTariffLinesDto createNationalTariffLines(CreateNationalTariffLinesDto createDto);

    public GenericResponse<NationalTariffLinesDto> updateNationalTariffLines(NationalTariffLinesDto dto,
            Long nationalTariffLinesId);

    public GenericResponse<Void> deleteNationalTariffLines(Long nationalTariffLinesId);


}