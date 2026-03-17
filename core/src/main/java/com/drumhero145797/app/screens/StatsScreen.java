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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;
import com.drumhero145797.app.UiFactory;

public class StatsScreen implements Screen {

    private final MainGame           game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    private static final float BACK_X = 20f;
    private static final float BACK_Y = 30f;
    private static final float BACK_W = 100f;
    private static final float BACK_H = Constants.BTN_SMALL_H;

    // Loaded stats
    private int   totalPlays, rockPlays, jazzPlays, metalPlays;
    private float totalScoreF;
    private int   bestStreak;
    private float accuracy;
    private int   rockBest, jazzBest, metalBest;
    private float rockAcc, jazzAcc, metalAcc;

    public StatsScreen(MainGame game) {
        this.game = game;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        loadStats();
    }

    private void loadStats() {
        Preferences p  = Gdx.app.getPreferences(Constants.PREFS_NAME);
        totalPlays     = p.getInteger(Constants.PREF_TOTAL_PLAYS,  0);
        totalScoreF    = p.getFloat("totalScoreF",                 0f);
        bestStreak     = p.getInteger(Constants.PREF_BEST_STREAK,  0);
        accuracy       = p.getFloat(Constants.PREF_ACCURACY,       0f);
        rockPlays      = p.getInteger(Constants.PREF_ROCK_PLAYS,   0);
        jazzPlays      = p.getInteger(Constants.PREF_JAZZ_PLAYS,   0);
        metalPlays     = p.getInteger(Constants.PREF_METAL_PLAYS,  0);
        rockBest       = p.getInteger(Constants.PREF_ROCK_BEST,    0);
        jazzBest       = p.getInteger(Constants.PREF_JAZZ_BEST,    0);
        metalBest      = p.getInteger(Constants.PREF_METAL_BEST,   0);
        rockAcc        = p.getFloat(Constants.PREF_ROCK_ACC,        0f);
        jazzAcc        = p.getFloat(Constants.PREF_JAZZ_ACC,        0f);
        metalAcc       = p.getFloat(Constants.PREF_METAL_ACC,       0f);
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
                if (hit(touchVec.x, touchVec.y, BACK_X, BACK_Y, BACK_W, BACK_H)) {
                    playSfx("sounds/sfx/sfx_button_back.ogg");
                    game.setScreen(new BandSelectScreen(game));
                }
                return true;
            }
        }));
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

        // ── Header ──────────────────────────────────────────────────────────────
        game.batch.begin();
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "YOUR STATS");
        game.fontTitle.draw(game.batch, "YOUR STATS",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 815f);

        game.fontBody.setColor(1f, 0.918f, 0f, 1f);
        layout.setText(game.fontBody, "LIFETIME PERFORMANCE");
        game.fontBody.draw(game.batch, "LIFETIME PERFORMANCE",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 770f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        // ── 2×2 stat cards ──────────────────────────────────────────────────────
        float cardW = 200f, cardH = 120f, gap = 16f;
        float gridX  = (Constants.WORLD_WIDTH - (2 * cardW + gap)) / 2f; // ≈ 32
        float row1Y  = 620f;
        float row2Y  = row1Y - cardH - gap;

        drawStatCard(gridX,           row1Y, cardW, cardH, "TOTAL PLAYS",
                String.valueOf(totalPlays), "games",
                1f, 0.09f, 0.267f);
        drawStatCard(gridX + cardW + gap, row1Y, cardW, cardH, "TOTAL SCORE",
                formatScore(totalScoreF), "points",
                0f, 0.83f, 1f);
        drawStatCard(gridX,           row2Y, cardW, cardH, "BEST STREAK",
                String.valueOf(bestStreak), "hits",
                0.18f, 0.545f, 0.34f);
        drawStatCard(gridX + cardW + gap, row2Y, cardW, cardH, "ACCURACY",
                String.format("%.1f", accuracy), "%",
                0.863f, 0.078f, 0.235f);

        // ── By-band header ────────────────────────────────────────────────────
        game.batch.begin();
        game.fontBody.setColor(Color.WHITE);
        game.fontBody.draw(game.batch, "BY BAND", 20f, row2Y - 26f);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();

        float bandH = 70f, bandW = Constants.WORLD_WIDTH - 40f, bandX = 20f;
        float b1Y = row2Y - 26f - 10f - bandH;
        float b2Y = b1Y - bandH - 8f;
        float b3Y = b2Y - bandH - 8f;

        drawBandRow(bandX, b1Y, bandW, bandH, "ROCK",  rockPlays,  rockBest,  rockAcc,
                Constants.COLOR_ROCK_R,  Constants.COLOR_ROCK_G,  Constants.COLOR_ROCK_B);
        drawBandRow(bandX, b2Y, bandW, bandH, "JAZZ",  jazzPlays,  jazzBest,  jazzAcc,
                Constants.COLOR_JAZZ_R,  Constants.COLOR_JAZZ_G,  Constants.COLOR_JAZZ_B);
        drawBandRow(bandX, b3Y, bandW, bandH, "METAL", metalPlays, metalBest, metalAcc,
                Constants.COLOR_METAL_R, Constants.COLOR_METAL_G, Constants.COLOR_METAL_B);

        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody,
                "BACK", BACK_X, BACK_Y, BACK_W, BACK_H);
    }

    private void drawStatCard(float x, float y, float w, float h,
                              String label, String value, String unit,
                              float cr, float cg, float cb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0.10f, 0.10f, 0.18f, 1f);
        game.shapeRenderer.rect(x, y, w, h);
        game.shapeRenderer.setColor(cr, cg, cb, 1f);
        game.shapeRenderer.rect(x,         y,         w,  2f);
        game.shapeRenderer.rect(x,         y + h - 2f, w,  2f);
        game.shapeRenderer.rect(x,         y,         2f, h);
        game.shapeRenderer.rect(x + w - 2f, y,         2f, h);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        game.fontSmall.setColor(cr, cg, cb, 1f);
        game.fontSmall.draw(game.batch, label, x + 8f, y + h - 10f);

        game.fontBody.setColor(Color.WHITE);
        layout.setText(game.fontBody, value);
        game.fontBody.draw(game.batch, value, x + (w - layout.width) / 2f, y + h * 0.52f);

        game.fontSmall.setColor(0.70f, 0.70f, 0.70f, 1f);
        layout.setText(game.fontSmall, unit);
        game.fontSmall.draw(game.batch, unit, x + (w - layout.width) / 2f, y + 18f);

        game.fontSmall.setColor(Color.WHITE);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();
    }

    private void drawBandRow(float x, float y, float w, float h,
                             String band, int plays, int best, float acc,
                             float cr, float cg, float cb) {
        float progress = Math.min(plays / 20f, 1f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0.10f, 0.10f, 0.18f, 1f);
        game.shapeRenderer.rect(x, y, w, h);
        // top / bottom borders
        game.shapeRenderer.setColor(cr, cg, cb, 1f);
        game.shapeRenderer.rect(x,         y,         w,  2f);
        game.shapeRenderer.rect(x,         y + h - 2f, w,  2f);
        // progress bar
        float barW = 60f, barH = 8f;
        float barX = x + w - barW - 10f;
        float barY = y + (h - barH) / 2f;
        game.shapeRenderer.setColor(0.06f, 0.06f, 0.12f, 1f);
        game.shapeRenderer.rect(barX, barY, barW, barH);
        game.shapeRenderer.setColor(cr, cg, cb, 1f);
        if (progress > 0f)
            game.shapeRenderer.rect(barX, barY, barW * progress, barH);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        game.fontBody.setColor(cr, cg, cb, 1f);
        game.fontBody.draw(game.batch, band, x + 10f, y + h - 8f);

        game.fontSmall.setColor(Color.WHITE);
        game.fontSmall.draw(game.batch,
                "Plays: " + plays + "   Best: " + best + "   Acc: " + String.format("%.0f%%", acc),
                x + 10f, y + 22f);

        game.fontSmall.setColor(Color.WHITE);
        game.fontBody.setColor(Color.WHITE);
        game.batch.end();
    }

    private String formatScore(float val) {
        if (val >= 1_000_000f) return String.format("%.1fM", val / 1_000_000f);
        if (val >= 1_000f)     return String.format("%.1fK", val / 1_000f);
        return String.valueOf((int) val);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
