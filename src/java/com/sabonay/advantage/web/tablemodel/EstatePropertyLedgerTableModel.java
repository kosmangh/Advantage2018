/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.tablemodel;

import com.sabonay.advantage.ejb.entities.EstatePropertyLedger;
import com.sabonay.modules.web.jsf.abstractactionhandlers.AbstractWebTableModel;

/**
 *
 * @author Edwin
 */
public class EstatePropertyLedgerTableModel extends AbstractWebTableModel<EstatePropertyLedger>
{
    private String[] columnHeader = { "Estate Property", "Debit/ Credit", "Amount ", "Entry For", "Payee Name", "Medium Of Payment", "Ledger Year", "Payment Type", "Payment Date", "Last Modified By" };

  private String[] columnCodes = { "estateProperty", "debitCreditEntry", "amountInvolved", "paymentFor", "payeeName", "mediumOfPayment", "ledgerYear", "paymentType", "#{commonUtils.formatDate(ledger.dateOfRecordTransaction)}", "lastModifiedBy" };


   

    public EstatePropertyLedgerTableModel()
    {
        setColumnCodes(columnCodes);
        setColumnHeaders(columnHeader);

        setVarName("ledger");
    }



}
