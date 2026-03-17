package com.drumhero145797.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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

public class SettingsScreen implements Screen {

    private final MainGame          game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    private boolean musicOn;
    private boolean sfxOn;

    // Toggle button geometry
    private static final float TOG_W  = Constants.BTN_MAIN_W;
    private static final float TOG_H  = Constants.BTN_MAIN_H;
    private static final float TOG_X  = (Constants.WORLD_WIDTH - TOG_W) / 2f;
    private static final float MUSIC_Y = 580f;
    private static final float SFX_Y   = 490f;

    // Main Menu button
    private static final float MENU_W = Constants.BTN_MAIN_W;
    private static final float MENU_H = Constants.BTN_MAIN_H;
    private static final float MENU_X = (Constants.WORLD_WIDTH - MENU_W) / 2f;
    private static final float MENU_Y = 80f;

    public SettingsScreen(MainGame game) {
        this.game = game;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicOn = prefs.getBoolean(Constants.PREF_MUSIC, true);
        sfxOn   = prefs.getBoolean(Constants.PREF_SFX,   true);
        game.musicEnabled = musicOn;
        game.sfxEnabled   = sfxOn;
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
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        if (hitRect(wx, wy, TOG_X, MUSIC_Y, TOG_W, TOG_H)) {
            musicOn = !musicOn;
            game.musicEnabled = musicOn;
            prefs.putBoolean(Constants.PREF_MUSIC, musicOn);
            prefs.flush();
            if (game.currentMusic != null) {
                if (musicOn) game.currentMusic.play();
                else         game.currentMusic.pause();
            }
            playSfx("sounds/sfx/sfx_toggle.ogg");

        } else if (hitRect(wx, wy, TOG_X, SFX_Y, TOG_W, TOG_H)) {
            sfxOn = !sfxOn;
            game.sfxEnabled = sfxOn;
            prefs.putBoolean(Constants.PREF_SFX, sfxOn);
            prefs.flush();
            playSfx("sounds/sfx/sfx_toggle.ogg");

        } else if (hitRect(wx, wy, MENU_X, MENU_Y, MENU_W, MENU_H)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private boolean hitRect(float wx, float wy, float rx, float ry, float rw, float rh) {
        return wx >= rx && wx <= rx + rw && wy >= ry && wy <= ry + rh;
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
        layout.setText(game.fontTitle, "SETTINGS");
        game.fontTitle.draw(game.batch, "SETTINGS",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 760f);
        game.batch.end();

        // Music toggle
        String musicLabel = musicOn ? "MUSIC: ON" : "MUSIC: OFF";
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                musicLabel, TOG_X, MUSIC_Y, TOG_W, TOG_H);

        // SFX toggle
        String sfxLabel = sfxOn ? "SFX: ON" : "SFX: OFF";
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                sfxLabel, TOG_X, SFX_Y, TOG_W, TOG_H);

        // Main Menu button
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "MAIN MENU", MENU_X, MENU_Y, MENU_W, MENU_H);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
