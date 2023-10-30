package com.studio8to4.smb.sprite.tileobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.Mario;
import com.studio8to4.smb.sprite.item.ItemDef;
import com.studio8to4.smb.sprite.item.Mushroom;

public class CoinBlock extends InteractiveTileObject{

	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;
	private boolean isHit;
	
	public CoinBlock(PlayScreen screen, MapObject object) {
		super(screen, object);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(SMBGame.COIN_BIT);
		isHit = false;
	}
	
	@Override
	public void onHeadHit(Mario mario) {
		if(getCell().getTile().getId() == BLANK_COIN)
			audioManager.playSound("bump");
		else {
			if(object.getProperties().containsKey("mushroom")) {
				screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / SMBGame.PPM),
						Mushroom.class));
				audioManager.playSound("powerup_spawn");
			}
			else
				audioManager.playSound("coin");
			getCell().setTile(tileSet.getTile(BLANK_COIN));
			hud.addScore(100);
		}
	}

}
