package br.edu.dmos5.agenda_dmos5.model;

import java.io.Serializable;

@Deprecated
public class Contact implements Serializable {

    public static final String CONTACT_KEY = "CONTACT_KEY";

    private Integer contactId;

    private String fullName;

    private String landlinePhone;

    private String cellPhone;

    private String userId;

    public Contact(Integer contactId, String landlinePhone, String cellPhone) {
        this.contactId = contactId;
        this.landlinePhone = landlinePhone;
        this.cellPhone = cellPhone;
    }

    public Contact(String fullName, String landlinePhone, String cellPhone, String userId) {
        this.fullName = fullName;
        this.landlinePhone = landlinePhone;
        this.cellPhone = cellPhone;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLandlinePhone() {
        return landlinePhone;
    }

    public void setLandlinePhone(String landlinePhone) {
        this.landlinePhone = landlinePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return getFullName();
    }

}
