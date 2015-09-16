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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

	private Player				player;
	private ArrayList<Shape>	shapes;

	private Texture			swordSpriteSheet;
	private TextureRegion[]	weapons	= new TextureRegion[4];
	private TextureRegion	sword;

	private boolean	animateSword		= false;
	private int		swordAnimationSteps	= 0;
	private float	swordX;
	private float	swordY;

	@Override
	public void create() {
		swordSpriteSheet = new Texture(Gdx.files.internal("Weapons.png"));
		sword = new TextureRegion(swordSpriteSheet, 0F, 0F, 0.24F, 1F);

		player = new Player(Constants.PLAYER_START_SPEED,
				Constants.PLAYER_START_SIZE);

		shapes = new ArrayList<Shape>();

		for (int i = 0; i < 10; i++) {
			shapes.add(new Shape(ShapeDefs.ZOMBIE, player,
					MathUtils.random(500), MathUtils.random(500),
					MathUtils.random(0.3F, 0.7F), Constants.SHAPE_START_SIZE));
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
		if (animateSword == true) {
			swordAnimation();
		}

		for (Shape shape : shapes) {
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
		renderShapes();
		shapeRend.end();

		batch.begin();
		float array[] = getHypotenuse(false);
		swordX = array[0] + cam.position.x;
		swordY = array[1] + cam.position.y;

		batch.draw(sword.getTexture(), swordX, swordY,
				player.getSize() / 2, 0, player.getSize(), 15F, 1F, 1F,
				-swordAnimationSteps * 15, sword.getRegionX(),
				sword.getRegionY(), sword.getRegionWidth(),
				sword.getRegionHeight(), false, false);
		batch.end();
	}

	public float[] getHypotenuse(boolean p) {
		float array[] = { 0, 0 };
		float x = (Gdx.input.getX() - 826);
		float y = (Gdx.input.getY() - 420);
		float hypotenuse = (float) Math.pow(Math.pow(x, 2) + Math.pow(y, 2),
				0.5);

		float scaleValue = (p == true)
				? (float) (player.getSpeed() / hypotenuse)
				: (float) (10 / hypotenuse);

		if ((x < 100 && x > -100) && (y < 100 && y > -100) && p == true) {
			x = x / 100;
			y = y / 100 * -1;
		} else {
			x = x * scaleValue;
			y = y * scaleValue * -1;
		}

		array[0] = x;
		array[1] = y;
		return array;
	}

	public void renderPlayer() {
		shapeRend.set(ShapeType.Filled);
		shapeRend.setColor(0, 1, 0, 1);
		shapeRend.rect(cam.position.x, cam.position.y, player.getSize(),
				player.getSize());
	}
	
	public void renderShapes() {
		for (Shape shape : shapes) {
			shapeRend.rect(shape.getxPos(), shape.getyPos(), shape.getSize(),
					shape.getSize());
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
	public void handleInput() {
		float xy[] = getHypotenuse(true);

		float x = xy[0];
		float y = xy[1];

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player.setSize(player.getSize() + 0.1F);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			player.setSize(player.getSize() - 0.1F);
		}
		if (Gdx.input.isTouched()) {
			if (animateSword == false) {
				animateSword = true;
				swordAnimationSteps = 0;
			}
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

	public void swordAnimation() {
		if (swordAnimationSteps > Constants.SWORD_STEPS_MAX) {
			swordAnimationSteps = 0;
			animateSword = false;
		} else {
			swordAnimationSteps++;
			swordCollision();
		}
	}

	public void swordCollision() {
		Rectangle swordRect = new Rectangle(swordX, swordY, 15F, 15F);
		Rectangle rect = new Rectangle();
		for(Shape shape : shapes) {
			rect.x = shape.getxPos();
			rect.y = shape.getyPos();
			rect.width = shape.getSize();
			rect.height = shape.getSize();
			
			if(swordRect.overlaps(rect)) {
				shape.setDead();
			}
		}
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
