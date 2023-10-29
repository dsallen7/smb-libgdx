package com.studio8to4.smb.di;

import org.codejargon.feather.Feather;

public class DIContainer {

    private static Feather feather;

    public static void initialize(){
        feather = Feather.with(new AssetModule(), new UiModule());
    }

    public static Feather getFeather(){
        return feather;
    }
}
