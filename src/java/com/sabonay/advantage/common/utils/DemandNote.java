/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author crash
 */
public class DemandNote {

    private String occupantName = null;
    private String assessment=null;
    private List<DemandNoticeDetails> listOfDemandNoticeDetails = new ArrayList<DemandNoticeDetails>();

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public List<DemandNoticeDetails> getListOfDemandNoticeDetails() {
        return listOfDemandNoticeDetails;
    }

    public void setListOfDemandNoticeDetails(List<DemandNoticeDetails> listOfDemandNoticeDetails) {
        this.listOfDemandNoticeDetails = listOfDemandNoticeDetails;
    }
    
}
