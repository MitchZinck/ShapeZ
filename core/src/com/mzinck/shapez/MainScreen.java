package com.mzinck.shapez;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mzinck.shapez.shapes.Shape;
import com.mzinck.shapez.shapes.ShapeDefs;

/**
 * Currently the starting point of the game.
 * 
 * @author Mitchell Zinck <mitchellzinck@yahoo.com>
 *
 */
public class MainScreen extends ApplicationAdapter {

	private float rotationSpeed;

	private ShapeRenderer		shapeRend;
	private SpriteBatch			batch;
	private OrthographicCamera	cam;

	private Player	player;
	private ArrayList<Shape> shapes;

	private Texture			swordSpriteSheet;
	private TextureRegion[]	weapons	= new TextureRegion[4];
	private TextureRegion	sword;

	@Override
	public void create() {
		swordSpriteSheet = new Texture(Gdx.files.internal("Weapons.png"));
		sword = new TextureRegion(swordSpriteSheet, 0F, 0F, 0.24F, 1F);

		player = new Player(Constants.PLAYER_START_SPEED,
				Constants.PLAYER_START_SIZE);
		
		shapes = new ArrayList<Shape>();
		
		for(int i = 0; i < 10; i++) {
			shapes.add(new Shape(ShapeDefs.ZOMBIE, player, MathUtils.random(500), MathUtils.random(500), MathUtils.random(0.3F, 0.7F)));
		}

		rotationSpeed = 0.5f;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(player.getCameraSize(),
				player.getCameraSize() * (h / w));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		batch = new SpriteBatch();

		shapeRend = new ShapeRenderer();
	}

	@Override
	public void render() {
		handleInput();
		for(Shape shape : shapes) {
			shape.update();
		}
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRend.setProjectionMatrix(cam.combined);
		shapeRend.begin(ShapeType.Line);
		shapeRend.setAutoShapeType(true);
		renderBackground();
		renderPlayer();
		shapeRend.end();

		batch.begin();
		batch.draw(sword, cam.position.x, cam.position.y, player.getSize(), 15);
		batch.end();
	}

	public void renderPlayer() {
		shapeRend.set(ShapeType.Filled);
		shapeRend.setColor(0, 1, 0, 1);
		shapeRend.rect(cam.position.x, cam.position.y, player.getSize(),
				player.getSize());
		
		for(Shape shape : shapes) {
		shapeRend.rect(shape.getxPos(), shape.getyPos(), player.getSize(),
				player.getSize());
		}
	}

	public void renderBackground() {
		Gdx.gl.glLineWidth(1F);
		shapeRend.setColor(0, 0, 0, 0);
		for (int i = 0; i < Constants.WORLD_SIZE; i += 10) {
			shapeRend.line(0, i, Constants.WORLD_SIZE, i);
			shapeRend.line(i, 0, i, Constants.WORLD_SIZE);
		}
	}

	/**
	 * 
	 * Handles the input.
	 * 
	 * Gets the hypotenuse of the mouse direction compared to where the player
	 * is located on the screen (the middle) and then scales it down to a
	 * triangle with a max hypotenuse of 1 (speed) which also gives the x and y
	 * values that the player can move.
	 * 
	 */
	private void handleInput() {
		double x = (Gdx.input.getX() - 826);
		double y = (Gdx.input.getY() - 420);
		double hypotenuse = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);

		float scaleValue = (float) (player.getSpeed() / hypotenuse);

		if ((x < 100 && x > -100) && (y < 100 && y > -100)) {
			x = x / 100;
			y = y / 100 * -1;
		} else {
			x = x * scaleValue;
			y = y * scaleValue * -1;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player.setSize(player.getSize() + 0.1F);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			player.setSize(player.getSize() - 0.1F);
		}

		// if(y < 100 && y > -100) {
		//
		// } else {
		//
		// }

		cam.translate((float) x, (float) y);

		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f,
				Constants.WORLD_SIZE / cam.viewportWidth);

		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

		cam.position.x = MathUtils.clamp(cam.position.x,
				effectiveViewportWidth / 2f,
				Constants.WORLD_SIZE - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y,
				effectiveViewportHeight / 2f,
				Constants.WORLD_SIZE - effectiveViewportHeight / 2f);

		player.setX(cam.position.x);
		player.setY(cam.position.y);
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = player.getCameraSize() * (player.getSize() / 10);
		cam.viewportHeight = player.getCameraSize() * height / width
				* (player.getSize() / 10);
		cam.update();
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void pause() {

	}

}
