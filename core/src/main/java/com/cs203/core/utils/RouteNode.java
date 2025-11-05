package com.cs203.core.utils;

import java.math.BigDecimal;
import java.util.List;

import com.cs203.core.dto.responses.RouteDto;

import lombok.Data;

@Data
public class RouteNode implements Comparable<RouteNode> {
    private BigDecimal cost;
    private Long countryId;
    private List<RouteDto> path;
    private int depth;

    public RouteNode(BigDecimal cost, Long countryId, List<RouteDto> path, int depth) {
            this.cost = cost;
            this.countryId = countryId;
            this.path = path;
            this.depth = depth;
    }

    @Override
    public int compareTo(RouteNode other) {
        return this.cost.compareTo(other.cost);
    }
}
