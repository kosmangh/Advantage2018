/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.common.utils.AdConstants;
import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.RentSpread;
import com.sabonay.advantage.web.tablemodel.RentSpreadTableModel;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.common.constants.DebitCredit;
import com.sabonay.modules.web.jsf.api.annotations.DataPanel;
import com.sabonay.modules.web.jsf.api.annotations.DataTableModelList;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Edwin
 */
@ManagedBean(name="rentBalance")
@SessionScoped
public class RentBalance implements Serializable{


    @DataTableModelList(group="rs")
    private List<RentSpread> rentSpreadsList = new LinkedList<RentSpread>();

    private List<EstateProperty> estatePropertyList = ds.getCustomQry().currentRentedEstateProperties("");

    private RentSpreadTableModel spreadTableModel = new  RentSpreadTableModel();

    private PaymentType houseRentPaymentType = PaymentType.HOUSE_RENT;

    @DataPanel(group="rs")
    private HtmlDataPanel<RentSpread> rentSpreadDataPanel;

    public RentBalance()
    {


        rentSpreadDataPanel = spreadTableModel.getDataPanel();
        rentSpreadDataPanel.setInitType(HtmlDataPanel.Init.NO_SEARCH_SELECTION);
        rentSpreadDataPanel.autoBindAndBuild(RentBalance.class, "rs");

        
        for (EstateProperty estateProperty : estatePropertyList)
        {
            RentSpread rentSpread = new RentSpread();

            rentSpread.setEstateProperty(estateProperty);

            PropertyOccupant occupant = ds.getCustomQry().estatePropertyCurrentOccupant(estateProperty);

            rentSpread.setPropertyOccupant(occupant);


            rentSpreadsList.add(rentSpread);
        }
    }

    public void save()
    {

        for (RentSpread rentSpread : rentSpreadsList)
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.OCTOBER);
            cal.set(Calendar.YEAR, 2010);
            cal.set(Calendar.DAY_OF_MONTH, 2);

            double total = rentSpread.getCurrentRentBalance();
            while (total > 0)
            {

                String billingMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                EstatePropertyLedger estatePropertyLedger = new EstatePropertyLedger();

                System.out.println("billing month : " + billingMonth);

                estatePropertyLedger.setAmountInvolved(rentSpread.getCurrentRent());
                estatePropertyLedger.setDebitCreditEntry(DebitCredit.DEBIT);
               estatePropertyLedger.setDateOfRecordEntry(new Date());
               estatePropertyLedger.setDateOfRecordTransaction(cal.getTime());
               estatePropertyLedger.setEstateProperty(rentSpread.getEstateProperty());
               estatePropertyLedger.setLedgerYear(cal.get(Calendar.YEAR));
               estatePropertyLedger.setPaymentType(houseRentPaymentType);
               estatePropertyLedger.setPropertyOccupant(rentSpread.getPropertyOccupant());
               estatePropertyLedger.setRecordedBy(AdConstants.SHC);
               estatePropertyLedger.setMediumOfPayment(AdConstants.NONE);
               estatePropertyLedger.setReceiptNumberIssued(AdConstants.NONE);
               estatePropertyLedger.setPayeeName(AdConstants.SHC);
               estatePropertyLedger.setPaymentFor("Property Rent ,"+ billingMonth + " " + cal.get(Calendar.YEAR));


               IDCreator.setEstateLedgerId(estatePropertyLedger, billingMonth);
               ds.getCommonQry().estatePropertyLedgerUpdate(estatePropertyLedger);

               System.out.println(estatePropertyLedger);

               cal.add(Calendar.MONTH, -1);
//               cal.a
               total = total - rentSpread.getCurrentRent();

//                System.out.println(cal.getTime());

            }
        }
    }

    public HtmlDataPanel<RentSpread> getRentSpreadDataPanel()
    {
        return rentSpreadDataPanel;
    }

    public void setRentSpreadDataPanel(HtmlDataPanel<RentSpread> rentSpreadDataPanel)
    {
        this.rentSpreadDataPanel = rentSpreadDataPanel;
    }

    public List<RentSpread> getRentSpreadsList()
    {
        return rentSpreadsList;
    }

    public void setRentSpreadsList(List<RentSpread> rentSpreadsList)
    {
        this.rentSpreadsList = rentSpreadsList;
    }



    
}
