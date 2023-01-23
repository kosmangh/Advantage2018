/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.routesms.smshttpapi.DeliveryReport;
import com.routesms.smshttpapi.HTTPTextMessage;
import com.routesms.smshttpapi.MessageType;
import com.sabonay.advantage.common.constants.ContactGroup;
import com.sabonay.advantage.common.constants.SMSContact;
import com.sabonay.advantage.ejb.entities.BindProperties;
import com.sabonay.advantage.ejb.entities.CreditBalance;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.web.sms.utils.GroundRentProcessor;
import com.sabonay.advantage.ejb.entities.SmessMessage;
import com.sabonay.advantage.modules.account.AdvantageUserData;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.collection.comparators.StringValueComparator;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.common.utils.MessagingUtils;
import com.sabonay.communication.center.client.ClientResponse;
import com.sabonay.communication.center.client.ScomcenterClient;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author crash
 */
@SessionScoped
@Named(value = "smsController")
public class SMSController implements Serializable {

    private List<EstateProperty> estatePropertiesList = null;
    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;
    private EstateProperty selectedEstateProperty;
    private String estatePropertyNumber;
    private SelectItem[] selectedEstateBlocksOptions;
    private SelectItem[] selectedEstatePropertiesOptions;
    private HTTPTextMessage textMessage;
    private String server;
    private int port;
    private String username;
    private String password;
    private final String SENDER_ID = "SHC KUSMASI";
    private boolean canSend = false;
    private SmessMessage sm = new SmessMessage();
    private Double creditRemaining = 0.0;
    private Double amountToBeCharge = 0.0;
    private Integer numberOfPages;
    private Integer numberOfContacts;
    private String contactGrpValue;
    private String contactGrpValue1;
    private ContactGroup contactGroup;
    private boolean renderBlocks = false;
    private boolean renderPropertyNumber = false;
    private boolean renderEstate = false;
    private boolean rendered = false;
    private List<SMSContact> occupantContacts;
    private List<String> listOfOccupantContacts;
    private List<String> listOfEstatePropertyIds;
    private int baseYear = DateTimeUtils.getCurrentYear();
    private AdvantageUserData userData;
    private boolean renderCheckBill = false;
    private boolean renderCheckReminder = false;
    private Double creditLeft = 0.0;
    private CreditBalance cb = new CreditBalance();
    
    private ScomcenterClient scomcenterClient = new ScomcenterClient();

    public SMSController() {
    }

    @PostConstruct
    public void init() {
        
        
        try {
            List<BindProperties> listOfBindProps = ds.getCommonQry().bindPropertiesGetAll();
            server = listOfBindProps.get(0).getSmppHost();
            port = listOfBindProps.get(0).getSmppPort();
            username = listOfBindProps.get(0).getSmppUser();
            password = listOfBindProps.get(0).getSmppPwd();
            textMessage = new HTTPTextMessage(server, port, username, password, SENDER_ID, MessageType.PLAIN_TEXT_GSM_ENCODING, DeliveryReport.DLR_REQUIRED);
        } catch (Exception e) {
        }
    }

    private List<String> sendToWhich() {
        try {

            if (contactGroup == ContactGroup.ALL_OCCUPANTS) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (contactGroup == ContactGroup.ESTATE) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (contactGroup == ContactGroup.BLOCK) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, contactGrpValue1));

            }
            if (contactGroup == ContactGroup.INDIVIDUAL) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (occupantContacts.isEmpty()) {
                JsfUtil.addErrorMessage("No contacts found");
            } else {
                listOfOccupantContacts = new ArrayList<>(occupantContacts.size());
                for (SMSContact contacts : occupantContacts) {
                    listOfOccupantContacts.add(contacts.getOccupantContactNumber());
                }
            }
            System.out.println("listOfContacts " + listOfOccupantContacts);
            return listOfOccupantContacts;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> estatePropertyIds() {
        try {
            if (contactGroup == ContactGroup.ALL_OCCUPANTS) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (contactGroup == ContactGroup.ESTATE) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (contactGroup == ContactGroup.BLOCK) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, contactGrpValue1));

            }
            if (contactGroup == ContactGroup.INDIVIDUAL) {
                occupantContacts = new ArrayList<>(ds.getCustomQry().getOccupantContacts(contactGroup, contactGrpValue, null));

            }
            if (occupantContacts.isEmpty()) {
                JsfUtil.addErrorMessage("No contacts found");
            } else {
                listOfEstatePropertyIds = new ArrayList<>(occupantContacts.size());
                for (SMSContact contacts : occupantContacts) {
                    listOfEstatePropertyIds.add(contacts.getEstateProperty());
                }
            }
            return listOfEstatePropertyIds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadOccupantContants() {
        String contacts = null;
        StringBuilder sb = new StringBuilder();
        try {
            List<String> contactList = sendToWhich();
            for (String numbers : contactList) {
                sb.append(MessagingUtils.parseAndValidatePhoneString(numbers, MessagingUtils.SMPP_COUNTRY_CODE_GHA));
            }
            contacts = sb.toString();
            return contacts;
        } catch (Exception e) {
        }
        return null;
    }

    private Integer numberOfOccupantContacts() {
        String numbers;
        List<String> list = new ArrayList<>();

        int totalContacts = 0;
        try {
            if (loadOccupantContants().isEmpty() || loadOccupantContants() == null) {
                JsfUtil.addErrorMessage("No valid contact found");
                totalContacts = 0;
            } else {
                numbers = loadOccupantContants();
                list.addAll(Arrays.asList(numbers.split(",")));
                totalContacts = list.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalContacts;
    }

    private void checkReminderSMSSending() {
        try {
            numberOfContacts = numberOfOccupantContacts();
            if(numberOfContacts == 0){
                renderCheckReminder = false;
            }else{
                renderCheckReminder = true;
                cb = ds.getCommonQry().creditBalanceGetAll().get(0);
                creditLeft = cb.getCreditLeft();
                amountToBeCharge = numberOfContacts * numberOfPages * 0.04;
                creditRemaining = creditLeft - amountToBeCharge;
                canSend = (creditRemaining < 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBillSMSSending() {
        try {
            numberOfContacts = numberOfOccupantContacts();
            if(numberOfContacts == 0){
                renderCheckBill = false;
            }else{
                renderCheckBill = true;
                cb = ds.getCommonQry().creditBalanceGetAll().get(0);
                creditLeft = cb.getCreditLeft();
                amountToBeCharge = numberOfContacts * numberOfPages * 0.04;
                creditRemaining = creditLeft - amountToBeCharge;
                canSend = (creditRemaining < 0);  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void prepReminderSMS() {
        try {
            numberOfPages = MessagingUtils.computeNumberOfPages(sm.getMessageBody(), false);
            checkReminderSMSSending();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void prepBillSMS() {
        try {
            String estatePropertyId = estatePropertyIds().get(0);
            String billSMS = GroundRentProcessor.prepareGroundRentSMS(estatePropertyId, baseYear);
            numberOfPages = MessagingUtils.computeNumberOfPages(billSMS, false);
            checkBillSMSSending();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendGrndRentSMS() {
        String deliveryReport = "";
        try {
            List<String> listOfPptIds = estatePropertyIds();
            for (String id : listOfPptIds) {
                occupantContacts = ds.getCustomQry().getOccupantContacts(ContactGroup.INDIVIDUAL, id, null);
                for (SMSContact contact : occupantContacts) {
                    String bill = GroundRentProcessor.prepareGroundRentSMS(id, baseYear);
                    System.out.println("GroundRent Report " + bill);
                    String number = MessagingUtils.parseAndValidatePhoneString(contact.getOccupantContactNumber(), MessagingUtils.SMPP_COUNTRY_CODE_GHA);
                    deliveryReport = textMessage.sendSMS(bill, number);
                    System.out.println("delivery Report " + deliveryReport);
                }
            }
            //create message details
            sm.setContactGroup(contactGroup.toString());
            sm.setContactGroupValue(contactGrpValue);
            sm.setDateOfDispatch(new Date());
            sm.setDateOfCompose(new Date());
            sm.setStatus("Sent");
            sm.setTitle("Ground Rent Report");
            sm.setNumberOfPeopleToSendTo(numberOfContacts);
            ds.getCommonQry().smessMessageCreate(sm);

            //update credit balance
            cb.setCreditLeft(creditRemaining);
            ds.getCommonQry().creditBalanceUpdate(cb);

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Failed to send groundrent bill");
            e.printStackTrace();
        }
        JsfUtil.addInformationMessage("GroundRent bill sent successfully");
        return "";
    }

    public String sendReminder() {
        try {
            if(testInternet("www.google.com")){
                
                
                ClientResponse clientResponse = scomcenterClient.sendMessage
        (loadOccupantContants(), sm.getMessageBody(), "ADVANTAGE");
                
                if(clientResponse.isSent()){
                      JsfUtil.addInformationMessage("Message sent successfully");
                }else{
                    JsfUtil.addInformationMessage("Message Not Sent, Please Try Again");
                }
             
                
                System.out.println("clientResponse " + clientResponse.getMessage());
                
              
               
                
//            String deliveryReport = textMessage.sendSMS(sm.getMessageBody(), loadOccupantContants());
//            if (deliveryReport.trim().substring(0, 4).equalsIgnoreCase("1701")) {
//                //if the message is sent, then create message details
//                sm.setContactGroup(contactGroup.toString());
//                sm.setContactGroupValue(contactGrpValue);
//                sm.setDateOfDispatch(new Date());
//                sm.setDateOfCompose(new Date());
//                sm.setMessageBody(sm.getMessageBody());
//                sm.setTitle(sm.getTitle());
//                sm.setStatus("Sent");
//                sm.setNumberOfPeopleToSendTo(numberOfContacts);
//                ds.getCommonQry().smessMessageCreate(sm);
//                //update credit balance when message is sent
//                cb.setCreditLeft(creditRemaining);
//                ds.getCommonQry().creditBalanceUpdate(cb);
//
//                JsfUtil.addInformationMessage("Message sent successfully");
//            } else {
//                JsfUtil.addErrorMessage("Message not sent");
//            }
        }else{
              JsfUtil.addErrorMessage("No Internet Connection");
            }
                
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
        
    }

    public String saveAsTemplateBtnAction() {
        try {
//            sm.setTemplate(true);
//            sm.setDateOfCompose(new Date());
//            ds.getCommonQry().smessMessageCreate(sm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String loadMessageTemplates() {
        return "";
    }

    public void estateChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            selectedEstate = (Estate) changeEvent.getNewValue();
            contactGrpValue = selectedEstate.getEstateName();
            contactGrpValue1 = selectedEstate.getEstateName();

            selectedEstateBlocksOptions = JsfUtil.createSelectItems(selectedEstate.getEstateBlockList(), true);

            estatePropertiesList = null;
        }
    }

    public String getSelectedEstateId() {
        if (selectedEstate != null) {
            return selectedEstate.getEstateId();
        }

        return null;
    }

    public void estateBlockPropertyChangeListener(ValueChangeEvent changeEvent) {
        if (changeEvent.getNewValue() != null) {
            try {

                selectedEstatePropertiesOptions = null;
                estatePropertiesList = null;

                selectedEstateBlock = (EstateBlock) changeEvent.getNewValue();

                if (selectedEstateBlock == null) {
                    JsfUtil.addErrorMessage("Please select an Estate Block");
                    return;
                }
                contactGrpValue = selectedEstateBlock.getBlockName();
                estatePropertiesList = ds.getCustomQry().findEstatePropertiesByBlock(selectedEstateBlock.getEstateBlockId());
                StringValueComparator.sort(estatePropertiesList);
                selectedEstatePropertiesOptions
                        = JsfUtil.createSelectItems(estatePropertiesList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void estatePropertyListener(ValueChangeEvent vce) {

        try {
            if (vce.getNewValue() != null) {
                selectedEstateProperty = (EstateProperty) vce.getNewValue();
                contactGrpValue = selectedEstateProperty.getEstatePropertyId();
            }
        } catch (Exception e) {
        }
    }

    public void selectContactGroupChangeEvent(ValueChangeEvent vce) {
        try {
            if (vce.getNewValue() != null) {
                contactGroup = (ContactGroup) vce.getNewValue();
                if (contactGroup == null) {
                    JsfUtil.addErrorMessage("Please select a contact group");
                    return;
                }
                if (contactGroup == ContactGroup.ALL_OCCUPANTS) {
                    rendered = false;
                    renderBlocks = false;
                    renderEstate = false;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.ESTATE) {
                    rendered = true;
                    renderEstate = true;
                    renderBlocks = false;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.BLOCK) {
                    renderBlocks = true;
                    renderEstate = true;
                    rendered = true;
                    renderPropertyNumber = false;

                }
                if (contactGroup == ContactGroup.INDIVIDUAL) {
                    renderBlocks = true;
                    renderEstate = true;
                    rendered = true;
                    renderPropertyNumber = true;

                }

            }
        } catch (Exception e) {
        }
    }
    
    
    public static boolean testInternet(String site){
        Socket socket = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress(site, 80);
        try {
            socket.connect(socketAddress);
            System.out.println("online");
            return true;
        } catch (IOException iOException) {
            System.out.println("offline");
            return false;
        }
        finally{
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public boolean isCanSend() {
        return canSend;
    }

    public void setCanSend(boolean canSend) {
        this.canSend = canSend;
    }

    public SmessMessage getSm() {
        return sm;
    }

    public void setSm(SmessMessage sm) {
        this.sm = sm;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Integer getNumberOfContacts() {
        return numberOfContacts;
    }

    public void setNumberOfContacts(Integer numberOfContacts) {
        this.numberOfContacts = numberOfContacts;
    }

    public Double getAmountToBeCharge() {
        return amountToBeCharge;
    }

    public void setAmountToBeCharge(Double amountToBeCharge) {
        this.amountToBeCharge = amountToBeCharge;
    }

    public String getContactGrpValue() {
        return contactGrpValue;
    }

    public void setContactGrpValue(String contactGrpValue) {
        this.contactGrpValue = contactGrpValue;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    public boolean isRenderBlocks() {
        return renderBlocks;
    }

    public void setRenderBlocks(boolean renderBlocks) {
        this.renderBlocks = renderBlocks;
    }

    public boolean isRenderPropertyNumber() {
        return renderPropertyNumber;
    }

    public void setRenderPropertyNumber(boolean renderPropertyNumber) {
        this.renderPropertyNumber = renderPropertyNumber;
    }

    public boolean isRenderEstate() {
        return renderEstate;
    }

    public void setRenderEstate(boolean renderEstate) {
        this.renderEstate = renderEstate;
    }

    public boolean isRenderCheckBill() {
        return renderCheckBill;
    }

    public void setRenderCheckBill(boolean renderCheckBill) {
        this.renderCheckBill = renderCheckBill;
    }

    public boolean isRenderCheckReminder() {
        return renderCheckReminder;
    }

    public void setRenderCheckReminder(boolean renderCheckReminder) {
        this.renderCheckReminder = renderCheckReminder;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public EstateBlock getSelectedEstateBlock() {
        return selectedEstateBlock;
    }

    public void setSelectedEstateBlock(EstateBlock selectedEstateBlock) {
        this.selectedEstateBlock = selectedEstateBlock;
    }

    public List<SMSContact> getOccupantContacts() {
        return occupantContacts;
    }

    public void setOccupantContacts(List<SMSContact> occupantContacts) {
        this.occupantContacts = occupantContacts;
    }

    public SelectItem[] getSelectedEstateBlocksOptions() {
        return selectedEstateBlocksOptions;
    }

    public void setSelectedEstateBlocksOptions(SelectItem[] selectedEstateBlocksOptions) {
        this.selectedEstateBlocksOptions = selectedEstateBlocksOptions;
    }

    public SelectItem[] getSelectedEstatePropertiesOptions() {
        return selectedEstatePropertiesOptions;
    }

    public void setSelectedEstatePropertiesOptions(SelectItem[] selectedEstatePropertiesOptions) {
        this.selectedEstatePropertiesOptions = selectedEstatePropertiesOptions;
    }

    public String getContactGrpValue1() {
        return contactGrpValue1;
    }

    public void setContactGrpValue1(String contactGrpValue1) {
        this.contactGrpValue1 = contactGrpValue1;
    }

    public Double getCreditLeft() {
        return creditLeft;
    }

    public void setCreditLeft(Double creditLeft) {
        this.creditLeft = creditLeft;
    }

    public CreditBalance getCb() {
        return cb;
    }

    public void setCb(CreditBalance cb) {
        this.cb = cb;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
