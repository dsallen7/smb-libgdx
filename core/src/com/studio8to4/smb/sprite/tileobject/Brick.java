package com.studio8to4.smb.sprite.tileobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;

public class Brick extends InteractiveTileObject {

	
	public Brick(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(SMBGame.BRICK_BIT);
	}
	
	@Override
	public void onHeadHit(Mario mario) {
		Gdx.app.log("Brick", "Hit");
		if(mario.isBig()) {
			setCategoryFilter(SMBGame.DESTROYED_BIT);
			getCell().setTile(null);
			hud.addScore(50);
			audioManager.playSound("breakblock");
		}else{
			audioManager.playSound("bump");
		}
	}
}
