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
 * @author SHC
 */
@Entity
@Table(name = "setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Setting.findAll", query = "SELECT s FROM Setting s"),
    @NamedQuery(name = "Setting.findBySettingsKey", query = "SELECT s FROM Setting s WHERE s.settingsKey = :settingsKey"),
    @NamedQuery(name = "Setting.findBySettingsValue", query = "SELECT s FROM Setting s WHERE s.settingsValue = :settingsValue"),
    @NamedQuery(name = "Setting.findByLastModifiedBy", query = "SELECT s FROM Setting s WHERE s.lastModifiedBy = :lastModifiedBy"),
    @NamedQuery(name = "Setting.findByLastModifiedDate", query = "SELECT s FROM Setting s WHERE s.lastModifiedDate = :lastModifiedDate"),
    @NamedQuery(name = "Setting.findByDeleted", query = "SELECT s FROM Setting s WHERE s.deleted = :deleted"),
    @NamedQuery(name = "Setting.findByUpdated", query = "SELECT s FROM Setting s WHERE s.updated = :updated")})
public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "settings_key")
    private String settingsKey;
    @Size(max = 255)
    @Column(name = "settings_value")
    private String settingsValue;
    @Size(max = 45)
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Size(max = 5)
    @Column(name = "deleted")
    private String deleted;
    @Size(max = 5)
    @Column(name = "updated")
    private String updated;

    public Setting() {
    }

    public Setting(String settingsKey) {
        this.settingsKey = settingsKey;
    }

    public String getSettingsKey() {
        return settingsKey;
    }

    public void setSettingsKey(String settingsKey) {
        this.settingsKey = settingsKey;
    }

    public String getSettingsValue() {
        return settingsValue;
    }

    public void setSettingsValue(String settingsValue) {
        this.settingsValue = settingsValue;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (settingsKey != null ? settingsKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setting)) {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.settingsKey == null && other.settingsKey != null) || (this.settingsKey != null && !this.settingsKey.equals(other.settingsKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Setting[ settingsKey=" + settingsKey + " ]";
    }
    
}
