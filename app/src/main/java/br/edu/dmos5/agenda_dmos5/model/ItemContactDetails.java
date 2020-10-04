package br.edu.dmos5.agenda_dmos5.model;

import java.io.Serializable;

public class ItemContactDetails implements Serializable {

    public static final String ITEM_CONTACT_DETAILS_KEY = "ITEM_CONTACT_DETAILS_KEY";

    private String fullName;

    private ContactItemType type;

    private String userId;

    public ItemContactDetails(String fullName, ContactItemType type, String userId) {
        this.fullName = fullName;
        this.type = type;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ContactItemType getType() {
        return type;
    }

    public void setType(ContactItemType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
