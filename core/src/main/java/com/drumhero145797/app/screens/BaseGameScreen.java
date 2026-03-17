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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;

/**
 * Abstract base for Rock / Jazz / Metal gameplay screens.
 * Sub-classes supply band-specific speed, interval, colours and feedback text.
 */
public abstract class BaseGameScreen implements Screen {

    // ── Note data object ────────────────────────────────────────────────────
    protected static final class Note {
        int   lane;
        float y;
        boolean active = true;
        Note(int lane, float y) { this.lane = lane; this.y = y; }
    }

    // ── LibGDX plumbing ─────────────────────────────────────────────────────
    protected final MainGame            game;
    protected final OrthographicCamera  camera;
    protected final Viewport            viewport;
    protected final Vector3             touchVec = new Vector3();
    protected final GlyphLayout         layout   = new GlyphLayout();

    // ── Game state ──────────────────────────────────────────────────────────
    protected int     score         = 0;
    protected int     streak        = 0;
    protected int     maxStreak     = 0;
    protected int     lives         = 3;
    protected int     perfectCharge = 0;   // consecutive PERFECTs toward drum solo
    protected float   drumSoloTimer = 0f;
    protected boolean gameOverTriggered = false;

    protected int   totalNotes = 0;
    protected int   totalHits  = 0;

    // ── Notes ───────────────────────────────────────────────────────────────
    protected final Array<Note> notes      = new Array<>();
    protected float             spawnTimer = 0f;

    // ── Hit-text feedback per lane ──────────────────────────────────────────
    private static final float FEEDBACK_DURATION = 0.55f;
    protected final String[] laneText  = new String[4];
    protected final float[]  laneTimer = new float[4];

    // ── Geometry constants ──────────────────────────────────────────────────
    private static final float IMPACT_CENTER_Y =
            Constants.IMPACT_ZONE_Y + Constants.IMPACT_ZONE_HEIGHT / 2f; // 224

    private static final float PAUSE_X =
            Constants.WORLD_WIDTH - Constants.PAUSE_BTN_SIZE - 8f;       // 424
    private static final float PAUSE_Y =
            Constants.WORLD_HEIGHT - Constants.HUD_TOP_HEIGHT - Constants.PAUSE_BTN_SIZE - 6f; // 792

    // ── Abstract API for sub-classes ────────────────────────────────────────
    protected abstract float  getNoteSpeed();
    protected abstract float  getNoteInterval();
    protected abstract int    getBandIndex();
    protected abstract Color  getLaneColor(int lane);   // background tint
    protected abstract Color  getNoteColor(int lane);   // note fill colour
    protected abstract String getPerfectText();          // "PERFECT!" / "GROOVE!" / "SHRED!"

    // ── Constructor ─────────────────────────────────────────────────────────
    protected BaseGameScreen(MainGame game) {
        this.game = game;
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
    }

    // ── Screen lifecycle ────────────────────────────────────────────────────

    @Override
    public void show() {
        setupInput();
        game.playMusic("sounds/music/music_gameplay.ogg");
    }

    /** Called by PauseScreen → game.setScreen(previousScreen) via LibGDX show(). */
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
                handleTouch(touchVec.x, touchVec.y);
                return true;
            }
        }));
        // Resume music when returning from PauseScreen
        if (game.musicEnabled && game.currentMusic != null && !game.currentMusic.isPlaying()) {
            game.currentMusic.play();
        }
    }

    // ── Input handling ──────────────────────────────────────────────────────

    private void handleTouch(float wx, float wy) {
        if (gameOverTriggered) return;

        // Pause button (top-right corner of gameplay area)
        if (wx >= PAUSE_X && wx <= PAUSE_X + Constants.PAUSE_BTN_SIZE &&
            wy >= PAUSE_Y && wy <= PAUSE_Y + Constants.PAUSE_BTN_SIZE) {
            if (game.currentMusic != null) game.currentMusic.pause();
            game.setScreen(new PauseScreen(game, this, getBandIndex()));
            return;
        }

        // Any other touch → lane tap (full-width columns)
        int lane = MathUtils.clamp((int)(wx / Constants.LANE_WIDTH), 0, 3);
        processTap(lane);
    }

    private void processTap(int lane) {
        float speed     = getNoteSpeed();
        float perfectPx = Constants.TIMING_PERFECT * speed;
        float goodPx    = Constants.TIMING_GOOD    * speed;

        Note  best     = null;
        float bestDist = Float.MAX_VALUE;

        for (Note n : notes) {
            if (!n.active || n.lane != lane) continue;
            float noteCenter = n.y + Constants.NOTE_HEIGHT / 2f;
            float dist       = Math.abs(noteCenter - IMPACT_CENTER_Y);
            if (dist < bestDist) { bestDist = dist; best = n; }
        }

        if (best == null) return;

        if      (bestDist <= perfectPx) registerHit(lane, best, true);
        else if (bestDist <= goodPx)    registerHit(lane, best, false);
        // tap outside timing window — ignore
    }

    // ── Score / hit logic ───────────────────────────────────────────────────

    private void registerHit(int lane, Note note, boolean perfect) {
        note.active = false;
        totalNotes++;
        totalHits++;

        int multiplier = (drumSoloTimer > 0f) ? Constants.SCORE_MULTIPLIER : 1;
        score += (perfect ? Constants.SCORE_PERFECT : Constants.SCORE_GOOD) * multiplier;

        if (perfect) {
            streak++;
            perfectCharge++;
            if (streak > maxStreak) maxStreak = streak;

            if (perfectCharge >= Constants.DRUM_SOLO_STREAK && drumSoloTimer <= 0f) {
                drumSoloTimer = Constants.DRUM_SOLO_DURATION;
                perfectCharge = 0;
                playSfx("sounds/sfx/sfx_level_complete.ogg");
            }
            laneText[lane]  = getPerfectText();
        } else {
            streak        = 0;
            perfectCharge = 0;
            laneText[lane] = "GOOD";
        }
        laneTimer[lane] = FEEDBACK_DURATION;
        playSfx("sounds/sfx/sfx_hit.ogg");
    }

    private void registerMiss(int lane) {
        if (gameOverTriggered) return;
        totalNotes++;
        streak        = 0;
        perfectCharge = 0;
        lives--;
        laneText[lane]  = "MISS";
        laneTimer[lane] = FEEDBACK_DURATION;
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_hit.ogg", Sound.class).play(0.3f);
        if (lives <= 0) triggerGameOver();
    }

    private void triggerGameOver() {
        gameOverTriggered = true;
        saveStats();
        game.setScreen(new GameOverScreen(game, score, maxStreak));
    }

    private void playSfx(String path) {
        if (game.sfxEnabled) game.manager.get(path, Sound.class).play(1.0f);
    }

    // ── Update ──────────────────────────────────────────────────────────────

    private void update(float delta) {
        if (gameOverTriggered) return;

        float speed        = getNoteSpeed();
        float missThresh   = Constants.IMPACT_ZONE_Y - Constants.TIMING_GOOD * speed - 4f;

        // Drum solo countdown
        if (drumSoloTimer > 0f) drumSoloTimer -= delta;

        // Move notes + detect misses
        for (int i = notes.size - 1; i >= 0; i--) {
            Note n = notes.get(i);
            if (!n.active) { notes.removeIndex(i); continue; }
            n.y -= speed * delta;
            if (n.y + Constants.NOTE_HEIGHT < missThresh) {
                n.active = false;
                notes.removeIndex(i);
                registerMiss(n.lane);
            }
        }

        // Spawn new note
        spawnTimer -= delta;
        if (spawnTimer <= 0f) {
            spawnTimer += getNoteInterval();
            notes.add(new Note(MathUtils.random(0, 3), Constants.GAME_AREA_TOP));
        }

        // Decay hit-text timers
        for (int i = 0; i < 4; i++) {
            if (laneTimer[i] > 0f) laneTimer[i] -= delta;
        }
    }

    // ── Render ──────────────────────────────────────────────────────────────

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.10f, 0.10f, 0.18f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        drawLanes();
        drawImpactZone();
        drawNotes();
        drawTapButtons();
        drawTopHud();
        drawPauseButton();
        drawFeedbackText();
    }

    private void drawLanes() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 4; i++) {
            Color c = getLaneColor(i);
            game.shapeRenderer.setColor(c.r * 0.28f, c.g * 0.28f, c.b * 0.28f, 1f);
            game.shapeRenderer.rect(i * Constants.LANE_WIDTH, 0f,
                    Constants.LANE_WIDTH, Constants.WORLD_HEIGHT);
        }
        // Lane dividers
        game.shapeRenderer.setColor(1f, 0.09f, 0.267f, 0.22f);
        for (int i = 1; i < 4; i++) {
            game.shapeRenderer.rect(i * Constants.LANE_WIDTH - 1f, 0f, 2f, Constants.WORLD_HEIGHT);
        }
        game.shapeRenderer.end();
    }

    private void drawImpactZone() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(1f, 1f, 1f, 0.06f);
        game.shapeRenderer.rect(0, Constants.IMPACT_ZONE_Y,
                Constants.WORLD_WIDTH, Constants.IMPACT_ZONE_HEIGHT);
        // Top/bottom border lines
        game.shapeRenderer.setColor(1f, 0.09f, 0.267f, 0.85f);
        game.shapeRenderer.rect(0, Constants.IMPACT_ZONE_Y, Constants.WORLD_WIDTH, 2f);
        game.shapeRenderer.rect(0,
                Constants.IMPACT_ZONE_Y + Constants.IMPACT_ZONE_HEIGHT - 2f,
                Constants.WORLD_WIDTH, 2f);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawNotes() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Note n : notes) {
            if (!n.active) continue;
            Color c  = getNoteColor(n.lane);
            float nx = n.lane * Constants.LANE_WIDTH
                     + (Constants.LANE_WIDTH - Constants.NOTE_WIDTH) / 2f;
            game.shapeRenderer.setColor(c.r, c.g, c.b, 1f);
            game.shapeRenderer.rect(nx, n.y, Constants.NOTE_WIDTH, Constants.NOTE_HEIGHT);
            // White outline top/bottom
            game.shapeRenderer.setColor(1f, 1f, 1f, 0.35f);
            game.shapeRenderer.rect(nx, n.y, Constants.NOTE_WIDTH, 1.5f);
            game.shapeRenderer.rect(nx, n.y + Constants.NOTE_HEIGHT - 1.5f,
                    Constants.NOTE_WIDTH, 1.5f);
        }
        game.shapeRenderer.end();
    }

    private void drawTapButtons() {
        float btnSize = Constants.TAP_BUTTON_SIZE;
        float btnY    = (Constants.GAME_AREA_BOTTOM - btnSize) / 2f; // ≈ 52

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 4; i++) {
            Color c  = getNoteColor(i);
            float bx = i * Constants.LANE_WIDTH + (Constants.LANE_WIDTH - btnSize) / 2f;
            // Fill
            game.shapeRenderer.setColor(c.r * 0.45f, c.g * 0.45f, c.b * 0.45f, 0.88f);
            game.shapeRenderer.rect(bx, btnY, btnSize, btnSize);
            // Border
            game.shapeRenderer.setColor(c.r, c.g, c.b, 1f);
            game.shapeRenderer.rect(bx, btnY, btnSize, 2f);
            game.shapeRenderer.rect(bx, btnY + btnSize - 2f, btnSize, 2f);
            game.shapeRenderer.rect(bx, btnY, 2f, btnSize);
            game.shapeRenderer.rect(bx + btnSize - 2f, btnY, 2f, btnSize);
        }
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // "TAP" labels
        game.batch.begin();
        game.fontSmall.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            float bx = i * Constants.LANE_WIDTH + (Constants.LANE_WIDTH - btnSize) / 2f;
            layout.setText(game.fontSmall, "TAP");
            game.fontSmall.draw(game.batch, "TAP",
                    bx + (btnSize - layout.width) / 2f,
                    btnY + (btnSize + layout.height) / 2f);
        }
        game.batch.end();
    }

    private void drawTopHud() {
        // HUD bar background
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0f, 0f, 0f, 0.72f);
        game.shapeRenderer.rect(0, Constants.WORLD_HEIGHT - Constants.HUD_TOP_HEIGHT,
                Constants.WORLD_WIDTH, Constants.HUD_TOP_HEIGHT);

        // Charge bar (3 px strip at very top of screen)
        if (drumSoloTimer > 0f) {
            float pct = drumSoloTimer / Constants.DRUM_SOLO_DURATION;
            game.shapeRenderer.setColor(1f, 0.918f, 0f, 1f);
            game.shapeRenderer.rect(0, Constants.WORLD_HEIGHT - 3f, Constants.WORLD_WIDTH * pct, 3f);
        } else if (perfectCharge > 0) {
            float pct = perfectCharge / (float) Constants.DRUM_SOLO_STREAK;
            game.shapeRenderer.setColor(1f, 0.09f, 0.267f, 0.7f);
            game.shapeRenderer.rect(0, Constants.WORLD_HEIGHT - 3f, Constants.WORLD_WIDTH * pct, 3f);
        }
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        float hudTextY = Constants.WORLD_HEIGHT - 10f;
        game.batch.begin();

        // Score (left)
        game.fontSmall.setColor(Color.WHITE);
        game.fontSmall.draw(game.batch, "SCORE: " + score, 8f, hudTextY);

        // Streak (centre)
        game.fontSmall.setColor(1f, 0.09f, 0.267f, 1f);
        String streakStr = "STREAK: " + streak;
        layout.setText(game.fontSmall, streakStr);
        game.fontSmall.draw(game.batch, streakStr,
                (Constants.WORLD_WIDTH - layout.width) / 2f, hudTextY);

        // Lives (right)
        game.fontSmall.setColor(1f, 0.918f, 0f, 1f);
        String livesStr = "LIVES: " + lives;
        layout.setText(game.fontSmall, livesStr);
        game.fontSmall.draw(game.batch, livesStr,
                Constants.WORLD_WIDTH - layout.width - 8f, hudTextY);
        game.fontSmall.setColor(Color.WHITE);

        // Drum solo banner (just below HUD bar)
        if (drumSoloTimer > 0f) {
            game.fontBody.setColor(1f, 0.918f, 0f, 1f);
            String solo = "2x DRUM SOLO!";
            layout.setText(game.fontBody, solo);
            game.fontBody.draw(game.batch, solo,
                    (Constants.WORLD_WIDTH - layout.width) / 2f,
                    Constants.WORLD_HEIGHT - Constants.HUD_TOP_HEIGHT - 6f);
            game.fontBody.setColor(Color.WHITE);
        }

        game.batch.end();
    }

    private void drawPauseButton() {
        float s = Constants.PAUSE_BTN_SIZE;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Background
        game.shapeRenderer.setColor(0.10f, 0.10f, 0.18f, 0.90f);
        game.shapeRenderer.rect(PAUSE_X, PAUSE_Y, s, s);
        // Border
        game.shapeRenderer.setColor(1f, 0.09f, 0.267f, 1f);
        game.shapeRenderer.rect(PAUSE_X,         PAUSE_Y,         s, 2f);
        game.shapeRenderer.rect(PAUSE_X,         PAUSE_Y + s - 2f, s, 2f);
        game.shapeRenderer.rect(PAUSE_X,         PAUSE_Y,         2f, s);
        game.shapeRenderer.rect(PAUSE_X + s - 2f, PAUSE_Y,         2f, s);
        // Pause icon (two vertical bars)
        float barW = 6f, barH = 20f;
        float barY = PAUSE_Y + (s - barH) / 2f;
        float bar1X = PAUSE_X + (s - 2f * barW - 6f) / 2f;
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.rect(bar1X,             barY, barW, barH);
        game.shapeRenderer.rect(bar1X + barW + 6f, barY, barW, barH);
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawFeedbackText() {
        float btnSize = Constants.TAP_BUTTON_SIZE;
        float btnY    = (Constants.GAME_AREA_BOTTOM - btnSize) / 2f; // ≈ 52
        float popBase = btnY + btnSize + 10f;

        game.batch.begin();
        for (int i = 0; i < 4; i++) {
            if (laneTimer[i] <= 0f || laneText[i] == null) continue;
            float alpha = Math.min(1f, laneTimer[i] / (FEEDBACK_DURATION * 0.5f));
            // Pop upward as timer elapses
            float ty = popBase + (FEEDBACK_DURATION - laneTimer[i]) * 80f;

            Color col;
            if      ("MISS".equals(laneText[i]))   col = new Color(1f, 0.09f, 0.267f, alpha);
            else if ("GOOD".equals(laneText[i]))   col = new Color(1f, 0.918f, 0f, alpha);
            else                                   col = new Color(0f, 1f, 0.5f, alpha);

            float cx = i * Constants.LANE_WIDTH + Constants.LANE_WIDTH / 2f;
            game.fontSmall.setColor(col);
            layout.setText(game.fontSmall, laneText[i]);
            game.fontSmall.draw(game.batch, laneText[i], cx - layout.width / 2f, ty);
        }
        game.fontSmall.setColor(Color.WHITE);
        game.batch.end();
    }

    // ── Stats persistence ───────────────────────────────────────────────────

    private void saveStats() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);

        int   prevPlays = p.getInteger(Constants.PREF_TOTAL_PLAYS, 0);
        float prevAcc   = p.getFloat(Constants.PREF_ACCURACY, 0f);
        float thisAcc   = (totalNotes > 0) ? (100f * totalHits / totalNotes) : 0f;
        float newAcc    = prevPlays > 0
                ? (prevAcc * prevPlays + thisAcc) / (prevPlays + 1)
                : thisAcc;

        p.putInteger(Constants.PREF_TOTAL_PLAYS, prevPlays + 1);
        p.putFloat("totalScoreF", p.getFloat("totalScoreF", 0f) + score);
        p.putFloat(Constants.PREF_ACCURACY, newAcc);

        if (maxStreak > p.getInteger(Constants.PREF_BEST_STREAK, 0))
            p.putInteger(Constants.PREF_BEST_STREAK, maxStreak);

        int band = getBandIndex();
        if (band == Constants.BAND_ROCK) {
            p.putInteger(Constants.PREF_ROCK_PLAYS, p.getInteger(Constants.PREF_ROCK_PLAYS, 0) + 1);
            if (score > p.getInteger(Constants.PREF_ROCK_BEST, 0))
                p.putInteger(Constants.PREF_ROCK_BEST, score);
            p.putFloat(Constants.PREF_ROCK_ACC, thisAcc);
        } else if (band == Constants.BAND_JAZZ) {
            p.putInteger(Constants.PREF_JAZZ_PLAYS, p.getInteger(Constants.PREF_JAZZ_PLAYS, 0) + 1);
            if (score > p.getInteger(Constants.PREF_JAZZ_BEST, 0))
                p.putInteger(Constants.PREF_JAZZ_BEST, score);
            p.putFloat(Constants.PREF_JAZZ_ACC, thisAcc);
        } else {
            p.putInteger(Constants.PREF_METAL_PLAYS, p.getInteger(Constants.PREF_METAL_PLAYS, 0) + 1);
            if (score > p.getInteger(Constants.PREF_METAL_BEST, 0))
                p.putInteger(Constants.PREF_METAL_BEST, score);
            p.putFloat(Constants.PREF_METAL_ACC, thisAcc);
        }

        LeaderboardScreen.addScore(score);
        p.flush();
    }

    // ── Boilerplate ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
