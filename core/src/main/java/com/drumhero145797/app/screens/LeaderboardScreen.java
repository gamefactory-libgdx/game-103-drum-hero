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

public class LeaderboardScreen implements Screen {

    // SharedPreferences key prefix for leaderboard entries
    private static final String LB_KEY = "lb_";

    private final MainGame          game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    private final int[] scores = new int[Constants.LEADERBOARD_SIZE];

    // Main Menu button
    private static final float BTN_W = Constants.BTN_MAIN_W;
    private static final float BTN_H = Constants.BTN_MAIN_H;
    private static final float BTN_X = (Constants.WORLD_WIDTH - BTN_W) / 2f;
    private static final float BTN_Y = 50f;

    // Row layout
    private static final float ROW_X = 20f;
    private static final float ROW_W = Constants.WORLD_WIDTH - 40f;
    private static final float ROW_H = 52f;
    private static final float ROW_START_Y = 720f;
    private static final float ROW_GAP    = 56f;

    public LeaderboardScreen(MainGame game) {
        this.game = game;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            scores[i] = prefs.getInteger(LB_KEY + i, 0);
        }
    }

    /**
     * Inserts a score into the top-10 leaderboard stored in SharedPreferences.
     * Scores are maintained in descending order.
     */
    public static void addScore(int score) {
        if (score <= 0) return;
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int[] existing = new int[Constants.LEADERBOARD_SIZE];
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            existing[i] = prefs.getInteger(LB_KEY + i, 0);
        }
        // Find insertion position
        int insertAt = -1;
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            if (score > existing[i]) {
                insertAt = i;
                break;
            }
        }
        if (insertAt < 0) return; // score didn't make the board
        // Shift lower entries down
        for (int i = Constants.LEADERBOARD_SIZE - 1; i > insertAt; i--) {
            prefs.putInteger(LB_KEY + i, existing[i - 1]);
        }
        prefs.putInteger(LB_KEY + insertAt, score);
        prefs.flush();
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
        if (wx >= BTN_X && wx <= BTN_X + BTN_W && wy >= BTN_Y && wy <= BTN_Y + BTN_H) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        }
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
        layout.setText(game.fontTitle, "LEADERBOARD");
        game.fontTitle.draw(game.batch, "LEADERBOARD",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 800f);

        game.fontBody.setColor(1f, 0.918f, 0f, 1f); // accent
        layout.setText(game.fontBody, "LOCAL HIGH SCORES");
        game.fontBody.draw(game.batch, "LOCAL HIGH SCORES",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 758f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        // Draw each row background + text
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            float rowY = ROW_START_Y - i * ROW_GAP;

            // Row background via ShapeRenderer
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            if (i == 0) {
                game.shapeRenderer.setColor(1f, 0.84f, 0f, 0.15f);  // gold tint
            } else if (i == 1) {
                game.shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 0.12f); // silver tint
            } else if (i == 2) {
                game.shapeRenderer.setColor(0.80f, 0.50f, 0.20f, 0.12f); // bronze tint
            } else {
                game.shapeRenderer.setColor(0f, 0f, 0f, 0.25f);
            }
            game.shapeRenderer.rect(ROW_X, rowY - ROW_H + 10f, ROW_W, ROW_H);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Row text
            game.batch.begin();
            String rank  = (i + 1) + ".";
            String value = scores[i] > 0 ? String.valueOf(scores[i]) : "---";

            game.fontBody.setColor(Color.WHITE);
            game.fontBody.draw(game.batch, rank, ROW_X + 8f, rowY);

            game.fontBody.setColor(1f, 0.918f, 0f, 1f); // accent for score
            layout.setText(game.fontBody, value);
            game.fontBody.draw(game.batch, value,
                    ROW_X + ROW_W - layout.width - 8f, rowY);
            game.fontBody.setColor(Color.WHITE);
            game.batch.end();
        }

        // Main Menu button
        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "MAIN MENU", BTN_X, BTN_Y, BTN_W, BTN_H);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
