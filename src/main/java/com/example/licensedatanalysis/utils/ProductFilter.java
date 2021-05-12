package com.example.licensedatanalysis.utils;

import com.opencsv.bean.CsvToBeanFilter;

public class ProductFilter implements CsvToBeanFilter {

    private final Integer productId;

    public ProductFilter(Integer _productId){
        this.productId = _productId;
    }

    @Override
    public boolean allowLine(String[] strings) {
        return new Integer(strings[2]).equals(productId);
    }
}
