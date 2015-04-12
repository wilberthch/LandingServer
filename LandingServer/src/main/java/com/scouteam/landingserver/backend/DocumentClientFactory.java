/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scouteam.landingserver.backend;

import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.ConnectionPolicy;

/**
 *
 * @author will
 */
public class DocumentClientFactory {

    private static final String HOST = "https://landing-storage.documents.azure.com:443/";
    private static final String MASTER_KEY = "pXtmQCIaDPnTV0CzNML3+gBykBX7ZYzDE19U1gOhVNpTeCo+qPyRYFS5GJbmJQ5SLeC8dvdFWzn0RZR/OGLbZQ==";
    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            documentClient = new DocumentClient(HOST, MASTER_KEY, ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }
        return documentClient;
    }
}
