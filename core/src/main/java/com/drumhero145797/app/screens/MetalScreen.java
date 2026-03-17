package com.drumhero145797.app.screens;

import com.badlogic.gdx.graphics.Color;
import com.drumhero145797.app.Constants;
import com.drumhero145797.app.MainGame;

/**
 * Metal band gameplay — extreme-speed note barrage.
 * Lane colours: bright crimson / orange-red / deep red / hot pink.
 */
public class MetalScreen extends BaseGameScreen {

    private static final Color[] LANE_BG = {
        new Color(0.863f, 0.078f, 0.235f, 1f),  // crimson   #DC143C
        new Color(1.00f,  0.42f,  0.21f,  1f),  // orange-red #FF6B35
        new Color(0.545f, 0.00f,  0.00f,  1f),  // deep red   #8B0000
        new Color(1.00f,  0.09f,  0.267f, 1f),  // hot red    #FF1744
    };

    private static final Color[] NOTE_COL = {
        new Color(1.00f, 0.30f, 0.40f,  1f),  // bright crimson
        new Color(1.00f, 0.60f, 0.20f,  1f),  // bright orange
        new Color(1.00f, 0.09f, 0.267f, 1f),  // primary red
        new Color(1.00f, 0.50f, 0.65f,  1f),  // pink
    };

    public MetalScreen(MainGame game) { super(game); }

    @Override protected float  getNoteSpeed()    { return Constants.NOTE_SPEED_METAL; }
    @Override protected float  getNoteInterval() { return Constants.NOTE_INTERVAL_METAL; }
    @Override protected int    getBandIndex()    { return Constants.BAND_METAL; }
    @Override protected Color  getLaneColor(int lane) { return LANE_BG[lane]; }
    @Override protected Color  getNoteColor(int lane) { return NOTE_COL[lane]; }
    @Override protected String getPerfectText()  { return "SHRED!"; }
}
