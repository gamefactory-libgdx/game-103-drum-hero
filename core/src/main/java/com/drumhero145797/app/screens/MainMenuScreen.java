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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;
import com.drumhero145797.app.UiFactory;

public class MainMenuScreen implements Screen {

    private final MainGame         game;
    private final OrthographicCamera camera;
    private final Viewport          viewport;
    private final Vector3           touchVec = new Vector3();
    private final GlyphLayout       layout   = new GlyphLayout();

    // Button geometry
    private static final float BTN_W = Constants.BTN_MAIN_W;
    private static final float BTN_H = Constants.BTN_MAIN_H;
    private static final float BTN_X = (Constants.WORLD_WIDTH - BTN_W) / 2f;

    private static final float BTN_PLAY_Y      = 500f;
    private static final float BTN_LBOARD_Y    = 410f;
    private static final float BTN_SETTINGS_Y  = 320f;

    public MainMenuScreen(MainGame game) {
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
                    Gdx.app.exit();
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
        if (hitBtn(wx, wy, BTN_PLAY_Y)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new BandSelectScreen(game));
        } else if (hitBtn(wx, wy, BTN_LBOARD_Y)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new LeaderboardScreen(game));
        } else if (hitBtn(wx, wy, BTN_SETTINGS_Y)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new SettingsScreen(game));
        }
    }

    private boolean hitBtn(float wx, float wy, float by) {
        return wx >= BTN_X && wx <= BTN_X + BTN_W && wy >= by && wy <= by + BTN_H;
    }

    private void playSfx(String path) {
        if (game.sfxEnabled) game.manager.get(path, Sound.class).play(1.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.129f, 0.129f, 0.129f, 1f); // #212121
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Title
        game.batch.begin();
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "DRUM HERO");
        game.fontTitle.draw(game.batch, "DRUM HERO",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 760f);

        game.fontBody.setColor(1f, 0.918f, 0f, 1f); // accent #FFEA00
        layout.setText(game.fontBody, "RHYTHM AWAITS");
        game.fontBody.draw(game.batch, "RHYTHM AWAITS",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 700f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        // Buttons
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "START GAME",  BTN_X, BTN_PLAY_Y,     BTN_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "LEADERBOARD", BTN_X, BTN_LBOARD_Y,   BTN_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "SETTINGS",    BTN_X, BTN_SETTINGS_Y, BTN_W, BTN_H);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
