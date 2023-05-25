/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "property_description")
@NamedQueries(
{
    @NamedQuery(name = "PropertyDescription.findAll", query = "SELECT p FROM PropertyDescription p")
})
public class PropertyDescription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "property_description_id")
    private String propertyDescriptionId;
    
    @Column(name = "property_category")
    private String propertyCategory;
    
    @Column(name = "property_description")
    private String propertyDescription;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Column(name = "deleted")
    private String deleted;
    
    @Column(name = "updated")
    private String updated;
    
    @OneToMany(mappedBy = "propertyDescription")
    private Collection<PropertyOccupant> propertyOccupantCollection;
//    @OneToMany(mappedBy = "propertyDescription")
//    private Collection<EstateProperty> estatePropertyCollection;

    public PropertyDescription()
    {
    }

    public PropertyDescription(String propertyDescriptionId)
    {
        this.propertyDescriptionId = propertyDescriptionId;
    }

    public String getPropertyDescriptionId()
    {
        return propertyDescriptionId;
    }

    public void setPropertyDescriptionId(String propertyDescriptionId)
    {
        this.propertyDescriptionId = propertyDescriptionId;
    }

    public String getPropertyCategory()
    {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory)
    {
        this.propertyCategory = propertyCategory;
    }

    public String getPropertyDescription()
    {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription)
    {
        this.propertyDescription = propertyDescription;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
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

    public Collection<PropertyOccupant> getPropertyOccupantCollection()
    {
        return propertyOccupantCollection;
    }

    public void setPropertyOccupantCollection(Collection<PropertyOccupant> propertyOccupantCollection)
    {
        this.propertyOccupantCollection = propertyOccupantCollection;
    }

//    public Collection<EstateProperty> getEstatePropertyCollection()
//    {
//        return estatePropertyCollection;
//    }
//
//    public void setEstatePropertyCollection(Collection<EstateProperty> estatePropertyCollection)
//    {
//        this.estatePropertyCollection = estatePropertyCollection;
//    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (propertyDescriptionId != null ? propertyDescriptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PropertyDescription))
        {
            return false;
        }
        PropertyDescription other = (PropertyDescription) object;
        if ((this.propertyDescriptionId == null && other.propertyDescriptionId != null) || (this.propertyDescriptionId != null && !this.propertyDescriptionId.equals(other.propertyDescriptionId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.sabonay.shcapp.entities.PropertyDescription[propertyDescriptionId=" + propertyDescriptionId + "]";
    }

}
