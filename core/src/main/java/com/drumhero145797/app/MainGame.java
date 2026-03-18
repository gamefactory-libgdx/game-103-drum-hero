package com.drumhero145797.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.drumhero145797.app.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch    batch;
    public ShapeRenderer  shapeRenderer;
    public AssetManager   manager;
    public BitmapFont     fontBody;
    public BitmapFont     fontTitle;
    public BitmapFont     fontSmall;
    public BitmapFont     fontScore;

    public boolean musicEnabled = true;
    public boolean sfxEnabled   = true;
    public Music   currentMusic = null;

    @Override
    public void create() {
        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        manager       = new AssetManager();

        loadPreferences();
        generateFonts();
        loadAssets();
        manager.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    private void loadPreferences() {
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicEnabled = prefs.getBoolean(Constants.PREF_MUSIC, true);
        sfxEnabled   = prefs.getBoolean(Constants.PREF_SFX,   true);
    }

    private void generateFonts() {
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT_BODY));
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT_TITLE));

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.borderColor = new Color(0f, 0f, 0f, 0.85f);

        param.size        = Constants.FONT_SIZE_SMALL;
        param.borderWidth = 1;
        fontSmall = bodyGen.generateFont(param);

        param.size        = Constants.FONT_SIZE_BODY;
        param.borderWidth = 2;
        fontBody = bodyGen.generateFont(param);

        param.size        = Constants.FONT_SIZE_SCORE;
        param.borderWidth = 3;
        fontScore = titleGen.generateFont(param);

        param.size        = Constants.FONT_SIZE_BIG;
        param.borderWidth = 3;
        fontTitle = titleGen.generateFont(param);

        bodyGen.dispose();
        titleGen.dispose();
    }

    private void loadAssets() {
        // Music
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        // Per-lane musical note sounds — Rock (hit), Jazz (pizz), Metal (8bit)
        for (int i : new int[]{0, 4, 8, 12}) {
            manager.load("sounds/sfx/sfx_jingle_hit_"  + String.format("%02d", i) + ".ogg", Sound.class);
            manager.load("sounds/sfx/sfx_jingle_pizz_" + String.format("%02d", i) + ".ogg", Sound.class);
            manager.load("sounds/sfx/sfx_jingle_8bit_" + String.format("%02d", i) + ".ogg", Sound.class);
        }
    }

    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        manager.dispose();
        fontBody.dispose();
        fontTitle.dispose();
        fontSmall.dispose();
        fontScore.dispose();
    }
}
