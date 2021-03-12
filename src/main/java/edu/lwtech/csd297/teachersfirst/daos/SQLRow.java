package edu.lwtech.csd297.teachersfirst.daos;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;

class SQLRow {
    private static final Logger logger = LogManager.getLogger(SQLRow.class.getName());

    private Map<String,String> items;

    public SQLRow() {
        items = new HashMap<>();
    }

    public SQLRow(String name, String value) {
        items = new HashMap<>();
        items.put(name, value);
    }

    public SQLRow(String name, int value) {
        this(name, ""+value);
    }

    public SQLRow(String value) {
        this("Value", value);
    }

    public SQLRow(int value) {
        this("Value", ""+value);
    }

    public SQLRow(List<String> columns, ResultSet rs) {
        items = new HashMap<>();

        try {
            for (int i=0; i < columns.size(); i++) {
                items.put(columns.get(i), rs.getString(i+1));
            }
        } catch (SQLException e) {
            logger.error("Unable to construct SQLRow object from ResultSet.", e);
        }
    }

    public String getItem(String name) {
        if (name == null) throw new IllegalArgumentException("Null parameter passed to getItem()");
        return items.get(name);
    }

    public String getItem() {
        return getItem("Value");
    }

    public void addItem(String name, String value) {
        if (name == null || value == null) throw new IllegalArgumentException("Null parameter passed to getItem()");
        items.put(name, value);
    }

    public void removeItem(String name) {
        if (name == null) throw new IllegalArgumentException("Null parameter passed to getItem()");
        items.remove(name);
    }

    @Override
    public String toString() {
        String s = "{";
        String comma = "";
        for (String name : items.keySet()) {
            s += comma + "{" + name + "," + items.get(name) + "}";
            comma = ",";
        }
        s += "}";
        return s;
    }
    
}