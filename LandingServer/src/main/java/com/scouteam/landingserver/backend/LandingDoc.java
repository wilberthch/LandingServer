/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scouteam.landingserver.backend;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author will
 */
public class LandingDoc {

    private String id;
    private String imageDump;
    private String DataMatrix;
    private Timestamp sendingTimeStamp;

    public LandingDoc(String imageDump, String DataMatrix) {
        Date timeStamp = new Date();
        this.imageDump = imageDump;
        this.DataMatrix = DataMatrix;
        this.sendingTimeStamp = new Timestamp(timeStamp.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageDump() {
        return imageDump;
    }

    public void setImageDump(String imageDump) {
        this.imageDump = imageDump;
    }

    public String getDataMatrix() {
        return DataMatrix;
    }

    public void setDataMatrix(String DataMatrix) {
        this.DataMatrix = DataMatrix;
    }

    public Timestamp getSendingTimeStamp() {
        return sendingTimeStamp;
    }

    public void setSendingTimeStamp(Timestamp sendingTimeStamp) {
        this.sendingTimeStamp = sendingTimeStamp;
    }
}
