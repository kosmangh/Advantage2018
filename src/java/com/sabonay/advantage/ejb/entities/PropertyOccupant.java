/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.entities;

import com.sabonay.advantage.common.constants.OccupantType;
import com.sabonay.common.api.Selectable;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "property_occupant")
public class PropertyOccupant implements Serializable, Selectable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "property_occupant_id")
    private String propertyOccupantId;
    
    @Column(name = "occupant_name")
    private String occupantName;
    
    @Lob
    @Column(name = "property_occupant_photo")
    private byte[] propertyOccupantPhoto;
    
    @Column(name = "local_address")
    private String localAddress;
    
    @Column(name = "foreign_address")
    private String foreignAddress;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "nationality")
    private String nationality;
    
    @Column(name = "marital_status")
    private String maritalStatus;
    
    @Column(name = "home_town")
    private String homeTown;
    
    @Column(name = "occupation")
    private String occupation;
    
    @Column(name = "national_id")
    private String nationalId;
    
    @Column(name = "national_id_type")
    private String nationalIdType;
    
    @Column(name = "employer_address")
    private String employerAddress;
    
    @Column(name = "house_type")
    private String houseType;
    
    @Column(name = "estate_required")
    private String estateRequired;
    
    @Column(name = "email_address")
    private String emailAddress;
    
    @Column(name = "telephone_number")
    private String telephoneNumber;
    
    @Column(name = "next_of_kin")
    private String nextOfKin;
    
    @Column(name = "relationship")
    private String relationship;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "deleted")
    private String deleted;
    
    @Column(name = "updated")
    private String updated;
    
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.DATE)
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @Column(name = "occupant_type")
    @Enumerated(EnumType.STRING)
    private OccupantType occupantType;
    
    @JoinColumn(name = "asset_description", referencedColumnName = "property_description_id")
    @ManyToOne
    private PropertyDescription propertyDescription;
    
    @OneToMany(mappedBy = "propertyOccupant")
    private List<EstatePropertyLedger> estatePropertyLedgerList;
    
    @OneToMany(mappedBy = "propertyOccupant")
    private List<Occupancy> occupantPropertyList;
    
    @Transient
    private boolean selected;

    public PropertyOccupant() {
    }

    public PropertyOccupant(String propertyOccupantId) {
        this.propertyOccupantId = propertyOccupantId;
    }

    public String getPropertyOccupantId() {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId) {
        this.propertyOccupantId = propertyOccupantId;
    }

    public byte[] getPropertyOccupantPhoto() {
        return propertyOccupantPhoto;
    }

    public void setPropertyOccupantPhoto(byte[] propertyOccupantPhoto) {
        this.propertyOccupantPhoto = propertyOccupantPhoto;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getForeignAddress() {
        return foreignAddress;
    }

    public void setForeignAddress(String foreignAddress) {
        this.foreignAddress = foreignAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalIdType() {
        return nationalIdType;
    }

    public void setNationalIdType(String nationalIdType) {
        this.nationalIdType = nationalIdType;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getEstateRequired() {
        return estateRequired;
    }

    public void setEstateRequired(String estateRequired) {
        this.estateRequired = estateRequired;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public PropertyDescription getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(PropertyDescription propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public List<EstatePropertyLedger> getEstatePropertyLedgerList() {
        return estatePropertyLedgerList;
    }

    public void setEstatePropertyLedgerList(List<EstatePropertyLedger> estatePropertyLedgerList) {
        this.estatePropertyLedgerList = estatePropertyLedgerList;
    }

    public List<Occupancy> getOccupantPropertyList() {
        return occupantPropertyList;
    }

    public void setOccupantPropertyList(List<Occupancy> occupantPropertyList) {
        this.occupantPropertyList = occupantPropertyList;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public OccupantType getOccupantType() {
        return occupantType;
    }

    public void setOccupantType(OccupantType occupantType) {
        this.occupantType = occupantType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PropertyOccupant other = (PropertyOccupant) obj;
        if ((this.propertyOccupantId == null) ? (other.propertyOccupantId != null) : !this.propertyOccupantId.equals(other.propertyOccupantId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.propertyOccupantId != null ? this.propertyOccupantId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return occupantName;
    }

    public String occupationNote() {
        String msg = "";
        for (Occupancy occupantProperty : occupantPropertyList) {
            msg += "[" + occupantProperty.getEstateProperty().getEstatePropertyId()
                    + "," + occupantProperty.getOccupationType().name() + "]";
        }

        return msg;
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
