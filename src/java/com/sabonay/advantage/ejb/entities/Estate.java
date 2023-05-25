package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.api.Selectable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "estate")
public class Estate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "estate_id")
    private String estateId;
    
    @Column(name = "estate_class")
    private String estateClass;
    
    @Column(name = "land_size")
    private double landSize;
    
    @Column(name = "estate_name")
    private String estateName;
    
    @Column(name = "estate_location")
    private String estateLocation;
    
    @Column(name = "date_initialized")
    @Temporal(TemporalType.DATE)
    private Date dateInitialized;
    
    @Column(name = "expiration_date")
    @Temporal(TemporalType.DATE)
    private Date expirationdate;
    
    @Column(name = "additional_desc")
    private String addDesc;
    
    @Column(name = "updated")
    private boolean updated;
    
    @Column(name = "deleted")
    private boolean deleted;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @OneToMany(mappedBy = "estate")
    private List<EstateBlock> estateBlockCollection;
    
    @Transient
    private double landsizeleft;
    
    @Transient
    private boolean selected;
    
    @Transient
    private String durationLeft;

    @Transient
    private int totalBlocks;
    
    @Transient
    private int totalProperty;
    
    
    
    
    
    
    
    public Estate() {
    }

    public Estate(String estateId) {
        this.estateId = estateId;
    }

    public Estate(String estateId, String estateName) {
        this.estateId = estateId;
        this.estateName = estateName;
    }

    public Estate(String estateId, double landSize, String estateClass,String estateName, String estateLocation, boolean updated, boolean deleted, Date lastModifiedDate, String lastModifiedBy, Date dateInitialized, Double landSizeLeft, Date expirationdate, String addDesc) {
        this.estateId = estateId;
        this.landSize = landSize;
        this.estateClass = estateClass;
        this.estateName = estateName;
        this.estateLocation = estateLocation;
        this.updated = updated;
        this.deleted = deleted;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
        this.dateInitialized = dateInitialized;
        this.landsizeleft = landSizeLeft;
        this.expirationdate = expirationdate;
        this.addDesc = addDesc;
    }
    
    
    
    
    
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public void setTotalBlocks(int totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public double getLandSize() {
        return landSize;
    }

    public void setLandSize(double landSize) {
        this.landSize = landSize;
    }

    public String getEstateLocation() {
        return estateLocation;
    }

    public void setEstateLocation(String estateLocation) {
        this.estateLocation = estateLocation;
    }

    public boolean getUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public String getDurationLeft() {
        if(expirationdate != null && dateInitialized != null){
            int yr = expirationdate.getYear() - new Date().getYear();
            int mnths = expirationdate.getMonth() - new Date().getMonth();
            
            if (mnths < 0 && yr >= 1){
                yr -= 1;
                mnths += 11;
            }
            
            if(mnths <= 0 && yr <=0)
                durationLeft = "Expired";
            else
                durationLeft = yr +" yr(s), "+mnths+" mnth(s)";
           return durationLeft;
        } else {
            durationLeft = null;
        }
        return durationLeft;
    }

    public void setDurationLeft(String durationLeft) {
        this.durationLeft = durationLeft;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    
    public Date getDateInitialized(){
        return dateInitialized;
    }
    
    public void setDateInitialized(Date dateInitialized){
        this.dateInitialized = dateInitialized;
    }

    public String getAddDesc() {
        return addDesc;
    }

    public void setAddDesc(String addDesc) {
        this.addDesc = addDesc;
    }
    
    public List<EstateBlock> getEstateBlockList() {
        return estateBlockCollection;
    }

    public void setEstateBlockCollection(List<EstateBlock> estateBlockCollection) {
        this.estateBlockCollection = estateBlockCollection;
    }

    public String getEstateClass() {
        return estateClass;
    }

    public void setEstateClass(String estateClass) {
        this.estateClass = estateClass;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getTotalBlock() {
        List<EstateBlock> blk = new ArrayList();
        for(EstateBlock blck : estateBlockCollection){
            if(blck.getDeleted() == false)
                blk.add(blck);
        }
        totalBlocks = blk.size();
        return totalBlocks;
    }

    public void setTotalBlock(int totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public int getTotalProperty() {
        totalProperty = 0;
        for(EstateBlock eb : estateBlockCollection){
            totalProperty += eb.getPropertiesSize();
        }
        return totalProperty;
    }

    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    public void setTotalProperty(int totalProperty) {
        this.totalProperty = totalProperty;
    }
    
    public double getLandsizeleft() {
//        if(!estateBlockCollection.isEmpty()){
//            double blkSize = 0;
//            for(EstateBlock eBlock : estateBlockCollection){
//                if(eBlock.getBlkSize() != 0)
//                    blkSize += eBlock.getBlkSize();
//            }
//            landsizeleft = landSize - blkSize;
//            return landsizeleft;
//        }
        return landsizeleft;
    }

    public void setLandsizeleft(double landsizeleft) {
        this.landsizeleft = landsizeleft;
    }
    
//</editor-fold>



    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estateId != null ? estateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estate)) {
            return false;
        }
        Estate other = (Estate) object;
        if ((this.estateId == null && other.estateId != null) || (this.estateId != null && !this.estateId.equals(other.estateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return estateName;
    }

    public List<EstateProperty> getEstatePropertiesList() {
        return ds.getCustomQry().allEstatesBlock(estateId);
    }

}
