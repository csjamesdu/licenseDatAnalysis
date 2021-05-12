package com.example.licensedatanalysis.services.impl;

import com.example.licensedatanalysis.models.InfoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;

class DataServiceImplTest {

    private DataServiceImpl dataService;

    @BeforeEach
    void setUp() {
        dataService = new DataServiceImpl();
    }

    @Test
    void cleanRawDataTest() {
        List<InfoItem> rawData = dataProviderForDataCleanTest();
        assertEquals(2, dataService.cleanRawData(rawData).size());
    }

    private List<InfoItem> dataProviderForDataCleanTest() {
        InfoItem[] arr = {
                            new InfoItem(1L, 1L,1,"Desktop"),
                            new InfoItem(1L, 1L,1,"desktop"),
                            new InfoItem(2L, 2L,1,"LAPTOP"),
                            new InfoItem(1L, 1L,1,"laptop")
                        };
        return Arrays.asList(arr);
    }

    @Test
    void groupDataByUserIdTest() {
        List<InfoItem> data = dataProviderForGroupByUserIDTest();
        assertEquals(3, dataService.groupDataByUserId(data).get(1L).size());
        assertEquals(4, dataService.groupDataByUserId(data).size());
    }

    private List<InfoItem> dataProviderForGroupByUserIDTest() {
        InfoItem[] arr = {
                new InfoItem(1L,1L, 1, "Desktop"),
                new InfoItem(2L,1L, 1,"Desktop"),
                new InfoItem(3L, 1L, 1, "Laptop"),
                new InfoItem(4L,2L,1,"Desktop"),
                new InfoItem(5L,3L,1,"Laptop"),
                new InfoItem(6L,4L, 1, "Desktop")
        };
        return Arrays.asList(arr);
    }

    @Test
    void getComplexCaseResultTest() {
        Map<Long, List<InfoItem>> userMap = dataProviderForComplexResultTest();
        assertEquals(16, dataService.getComplexCaseResult(userMap,true));
        assertEquals(18,dataService.getComplexCaseResult(userMap,false));
    }

    @Test
    void getComplexCaseValueMapWithAssumptionTest() {

        Map<Long, List<InfoItem>> userMap = dataProviderForComplexResultTest();
        assertThat(dataService.getComplexCaseValueMapWithAssumption(userMap),
                hasEntry(1L,4));
        assertThat(dataService.getComplexCaseValueMapWithAssumption(userMap),
                hasEntry(2L,4));
        assertThat(dataService.getComplexCaseValueMapWithAssumption(userMap),
                hasEntry(3L,2));
        assertThat(dataService.getComplexCaseValueMapWithAssumption(userMap),
                hasEntry(4L,3));
        assertThat(dataService.getComplexCaseValueMapWithAssumption(userMap),
                hasEntry(5L,3));

    }

    @Test
    void getComplexCaseValueMapWithoutAssumptionTest() {

        Map<Long, List<InfoItem>> userMap = dataProviderForComplexResultTest();
        assertThat(dataService.getComplexCaseValueMapWithoutAssumption(userMap),
                    hasEntry(1L,4));
        assertThat(dataService.getComplexCaseValueMapWithoutAssumption(userMap),
                hasEntry(2L,4));
        assertThat(dataService.getComplexCaseValueMapWithoutAssumption(userMap),
                hasEntry(3L,3));
        assertThat(dataService.getComplexCaseValueMapWithoutAssumption(userMap),
                hasEntry(4L,3));
        assertThat(dataService.getComplexCaseValueMapWithoutAssumption(userMap),
                hasEntry(5L,4));
    }

    private Map<Long, List<InfoItem>> dataProviderForComplexResultTest() {
        Map<Long,List<InfoItem>> resultMap = new HashMap<>();
        InfoItem i1 = new InfoItem(1L, 1L, 1, "Desktop");
        InfoItem i2 = new InfoItem(2L,1L,1,"Desktop");
        InfoItem i3 = new InfoItem(3L,1L,1,"DESKTOP");
        InfoItem i4 = new InfoItem(4L, 1L, 1, "desktop");
        InfoItem[] allDeskTop = {i1, i2, i3, i4};
        resultMap.put(1L, Arrays.asList(allDeskTop));
        InfoItem i5 = new InfoItem(5L, 2L, 1, "Laptop");
        InfoItem i6 = new InfoItem(6L, 2L, 1, "LAPTOP");
        InfoItem i7 = new InfoItem(7L, 2L, 1, "laptop");
        InfoItem i8 = new InfoItem(8L, 2L, 1, "LapTop");
        InfoItem[] allLapTop = {i5,i6,i7,i8};
        resultMap.put(2L, Arrays.asList(allLapTop));
        InfoItem i9 = new InfoItem(9L, 3L,1,"DeskTop");
        InfoItem i10 = new InfoItem(10L,3L,1,"DeskTop");
        InfoItem i11 = new InfoItem(11L, 3L, 1,"LapTop");
        InfoItem i12 = new InfoItem(12L, 3L, 1, "laptop");
        InfoItem[] halfHalf = {i9,i10,i11,i12};
        resultMap.put(3L, Arrays.asList(halfHalf));
        InfoItem i13 = new InfoItem(13L, 4L,1,"DeskTop");
        InfoItem i14 = new InfoItem(14L, 4L,1,"DESKTOP");
        InfoItem i15 = new InfoItem(15L, 4L,1,"desktop");
        InfoItem i16 = new InfoItem(16L, 4L,1,"laptop");
        InfoItem[] moreDeskTop = {i13, i14, i15, i16};
        resultMap.put(4L, Arrays.asList(moreDeskTop));
        InfoItem i17 = new InfoItem(17L, 5L,1,"DeskTop");
        InfoItem i18 = new InfoItem(18L, 5L,1,"DESKTOP");
        InfoItem i19 = new InfoItem(19L, 5L,1,"LAPTOP");
        InfoItem i20 = new InfoItem(20L, 5L,1,"laptop");
        InfoItem i21 = new InfoItem(21L, 5L,1,"laptop");
        InfoItem[] moreLapTop = {i17, i18, i19, i20, i21};
        resultMap.put(5L, Arrays.asList(moreLapTop));
        return resultMap;
    }

    @Test
    void getSimpleCaseResultTest() {

        Map<Long, List<InfoItem>> userMap = dataProviderForSimpleResultTest();
        assertEquals(3, dataService.getSimpleCaseResult(userMap));
    }

    private Map<Long, List<InfoItem>> dataProviderForSimpleResultTest() {
        Map<Long,List<InfoItem>> resultMap = new HashMap<>();
        InfoItem i1 = new InfoItem(100L, 4L, 1, "Desktop");
        InfoItem[] allDeskTop = {i1};
        resultMap.put(4L, Arrays.asList(allDeskTop));
        InfoItem i5 = new InfoItem(200L, 5L, 1, "Laptop");
        InfoItem[] allLapTop = {i5};
        resultMap.put(5L, Arrays.asList(allLapTop));
        InfoItem i9 = new InfoItem(300L, 6L,1,"DeskTop");
        InfoItem[] halfHalf = {i9};
        resultMap.put(6L, Arrays.asList(halfHalf));
        return resultMap;
    }

    @Test
    void getResultTest() {
        Map<Long, List<InfoItem>> userMap = dataProviderForGetResultTest();
        assertEquals(13, dataService.getResult(userMap,true));
        assertEquals(14, dataService.getResult(userMap,false));

    }

    private Map<Long, List<InfoItem>> dataProviderForGetResultTest(){
        Map<Long,List<InfoItem>> resultMap = new HashMap<>();
        InfoItem i1 = new InfoItem(1L, 1L, 1, "Desktop");
        InfoItem i2 = new InfoItem(2L,1L,1,"Desktop");
        InfoItem i3 = new InfoItem(3L,1L,1,"DESKTOP");
        InfoItem i4 = new InfoItem(4L, 1L, 1, "desktop");
        InfoItem[] allDeskTop = {i1, i2, i3, i4};
        resultMap.put(1L, Arrays.asList(allDeskTop));
        InfoItem i5 = new InfoItem(5L, 2L, 1, "Laptop");
        InfoItem i6 = new InfoItem(6L, 2L, 1, "LAPTOP");
        InfoItem i7 = new InfoItem(7L, 2L, 1, "laptop");
        InfoItem i8 = new InfoItem(8L, 2L, 1, "LapTop");
        InfoItem[] allLapTop = {i5,i6,i7,i8};
        resultMap.put(2L, Arrays.asList(allLapTop));
        InfoItem i9 = new InfoItem(9L, 3L,1,"DeskTop");
        InfoItem i10 = new InfoItem(10L,3L,1,"DeskTop");
        InfoItem i11 = new InfoItem(11L, 3L, 1,"LapTop");
        InfoItem i12 = new InfoItem(12L, 3L, 1, "laptop");
        InfoItem[] halfHalf = {i9,i10,i11,i12};
        resultMap.put(3L, Arrays.asList(halfHalf));
        InfoItem i13 = new InfoItem(100L, 4L, 1, "Desktop");
        InfoItem[] singleItemOne = {i13};
        resultMap.put(4L, Arrays.asList(singleItemOne));
        InfoItem i14 = new InfoItem(200L, 5L, 1, "Laptop");
        InfoItem[] singleItemTwo = {i14};
        resultMap.put(5L, Arrays.asList(singleItemTwo));
        InfoItem i15= new InfoItem(300L, 6L,1,"DeskTop");
        InfoItem[] singleItemThree = {i15};
        resultMap.put(6L, Arrays.asList(singleItemThree));
        return resultMap;
    }

}