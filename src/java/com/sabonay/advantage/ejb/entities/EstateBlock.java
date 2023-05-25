/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.entities;

import com.sabonay.common.api.Selectable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Edwin / Ritchid
 */
@Entity
@Table(name = "estate_block")
@NamedQueries({
    @NamedQuery(name = "EstateBlock.findAll", query = "SELECT e FROM EstateBlock e")
})
public class EstateBlock implements Serializable, Selectable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "estate_block_id")
    private String estateBlockId;
    
    @Column(name = "block_name")
    private String blockName;
        
    @Column(name="blk_size", scale = 1)
    private double blkSize;
    
    @Column(name = "descrptn")
    private String AddDesc;
    
    @Column(name = "deleted")
    private boolean deleted;
    
    @Column(name = "updated")
    private boolean updated;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @JoinColumn(name = "estate", referencedColumnName = "estate_id")
    @ManyToOne
    private Estate estate;
    
    @OneToMany(mappedBy = "estateBlock", fetch = FetchType.EAGER)
    private List<EstateProperty> estatePropertyCollection;
    
    @Transient
    private boolean selected;

    @Transient
    private int propertiesSize;
    
    
    
    
    
    
    public EstateBlock() {
    }

    public EstateBlock(String estateBlockId) {
        this.estateBlockId = estateBlockId;
    }

    
    
    
    
    
    public String getEstateBlockId() {
        return estateBlockId;
    }

    public void setEstateBlockId(String estateBlockId) {
        this.estateBlockId = estateBlockId;
    }

    public String getBlockName() {
        if (blockName == null) {
            return null;
        } else {
            return blockName;
        }
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
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

    public Estate getEstate() {
        return estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public double getBlkSize() {
        return blkSize;
    }

    public void setBlkSize(double blkSize) {
        this.blkSize = blkSize;
    }

    public String getAddDesc() {
        return AddDesc;
    }

    public void setAddDesc(String AddDesc) {
        this.AddDesc = AddDesc;
    }

    public List<EstateProperty> getEstatePropertyList() {
        return estatePropertyCollection;
    }

    public void setEstatePropertyCollection(List<EstateProperty> estatePropertyCollection) {
        this.estatePropertyCollection = estatePropertyCollection;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPropertiesSize() {
        List<EstateProperty> estProp = new ArrayList<>();
        for(EstateProperty EsP : estatePropertyCollection){
            if(EsP.getDeleted() == false)
                estProp.add(EsP);
        }
        propertiesSize = estProp.size();
        return propertiesSize;
    }

    public void setPropertiesSize(int propertiesSize) {
        this.propertiesSize = propertiesSize;
    }

    
    
    
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estateBlockId != null ? estateBlockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstateBlock)) {
            return false;
        }
        EstateBlock other = (EstateBlock) object;
        if ((this.estateBlockId == null && other.estateBlockId != null) || (this.estateBlockId != null && !this.estateBlockId.equals(other.estateBlockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return estateBlockId;
    }

    @Override
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public Boolean getSelected() {
        return selected;
    }
}
