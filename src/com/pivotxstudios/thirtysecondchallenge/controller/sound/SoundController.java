package com.pivotxstudios.thirtysecondchallenge.controller.sound;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import com.pivotxstudios.thirtysecondchallenge.GameActivity;

public class SoundController {
    public Sound TIME, SLIDE, LOST;

    private boolean mSoundEnabled = true;
    GameActivity _game;

    public SoundController(GameActivity game) {
        _game = game;
        SoundFactory.setAssetBasePath("sounds/");
        SoundManager sm = _game.getEngine().getSoundManager();
        try {
            TIME = SoundFactory.createSoundFromAsset(sm, _game, "time.ogg");
            SLIDE = SoundFactory.createSoundFromAsset(sm, _game, "slide.ogg");
            LOST = SoundFactory.createSoundFromAsset(sm, _game, "lost.ogg");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void play(Sound sound) {
        if (mSoundEnabled) {
            sound.play();
        }
    }

    public synchronized void stop(Sound sound) {
        sound.stop();
    }

    public void setSoundEnabled(boolean v) {
        mSoundEnabled = v;
    }

    public boolean isSoundEnabled() {
        return mSoundEnabled;
    }

}
