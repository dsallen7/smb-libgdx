package com.studio8to4.smb.sprite.item;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.di.DIContainer;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;

import javax.inject.Inject;

public abstract class Item extends Sprite {
    @Inject
    protected AssetManager assetManager;
    protected PlayScreen screen;
    protected World b2dworld;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body b2Body;

    public Item(PlayScreen screen, float x, float y){
        DIContainer.getFeather().injectFields(this);
        this.screen = screen;
        this.b2dworld = screen.getB2dworld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16/ SMBGame.PPM,16/ SMBGame.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use(Mario mario);

    public void update(float delta){
        if(toDestroy && !destroyed){
            b2dworld.destroyBody(b2Body);
            destroyed = true;
        }
    }

    public void destroy(){
        toDestroy = true;
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed)
            super.draw(batch);
    }
    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
