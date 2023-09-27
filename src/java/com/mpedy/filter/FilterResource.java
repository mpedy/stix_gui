/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.filter;

//import com.mpedy.beans.SessionManager;
//import com.mpedy.beans.SessionPrivileges;
//import com.mpedy.persistence.dbentities.ResourceRenderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Matteo
 */
@WebServlet("/FilterResource")
public class FilterResource implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        this.context.log("FilterResource initialized");
    }
//    TODO

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        HttpSession session = req.getSession(false);
//        SessionManager sessionM = null;
//        try {
//            sessionM = (SessionManager) session.getAttribute("sessionManager");
//        } catch (Exception e) {
//            context.log("Warning: SessionManager is null");
//        }
//        sesssionM.getCurrentRoleCompany().getRolecompanyid();
//        sesssionM.getRc();  
        String risorsa_puntata = FilenameUtils.getBaseName(FilenameUtils.getName(uri));
//        System.out.println("URI: " + uri);
        if (uri.equals("/performance/") || uri.equals("/performance/login.xhtml") || uri.equals("/performance/index.xhtml")) {
            chain.doFilter(request, response);
            return;
        }

        List<String> resources = new ArrayList<>(Arrays.asList("composer", "wizardchart", "wizarddashboard", "wizardsequence", "channelmanager"));
//        if (session != null && sessionM != null) {
//            SessionPrivileges privileges = sessionM.getSessionPrivileges();
//            for (String s : resources) {
//                if (uri.toLowerCase().contains(s + ".xhtml")) {
//                    if (!privileges.contains(s) || !privileges.getRendered(s)) {
//                        this.context.log("Unauthorized access request");
////                        System.out.println("Unauthorized access");
//                        res.sendRedirect("/index.xhtml");
//                        return;
//                    }
//                    for (ResourceRenderer resource : sessionM.getResourceRenderer()) {
//                        if (resource.getUR().getResourceid().equals(s)) {
//                            if (resource.isToberendered()) {
//                                chain.doFilter(request, response);
//                                return;
//                            }
//                        }
//                    }
//                    this.context.log("Unauthorized access request");
////                    System.out.println("Unauthorized access");
//                    res.sendRedirect("/index.xhtml");
//                    return;
//                }
//            }
//        }
        if (session == null && !(uri.endsWith("html") || uri.endsWith("LoginServlet"))) {
            this.context.log("Unauthorized access request");
//            System.out.println("Unauthorized access");
            res.sendRedirect("/login.xhtml");
        } else {
            // pass the request along the filter chain
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
