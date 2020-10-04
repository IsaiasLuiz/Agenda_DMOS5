package br.edu.dmos5.agenda_dmos5.model;

import java.io.Serializable;

public enum ContactItemType implements Serializable {

    EMAIL("EMAIL"), CELLPHONE("CELLPHONE"), LANDLINEPHONE("LANDLINEPHONE");

    private String type;

    ContactItemType(String type) {
        this.type = type;
    }

}
