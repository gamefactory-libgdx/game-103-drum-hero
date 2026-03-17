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

public class TutorialScreen implements Screen {

    private final MainGame           game;
    private final OrthographicCamera camera;
    private final Viewport           viewport;
    private final Vector3            touchVec = new Vector3();
    private final GlyphLayout        layout   = new GlyphLayout();

    private static final float BACK_X = 20f;
    private static final float BACK_Y = 30f;
    private static final float BACK_W = 100f;
    private static final float BACK_H = Constants.BTN_SMALL_H;

    // Section layout
    private static final float SEC_X = 20f;
    private static final float LINE  = 22f; // line height for wrapped body text

    public TutorialScreen(MainGame game) {
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

        game.batch.begin();

        // Title
        game.fontTitle.setColor(Color.WHITE);
        layout.setText(game.fontTitle, "HOW TO PLAY");
        game.fontTitle.draw(game.batch, "HOW TO PLAY",
                (Constants.WORLD_WIDTH - layout.width) / 2f, 815f);

        // ── Section 1 ──────────────────────────────────────────────────────────
        drawSectionHeader("TAP THE PADS", SEC_X, 755f,
                1f, 0.09f, 0.267f); // primary red
        game.fontSmall.setColor(Color.WHITE);
        drawLine("Four drum pads scroll downward. Tap each pad when the", SEC_X, 725f);
        drawLine("note reaches the timing line to score points.", SEC_X, 725f - LINE);

        // ── Section 2 ──────────────────────────────────────────────────────────
        drawSectionHeader("PERFECT, GOOD, MISS", SEC_X, 665f,
                1f, 0.918f, 0f); // accent yellow
        game.fontSmall.setColor(Color.WHITE);
        drawLine("PERFECT hit (tight window) = +2 pts.", SEC_X, 635f);
        drawLine("GOOD hit (wider window) = +1 pt.", SEC_X, 635f - LINE);
        drawLine("MISS = no points and you lose 1 life.", SEC_X, 635f - LINE * 2f);

        // ── Section 3 ──────────────────────────────────────────────────────────
        drawSectionHeader("DRUM SOLO POWER", SEC_X, 555f,
                0.18f, 0.545f, 0.34f); // jazz green
        game.fontSmall.setColor(Color.WHITE);
        drawLine("Land 10 consecutive PERFECT hits to activate DRUM SOLO.", SEC_X, 525f);
        drawLine("For 5 seconds all points are DOUBLED (2x)!", SEC_X, 525f - LINE);

        // ── Section 4 ──────────────────────────────────────────────────────────
        drawSectionHeader("WATCH YOUR LIVES", SEC_X, 465f,
                0.863f, 0.078f, 0.235f); // metal crimson
        game.fontSmall.setColor(Color.WHITE);
        drawLine("You start with 3 lives. Each missed note costs one life.", SEC_X, 435f);
        drawLine("Lose all 3 lives and it is GAME OVER.", SEC_X, 435f - LINE);

        // ── Section 5 ──────────────────────────────────────────────────────────
        drawSectionHeader("BANDS & DIFFICULTY", SEC_X, 375f,
                0f, 0.83f, 1f); // cyan
        game.fontSmall.setColor(Color.WHITE);
        drawLine("ROCK  = slow steady beats (beginner-friendly)", SEC_X, 345f);
        drawLine("JAZZ  = medium syncopated rhythm (intermediate)", SEC_X, 345f - LINE);
        drawLine("METAL = extreme high-speed barrage (expert)", SEC_X, 345f - LINE * 2f);

        game.fontSmall.setColor(Color.WHITE);
        game.batch.end();

        // Drum-Solo charge bar diagram
        drawChargeBarDiagram(255f);

        UiFactory.drawButton(game.shapeRenderer, game.batch, game.fontBody, "BACK", BACK_X, BACK_Y, BACK_W, BACK_H);
    }

    private void drawSectionHeader(String text, float x, float y, float cr, float cg, float cb) {
        game.fontBody.setColor(cr, cg, cb, 1f);
        game.fontBody.draw(game.batch, text, x, y);
    }

    private void drawLine(String text, float x, float y) {
        game.fontSmall.draw(game.batch, text, x, y);
    }

    /** Draws a small 10-dot charge bar illustrating the Drum Solo mechanic. */
    private void drawChargeBarDiagram(float y) {
        float dotW = 30f, dotH = 12f, gap = 4f;
        float totalW = 10 * dotW + 9 * gap;
        float startX = (Constants.WORLD_WIDTH - totalW) / 2f;

        game.batch.begin();
        game.fontSmall.setColor(0.7f, 0.7f, 0.7f, 1f);
        layout.setText(game.fontSmall, "DRUM SOLO CHARGE:");
        game.fontSmall.draw(game.batch, "DRUM SOLO CHARGE:",
                (Constants.WORLD_WIDTH - layout.width) / 2f, y + 28f);
        game.fontSmall.setColor(Color.WHITE);
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 10; i++) {
            float dx = startX + i * (dotW + gap);
            if (i < 7) {
                game.shapeRenderer.setColor(1f, 0.09f, 0.267f, 0.9f); // filled
            } else {
                game.shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.6f);  // empty
            }
            game.shapeRenderer.rect(dx, y, dotW, dotH);
        }
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {}
}
