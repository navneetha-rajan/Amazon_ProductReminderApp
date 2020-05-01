package com.e.reminder;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsTableDatabaseAccess {
    private static final String COGNITO_POOL_ID = "us-east-1:f25770b9-bed5-472a-b377-29635e14af4e";
    private static final Regions MY_REGION = Regions.US_EAST_1;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;
    private Context context;
    private final String DYNAMODB_TABLE = "products";
    CognitoCachingCredentialsProvider credentialsProvider;

    private static volatile ProductsTableDatabaseAccess instance;

    private ProductsTableDatabaseAccess(Context context) {
        this.context = context;
        credentialsProvider = new CognitoCachingCredentialsProvider(context, COGNITO_POOL_ID, MY_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }

    public static synchronized ProductsTableDatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new ProductsTableDatabaseAccess(context);
        }
        return instance;
    }

    // sample db APIs. Write necessary APIs here.
    public Product getItem(String user_id) {
        Document result = dbTable.getItem(new Primitive(credentialsProvider.getCachedIdentityId()), new Primitive(user_id));
        return new Product(result);
    }

    public List<Product> getAllItems() {
        List<Product> result = new ArrayList<>();
        for (Document doc : dbTable.query(new Primitive("9988")).getAllResults()) {
            result.add(new Product(doc));
        }
        return result;
    }

    public ArrayList<Product> getAllContents() {
        ScanOperationConfig scanConfig = new ScanOperationConfig();
        List<String> attributeList = new ArrayList<>();
        attributeList.add("item_name");
        attributeList.add("item_id");
        attributeList.add("category");
        attributeList.add("description");
        attributeList.add("cost");
        attributeList.add("expiry_date");
        attributeList.add("seller");
        scanConfig.withAttributesToGet(attributeList);
        Search searchResult = dbTable.scan(scanConfig);
        ArrayList<Product> result = new ArrayList<>();
        for (Document doc : searchResult.getAllResults()) {
            result.add(new Product(doc));
        }
        return result;
    }

    public void create(Product product) {
        Map<String, AttributeValue> hashMap = new HashMap<>();

        hashMap.put("item_name", new AttributeValue().withS(product.getName()));
        hashMap.put("item_id", new AttributeValue().withN(product.getItemid()));
        hashMap.put("category", new AttributeValue().withS(product.getCategory()));
        hashMap.put("description", new AttributeValue().withS(product.getDescription()));
        hashMap.put("cost", new AttributeValue().withS(product.getCost()));
        hashMap.put("MFD", new AttributeValue().withS(product.getMfd()));
        hashMap.put("expiry_date", new AttributeValue().withS(product.getExpiryDate()));
        hashMap.put("seller", new AttributeValue().withS(product.getSeller()));

        Document document = Document.fromAttributeMap(hashMap);
        dbTable.putItem(document);
    }

    public void update(Product product) {
        Map<String, AttributeValue> hashMap = new HashMap<>();

        hashMap.put("item_name", new AttributeValue().withS(product.getName()));
        hashMap.put("item_id", new AttributeValue().withN(product.getItemid()));
        hashMap.put("category", new AttributeValue().withS(product.getCategory()));
        hashMap.put("description", new AttributeValue().withS(product.getDescription()));
        hashMap.put("cost", new AttributeValue().withS(product.getCost()));
        hashMap.put("MFD", new AttributeValue().withS(product.getMfd()));
        hashMap.put("expiry_date", new AttributeValue().withS(product.getExpiryDate()));
        hashMap.put("seller", new AttributeValue().withS(product.getSeller()));

        Document document = Document.fromAttributeMap(hashMap);
        dbTable.deleteItem(
                document.get("item_id").asPrimitive());   // The Partition Key
        dbTable.putItem(document);
    }

    public void delete(Product product) {
        Map<String, AttributeValue> hashMap = new HashMap<>();
        hashMap.put("item_id", new AttributeValue().withN(product.getItemid()));

        Document document = Document.fromAttributeMap(hashMap);
        dbTable.deleteItem(
                document.get("item_id").asPrimitive());   // The Partition Key
    }
}
