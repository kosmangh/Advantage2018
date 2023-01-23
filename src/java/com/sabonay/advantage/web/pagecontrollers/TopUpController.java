/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.ejb.entities.CreditBalance;
import com.sabonay.advantage.ejb.entities.TopUpLogPK;
import com.sabonay.advantage.ejb.entities.TopupLog;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author crash
 */
@Named
@RequestScoped
public class TopUpController implements Serializable{
    private static final Logger LOG = Logger.getLogger(TopUpController.class.getName());
    
    private List<CreditBalance> cbList = null;
    private List<TopupLog> topUpList = null;
    private DataModel<TopupLog> logsTbl;
    private CreditBalance cb = new CreditBalance();
    private DataModel<CreditBalance> cbTbl;
    private TopUpLogPK topUpLogPK = new TopUpLogPK();
    private TopupLog topupLog;
    private Double amount;
    private final Integer clientId = 1;

    public TopUpController() {
    }
    
    @PostConstruct
    private void init(){
        try {
            cbList = ds.getCommonQry().creditBalanceGetAll();
            topUpList = ds.getCommonQry().topUpLogGetAll();
            cbTbl = new ListDataModel<>(cbList);
            logsTbl = new ListDataModel<>(topUpList);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "TopupController::init {0}: ", e);
        }
    }
    
    public String topupBalance(){
        topupLog = new TopupLog(topUpLogPK);
        try {
            topupLog.getLogPK().setClientId(clientId);
            topupLog.setAmount(amount);
            ds.getCommonQry().topUpLogCreate(topupLog);
            ds.getCommonQry().creditBalanceUpdate(topupLog);
            JsfUtil.addInformationMessage("Credit balance updated successfully");
            updatecreditBalanceTables();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Credit balance update failed");
            LOG.log(Level.SEVERE, "TopupController::topupBalance {0} ", e);
        }
        return null;
    }
    
    private void updatecreditBalanceTables(){
        try {
            init();
        } catch (Exception e) {
        }
    }

    public List<CreditBalance> getCbList() {
        return cbList;
    }

    public void setCbList(List<CreditBalance> cbList) {
        this.cbList = cbList;
    }

    public CreditBalance getCb() {
        return cb;
    }

    public void setCb(CreditBalance cb) {
        this.cb = cb;
    }

    public DataModel<CreditBalance> getCbTbl() {
        return cbTbl;
    }

    public void setCbTbl(DataModel<CreditBalance> cbTbl) {
        this.cbTbl = cbTbl;
    }

    public TopUpLogPK getTopUpLogPK() {
        return topUpLogPK;
    }

    public void setTopUpLogPK(TopUpLogPK topUpLogPK) {
        this.topUpLogPK = topUpLogPK;
    }

    public TopupLog getTopupLog() {
        return topupLog;
    }

    public void setTopupLog(TopupLog topupLog) {
        this.topupLog = topupLog;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<TopupLog> getTopUpList() {
        return topUpList;
    }

    public void setTopUpList(List<TopupLog> topUpList) {
        this.topUpList = topUpList;
    }

    public DataModel<TopupLog> getLogsTbl() {
        return logsTbl;
    }

    public void setLogsTbl(DataModel<TopupLog> logsTbl) {
        this.logsTbl = logsTbl;
    }
    
    
}
