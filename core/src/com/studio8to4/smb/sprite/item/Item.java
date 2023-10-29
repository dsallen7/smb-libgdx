package com.studio8to4.smb.sprite.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World b2dworld;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.b2dworld = screen.getB2dworld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16/ SMBGame.PPM,16/ SMBGame.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void useItem();

    public void update(float delta){
        if(toDestroy && !destroyed){
            b2dworld.destroyBody(body);
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
}
