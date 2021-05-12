package com.example.licensedatanalysis.services;

import com.example.licensedatanalysis.models.InfoItem;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface DataService {

    List<InfoItem> getRawListWithTargetProductId() throws FileNotFoundException;

    List<InfoItem> cleanRawData(List<InfoItem> targetProductList);

    Map<Long, List<InfoItem>> groupDataByUserId(List<InfoItem> dataWithoutRedundancy);

    Integer getResult(Map<Long, List<InfoItem>> dataGroupedByUserId, boolean withAssumption);

}
