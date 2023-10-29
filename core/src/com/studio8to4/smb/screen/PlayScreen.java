package com.studio8to4.smb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.studio8to4.smb.SMBGame;
import com.studio8to4.smb.di.DIContainer;
import com.studio8to4.smb.scene.HUD;
import com.studio8to4.smb.sprite.enemy.Enemy;
import com.studio8to4.smb.sprite.Mario;
import com.studio8to4.smb.tools.B2WorldCreator;
import com.studio8to4.smb.tools.WorldContactListener;

import javax.inject.Inject;

public class PlayScreen implements Screen {

	@Inject
	private AssetManager assetManager;
	private SMBGame game;
	private TextureAtlas atlas;
	private OrthographicCamera camera;
	private Viewport viewport;
	@Inject
	private HUD hud;
	
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	//Box2d variables
	private final World b2dworld;
	private Box2DDebugRenderer b2dr;
	private B2WorldCreator creator;
	
	private Mario mario;

	private Music mainTheme;
	
	public PlayScreen(SMBGame game) {
		this.game = game;
		DIContainer.getFeather().injectFields(this);
		camera = new OrthographicCamera();
		viewport = new FitViewport(SMBGame.V_WIDTH / SMBGame.PPM, SMBGame.V_HEIGHT / SMBGame.PPM, camera);
		hud.initialise(game.batch);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("1-1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / SMBGame.PPM);
		camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
		b2dworld = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();
		atlas = new TextureAtlas("MarioAndEnemies.atlas");
		creator = new B2WorldCreator(this, game);
		creator.loadMap(this.getMap());
		mario = new Mario(b2dworld, this);
		
		b2dworld.setContactListener(new WorldContactListener());

		mainTheme = assetManager.get("audio/music/mario_music.ogg", Music.class);
		mainTheme.setVolume(0.25f);
		mainTheme.setLooping(true);
		mainTheme.play();
	}
	
	public void update(float delta) {
		handleInput(delta);
		
		b2dworld.step(1/60f, 6, 2);
		mario.update(delta);
		for (Enemy enemy : creator.getGoombas()){
			enemy.update(delta);
			if(enemy.getX() < mario.getX() + 224 / SMBGame.PPM){
				enemy.b2Body.setActive(true);
			}
		}
		hud.update(delta);
		camera.position.x = mario.b2Body.getPosition().x;
		camera.update();
		mapRenderer.setView(camera);
	}
	
	public void handleInput(float delta) {
		mario.handleInput(delta);
	}

	public TiledMap getMap(){
		return map;
	}

	public World getB2dworld() {
		return b2dworld;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.render();
		
		b2dr.render(b2dworld, camera.combined);
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		mario.draw(game.batch);
		for (Enemy enemy : creator.getGoombas()){
			enemy.draw(game.batch);
		}
		game.batch.end();
		
		//Set batch to draw what Hud camera sees
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		mapRenderer.dispose();
		b2dworld.dispose();
		b2dr.dispose();
		hud.dispose();
	}

}
