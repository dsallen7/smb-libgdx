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
import com.studio8to4.smb.sprite.item.Item;
import com.studio8to4.smb.sprite.other.FireBall;
import com.studio8to4.smb.sprite.tileobject.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef){
			case SMBGame.MARIO_HEAD_BIT | SMBGame.BRICK_BIT:
			case SMBGame.MARIO_HEAD_BIT | SMBGame.COIN_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.MARIO_HEAD_BIT)
					((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
				else
					((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
				break;
			case SMBGame.ENEMY_HEAD_BIT | SMBGame.MARIO_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ENEMY_HEAD_BIT)
					((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
				else
					((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
				break;
			case SMBGame.ENEMY_BIT | SMBGame.OBJECT_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ENEMY_BIT)
					((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				else
					((Enemy)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case SMBGame.MARIO_BIT | SMBGame.ENEMY_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.MARIO_BIT)
					((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());
				else
					((Mario) fixB.getUserData()).hit((Enemy)fixA.getUserData());
				break;
			case SMBGame.ENEMY_BIT | SMBGame.ENEMY_BIT:
				((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
				((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
				break;
			case SMBGame.ITEM_BIT | SMBGame.OBJECT_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ITEM_BIT)
					((Item)fixA.getUserData()).reverseVelocity(true, false);
				else
					((Item)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case SMBGame.ITEM_BIT | SMBGame.MARIO_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.ITEM_BIT)
					((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
				else
					((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
				break;
			case SMBGame.FIREBALL_BIT | SMBGame.OBJECT_BIT:
				if(fixA.getFilterData().categoryBits == SMBGame.FIREBALL_BIT)
					((FireBall)fixA.getUserData()).setToDestroy();
				else
					((FireBall)fixB.getUserData()).setToDestroy();
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
