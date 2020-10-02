package br.edu.dmos5.agenda_dmos5.model;

import java.io.Serializable;

public class Contact implements Serializable {

    public static final String CONTACT_KEY = "CONTACT_KEY";

    private String fullName;

    private String landlinePhone;

    private String cellPhone;

    private String userId;

    public Contact() {
    }

    public Contact(String fullName, String landlinePhone, String cellPhone) {
        this.fullName = fullName;
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

    @Override
    public String toString() {
        return getFullName();
    }

}
