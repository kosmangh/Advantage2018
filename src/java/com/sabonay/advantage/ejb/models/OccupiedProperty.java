/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import java.util.Date;

/**
 *
 * @author Edwin
 */
public class OccupiedProperty extends EstatePropertyDetail{


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

    private Date firstDateOfOccupancy;
    private Date lastDateOfOccupancy;
    
    

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getEmployerAddress()
    {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress)
    {
        this.employerAddress = employerAddress;
    }

    public String getEstateRequired()
    {
        return estateRequired;
    }

    public void setEstateRequired(String estateRequired)
    {
        this.estateRequired = estateRequired;
    }

    public String getForeignAddress()
    {
        return foreignAddress;
    }

    public void setForeignAddress(String foreignAddress)
    {
        this.foreignAddress = foreignAddress;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getHomeTown()
    {
        return homeTown;
    }

    public void setHomeTown(String homeTown)
    {
        this.homeTown = homeTown;
    }

    public String getHouseType()
    {
        return houseType;
    }

    public void setHouseType(String houseType)
    {
        this.houseType = houseType;
    }

    public String getPropertyOccupantId()
    {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId)
    {
        this.propertyOccupantId = propertyOccupantId;
    }


    public String getOccupantName()
    {
        return occupantName;
    }

    public void setOccupantName(String occupantName)
    {
        this.occupantName = occupantName;
    }

    public byte[] getPropertyOccupantPhoto()
    {
        return propertyOccupantPhoto;
    }

    public void setPropertyOccupantPhoto(byte[] propertyOccupantPhoto)
    {
        this.propertyOccupantPhoto = propertyOccupantPhoto;
    }

    public Date getFirstDateOfOccupancy()
    {
        return firstDateOfOccupancy;
    }

    public void setFirstDateOfOccupancy(Date firstDateOfOccupancy)
    {
        this.firstDateOfOccupancy = firstDateOfOccupancy;
    }

    public Date getLastDateOfOccupancy()
    {
        return lastDateOfOccupancy;
    }

    public void setLastDateOfOccupancy(Date lastDateOfOccupancy)
    {
        this.lastDateOfOccupancy = lastDateOfOccupancy;
    }


    public String getLocalAddress()
    {
        return localAddress;
    }

    public void setLocalAddress(String localAddress)
    {
        this.localAddress = localAddress;
    }

    public String getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
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

    public String getNationality()
    {
        return nationality;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public String getNextOfKin()
    {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin)
    {
        this.nextOfKin = nextOfKin;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }


    

}
