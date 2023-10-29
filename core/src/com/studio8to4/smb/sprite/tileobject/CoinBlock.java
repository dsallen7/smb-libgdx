package com.studio8to4.smb.sprite.tileobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;

public class CoinBlock extends InteractiveTileObject{

	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;
	private boolean isHit;
	
	public CoinBlock(PlayScreen screen, Rectangle bounds) {
		super(screen.getB2dworld(), screen.getMap(), bounds);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(SMBGame.COIN_BIT);
		isHit = false;
	}
	
	@Override
	public void onHeadHit() {
		Gdx.app.log("Coin", "Hit");
		if(isHit)
			audioManager.playSound("bump");
		else
			audioManager.playSound("coin");
		getCell().setTile(tileSet.getTile(BLANK_COIN));
		hud.addScore(50);
		isHit = true;
	}

}
