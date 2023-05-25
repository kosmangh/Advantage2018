/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author crash
 */
@Entity
@Table(name = "credit_balance")
@NamedQueries({
    @NamedQuery(name = "CreditBalance.findAll", query = "SELECT c FROM CreditBalance c"),
    @NamedQuery(name = "CreditBalance.findByClientId", query = "SELECT c FROM CreditBalance c WHERE c.clientId = :clientId"),
    @NamedQuery(name = "CreditBalance.findByTotalPurchasedCredit", query = "SELECT c FROM CreditBalance c WHERE c.totalPurchasedCredit = :totalPurchasedCredit"),
    @NamedQuery(name = "CreditBalance.findByCreditLeft", query = "SELECT c FROM CreditBalance c WHERE c.creditLeft = :creditLeft")})
public class CreditBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "client_id")
    private Integer clientId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_purchased_credit")
    private Double totalPurchasedCredit;
    @Column(name = "credit_left")
    private Double creditLeft;

    public CreditBalance() {
    }

    public CreditBalance(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Double getTotalPurchasedCredit() {
        return totalPurchasedCredit;
    }

    public void setTotalPurchasedCredit(Double totalPurchasedCredit) {
        this.totalPurchasedCredit = totalPurchasedCredit;
    }

    public Double getCreditLeft() {
        return creditLeft;
    }

    public void setCreditLeft(Double creditLeft) {
        this.creditLeft = creditLeft;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientId != null ? clientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditBalance)) {
            return false;
        }
        CreditBalance other = (CreditBalance) object;
        if ((this.clientId == null && other.clientId != null) || (this.clientId != null && !this.clientId.equals(other.clientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.CreditBalance[ clientId=" + clientId + " ]";
    }
    
}
