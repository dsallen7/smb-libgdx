package com.studio8to4.smb.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.sprite.Mario;
import com.studio8to4.smb.sprite.enemy.Enemy;
import com.studio8to4.smb.sprite.tileobject.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		if(fixA.getUserData() instanceof  Mario || fixB.getUserData() instanceof Mario) {
			Fixture head = fixA.getUserData() instanceof Mario ? fixA : fixB;
			Fixture object = head == fixA ? fixB : fixA;
			
			if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
				((InteractiveTileObject)object.getUserData()).onHeadHit();
				((Mario)head.getUserData()).onHeadHit();
			}
		}

		switch(cDef){
			case SMBGame.ENEMY_HEAD_BIT | SMBGame.MARIO_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ENEMY_HEAD_BIT)
					((Enemy)fixA.getUserData()).hitOnHead();
				else if(fixB.getFilterData().categoryBits == SMBGame.ENEMY_HEAD_BIT)
					((Enemy)fixB.getUserData()).hitOnHead();
				break;
			case SMBGame.ENEMY_BIT | SMBGame.OBJECT_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ENEMY_BIT)
					((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				else
					((Enemy)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case SMBGame.ENEMY_BIT | SMBGame.ENEMY_BIT:
				((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				((Enemy)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case SMBGame.MARIO_BIT | SMBGame.ENEMY_BIT:
				Gdx.app.log("MARIO", "DIED");
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
}
