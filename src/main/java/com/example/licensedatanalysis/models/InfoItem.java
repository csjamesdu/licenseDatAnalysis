package com.example.licensedatanalysis.models;

import com.opencsv.bean.CsvBindByName;

import java.util.List;
import java.util.function.Function;

public class InfoItem {

    @CsvBindByName
    private Long ComputerId;

    @CsvBindByName
    private Long UserId;

    @CsvBindByName
    private Integer ApplicationID;

    @CsvBindByName
    private String ComputerType;

    @CsvBindByName
    private String Comment;

    public InfoItem(){}

    public InfoItem(Long computerId, Long userId, Integer applicationID, String computerType) {
        ComputerId = computerId;
        UserId = userId;
        ApplicationID = applicationID;
        ComputerType = computerType;
    }

    public String getJointIdOfComputerAndUser(){
        return ComputerId.toString() + UserId.toString();
    }

    public Long getComputerId() {
        return ComputerId;
    }

    public Long getUserId() {
        return UserId;
    }

    public Integer getApplicationID() {
        return ApplicationID;
    }

    public String getComputerType() {
        return ComputerType;
    }

    public String getComment() {
        return Comment;
    }

    public void setComputerId(Long computerId) {
        ComputerId = computerId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public void setApplicationID(Integer applicationID) {
        ApplicationID = applicationID;
    }

    public void setComputerType(String computerType) {
        ComputerType = computerType;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    @Override
    public String toString() {
        return "InfoItem{" +
                "ComputerId=" + ComputerId +
                ", UserId=" + UserId +
                ", ApplicationID=" + ApplicationID +
                ", ComputerType='" + ComputerType + '\'' +
                '}';
    }


}
