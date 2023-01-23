/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.utils;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Edwin
 */

public class AdvantageListener implements PhaseListener{

    @Override
    public void afterPhase(PhaseEvent event)
    {
        FacesContext context = event.getFacesContext();

        System.out.println(context.getExternalContext().getRequestPathInfo());
    }

    @Override
    public void beforePhase(PhaseEvent event)
    {
        System.out.println("face chainging .... ");
    }

    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }

}
