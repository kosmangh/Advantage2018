/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.ejb.entities.*;
import com.sabonay.common.api.security.UserAccountUtil;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.utils.GenUUID;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edwin / Ritchid
 */
public class IDCreator {

    private static String id = "";
    
    //<editor-fold defaultstate="collapsed" desc="Estates">
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Estate Blocks">
    public static void setEstateBlockId(EstateBlock estateBlock) {
        try {
            if (estateBlock.getBlockName() == null) {
                JsfUtil.addErrorMessage("Please enter block name");
                JsfUtil.getFacesContext().renderResponse();
            } else{
                id = estateBlock.getEstate().getEstateId() + "/" + estateBlock.getBlockName().toUpperCase();
                estateBlock.setEstateBlockId(id);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            JsfUtil.addErrorMessage(e.toString());
            JsfUtil.getFacesContext().renderResponse();
        }
    }
    //</editor-fold>

    

    public static void setPropertyOccupantId(PropertyOccupant propertyOccupant) {
        id = UserAccountUtil.createDate_uuidPart(propertyOccupant.getOccupantName());
        id = id + "-" + UserAccountUtil.createStringToken(propertyOccupant.getOccupantType().name());
        propertyOccupant.setPropertyOccupantId(id);

    }

    public static void setEstatePropertyId(EstateProperty estateProperty) {
        id = estateProperty.getEstateBlock().getEstateBlockId() + "." + estateProperty.getPropertyNumber();

        estateProperty.setEstatePropertyId(id.toUpperCase());
    }

    public static void setOccupantPropertyId(Occupancy occupantProperty) {
        id = occupantProperty.getEstateProperty().getEstatePropertyId()
                + "#" + occupantProperty.getPropertyOccupant().getPropertyOccupantId();

        occupantProperty.setOccupantPropertyId(id);
    }

    public static void setEstateLedgerId(EstatePropertyLedger estatePropertyLedger, String month) {
        if (estatePropertyLedger.getDebitCreditEntry() == null) {
            return;
        }
        if (estatePropertyLedger.getEstateProperty() == null) {
            return;
        }

        if (estatePropertyLedger.getLedgerYear() == null) {
            return;
        }

        if (estatePropertyLedger.getLedgerYear() < 1979) {
            return;
        }


        System.out.println("in idsetter := " + estatePropertyLedger.getEstateProperty() + "  "
                + " po: " + estatePropertyLedger.getPropertyOccupant() + "  "
                + estatePropertyLedger.getPaymentType());

        id = estatePropertyLedger.getEstateProperty().getEstatePropertyId() + "/"
                + estatePropertyLedger.getPaymentType().getInitials() + "-"
                + estatePropertyLedger.getDebitCreditEntry().getInitials() + "/"
                + estatePropertyLedger.getLedgerYear();

        if (estatePropertyLedger.getPaymentType() == PaymentType.GROUND_RENT) {
            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
            }else if(estatePropertyLedger.getReceiptNumberIssued() != null && estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT){
                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
            }
        } else if (estatePropertyLedger.getPaymentType() == PaymentType.HOUSE_RENT) {
            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT) {
                if (month == null) {
                    throw new RuntimeException("Month cannot be null");
                }

                id = id + "#" + month;
            } else if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
            }
        }

        estatePropertyLedger.setEstatePropertyLedgerId(id);
    }
    
    
    
//      public static void setBillId(Bills estatePropertyLedger, String month) {
//        if (estatePropertyLedger.getDebitCreditEntry() == null) {
//            return;
//        }
//        if (estatePropertyLedger.getEstateProperty() == null) {
//            return;
//        }
//
//        if (estatePropertyLedger.getLedgerYear() == null) {
//            return;
//        }
//
//        if (estatePropertyLedger.getLedgerYear() < 1979) {
//            return;
//        }
//
//
//        System.out.println("in idsetter := " + estatePropertyLedger.getEstateProperty() + "  "
//                + " po: " + estatePropertyLedger.getPropertyOccupant() + "  "
//                + estatePropertyLedger.getPaymentType());
//
//        id = estatePropertyLedger.getEstateProperti().getEstatePropertyId() + "/"
//                + estatePropertyLedger.getPaymentTyp().getInitials() + "-"
//                + estatePropertyLedger.getDebitCreditEntry().getInitials() + "/"
//                + estatePropertyLedger.getLedgerYear();
//
//        if (estatePropertyLedger.getPaymentTyp() == PaymentType.GROUND_RENT) {
//            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
//                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
//            }else if(estatePropertyLedger.getReceiptNumberIssued() != null && estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT){
//                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
//            }
//        } else if (estatePropertyLedger.getPaymentTyp() == PaymentType.HOUSE_RENT) {
//            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT) {
//                if (month == null) {
//                    throw new RuntimeException("Month cannot be null");
//                }
//
//                id = id + "#" + month;
//            } else if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
//                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
//            }
//        }
//
//        estatePropertyLedger.setEstatePropertyLedgerId(id);
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static String setEstateLedgerId1(EstatePropertyLedger estatePropertyLedger, String month) {
        if (estatePropertyLedger.getDebitCreditEntry() == null) {
            return null;
        }
        if (estatePropertyLedger.getEstateProperty() == null) {
            return null;
        }

        if (estatePropertyLedger.getLedgerYear() == null) {
            return null;
        }

        if (estatePropertyLedger.getLedgerYear() < 1979) {
            return null;
        }


        System.out.println("in idsetter := " + estatePropertyLedger.getEstateProperty() + "  "
                + " po: " + estatePropertyLedger.getPropertyOccupant() + "  "
                + estatePropertyLedger.getPaymentType());

        id = estatePropertyLedger.getEstateProperty().getEstatePropertyId() + "/"
                + estatePropertyLedger.getPaymentType().getInitials() + "-"
                + estatePropertyLedger.getDebitCreditEntry().getInitials() + "/"
                + estatePropertyLedger.getLedgerYear();

        if (estatePropertyLedger.getPaymentType() == PaymentType.GROUND_RENT) {
            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
            }
        } else if (estatePropertyLedger.getPaymentType() == PaymentType.HOUSE_RENT) {
            if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.DEBIT) {
                if (month == null) {
                    throw new RuntimeException("Month cannot be null");
                }

                id = id + "#" + month;
            } else if (estatePropertyLedger.getDebitCreditEntry() == DebitCredit.CREDIT) {
                id += "/" + "{" + estatePropertyLedger.getReceiptNumberIssued() + "}";
            }
        }

       return id;
    }

    public static void setGroundRentId(GroundRent groundRent) {
        id = groundRent.getYearDue() + "#" + groundRent.getPropertyUsage();

        groundRent.setGroundRentId(id);

    }

//    public static void setHouseRentId(HouseRent houseRent){
//        id = houseRent.getMonthDue().substring(0, 2) + "#" +  houseRent.getPropertyUsage();
//        houseRent.setHouseRentId(id);
//    }
    public static void setArreasRecordId(ArreasRecord arreasRecord) {
        id = arreasRecord.getEstateProperty().getEstatePropertyId();

        arreasRecord.setArreasRecordId(id);
    }

    public static void setStaffId(Staff staff) {
        id = UserAccountUtil.createDate_uuidPart(staff.toString());
        staff.setStaffId(id);
    }

    public static void setUserAccountId(UserAccount userAccount) {
        id = userAccount.getUsername() + userAccount.getUserPassword();
        id = GenUUID.getEnterUUIDPK(id);
        userAccount.setUserAccountId(id);
    }

    public static void setEstateId(Estate estate) {
    }

    public static String generateUsername(String surname, String othername) {

        String initials = "";

        String[] splitOname = othername.split(" ");

        List<String> splittedName = new ArrayList<String>();

        for (String s : splitOname) {
            if (!s.equals("")) {
                splittedName.add(s);
            }
        }

        for (String s : splittedName) {
            initials += s.substring(0, 1).toLowerCase();
        }

        return (initials.toLowerCase() + surname.toLowerCase());
    }
    
    public static int setBillNumber(){
        int rand = 0;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            rand = random.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rand;
    }
    
}
