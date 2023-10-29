package com.studio8to4.smb.sprite.tileobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;

public class Brick extends InteractiveTileObject {

	
	public Brick(PlayScreen screen, Rectangle bounds) {
		super(screen.getB2dworld(), screen.getMap(), bounds);
		fixture.setUserData(this);
		setCategoryFilter(SMBGame.BRICK_BIT);
	}
	
	@Override
	public void onHeadHit() {
		Gdx.app.log("Brick", "Hit");
		setCategoryFilter(SMBGame.DESTROYED_BIT);
		getCell().setTile(null);
		hud.addScore(50);
		audioManager.playSound("breakblock");
	}
}
