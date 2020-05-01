package com.e.reminder;

import androidx.annotation.NonNull;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.StringUtils;

import java.util.Map;

public class Product {
    private String itemid;
    private String name;
    private String category;
    private String description;
    private String cost;
    private String mfd;
    private String expiryDate;
    private String seller;

    public String getItemid() {
        return itemid;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }

    public String getMfd() {
        return mfd;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getSeller() {
        return seller;
    }

    public String getName() {
        return name;
    }

    public Product(String itemid, String name, String category, String expiryDate) {
        this.itemid = itemid;
        this.name = name;
        this.category = category;
        this.description = "description";
        this.cost = "000";
        this.mfd = "mfd";
        this.expiryDate = expiryDate;
        this.seller = "seller";
    }

    public Product(Document document) {
        if (document != null) {
            Map<String, AttributeValue> attributes = document.toAttributeMap();
            if (attributes.containsKey("item_id")) {
                this.itemid = attributes.get("item_id").getN();
            }

            if (attributes.containsKey("item_name")) {
                this.name = attributes.get("item_name").getS();
            }

            if (attributes.containsKey("category")) {
                this.category = attributes.get("category").getS();
            }

            if (attributes.containsKey("description")) {
                this.description = attributes.get("description").getS();
            }

            if (attributes.containsKey("cost")) {
                this.cost = attributes.get("cost").getS();
            }

            if (attributes.containsKey("MFD")) {

                this.mfd = attributes.get("MFD").getS();
            }

            if (attributes.containsKey("expiry_date")) {
                this.expiryDate = attributes.get("expiry_date").getS();
            }

            if (attributes.containsKey("seller")) {
                this.seller = attributes.get("seller").getS();
            }
        }
    }

    public Boolean filterBasedOnString(CharSequence sequence) {
        CharSequence lowerCaseSequence = StringUtils.lowerCase(sequence.toString());

        if (this.itemid != null && StringUtils.lowerCase(this.itemid).contains(lowerCaseSequence)) {
            return true;
        }

        if (this.name != null && StringUtils.lowerCase(this.name).contains(lowerCaseSequence)) {
            return true;
        }

        if (this.description != null && StringUtils.lowerCase(this.description).contains(lowerCaseSequence)) {
            return true;
        }

        if (this.seller != null && StringUtils.lowerCase(this.seller).contains(lowerCaseSequence)) {
            return true;
        }

        if (this.category != null && StringUtils.lowerCase(this.category).contains(lowerCaseSequence)) {
            return true;
        }

        return false;
    }


    @Override
    @NonNull
    public String toString() {
        return "item_id='" + itemid + "\'\n" +
                ", item_name='" + name + "\'\n" +
                ", category='" + category + "\'\n" +
                ", description='" + description + "\'\n" +
                ", cost='" + cost + "\'\n" +
                ", mfd='" + mfd + "\'\n" +
                ", expiryDate='" + expiryDate + "\'\n" +
                ", seller='" + seller + "\'\n";
    }
}
