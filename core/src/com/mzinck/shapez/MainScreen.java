package com.mzinck.shapez;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mzinck.shapez.shapes.Shape;
import com.mzinck.shapez.shapes.ShapeDefs;
import com.mzinck.shapez.shapes.ShapeFactory;

/**
 * Currently the starting point of the game.
 * 
 * @author Mitchell Zinck <mitchellzinck@yahoo.com>
 *
 */
public class MainScreen extends ApplicationAdapter {

    private ShapeRenderer       shapeRend;
    private BitmapFont          font;
    private SpriteBatch         batch;
    private Sprite              map, box;
    private Rectangle           boxRect;
    private OrthographicCamera  cam;
    private Player              player;
    private ArrayList<Shape>    shapes;
    private Sprite[]            swords;
    private TextureAtlas        atlas;
    private Vector3             coords;
    private ShapeFactory<Shape> shapeFactory;
    private boolean             animateSword        = false;
    private int                 swordAnimationSteps = 0,
                                leftOrRight         = 0, 
                                level               = 1, 
                                totalShapes         = 0, 
                                deadShapes          = 0, 
                                boxCounter          = 0, 
                                swordCount          = 0;
    private float               swordX, swordY;
    
    public static float SCALE_RATIO;

    @Override
    public void create() {
        SCALE_RATIO = 150 / Gdx.graphics.getWidth();
        shapeFactory = Shape::new;
        
        atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
        swords = new Sprite[10];
        for(int i = 1; i <=10; i++) {
            swords[i - 1] = createScaledSprite(atlas.createSprite("sword" + i));
        }

        player = new Player(Constants.PLAYER_START_SPEED,
                Constants.PLAYER_START_SIZE, Sword.ONE);

        shapes = new ArrayList<Shape>();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(player.getCameraSize(),
                player.getCameraSize() * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        
        box = new Sprite(new Texture(Gdx.files.internal("camera.png")));
        box.setPosition(300, 300);
        box.setSize(100F, 100F);
        boxRect = new Rectangle(box.getX(), box.getY(), 100F, 100F);
        
//        map = new Sprite(new Texture(Gdx.files.internal("camera.png")));
//        map.setPosition(0 + (cam.viewportWidth * cam.zoom / 2F), 0 + (cam.viewportHeight * cam.zoom / 2F));
//        map.setSize(Constants.WORLD_SIZE - cam.viewportWidth + 10F, Constants.WORLD_SIZE - cam.viewportHeight + 10F);

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
           player = new Player(Constants.PLAYER_START_SPEED, Constants.PLAYER_START_SIZE, Sword.ONE);
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

//        batch.begin();
//        map.draw(batch);
//        batch.end();
        
        batch.begin();
        box.draw(batch);
        batch.end();
        
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
        font.draw(batch, "Points: " + player.getPoints(), cam.position.x - 140, cam.position.y + 40);
        batch.end();
        
        if(boxCounter != 0) {
            boxCounter--;
        }
        if(swordCount != 0) {
            swordCount--;
        }
    }
    
    public void drawSword() {
        float array[] = getHypotenuse(false);
        swordX = array[0] + cam.position.x - 512 * player.getSword().getScaleSize() / 2;
        swordY = array[1] + cam.position.y - 512 * player.getSword().getScaleSize() / 2;             

        batch.draw(swords[player.getSword().getIndex()].getTexture(), swordX, swordY, player.getSize() / 2, 0,
                swords[player.getSword().getIndex()].getRegionWidth(), swords[player.getSword().getIndex()].getRegionHeight(), 
                player.getSword().getScaleSize(), player.getSword().getScaleSize(), swordAnimationSteps * leftOrRight * 15,
                swords[player.getSword().getIndex()].getRegionX(), swords[player.getSword().getIndex()].getRegionY(), 
                swords[player.getSword().getIndex()].getRegionWidth(), swords[player.getSword().getIndex()].getRegionHeight(), false, false);
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
                int x = MathUtils.random(2) == 1 ? 600 : -600;
                shapes.add(shapeFactory.create(ShapeDefs.ZOMBIE, player,
                        MathUtils.random(600) + x, MathUtils.random(600) + x,
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

        if (Gdx.input.isTouched()) {
            if (animateSword == false && swordCount == 0) {
                leftOrRight = ((Gdx.input.getX() - 826) > 0) ? -1 : 1;
                animateSword = true;
                swordAnimationSteps = 0;
            }
            
            coords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            
            if(boxRect.contains(coords.x, coords.y)) {
                touchedBox();
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
    
    public static Sprite createScaledSprite(Sprite sprite2) {
        Sprite sprite = new Sprite(sprite2);
        sprite.getTexture().setFilter(TextureFilter.Linear,
                 TextureFilter.Linear);
        sprite.setSize(sprite.getWidth() / SCALE_RATIO,
             sprite.getHeight() / SCALE_RATIO);
        return sprite;
    }

    public void swordAnimation() {
        if (swordAnimationSteps > 21) {
            swordAnimationSteps = 0;
            swordCount = player.getSword().getSpeed() * 10;
            animateSword = false;
        } else {
            swordAnimationSteps += 1;
            swordCollision();
        }
    }
    
    public void touchedBox() {
        if(boxCounter == 0) {
            if(player.getPoints() < 950){
                System.out.println("You don't have enough points! Get 950");
                return;
            } 
            player.setPoints(player.getPoints() - 950);
            
            int rand = MathUtils.random(100);
            for(Sword s : Sword.values()) {
                System.out.println(rand);
                System.out.println(s.getRarity());
                if(rand >= s.getRarity()) {
                    continue;
                } else {
                    if(s.getRarity() >= player.getSword().getRarity()) {
                        System.out.println("You got nothing of value.");
                    } else {
                        System.out.println("You got sword: " + s);
                        player.setSword(s);
                    }
                    break;
                }
            }
            
            boxCounter = 60;
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
                    player.setPoints(player.getPoints() + 75);
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
