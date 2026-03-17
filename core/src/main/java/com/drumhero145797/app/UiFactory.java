package com.drumhero145797.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Pixel-style button factory.
 * Style: sharp rect, 3 px border in primary #FF1744,
 *        semi-transparent black fill, text in accent #FFEA00.
 */
public class UiFactory {

    // Primary color #FF1744
    private static final float PR = 1.00f, PG = 0.09f, PB = 0.267f;
    // Accent color #FFEA00
    private static final float AR = 1.00f, AG = 0.918f, AB = 0.00f;

    private static final GlyphLayout layout = new GlyphLayout();

    /**
     * Draws a pixel-style button. Caller must set projection matrix on both
     * sr and batch before calling this method.
     */
    public static void drawButton(ShapeRenderer sr, SpriteBatch batch, BitmapFont font,
                                   String label, float x, float y, float w, float h) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Outer border rect: primary color
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(PR, PG, PB, 1f);
        sr.rect(x, y, w, h);
        // Inner fill rect: semi-transparent black
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(x + 3, y + 3, w - 6, h - 6);
        sr.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Label in accent color, centered
        batch.begin();
        font.setColor(AR, AG, AB, 1f);
        layout.setText(font, label);
        font.draw(batch, label,
                  x + (w - layout.width) / 2f,
                  y + (h + layout.height) / 2f);
        font.setColor(Color.WHITE);
        batch.end();
    }
}
