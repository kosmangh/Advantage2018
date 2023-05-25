/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "arreas_record")
@NamedQueries(
{
    @NamedQuery(name = "ArreasRecord.findAll", query = "SELECT a FROM ArreasRecord a")
})
public class ArreasRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "arreas_record_id")
    private String arreasRecordId;
    
    @JoinColumn(name = "estate_property")
    private EstateProperty estateProperty;
    @Column(name = "year_arreas_started_from")
    private Integer yearArreasStartedFrom;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Column(name = "deleted")
    private String deleted;
    @Column(name = "updated")
    private String updated;
    @Basic
    @Column(name = "verified")
    private Boolean verified = Boolean.FALSE;

    public ArreasRecord()
    {
    }



    public String getArreasRecordId()
    {
        return arreasRecordId;
    }

    public void setArreasRecordId(String arreasRecordId)
    {
        this.arreasRecordId = arreasRecordId;
    }

    public EstateProperty getEstateProperty()
    {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty)
    {
        this.estateProperty = estateProperty;
    }

    public Integer getYearArreasStartedFrom()
    {
        return yearArreasStartedFrom;
    }

    public void setYearArreasStartedFrom(Integer yearArreasStartedFrom)
    {
        this.yearArreasStartedFrom = yearArreasStartedFrom;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDeleted()
    {
        return deleted;
    }

    public void setDeleted(String deleted)
    {
        this.deleted = deleted;
    }

    public String getUpdated()
    {
        return updated;
    }

    public void setUpdated(String updated)
    {
        this.updated = updated;
    }

    public Boolean getVerified()
    {
        return verified;
    }

    public void setVerified(Boolean verified)
    {
        this.verified = verified;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (estateProperty != null ? estateProperty.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArreasRecord))
        {
            return false;
        }
        ArreasRecord other = (ArreasRecord) object;
        if ((this.estateProperty == null && other.estateProperty != null) || (this.estateProperty != null && !this.estateProperty.equals(other.estateProperty)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return arreasRecordId + ":" + verified;
    }

}
