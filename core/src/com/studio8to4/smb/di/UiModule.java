package com.studio8to4.smb.di;

import com.studio8to4.smb.scene.HUD;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;

public class UiModule {

    @Provides
    @Singleton
    HUD hud(){
        HUD hud = new HUD();
        return hud;
    }
}
