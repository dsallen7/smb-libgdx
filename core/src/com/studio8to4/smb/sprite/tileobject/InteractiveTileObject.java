package com.studio8to4.smb.sprite.tileobject;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.audio.AudioManager;
import com.studio8to4.smb.di.DIContainer;
import com.studio8to4.smb.scene.HUD;

import javax.inject.Inject;

public abstract class InteractiveTileObject {

	@Inject
	protected AudioManager audioManager;
	@Inject
	protected HUD hud;
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Fixture fixture;
	
	public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
		this.world = world;
		this.map = map;
		this.bounds = bounds;

		DIContainer.getFeather().injectFields(this);
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth()/2) / SMBGame.PPM, (bounds.getY() + bounds.getHeight()/2) / SMBGame.PPM);
		body = world.createBody(bdef);
		
		shape.setAsBox(bounds.getWidth()/2 / SMBGame.PPM, bounds.getHeight()/2 / SMBGame.PPM);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
	}
	
	public abstract void onHeadHit();
	
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}
	
	public Cell getCell() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int)(body.getPosition().x*SMBGame.PPM / 16), (int)(body.getPosition().y*SMBGame.PPM / 16));
	}
}
