package com.e.afinal;


import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.UpdateItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAccess {


    // I have table created in dynamodb with name "user" and key is "user_id" which takes string and other attributes.
    //put your own cognito credentials here and write policies.
    // link: https://medium.com/@shukla.iitm/integrating-dynamodb-document-api-in-your-android-application-c9120c1592d7
    //link : https://www.youtube.com/watch?v=RXs-KoUaFFY&list=LL6ryGCDp3Q82vFDBxYcfyxg&index=8&t=0s

    private static final String COGNITO_POOL_ID = "us-east-1:f25770b9-bed5-472a-b377-29635e14af4e";
    private static final Regions MY_REGION = Regions.US_EAST_1;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;
    private Context context;
    private final String DYNAMODB_TABLE = "products";
    CognitoCachingCredentialsProvider credentialsProvider;

    private static volatile DatabaseAccess instance;
    private DatabaseAccess (Context context) {
        this.context =context;
        credentialsProvider = new CognitoCachingCredentialsProvider (context, COGNITO_POOL_ID, MY_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }
    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    // sample db APIs. Write necessary APIs here.
    public Document getItem (String user_id){
        Document result = dbTable.getItem(new Primitive(credentialsProvider.getCachedIdentityId()), new Primitive(user_id));
        return result;
    }
    public List<Document> getAllItems() {
        return dbTable.query(new Primitive("9988")).getAllResults();
    }

    public List<Document> getAllContents() {
        ScanOperationConfig scanConfig = new ScanOperationConfig();
        List<String> attributeList = new ArrayList<>();
        attributeList.add("name");
        attributeList.add("category");
        attributeList.add("expiry_date");
        scanConfig.withAttributesToGet(attributeList);
        Search searchResult = dbTable.scan(scanConfig);
        return searchResult.getAllResults();

    }
    public void create(Document product) {

            product.put("name", product.get("name"));
            product.put("category", product.get("category"));
            product.put("expiry_date", product.get("expiry_date"));
        dbTable.putItem(product);
    }

    public void update(Document product) {
        product.put("name", product.get("name"));
        product.put("category", product.get("category"));
        product.put("expiry_date", product.get("expiry_date"));
        dbTable.deleteItem(
                product.get("name").asPrimitive());   // The Partition Key
        dbTable.putItem(product);

    }

    public void delete(Document product) {
        dbTable.deleteItem(
                product.get("name").asPrimitive());   // The Partition Key
    }


}
