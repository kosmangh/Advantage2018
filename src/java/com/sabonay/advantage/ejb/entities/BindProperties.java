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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author crash
 */
@Entity
@Table(name = "bind_properties")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BindProperties.findAll", query = "SELECT b FROM BindProperties b")})
public class BindProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bind_id")
    private Integer bindId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "thread_name")
    private String threadName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "src_ip_address")
    private String srcIpAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "smpp_host")
    private String smppHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "smpp_port")
    private int smppPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "smpp_user")
    private String smppUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "smpp_pwd")
    private String smppPwd;
    @Size(max = 100)
    @Column(name = "addrRange")
    private String addrRange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "operator_id")
    private int operatorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "submit_multi_limit")
    private int submitMultiLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_simulate")
    private boolean isSimulate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ton_src")
    private int tonSrc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "npi_src")
    private int npiSrc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ton_dest")
    private int tonDest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "npi_dest")
    private int npiDest;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "protocol")
    private String protocol;
    @Size(max = 20)
    @Column(name = "param1")
    private String param1;
    @Size(max = 20)
    @Column(name = "param2")
    private String param2;
    @Size(max = 5)
    @Column(name = "deleted")
    private String deleted;
    @Size(max = 5)
    @Column(name = "updated")
    private String updated;
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    public BindProperties() {
    }

    public BindProperties(Integer bindId) {
        this.bindId = bindId;
    }

    public BindProperties(Integer bindId, String threadName, String srcIpAddress, String smppHost, int smppPort, String smppUser, String smppPwd, int operatorId, int submitMultiLimit, boolean isSimulate, int tonSrc, int npiSrc, int tonDest, int npiDest, String protocol) {
        this.bindId = bindId;
        this.threadName = threadName;
        this.srcIpAddress = srcIpAddress;
        this.smppHost = smppHost;
        this.smppPort = smppPort;
        this.smppUser = smppUser;
        this.smppPwd = smppPwd;
        this.operatorId = operatorId;
        this.submitMultiLimit = submitMultiLimit;
        this.isSimulate = isSimulate;
        this.tonSrc = tonSrc;
        this.npiSrc = npiSrc;
        this.tonDest = tonDest;
        this.npiDest = npiDest;
        this.protocol = protocol;
    }

    public Integer getBindId() {
        return bindId;
    }

    public void setBindId(Integer bindId) {
        this.bindId = bindId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getSrcIpAddress() {
        return srcIpAddress;
    }

    public void setSrcIpAddress(String srcIpAddress) {
        this.srcIpAddress = srcIpAddress;
    }

    public String getSmppHost() {
        return smppHost;
    }

    public void setSmppHost(String smppHost) {
        this.smppHost = smppHost;
    }

    public int getSmppPort() {
        return smppPort;
    }

    public void setSmppPort(int smppPort) {
        this.smppPort = smppPort;
    }

    public String getSmppUser() {
        return smppUser;
    }

    public void setSmppUser(String smppUser) {
        this.smppUser = smppUser;
    }

    public String getSmppPwd() {
        return smppPwd;
    }

    public void setSmppPwd(String smppPwd) {
        this.smppPwd = smppPwd;
    }

    public String getAddrRange() {
        return addrRange;
    }

    public void setAddrRange(String addrRange) {
        this.addrRange = addrRange;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getSubmitMultiLimit() {
        return submitMultiLimit;
    }

    public void setSubmitMultiLimit(int submitMultiLimit) {
        this.submitMultiLimit = submitMultiLimit;
    }

    public boolean getIsSimulate() {
        return isSimulate;
    }

    public void setIsSimulate(boolean isSimulate) {
        this.isSimulate = isSimulate;
    }

    public int getTonSrc() {
        return tonSrc;
    }

    public void setTonSrc(int tonSrc) {
        this.tonSrc = tonSrc;
    }

    public int getNpiSrc() {
        return npiSrc;
    }

    public void setNpiSrc(int npiSrc) {
        this.npiSrc = npiSrc;
    }

    public int getTonDest() {
        return tonDest;
    }

    public void setTonDest(int tonDest) {
        this.tonDest = tonDest;
    }

    public int getNpiDest() {
        return npiDest;
    }

    public void setNpiDest(int npiDest) {
        this.npiDest = npiDest;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bindId != null ? bindId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BindProperties)) {
            return false;
        }
        BindProperties other = (BindProperties) object;
        if ((this.bindId == null && other.bindId != null) || (this.bindId != null && !this.bindId.equals(other.bindId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.BindProperties[ bindId=" + bindId + " ]";
    }
    
}
