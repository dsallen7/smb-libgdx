package com.studio8to4.smb.sprite.other;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;

public class FireBall extends Sprite {

    PlayScreen screen;
    World b2dWorld;
    Array<TextureRegion> frames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.b2dWorld = screen.getB2dworld();
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);
        setRegion((Texture) fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / SMBGame.PPM, 6 / SMBGame.PPM);
        defineFireBall();
    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /SMBGame.PPM : getX() - 12 /SMBGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!b2dWorld.isLocked())
            b2body = b2dWorld.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / SMBGame.PPM);
        fdef.filter.categoryBits = SMBGame.FIREBALL_BIT;
        fdef.filter.maskBits = SMBGame.GROUND_BIT |
                SMBGame.COIN_BIT |
                SMBGame.BRICK_BIT |
                SMBGame.ENEMY_BIT |
                SMBGame.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion((Texture) fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            b2dWorld.destroyBody(b2body);
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


}