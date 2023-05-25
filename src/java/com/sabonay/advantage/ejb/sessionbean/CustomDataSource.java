/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.sessionbean;

import com.sabonay.advantage.common.constants.ContactGroup;
import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.common.constants.PropertyUsage;
import com.sabonay.advantage.common.constants.SMSContact;
import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.common.utils.GroundRentBiller;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.*;
import com.sabonay.common.collection.comparators.StringValueComparator;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.common.utils.DateTimeUtils;
import com.sabonay.common.utils.StringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Edwin / Ritchid
 */
@Stateless
public class CustomDataSource implements Serializable {

    @PersistenceContext(unitName = "advantagePU")
    private EntityManager em;
    
    @EJB
    private SHCCRUDManager sHCCRUDManager;
    
    private int currentYear = DateTimeUtils.getCurrentYear();
    private final static Logger logg = Logger.getLogger(CustomDataSource.class.getName());

    @PreDestroy
    public void close() {
        em.flush();
        em.close();
    }
    
    /**
     * This method is called to synchronize the persistence context content with the 
     * database current content
     */
    public void synchPCnDB(){
        em.flush();
    }
    
    /**
     * Method to check for user's credentials to an account existence in the database
     * @param userName the username for the account
     * @param userPassword password for the account
     * @return an object of type UserAccount if account exist or null if otherwise
     */
    public UserAccount findUserDetail(String userName, String userPassword) {
        String query = "SELECT u FROM UserAccount u "
                + "WHERE u.userPassword = '" + userPassword + "' AND u.username = '" + userName + "' AND u.deleted = false ";
        
        try {
            synchPCnDB();
            return (UserAccount) em.createQuery(query).getSingleResult();

        } catch (Exception e) {
            System.out.println("Error executing query = " + query);
            logg.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
    
    

    
    
    

    //First Step
    public PropertyOccupant estatePropertyCurrentOccupant(EstateProperty estateProperty) {
        if (estateProperty == null) {
            return null;
        }

        Occupancy occupancy = currentOccupancyByPropertyId(estateProperty.getEstatePropertyId());

        if (occupancy != null) {
            return occupancy.getPropertyOccupant();
        }

        return null;
    }

     //Second Step
    public Occupancy currentOccupancyByPropertyId(String propertyId) {
        return occupantPropertyByPrpertyId(propertyId, new Date());
    }

    public Occupancy getOccupantPropertyByPropertyId(EstateProperty ep) {
        Occupancy op = null;
        String qry = "SELECT op FROM  OccupantProperty op "
                + "WHERE op.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' ";
        try {
            op = (Occupancy) em.createQuery(qry).getSingleResult();
            return op;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Third Step
    @PrePersist
    public Occupancy occupantPropertyByPrpertyId(String estaterProperrtyId, Date checkDate) {

        String qry = "SELECT l FROM OccupantProperty l "
                + "WHERE l.estateProperty.estatePropertyId ='" + estaterProperrtyId + "'"
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.firstDateOfOccupancy ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE).setFirstResult(0).setMaxResults(1);

            return (Occupancy) query.getSingleResult();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    public List<Occupancy> getEstatePropertyOccupancyOnDate(EstateProperty estateProperty, Date checkDate) {

        String qry = "SELECT l FROM OccupantProperty l "
                + "WHERE l.estateProperty =:estateProperty "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.firstDateOfOccupancy ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE).setParameter("estateProperty", estateProperty);

            return query.getResultList();

        } catch (Exception e) {

            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> loadUnOccupiedProperties(EstateProperty estateProperty, Date checkDate) {

        String qry = "SELECT ep "
                + "FROM EstateProperty ep LEFT JOIN OccupantProperty l "
                + "WHERE l.estateProperty =:estateProperty "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.firstDateOfOccupancy ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);

            return query.getResultList();

        } catch (Exception e) {

            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    public List<EstatePropertyLedger> loadAllLedgerEntriesForProperty(EstateProperty estateProperty, int year) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "AND l.deleted = 'NO' " 
                + "ORDER BY l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
    
     public List<EstatePropertyLedger> loadAllLedgerEntriesForPropertyRental(EstateProperty estateProperty, int year) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "AND l.paymentType = 'HOUSE_RENT' " 
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public List<EstatePropertyLedger> loadEstatePropertyLegders(EstateProperty ep) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND  l.deleted = 'NO' "
                + "ORDER BY l.ledger ASC";
        try {

            return em.createQuery(qry).getResultList();
        } catch (Exception e) {

        }
        return new LinkedList<>();
    }

    public List<EstatePropertyLedger> loadLedgerEntriesForSelectedYear(EstateProperty estateProperty, int year) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear = " + year + " "
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.ledgerYear, "
                + "l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
    
    public List<EstatePropertyLedger> loadLedgerEntriesForSelectedYearRental(EstateProperty estateProperty, int year) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear = " + year + " "
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.ledgerYear, "
                + "l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public List<EstatePropertyLedger> loadAllBillsPaid(int year, PaymentType paymentType) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.ledgerYear = " + year + " "
                + "AND l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.debitCreditEntry = " + DebitCredit.CREDIT.getFullString() + " ";
        try {

            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }

        return null;
    }

    public List<Object[]> loadAllBills(EstateProperty ep, int year, PaymentType paymentType) {
        List<Object[]> list = new ArrayList<>();
        String qry = "Select l.debitCreditEntry, SUM(l.amountInvolved), MAX(l.dateOfRecordTransaction), l.dateOfRecordTransaction , "
                + "l.amountInvolved, l.estateProperty.estatePropertyId "
                + "FROM EstatePropertyLedger l "
                + "WHERE l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND l.ledgerYear = " + year + " "
                + "AND l.deleted = 'NO' "
                //                + "ORDER BY l.ledgerYear "
                + "ORDER BY l.dateOfRecordTransaction ASC ";
        try {
            list = (List<Object[]>) em.createQuery(qry).getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Object[]> loadAllBillsDesc(EstateProperty ep, int year, PaymentType paymentType) {
        List<Object[]> list = new ArrayList<>();
        String qry = "Select l.debitCreditEntry, SUM(l.amountInvolved), MAX(l.dateOfRecordTransaction), l.dateOfRecordTransaction , "
                + "l.amountInvolved, l.estateProperty.estatePropertyId "
                + "FROM EstatePropertyLedger l "
                + "WHERE l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "AND l.deleted = 'NO' "
                //                + "ORDER BY l.ledgerYear "
                + "ORDER BY l.dateOfRecordTransaction ASC ";
        try {
            list = (List<Object[]>) em.createQuery(qry).getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<EstatePropertyLedger> loadLedgerEntriesOnEntryType(EstateProperty ep, int year, DebitCredit debitCredit) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.ledgerYear = " + year + " "
                + "AND l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND l.debitCreditEntry = " + debitCredit.getFullString() + " ";
//                + "ORDER BY l.ledgerYear ASC";
        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EstatePropertyLedger> loadLedgerEntriesBtnYears(EstateProperty ep, int startYear, int endYear, DebitCredit dc) {
        String qry = "SELECT epl FROM EstatePropertyLedger epl "
                + "WHERE epl.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND epl.ledgerYear BETWEEN :startYear AND :endYear "
                + "AND epl.debitCreditEntry = " + dc.getFullString() + " "
                + "ORDER BY epl.ledgerYear ASC ";
        try {
            return em.createQuery(qry)
                    .setParameter("startYear", startYear)
                    .setParameter("endYear", endYear).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get Ledger payments for every month
    public List<EstatePropertyLedger> getLedgerPaymentsForMonth(int year, String month) {
        String qry = "";
        try {
            qry = "SELECT epl.estateProperty.estatePropertyId, epl.dateOfRecordTransaction, epl.amountInvoled FROM EstatePropertyLedger epl "
                    + "WHERE YEAR(epl.dateOfRecordTransaction) = '" + year + "' "
                    + "AND MONTH(epl.dateOfRecordTransaction) = '" + month + "' "
                    + "ORDER BY ep.dateOfRecordTransaction ASC";
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EstatePropertyLedger> loadLedgerEntriesForPropertyOfCurrentOccupant(EstateProperty estateProperty, int year) {
        if (estateProperty == null) {
            return null;
        }

//        PropertyOccupant currentOccupant = estatePropertyCurrentOccupant(estateProperty);
//
//        if (currentOccupant == null) {
//            return new LinkedList<EstatePropertyLedger>();
//        }
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                //                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.ledgerYear DESC, "
                + "l.dateOfRecordTransaction DESC, l.lastModifiedDate DESC";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public List<EstatePropertyLedger> loadLedgerEntriesForPropertyOfCurrentOccupantAsc(EstateProperty estateProperty, int year) {
        if (estateProperty == null) {
            return null;
        }

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                //                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "ORDER BY l.ledgerYear ASC, "
                + "l.dateOfRecordTransaction ASC, l.lastModifiedDate ASC";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public List<EstatePropertyLedger> getRentalArrearsForEstate(EstateProperty ep, Date startDate, Date endDate) {
        List<EstatePropertyLedger> list = null;
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.dateOfRecordTransaction >= :startDate AND l.dateOfRecordTransaction <= :endDate "
                + "AND l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                + "AND l.deleted = 'NO' "
                + "ORDER BY l.dateOfRecordTransaction DESC ";
        try {
            list = (List<EstatePropertyLedger>) em.createQuery(qry)
                    .setParameter("startDate", new java.sql.Date(startDate.getTime()))
                    .setParameter("endDate", new java.sql.Date(endDate.getTime()))
                    .getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    
    public EstatePropertyLedger getRentalLedgerEntryForMonth(EstateProperty ep, DebitCredit dc, int ledgerYear, String selectedMonth){
        EstatePropertyLedger epl = null; 
        try {
            int month = getIntMonth(selectedMonth);
            String qry = "SELECT epl FROM EstatePropertyLedger epl "
                    + "WHERE epl.debitCreditEntry = " + dc.getFullString()+ " "
                    + "AND epl.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                    + "AND epl.ledgerYear = " + ledgerYear+ " "
                    + "AND SQL('EXTRACT(MONTH FROM ?)', epl.dateOfRecordTransaction) = " + month + "";
            
            epl = (EstatePropertyLedger) em.createQuery(qry).setFirstResult(0).setMaxResults(1).getSingleResult();
            return epl;
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return null;
    }

    public List<EstatePropertyLedger> propertyLedgerForAccount(EstateProperty estateProperty, Date beginDate, Date endDate) {

        if (estateProperty == null) {
            return null;
        }

        PropertyOccupant currentOccupant = estatePropertyCurrentOccupant(estateProperty);

        if (currentOccupant == null) {
            return new LinkedList<EstatePropertyLedger>();
        }

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
                + "AND l.dateOfRecordTransaction BETWEEN :beginDate  AND :endDate "
                + "ORDER BY l.ledgerYear, l.dateOfRecordTransaction ASC, l.lastModifiedDate ASC";

        try {
            return em.createQuery(qry).setParameter("beginDate", beginDate, TemporalType.DATE).setParameter("endDate", endDate, TemporalType.DATE).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    //Search  for all bills paid within a specific period 
    public List<EstatePropertyLedger> loadBillsPaidForThisPeriod(PaymentType paymentType, Date beginDate, Date endDate) {

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.dateOfRecordTransaction BETWEEN :beginDate  AND :endDate "
                + "AND l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.debitCreditEntry = " + DebitCredit.CREDIT.getFullString() + " ";
        try {

            return em.createQuery(qry).setParameter("beginDate", beginDate, TemporalType.DATE).setParameter("endDate", endDate, TemporalType.DATE).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }

        return null;

    }

    //For Lessee Statement of Account
    public List<EstatePropertyLedger> loadBillsPaidForCurrentOccupant(EstateProperty estateProperty, int year) {
        if (estateProperty == null) {
            return null;
        }

        PropertyOccupant currentOccupant = estatePropertyCurrentOccupant(estateProperty);

        if (currentOccupant == null) {
            return new LinkedList<EstatePropertyLedger>();
        }

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
                + "AND l.ledgerYear <= " + year + " "
                + "AND l.debitCreditEntry = " + DebitCredit.CREDIT.getFullString() + " "
                + "ORDER BY l.ledgerYear, "
                + "l.dateOfRecordTransaction ASC, l.lastModifiedDate ASC";

//          System.out.println("PRINT_QRY = " + qry);
        try {

            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }

        return null;

    }

    public List<EstatePropertyLedger> loadLedgerEntriesForProperty(String estateID, String blockLetter, String propertyNumber, int year) {
        System.out.println("searhing with .. " + estateID + " " + blockLetter
                + " " + propertyNumber);

        String estateSearch = "";
        String blockSearch = "";
        String assetSearch = "";
        String yearSearch = "";

        if (estateID.isEmpty()) {
            estateSearch = "LIKE '%" + estateID + "%'";
        } else {
            estateSearch = "= '" + estateID + "' ";
        }

        if (blockLetter.isEmpty()) {
            blockSearch = "LIKE '%" + blockLetter + "%'";
        } else {
            blockSearch = "= '" + blockLetter + "' ";
        }

        if (propertyNumber.isEmpty()) {
            assetSearch = "LIKE '%" + propertyNumber + "%'";
        } else {
            assetSearch = "= '" + propertyNumber + "' ";
        }

        if (year != 0) {
            yearSearch = "AND l.ledgerYear <= " + year + " ";
        }

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estateBlock.estate.estateId " + estateSearch + " "
                + "AND l.estateProperty.estateBlock.blockName " + blockSearch + " "
                + "AND l.estateProperty.propertyNumber " + assetSearch + " "
                + "" + yearSearch
                + "ORDER BY l.estateProperty.estateBlock.estate.estateId ,"
                + "l.estateProperty.estateBlock.blockName, "
                + "l.estateProperty.propertyNumber, l.ledgerYear, "
                + "l.dateOfRecordTransaction ASC, l.lastModifiedDate ASC";

//        String qry = "SELECT l FROM EstateAssetLedger l "
//                + "WHERE l.estateAsset.estateBlock.estate.estateId = '"+estateID+"' "
//                + "AND l.estateAsset.estateBlock.blockName = '"+blockLetter+"' "
//                + "AND l.estateAsset.assetNumber = '"+assetNumber+"'";
        try {

            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }

        return null;

    }

    public List<EstatePropertyLedger> findOccupantPropertyLedgersUpToYear(String propertyOccupantId, int year) {
        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.ledgerYear <= " + year + " "
                + "AND l.propertyOccupant.propertyOccupantId = '" + propertyOccupantId + "'";

        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }

        return null;

    }

    public double sumLedgerEntryPropertyForYear(String estatePropertyId, DebitCredit debitCreditEntry, int year) {

        String qryString = "SELECT SUM(l.amountInvolved) FROM EstatePropertyLedger l"
                + " WHERE l.ledgerYear = " + year + " "
                + " AND l.estateProperty.estatePropertyId = '" + estatePropertyId + "' "
                + "AND l.debitCreditEntry = " + debitCreditEntry.getFullString() + "";

        try {
            Double amt = (Double) em.createQuery(qryString).getSingleResult();

            if (amt != null) {
                return amt.doubleValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double sumLedgerEntryForProperty(EstateProperty estateProperty, DebitCredit debitCreditEntry) {
        String qry = "SELECT SUM(l.amountInvolved) FROM EstatePropertyLedger l "
                + "WHERE  l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.debitCreditEntry = " + debitCreditEntry.getFullString() + " ";
        try {
            Double d = (Double) em.createQuery(qry).getSingleResult();
            if (d != null) {
                return d.doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception e) {
        }
        return 0.0;
    }
    
    public double getGroundRentBasicChargee(EstateProperty estateProperty, int yearOfGroundRent) {
        estateProperty = em.find(EstateProperty.class, estateProperty.getEstatePropertyId());  

        String classOfEstate = estateProperty.getEstateBlock().getEstate().getEstateClass();

        String qryClasss = "";

        if (classOfEstate.equalsIgnoreCase(AdConstants.FIRST_CLASS_ESTATE)) {
            qryClasss = "firstClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.SECOND_CLASS_ESTATE)) {
            qryClasss = "secondClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.THHIRD_CLASSE_ESTAEE)) {
            qryClasss = "thirdClassEstateAmountCharged";
        }

        String qryString = "SELECT g." + qryClasss + " FROM GroundRent g"
                + " WHERE g.yearDue = " + yearOfGroundRent
                + " AND g.propertyUsage = " + estateProperty.getPropertyUsage().getFullString() + "";

        try {
            Double amt = (Double) em.createQuery(qryString).getSingleResult();

            if (amt != null) {
                return amt.doubleValue();
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return 0.0;

    }

    public double getGroundRentBasicCharge(EstateProperty estateProperty, int yearOfGroundRent) {
        estateProperty = em.find(EstateProperty.class, estateProperty.getEstatePropertyId());  

        String classOfEstate = estateProperty.getEstateBlock().getEstate().getEstateClass();

        String qryClasss = "";

        if (classOfEstate.equalsIgnoreCase(AdConstants.FIRST_CLASS_ESTATE)) {
            qryClasss = "firstClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.SECOND_CLASS_ESTATE)) {
            qryClasss = "secondClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.THHIRD_CLASSE_ESTAEE)) {
            qryClasss = "thirdClassEstateAmountCharged";
        }

        String qryString = "SELECT g." + qryClasss + " FROM GroundRent g"
                + " WHERE g.yearDue = " + yearOfGroundRent
                + " AND g.propertyUsage = " + estateProperty.getPropertyUsage().getFullString() + "";

        try {
            Double amt = (Double) em.createQuery(qryString).getSingleResult();

            if (amt != null) {
                return amt.doubleValue();
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return 0.0;

    }
    
    
    
    
    
    
    
    public double getGroundRentBasicChargeOnSingleBill(EstateProperty estateProperty) {
        estateProperty = em.find(EstateProperty.class, estateProperty.getEstatePropertyId());  

        String classOfEstate = estateProperty.getEstateBlock().getEstate().getEstateClass();

        String qryClasss = "";

        if (classOfEstate.equalsIgnoreCase(AdConstants.FIRST_CLASS_ESTATE)) {
            qryClasss = "firstClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.SECOND_CLASS_ESTATE)) {
            qryClasss = "secondClassEstateAmountCharged";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.THHIRD_CLASSE_ESTAEE)) {
            qryClasss = "thirdClassEstateAmountCharged";
        }

        String qryString = "SELECT g." + qryClasss + " FROM GroundRent g"
                + " WHERE g.yearDue = " + currentYear
                + " AND g.propertyUsage = " + estateProperty.getPropertyUsage().getFullString() + "";

        try {
            Double amt = (Double) em.createQuery(qryString).getSingleResult();

            if (amt != null) {
                return amt.doubleValue();
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return 0.0;

    }
    
    public double getHouseRentBasicCharge(EstateProperty estateProperty, String month, int year) {
        estateProperty = em.find(EstateProperty.class, estateProperty.getEstatePropertyId());

        String classOfEstate = estateProperty.getEstateBlock().getEstate().getEstateClass();

        String qryClasss = "";

        if (classOfEstate.equalsIgnoreCase(AdConstants.FIRST_CLASS_ESTATE)) {
            qryClasss = "firstClassAmt";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.SECOND_CLASS_ESTATE)) {
            qryClasss = "secondClassAmt";
        } else if (classOfEstate.equalsIgnoreCase(AdConstants.THHIRD_CLASSE_ESTAEE)) {
            qryClasss = "thirdClassAmt";
        }

        String qryString = "SELECT hr." + qryClasss + " FROM HouseRent hr"
                + " WHERE hr.year = " + year
                + "AND hr.monthDue = '" + month + "'"
                + " AND hr.propertyUsage = " + estateProperty.getPropertyUsage().getFullString() + "";

        try {
            Double amt = (Double) em.createQuery(qryString).getSingleResult();

            if (amt != null) {
                return amt.doubleValue();
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return 0.0;
    }

    public List<GroundRent> findGroundRentBetweenYears(int beginYear, int endYear) {
        String qry = "SELECT g FROM GroundRent g "
                + "WHERE g.yearDue  BETWEEN " + beginYear
                + " AND " + endYear + " ORDER BY g.propertyUsage, g.yearDue";

//        System.out.println("qry is   !!!!!!!!!!!!!!!!!!!!!! " + qry);
        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            logg.severe(e.toString());
        }

        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Estate Properties Summaries">
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Object[]> doSearch(PropOccupationType occupationType) {
        String lesseeType = "";
        String qryClass = "";
        String qry = "";
        String rs = "";
        List<Object[]> result = null;
        if (occupationType == PropOccupationType.NONE) {
            qry = "SELECT COUNT(DISTINCT ep.estatePropertyId), ep.estateBlock.estate.estateName, ep.estateBlock.blockName, ep.estateBlock.estateBlockId, ep.propertyUsage, "
                    + "ep.estatePropertyId "
                    + "FROM OccupantProperty op JOIN op.estateProperty ep "
                    + "WHERE op.estateProperty.estatePropertyId = ep.estatePropertyId "
                    + "GROUP BY ep.estateBlock.estate.estateName, ep.estateBlock.blockName, ep.estateBlock.estateBlockId, ep.propertyUsage";
            try {
                result = em.createQuery(qry)
                        .getResultList();

                for (Object[] obj : result) {
                    rs += obj[0] + " " + obj[1] + " " + obj[2] + " " + obj[3] + "\n";
                }

                rs += result.size();
            } catch (Exception e) {
                e.printStackTrace();
                rs = e.toString();
            }
        } else {
            qry = "SELECT COUNT(DISTINCT ep.estatePropertyId), ep.estateBlock.estate.estateName, ep.estateBlock.blockName, ep.estateBlock.estateBlockId, ep.propertyUsage, "
                    + "ep.estatePropertyId "
                    + "FROM OccupantProperty op JOIN op.estateProperty ep "
                    + "WHERE op.estateProperty.estatePropertyId = ep.estatePropertyId "
                    + "AND op.occupationType = :occupationType "
                    + "GROUP BY ep.estateBlock.estate.estateName, ep.estateBlock.blockName, ep.estateBlock.estateBlockId, ep.propertyUsage";

            try {
                result = em.createQuery(qry)
                        .setParameter("occupationType", occupationType)
                        .getResultList();

                for (Object[] obj : result) {
                    rs += obj[0] + " " + obj[1] + " " + obj[2] + " " + obj[3] + "\n";
                }

                rs += result.size();
            } catch (Exception e) {
                e.printStackTrace();
                rs = e.toString();
            }
        }

        return result;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Entire Ledger Sheet">
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Object[]> prepareEntireLedgerSheet(int ledgeYear, boolean addPrevious, PaymentType paymentType) {

        String ledgerYearConstraints = "";

        String paymentTypeConst = "";

        if (addPrevious == true) {
            ledgerYearConstraints = " <= ";
        } else {
            ledgerYearConstraints = " = ";
        }

        if (paymentType == null) {
            paymentTypeConst = "";
        } else {
            paymentTypeConst = "AND l.paymentType = " + paymentType.getFullString();
        }

        String qry = "SELECT l.estateProperty.estateBlock.estate.estateName, "
                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry, "
                + " SUM(l.amountInvolved), COUNT(DISTINCT l.estateProperty), l.dateOfRecordTransaction "
                + "FROM EstatePropertyLedger l "
                + "WHERE l.ledgerYear " + ledgerYearConstraints + " " + ledgeYear + " "
                + "" + paymentTypeConst + " "
                + "GROUP BY  "
                + "l.estateProperty.estateBlock.estate.estateName, "
                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry";

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

//    @TransactionAttribute(TransactionAttributeType.NEVER)
//    public List<Object[]> prepareEntireLedgerSheetForMonth(int ledgeYear, String month ,boolean addPrevious, PaymentType paymentType) {
//
//        String ledgerYearConstraints = "";
//
//        String paymentTypeConst = "";
//
//        if (addPrevious == true) {
//            ledgerYearConstraints = " <= ";
//        } else {
//            ledgerYearConstraints = " = ";
//        }
//
//        if (paymentType == null) {
//            paymentTypeConst = "";
//        } else {
//            paymentTypeConst = "AND l.paymentType = " + paymentType.getFullString();
//        }
//
//
//        String qry = "SELECT l.estateProperty.estateBlock.estate.estateName, "
//                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry, "
//                + " SUM(l.amountInvolved), COUNT(DISTINCT l.estateProperty), l.dateRecordTransaction "
//                + "FROM EstatePropertyLedger l "
//                //+ "WHERE MONTH (l.dateOfRecordTransaction) = " + month + " "
//                + "WHERE l.ledgerYear " + ledgerYearConstraints + " " + ledgeYear + " "
//                + "" + paymentTypeConst + " "
//                + "GROUP BY  "
//                + "l.estateProperty.estateBlock.estate.estateName, "
//                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry ";
//
//        String rs = "";
//
//        List<Object[]> result = new LinkedList<Object[]>();
//
//        try {
//            result = em.createQuery(qry).getResultList();
//            
//            StringUtil.pringListArray(result);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            rs = e.toString();
//        }
//
//        return result;
//    }
    public List<Object[]> prepareDetailEstateProperty(int ledgerYear, PaymentType paymentType, boolean addPrevious, Estate estate) {
        String ledgerYearConstraints = "";

        String paymentTypeConst = "";

        if (addPrevious == true) {
            ledgerYearConstraints = " <= ";
        } else {
            ledgerYearConstraints = " = ";
        }

        if (paymentType == null) {
            paymentTypeConst = "";
        } else {
            paymentTypeConst = "AND l.paymentType = " + paymentType.getFullString();
        }

        List<Object[]> result = new LinkedList<>();
        String qry = "SELECT l.estateProperty.estateBlock.blockName , l.debitCreditEntry, SUM(l.amountInvolved) "
                + " l.estateProperty.estatePropertyId, l.estateProperty.propertyNumber, l.estateProperty.estateBlock.estate.estateName"
                + "FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estateBlock.estate.estateName.estateId = '" + estate.getEstateId() + " '"
                + "AND l.ledgerYear " + ledgerYearConstraints + " " + ledgerYear + " "
                + "" + paymentTypeConst + " "
                + "GROUP BY l.estateProperty.estateBlock.blockName "
                + "ORDER BY l.estateProperty.propertyNumber ASC ";
        try {
            result = em.createQuery(qry).getResultList();
            StringUtil.pringListArray(result);
            StringValueComparator.sort(result);
        } catch (Exception e) {
        }
        return result;

    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Return of Arreas - quick">

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<Object[]> prepareEntireLedgerSheetByNames(int ledgeYear, boolean addPrevious, PaymentType paymentType) {

        String ledgerYearConstraints = "";

        if (addPrevious == true) {
            ledgerYearConstraints = " <= ";
        } else {
            ledgerYearConstraints = " = ";
        }

        String qry = "SELECT l.estateProperty.estatePropertyId,"
                + "l.estateProperty.estateBlock.estate.estateName, "
                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry, "
                + " SUM(l.amountInvolved), COUNT(DISTINCT l.estateProperty) "
                + "FROM EstatePropertyLedger l "
                + "WHERE l.ledgerYear " + ledgerYearConstraints + " " + ledgeYear + " "
                + "AND l.paymentType = " + paymentType.getFullString() + " "
                + "GROUP BY  "
                + "l.estateProperty.estateBlock.estate.estateName, "
                + "l.estateProperty.estateBlock.blockName, l.debitCreditEntry";

        List<Object[]> result = new LinkedList<Object[]>();

        try {
            result = em.createQuery(qry).getResultList();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Properties of Estate">
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> allEstatesBlock(String estateId) {

        String qry = "SELECT p FROM EstateProperty p "
                + "WHERE p.estateBlock.estate.estateId LIKE '%" + estateId + "%' "
                + "AND p.deleted = 'NO' "
                + "ORDER BY p.estateBlock.blockName, p.propertyNumber ";

        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return new LinkedList<EstateProperty>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Find LESSEE Estate Properties By Id">
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> findLesseeEstatePropertyById(String propertyOccupantId) {
        String qry = "SELECT lp.estateProperty FROM  OccupantProperty lp "
                + "WHERE lp.propertyOccupant.propertyOccupantId = '" + propertyOccupantId + "'";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<EstateProperty>(0);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Find LESSEE Properties By Id">
    public List<Occupancy> findOccupantPropertiesById(String propertyOccupantId) {
        String qry = "SELECT lp FROM  OccupantProperty lp "
                + "WHERE lp.propertyOccupant.propertyOccupantId = '" + propertyOccupantId + "'";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Find Occupied Properties">
    public List<EstateProperty> occupiedEstatePropertiesByDate(String estateBlockId, PropOccupationType occupationType, Date checkDate) {

        String qry = "SELECT l.estateProperty FROM OccupantProperty l "
                + "WHERE l.occupationType =  " + occupationType.getFullString() + " "
                + "AND l.estateProperty.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.firstDateOfOccupancy ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);

            System.out.println(qry);
            return query.getResultList();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Find Occupied Occupant Properties">
    public List<EstateProperty> currentOccupiedProperties(String estateBlockId, PropOccupationType occpationType) {
        return occupiedEstatePropertiesByDate(estateBlockId, occpationType, new Date());
    }

    public List<EstateProperty> currentOccupiedPropertiesOnLeaseHold(String estateBlockId) {
        return occupiedEstatePropertiesByDate(estateBlockId, PropOccupationType.Leasehold, new Date());
    }

    public List<Occupancy> occupiedOccpantPropertiesByDate(String estateBlockId, PropOccupationType occupationType, Date checkDate) {

        String qry = "SELECT l FROM OccupantProperty l "
                + "WHERE l.occupationType = " + occupationType.getFullString() + " "
                + "AND l.estateProperty.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.firstDateOfOccupancy ASC";

        System.out.println(qry);

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);

            return query.getResultList();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        System.out.println("error occured i rented propop");
        return null;
    }
    
    public List<Occupancy> getPropertyOccupantByEstateProperty(String estateProperty){
        String query = "SELECT o.estateProperty FROM OccupantProperty o "
                + "WHERE o.estateProperty = " + estateProperty  +  "";
        System.out.println(query);
        
        try {
              List<Occupancy>  getEstateProperty;
             getEstateProperty =   em.createQuery(query).getResultList();
             return getEstateProperty;
        } catch (Exception e) {
            e.printStackTrace();
          Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString()); 
          return null;
        }
    }

    // </editor-fold>
    
    public List<Occupancy> currentOccupiedOccpantProperties(String estateBlockId, PropOccupationType occupationType) {
        return occupiedOccpantPropertiesByDate(estateBlockId, occupationType, new Date());
    }

    // <editor-fold defaultstate="collapsed" desc="Estate Block Rented Properties">
    public List<EstateProperty> rentedEstatePropertiesByDate(String estateBlockId, Date checkDate) {
        String qry = "SELECT l.estateProperty FROM OccupantProperty l "
                + "WHERE l.occupationType = com.sabonay.advantage.common.constants.PropOccupationType.Rental "
                + "AND l.estateProperty.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.estateProperty.estatePropertyId ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);

            return query.getResultList();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public List<EstateProperty> leaseHoldEstatePropertiesByDate(String estateBlockId, Date checkDate) {
        String qry = "SELECT l.estateProperty FROM OccupantProperty l "
                //                + "WHERE l.occupationType = com.sabonay.advantage.common.constants.PropOccupationType.Leasehold "
                + "WHERE l.estateProperty.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "AND l.firstDateOfOccupancy <=:checkDate "
                + "AND l.lastDateOfOccupancy IS NULL "
                + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.estateProperty.estatePropertyId ASC";

        try {
            Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);

            return query.getResultList();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    public List<EstateProperty> findEstatePropertiesByBlock(String estateBlockId) {
        String qry = "SELECT ep FROM EstateProperty ep "
                + "WHERE ep.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "AND ep.deleted = 'NO' "
                + "ORDER BY ep.estatePropertyId ASC";
        try {
            Query query = em.createQuery(qry);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EstateProperty> findEstatePropertiesByBlock1(String estateBlockId) {
        String qry = "SELECT ep FROM EstateProperty ep "
                + "WHERE ep.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%' "
                + "ORDER BY ep.estatePropertyId ASC";
        try {
            Query query = em.createQuery(qry);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="comment">
    public List<EstateProperty> occupiedProperties(String estateBlockId, Date checkDate, PropOccupationType occupationType, PropertyUsage propertyUsage) {
        String propOccpationQry = "";
        String propUsage = "";

        if (occupationType != null) {
            propOccpationQry = " l.occupationType = " + occupationType.getFullString();
        }

//        if (propertyUsage.equals(PropertyUsage.ALL)) {
//            propUsage = "";
//        }
        //else {
            propUsage = " AND l.estateProperty.propertyUsage = " + propertyUsage.getFullString() + " ";
       // }
        System.out.println("property Usage in query " + propertyUsage);

        String qry = "SELECT l.estateProperty FROM OccupantProperty l "
                + "WHERE " + propOccpationQry
                + " AND l.estateProperty.estateBlock.estate.estateId = '" + estateBlockId + "' "
                + "" + propUsage + " "
                + "AND l.firstDateOfOccupancy <=:checkDate "
             //   + "AND l.lastDateOfOccupancy IS NULL "
              //  + "OR l.lastDateOfOccupancy >=:checkDate "
                + "ORDER BY l.estateProperty.estatePropertyId ASC";

        try {
           // Query query = em.createQuery(qry).setParameter("checkDate", checkDate, TemporalType.DATE);
            Query query = em.createQuery(qry);
                 
                    
            
            
            return query.setParameter("checkDate", checkDate, TemporalType.DATE).getResultList();

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }
        return null;
    }
    
    public List<Bills> load_all_paid_bills_to_report() {
        String status_unpaid = "unpaid";
        String qry = "SELECT b FROM Bills b "
                + "WHERE b.billStatus = 'paid' "
                //                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
               // + "AND l.ledgerYear <= " + year + " "
                + "AND b.deleted = 'NO' ";
        
        System.out.println("Unpaid Query ......................." + qry);
        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            
            logg.log(Level.SEVERE, e.toString());
             return null;
        }
       
    }

    // </editor-fold>
     
    // <editor-fold defaultstate="collapsed" desc="Current Rented Properties">
    public List<EstateProperty> currentRentedEstateProperties(String estateBlockId) { 
        return rentedEstatePropertiesByDate(estateBlockId, new Date());
    }

    public List<EstateProperty> currentLeaseHoldEstateProperties(String estateBlockId) {
        return leaseHoldEstatePropertiesByDate(estateBlockId, new Date());
    }
    // </editor-fold>

    public List<EstateProperty> getAllOccupiedPropperty() {
        String qry = "SELECT ep FROM EstateProperty ep "
                + "LEFT JOIN ep.occupantPropertyList op "
                + "WHERE op.estateProperty IS NULL";

        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    public List<EstateProperty> estateBlockUnOccupiedProperties(String estateBlockId) {
        String qry = "SELECT ep FROM EstateProperty ep "
                + "LEFT JOIN ep.occupantPropertyList op "
                + "WHERE op.estateProperty IS NULL "
                + "AND ep.estateBlock.estateBlockId LIKE '%" + estateBlockId + "%'";

        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="Rented Properties Occupants">
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Rented Properties Last Rent">
    public Double propertyLastRentBill(String propertyID) {

        Occupancy op = currentOccupancyByPropertyId(propertyID);

        if (op == null) {
            return null;
        }
        try {
            
        EstatePropertyLedger propertyLedger = lastDebitLedgerEntryForProperty(propertyID);

        if (propertyLedger != null) {
            return propertyLedger.getAmountInvolved();
        }

        return null;
        } catch (Exception e) {
         return null;
        }
    }

    public Double rentedPropertyLastRentBill(String propertyID) {  

        try {
        Occupancy op = currentOccupancyByPropertyId(propertyID);

        if (op == null) {
            return null;
        }

        if (op.getOccupationType() != PropOccupationType.Rental) {
            return null;
        }
            System.out.println("jux before............");
        EstatePropertyLedger propertyLedger = lastDebitLedgerEntryForProperty(propertyID);

        if (propertyLedger != null) {
            return propertyLedger.getAmountInvolved();
        }

        return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double leasedPropertyLastGroundRentBill(String propertyId) {
        try {
            Occupancy op = currentOccupancyByPropertyId(propertyId);
            if (op == null) {
                return null;
            }
            if (op.getOccupationType() != PropOccupationType.Leasehold) {
                return null;
            }
            EstatePropertyLedger propertyLedger = lastDebitLedgerEntryForProperty(propertyId);
            if (propertyId != null) {
                propertyLedger.getAmountInvolved();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // </editor-fold>
    
    public EstatePropertyLedger lastDebitLedgerEntryForProperty(String estatePropertyID) {
        EstatePropertyLedger epl = null;
        String qry = "SELECT pl FROM EstatePropertyLedger pl "
                + "WHERE pl.debitCreditEntry = " + DebitCredit.DEBIT.getFullString() + " "
                + "AND pl.estateProperty.estatePropertyId = '" + estatePropertyID + "' "
                + "ORDER BY pl.dateOfRecordTransaction DESC";

        try {
            epl = (EstatePropertyLedger) em.createQuery(qry).setMaxResults(1).setFirstResult(0).getSingleResult(); 
            return epl;
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }

    public List<GroundRent> lastGroundRentForYear(int selectedYear){
        List<GroundRent> groundRents = null ;
        String qryString = "select e from GroundRent e where e.yearDue = '"+selectedYear+"'";
        System.out.println("query "+ qryString);
       groundRents = em.createQuery(qryString).getResultList();
        return groundRents;
    }
    
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public EstatePropertyLedger monthDebitLedgerEntryForProperty(String estatePropertyId, String month, int year) {
        String qry = "SELECT pl FROM EstatePropertyLedger pl "
                + "WHERE pl.debitCreditEntry = " + DebitCredit.DEBIT.getFullString() + " "
                + "AND pl.estateProperty.estatePropertyId = '" + estatePropertyId + "' "
                + "AND MONTH (pl.dateOfRecordTransaction) = '" + month + "' "
                + "AND pl.ledgerYear = " + year + "";
        try {
            return (EstatePropertyLedger) em.createQuery(qry).setMaxResults(1).setFirstResult(1).getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Find Number of PSU in Estate Block">
    public int numberOfEstatePSUinEstateBlock(String esateBlockId) {

        String qry = "SELECT COUNT(ep.estatePropertyId) "
                + "FROM EstateProperty ep "
                + "WHERE ep.estateBlock.estateBlockId = '" + esateBlockId + "' "
                + "AND ep.estatePropertyId LIKE '%" + AdConstants.PSU_INITIALS + "%'";

        try {
            long number = (Long) em.createQuery(qry).getSingleResult();

            return (int) number;

        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return 0;
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Unverified Record">
    public List<EstateProperty> unverifiedRecords(String estateId) {

        String q = "SELECT ar.estateProperty FROM ArreasRecord ar "
                + "WHERE ar.estateProperty.estatePropertyId LIKE '" + estateId + "%' "
                + "AND ar.verified = 0";

        List<EstateProperty> estatePropertyList = new LinkedList<>();

        try {
            System.out.println(q);

            List<EstateProperty> estatePropertys = em.createQuery(q).getResultList();
                    //                    .setParameter("state", false)

            return estatePropertys;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.toString());
        }

        return estatePropertyList;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Estate Property Ledger Summation For PaymentType">
    public List<Object[]> getLegerSummationForPaymenttype(EstateProperty estateProperty, PaymentType paymentType, int ledgerYear) {
        PropertyOccupant currentOccupant = estatePropertyCurrentOccupant(estateProperty);

        if (currentOccupant == null) {
            return new LinkedList<>();
        }

        String qry = "SELECT l.debitCreditEntry, SUM(l.amountInvolved), MAX(l.dateOfRecordTransaction), l.dateOfRecordTransaction , l.amountInvolved "
                + "FROM EstatePropertyLedger l "
                + "WHERE l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.propertyOccupant.propertyOccupantId = '" + currentOccupant.getPropertyOccupantId() + "' "
              //  + "AND l.ledgerYear = " + ledgerYear + " "
               //  + "AND l.ledgerYear = " + ledgerYear + " "
                + "AND l.ledgerYear <= " + ledgerYear + " "
                + "AND l.deleted = 'NO' "
                + "GROUP BY l.estateProperty.estatePropertyId, l.debitCreditEntry "
                + "ORDER BY l.ledgerYear, "
                + "l.dateOfRecordTransaction ASC";

        try {
               //boolean state = em.createQuery(qry).getResultList();
            synchPCnDB();
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return new LinkedList<>();
    }

    public List<Object[]> getLedgerPaymentForType(EstateProperty estateProperty, PaymentType paymentType, int ledgerYear) {
        PropertyOccupant propertyOccupant = estatePropertyCurrentOccupant(estateProperty);
        if (propertyOccupant == null) {
            return new LinkedList<>();
        }

        String qry = "SELECT l.debitCreditEntry, l.amountInvolved, l.dateOfRecordTransaction FROM EstatePropertyLedger l "
                + "WHERE l.paymentType = " + paymentType.getFullString() + " "
                + "AND l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear <= " + ledgerYear + " "
                + "GROUP BY l.estateProperty.estatePropertyId, l.debitCreditEntry "
                + "ORDER BY l.ledgerYear ASC";
        try {
            return em.createQuery(qry).getResultList();
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.getMessage());
        }
        return new LinkedList<>();
    }

    public Integer getFirstEstateProperyLedgeryear(EstateProperty estateProperty) {
//        PropertyOccupant propertyOccupant = estatePropertyCurrentOccupant(estateProperty);
//        if(propertyOccupant == null){
//            return null;
//        }
        String qry = "SELECT MIN(l.ledgerYear) FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                //                + "AND l.propertyOccupant.propertyOccupantId = '" + propertyOccupant.getPropertyOccupantId() + "' "
                + "ORDER BY l.ledgerYear ASC";
        try {
            Integer integer = (Integer) em.createQuery(qry).getSingleResult();
            if (integer == null) {
                return 0;
            }
            return integer.intValue();
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Double getLedgerByYearPaidFor(EstateProperty estateProperty, int yearPaidFor, DebitCredit debitCredit) {
//        PropertyOccupant propertyOccupant = estatePropertyCurrentOccupant(estateProperty);
//
//        if (propertyOccupant == null) {
//            return null;
//        }

        String qry = "SELECT SUM(epl.amountInvolved) FROM EstatePropertyLedger epl "
                + "WHERE epl.debitCreditEntry = " + debitCredit.getFullString() + " "
                + "AND epl.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND epl.ledgerYear = " + yearPaidFor;
        try {
//            Object object = em.createQuery(qry).getSingleResult();
            Number number = (Number) em.createQuery(qry).getSingleResult();
            if (number == null) {
                return 0.0;
            }
            return number.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public GroundRent searchPropertyUseage(EstateProperty estateProperty, int currentYear) {
        String srl = "SELECT g FROM GroundRent g WHERE g.propertyUsage= " + estateProperty.getPropertyUsage().getFullString() + " "
                + "AND g.yearDue= '" + currentYear + "' ";

        try {
            return (GroundRent) em.createQuery(srl).getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Current Ledger Summation">
    public List<Object[]> getCurrentLegerSummation(EstateProperty estateProperty, int ledgerYear) {
        Occupancy occupancy = estateProperty.getOccupancy();

        if (occupancy != null) {
            return getLegerSummationForPaymenttype(estateProperty, occupancy.getAppropriatePaymentType(), ledgerYear);
        }

        return new LinkedList<>();
    }

    public List<EstatePropertyLedger> getEstatePropertyLedger(EstateProperty ep) {
        Occupancy op = ep.getOccupancy();
        if (op != null) {
            return loadEstatePropertyLegders(ep);
        }
        return new LinkedList<>();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rental Charges for date">
    public double rentCharges(EstateProperty estateProperty, Date date) {

        EstatePropertyLedger ledger = new EstatePropertyLedger();
        ledger.setDebitCreditEntry(DebitCredit.DEBIT);
        ledger.setLedgerYear(DateTimeUtils.getYearInDate(date));
        ledger.setEstateProperty(estateProperty);
        ledger.setPaymentType(PaymentType.HOUSE_RENT);

        try {

            IDCreator.setEstateLedgerId(ledger, DateTimeUtils.getMonthLongName(date));

            System.out.println("\t" + ledger.getEstatePropertyLedgerId());
            ledger = em.find(EstatePropertyLedger.class, ledger.getEstatePropertyLedgerId());

            return ledger.getAmountInvolved();
        } catch (Exception e) {
//            Logger.getLogger(CustomDataSource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return 0;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Check Estate Property Usage">
    public boolean hasPropertyBeenUsed(String estatePropertyId) {
        String qry = "SELECT op FROM OccupantProperty op "
                + "where op.estateProperty.estatePropertyId = '" + estatePropertyId + "'";

        try {
            List<Occupancy> ops = em.createQuery(qry).getResultList();
            if (ops.isEmpty() == false) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Check Estate Block Usage">
    public boolean hasBlockBeenUsed(String estateBlockId) {
        String qry = "SELECT eb.estateBlock.estateBlockId FROM EstateProperty eb "
                + "where eb.estateBlock.estateBlockId = '" + estateBlockId + "'";

        try {
            List<EstateProperty> blks = em.createQuery(qry).getResultList();
            if (!blks.isEmpty() == true) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasPropertyBeenAssigned(String estatePropertyNumber, String blockName) {
        String qry = "SELECT eb.estatePropertyNumber FROM EstateProperty eb"
                + "WHERE eb.propertyNumber = ' " + estatePropertyNumber + "' ";
        //+ "AND eb.estateBlock.blockName = ' " + blockName + "' ";
        try {
            List<EstateProperty> ppts = em.createQuery(qry).getResultList();
            if (ppts.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Check Estate Usage">
    public boolean hasEstateBeenUsed(String estateId) {
        String qry = "Select eb FROM EstateBlock eb "
                + "WHERE eb.estate.estateId = '" + estateId + "' ";
        try {
            List<EstateBlock> ebList = em.createQuery(qry).getResultList();
            if (ebList.isEmpty()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This functionality will check for the existence of an Estate block
     * @param estateId the specific Estate Id the block is affiliated
     * @param blockName the Estate block name to check for its existence
     * @return true if the estate already exist or false if otherwise
     */
    public boolean doesEstateBlockExist(String estateId, String blockName) {
        String qryString = "SELECT eb FROM EstateBlock eb "
                + "WHERE eb.blockName = '" + blockName + "' "
                + "AND eb.estate.estateId = '" + estateId + "' ";
        try {
            List<EstateBlock> ebList = em.createQuery(qryString).getResultList();
            return !ebList.isEmpty();
            
        } catch (Exception e) {
            System.out.println("System was unable to check for Estate Block Existence");
            System.out.println("From : ");
            System.out.println("CustomDataSource.doesEstateBlockExist()");
            logg.severe(e.getMessage());
            return true;
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Does Property Occupant Have records">
    public boolean doesOccupantOccupyAProperty(String propertyOccupantId) {
        String qry = "SELECT op FROM OccupantProperty op "
                + "where op.propertyOccupant.propertyOccupantId = '" + propertyOccupantId + "'";

        try {
            List<Occupancy> ops = em.createQuery(qry).getResultList();
            if (ops.isEmpty() == false) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Does Property Occupant Have records in estate ledger">
    public boolean doesOccupantHaveRecordsInLedger(String propertyOccupantId) {
        String qry = "SELECT pl FROM EstatePropertyLedger pl "
                + "where pl.propertyOccupant.propertyOccupantId = '" + propertyOccupantId + "'";

        try {
            List<EstatePropertyLedger> ops = em.createQuery(qry).getResultList();
            if (ops.isEmpty() == false) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Scheduled Jobs">
    @Schedule(hour = "0", year = "*", month = "Dec", dayOfMonth = "Last", persistent = false)
    public void automaticGrndRentBilling() {
        System.out.println("we are automatically billing for the year");
        try {
            List<Estate> estateList = sHCCRUDManager.estateGetAll(false);
            List<GroundRent> grndRentForYear = sHCCRUDManager.groundRentFindByAttribute("yearDue", currentYear, "NUMBER", false);
            for (Estate es : estateList) {
                List<EstateProperty> estatePropertyList = es.getEstatePropertiesList();
                for (EstateProperty ep : estatePropertyList) {
                    if (!grndRentForYear.isEmpty()) {
                        GroundRentBiller biller = new GroundRentBiller();
                        biller.setEstateProperty(ep);
                        biller.setSelectedYear(currentYear);
                        biller.billPropertyGroud();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void autoSendGrndRentBills() {
        try {
        } catch (Exception e) {
        }
    }

    public void autoSendHouseRentBills() {
        try {
        } catch (Exception e) {
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UserAccounts">
    public boolean doesStaffHaveAccountWithSameAccessRight(String staffId, Integer accessRightId) {
        String qry = "SELECT ua FROM UserAccount ua "
                + "WHERE ua.staff.staffId = '" + staffId + "' "
                + "AND ua.userCategory.id = " + accessRightId + " ";
        try {
            List<UserAccount> uas = em.createQuery(qry).getResultList();
            if (uas.isEmpty() == false) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Occupant Contacts Functionalities">
    public List<SMSContact> getOccupantContacts(ContactGroup contactGroup, String grpValue, String grpValue1) {
        String qry = "";
        switch (contactGroup) {
            case ALL_OCCUPANTS:
                qry = "SELECT op.estateProperty.estatePropertyId, op.propertyOccupant.telephoneNumber FROM OccupantProperty op ";
                break;
            case ESTATE:
                qry = "SELECT op.estateProperty.estatePropertyId, op.propertyOccupant.telephoneNumber FROM OccupantProperty op "
                        + "WHERE op.estateProperty.estateBlock.estate.estateName = '" + grpValue + "' ";
                break;
            case BLOCK:
                qry = "SELECT op.estateProperty.estatePropertyId, op.propertyOccupant.telephoneNumber FROM OccupantProperty op "
                        + "WHERE  op.estateProperty.estateBlock.blockName = '" + grpValue + "' "
                        + "AND  op.estateProperty.estateBlock.estate.estateName = '" + grpValue1 + "' ";
                break;
            case INDIVIDUAL:
                qry = "SELECT op.estateProperty.estatePropertyId, op.propertyOccupant.telephoneNumber FROM OccupantProperty op "
                        + "WHERE op.estateProperty.estatePropertyId = '" + grpValue + "' ";
                break;
        }
        try {
            List<Object[]> list = em.createQuery(qry).getResultList();
            List<SMSContact> smsContactList = new ArrayList<>(list.size());
            for (Object[] objs : list) {
                smsContactList.add(new SMSContact(objs));
            }
            System.out.println("smsContactList " + smsContactList);
            return smsContactList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Is RentalBill Set for Current Month And Year">
    public boolean isRentalUnitBilledForCurrentMonth(String month, int year) {
        String qry = "";
        List<EstateProperty> rentedPropertyList = null;

        try {
            qry = "SELECT epl.estate_property FROM estate_property_ledger AS epl "
                    + "WHERE epl.payment_type = '" + PaymentType.HOUSE_RENT + "' "
                    + "AND MONTHNAME(epl.date_of_record_transaction) = '" + month + "' "
                    + "AND epl.ledger_year = " + year + " ";
            rentedPropertyList = em.createNativeQuery(qry).getResultList();
            System.out.println("rentedProperyList " + rentedPropertyList);
            if (rentedPropertyList.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Block Ledger For EstateProperty">
    public boolean blockLedgerForEstateProperty(EstateProperty ep) {
        //query to delete epl for selected estate property
        String qry = "UPDATE EstatePropertyLedger l SET l.deleted = 'YES' "
                + "WHERE l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' ";

        //query to delete estate property for selected estate
        String qry1 = "UPDATE EstateProperty ep SET ep.deleted = 'YES' "
                + "WHERE ep.estatePropertyId = '" + ep.getEstatePropertyId() + "' ";
        try {
            Query query = em.createQuery(qry);
            Query query1 = em.createQuery(qry1);
            query1.executeUpdate();
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    public boolean unBlockLedgerForEstateProperty(EstateProperty ep) {
        String qry = "UPDATE EstatePropertyLedger l SET l.deleted = 'NO' "
                + "WHERE l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' ";

        //query to delete estate property for selected estate
        String qry1 = "UPDATE EstateProperty ep SET ep.deleted = 'NO' "
                + "WHERE ep.estatePropertyId = '" + ep.getEstatePropertyId() + "' ";
        try {
            Query query = em.createQuery(qry);
            Query query1 = em.createQuery(qry1);
            query1.executeUpdate();
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Return of Arrears">
     /*
     this method parses a string month to an int
     */
    @PrePersist
    private int getIntMonth(String monthName) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
            DateTime dateTime = formatter.withLocale(Locale.ENGLISH).parseDateTime(monthName);
            return dateTime.getMonthOfYear();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /*
     return of arrears for rental units
     */
    /*
     select estate ledger based on report type, report param, payment type, payment entry type and ledger year
     */

    public Double generateEstateArrears(EstateProperty ep, String reportType, String reportParam, DebitCredit debitCredit, int ledgerYear) {
        try {
            //we use the EXTRACT FUNC to retreive year, month and day component of the dateOfRecordTransaction column in DB
            String qry = "SELECT SUM(l.amountInvolved) FROM EstatePropertyLedger l WHERE l.estateProperty.estatePropertyId = '" + ep.getEstatePropertyId() + "' "
                    + "AND l.ledgerYear <= " + ledgerYear + " "
                    + "AND l.debitCreditEntry = " + debitCredit.getFullString() + " ";
            if (reportType.equalsIgnoreCase("Monthly Report")) {
                int month = getIntMonth(reportParam);

                qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= " + month + " "
                        + "ORDER BY l.dateOfRecordTransaction ASC";
                return (Double) em.createQuery(qry).getSingleResult();
            } else if (reportType.equalsIgnoreCase("Quarterly Report")) {
                if (reportParam.equalsIgnoreCase("1st Quarter")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 3 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                } else if (reportParam.equalsIgnoreCase("2nd Quarter")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 6 "
                            //+ "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) < 6 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                } else if (reportParam.equalsIgnoreCase("3rd Quarter")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 9 "
                            //+ "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) < 9 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                } else if (reportParam.equalsIgnoreCase("4th Quarter")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 8 "
                            //+ "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) < 12 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                }
            } else if (reportType.equalsIgnoreCase("Mid Year Report")) {
                if (reportParam.equalsIgnoreCase("1st Mid Year")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 6 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                } else if (reportParam.equalsIgnoreCase("2nd Mid Year")) {
                    qry += "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 12 "
                            //+ "AND SQL('EXTRACT(MONTH FROM ?)', l.dateOfRecordTransaction) <= 12 "
                            + "ORDER BY l.dateOfRecordTransaction ASC";
                    return (Double) em.createQuery(qry).getSingleResult();
                }
            } else if (reportType.equalsIgnoreCase("Annual Report")) {
                return (Double) em.createQuery(qry).getSingleResult();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//</editor-fold>
    
    
    
    
    
    
    
    
    
//    NEW QUERIES ON SINGLE BILLING
    
    
    public Estate getEstateIdByEstateName(Estate EstateName) {
        String qry = "SELECT e.estateId FROM Estate "
                + "WHERE e.estateName = '" + EstateName + "' "
               // + "AND l.ledgerYear <= " + year + " "
                + "AND e.deleted = 'NO' " ;
               // + "ORDER BY l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
              return (Estate) em.createQuery(qry).getSingleResult();
           // return em.createQuery(qry).getSingleResult().toString();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
      
    public String getEstateIdByEstateNameMethodString(Estate estateName) {
        String qry = "SELECT e.estateId FROM Estate e "
                + "WHERE e.estateName = '" + estateName.getEstateName() + "' "
                
                + "AND e.deleted = 'NO' " ;
                

          System.out.println("PRINT_QRY = " + qry);
        try {
            System.out.println("......................."+(String)em.createQuery(qry).getSingleResult());
              return (String) em.createQuery(qry).getSingleResult();
           // return em.createQuery(qry).getSingleResult().toString();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
      
    public String getEstateBlockMethodString(EstateBlock estateBlock) {
        String qry = "SELECT e.estateBlockId FROM EstateBlock "
                + "WHERE e.estateBlockId = '" + estateBlock + "' ";
               // + "AND l.ledgerYear <= " + year + " "
               // + "AND l.deleted = 'NO' " ;
               // + "ORDER BY l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
              return (String) em.createQuery(qry).getSingleResult();
           // return em.createQuery(qry).getSingleResult().toString();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
        
    public String getEstatePropertyNumber(EstateProperty estateproperyNumber) {
        String qry = "SELECT e.estate_property_id FROM EstatePropery "
                + "WHERE e.propertyNumber = '" + estateproperyNumber + "' ";
               // + "AND l.ledgerYear <= " + year + " "
               // + "AND l.deleted = 'NO' " ;
               // + "ORDER BY l.dateOfRecordTransaction ASC ";

//          System.out.println("PRINT_QRY = " + qry);
        try {
              return (String) em.createQuery(qry).getSingleResult();
           // return em.createQuery(qry).getSingleResult().toString();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
      
    public PropertyOccupant getPropertiOccupantByEstatePropertyID(String estatePropertyId) {
        String qry = "SELECT o.propertyOccupant FROM OccupantProperty o "
                + "WHERE o.estateProperty.estatePropertyId = '" + estatePropertyId + "' ";
               
        System.out.println("PRINT_QRY = " + qry);
        try {
            System.out.println  ("..........."+(PropertyOccupant) em.createQuery(qry).getSingleResult());
            return (PropertyOccupant) em.createQuery(qry).getSingleResult();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
      
    public String getPropertyUsageByEstatePropertyId(String stringId){
          String qry = "SELECT o.propertyUsage FROM EstateProperty o"
               // + " e.estateId FROM Estate "
                + "WHERE o.estatePropertyId = '" + stringId + "' ";
               // + "AND l.ledgerYear <= " + year + " "
               // + "AND l.deleted = 'NO' " ;
               // + "ORDER BY l.dateOfRecordTransaction ASC ";
            try {
            return (String) em.createQuery(qry).getSingleResult();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
          
      }
      
      
    public List<EstatePropertyLedger> getCalculatedBillForYear(EstateProperty estateProperty, int year) {
        if (estateProperty == null) {
            System.out.println("estate property is null");
            return null;
        }

        String qry = "SELECT l FROM EstatePropertyLedger l "
                + "WHERE l.estateProperty.estatePropertyId = '" + estateProperty.getEstatePropertyId() + "' "
                + "AND l.ledgerYear = " + year + " AND l.debitCreditEntry = " + DebitCredit.DEBIT.getFullString() + "";

        try {
            return em.createQuery(qry).getResultList();

        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
        }
        return null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Staffs Operations">
    
    /**
     * This method gets all staffs with the status active from the database
     * @return collection object (List) of Staff
     */
    public List<Staff> getActiveStaffs(){
        try {
            String qry = "SELECT acs FROM Staff acs WHERE acs.status.status = 'Active' ORDER BY acs.firstName ASC, acs.lastName ASC";
            synchPCnDB();
            return (List<Staff>) em.createQuery(qry).getResultList();
            
        } catch (Exception e) {
            logg.log(Level.SEVERE, e.toString());
            return null;
        }
    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="User Account Operations">
    
    /**
     * Method checks if the parameter object (UserAcount) is already existing pertaining to a specific staff
     * @param account object
     * @return true if exist<br />
     *         false if not
     */
    public boolean doesStaffHaveAccountWithSameAccessRight(UserAccount account) {
        String qry = "SELECT ua FROM UserAccount ua WHERE ua.staff.staffId = '" + account.getStaff().getStaffId() + "' "
                + "AND ua.userCategory.id = " + account.getUserCategory().getId()+ " ";
        try {
            synchPCnDB();
            List<UserAccount> uas = em.createQuery(qry).getResultList();
            return !uas.isEmpty();
            
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
//</editor-fold>

}