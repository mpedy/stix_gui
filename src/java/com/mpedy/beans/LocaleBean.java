/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.beans;

//import com.mpedy.persistence.UserAutenticateJPA;
//import com.mpedy.persistence.dbentities.UserAutenticate;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "localeBean")
@SessionScoped
public class LocaleBean implements Serializable {

//    @ManagedProperty("#{sessionManager}")
//    private SessionManager sessionManager = null;
    private Locale locale = null;
    private String lan;
    private List<String> languages;

    public LocaleBean() {
//        languages = Arrays.asList("it", "en", "de");
        Iterator<Locale> il = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        while(il.hasNext()){
            if(il.next().getLanguage().equals(locale.getLanguage())){
                return;
            }
        }
        locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
//        lan = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
//        locale = new Locale(lan);
    }

    public Locale getLocale() {
//        if (sessionManager != null) {
//            try {
//                UserAutenticate ua = UserAutenticateJPA.getByUserId(sessionManager.getUserId());
//                if (ua.getLanguage() != null && languages.contains(ua.getLanguage())) {
//                    return new Locale(ua.getLanguage());
//                }
//            } catch (Exception e) {
//            }
//        }
        return locale;
//        return new Locale("it");
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void saveLanguage(String language, String userid) {
        locale = new Locale(language);
        try {
//            UserAutenticate ua = UserAutenticateJPA.getByUserId(userid);
//            ua.setLanguage(locale.getLanguage());
//            UserAutenticateJPA.update(ua);
        } catch (Exception e) {
            System.out.println("Errore in LocalBean.saveLanguage(): " + e.getMessage());
        }
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLocale(String lang) {
        try {
            if (lang == null) {
                this.locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
            } else {
                this.locale = new Locale(lang);
            }
        } catch (Exception e) {
            this.locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
        }finally{
            FacesContext.getCurrentInstance().getViewRoot().setLocale(this.locale);
        }
    }

}
