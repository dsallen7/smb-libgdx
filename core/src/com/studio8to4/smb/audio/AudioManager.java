package com.studio8to4.smb.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.studio8to4.smb.di.DIContainer;

import javax.inject.Inject;

public class AudioManager {
    @Inject
    private AssetManager assetManager;

    public AudioManager(){
        DIContainer.getFeather().injectFields(this);
    }

    public void playSound(String id){
        assetManager.get("audio/sounds/"+id+".wav", Sound.class).play();
    }
}
