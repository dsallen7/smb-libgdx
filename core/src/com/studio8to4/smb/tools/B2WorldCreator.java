package com.studio8to4.smb.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.screen.PlayScreen;
import com.studio8to4.smb.sprite.tileobject.Brick;
import com.studio8to4.smb.sprite.tileobject.CoinBlock;
import com.studio8to4.smb.sprite.enemy.Goomba;

public class B2WorldCreator {

	private Array<Goomba> goombas;
	private PlayScreen screen;
	private SMBGame game;

	public B2WorldCreator(PlayScreen screen, SMBGame game) {
		this.screen = screen;
		this.game = game;
	}

	public void loadMap(TiledMap map){
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		goombas = new Array<Goomba>();
		for (int i = 2; i <= 6; i++) {
			for(MapObject object : map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject)object).getRectangle();
				switch(i) {
					case 2:
						// ground
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2) / SMBGame.PPM, (rect.getY() + rect.getHeight()/2) / SMBGame.PPM);
						body = screen.getB2dworld().createBody(bdef);

						shape.setAsBox(rect.getWidth()/2 / SMBGame.PPM, rect.getHeight()/2 / SMBGame.PPM);
						fdef.shape = shape;
						fdef.filter.categoryBits = SMBGame.GROUND_BIT;
						body.createFixture(fdef);
						break;
					case 3:
						// pipe
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2) / SMBGame.PPM, (rect.getY() + rect.getHeight()/2) / SMBGame.PPM);
						body = screen.getB2dworld().createBody(bdef);

						shape.setAsBox(rect.getWidth()/2 / SMBGame.PPM, rect.getHeight()/2 / SMBGame.PPM);
						fdef.shape = shape;
						fdef.filter.categoryBits = SMBGame.OBJECT_BIT;
						body.createFixture(fdef);
						break;
					case 4:
						// coin
						new CoinBlock(screen, rect);
						break;
					case 5:
						// brick
						new Brick(screen, rect);
						break;
					case 6:
						goombas.add(new Goomba(screen, rect.getX() / SMBGame.PPM,rect.getY() / SMBGame.PPM));
						break;
				}
			}
		}
	}

	public Array<Goomba> getGoombas() {
		return goombas;
	}
}
