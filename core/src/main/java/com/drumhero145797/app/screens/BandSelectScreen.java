package com.drumhero145797.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;
import com.drumhero145797.app.UiFactory;

public class BandSelectScreen implements Screen {

    private final MainGame           game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    // Band card geometry
    private static final float CARD_W = Constants.BAND_CARD_W;   // 360
    private static final float CARD_H = Constants.BAND_CARD_H;   // 140
    private static final float CARD_X = (Constants.WORLD_WIDTH - CARD_W) / 2f; // 60

    private static final float ROCK_Y  = 560f;
    private static final float JAZZ_Y  = 400f;
    private static final float METAL_Y = 240f;

    // Footer buttons
    private static final float BTN_H  = Constants.BTN_SMALL_H;  // 50
    private static final float BACK_X = 20f;
    private static final float BACK_Y = 30f;
    private static final float BACK_W = 100f;

    private static final float TUTO_W = 120f;
    private static final float TUTO_X = Constants.WORLD_WIDTH - 20f - TUTO_W;
    private static final float TUTO_Y = 30f;

    private static final float STATS_W = 110f;
    private static final float STATS_X = TUTO_X - STATS_W - 12f;
    private static final float STATS_Y = 30f;

    public BandSelectScreen(MainGame game) {
        this.game = game;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
    }

    @Override
    public void show() {
        setupInput();
        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int sx, int sy, int pointer, int btn) {
                touchVec.set(sx, sy, 0);
                camera.unproject(touchVec,
                        viewport.getScreenX(), viewport.getScreenY(),
                        viewport.getScreenWidth(), viewport.getScreenHeight());
                onTouch(touchVec.x, touchVec.y);
                return true;
            }
        }));
    }

    private void onTouch(float wx, float wy) {
        if (hit(wx, wy, CARD_X, ROCK_Y, CARD_W, CARD_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new RockScreen(game));
        } else if (hit(wx, wy, CARD_X, JAZZ_Y, CARD_W, CARD_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new JazzScreen(game));
        } else if (hit(wx, wy, CARD_X, METAL_Y, CARD_W, CARD_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new MetalScreen(game));
        } else if (hit(wx, wy, BACK_X, BACK_Y, BACK_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        } else if (hit(wx, wy, STATS_X, STATS_Y, STATS_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new StatsScreen(game));
        } else if (hit(wx, wy, TUTO_X, TUTO_Y, TUTO_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new TutorialScreen(game));
        }
    }

    private boolean hit(float wx, float wy, float rx, float ry, float rw, float rh) {
        return wx >= rx && wx <= rx + rw && wy >= ry && wy <= ry + rh;
    }

    private void playSfx(String path) {
        if (game.sfxEnabled) game.manager.get(path, Sound.class).play(1.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.129f, 0.129f, 0.129f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Title
        game.batch.begin();
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "SELECT BAND");
        game.fontTitle.draw(game.batch, "SELECT BAND",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 810f);

        game.fontBody.setColor(1f, 0.918f, 0f, 1f);
        layout.setText(game.fontBody, "CHOOSE YOUR DIFFICULTY");
        game.fontBody.draw(game.batch, "CHOOSE YOUR DIFFICULTY",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 760f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        // Band cards
        drawCard(ROCK_Y,  "ROCK",  "STEADY BEATS",  "Low-Mid density",
                Constants.COLOR_ROCK_R,  Constants.COLOR_ROCK_G,  Constants.COLOR_ROCK_B);
        drawCard(JAZZ_Y,  "JAZZ",  "SYNCOPATED",    "High density",
                Constants.COLOR_JAZZ_R,  Constants.COLOR_JAZZ_G,  Constants.COLOR_JAZZ_B);
        drawCard(METAL_Y, "METAL", "EXTREME SPEED", "Max density",
                Constants.COLOR_METAL_R, Constants.COLOR_METAL_G, Constants.COLOR_METAL_B);

        // Footer buttons
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "BACK",     BACK_X,  BACK_Y,  BACK_W,  BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "STATS",    STATS_X, STATS_Y, STATS_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "TUTORIAL", TUTO_X,  TUTO_Y,  TUTO_W,  BTN_H);
    }

    private void drawCard(float cardY, String name, String tagline, String desc,
                          float cr, float cg, float cb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Left colour strip
        game.shapeRenderer.setColor(cr, cg, cb, 1f);
        game.shapeRenderer.rect(CARD_X, cardY, CARD_W * 0.35f, CARD_H);

        // Right dark area
        game.shapeRenderer.setColor(0.10f, 0.10f, 0.18f, 0.97f);
        game.shapeRenderer.rect(CARD_X + CARD_W * 0.35f, cardY, CARD_W * 0.65f, CARD_H);

        // Border (3 px, band colour)
        game.shapeRenderer.setColor(cr, cg, cb, 1f);
        game.shapeRenderer.rect(CARD_X,              cardY,              CARD_W,  3f);
        game.shapeRenderer.rect(CARD_X,              cardY + CARD_H - 3f, CARD_W, 3f);
        game.shapeRenderer.rect(CARD_X,              cardY,              3f,      CARD_H);
        game.shapeRenderer.rect(CARD_X + CARD_W - 3f, cardY,            3f,      CARD_H);

        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        float textX = CARD_X + CARD_W * 0.38f;

        game.batch.begin();
        game.fontBody.setColor(Color.WHITE);
        game.fontBody.draw(game.batch, name, textX, cardY + CARD_H - 16f);

        game.fontSmall.setColor(1f, 0.918f, 0f, 1f);
        game.fontSmall.draw(game.batch, tagline, textX, cardY + CARD_H * 0.55f + 6f);

        game.fontSmall.setColor(0.78f, 0.78f, 0.78f, 1f);
        game.fontSmall.draw(game.batch, desc, textX, cardY + 20f);

        game.fontSmall.setColor(Color.WHITE);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
