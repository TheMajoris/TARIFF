package com.cs203.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.requests.RouteOptimizationRequestDto;
import com.cs203.core.dto.responses.AlternativeRouteDto;
import com.cs203.core.dto.responses.RouteDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.SavedCalculationsRepository;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.RouteService;
import com.cs203.core.service.TariffRateService;

import com.cs203.core.utils.*;

@Service
public class RouteServiceImpl implements RouteService {

    // Never <1
    private static final int MAX_DEPTH_OPTIMIZER = 3;

    @Autowired
    TariffRateService tariffRateService;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    TariffRateRepository tariffRateRepository;
    @Autowired
    SavedCalculationsRepository savedCalculationsRepository;
    @Autowired
    ProductCategoriesRepository productCategoriesRepository;

    @Override
    public GenericResponse<List<AlternativeRouteDto>> getAlternativeRoute(RouteOptimizationRequestDto dto) {

        System.out.println(dto);

        // Check for valid country codes
        if (!countryRepository.existsByCountryCode(dto.getExportingCountryCode())) {
            return new GenericResponse<List<AlternativeRouteDto>>(HttpStatus.NOT_FOUND, "Source country code does not exist",
                    null);
        }
        if (!countryRepository.existsByCountryCode(dto.getImportingCountryCode())) {
            return new GenericResponse<List<AlternativeRouteDto>>(HttpStatus.NOT_FOUND,
                    "Destination country code does not exist", null);
        }

        // Check for valid HsCode
        if (!productCategoriesRepository.existsByCategoryCode(dto.getHsCode())) {
            return new GenericResponse<List<AlternativeRouteDto>>(HttpStatus.NOT_FOUND, "HS code does not exist", null);
        }

        // Get IDs of country
        Long sourceId = countryRepository.findByCountryCode(dto.getExportingCountryCode()).get().getId();
        Long destId = countryRepository.findByCountryCode(dto.getImportingCountryCode()).get().getId();
        Integer hsCode = dto.getHsCode();

        // Now get alternative routes by Djikstra
        PriorityQueue<RouteNode> queue = new PriorityQueue<>();
        Map<Long, BigDecimal> bestCostMap = new HashMap<>();

        queue.add(new RouteNode(BigDecimal.ZERO, sourceId, new ArrayList<>(), 0));
        bestCostMap.put(sourceId, BigDecimal.ZERO);

        List<AlternativeRouteDto> allRoutes = new ArrayList<>();

        while (!queue.isEmpty()) {
            RouteNode current = queue.poll();
            if (current.getCountryId().equals(destId)) {
                AlternativeRouteDto altRoute = new AlternativeRouteDto();
                altRoute.setRoutes(current.getPath());
                altRoute.setTariffTotalCost(current.getCost());
                altRoute.setTotalCost(dto.getInitialPrice().add(current.getCost()));
                allRoutes.add(altRoute);
                continue; // find other routes
            }

            if (current.getDepth() >= MAX_DEPTH_OPTIMIZER) {
                continue; // reached max depth
            }

            List<TariffRateEntity> neighbors = tariffRateRepository.findAllByExportingCountryIdAndHsCode(current.getCountryId(),
                    hsCode);
            System.out.println("Current from countryId: " + current.getCountryId() + " neighbors count: " + neighbors.size());
            for (TariffRateEntity tre : neighbors) {
                Long nextCountryId = tre.getImportingCountryId();
                System.out.println(" -> To countryId: " + tre.getImportingCountryId() + " tariff " + tre.getTariffRate());

                // Compute cost to next node
                BigDecimal finalPrice = tariffRateService.getFinalPrice(
                        nextCountryId,
                        tre.getExportingCountryId(),
                        hsCode,
                        dto.getInitialPrice(),
                        dto.getQuantity(),
                        dto.getDate());
                BigDecimal nextCost = tariffRateService.getTariffCost(finalPrice, dto.getInitialPrice());
                System.out.println("COST: " + nextCost);

                BigDecimal totalCost = current.getCost().add(nextCost);

                // If this is a cheaper path or not visited
                if (!bestCostMap.containsKey(nextCountryId)
                        || bestCostMap.get(nextCountryId).compareTo(totalCost) > 0) {
                    bestCostMap.put(nextCountryId, totalCost);

                    // Build new path by extending current path
                    List<RouteDto> newPath = new ArrayList<>(current.getPath());
                    RouteDto routeDto = new RouteDto();
                    routeDto.setSourceCountryCode(tre.getExportingCountry().getCountryCode());
                    routeDto.setDestinationCountryCode(tre.getImportingCountry().getCountryCode());
                    routeDto.setTariffRate(tariffRateService.convertToDto(tre));
                    routeDto.setCost(nextCost);
                    newPath.add(routeDto);

                    queue.add(new RouteNode(totalCost, nextCountryId, newPath, current.getDepth() + 1));
                }
            }
        }

        // Caching, to be done in future sprint

        // Removal of AlternativeRoutes that only has size of 1 (aka direct routes)
        // as these would have been the initial calculation post request from frontend
        for (int i = 0; i < allRoutes.size(); i++) {
            if (allRoutes.get(i).getRoutes().size() <= 1) {
                allRoutes.remove(i);
                i--;
            }
        }

        return new GenericResponse<>(HttpStatus.OK, "Alternative routes found", allRoutes);
    }

}
