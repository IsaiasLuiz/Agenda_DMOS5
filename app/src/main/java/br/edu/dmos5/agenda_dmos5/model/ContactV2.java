package br.edu.dmos5.agenda_dmos5.model;

import java.util.List;

public class ContactV2 {

    public static final String CONTACT_NAME_KEY = "CONTACT_NAME_KEY";

    private String fullName;

    private List<ContactItem> contactItems;

    public ContactV2(String fullName, List<ContactItem> contactItems) {
        this.fullName = fullName;
        this.contactItems = contactItems;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<ContactItem> getContactItems() {
        return contactItems;
    }

    public void setContactItems(List<ContactItem> contactItems) {
        this.contactItems = contactItems;
    }
}
