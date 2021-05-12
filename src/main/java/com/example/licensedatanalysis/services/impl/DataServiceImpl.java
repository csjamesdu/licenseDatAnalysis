package com.example.licensedatanalysis.services.impl;

import com.example.licensedatanalysis.utils.ProductFilter;
import com.example.licensedatanalysis.models.InfoItem;
import com.example.licensedatanalysis.services.DataService;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    @Value("${file.dir}")
    private String FILE_DIR;

    @Value("${file.name}")
    private String FILE_NAME;

    @Value("${product.id}")
    private Integer PROD_ID;

    @Override
    public List<InfoItem> getRawListWithTargetProductId() throws FileNotFoundException {
        InputStreamReader streamReader = new FileReader(FILE_DIR + FILE_NAME);
        CsvToBeanFilter prodFilter = new ProductFilter(PROD_ID);
        HeaderColumnNameTranslateMappingStrategy<InfoItem> strategy = new HeaderColumnNameTranslateMappingStrategy();
        Map<String, String> columnMap = new HashMap();
        columnMap.put("ComputerId", "computerId");
        columnMap.put("UserId", "userId");
        columnMap.put("ApplicationID", "applicationID");
        columnMap.put("ComputerType", "computerType");
        strategy.setType(InfoItem.class);
        strategy.setColumnMapping(columnMap);
        CsvToBean csvToBean = new CsvToBean();
        csvToBean.setFilter(prodFilter);
        csvToBean.setCsvReader(new CSVReader(streamReader));
        csvToBean.setMappingStrategy(strategy);
        return csvToBean.parse();
    }

    @Override
    public List<InfoItem> cleanRawData(List<InfoItem> targetProductList) {
        Map<String, InfoItem> map = targetProductList.stream()
                .collect(Collectors.toMap(InfoItem::getJointIdOfComputerAndUser, item->item, (item1, item2) -> item1));

        return new ArrayList<>(map.values());
    }

    @Override
    public Map<Long, List<InfoItem>> groupDataByUserId(List<InfoItem> dataWithoutRedundancy) {
        return dataWithoutRedundancy.stream()
                .collect(Collectors.groupingBy(InfoItem::getUserId, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public Integer getResult(Map<Long, List<InfoItem>> dataGroupedByUserId, boolean withAssumption) {
        
        Integer simpleCaseResult = getSimpleCaseResult(dataGroupedByUserId);
        Integer complexCaseResult = getComplexCaseResult(dataGroupedByUserId, withAssumption);

        return simpleCaseResult + complexCaseResult;
    }


    protected Integer getComplexCaseResult(Map<Long, List<InfoItem>> dataGroupedByUserId, boolean withAssumption) {
        Map<Long, Integer> valueMap;

        Map<Long, List<InfoItem>> filteredMap = dataGroupedByUserId.entrySet().stream()
                .filter(entry->entry.getValue().size()>1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if(withAssumption){
            valueMap = getComplexCaseValueMapWithAssumption(filteredMap);
        } else{
            valueMap = getComplexCaseValueMapWithoutAssumption(filteredMap);
        }

        return valueMap.values().stream().reduce(0, Integer::sum);
    }

    protected  Map<Long, Integer> getComplexCaseValueMapWithAssumption(Map<Long, List<InfoItem>> complexCasesMap){

        return complexCasesMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, t -> {
                    List<InfoItem> values = t.getValue();
                    List<String> mappedValue = values.stream()
                            .map(item -> item.getComputerType().toUpperCase())
                            .collect(Collectors.toList());
                    int deskTopNumbers = (int) mappedValue.stream()
                            .filter(e -> e.equals("DESKTOP")).count();
                    int lapTopNumbers = (int) mappedValue.stream()
                            .filter(e -> e.equals("LAPTOP")).count();
                    if (mappedValue.stream().distinct().count() > 1) {
                        return Math.max(deskTopNumbers, lapTopNumbers);
                    } else {
                        return values.size();
                    }
                }));
    }

    protected  Map<Long, Integer> getComplexCaseValueMapWithoutAssumption(Map<Long, List<InfoItem>> complexCasesMap){

        return complexCasesMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v->{
                    List<InfoItem> values = v.getValue();
                    if(values.stream().map(e->e.getComputerType().toUpperCase()).distinct().count()<=1){
                        return values.size();
                    } else{
                        return values.size()-1;
                    }
                }));
    }

    protected Integer getSimpleCaseResult(Map<Long, List<InfoItem>> dataGroupedByUserId) {
        Map<Long,List<InfoItem>> simpleCases = dataGroupedByUserId.entrySet().stream()
                .filter(entry->entry.getValue().size()==1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return simpleCases.size();
    }


}
