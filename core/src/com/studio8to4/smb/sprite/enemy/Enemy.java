package com.studio8to4.smb.sprite.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.studio8to4.smb.audio.AudioManager;
import com.studio8to4.smb.di.DIContainer;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;

import javax.inject.Inject;

public abstract class Enemy extends Sprite {
    @Inject
    protected AssetManager assetManager;
    @Inject
    protected AudioManager audioManager;
    protected World b2dworld;
    protected PlayScreen screen;
    public Body b2Body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        DIContainer.getFeather().injectFields(this);
        this.b2dworld = screen.getB2dworld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(1, -2);
        b2Body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead(Mario mario);
    public abstract void hitByEnemy(Enemy enemy);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    public abstract void update(float delta);
}
