package com.richie.model;

import java.util.ArrayList;

public interface Customizable {
    void addExtra(Extra extra);
    ArrayList<Extra> getExtras();
    boolean hasExtras();
}