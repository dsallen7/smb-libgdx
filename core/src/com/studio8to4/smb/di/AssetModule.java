package com.studio8to4.smb.di;

import com.badlogic.gdx.assets.AssetManager;
import com.studio8to4.smb.audio.AudioManager;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;

public class AssetModule {
    @Provides
    @Singleton
    AssetManager assetManager(){
        return new AssetManager();
    }

    @Provides
    @Singleton
    AudioManager audioManager(){
        return new AudioManager();
    }
}
