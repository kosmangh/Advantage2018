/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author crash
 */
@Entity
@Table(name = "topup_log")
public class TopupLog implements Serializable{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private TopUpLogPK logPK;
    @Column(name = "amount")
    private Double amount;

    public TopupLog() {
    }
    
    public TopupLog(TopUpLogPK logPK) {
        this.logPK = logPK;
    }

    public TopUpLogPK getLogPK() {
        return logPK;
    }

    public void setLogPK(TopUpLogPK logPK) {
        this.logPK = logPK;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.logPK);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TopupLog other = (TopupLog) obj;
        if (!Objects.equals(this.logPK, other.logPK)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TopupLog{" + "logPK=" + logPK + '}';
    }
    
    
}
