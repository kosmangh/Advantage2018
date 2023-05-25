package com.sabonay.advantage.ejb.sessionbean;

//SHCCRUDManager

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.ejb.entities.*;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.utils.StringUtil;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * 
 * @author Ritchid
 */
@Stateless
public class SHCCRUDManager implements Serializable {

    @PersistenceContext(unitName = "advantagePU")
    private EntityManager em;
    
    private String currentUserID = "";
    private static final Logger log = Logger.getLogger("crud.sessionbean");
    int year = Calendar.getInstance().get(Calendar.YEAR);
    
    int year_current = Calendar.getInstance().get(Calendar.YEAR);

    public void synchPCnDB(){
        em.flush();
    }
    
    @PreDestroy
    public void destroy() {
        em.flush();
        em.close();
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    // <editor-fold defaultstate="collapsed" desc=" ArreasRecord Persistence Functionalities">
    public String arreasRecordCreate(ArreasRecord arreasRecord) {
        try {

            arreasRecord.setLastModifiedDate(new Date());
            arreasRecord.setLastModifiedBy(currentUserID);
            em.persist(arreasRecord);
            em.flush();
            return arreasRecord.getArreasRecordId();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean arreasRecordDelete(ArreasRecord arreasRecord, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(arreasRecord));
            } else if (permanent == false) {
                arreasRecord.setDeleted("YES");
                arreasRecord.setLastModifiedDate(new Date());
                arreasRecord.setLastModifiedBy(currentUserID);
                em.merge(arreasRecord);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean arreasRecordUpdate(ArreasRecord arreasRecord) {
        try {
            arreasRecord.setUpdated("YES");
            arreasRecord.setLastModifiedDate(new Date());
            arreasRecord.setLastModifiedBy(currentUserID);
            em.merge(arreasRecord);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public ArreasRecord arreasRecordFind(String arreasRecordId) {
        try {

            ArreasRecord arreasRecord = em.find(ArreasRecord.class, arreasRecordId);
            return arreasRecord;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<ArreasRecord> arreasRecordFindByAttribute(String arreasRecordAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<ArreasRecord> listOfArreasRecord = null;

        String qryString = "";

        qryString = "SELECT e FROM ArreasRecord e ";
        qryString += "WHERE e." + arreasRecordAttribute + " =:arreasRecordAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfArreasRecord = (List<ArreasRecord>) em.createQuery(qryString).setParameter("arreasRecordAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM ArreasRecord e ";
                qryString += "WHERE e." + arreasRecordAttribute + " LIKE '%" + attributeValue + "%'";
                listOfArreasRecord = (List<ArreasRecord>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfArreasRecord = (List<ArreasRecord>) em.createQuery(qryString).setParameter("arreasRecordAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfArreasRecord;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<ArreasRecord>();
    }

    public List<ArreasRecord> arreasRecordGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<ArreasRecord> listOfArreasRecord = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM ArreasRecord e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM ArreasRecord e WHERE e.deleted = 'NO'";
            }

            listOfArreasRecord = (List<ArreasRecord>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfArreasRecord;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<ArreasRecord>();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<ArreasRecord> arreasRecordGetAll(boolean includeLogicallyDeleted) {
        List<ArreasRecord> listOfArreasRecord = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM ArreasRecord e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM ArreasRecord e WHERE e.deleted = 'NO'";
            }

            listOfArreasRecord = (List<ArreasRecord>) em.createQuery(qryString).getResultList();

            return listOfArreasRecord;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<ArreasRecord>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Estate Persistence Functionalities">
    public boolean estateCreate(Estate estate) {
        try {
            em.persist(estate);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean estateDelete(Estate estate, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(estate));
            } else if (permanent == false) {
                estate.setDeleted(true);
                estate.setLastModifiedDate(new Date());
                estate.setLastModifiedBy(currentUserID);
                em.merge(estate);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }
    
    /**
     * This functionality makes user of the estateDelete method for deleting a List<Estate>
     * @param estates the Estates to be deleted
     * @param permanent determines whether to delete Estate object permanently or temporary NB: true - Permanently and false - temporarily
     * @return true if merged/removed successfully or false if otherwise
     */
    public boolean batchEstateDelete(List<Estate> estates, boolean permanent){
        try {
            for(Estate Es : estates){
                estateDelete(Es, permanent);
            }
            return true;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public boolean estateUpdate(Estate estate) {
        try {

            estate.setUpdated(true);
            estate.setLastModifiedDate(new Date());
            estate.setLastModifiedBy(currentUserID);
            em.merge(estate);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public Estate estateFind(String estateId) {
        try {

            Estate estate = em.find(Estate.class, estateId);
            em.refresh(estate);
            return estate;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    /**
     * An interface for finding an estate using it's attribute and the value of that attribute to search
     * @param estateAttribute the attribute/column to query for estate
     * @param attributeValue the value to search from the attribute
     * @param fieldType the type of the attribute eg. Strung,Number etc.
     * @param includeLogicallyDeleted this specify if logically deleted or not
     * @return a List<Estate> containing the results from the query
     */
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Estate> estateFindByAttribute(String estateAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<Estate> listOfEstate = null;

        String qryString;
        qryString = "SELECT e FROM Estate e ";
        qryString += "WHERE e." + estateAttribute + " =:estateAttribute AND e.deleted = "+includeLogicallyDeleted+" ORDER BY e.estateName ASC";

        try {
//            if (includeLogicallyDeleted == true) {
//                qryString += "AND e.deleted = true";
//            } else if (includeLogicallyDeleted == false) {
//                qryString += " AND e.deleted = false";
//            }
            
//            qryString += " ORDER BY e.estateName ASC";
            
            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfEstate = (List<Estate>) em.createQuery(qryString).setParameter("estateAttribute", attributeValue).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM Estate e ";
                qryString += "WHERE e." + estateAttribute + " LIKE '%" +attributeValue+ "%' AND e.deleted = "+includeLogicallyDeleted+" ORDER BY e.estateName ASC";
                
//                if (includeLogicallyDeleted == true) {
//                    qryString += "AND e.deleted = true";
//                } else if (includeLogicallyDeleted == false) {
//                    qryString += " AND e.deleted = false";
//                }
                
//                qryString += " ORDER BY e.estateName ASC";
                
                listOfEstate = (List<Estate>) em.createQuery(qryString).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfEstate = (List<Estate>) em.createQuery(qryString).setParameter("estateAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfEstate;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Estate> estateGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<Estate> listOfEstate = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Estate e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Estate e WHERE e.deleted = 'NO'";
            }

            listOfEstate = (List<Estate>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfEstate;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    /**
     * This retrieves all the available estates either with their deleted column = 'False' or 'True' or all
     * @param includeLogicallyDeleted the logical deleted value of the block thus true or false
     * @return a list of estates after query or null if no block exist;
     */
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Estate> estateGetAll(boolean includeLogicallyDeleted) {
        List<Estate> listOfEstate;
        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Estate e ORDER BY e.estateName ASC";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Estate e WHERE e.deleted = false ORDER BY e.estateName ASC";
            }

            listOfEstate = (List<Estate>) em.createQuery(qryString).getResultList();
            return listOfEstate;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }
    
    
     @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Estate> estateGetAlll() {
        List<Estate> listOfEstate = null;

        String qryString = "";

        try {
        
                qryString = "SELECT e FROM Estate e";
             

            listOfEstate = (List<Estate>) em.createQuery(qryString).getResultList();

            return listOfEstate;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.toString());
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Estate>();
    }
    
    
    
    
    
    

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" EstateBlock Persistence Functionalities">
    
    /**
     * This is a simple functionality that saves an estate's block into the database
     * @param estateBlock the estate block object to persist
     * @return true if persisted successfully or false if otherwise
     */
    public boolean estateBlockCreate(EstateBlock estateBlock) {
        try {
            em.persist(estateBlock);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    /**
     * This functionality deletes an estate's block from the database base on the boolean parameter
     * @param estateBlock the estate block object to remove/merge
     * @param permanent determines whether to delete EstateBlock object permanently or temporary NB: true - Permanently and false - temporarily
     * @return true if merged/removed successfully or false if otherwise
     */
    public boolean estateBlockDelete(EstateBlock estateBlock, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(estateBlock));
                
            } else if (permanent == false) {
                estateBlock.setDeleted(true);
                em.merge(estateBlock);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    /**
     * This functionality makes user of the estateBlockDelete method for deleting a List<EstateBlock>
     * @param Blocks the blocks to be deleted
     * @param permanent determines whether to delete EstateBlock object permanently or temporary NB: true - Permanently and false - temporarily
     * @return true if merged/removed successfully or false if otherwise
     */
    public boolean batchEstateBlockDelete(List<EstateBlock> Blocks, boolean permanent){
        try {
            for(EstateBlock Eb : Blocks){
                estateBlockDelete(Eb, permanent);
            }
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public boolean estateBlockUpdate(EstateBlock estateBlock) {
        try {
            estateBlock.setUpdated(true);
            estateBlock.setLastModifiedDate(new Date());
            estateBlock.setLastModifiedBy(currentUserID);
            em.merge(estateBlock);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public EstateBlock estateBlockFind(String estateBlockId) {
        try {

            EstateBlock estateBlock = em.find(EstateBlock.class, estateBlockId);
            return estateBlock;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateBlock> estateBlockFindByAttribute(String estateBlockAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<EstateBlock> listOfEstateBlock = null;

        String qryString;

        qryString = "SELECT e FROM EstateBlock e ";
        qryString += "WHERE e." + estateBlockAttribute + " =:estateBlockAttribute ";

        try {
            if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = false";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfEstateBlock = (List<EstateBlock>) em.createQuery(qryString).setParameter("estateBlockAttribute", attributeValue).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM EstateBlock e ";
                qryString += "WHERE e." + estateBlockAttribute + " LIKE '%" + attributeValue + "%'";
                
                if (includeLogicallyDeleted == false) {
                    qryString += " AND e.deleted = false";
                }
                
                qryString += " ORDER BY e.estate.estateName ASC";
                
                listOfEstateBlock = (List<EstateBlock>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfEstateBlock = (List<EstateBlock>) em.createQuery(qryString).setParameter("estateBlockAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfEstateBlock;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public final List<EstateBlock> estateBlockGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<EstateBlock> listOfEstateBlock = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstateBlock e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstateBlock e WHERE e.deleted = 'NO'";
            }

            listOfEstateBlock = (List<EstateBlock>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfEstateBlock;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<EstateBlock>();
    }

    /**
     * This retrieves all the available estate's blocks either with their deleted column = 'False' or 'True' or all
     * @param includeLogicallyDeleted the logical deleted value of the block thus true or false
     * @return a list of EstatesBlock after query or null if no block exist;
    */
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateBlock> estateBlockGetAll(boolean includeLogicallyDeleted) {
        List<EstateBlock> listOfEstateBlock ;
        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstateBlock e ORDER BY e.estate.estateName ASC";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstateBlock e WHERE e.deleted = false ORDER BY e.estate.estateName ASC";
            }

            listOfEstateBlock = (List<EstateBlock>) em.createQuery(qryString).getResultList();

            return listOfEstateBlock;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" EstateProperty Persistence Functionalities">
    public boolean estatePropertyCreate(EstateProperty estateProperty) {
        try {
            em.persist(estateProperty);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean estatePropertyDelete(EstateProperty estateProperty, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(estateProperty));
                
            } else if (permanent == false) {
                estateProperty.setDeleted(true);
                estateProperty.setLastModifiedDate(new Date());
                estateProperty.setLastModifiedBy(currentUserID);
                em.merge(estateProperty);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean estatePropertyUpdate(EstateProperty estateProperty) {
        try {
            estateProperty.setUpdated(true);
            em.merge(estateProperty);
            em.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }
    
    /**
     * This functionality makes user of the estatePropertyDelete method for deleting a List<EstateProperty>
     * @param estateProperties the Properties to be deleted
     * @param permanent determines whether to delete EstateProperty object permanently or temporary NB: true - Permanently and false - temporarily
     * @return true if merged/removed successfully or false if otherwise
     */
    public boolean batchEstatePropertyDelete(List<EstateProperty> estateProperties, boolean permanent){
        try {
            for(EstateProperty Ep : estateProperties){
                estatePropertyDelete(Ep, permanent);
            }
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public EstateProperty estatePropertyFind(String estatePropertyId) {
        try {
            EstateProperty estateProperty = em.find(EstateProperty.class, estatePropertyId);
            if (estateProperty != null) {
                em.refresh(estateProperty);
            }
            return estateProperty;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<EstateProperty> estatePropertyFindByAttribute(String estatePropertyAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<EstateProperty> listOfEstateProperty = null;

        String qryString;

        qryString = "SELECT e FROM EstateProperty e ";
        qryString += "WHERE e." + estatePropertyAttribute + " =:estatePropertyAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfEstateProperty = (List<EstateProperty>) em.createQuery(qryString).setParameter("estatePropertyAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM EstateProperty e ";
                qryString += "WHERE e." + estatePropertyAttribute + " LIKE '%" + attributeValue + "%'";
                listOfEstateProperty = (List<EstateProperty>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfEstateProperty = (List<EstateProperty>) em.createQuery(qryString).setParameter("estatePropertyAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfEstateProperty;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> estatePropertyGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<EstateProperty> listOfEstateProperty = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstateProperty e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstateProperty e WHERE e.deleted = 'NO'";
            }

            listOfEstateProperty = (List<EstateProperty>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfEstateProperty;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> estatePropertyGetAll(boolean includeLogicallyDeleted) {
        List<EstateProperty> listOfEstateProperty;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstateProperty e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstateProperty e WHERE e.deleted = false";
            }

            listOfEstateProperty = (List<EstateProperty>) em.createQuery(qryString).getResultList();

            return listOfEstateProperty;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" EstatePropertyLedger Persistence Functionalities">
    public String estatePropertyLedgerCreate(EstatePropertyLedger estatePropertyLedger) {
        try {

            estatePropertyLedger.setDeleted("NO");
            estatePropertyLedger.setUpdated("NO");
            estatePropertyLedger.setLastModifiedDate(new Date());
            estatePropertyLedger.setLastModifiedBy(currentUserID);
            em.persist(estatePropertyLedger);
            em.flush();
            return estatePropertyLedger.getEstatePropertyLedgerId();

        } catch (Exception e) {
            em.merge(estatePropertyLedger);
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }
    
    public boolean recordEntryIntoBills (Bills bills){
        try {
             double defaultAmountPaid = 0.0;
            
            bills.setDeleted("NO");
            bills.setUpdated("NO");
          //  bills.setLastModifiedDate(new Date());
            bills.setLastModifiedBy(currentUserID);
            bills.setRecordedBy(currentUserID);
            bills.setBillStatus("unpaid");
            bills.setBillAmountPaid(defaultAmountPaid);
            em.merge(bills);
            em.flush();
            return true;
        } catch (Exception e) {
           
            log.log(Level.SEVERE,e.toString(),e);
            
            return false;
        }
    }
    
    public boolean record_for_bill_payments (BillPayment billPayment){
        try {
            billPayment.setDatePaid(new Date());
          //  bills.setLastModifiedDate(new Date());
            billPayment.setDateOfTransaction(new Date());
             
            em.persist(billPayment);
            em.flush();
            return true;
        } catch (Exception e) {
           
            log.log(Level.SEVERE,e.toString(),e);
            
            return false;
        }
    }
       
    public boolean record (Setting s){
        try {
            s.setDeleted("YES");
            s.setSettingsValue("check-if-readyyyyyyyyyyyy");
             
            em.merge(s);
            em.flush();
            return true;
        } catch (Exception e) {
           
            log.log(Level.SEVERE,e.toString(),e);
            
            return false;
        }
    }
    
    public boolean checkForCurrentYearLedger(){
           try {
            String query = "SELECT e FROM EstatePropertyLedger e WHERE e.ledgerYear =" + year + "";
                
            em.createQuery(query).getResultList();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean estatePropertyLedgerDelete(EstatePropertyLedger estatePropertyLedger, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(estatePropertyLedger));
            } else if (permanent == false) {
                estatePropertyLedger.setDeleted("YES");
                estatePropertyLedger.setUpdated("NO");
                estatePropertyLedger.setLastModifiedDate(new Date());
                estatePropertyLedger.setLastModifiedBy(currentUserID);
                em.merge(estatePropertyLedger);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean estatePropertyLedgerUpdate(EstatePropertyLedger estatePropertyLedger) {
        try {

            estatePropertyLedger.setDeleted("NO");
            estatePropertyLedger.setUpdated("YES");
            estatePropertyLedger.setLastModifiedDate(new Date());
            estatePropertyLedger.setLastModifiedBy(currentUserID);
            em.merge(estatePropertyLedger);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public EstatePropertyLedger estatePropertyLedgerFind(String estatePropertyLedgerId) {
        try {

            EstatePropertyLedger estatePropertyLedger = em.find(EstatePropertyLedger.class, estatePropertyLedgerId);
            //em.refresh(estatePropertyLedger);
            return estatePropertyLedger;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<EstatePropertyLedger> estatePropertyLedgerFindByAttribute(String estatePropertyLedgerAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<EstatePropertyLedger> listOfEstatePropertyLedger = null;

        String qryString = "";

        qryString = "SELECT e FROM EstatePropertyLedger e ";
        qryString += "WHERE e." + estatePropertyLedgerAttribute + " =:estatePropertyLedgerAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfEstatePropertyLedger = (List<EstatePropertyLedger>) em.createQuery(qryString).setParameter("estatePropertyLedgerAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM EstatePropertyLedger e ";
                qryString += "WHERE e." + estatePropertyLedgerAttribute + " LIKE '%" + attributeValue + "%'";
                if (includeLogicallyDeleted == true) {
                } else if (includeLogicallyDeleted == false) {
                    qryString += "AND e.deleted = 'NO' ";
                }
                listOfEstatePropertyLedger = (List<EstatePropertyLedger>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfEstatePropertyLedger = (List<EstatePropertyLedger>) em.createQuery(qryString).setParameter("estatePropertyLedgerAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfEstatePropertyLedger;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<EstatePropertyLedger>();
    }

    public List<EstatePropertyLedger> estatePropertyLedgerGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<EstatePropertyLedger> listOfEstatePropertyLedger = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstatePropertyLedger e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstatePropertyLedger e WHERE e.deleted = 'NO'";
            }

            listOfEstatePropertyLedger = (List<EstatePropertyLedger>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfEstatePropertyLedger;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<EstatePropertyLedger> estatePropertyLedgerGetAll(boolean includeLogicallyDeleted) {
        List<EstatePropertyLedger> listOfEstatePropertyLedger = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM EstatePropertyLedger e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM EstatePropertyLedger e WHERE e.deleted = 'NO'";
            }

            listOfEstatePropertyLedger = (List<EstatePropertyLedger>) em.createQuery(qryString).getResultList();

            return listOfEstatePropertyLedger;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }
    
    public EstatePropertyLedger getYearsLedger(String estateId, int year, DebitCredit debitCredit, PaymentType pt) {
        String qryString = "";
        EstatePropertyLedger epl = new EstatePropertyLedger();
        try {

            qryString = "SELECT e FROM EstatePropertyLedger e WHERE e.estateProperty.estatePropertyId = '" + estateId + "' "
                    + " and e.paymentType =" + pt.getFullString() +" "
                    + "and e.ledgerYear = '" +year+ "' "
                    + "and e.debitCreditEntry = " + debitCredit.getFullString() + " ";

            epl = (EstatePropertyLedger) em.createQuery(qryString).getSingleResult();
            return epl;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" GroundRent Persistence Functionalities">
    
    public boolean checkGroundActivation(){
        try {
            String query = "SELECT e FROM GroundRent e WHERE e.activated = 'YES'"
                + "AND e.yearDue = " + year + "";
            em.createQuery(query).getResultList();
            return true;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            e.printStackTrace();
            return false;
        }
    }
    
    public String groundRentCreate(GroundRent groundRent) {
        try {
            groundRent.setLastModifiedDate(new Date());
            groundRent.setLastModifiedBy(currentUserID);
            em.persist(groundRent);
            em.flush();
            return groundRent.getGroundRentId();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean groundRentDelete(GroundRent groundRent, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(groundRent));
            } else if (permanent == false) {
                groundRent.setDeleted(false);
                groundRent.setLastModifiedDate(new Date());
                groundRent.setLastModifiedBy(currentUserID);
                em.merge(groundRent);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean groundRentUpdate(GroundRent groundRent) {
        try {
            groundRent.setUpdated(true);
            groundRent.setLastModifiedDate(new Date());
//            groundRent.setActivated("YES");
            groundRent.setLastModifiedBy(currentUserID);
            em.merge(groundRent);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public GroundRent groundRentFind(String groundRentId) {
        try {

            GroundRent groundRent = em.find(GroundRent.class, groundRentId);
//           em.refresh(groundRent);
            return groundRent;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<GroundRent> groundRentFindByAttribute(String groundRentAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<GroundRent> listOfGroundRent = null;

        String qryString = "";

        qryString = "SELECT e FROM GroundRent e ";
        qryString += "WHERE e." + groundRentAttribute + " =:groundRentAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfGroundRent = (List<GroundRent>) em.createQuery(qryString).setParameter("groundRentAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM GroundRent e ";
                qryString += "WHERE e." + groundRentAttribute + " LIKE '%" + attributeValue + "%'";
                listOfGroundRent = (List<GroundRent>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfGroundRent = (List<GroundRent>) em.createQuery(qryString).setParameter("groundRentAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfGroundRent;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<GroundRent> groundRentGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<GroundRent> listOfGroundRent;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM GroundRent e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM GroundRent e WHERE e.deleted = 'NO'";
            }

            listOfGroundRent = (List<GroundRent>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfGroundRent;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<GroundRent> groundRentGetAll(boolean includeLogicallyDeleted) {
        List<GroundRent> listOfGroundRent;
        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM GroundRent e ORDER BY e.yearDue DESC";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM GroundRent e WHERE e.deleted = 'false' ORDER BY e.yearDue DESC";
            }

            listOfGroundRent = (List<GroundRent>) em.createQuery(qryString).getResultList();
            return listOfGroundRent;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" PropertyOccupant Persistence Functionalities">
    public String propertyOccupantCreate(PropertyOccupant propertyOccupant) {
        try {
            propertyOccupant.setLastModifiedDate(new Date());
            propertyOccupant.setLastModifiedBy(currentUserID);
            propertyOccupant.setDeleted("NO");
            em.persist(propertyOccupant);
            em.flush();
            return propertyOccupant.getPropertyOccupantId();

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean propertyOccupantDelete(PropertyOccupant propertyOccupant, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(propertyOccupant));
            } else if (permanent == false) {
                propertyOccupant.setDeleted("YES");
                propertyOccupant.setLastModifiedDate(new Date());
                propertyOccupant.setLastModifiedBy(currentUserID);
                em.merge(propertyOccupant);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean propertyOccupantUpdate(PropertyOccupant propertyOccupant) {
        try {
            propertyOccupant.setUpdated("YES");
            propertyOccupant.setLastModifiedDate(new Date());
            propertyOccupant.setLastModifiedBy(currentUserID);
            em.merge(propertyOccupant);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public PropertyOccupant propertyOccupantFind(String propertyOccupantId) {
        try {

            PropertyOccupant propertyOccupant = em.find(PropertyOccupant.class, propertyOccupantId);
            em.refresh(propertyOccupant);
            return propertyOccupant;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<PropertyOccupant> propertyOccupantFindByAttribute(String propertyOccupantAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<PropertyOccupant> listOfPropertyOccupant = null;

        String qryString = "";

        qryString = "SELECT e FROM PropertyOccupant e ";
        qryString += "WHERE e." + propertyOccupantAttribute + " =:propertyOccupantAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfPropertyOccupant = (List<PropertyOccupant>) em.createQuery(qryString).setParameter("propertyOccupantAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM PropertyOccupant e ";
                qryString += "WHERE e." + propertyOccupantAttribute + " LIKE '%" + attributeValue + "%'";
                listOfPropertyOccupant = (List<PropertyOccupant>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfPropertyOccupant = (List<PropertyOccupant>) em.createQuery(qryString).setParameter("propertyOccupantAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfPropertyOccupant;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyOccupant>();
    }

    public List<PropertyOccupant> propertyOccupantGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<PropertyOccupant> listOfPropertyOccupant = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM PropertyOccupant e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM PropertyOccupant e WHERE e.deleted = 'NO'";
            }

            listOfPropertyOccupant = (List<PropertyOccupant>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfPropertyOccupant;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyOccupant>();
    }

    public List<PropertyOccupant> propertyOccupantGetAll(boolean includeLogicallyDeleted) {
        List<PropertyOccupant> listOfPropertyOccupant = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM PropertyOccupant e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM PropertyOccupant e WHERE e.deleted = 'NO'";
            }

            listOfPropertyOccupant = (List<PropertyOccupant>) em.createQuery(qryString).getResultList();

            return listOfPropertyOccupant;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyOccupant>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Occupancy Persistence Functionalities">
    public String occupancyCreate(Occupancy occupancy) {
        try {
            occupancy.setLastModifiedDate(new Date());
            occupancy.setLastModifiedBy(currentUserID);
            em.persist(occupancy);
            em.flush();
            return occupancy.getOccupantPropertyId();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean occupancyDelete(Occupancy occupancy, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(occupancy));
            } else if (permanent == false) {
                occupancy.setDeleted(true);
                occupancy.setLastModifiedDate(new Date());
                occupancy.setLastModifiedBy(currentUserID);
                em.merge(occupancy);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean occupancyUpdate(Occupancy occupancy) {
        try {
            occupancy.setUpdated(true);
            occupancy.setLastModifiedDate(new Date());
            occupancy.setLastModifiedBy(currentUserID);
            em.merge(occupancy);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public Occupancy occupancyFind(String occupancyId) {
        try {
            Occupancy occupantProperty = em.find(Occupancy.class, occupancyId);
            em.refresh(occupantProperty);
            return occupantProperty;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<Occupancy> occupancyFindByAttribute(String occupancyAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<Occupancy> listOfOccupancy = null;

        String qryString = "";

        qryString = "SELECT e FROM Occupancy e ";
        qryString += "WHERE e." + occupancyAttribute + " =:occupancyAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = false";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfOccupancy = (List<Occupancy>) em.createQuery(qryString).setParameter("occupancyAttribute", attributeValue).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM Occupancy e ";
                qryString += "WHERE e." + occupancyAttribute + " LIKE '%" + attributeValue + "%'";
                listOfOccupancy = (List<Occupancy>) em.createQuery(qryString).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfOccupancy = (List<Occupancy>) em.createQuery(qryString).setParameter("occupancyAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfOccupancy;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<Occupancy> occupantPropertyGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<Occupancy> listOfOccupancy = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Occupancy e";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Occupancy e WHERE e.deleted = false";
            }

            listOfOccupancy = (List<Occupancy>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfOccupancy;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<Occupancy> occupancyGetAll(boolean includeLogicallyDeleted) {
        List<Occupancy> listOfOccupancy = null;
        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Occupancy e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Occupancy e WHERE e.deleted = false";
            }

            listOfOccupancy = (List<Occupancy>) em.createQuery(qryString).getResultList();

            return listOfOccupancy;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public Occupancy checkLease(String estateId) {
        String qryString = "";
        Occupancy op = new Occupancy();
        
        try {
            qryString = "SELECT e FROM Occupancy e WHERE e.estateProperty.estatePropertyId = '" + estateId + "'";
            op = (Occupancy) em.createQuery(qryString).getSingleResult();
            return op;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" PropertyDescription Persistence Functionalities">
    public String propertyDescriptionCreate(PropertyDescription propertyDescription) {
        try {
            propertyDescription.setLastModifiedDate(new Date());
            propertyDescription.setLastModifiedBy(currentUserID);
            em.persist(propertyDescription);
            em.flush();
            return propertyDescription.getPropertyDescriptionId();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean propertyDescriptionDelete(PropertyDescription propertyDescription, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(propertyDescription));
            } else if (permanent == false) {
                propertyDescription.setDeleted("YES");
                propertyDescription.setUpdated("NO");
                propertyDescription.setLastModifiedDate(new Date());
                propertyDescription.setLastModifiedBy(currentUserID);
                em.merge(propertyDescription);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean propertyDescriptionUpdate(PropertyDescription propertyDescription) {
        try {

            propertyDescription.setDeleted("NO");
            propertyDescription.setUpdated("NO");
            propertyDescription.setLastModifiedDate(new Date());
            propertyDescription.setLastModifiedBy(currentUserID);
            em.merge(propertyDescription);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public PropertyDescription propertyDescriptionFind(String propertyDescriptionId) {
        try {

            PropertyDescription propertyDescription = em.find(PropertyDescription.class, propertyDescriptionId);
            em.refresh(propertyDescription);
            return propertyDescription;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<PropertyDescription> propertyDescriptionFindByAttribute(String propertyDescriptionAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<PropertyDescription> listOfPropertyDescription = null;

        String qryString = "";

        qryString = "SELECT e FROM PropertyDescription e ";
        qryString += "WHERE e." + propertyDescriptionAttribute + " =:propertyDescriptionAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfPropertyDescription = (List<PropertyDescription>) em.createQuery(qryString).setParameter("propertyDescriptionAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM PropertyDescription e ";
                qryString += "WHERE e." + propertyDescriptionAttribute + " LIKE '%" + attributeValue + "%'";
                listOfPropertyDescription = (List<PropertyDescription>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfPropertyDescription = (List<PropertyDescription>) em.createQuery(qryString).setParameter("propertyDescriptionAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfPropertyDescription;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyDescription>();
    }

    public List<PropertyDescription> propertyDescriptionGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<PropertyDescription> listOfPropertyDescription = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM PropertyDescription e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM PropertyDescription e WHERE e.deleted = 'NO'";
            }

            listOfPropertyDescription = (List<PropertyDescription>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfPropertyDescription;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyDescription>();
    }

    public List<PropertyDescription> propertyDescriptionGetAll(boolean includeLogicallyDeleted) {
        List<PropertyDescription> listOfPropertyDescription = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM PropertyDescription e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM PropertyDescription e WHERE e.deleted = 'NO'";
            }

            listOfPropertyDescription = (List<PropertyDescription>) em.createQuery(qryString).getResultList();

            return listOfPropertyDescription;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<PropertyDescription>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Staff Persistence Functionalities">
    public boolean staffCreate(Staff staff) {
        try {
            staff.setUpdated(false);
            staff.setDeleted(false);
            em.persist(staff);
            em.flush();
            
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    /**
     * Use this method to remove a staff from the system either permanently or temporarily
     * @param staff the object to be remove from the database
     * @param permanent decision to remove permanently or temporarily<br />
     *                  true - permanently <br />
     *                  false - temporarily
     * @return 
     */
    public boolean staffDelete(Staff staff, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(staff));
                
            } else if (permanent == false) {
                staff.setDeleted(true);
                em.merge(staff);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }
    
    public boolean batchStaffDelete(List<Staff> staffs, boolean permanent){
        try {
            for(Staff stf : staffs){
                staffDelete(stf, permanent);
            }
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean staffUpdate(Staff staff) {
        try {
            em.merge(staff);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public Staff staffFind(String staffId) {
        try {

            Staff staff = em.find(Staff.class, staffId);
            em.refresh(staff);
            return staff;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<Staff> staffFindByAttribute(String staffAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<Staff> listOfStaff = null;

        String qryString = "";

        qryString = "SELECT e FROM Staff e ";
        qryString += "WHERE e." + staffAttribute + " =:staffAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = '0'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfStaff = (List<Staff>) em.createQuery(qryString).setParameter("staffAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM Staff e ";
                qryString += "WHERE e." + staffAttribute + " LIKE '%" + attributeValue + "%'";
                listOfStaff = (List<Staff>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfStaff = (List<Staff>) em.createQuery(qryString).setParameter("staffAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfStaff;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Staff>();
    }

    public List<Staff> staffGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<Staff> listOfStaff = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Staff e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Staff e WHERE e.deleted = '0'";
            }

            listOfStaff = (List<Staff>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfStaff;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<Staff> staffGetAll(boolean includeLogicallyDeleted) {
        List<Staff> listOfStaff = null;
        String qryString = "";

        try {
            
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Staff e ORDER BY e.firstName, e.lastName ASC";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Staff e WHERE e.deleted = false ORDER BY e.firstName ASC, e.lastName ASC";
            }
            
            synchPCnDB();
            listOfStaff = (List<Staff>) em.createQuery(qryString).getResultList();

            return listOfStaff;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Staff>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Setting Persistence Functionalities">
    public String settingCreate(Setting setting) {
        try {

            setting.setDeleted("NO");
            setting.setUpdated("NO");
            setting.setLastModifiedDate(new Date());
            setting.setLastModifiedBy(currentUserID);
            em.persist(setting);
            em.flush();
            return setting.getSettingsKey();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean settingDelete(Setting setting, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(setting));
            } else if (permanent == false) {
                setting.setDeleted("YES");
                setting.setUpdated("NO");
                setting.setLastModifiedDate(new Date());
                setting.setLastModifiedBy(currentUserID);
                em.merge(setting);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean settingUpdate(Setting setting) {
        try {

            setting.setDeleted("NO");
            setting.setUpdated("NO");
            setting.setLastModifiedDate(new Date());
            setting.setLastModifiedBy(currentUserID);
            em.merge(setting);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public Setting settingFind(String settingsKey) {
        try {

            Setting setting = em.find(Setting.class, settingsKey);
            em.refresh(setting);
            return setting;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<Setting> settingFindByAttribute(String settingAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<Setting> listOfSetting = null;

        String qryString = "";

        qryString = "SELECT e FROM Setting e ";
        qryString += "WHERE e." + settingAttribute + " =:settingAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfSetting = (List<Setting>) em.createQuery(qryString).setParameter("settingAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM Setting e ";
                qryString += "WHERE e." + settingAttribute + " LIKE '%" + attributeValue + "%'";
                listOfSetting = (List<Setting>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfSetting = (List<Setting>) em.createQuery(qryString).setParameter("settingAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfSetting;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Setting>();
    }

    public List<Setting> settingGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<Setting> listOfSetting = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Setting e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Setting e WHERE e.deleted = 'NO'";
            }

            listOfSetting = (List<Setting>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfSetting;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Setting>();
    }

    public List<Setting> settingGetAll(boolean includeLogicallyDeleted) {
        List<Setting> listOfSetting = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Setting e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Setting e WHERE e.deleted = 'NO'";
            }

            listOfSetting = (List<Setting>) em.createQuery(qryString).getResultList();

            return listOfSetting;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<Setting>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" UserAccount Persistence Functionalities">
    public boolean userAccountCreate(UserAccount userAccount) {
        try {
            em.persist(userAccount);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean userAccountDelete(UserAccount userAccount, boolean permanent) {
        try {
            if (permanent == true) {
                em.remove(em.merge(userAccount));
                
            } else if (permanent == false) {
                userAccount.setDeleted(true);
                em.merge(userAccount);
            }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }
    
    public boolean batchUserAccountDelete(List<UserAccount> UAcounts, boolean permanent){
        try {
            for(UserAccount uacct : UAcounts){
                userAccountDelete(uacct, permanent);
            }
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public boolean userAccountUpdate(UserAccount userAccount) {
        try {

            userAccount.setUpdated(true);
            userAccount.setLastModifiedDate(new Date());
            userAccount.setLastModifiedBy(currentUserID);
            em.merge(userAccount);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public UserAccount userAccountFind(String userAccountId) {
        try {

            UserAccount userAccount = em.find(UserAccount.class, userAccountId);
            em.refresh(userAccount);
            return userAccount;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<UserAccount> userAccountFindByAttribute(String userAccountAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<UserAccount> listOfUserAccount = null;
        String qryString;

        qryString = "SELECT e FROM UserAccount e ";
        qryString += "WHERE e." + userAccountAttribute + " =:userAccountAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = '0'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                synchPCnDB();
                listOfUserAccount = (List<UserAccount>) em.createQuery(qryString).setParameter("userAccountAttribute", attributeValue).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM UserAccount e ";
                qryString += "WHERE e." + userAccountAttribute + " LIKE '%" + attributeValue + "%'";
                synchPCnDB();
                listOfUserAccount = (List<UserAccount>) em.createQuery(qryString).getResultList();
                
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                synchPCnDB();
                listOfUserAccount = (List<UserAccount>) em.createQuery(qryString).setParameter("userAccountAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfUserAccount;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<UserAccount> userAccountGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<UserAccount> listOfUserAccount = null;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM UserAccount e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM UserAccount e WHERE e.deleted = '0'";
            }
            
            synchPCnDB();
            listOfUserAccount = (List<UserAccount>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();
            return listOfUserAccount;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<UserAccount> userAccountGetAll(boolean includeLogicallyDeleted) {
        List<UserAccount> listOfUserAccount;
        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM UserAccount e ORDER BY e.staff ASC";
                
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM UserAccount e WHERE e.deleted = false ORDER BY e.staff ASC";
            }

            synchPCnDB();
            listOfUserAccount = (List<UserAccount>) em.createQuery(qryString).getResultList();
            return listOfUserAccount;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="EstateClass Persistence">
    public List<EstateClass> estateClassGetAll() {
        List<EstateClass> listOfEstateClass = null;
        try {
            listOfEstateClass = (List<EstateClass>) em.createNamedQuery("EstateClass.findAll", EstateClass.class).getResultList();
            return listOfEstateClass;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<EstateClass>();
    }

    public EstateClass estateClassFind(Integer Id) {
        try {
            EstateClass ec = em.find(EstateClass.class, Id);
            return ec;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="House Rent Persistence Functionalities">
//    public String houseRentCreate(HouseRent houseRent){
//        try {
//            em.persist(houseRent);
//            em.flush();
//            return houseRent.getHouseRentId();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    
//    public boolean houseRentUpdate(HouseRent houseRent){
//        try {
//            houseRent.setUpdated("YES");
//            em.merge(houseRent);
//            em.flush();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    
//    public HouseRent houseRentFind(String id){
//        try {
//            HouseRent houseRent = em.find(HouseRent.class, id);
//            return houseRent;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    
//    public List<HouseRent> houseRentGetAll(){
//        try {
//            List<HouseRent> houseRentList = (List<HouseRent>)em.createNamedQuery("HouseRent.findAll", HouseRent.class).getResultList();
//            return houseRentList;
//        } catch (Exception e) {
//        }
//        return new ArrayList<>();
//    }
    //</editor-fold>

// MY VERSION OF THE CODE
    public String userCreate(Usertable userTable) {
        try {

            em.persist(userTable);
            em.flush();
            return userTable.getUserid();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);

            return null;

        }
    }

    public boolean userDelete(Usertable userTable, boolean permanent) {
        try {

            if (permanent == true) {
                em.remove(em.merge(userTable));
            }
//           else if(permanent == false)
//           {
//               userAccount.setDeleted("YES");
//               userAccount.setUpdated("NO");
//               userAccount.setLastModifiedDate(new Date());
//               userAccount.setLastModifiedBy(currentUserID);
//               em.merge(userAccount);
//           }
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public boolean userUpdate(Usertable userTable) {
        try {

//           userAccount.setDeleted("NO");
//           userAccount.setUpdated("NO");
//           userAccount.setLastModifiedDate(new Date());
//           userAccount.setLastModifiedBy(currentUserID);
            em.merge(userTable);
            em.flush();
            return true;

        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return false;

        }
    }

    public Usertable userFind(String userId) {
        try {

            Usertable userTable = em.find(Usertable.class, userId);
            em.refresh(userTable);
            return userTable;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<Usertable> userFindByAttribute(String userTableAttribute, Object attributeValue, String fieldType, boolean includeLogicallyDeleted) {
        List<Usertable> listOfUsers = null;

        String qryString = "";

        qryString = "SELECT e FROM Usertable e ";
        qryString += "WHERE e." + userTableAttribute + " =:userTableAttribute ";

        try {
            if (includeLogicallyDeleted == true) {
            } else if (includeLogicallyDeleted == false) {
                qryString += " AND e.deleted = 'NO'";
            }

            if (fieldType.equalsIgnoreCase("NUMBER")) {
                listOfUsers = (List<Usertable>) em.createQuery(qryString).setParameter("userTableAttribute", attributeValue).getResultList();
            } else if (fieldType.equalsIgnoreCase("STRING")) {
                qryString = "SELECT e FROM UserAccount e ";
                qryString += "WHERE e." + userTableAttribute + " LIKE '%" + attributeValue + "%'";
                listOfUsers = (List<Usertable>) em.createQuery(qryString).getResultList();
            } else if (fieldType.equalsIgnoreCase("DATE")) {
                listOfUsers = (List<Usertable>) em.createQuery(qryString).setParameter("userTableAttribute", (Date) attributeValue, TemporalType.DATE).getResultList();
            }

            return listOfUsers;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<Usertable> userGetRange(int firstResultIndex, int numberToRetrieve, boolean includeLogicallyDeleted) {
        List<Usertable> listOfUsers;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Usertable e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Usertable e WHERE e.deleted = 'NO'";
            }

            listOfUsers = (List<Usertable>) em.createQuery(qryString).setFirstResult(firstResultIndex).setMaxResults(numberToRetrieve).getResultList();

            return listOfUsers;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    public List<Usertable> userGetAll(boolean includeLogicallyDeleted) {
        List<Usertable> listOfUsers ;

        String qryString = "";

        try {
            if (includeLogicallyDeleted == true) {
                qryString = "SELECT e FROM Usertable e";
            } else if (includeLogicallyDeleted == false) {
                qryString = "SELECT e FROM Usertable e WHERE e.deleted = 'NO'";
            }

            listOfUsers = (List<Usertable>) em.createQuery(qryString).getResultList();

            return listOfUsers;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
        }
        return new ArrayList<>();
    }

    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Status Persistence Functionalities">
    public Status statusFind(Boolean Id) {
        try {
            Status status = em.find(Status.class, Id);
            return status;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    public List<Status> statusGetAll() {
        List<Status> listOfStatus ;
        try {
            synchPCnDB();
            listOfStatus = (List<Status>) em.createNamedQuery("Status.findAll", Status.class).getResultList();
            return listOfStatus;
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="UserRole Persistence Functionalities">

    public UserRole userRoleFind(Integer ID) {
        try {
            UserRole ur = em.find(UserRole.class, ID);
            return ur;
        } catch (Exception e) {
        }
        return null;
    }

    public List<UserRole> getUSerRoleAll() {
        try {
            Query query = em.createNamedQuery("UserRole.findAll", UserRole.class);
            synchPCnDB();
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="BindProperties Functionalites">

    public List<BindProperties> bindPropertiesGetAll() {
        List<BindProperties> listOfBindProps = null;
        try {
            listOfBindProps = em.createNamedQuery("BindProperties.findAll", BindProperties.class).getResultList();
            return listOfBindProps;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e.getMessage());
        }
        return new ArrayList<>(listOfBindProps);
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CreditBalance Persistence">

    public boolean creditBalanceUpdate(CreditBalance cb) {
        try {
            em.merge(cb);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Double getCreditBalaceLeft() {
        String qry = "";
        try {
            qry = "SELECT c.creditLeft FROM CreditBalance c";
            Number number = (Number) em.createQuery(qry).getSingleResult();
            if (number == null) {
                return 0.0;
            }
            return number.doubleValue();
        } catch (Exception e) {
        }
        return 0.0;
    }

    public Integer creditBalanceCreate(CreditBalance cb) {
        try {

            em.persist(cb);
            em.flush();
            return cb.getClientId();
        } catch (Exception e) {
        }
        return null;
    }

    public boolean creditBalanceUpdate(TopupLog topupLog) {
        try {
            Integer clientId = 1;
            Double topUpAmt = topupLog.getAmount();
            CreditBalance cb = creditBalanceFind(clientId);
            if (cb != null) {
                Double creditLeft = cb.getCreditLeft() + topUpAmt;
                Double totalPurchased = cb.getTotalPurchasedCredit() + topUpAmt;
                cb.setCreditLeft(creditLeft);
                cb.setTotalPurchasedCredit(totalPurchased);
                em.merge(cb);
                em.flush();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<CreditBalance> creditBalanceGetAll() {
        String qry = "";
        try {
            qry = "SELECT c FROM CreditBalance c";
            return (List<CreditBalance>) em.createQuery(qry).getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public CreditBalance creditBalanceFind(Integer Id) {
        try {
            CreditBalance cb = em.find(CreditBalance.class, Id);
            return cb;
        } catch (Exception e) {
            return null;
        }
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TopupLog Persistence">
    public TopUpLogPK topUpLogCreate(TopupLog topupLog) {
        try {
            topupLog.getLogPK().setCreatedBy(currentUserID);
            topupLog.getLogPK().setDateOfTransaction(new Date());
            em.persist(topupLog);
            em.flush();
            return topupLog.getLogPK();
        } catch (Exception e) {
        }
        return null;
    }

    public List<TopupLog> topUpLogGetAll() {
        String qry = "";
        try {
            qry = "SELECT t FROM TopupLog t";
            return (List<TopupLog>) em.createQuery(qry).getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SmessMessage Persistence Functionalites">

    public Integer smessMessageCreate(SmessMessage sm) {
        try {
            sm.setDeleted(false);
            em.persist(sm);
            em.flush();
            return sm.getMessageId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SmessMessage smessMessageFind(Integer id) {
        try {
            SmessMessage sm = em.find(SmessMessage.class, id);
            return sm;
        } catch (Exception e) {
            return null;
        }
    }

    public List<SmessMessage> smessMessageGetAll() {
        try {
            return em.createNamedQuery("SmessMessage.findAll", SmessMessage.class).getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
//</editor-fold>
    
    
    
    
    
    //NEW QUERIES ON BILLS TABLE
    
    
   
    
    public double get_bill_amount_paid_by_occu_naitive(String res){
        try {
             String qry = "SELECT bill_amount_paid FROM bills "
                     + "WHERE property_occupant ='" + res + "' "
                     + "AND  ( bill_status =  'part' OR bill_status = 'unpaid') "
                     + " ORDER BY bill_year ASC LIMIT 1";
                    
              
           
            System.out.println("Bill Amount Paid Naitive Query....................." + qry);
        return (double) em.createNativeQuery(qry).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
             return 0.0;
        }
       
       
    }
    
       
    public List loadOccupantBillStatus(String res) {
           
           String unpaid = "paid";
           String part = "unpaid";
        if (res == null) {
            return null;
        }

 
        String qry = "SELECT l FROM Bills l "
                + "WHERE l.propertyOccupant = '" + res + "' "
                //                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
                + "AND l.billStatus ='" + unpaid + "' "
                + "OR l.billStatus = '" + part + "' "
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.billYear ASC";
              //  + "l.dateOfRecordTransaction DESC, l.lastModifiedDate DESC";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
           // logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    
    public double get_total_owings_of_occupants(String res){ 
        double check =0;
        try {
            String qry = "SELECT SUM(e.billAmount) FROM Bills e "
                + "WHERE  e.propertyOccupant = '" + res  + "' ";
                   // + "AND  e.billStatus = 'unpaid' OR e.billStatus = 'part' ";
          check =   (double) em.createQuery(qry).getSingleResult();
           
        
           System.out.println(".Get check Before>>>>>>>>" + check);
            return check;  
        
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0.0;
        }
    }
      
    public int count_total_owings_of_occupants(String res){ 
           int check ;
        try {
            String qry = "SELECT COUNT(e.billAmount) FROM Bills e "
                + "WHERE  e.propertyOccupant = '" + res  + "' ";
                   // + "AND  e.billStatus = 'unpaid' OR e.billStatus = 'part' ";
          check =   (int) em.createQuery(qry).getSingleResult();
           
        
           System.out.println(".Get check Before>>>>>>>>" + check);
            return check;  
        
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0;
        }
    }
      
    public double get_total_owings_part_of_occupants(String res){
        try {
            String qry = "SELECT SUM(e.billAmount) FROM Bills e "
                + "WHERE  e.propertyOccupant = '" + res  + "' "
                    + "AND  e.billStatus = 'part' ";
                    
                   
            
                 System.out.println(".Get total owing part query>>>>>>>>" + qry);
            return (double) em.createQuery(qry).getSingleResult();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0.0;
        }
    }

      
    public double get_total_part_payments_of_occupants(String res){
        try {
            String qry = "SELECT DISTINCT(e.billAmountPaid) FROM Bills e "
                + "WHERE e.propertyOccupant = '" + res  + "'"
                    + "AND e.billStatus = 'part' ";
                    
            
                 System.out.println(".>>>>>>>>" + qry);
            return (double) em.createQuery(qry).getSingleResult();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0.0;
        }
    }
          
    public double get_occupant_bill_amount(String res){
              String unpaid = "unpaid";
              String part = "part";
               String qry = "SELECT l.billAmount FROM Bills l "
                + "WHERE l.propertyOccupant = '" + res + "' "
                
                       
                       + "AND l.billStatus ='" + unpaid + "' "
                + "OR l.billStatus = '" + part + "' "
               
                + "ORDER BY l.billYear ASC";
              
             return (double) em.createQuery(qry).getSingleResult();   
              
          }
          
    public boolean update_occ_status_to_part (String res,double amount,int bill_year){
           
          String part = "part";
        
        try {
                   String query = "UPDATE bills SET bill_status = 'part', bill_amount_paid = '"+ amount +"' "
                           + " WHERE property_occupant = '" + res + "' "
                           + "AND bill_amount_paid < bill_amount "
                           + "AND bill_year = ' "+ bill_year +" ' ";
                          // + "AND bill_status = 'part' "
                           //+ "ORDER BY bill_year ASC LIMIT 1 ";      
          
             
             System.out.println(".Update part query>>>>>>>>" + query);
             
                em.createNativeQuery(query).executeUpdate();
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
                
    }
     
     
    public boolean update_occ_status_to_part_no_year (String res,double amount){
           
          String part = "part";
        
        try {
                   String query = "UPDATE bills SET bill_status = 'part', bill_amount_paid = '"+ amount +"' "
                           + " WHERE property_occupant = '" + res + "' "
                           + "AND bill_amount_paid < bill_amount "
                           //+ "AND bill_year = ' "+ bill_year +" ' ";
                          // + "AND bill_status = 'part' "
                           + "ORDER BY bill_year ASC LIMIT 1 ";      
          
             
             System.out.println(".Update part query>>>>>>>>" + query);
             
                em.createNativeQuery(query).executeUpdate();
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
                
    }
              
    public boolean update_occ_status_to_full_status (String res,double amount,int bill_year){
                  
                  
        
          String paid = "paid";
        
        try {
                String query = "UPDATE bills SET bill_status = '"+ paid +"', bill_amount_paid = '"+ amount +"' "
                        + "WHERE property_occupant = '"+ res + "' "
                        + "AND bill_year = '" + bill_year  + "'";
                      //  + "AND bill_amount_paid < bill_amount "
                          // + "ORDER BY bill_year ASC LIMIT 1 ";      
          
             
             System.out.println(".Update full status query>>>>>>>>" + query);
             
                em.createNativeQuery(query).executeUpdate();
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
                
    }
   
    public boolean upadate_occ_status_to_discount (String res,double amount){
                  
                  
        
          String paid = "paid";
        
        try {
             String qry = "UPDATE Bills b "
                + "SET b.billDiscount = '" + amount +  "'"
                     
                        + "WHERE b.propertyOccupant = '" + res + "' ";
                
                     
             
             
             Query query = em.createQuery(qry);
             query.executeUpdate();
             
             System.out.println(".Occupant Credit Balance>>>>>>>>" + qry);
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
        
    public List<Bills> load_all_paid_bills(String status) {
                
             
      

 
        String qry = "SELECT b FROM Bills b "
                + "WHERE b.billStatus = '" + status + "' "
                + "AND b.deleted = 'NO' ";
        System.out.println("Unpaid Query ......................." + qry);

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }

    public List<Bills> load_bill_report_of_occupant(String res) {
                
             
      

 
        String qry = "SELECT b FROM Bills b "
                + "WHERE b.propertyOccupant = '" + res + "' "
                + "AND b.deleted = 'NO' "
                + "ORDER BY "
                + "b.billYear ASC ";
        System.out.println("Unpaid Query ......................." + qry);

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }
    
     
    public List<Bills> load_status_of_occu(EstateProperty estateProperty) {
                
             
      

 
        String qry = "SELECT b FROM Bills b "
                + "WHERE b.estateProperty.estatePropertyId = '"+estateProperty+"' "
               // + "OR b.propertyOccupant = '"+res+"' "
                + "AND b.deleted = 'NO' ";
        System.out.println("Unpaid Query ......................." + qry);

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }
    
    public List<Bills> load_all_paid_bills_to_report() {
                
                String status_unpaid = "unpaid";
      

 
        String qry = "SELECT b FROM Bills b "
                + "WHERE b.billStatus = 'paid' "
               
               
                + "AND b.deleted = 'NO' ";
                 
        
        
        System.out.println("Unpaid Query ......................." + qry);

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }
    
    public double total_unpaid_bills(){
            
            String status = "unpaid";
        try {
            String qry = "SELECT SUM(e.billAmount) FROM Bills e "
                + "WHERE  e.billStatus = '" + status  + "' ";
                   // + "AND  e.billStatus = 'part' ";
                    
                   
            
                 System.out.println(".Sum of unpaid query>>>>>>>>" + qry);
            return (double) em.createQuery(qry).getSingleResult();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0.0;
        }
    }
        
        
        
    public double total_paid_bills(String status){
            
        
        try {
            String qry = "SELECT SUM(e.billAmount) FROM Bills e "
                + "WHERE  e.billStatus = '" + status  + "' ";
                   // + "AND  e.billStatus = 'part' ";
                    
                   
            
                 System.out.println(".Sum of unpaid query>>>>>>>>" + qry);
            return (double) em.createQuery(qry).getSingleResult();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0.0;
        }
    }
           
    public int count_paid_bills(){
            
            String status = "paid";
        try {
            String qry = "SELECT COUNT(e.propertyOccupant) FROM Bills e "
                + "WHERE  e.billStatus = '" + status  + "' ";
                   // + "AND  e.billStatus = 'part' ";
                    
                   
            
                 System.out.println(".Sum of unpaid query>>>>>>>>" + qry);
            return (int) em.createQuery(qry).getSingleResult();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return 0;
        }
    }
                
                

                
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Object[]> prepareEntirePaidBillsSheetPerOccupant(String res) {
        
       // String status = "paid";
 
        
        //SELECT DISTINCT(e.billAmountPaid)
        
         String qry = "SELECT b.billDetail, b.billType, b.billYear, b.billAmount, b.billAmountPaid, b.billStatus FROM Bills b "
                + "WHERE b.propertyOccupant = '" + res + "' "
                + "AND b.deleted = 'NO' "
                + "ORDER BY "
                + "b.billYear ASC ";
         

            System.out.println("Print this query out>>>>>>>>>" + qry );

        String rs = "";

        List<Object[]> result = new LinkedList<>();

        try {
            result = em.createQuery(qry).getResultList();

            StringUtil.pringListArray(result);
            
            System.out.println("????????????>>>>>>>>>>>.Results" + result);

        } catch (Exception e) {
            e.printStackTrace();
            rs = e.toString();
        }

        return result;
    }           
                
   @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Object[]> prepareEntirePaidBillsSheet(String status) {
        
       // String status = "paid";
 
        
        //SELECT DISTINCT(e.billAmountPaid)

        String qry = "SELECT l.estateProperty, "
                + "l.propertyOccupant, "
                  + "l.billAmountPaid  "
                //+ "l.propertyOccupant, l.billAmountPaid "
                //+ " SUM(l.billAmountPaid), COUNT(DISTINCT l.estateProperty), l.dateOfRecordEntry "
                + "FROM Bills l "
                + "WHERE l.billStatus = '" + status + "' ";
                 
              //  + "GROUP BY  "
              //  + "l.estateProperty";
                //+ "l.propertyOccupant, l.dateOfRecordEntry";

        String rs = "";

        List<Object[]> result = new LinkedList<Object[]>();

        try {
            result = em.createQuery(qry).getResultList();

            StringUtil.pringListArray(result);

        } catch (Exception e) {
            e.printStackTrace();
            rs = e.toString();
        }

        return result;
    }
    
      
    
                   
   //@TransactionAttribute(TransactionAttributeType.NEVER)
    public double get_amount_owing_on_limit_of_occu(String res) {
        String qry = "SELECT l.billAmount "
                + "FROM Bills l "
                + "WHERE l.propertyOccupant = '" + res  +  "'  "
                + "AND l.billStatus = 'unpaid' "
                + "OR l.billStatus = 'part' "
                + "ORDER BY "
                + "l.billYear ASC ";
        try {
            
                System.out.println("....>>>>Query On Limit Results" + qry);
           double result =(double) em.createQuery(qry).setMaxResults(1).getSingleResult();
           
           return result;

            //StringUtil.pringListArray(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }}
    
    
    public List<EstatePropertyLedger> load_occu_debit_from_ledger(String res) {
        String qry = "SELECT l.amountInvolved "
                + "FROM EstatePropertyLedger "
                + "WHERE l.estateProperty = '" + res  +  "'  "
                + "AND l.typeOfEntry = 'DEBIT' "
               // + "OR l.billStatus = 'part' "
                + "ORDER BY "
                + "l.ledgerYear ASC ";
        try {
            
                System.out.println("....>>>>Query On Limit Results" + qry);
           List<EstatePropertyLedger> result =(List<EstatePropertyLedger>)em.createQuery(qry).getResultList();
           
           return result;

            //StringUtil.pringListArray(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }}
    
    public List<Bills> load_all_unpaid_part_occu(String res) {
                
         String qry = "SELECT l "
                + "FROM Bills l "
                + "WHERE l.propertyOccupant = '" + res  +  "'  "
                 + "AND l.billAmountPaid < l.billAmount "
                + "AND ( l.billStatus = 'unpaid' "
                + "OR l.billStatus = 'part') "
                + "ORDER BY "
                + "l.billYear ASC ";
         
          
                 
        
        
        System.out.println("Unpaid Or Part Query ......................." + qry);

        try {
            return  em.createQuery(qry).setMaxResults(1).getResultList();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }
       
    public double amount_unpaid_part_occu(String res,int year) {
                
         String qry = "SELECT l.billAmount "
                + "FROM Bills l "
                + "WHERE l.propertyOccupant = '"+res+"'  "
                 + "AND l.billYear = '"+year+"'  "
                 + "AND l.billAmountPaid < l.billAmount "
                + "AND ( l.billStatus = 'unpaid' "
                + "OR l.billStatus = 'part') "
                + "ORDER BY "
                + "l.billYear ASC ";
         
          
                 
        
        
        System.out.println("BillAmountFor A particular year ......................." + qry);

        try {
            return (double)  em.createQuery(qry).setMaxResults(1).getSingleResult();

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return 0.0;
        }
       
    }
       
       
    public List<Bills> load_all_unpaid_part_occu_native(String res) {
             
             List<Bills> billses;
                
         String qry  ="SELECT * FROM bills WHERE property_occupant =' " + res +  " ' "
                 + " AND ( bill_status =  'part' OR bill_status = 'unpaid' )"
                 + " ORDER BY "
                 + " bill_year "
                 + " ASC LIMIT 1 ";
         
          
                 
        
        
        System.out.println("Unpaid Or Part Query Naitive ......................." + qry);

        try {
            billses = (List<Bills>) em.createNativeQuery(qry,com.sabonay.advantage.ejb.entities.Bills.class).getResultList();
            
            return billses;

        } catch (Exception e) {
            
            log.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }
        
        
                
    }
    
     
    
 
