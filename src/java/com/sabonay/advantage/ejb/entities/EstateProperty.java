/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.web.utils.ds;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Edwin / Ritchid
 */
@Entity
@Table(name = "estate_property")
public class EstateProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "estate_property_id")
    private String estatePropertyId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "property_usage")
    private PropertyUsage propertyUsage;
    
    @Column(name = "property_category")
    private String propertyCategory;
    
    @Column(name = "property_number")
    private String propertyNumber;
    
    @Column(name = "property_land_size")
    private Double propertyLandSize;
    
    @Column(name = "deleted")
    private boolean deleted;
    
    @Column(name = "updated")
    private boolean updated;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Column(name = "blocked")
    private boolean blocked;
    
    @Column(name = "property_description")
    private String description;
    
    @OneToMany(mappedBy = "estateProperty")
    private List<EstatePropertyLedger> estatePropertyLedgerList;
    
    @JoinColumn(name = "estate_block", referencedColumnName = "estate_block_id")
    @ManyToOne
    private EstateBlock estateBlock;
    
    @OneToOne(mappedBy = "estateProperty")
    private Occupancy occupancy;
    
    @Transient
    private Estate propertyEstate;
    
    @Transient
    private String propertyName;
    
    @Transient
    private String currentOccupantName;
    
    @Transient
    private PropertyOccupant currentPropertyOccupant; //redundant
    
    @Transient
    private boolean selected;

    @Transient
    private String allocated;

    @Transient
    private String occupied;    
    
    
    
    
    public EstateProperty() {
    }

    public EstateProperty(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    
    
    
    
    
    
    public Estate getPropertyEstate() {
        if (getEstateBlock() != null) {
            propertyEstate = getEstateBlock().getEstate();
        }

        return propertyEstate;
    }

    public String getEstatePropertyId() {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    public PropertyUsage getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(PropertyUsage propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public Double getPropertyLandSize() {
        return propertyLandSize;
    }

    public void setPropertyLandSize(Double propertyLandSize) {
        this.propertyLandSize = propertyLandSize;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EstatePropertyLedger> getEstatePropertyLedgerList() {
        return estatePropertyLedgerList;
    }

    public void setEstatePropertyLedgerList(List<EstatePropertyLedger> estatePropertyLedgerList) {
        this.estatePropertyLedgerList = estatePropertyLedgerList;
    }

    public EstateBlock getEstateBlock() {
        return estateBlock;
    }

    public String getPropertyName() {
        if (getEstateBlock() != null) {
            propertyName = getEstateBlock().getBlockName() + "." + propertyNumber;
        }

        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setEstateBlock(EstateBlock estateBlock) {
        this.estateBlock = estateBlock;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Occupancy occupancy) {
        this.occupancy = occupancy;
    }
    
    public String getCurrentOccupantName() {
        PropertyOccupant current = getCurrentPropertyOccupant();
        if (current != null) {
            currentOccupantName = current.getOccupantName();
        }

        return currentOccupantName;
    }

    public PropertyOccupant getCurrentPropertyOccupant() {
        currentPropertyOccupant = ds.getCustomQry().estatePropertyCurrentOccupant(this);
        //currentPropertyOccupant = ds.getCustomQry().getOccupantPropertyByPropertyId(this);
        return currentPropertyOccupant;
    }

    public Occupancy currentOccupantProperty() {
        return ds.getCustomQry().currentOccupancyByPropertyId(estatePropertyId);
    }

    public PaymentType supposePaymentType() {
        Occupancy op = currentOccupantProperty(); 
 
        if (op != null) {
            return op.getAppropriatePaymentType();
        }

        return null;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public String getAllocated() {
        return allocated;
    }

    public void setAllocated(String allocated) {
        this.allocated = allocated;
    }

    public String getOccupied() {
        if(occupancy == null)
            occupied = "Unoccupied";
        else
            occupied = "Occupied";
        
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estatePropertyId != null ? estatePropertyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstateProperty)) {
            return false;
        }
        EstateProperty other = (EstateProperty) object;
        if ((this.estatePropertyId == null && other.estatePropertyId != null) || (this.estatePropertyId != null && !this.estatePropertyId.equals(other.estatePropertyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return estatePropertyId;
        return getPropertyName();
    }

    
}
