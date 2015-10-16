package com.pivotxstudios.thirtysecondchallenge.controller.game;

import java.util.ArrayList;
import java.util.List;

import com.pivotxstudios.thirtysecondchallenge.GameActivity;
import com.pivotxstudios.thirtysecondchallenge.model.Config;
import com.pivotxstudios.thirtysecondchallenge.model.Pair;

public class ScoreController {

        GameActivity _game;
        int numplays[];
        int _bestScore;
        int _totalPoint;
        
        public ScoreController(GameActivity game) {
                _game = game;
                numplays = _game.dbController.getNumPlayed();
                _totalPoint = _game.dbController.getTotalPoint();
        }
        public int getNumPlayAll() {
                return numplays[0];
        }
        
        public int getNumPlayEasy() {
                return numplays[1];
        }

        public int getNumPlayMedium() {
                return numplays[2];
        }
        
        public int getNumPlayHard() {
                return numplays[3];
        }
        
        public List<Pair> getUserTop10Weekly(int mode) {
                int[] scores = new int[10];
                int count = _game.dbController.getUserWeekTopScore(mode, scores);
                ArrayList<Pair> ret = new ArrayList<Pair>();
                for (int i = 0; i < count; i++) {
                        ret.add(new Pair("You", scores[i]));
                }
                _bestScore = scores[0];
                return ret;
        }
        
        public List<Pair> getUserTop10AllTime(int mode) {
                int[] scores = new int[10];
                int count = _game.dbController.getUserTopScore(mode, scores);
                ArrayList<Pair> ret = new ArrayList<Pair>();
                for (int i = 0; i < count; i++) {
                        ret.add(new Pair("You", scores[i]));
                }
                _bestScore = scores[0];
                return ret;
        }
        
        public int getUserTopScore() {
                return _bestScore;
        }
        
        public int getTotalPoint() {
                return _totalPoint;
        }
        
        public void recordScore(int mode, int score) {
                _game.dbController.recordScore(mode, score);
                _totalPoint += score;
                _game.dbController.updateTotalPoint(_totalPoint);
        }
        
        public void addBonusScore(int score) {
                _totalPoint += score;
                _game.dbController.updateTotalPoint(_totalPoint);
        }
        
        
        public void recordGamePlayed(int mode) {
                numplays[0]++;
                switch (mode) {
                case Config.MODE_EASY:
                        numplays[1]++;
                        break;
                case Config.MODE_MEDIUM:
                        numplays[2]++;
                        break;
                case Config.MODE_HARD:
                        numplays[3]++;
                        break;
                }
                _game.dbController.recordPlay(numplays[0], numplays[1], numplays[2], numplays[3]);
        }

}
