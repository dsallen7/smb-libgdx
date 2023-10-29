package com.studio8to4.smb.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.audio.AudioManager;
import com.studio8to4.smb.di.DIContainer;
import com.studio8to4.smb.screen.PlayScreen;

import javax.inject.Inject;

public class Mario extends Sprite {

	public enum MarioState{
		FALLING, JUMPING, STANDING, RUNNING;
	}
	@Inject
	private AudioManager audioManager;
	public MarioState currentState, previousState;
	public World world;
	public Body b2Body;
	private TextureRegion marioStand;
	private Animation<TextureRegion> marioRun, marioJump;
	private float stateTimer;
	private boolean runningRight;
	
	public Mario(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("little_mario"));
		DIContainer.getFeather().injectFields(this);
		this.world = world;
		defineMario();
		marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
		setBounds(0, 0, 16/SMBGame.PPM, 16/SMBGame.PPM);
		setRegion(marioStand);
		currentState = MarioState.STANDING;
		previousState = MarioState.STANDING;
		stateTimer = 0;
		runningRight = true;

		b2Body.setUserData(this);
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
		}
		marioRun = new Animation<>(0.1f, frames);
		frames.clear();
		
		for(int i = 4; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), i* 16, 0, 16, 16));
		}
		marioJump = new Animation<>(0.1f, frames);
	}
	
	public void update(float delta) {
		setPosition(b2Body.getPosition().x - getWidth()/2, b2Body.getPosition().y - getHeight()/2);
		setRegion(getFrame(delta));
	}
	
	public TextureRegion getFrame(float delta) {
		currentState = getState();
		TextureRegion region;
		switch(currentState) {
		case JUMPING:
			region = marioJump.getKeyFrame(stateTimer);
			break;
		case RUNNING:
			region = marioRun.getKeyFrame(stateTimer, true);
			break;
		case STANDING:
		case FALLING:
		default:
			region = marioStand;
			break;
		}
		if((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX() ) {
			region.flip(true, false);
			runningRight = false;
		}else if((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		stateTimer = currentState == previousState ? stateTimer + delta : 0;
		previousState = currentState;
		return region;
	}
	
	public MarioState getState() {
		if(b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == MarioState.JUMPING))
			return MarioState.JUMPING;
		else if(b2Body.getLinearVelocity().y < 0)
			return MarioState.FALLING;
		else if(b2Body.getLinearVelocity().x != 0)
			return MarioState.RUNNING;
		else
			return MarioState.STANDING;
	}
	
	public void defineMario() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(32 / SMBGame.PPM, 32 / SMBGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		b2Body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(7 / SMBGame.PPM);
		fdef.filter.categoryBits = SMBGame.MARIO_BIT;
		fdef.filter.maskBits = SMBGame.GROUND_BIT |
				SMBGame.COIN_BIT |
				SMBGame.BRICK_BIT |
				SMBGame.ENEMY_BIT |
				SMBGame.OBJECT_BIT |
				SMBGame.ENEMY_HEAD_BIT;
		
		fdef.shape = shape;
		
		b2Body.createFixture(fdef);
		
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / SMBGame.PPM, 7 / SMBGame.PPM), new Vector2(2 / SMBGame.PPM, 7 / SMBGame.PPM));
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2Body.createFixture(fdef).setUserData(this);
	}

	public void handleInput(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			switch(currentState){

				case FALLING:
				case JUMPING:
					break;
				case STANDING:
				case RUNNING:
					b2Body.applyLinearImpulse(new Vector2(0, 4f), b2Body.getWorldCenter(), true);
					audioManager.playSound("jump");
					break;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b2Body.getLinearVelocity().x <= 2) {
			b2Body.applyLinearImpulse(new Vector2(0.1f, 0), b2Body.getWorldCenter(), true);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && b2Body.getLinearVelocity().x >= -2) {
			b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
		}
	}

	public void onHeadHit() {
		b2Body.setLinearVelocity(b2Body.getLinearVelocity().x, -b2Body.getLinearVelocity().y);
		this.currentState = MarioState.FALLING;
	}
}
