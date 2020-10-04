package br.edu.dmos5.agenda_dmos5.model;

public class ContactItem {

    protected String value;

    protected ContactItemType type;

    public ContactItem(String value, ContactItemType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactItemType getType() {
        return type;
    }

}
