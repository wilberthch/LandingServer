/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scouteam.landingserver.backend;

/**
 *
 * @author will
 */
import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DatabaseSave {

    private static final String DATABASE_ID = "LandingData";
    private static final String COLLECTION_ID = "LandingDoc";
    private static DocumentClient documentClient = DocumentClientFactory.getDocumentClient();
    private static Database databaseCache;
    private static DocumentCollection collectionCache;
    private static Gson gson = new Gson();
    
    private static final DatabaseSave _databaseSaveInstance = new DatabaseSave();
    
    private DatabaseSave() {}
    
    public static DatabaseSave getInstance()
    {
        return _databaseSaveInstance;
    }

    private Database getDatabase() {
        if (databaseCache == null) {
            // Get the database if it exists
            List<Database> databaseList = documentClient
                    .queryDatabases(
                            "SELECT * FROM root r WHERE r.id='" + DATABASE_ID
                            + "'", null).getQueryIterable().toList();
            if (databaseList.size() > 0) {
                // Cache the database object so we won't have to query for it
                // later to retrieve the selfLink.
                databaseCache = databaseList.get(0);
            } else {
                // Create the database if it doesn't exist.
                try {
                    Database databaseDefinition = new Database();
                    databaseDefinition.setId(DATABASE_ID);
                    databaseCache = documentClient.createDatabase(
                            databaseDefinition, null).getResource();
                } catch (DocumentClientException e) {
                    // TODO: Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }
        return databaseCache;
    }

    private DocumentCollection getTodoCollection() {
        if (collectionCache == null) {
            // Get the collection if it exists.
            List<DocumentCollection> collectionList = documentClient
                    .queryCollections(
                            getDatabase().getSelfLink(),
                            "SELECT * FROM root r WHERE r.id='" + COLLECTION_ID
                            + "'", null).getQueryIterable().toList();
            if (collectionList.size() > 0) {
                // Cache the collection object so we won't have to query for it
                // later to retrieve the selfLink.
                collectionCache = collectionList.get(0);
            } else {
                // Create the collection if it doesn't exist.
                try {
                    DocumentCollection collectionDefinition = new DocumentCollection();
                    collectionDefinition.setId(COLLECTION_ID);
                    // Configure the new collection performance tier to S1.
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.setOfferType("S1");
                    collectionCache = documentClient.createCollection(
                            getDatabase().getSelfLink(),
                            collectionDefinition, requestOptions).getResource();
                } catch (DocumentClientException e) {
                    // TODO: Something has gone terribly wrong - the app wasn't 
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }
        return collectionCache;
    }

    private Document getDocumentById(String id) {
        // Retrieve the document using the DocumentClient.
        List<Document> documentList = documentClient
                .queryDocuments(getTodoCollection().getSelfLink(),
                        "SELECT * FROM root r WHERE r.id='" + id + "'", null)
                .getQueryIterable().toList();
        if (documentList.size() > 0) {
            return documentList.get(0);
        } else {
            return null;
        }
    }

    public LandingDoc createLandingDoc(LandingDoc landingDoc) {
        // Serialize the TodoItem as a JSON Document.
        Document todoItemDocument = new Document(gson.toJson(landingDoc));
        // Annotate the document as a TodoItem for retrieval (so that we can
        // store multiple entity types in the collection).
        todoItemDocument.set("entityType", "landingDoc");
        try {
            // Persist the document using the DocumentClient.
            todoItemDocument = documentClient.createDocument(
                    getTodoCollection().getSelfLink(), todoItemDocument, null,
                    false).getResource();
        } catch (DocumentClientException e) {
            e.printStackTrace();
            return null;
        }
        return gson.fromJson(todoItemDocument.toString(), LandingDoc.class);
    }

    public LandingDoc readLandingDoc(String id) {
        // Retrieve the document by id using our helper method.
        Document todoItemDocument = getDocumentById(id);
        if (todoItemDocument != null) {
            // De-serialize the document in to a TodoItem.
            return gson.fromJson(todoItemDocument.toString(), LandingDoc.class);
        } else {
            return null;
        }
    }
    
    
    public List<LandingDoc> readLandingDocs(int pMinutes) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        Timestamp timeCapture = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.SECOND, -pMinutes);
        Timestamp newTimeCapture = new Timestamp(calendar.getTimeInMillis());
        
        String query = String.format("SELECT * FROM root r WHERE r.entityType = 'landingDoc' AND (sendingTimeStamp BETWEEN '%s' AND '%s')", 
                timeCapture.toString(), 
                newTimeCapture.toString());
        
        List<LandingDoc> landingDocs = new ArrayList<LandingDoc>();
        // Retrieve the TodoItem documents
        List<Document> documentList = documentClient
                .queryDocuments(getTodoCollection().getSelfLink(),
                        query,
                        null).getQueryIterable().toList();
        // De-serialize the documents in to TodoItems.
        for (Document todoItemDocument : documentList) {
            landingDocs.add(gson.fromJson(todoItemDocument.toString(),
                    LandingDoc.class));
        }
        return landingDocs;
    }
}
