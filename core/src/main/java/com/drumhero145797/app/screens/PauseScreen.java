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

public class PauseScreen implements Screen {

    private final MainGame           game;
    private final Screen             previousScreen;
    private final int                bandIndex;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    private static final float BTN_W    = Constants.BTN_MAIN_W;
    private static final float BTN_H    = Constants.BTN_MAIN_H;
    private static final float BTN_X    = (Constants.WORLD_WIDTH - BTN_W) / 2f;
    private static final float RESUME_Y  = 490f;
    private static final float RESTART_Y = 400f;
    private static final float MENU_Y    = 310f;

    public PauseScreen(MainGame game, Screen previousScreen, int bandIndex) {
        this.game           = game;
        this.previousScreen = previousScreen;
        this.bandIndex      = bandIndex;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
    }

    @Override
    public void show() {
        setupInput();
        // Pause music while on pause screen
        if (game.currentMusic != null && game.currentMusic.isPlaying()) {
            game.currentMusic.pause();
        }
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    doResume();
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
        if (hit(wx, wy, BTN_X, RESUME_Y, BTN_W, BTN_H)) {
            doResume();
        } else if (hit(wx, wy, BTN_X, RESTART_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            Screen newGame;
            if (bandIndex == Constants.BAND_ROCK)       newGame = new RockScreen(game);
            else if (bandIndex == Constants.BAND_JAZZ)  newGame = new JazzScreen(game);
            else                                        newGame = new MetalScreen(game);
            game.setScreen(newGame);
        } else if (hit(wx, wy, BTN_X, MENU_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void doResume() {
        playSfx("sounds/sfx/sfx_button_click.ogg");
        game.setScreen(previousScreen); // calls previousScreen.show() → re-registers input + resumes music
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

        // Dark overlay
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0f, 0f, 0f, 0.85f);
        game.shapeRenderer.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Title
        game.batch.begin();
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "PAUSED");
        game.fontTitle.draw(game.batch, "PAUSED",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 700f);

        // Band label
        String bandName = (bandIndex == Constants.BAND_ROCK) ? "ROCK" :
                          (bandIndex == Constants.BAND_JAZZ) ? "JAZZ" : "METAL";
        game.fontBody.setColor(1f, 0.918f, 0f, 1f);
        layout.setText(game.fontBody, bandName);
        game.fontBody.draw(game.batch, bandName,
                (Constants.WORLD_WIDTH - layout.width) / 2f, 640f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "RESUME",    BTN_X, RESUME_Y,  BTN_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "RESTART",   BTN_X, RESTART_Y, BTN_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "MAIN MENU", BTN_X, MENU_Y,    BTN_W, BTN_H);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
