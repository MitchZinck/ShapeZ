package com.mzinck.shapez;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

    private ShapeRenderer      shapeRend;
    private BitmapFont         font;
    private SpriteBatch        batch;
    private OrthographicCamera cam;
    private Player             player; //The player
    private ArrayList<Shape>   shapes; //List of Enemies in the game
    private Texture            swordSpriteSheet;
    private TextureRegion      sword;
    private TextureRegion[]    weapons             = new TextureRegion[4];
    private boolean            animateSword        = false; //whether the sword is swinging or not
    private int                swordAnimationSteps = 0, 
                               leftOrRight = 0, 
                               level = 1, 
                               totalShapes = 0, 
                               deadShapes = 0; //How long the animation has gone on for, whether the sword is swinging to the right or to the left
    private float              swordX, swordY;

    @Override
    public void create() {
        swordSpriteSheet = new Texture(Gdx.files.internal("Weapons.png"));
        sword = new TextureRegion(swordSpriteSheet, 0F, 0F, 0.24F, 1F);

        player = new Player(Constants.PLAYER_START_SPEED,
                Constants.PLAYER_START_SIZE);

        shapes = new ArrayList<Shape>();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(player.getCameraSize(),
                player.getCameraSize() * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        batch = new SpriteBatch();

        shapeRend = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(0.3F);
        font.setColor(Color.RED);
    }

    @Override
    public void render() {        
        spawnShapes();
        handleInput();
        player.update();
        if(player.getHP() < 1) {
           newLevel();
           level = 1;
           player = new Player(Constants.PLAYER_START_SPEED,
                   Constants.PLAYER_START_SIZE);
        }
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
        drawSword();
        font.draw(batch, "HP: " + player.getHP(), cam.position.x - 140, cam.position.y + 80);
        font.draw(batch, "Level: " + level, cam.position.x - 140, cam.position.y + 70);
        font.draw(batch, "Zombies Killed: " + deadShapes, cam.position.x - 140, cam.position.y + 60);
        font.draw(batch, "Zombies Alive: " + (totalShapes - deadShapes), cam.position.x - 140, cam.position.y + 50);
        batch.end();
    }
    
    public void drawSword() {
        float array[] = getHypotenuse(false);
        swordX = array[0] + cam.position.x;
        swordY = array[1] + cam.position.y;        

        batch.draw(sword.getTexture(), swordX, swordY, player.getSize() / 2, 0,
                player.getSize(), 15F, 1F, 1F, swordAnimationSteps * leftOrRight * 15,
                sword.getRegionX(), sword.getRegionY(), sword.getRegionWidth(),
                sword.getRegionHeight(), false, false);
    }

    public float[] getHypotenuse(boolean playerHyp) {
        float array[] = { 0, 0 };
        float x = (Gdx.input.getX() - 826);
        float y = (Gdx.input.getY() - 420);
        float hypotenuse = (float) Math.pow(Math.pow(x, 2) + Math.pow(y, 2),
                0.5);

        float scaleValue = (playerHyp == true)
                ? (float) (player.getSpeed() / hypotenuse)
                : (float) (10 / hypotenuse);

        if ((x < 100 && x > -100) && (y < 100 && y > -100) && playerHyp == true) {
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
    
    
    public void spawnShapes() {
        if(deadShapes >= (level * 10)) {
            if(deadShapes == totalShapes) {
                newLevel();
            }
        } else if(totalShapes < level * 10){
            if(MathUtils.random(10000) < level * Constants.ZOMBIES_PER_LEVEL_MODIFIER) {
                shapes.add(new Shape(ShapeDefs.ZOMBIE, player,
                        MathUtils.random(500), MathUtils.random(500),
                        MathUtils.random(0.3F, 0.7F), Constants.SHAPE_START_SIZE));
                totalShapes++;
            }
        }
    }
    
    public void newLevel() {
        level++;
        totalShapes = 0;
        deadShapes = 0;
        shapes.clear();
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
                leftOrRight = ((Gdx.input.getX() - 826) > 0) ? -1 : 1;
                animateSword = true;
                swordAnimationSteps = 0;
            }
        }

        // if(y < 100 && y > -100) {
        //
        // } else {
        //
        // }

        cam.translate(x, y);

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
        for (Shape shape : shapes) {
            if(!shape.isDead()) {
                rect.x = shape.getxPos();
                rect.y = shape.getyPos();
                rect.width = shape.getSize();
                rect.height = shape.getSize();
    
                if (swordRect.overlaps(rect)) {
                    shape.setDead();
                    deadShapes++;
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = player.getCameraSize() * (player.getSize() / 10);
        cam.viewportHeight = player.getCameraSize() * height / width * (player.getSize() / 10);
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
