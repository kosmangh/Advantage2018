/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

import java.io.Serializable;
import java.util.Date;

public class PropertyOccupantDetail implements Serializable
{


    private String propertyOccupantId;
    private String occupantName;
    private byte[] propertyOccupantPhoto;
    private String localAddress;
    private String foreignAddress;
    private Date dateOfBirth;
    private String nationality;
    private String maritalStatus;
    private String homeTown;
    private String occupation;
    private String nationalId;
    private String nationalIdType;
    private String employerAddress;
    private String houseType;
    private String estateRequired;
    private String emailAddress;
    private String telephoneNumber;
    private String nextOfKin;
    private String relationship;
    private String gender;
    private String deleted;
    private String updated;
    private Date lastModifiedDate;
    private String lastModifiedBy;

    public PropertyOccupantDetail()
    {
    }

    public PropertyOccupantDetail(String propertyOccupantId)
    {
        this.propertyOccupantId = propertyOccupantId;
    }

    public String getPropertyOccupantId()
    {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId)
    {
        this.propertyOccupantId = propertyOccupantId;
    }




    public byte[] getPropertyOccupantPhoto()
    {
        return propertyOccupantPhoto;
    }

    public void setPropertyOccupantPhoto(byte[] propertyOccupantPhoto)
    {
        this.propertyOccupantPhoto = propertyOccupantPhoto;
    }

    public String getLocalAddress()
    {
        return localAddress;
    }

    public void setLocalAddress(String localAddress)
    {
        this.localAddress = localAddress;
    }

    public String getForeignAddress()
    {
        return foreignAddress;
    }

    public void setForeignAddress(String foreignAddress)
    {
        this.foreignAddress = foreignAddress;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality()
    {
        return nationality;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public String getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public String getHomeTown()
    {
        return homeTown;
    }

    public void setHomeTown(String homeTown)
    {
        this.homeTown = homeTown;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public String getNationalId()
    {
        return nationalId;
    }

    public void setNationalId(String nationalId)
    {
        this.nationalId = nationalId;
    }

    public String getNationalIdType()
    {
        return nationalIdType;
    }

    public void setNationalIdType(String nationalIdType)
    {
        this.nationalIdType = nationalIdType;
    }

    public String getEmployerAddress()
    {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress)
    {
        this.employerAddress = employerAddress;
    }

    public String getHouseType()
    {
        return houseType;
    }

    public void setHouseType(String houseType)
    {
        this.houseType = houseType;
    }

    public String getEstateRequired()
    {
        return estateRequired;
    }

    public void setEstateRequired(String estateRequired)
    {
        this.estateRequired = estateRequired;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }

    public String getNextOfKin()
    {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin)
    {
        this.nextOfKin = nextOfKin;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
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

    public String getOccupantName()
    {
        return occupantName;
    }

    public void setOccupantName(String occupantName)
    {
        this.occupantName = occupantName;
    }

    


    @Override
    public String toString()
    {
        return occupantName;
    }
}
