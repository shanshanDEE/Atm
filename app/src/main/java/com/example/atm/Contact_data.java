package com.example.atm;

import java.util.ArrayList;
import java.util.List;

public class Contact_data {
    int id;
    String name;
    //String phone;
    List<String> phone = new ArrayList<>();

    public Contact_data(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }


}
