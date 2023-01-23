/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.validations;

import com.sabonay.advantage.common.constants.PropOccupationType;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.web.pagecontrollers.PropertySelector;
import com.sabonay.advantage.web.pagecontrollers.RentalBillingController;

/**
 *
 * @author Edwin /Ritchid
 */
public class RentalBillingValidator {

    public static String msg = "";

    public static boolean validateSaving(RentalBillingController controller) {
        if (controller.getEstatePropertyLedger() == null) {
            msg = "Error Occured ....";
            return false;
        }

        if (controller.getSelectedEstateProperty() == null) {
            msg = "Cannot do bill payment with no estate property selected";
            return false;
        }

        Occupancy op = PropertySelector.getMgedInstance().getPropertyOccupationDetails();
        if (op == null) {
            msg = "There is no propert selected or property is not occupied *";
            return false;
        }

        if (op.getOccupationType() != PropOccupationType.Rental) {
            msg = "Selected property is not occupied as Rental";
            return false;
        }

        if (controller.getEstatePropertyLedger().getDateOfRecordTransaction() == null) {
            msg = "Date of payment is empty";
            return false;
        }

        return true;
    }
}
