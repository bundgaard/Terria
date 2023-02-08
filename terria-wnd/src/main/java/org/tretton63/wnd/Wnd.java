package org.tretton63.wnd;

import javax.swing.*;
import javafx.application.Application
public class Wnd extends Application{

    private final String[] items = new String[]{"a","b","c","d","abcdef"};
    public Wnd() {
        super("Wnd");
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var list = new JList<>();


        list.setListData(items);
        getContentPane().add(list);
    }



}
