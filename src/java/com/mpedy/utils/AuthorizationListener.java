package com.mpedy.utils;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class AuthorizationListener implements PhaseListener {

    final static Logger LOGGER = LogManager.getLogger(AuthorizationListener.class);
    private static final String ROOTPATH = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/";

    /**
     *
     * @param event
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        try {
            if (!userExists(context)) {
                if (requestingSecureView(context)) {
                    context.responseComplete();
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
                }
            }
        } catch (Exception e) {
            LOGGER.error("AuthorizationListenerioe.afterPhase(): " + e);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();//remove("user");  
//            try {  
//                FacesContext.getCurrentInstance().getExternalContext().redirect(ROOTPATH + "login.xhtml");  
//            } catch (IOException ioe) {  
//                LOGGER.error("AuthorizationListenerioe.afterPhase() - redirect " + ioe);  
//            }  
        }
    }

    public void beforePhase(PhaseEvent event) {
//Do nothing
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private boolean userExists(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        return (extContext.getSessionMap().containsKey("user"));
    }

    private boolean requestingSecureView(FacesContext context) throws IOException {
        return false;
        /*ExternalContext extContext = context.getExternalContext();
        String path = extContext.getRequestServletPath(); // parte finale dell'url esempio "/login.xhtml" 
        
        if (path.contains("WallDisplay")) {
            return false;
        }
        return (!"/login.xhtml".equals(path)
                && !"/resetPassword.xhtml".equals(path)
                && !"/createNewPassword.xhtml".equals(path)
                && !"/registration.xhtml".equals(path)
                && !"/confirmRegistration.xhtml".equals(path)
                && !"/WEB-INF/notfound.xhtml".equals(path)
                && !"/Survey/campaign.xhtml".equals(path)
                && !"/Survey/replyConfirmed.xhtml".equals(path)
                && !path.contains("WallDisplay"));*/
    }
}
