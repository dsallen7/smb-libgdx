package com.studio8to4.smb.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.enemy.Enemy;
import com.studio8to4.smb.sprite.enemy.Turtle;
import com.studio8to4.smb.sprite.tileobject.Brick;
import com.studio8to4.smb.sprite.tileobject.CoinBlock;
import com.studio8to4.smb.sprite.enemy.Goomba;

public class B2WorldCreator {

	private Array<Goomba> goombas;
	private Array<Turtle> turtles;
	private PlayScreen screen;
	private SMBGame game;
	private World b2dworld;

	public B2WorldCreator(PlayScreen screen, SMBGame game) {
		this.screen = screen;
		this.game = game;
		this.b2dworld = screen.getB2dworld();
	}

	public void loadMap(TiledMap map){
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;


		//create ground bodies/fixtures
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / SMBGame.PPM, (rect.getY() + rect.getHeight() / 2) / SMBGame.PPM);

			body = b2dworld.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / SMBGame.PPM, rect.getHeight() / 2 / SMBGame.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		//create pipe bodies/fixtures
		for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / SMBGame.PPM, (rect.getY() + rect.getHeight() / 2) / SMBGame.PPM);

			body = b2dworld.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / SMBGame.PPM, rect.getHeight() / 2 / SMBGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = SMBGame.OBJECT_BIT;
			body.createFixture(fdef);
		}

		//create brick bodies/fixtures
		for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
			new Brick(screen, object);
		}

		//create coin bodies/fixtures
		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

			new CoinBlock(screen, object);
		}

		//create all goombas
		goombas = new Array<Goomba>();
		for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			goombas.add(new Goomba(screen, rect.getX() / SMBGame.PPM, rect.getY() / SMBGame.PPM));
		}
		turtles = new Array<Turtle>();
		for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			turtles.add(new Turtle(screen, rect.getX() / SMBGame.PPM, rect.getY() / SMBGame.PPM));
		}
	}

	public Array<Goomba> getGoombas() {
		return goombas;
	}
	public Array<Enemy> getEnemies(){
		Array<Enemy> enemies = new Array<Enemy>();
		enemies.addAll(goombas);
		enemies.addAll(turtles);
		return enemies;
	}
}
