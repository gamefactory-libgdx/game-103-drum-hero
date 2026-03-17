package com.drumhero145797.app;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Lane layout
    public static final int   LANE_COUNT        = 4;
    public static final float LANE_WIDTH         = 120f;  // WORLD_WIDTH / LANE_COUNT
    public static final float HUD_TOP_HEIGHT     = 50f;
    public static final float HUD_BOTTOM_HEIGHT  = 204f;  // 854 - 650
    public static final float GAME_AREA_TOP      = 804f;  // WORLD_HEIGHT - HUD_TOP_HEIGHT
    public static final float GAME_AREA_BOTTOM   = 204f;  // HUD_BOTTOM_HEIGHT
    public static final float IMPACT_ZONE_Y      = 204f;  // bottom of game area
    public static final float IMPACT_ZONE_HEIGHT = 40f;

    // Note sizes
    public static final float NOTE_WIDTH  = 100f;
    public static final float NOTE_HEIGHT = 20f;

    // Tap target buttons
    public static final float TAP_BUTTON_SIZE = 100f;

    // Note speeds (world units per second)
    public static final float NOTE_SPEED_ROCK  = 300f;
    public static final float NOTE_SPEED_JAZZ  = 420f;
    public static final float NOTE_SPEED_METAL = 540f;

    // Timing windows (seconds)
    public static final float TIMING_PERFECT = 0.08f;
    public static final float TIMING_GOOD    = 0.15f;

    // Scoring
    public static final int SCORE_PERFECT     = 2;
    public static final int SCORE_GOOD        = 1;
    public static final int SCORE_MISS        = 0;
    public static final int DRUM_SOLO_STREAK  = 10;   // consecutive perfects to activate
    public static final float DRUM_SOLO_DURATION = 5f; // seconds
    public static final int SCORE_MULTIPLIER  = 2;

    // Note spawn interval (seconds) — base, multiplied by density
    public static final float NOTE_INTERVAL_ROCK  = 0.55f;
    public static final float NOTE_INTERVAL_JAZZ  = 0.35f;
    public static final float NOTE_INTERVAL_METAL = 0.22f;

    // UI sizes
    public static final float BTN_MAIN_W   = 320f;
    public static final float BTN_MAIN_H   = 64f;
    public static final float BTN_SMALL_W  = 100f;
    public static final float BTN_SMALL_H  = 50f;
    public static final float BTN_ICON_W   = 60f;
    public static final float BTN_ICON_H   = 60f;
    public static final float BAND_CARD_W  = 360f;
    public static final float BAND_CARD_H  = 140f;
    public static final float PAUSE_BTN_SIZE = 48f;

    // Font sizes
    public static final int FONT_SIZE_SMALL  = 12;
    public static final int FONT_SIZE_BODY   = 18;
    public static final int FONT_SIZE_BUTTON = 22;
    public static final int FONT_SIZE_TITLE  = 36;
    public static final int FONT_SIZE_BIG    = 48;
    public static final int FONT_SIZE_SCORE  = 52;

    // Font files (assigned by pipeline)
    public static final String FONT_TITLE = "fonts/Kongtext.ttf";
    public static final String FONT_BODY  = "fonts/Manaspace.ttf";

    // Color palette (packed RGBA8888)
    public static final float COLOR_BRAND_R    = 1.00f; public static final float COLOR_BRAND_G    = 0.42f; public static final float COLOR_BRAND_B    = 0.21f;
    public static final float COLOR_DARK_R     = 0.10f; public static final float COLOR_DARK_G     = 0.10f; public static final float COLOR_DARK_B     = 0.18f;
    public static final float COLOR_CYAN_R     = 0.00f; public static final float COLOR_CYAN_G     = 0.83f; public static final float COLOR_CYAN_B     = 1.00f;
    public static final float COLOR_ROCK_R     = 0.545f; public static final float COLOR_ROCK_G     = 0.00f; public static final float COLOR_ROCK_B     = 0.00f;
    public static final float COLOR_JAZZ_R     = 0.18f; public static final float COLOR_JAZZ_G     = 0.545f; public static final float COLOR_JAZZ_B     = 0.34f;
    public static final float COLOR_METAL_R    = 0.863f; public static final float COLOR_METAL_G    = 0.078f; public static final float COLOR_METAL_B    = 0.235f;

    // SharedPreferences keys
    public static final String PREFS_NAME        = "DrumHeroPrefs";
    public static final String PREF_MUSIC        = "musicEnabled";
    public static final String PREF_SFX          = "sfxEnabled";
    public static final String PREF_HIGH_SCORE   = "highScore";
    public static final String PREF_HIGH_ROCK    = "highScoreRock";
    public static final String PREF_HIGH_JAZZ    = "highScoreJazz";
    public static final String PREF_HIGH_METAL   = "highScoreMetal";
    public static final String PREF_TOTAL_PLAYS  = "totalPlays";
    public static final String PREF_TOTAL_SCORE  = "totalScore";
    public static final String PREF_BEST_STREAK  = "bestStreak";
    public static final String PREF_ACCURACY     = "accuracy";
    public static final String PREF_ROCK_PLAYS   = "rockPlays";
    public static final String PREF_JAZZ_PLAYS   = "jazzPlays";
    public static final String PREF_METAL_PLAYS  = "metalPlays";
    public static final String PREF_ROCK_BEST    = "rockBest";
    public static final String PREF_JAZZ_BEST    = "jazzBest";
    public static final String PREF_METAL_BEST   = "metalBest";
    public static final String PREF_ROCK_ACC     = "rockAccuracy";
    public static final String PREF_JAZZ_ACC     = "jazzAccuracy";
    public static final String PREF_METAL_ACC    = "metalAccuracy";
    public static final String PREF_VIBRATION    = "vibration";
    public static final String PREF_COLORBLIND   = "colorBlind";
    public static final String PREF_MUSIC_VOL    = "musicVolume";
    public static final String PREF_SFX_VOL      = "sfxVolume";

    // Leaderboard
    public static final int LEADERBOARD_SIZE = 10;

    // Band indices
    public static final int BAND_ROCK  = 0;
    public static final int BAND_JAZZ  = 1;
    public static final int BAND_METAL = 2;
}
