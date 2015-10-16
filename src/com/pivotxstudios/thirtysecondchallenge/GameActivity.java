package com.pivotxstudios.thirtysecondchallenge;

import java.io.IOException;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.pivotxstudios.thirtysecondchallenge.controller.db.DBController;

public class GameActivity extends SimpleBaseGameActivity {

    public DBController dbController;
    
    @Override
    public EngineOptions onCreateEngineOptions() {
        return null;
    }

    @Override
    protected void onCreateResources() throws IOException {

    }

    @Override
    protected Scene onCreateScene() {
        return null;
    }

}
