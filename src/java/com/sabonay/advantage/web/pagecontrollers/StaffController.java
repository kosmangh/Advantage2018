package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Staff;
import com.sabonay.advantage.ejb.entities.Status;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "staffController")
@RequestScoped
public class StaffController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Variables">
        private Staff staff;
        private Staff edStaff;
        
        private String searchCriteria;
        private String searchText;
        private String totalStaffs;
        
        private DataModel<Staff> staffTableModel;
        
        private List<Staff> staffList;
        
        private Date currentDate;
    
        private static int tabIndex;

        @Inject
        AdvantageUserData userAccount;
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Initiators">
         public StaffController() {
             staff = new Staff();
            edStaff = new Staff();
            currentDate = new Date();
//            tabIndex = 0;
        }

        @PostConstruct
        private void init() {
            GetStaffList();
        }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="System Logics">
        public void toStaffslist(){
            tabIndex = 0;
        }

        public void toNewStaff(){
            staff = new Staff();
            tabIndex = 1;
        }

        public void toEditStaffs(){
            tabIndex = 2;
        }

        public void newBackAction(){
            staff = new Staff();
            toStaffslist();
        }

        public void edBackAction(){
            edStaff = new Staff();
            toStaffslist();
        }

        public void selectRow() {
            try {
                edStaff = (Staff) staffTableModel.getRowData();
                toEditStaffs();

            } catch (Exception exp) {
                Logger.getLogger(StaffController.class.getName()).log(Level.SEVERE, exp.toString(), exp);
                JsfUtil.addErrorMessage(null, "Error", "Error occurred in selecting Staff from table ");
            }
        }

        public void refreshList(){
            searchText = "";
            searchCriteria = null;
            GetStaffList();
            toStaffslist();

        }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Business Loics">
        public void GetStaffList(){
            staffList = ds.getCommonQry().staffGetAll(false);
            staffTableModel = new ListDataModel<>(staffList);
            totalStaffs = String.valueOf(staffList.size());
        }
        
        public void searchStaff() {
            toStaffslist();
            try {
                if(!"".equals(searchText)){
                    staffList = ds.getCommonQry().staffFindByAttribute(searchCriteria, searchText, "STRING", true);
                    staffTableModel = new ListDataModel<>(staffList);
                }
            } catch (Exception exp) {
                Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, exp.getMessage(), exp);
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }
        }
        
        public void deleteStaff() {
            toStaffslist();
            try {
                List<Staff> selectedStaffs = new ArrayList<>();
                for(Staff staff : staffList){
                    if(staff.getSelected() == true)
                        selectedStaffs.add(staff);
                }
            
                if(selectedStaffs.isEmpty()){
                    JsfUtil.addErrorMessage(null, "No Staff(s) Selected", "No staff has been selected from list");

                } else{
                    List<Staff> deletingStaffs = new ArrayList<>();
                    for(Staff s : selectedStaffs){
                        if(s.getUserAccount() != null){
                            JsfUtil.addErrorMessage(null, "Error", s.getFullName()+" is having a User Account which is required to be deleted before removing staff");
                            break;
                        } else {
                            s.setLastModifiedBy(userAccount.getCurrentUser().getUserAccountId());
                            s.setLastModifiedDate(new Date());
                            deletingStaffs.add(s);
                        }                    
                    }
                
                    if(!deletingStaffs.isEmpty()){
                        boolean deleted = ds.getCommonQry().batchStaffDelete(deletingStaffs, false);

                        if (deleted == true) {
                            refreshList();
                            JsfUtil.addInformationMessage("Staff(s) Deleted Succcessfully");

                        } else {
                            refreshList();
                            JsfUtil.addErrorMessage(null, "Error", "Failed to Delete selected Staff from the list. Try again later");
                        }
                    }
                }
            
            } catch (Exception exp) {
                Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, exp.getMessage(), exp);
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }
        }
        
        public void saveStaff() {
            tabIndex = 1;
            try {
                Status status = new Status(Boolean.TRUE);
                IDCreator.setStaffId(staff);
                staff.setStatus(status);
                staff.setLastModifiedBy(userAccount.getCurrentUser().getStaff().getFullName());
                staff.setLastModifiedDate(new Date());
                boolean saved = ds.getCommonQry().staffCreate(staff);

                if (saved == true) {
                    toNewStaff();
                    GetStaffList();
                    JsfUtil.addInformationMessage("Staff created sucessfully ");
                    
                } else {
                    JsfUtil.addErrorMessage(null, "Failed", "Failed to Create new Staff");
                }

            } catch (Exception exp) {
                Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, exp.toString(), exp);
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }

        }
        
        public void editStaff() {
            toEditStaffs();
            try {
                edStaff.setUpdated(true);
                edStaff.setLastModifiedBy(userAccount.getCurrentUser().getUserAccountId());
                edStaff.setLastModifiedDate(new Date());
                boolean updated = ds.getCommonQry().staffUpdate(edStaff);

                if (updated == true) {
                    GetStaffList();
                    JsfUtil.addInformationMessage("Staff updated sucessfully ");

                } else if (updated == false) {
                    JsfUtil.addErrorMessage(null,"Error", "Failed to Update Staff details. Try again later");
                }
            } catch (Exception e) {
                Logger.getLogger(StaffController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                JsfUtil.addErrorMessage(null, "System Error", "System encountered an error. Please contact system administrator for solutions");
            }
        }
    //</editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Staff getEdStaff() {
        return edStaff;
    }

    public void setEdStaff(Staff edStaff) {
        this.edStaff = edStaff;
    }

    public String getTotalStaffs() {
        return totalStaffs;
    }

    public void setTotalStaffs(String totalStaffs) {
        this.totalStaffs = totalStaffs;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        StaffController.tabIndex = tabIndex;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public DataModel<Staff> getStaffTableModel() {
        return staffTableModel;
    }

    public void setStaffTableModel(DataModel<Staff> staffTableModel) {
        this.staffTableModel = staffTableModel;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    // </editor-fold>
}
