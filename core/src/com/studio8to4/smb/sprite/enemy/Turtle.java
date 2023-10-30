package com.studio8to4.smb.sprite.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;

public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;
    public enum State {WALKING, MOVING_SHELL, STANDING_SHELL}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private boolean setToDestroy;
    private boolean destroyed;


    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        walkAnimation = new Animation<TextureRegion>(0.2f, frames);
        currentState = previousState = State.WALKING;

        setBounds(getX(), getY(), 16 / SMBGame.PPM, 24 / SMBGame.PPM);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = b2dworld.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SMBGame.PPM);
        fdef.filter.categoryBits = SMBGame.ENEMY_BIT;
        fdef.filter.maskBits = SMBGame.GROUND_BIT |
                SMBGame.COIN_BIT |
                SMBGame.BRICK_BIT |
                SMBGame.ENEMY_BIT |
                SMBGame.OBJECT_BIT |
                SMBGame.MARIO_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / SMBGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / SMBGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / SMBGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / SMBGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1.8f;
        fdef.filter.categoryBits = SMBGame.ENEMY_HEAD_BIT;
        b2Body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;

        switch (currentState){
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && region.isFlipX() == false){
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX() == true){
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if(currentState == State.STANDING_SHELL && stateTime > 5){
            currentState = State.WALKING;
            velocity.x = 1;
            System.out.println("WAKE UP SHELL");
        }

        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - 8 /SMBGame.PPM);
        b2Body.setLinearVelocity(velocity);
    }

    @Override
    public void hitOnHead(Mario mario) {
        if(currentState == State.STANDING_SHELL) {
            if(mario.b2Body.getPosition().x > b2Body.getPosition().x)
                velocity.x = -2;
            else
                velocity.x = 2;
            currentState = State.MOVING_SHELL;
            System.out.println("Set to moving shell");
        }
        else {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }

    public void kick(int direction){
        velocity.x = direction;
        currentState = State.MOVING_SHELL;
    }
}