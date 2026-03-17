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

public class GameOverScreen implements Screen {

    private final MainGame          game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec  = new Vector3();
    private final GlyphLayout        layout    = new GlyphLayout();

    private final int score;
    private final int maxStreak; // extra parameter (streak / stars)
    private final int personalBest;

    // Button geometry
    private static final float BTN_W  = Constants.BTN_MAIN_W;
    private static final float BTN_H  = Constants.BTN_MAIN_H;
    private static final float BTN_X  = (Constants.WORLD_WIDTH - BTN_W) / 2f;
    private static final float RETRY_Y = 220f;
    private static final float MENU_Y  = 140f;

    /**
     * @param game      the main game instance
     * @param score     final score achieved in this run
     * @param maxStreak max consecutive perfect streak achieved (extra context)
     */
    public GameOverScreen(MainGame game, int score, int maxStreak) {
        this.game      = game;
        this.score     = score;
        this.maxStreak = maxStreak;

        // Save score and update personal best
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int prev = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > prev) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }
        personalBest = Math.max(score, prev);

        LeaderboardScreen.addScore(score);

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
    }

    @Override
    public void show() {
        setupInput();
        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled) game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);
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
        if (hitRect(wx, wy, BTN_X, RETRY_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new BandSelectScreen(game));
        } else if (hitRect(wx, wy, BTN_X, MENU_Y, BTN_W, BTN_H)) {
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

        game.batch.begin();

        // "GAME OVER" title
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "GAME OVER");
        game.fontTitle.draw(game.batch, "GAME OVER",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 760f);

        // Final score
        game.fontScore.setColor(1f, 0.918f, 0f, 1f); // accent #FFEA00
        String scoreStr = String.valueOf(score);
        layout.setText(game.fontScore, scoreStr);
        game.fontScore.draw(game.batch, scoreStr,
                (Constants.WORLD_WIDTH - layout.width) / 2f, 640f);
        game.fontScore.setColor(Color.WHITE);

        // Labels
        game.fontBody.setColor(Color.WHITE);

        game.fontBody.draw(game.batch, "FINAL SCORE",
                40f, 680f);

        game.fontBody.draw(game.batch, "BEST: " + personalBest,
                40f, 560f);

        game.fontBody.draw(game.batch, "MAX STREAK: " + maxStreak,
                40f, 510f);

        game.batch.end();

        // Buttons
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "PLAY AGAIN", BTN_X, RETRY_Y, BTN_W, BTN_H);
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "MAIN MENU",  BTN_X, MENU_Y,  BTN_W, BTN_H);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
