package com.studio8to4.smb.sprite.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.audio.AudioManager;
import com.studio8to4.smb.screen.PlayScreen;

import javax.inject.Inject;

public class Goomba extends Enemy{

    @Inject
    private AudioManager audioManager;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y){
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/SMBGame.PPM, 16/SMBGame.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float delta){
        stateTime += delta;
        if(setToDestroy && !destroyed){
            b2dworld.destroyBody(b2Body);
            destroyed = true;
            setRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16);
            stateTime = 0;
        }else if (!destroyed){
            b2Body.setLinearVelocity(velocity);
            setPosition(b2Body.getPosition().x - getWidth() /2, b2Body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2Body = b2dworld.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / SMBGame.PPM);
        fdef.filter.categoryBits = SMBGame.ENEMY_BIT;
        fdef.filter.maskBits = SMBGame.GROUND_BIT |
                SMBGame.COIN_BIT |
                SMBGame.BRICK_BIT |
                SMBGame.ENEMY_BIT |
                SMBGame.OBJECT_BIT |
                SMBGame.MARIO_BIT;

        fdef.shape = shape;

        b2Body.createFixture(fdef).setUserData(this);

        // Head

        PolygonShape head = new PolygonShape();
        Vector2[] vertex = new Vector2[4];
        vertex[0] = new Vector2(-5, 8).scl(1/SMBGame.PPM);
        vertex[1] = new Vector2(5, 8).scl(1/SMBGame.PPM);
        vertex[2] = new Vector2(-3, 3).scl(1/SMBGame.PPM);
        vertex[3] = new Vector2(3, 3).scl(1/SMBGame.PPM);
        head.set(vertex);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = SMBGame.ENEMY_HEAD_BIT;
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        audioManager.playSound("stomp");
    }
}
