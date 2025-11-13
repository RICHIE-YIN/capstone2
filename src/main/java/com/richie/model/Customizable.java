package com.richie.model;

import java.util.ArrayList;

public interface Customizable {
    void addExtra(String extra);
    ArrayList getExtras();
    boolean hasExtras();

}