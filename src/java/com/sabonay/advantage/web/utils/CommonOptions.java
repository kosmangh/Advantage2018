/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

import com.sabonay.advantage.common.constants.ContactGroup;
import com.sabonay.advantage.common.constants.OccupantType;
import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.ejb.entities.Staff;
import com.sabonay.advantage.ejb.entities.Status;
import com.sabonay.advantage.ejb.entities.UserRole;
import com.sabonay.common.constants.Gender;
import com.sabonay.common.constants.RegistrationIDs;
import com.sabonay.common.constants.Relationship;
import com.sabonay.common.constants._MediumOfPayment;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.common.utils.NumberRange;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Edwin / Ritchid
 */
@Named
@ApplicationScoped
public class CommonOptions implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger LOG = Logger.getLogger(CommonOptions.class.getName());
    
    private SelectItem[] blkFilters;
    //</editor-fold>
    
    private static SelectItem[] propertyUsage;
    private static SelectItem[] propertyCategory;
    private SelectItem[] landSizeItems;
//    private SelectItem[] ledgerEntryCategory;
    private static SelectItem[] groundRentBillingYears;
    private static SelectItem[] mediumOfPaymentOptions;
    private static SelectItem[] estateOptions;
    private SelectItem[] occupantTypeOptions;
    private SelectItem[] propertyOccupiedAsOptions;
    private SelectItem[] paymentTypeOptions;
    private SelectItem[] calenderMonthNamesOptions;
    private SelectItem[] reportTypesOptions;
    private static SelectItem[] estateClassOptions;
    private SelectItem[] statusOptions;
    private SelectItem[] genderOptions;
    private SelectItem[] nationalIdTypeOptions;
    private SelectItem[] relationshipOptions;
    private SelectItem[] userAccessRightsOptions;
    private SelectItem[] contactGroupOptions;
    private SelectItem[] activeStaffNames;



    
    
//    private SelectItem[] propertiesLoadingConstraintsOptions;
    public CommonOptions() {

        genderOptions = JsfUtil.createSelectItems(Gender.values(), true);

        nationalIdTypeOptions = JsfUtil.createSelectItems(RegistrationIDs.values(), true);

        relationshipOptions = JsfUtil.createSelectItems(Relationship.values(), true);

        propertyUsage = JsfUtil.createSelectItems(PropertyUsage.values(), true);

        String propteryCat[] = {"House", "Land"};
        propertyCategory = JsfUtil.createSelectItems(propteryCat, true);

        String landSizez[] = {"0.00-0.25 Arc", "0.26-0.50 Arc", "0.51-0.75 Arc"};
        landSizeItems = JsfUtil.createSelectItems(landSizez, true);

        String estateClass[] = {"First Class", "Second Class", "Third Class"};
        estateClassOptions = JsfUtil.createSelectItems(estateClass, true);

        String reportCategory[] = {"Monthly Report", "Quarterly Report", "Mid Year Report", "Annual Report"};
        reportTypesOptions = JsfUtil.createSelectItems(reportCategory, false);

        List<Integer> years = NumberRange.generateRange(1980,
                DateTimeUtils.getCurrentYear() + 1);
        
//         List<Integer> years = NumberRange.generateRange(1980,
//                DateTimeUtils.getCurrentYear());

        Collections.reverse(years);

        groundRentBillingYears = JsfUtil.createSelectItems(years, false);

        mediumOfPaymentOptions = JsfUtil.createSelectItems(true, _MediumOfPayment.CASH, _MediumOfPayment.CHEQUE, _MediumOfPayment.BANKERS_DRAFT);

        occupantTypeOptions = JsfUtil.createSelectItems(OccupantType.list(), true);

        propertyOccupiedAsOptions = JsfUtil.createSelectItems(PropOccupationType.list(), true);

        paymentTypeOptions = JsfUtil.createSelectItems(PaymentType.values(), false);

        calenderMonthNamesOptions = JsfUtil.createSelectItems(DateTimeUtils.getMonthNames(), true);
//        List<Estate> estatesList = new ArrayList<>(ds.getCommonQry().estateGetAll(true));
//        estateOptions = JsfUtil.createSelectItems(estatesList, true);

    }

    public SelectItem[] getEstateOptions() {
        int count = 1;
        try {
            List<Estate> estatesList = new ArrayList<>(ds.getCommonQry().estateGetAlll());
            estateOptions = new SelectItem[estatesList.size() + 1];
            estateOptions[0] = new SelectItem(null, "--Select Estate--");
            for (Estate e : estatesList) {
                estateOptions[count] = new SelectItem(e, e.getEstateName());
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return estateOptions;
    }

    public void setEstateOptions(SelectItem[] estateOptions) {
        CommonOptions.estateOptions = estateOptions;
    }

    public SelectItem[] getRelationshipOptions() {
        return relationshipOptions;
    }

    public void setRelationshipOptions(SelectItem[] relationshipOptions) {
        this.relationshipOptions = relationshipOptions;
    }

    public SelectItem[] getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(SelectItem[] propertyUsage) {
        CommonOptions.propertyUsage = propertyUsage;
    }

    public SelectItem[] getNationalIdTypeOptions() {
        return nationalIdTypeOptions;
    }

    public void setNationalIdTypeOptions(SelectItem[] nationalIdTypeOptions) {
        this.nationalIdTypeOptions = nationalIdTypeOptions;
    }

    public SelectItem[] getLandSizeItems() {
        return landSizeItems;
    }

    public void setLandSizeItems(SelectItem[] landSizeItems) {
        this.landSizeItems = landSizeItems;
    }

    public SelectItem[] getGroundRentBillingYears() {
        return groundRentBillingYears;
    }

    public void setGroundRentBillingYears(SelectItem[] groundRentBillingYears) {
        CommonOptions.groundRentBillingYears = groundRentBillingYears;
    }

    public SelectItem[] getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(SelectItem[] propertyCategory) {
        CommonOptions.propertyCategory = propertyCategory;
    }

    public SelectItem[] getReportTypesOptions() {
        return reportTypesOptions;
    }

    public void setReportTypesOptions(SelectItem[] reportTypesOptions) {
        this.reportTypesOptions = reportTypesOptions;
    }

    public SelectItem[] getMediumOfPaymentOptions() {
        return mediumOfPaymentOptions;
    }

    public void setMediumOfPaymentOptions(SelectItem[] mediumOfPaymentOptions) {
        CommonOptions.mediumOfPaymentOptions = mediumOfPaymentOptions;
    }

    public SelectItem[] getOccupantTypeOptions() {
        return occupantTypeOptions;
    }

    public void setOccupantTypeOptions(SelectItem[] occupantTypeOptions) {
        this.occupantTypeOptions = occupantTypeOptions;
    }

    public SelectItem[] getPropertyOccupiedAsOptions() {
        return propertyOccupiedAsOptions;
    }

    public void setPropertyOccupiedAsOptions(SelectItem[] propertyOccupiedAsOptions) {
        this.propertyOccupiedAsOptions = propertyOccupiedAsOptions;
    }

    public SelectItem[] getPaymentTypeOptions() {
        return paymentTypeOptions;
    }

    public void setPaymentTypeOptions(SelectItem[] paymentTypeOptions) {
        this.paymentTypeOptions = paymentTypeOptions;
    }

    public SelectItem[] getCalenderMonthNamesOptions() {
        return calenderMonthNamesOptions;
    }

    public void setCalenderMonthNamesOptions(SelectItem[] calenderMonthNamesOptions) {
        this.calenderMonthNamesOptions = calenderMonthNamesOptions;
    }

    public SelectItem[] getGenderOptions() {
        return genderOptions;
    }

    public void setGenderOptions(SelectItem[] genderOptions) {
        this.genderOptions = genderOptions;
    }

    public SelectItem[] getEstateClassOptions() {
//        int count = 1;
//        try {
////            List<EstateClass> listOfEstateClass = ds.getCommonQry().estateClassGetAll();
////            estateClassOptions = new SelectItem[listOfEstateClass.size() + 1];
////            estateClassOptions[0] = new SelectItem(null, "--Select Class--");
////            for (EstateClass ec : listOfEstateClass) {
////                estateClassOptions[count] = new SelectItem(ec, ec.getCategory());
////                count++;
////            }
//         } catch (Exception e) {
//            LOG.log(Level.SEVERE, e.toString(), e);
//        }
        return estateClassOptions;
    }

    public void setEstateClassOptions(SelectItem[] estateClassOptions) {
        CommonOptions.estateClassOptions = estateClassOptions;
    }

    public SelectItem[] getStatusOptions() {
        int count = 1;
        try {
            List<Status> listOfStatus = ds.getCommonQry().statusGetAll();
            statusOptions = new SelectItem[listOfStatus.size() + 1];
            statusOptions[0] = new SelectItem(null, "--Select Status--");
            for (Status s : listOfStatus) {
                statusOptions[count] = new SelectItem(s, s.getStatus());
                count++;
            }
        } catch (Exception e) {
        }
        return statusOptions;
    }

    public void setStatusOptions(SelectItem[] statusOptions) {
        this.statusOptions = statusOptions;
    }

    public SelectItem[] getUserAccessRightsOptions() {
        int count = 1;
        try {
            List<UserRole> listOfRoles = ds.getCommonQry().getUSerRoleAll();
            userAccessRightsOptions = new SelectItem[listOfRoles.size() + 1];
            userAccessRightsOptions[0] = new SelectItem(null, "--Select Access Right--");
            for (UserRole role : listOfRoles) {
                userAccessRightsOptions[count] = new SelectItem(role, role.getRoleName());
                count++;
            }
        } catch (Exception e) {
        }

        return userAccessRightsOptions;
    }

    public void setUserAccessRightsOptions(SelectItem[] userAccessRightsOptions) {
        this.userAccessRightsOptions = userAccessRightsOptions;
    }

    public SelectItem[] getContactGroupOptions() {
        contactGroupOptions = JsfUtil.createSelectItems(ContactGroup.values(), true);
        return contactGroupOptions;
    }

    public void setContactGroupOptions(SelectItem[] contactGroupOptions) {
        this.contactGroupOptions = contactGroupOptions;
    }

    /**
     * Presents a drop down of all active staffs
     * @return selectItem object of active staffs for drop down population
     */
    public SelectItem[] getActiveStaffNames() {
        int count = 1;
        try {
            List<Staff> staffsList = ds.getCustomQry().getActiveStaffs();
            activeStaffNames = new SelectItem[staffsList.size() + 1];
            activeStaffNames[0] = new SelectItem(null, "-- Select Staff Name --");
            for(Staff staff : staffsList){
                activeStaffNames[count] = new SelectItem(staff.getStaffId(), staff.getFullName());
                count++;
            }
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
        return activeStaffNames;
    }

    public void setActiveStaffNames(SelectItem[] activeStaffNames) {
        this.activeStaffNames = activeStaffNames;
    }

    public SelectItem[] getBlkFilters() {
        try {
            blkFilters = new SelectItem[5];
            blkFilters[0] = new SelectItem(null, "-- Sort By --");
            blkFilters[1] = new SelectItem("A-Z", "A-Z", "Sort list in asceding order of the alphabets",  false, false, false);
            blkFilters[2] = new SelectItem("Z-A", "Z-A", "Sort list in descending order of the alphabets",  false, false, false);
            blkFilters[3] = new SelectItem("Date", "Date", "Sort list According to date",  false, false, false);
            blkFilters[4] = new SelectItem("Property", "Property", "Sort list in order of the biggest number of properties",  false, false, false);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
        return blkFilters;
    }

    public void setBlkFilters(SelectItem[] blkFilters) {
        this.blkFilters = blkFilters;
    }

}
