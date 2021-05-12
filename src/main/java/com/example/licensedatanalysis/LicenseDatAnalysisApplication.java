package com.example.licensedatanalysis;

import com.example.licensedatanalysis.models.InfoItem;
import com.example.licensedatanalysis.services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class LicenseDatAnalysisApplication implements CommandLineRunner {

    @Value("${file.dir}")
    private String FILE_DIR;

    @Value("${file.name}")
    private String FILE_NAME;

    @Autowired
    private DataService dataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseDatAnalysisApplication.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public static void main(String[] args) {
        SpringApplication.run(LicenseDatAnalysisApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        LOGGER.info("*******************************************");
        LOGGER.info("Start Executing Task @ " + dateFormat.format(new Date()));
        try {
            long start = System.currentTimeMillis();
            executeTask();
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            LOGGER.info("Task Execution Time: " + timeElapsed + " milliseconds");
        } catch (FileNotFoundException e) {
            LOGGER.info("File not found, please check the file uri ->" + FILE_DIR + FILE_NAME);
        }

        LOGGER.info("*******************************************");
    }

    private void executeTask() throws FileNotFoundException {

        /*Import CSV using OpenCSV with ProductID filter*/
        List<InfoItem> targetProductList = dataService.getRawListWithTargetProductId();
        LOGGER.info("Size of data with target productID: " + targetProductList.size());

        /*Use ComputerID and UserID as the Joint Key to eliminate redundant data*/
        List<InfoItem> dataWithoutRedundancy = dataService.cleanRawData(targetProductList);
        LOGGER.info("Size of data without redundancy: " + dataWithoutRedundancy.size());

        /*Group cleaned data by UserID*/
        Map<Long, List<InfoItem>> dataGroupedByUserId = dataService.groupDataByUserId(dataWithoutRedundancy);

        /*Get license number without assuming each license supports two devices for the same user*/
        /*When same user is mapped to multiple pairs of Laptop and Desktop, only one pair of devices can share the same license*/
        Integer resultWithoutAssumption = dataService.getResult(dataGroupedByUserId, false);
        LOGGER.info("Total License Without Assumption: " + resultWithoutAssumption);

        /*Get license number assuming each license supports two devices for the same user*/
        /*When same user is mapped to multiple pairs of Laptop and Desktop,each pair of devices can share the same license*/
        Integer resultWithAssumption = dataService.getResult(dataGroupedByUserId, true);
        LOGGER.info("Total License With Assumption: " + resultWithAssumption);

    }
}
