package com.studio8to4.smb.sprite.item;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;

public class Mushroom extends Item{

    public Mushroom(PlayScreen screen, float x, float y){
        super(screen, x, y);
        setRegion(((TextureAtlas)assetManager.get("MarioAndEnemies.atlas")).findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0, 0);
    }
    @Override
    public void update(float delta){
        super.update(delta);
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        b2Body.setLinearVelocity(velocity);
    }

    @Override
    public void defineItem() {
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

    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
    }

}
