package com.drumhero145797.app.screens;

import com.badlogic.gdx.graphics.Color;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;

/**
 * Jazz band gameplay — medium-speed syncopated notes.
 * Lane colours: forest green / electric cyan / orange-red / deep red.
 */
public class JazzScreen extends BaseGameScreen {

    private static final Color[] LANE_BG = {
        new Color(0.18f,  0.545f, 0.34f,  1f),  // forest green #2E8B57
        new Color(0.00f,  0.831f, 1.00f,  1f),  // cyan         #00D4FF
        new Color(1.00f,  0.42f,  0.21f,  1f),  // orange-red   #FF6B35
        new Color(0.545f, 0.00f,  0.00f,  1f),  // deep red     #8B0000
    };

    private static final Color[] NOTE_COL = {
        new Color(0.24f, 0.85f,  0.54f,  1f),  // bright green
        new Color(0.40f, 0.90f,  1.00f,  1f),  // bright cyan
        new Color(1.00f, 0.60f,  0.30f,  1f),  // bright orange
        new Color(1.00f, 0.09f,  0.267f, 1f),  // primary red
    };

    public JazzScreen(MainGame game) { super(game); }

    @Override protected float  getNoteSpeed()    { return Constants.NOTE_SPEED_JAZZ; }
    @Override protected float  getNoteInterval() { return Constants.NOTE_INTERVAL_JAZZ; }
    @Override protected int    getBandIndex()    { return Constants.BAND_JAZZ; }
    @Override protected Color  getLaneColor(int lane) { return LANE_BG[lane]; }
    @Override protected Color  getNoteColor(int lane) { return NOTE_COL[lane]; }
    @Override protected String getPerfectText()  { return "GROOVE!"; }
    @Override protected String getBackgroundPath() { return "backgrounds/game/2-background_1.png"; }
    @Override protected String[] getLaneSounds() {
        return new String[]{
            "sounds/sfx/sfx_jingle_pizz_00.ogg",
            "sounds/sfx/sfx_jingle_pizz_04.ogg",
            "sounds/sfx/sfx_jingle_pizz_08.ogg",
            "sounds/sfx/sfx_jingle_pizz_12.ogg"
        };
    }
}
