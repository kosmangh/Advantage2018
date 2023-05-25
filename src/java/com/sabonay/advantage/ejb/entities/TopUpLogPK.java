/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author crash
 */
@Embeddable
public class TopUpLogPK implements Serializable{
    @Basic(optional = false)
    @Column(name = "client_id")
    private int clientId;
    @Column(name = "date_of_transaction")
    @Temporal(TemporalType.DATE)
    private Date dateOfTransaction;
    @Column(name = "created_by")
    private String createdBy; 

    public TopUpLogPK() {
    }

    
    public TopUpLogPK(int clientId, Date dateOfTransaction, String createdBy) {
        this.clientId = clientId;
        this.dateOfTransaction = dateOfTransaction;
        this.createdBy = createdBy;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.clientId;
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
        final TopUpLogPK other = (TopUpLogPK) obj;
        if (this.clientId != other.clientId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TopUpLogPK{" + "clientId=" + clientId + '}';
    }
    
    
}
