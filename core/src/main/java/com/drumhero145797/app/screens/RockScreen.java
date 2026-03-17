package com.drumhero145797.app.screens;

import com.badlogic.gdx.graphics.Color;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;

/**
 * Rock band gameplay — slow, steady beats.
 * Lane colours: deep red / forest green / electric cyan / crimson.
 */
public class RockScreen extends BaseGameScreen {

    // Lane background tints
    private static final Color[] LANE_BG = {
        new Color(0.545f, 0.00f,  0.00f,  1f),  // deep red   #8B0000
        new Color(0.18f,  0.545f, 0.34f,  1f),  // forest grn #2E8B57
        new Color(0.00f,  0.831f, 1.00f,  1f),  // cyan       #00D4FF
        new Color(0.863f, 0.078f, 0.235f, 1f),  // crimson    #DC143C
    };

    // Note fill colours (brighter than lane tints)
    private static final Color[] NOTE_COL = {
        new Color(1.00f,  0.09f,  0.267f, 1f),  // primary red  #FF1744
        new Color(0.24f,  0.75f,  0.44f,  1f),  // bright green
        new Color(0.40f,  0.90f,  1.00f,  1f),  // bright cyan
        new Color(1.00f,  0.25f,  0.35f,  1f),  // bright crimson
    };

    public RockScreen(MainGame game) { super(game); }

    @Override protected float  getNoteSpeed()    { return Constants.NOTE_SPEED_ROCK; }
    @Override protected float  getNoteInterval() { return Constants.NOTE_INTERVAL_ROCK; }
    @Override protected int    getBandIndex()    { return Constants.BAND_ROCK; }
    @Override protected Color  getLaneColor(int lane) { return LANE_BG[lane]; }
    @Override protected Color  getNoteColor(int lane) { return NOTE_COL[lane]; }
    @Override protected String getPerfectText()  { return "PERFECT!"; }
}
