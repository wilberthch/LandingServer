/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scouteam.landingserver.frontend;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import com.scouteam.landingserver.backend.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
                                                                                                                    

/**
 *
 * @author will
 */
@WebService(serviceName = "LandingWebServer")
public class LandingWebServer {

    /**
     * This is a sample web service operation
     * @param imageDump
     * @param dataMatrix
     * @return 
     */
    @WebMethod(operationName = "addLandingDoc")
    public String addLandingDoc(@WebParam(name = "imageDump") String imageDump, @WebParam(name = "dataMatrix") String dataMatrix) {
        LandingDoc doc = new LandingDoc(imageDump, dataMatrix);
        DatabaseSave saver = DatabaseSave.getInstance();
        doc = saver.createLandingDoc(doc);
        return doc.getId() + " --- " + doc.getSendingTimeStamp().toString();
    }
    
    @WebMethod(operationName = "getLandingDocs")
    public LandingDoc[] getLandingDocs(@WebParam(name = "minutes") int minutes)
    {
        DatabaseSave saver = DatabaseSave.getInstance();
        List<LandingDoc> landingDocs = saver.readLandingDocs(minutes);
        
        LandingDoc[] landingDocsArray = new LandingDoc[landingDocs.size()];
        
        System.out.println(landingDocsArray.length + "<------------------------------------------------------------------");
        for(LandingDoc lan : landingDocs)
        {
            System.out.println(lan.toString());
        }
        
        return landingDocs.toArray(landingDocsArray);
    }
}
