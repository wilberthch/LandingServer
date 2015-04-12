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
                                                                                                                    

/**
 *
 * @author will
 */
@WebService(serviceName = "LandingWebServer")
public class LandingWebServer {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        LandingDoc doc = new LandingDoc(txt, txt, Timestamp.valueOf("2015-04-12 01:02:02"));
        DatabaseSave saver = new DatabaseSave();
        saver.createTodoItem(doc);
        return "Hello " + txt + " !";
    }
}
