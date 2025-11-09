package com.cs203.core.utils;

import com.cs203.core.dto.responses.RouteDto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RouteNodeTest {

    @Test
    void testConstructorAndGetters() {
        BigDecimal cost = new BigDecimal("100.50");
        Long countryId = 1L;
        List<RouteDto> path = List.of();
        int depth = 2;

        RouteNode node = new RouteNode(cost, countryId, path, depth);

        assertEquals(cost, node.getCost());
        assertEquals(countryId, node.getCountryId());
        assertEquals(path, node.getPath());
        assertEquals(depth, node.getDepth());
    }

    @Test
    void testSetters() {
        RouteNode node = new RouteNode(BigDecimal.ZERO, 0L, List.of(), 0);

        BigDecimal newCost = new BigDecimal("200.75");
        Long newCountryId = 2L;
        List<RouteDto> newPath = List.of(new RouteDto());
        int newDepth = 5;

        node.setCost(newCost);
        node.setCountryId(newCountryId);
        node.setPath(newPath);
        node.setDepth(newDepth);

        assertEquals(newCost, node.getCost());
        assertEquals(newCountryId, node.getCountryId());
        assertEquals(newPath, node.getPath());
        assertEquals(newDepth, node.getDepth());
    }

    @Test
    void testCompareTo() {
        RouteNode node1 = new RouteNode(new BigDecimal("50"), 1L, List.of(), 0);
        RouteNode node2 = new RouteNode(new BigDecimal("100"), 2L, List.of(), 0);

        assertTrue(node1.compareTo(node2) < 0);
        assertTrue(node2.compareTo(node1) > 0);
        assertEquals(0, node1.compareTo(new RouteNode(new BigDecimal("50"), 3L, List.of(), 0)));
    }
}
