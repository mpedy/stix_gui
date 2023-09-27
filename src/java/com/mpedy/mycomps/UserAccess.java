/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.mycomps;

/**
 *
 * @author Matteo
 *
 * https://stackoverflow.com/questions/6453842/jsf-2-how-can-i-add-an-ajax-listener-method-to-composite-component-interface
 */
import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.event.TransferEvent;

@FacesComponent(value = "userAccess")
public class UserAccess extends UINamingContainer {

    public void transferListener(TransferEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression transferListener = (MethodExpression) getAttributes().get("transferListener");
        transferListener.invoke(context.getELContext(), new Object[]{event});
    }
    public void transferListenerRole(TransferEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression transferListener = (MethodExpression) getAttributes().get("transferListenerRole");
        transferListener.invoke(context.getELContext(), new Object[]{event});
    }
}
