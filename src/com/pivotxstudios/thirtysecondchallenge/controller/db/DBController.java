package com.pivotxstudios.thirtysecondchallenge.controller.db;

import com.pivotxstudios.thirtysecondchallenge.model.Config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "TSCDB";

    private static final String DB_TABLE_AWARD = "award";
    private static final String DB_TABLE_CONFIG_NAME = "tscconfig";
    private static final String DB_TABLE_SCORE_EASY = "scoreeasy";
    private static final String DB_TABLE_SCORE_MEDIUM = "scoremedium";
    private static final String DB_TABLE_SCORE_HARD = "scorehard";

    private static final String DB_COL_ID = "id";

    /* award */
    private static final String DB_COL_AWARD_ID = "awardid";

    /* programstat */
    private static final String DB_COL_TOTALPOINT = "totalpoint";
    private static final String DB_COL_NUMPLAYED = "numplayed";
    private static final String DB_COL_NUMOPENED = "numopened";
    private static final String DB_COL_NUMPLAYED_EASY = "numplayedeasy";
    private static final String DB_COL_NUMPLAYED_MEDIUM = "numplayedmedium";
    private static final String DB_COL_NUMPLAYED_HARD = "numplayedhard";
    private static final String DB_COL_SOUND = "sound";
    private static final String DB_COL_VIBRATE = "vibrate";

    /* Easy, Medium, Hard */
    private static final String DB_COL_SCORE = "score";
    private static final String DB_COL_DATE = "datetime";

    private int vibrate_state = -1;

    public DBController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tbAward = "CREATE TABLE " + DB_TABLE_AWARD + "(" + DB_COL_ID + " INTEGER PRIMARY KEY," + DB_COL_AWARD_ID + " INTEGER" + ")";
        db.execSQL(tbAward);

        String tbConfig = "CREATE TABLE " + DB_TABLE_CONFIG_NAME + "(" + DB_COL_ID + " INTEGER PRIMARY KEY," + DB_COL_TOTALPOINT + " INTEGER," + DB_COL_NUMPLAYED + " INTEGER,"
                + DB_COL_NUMOPENED + " INTEGER," + DB_COL_NUMPLAYED_EASY + " INTEGER," + DB_COL_NUMPLAYED_MEDIUM + " INTEGER," + DB_COL_NUMPLAYED_HARD + " INTEGER," + DB_COL_SOUND
                + " INTEGER," + DB_COL_VIBRATE + " INTEGER" + ")";
        db.execSQL(tbConfig);

        String tbScoreEasy = "CREATE TABLE " + DB_TABLE_SCORE_EASY + "(" + DB_COL_ID + " INTEGER PRIMARY KEY," + DB_COL_SCORE + " INTEGER," + DB_COL_DATE
                + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
        db.execSQL(tbScoreEasy);

        String tbScoreMedium = "CREATE TABLE " + DB_TABLE_SCORE_MEDIUM + "(" + DB_COL_ID + " INTEGER PRIMARY KEY," + DB_COL_SCORE + " INTEGER," + DB_COL_DATE
                + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
        db.execSQL(tbScoreMedium);

        String tbScoreHard = "CREATE TABLE " + DB_TABLE_SCORE_HARD + "(" + DB_COL_ID + " INTEGER PRIMARY KEY," + DB_COL_SCORE + " INTEGER," + DB_COL_DATE
                + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
        db.execSQL(tbScoreHard);

        this.mInitDbRow(db);
    }

    private void mInitDbRow(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DB_COL_SOUND, 1);
        values.put(DB_COL_TOTALPOINT, 0);
        values.put(DB_COL_VIBRATE, 1);
        values.put(DB_COL_NUMPLAYED, 0);
        values.put(DB_COL_NUMOPENED, 0);
        values.put(DB_COL_NUMPLAYED_EASY, 0);
        values.put(DB_COL_NUMPLAYED_MEDIUM, 0);
        values.put(DB_COL_NUMPLAYED_HARD, 0);
        db.insert(DB_TABLE_CONFIG_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int getNumOpened() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE_CONFIG_NAME, new String[] { DB_COL_NUMOPENED }, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    public void recordGameOpen() {
        int c = getNumOpened() + 1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DB_COL_NUMOPENED, c);

            db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?", new String[] { "1" });
        } catch (Exception e) {
        }
    }

    public int[] getNumPlayed() {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] ret = new int[4];
        Cursor cursor = db.query(DB_TABLE_CONFIG_NAME, new String[] { DB_COL_NUMPLAYED, DB_COL_NUMPLAYED_EASY, DB_COL_NUMPLAYED_MEDIUM, DB_COL_NUMPLAYED_HARD }, null, null, null,
                null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                cursor.close();
                return null;
            }
            cursor.moveToFirst();
            ret[0] = cursor.getInt(0); // All num played
            ret[1] = cursor.getInt(1); // Easy num played
            ret[2] = cursor.getInt(2); // Medium num played
            ret[3] = cursor.getInt(3); // Hard num played
            cursor.close();
            return ret;
        }
        return null;
    }

    public void issueAward(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + DB_TABLE_AWARD + " WHERE " + DB_COL_AWARD_ID + "=?";
            Cursor cursor = db.rawQuery(selectQuery, new String[] { id + "" });
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    cursor.close();
                    ContentValues values = new ContentValues();
                    values.put(DB_COL_AWARD_ID, id);
                    db.insert(DB_TABLE_AWARD, null, values);
                } else {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getIssuedAwards() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + DB_TABLE_AWARD;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            int size = cursor.getCount();
            if (size > 0) {
                int[] ret = new int[size];
                cursor.moveToFirst();
                int i = 0;
                while (cursor.isAfterLast() == false) {
                    ret[i] = cursor.getInt(1);
                    cursor.moveToNext();
                    i++;
                }
                return ret;
            }
        }
        return null;
    }

    public void recordPlay(int all, int easy, int medium, int hard) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DB_COL_NUMPLAYED, all);
            values.put(DB_COL_NUMPLAYED_EASY, easy);
            values.put(DB_COL_NUMPLAYED_MEDIUM, medium);
            values.put(DB_COL_NUMPLAYED_HARD, hard);
            db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?", new String[] { "1" });
        } catch (Exception e) {
            ;
        }
    }

    public void recordScore(int mode, int score) {
        String dbname = (mode == Config.MODE_HARD) ? DB_TABLE_SCORE_HARD : (mode == Config.MODE_MEDIUM ? DB_TABLE_SCORE_MEDIUM : DB_TABLE_SCORE_HARD);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COL_SCORE, score);
        db.insert(dbname, null, values);
    }

    public int getTotalPoint() {
        int ret = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT  \"" + DB_COL_TOTALPOINT + "\" FROM " + DB_TABLE_CONFIG_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor == null) {
                return 0;
            }
            if (cursor.moveToFirst()) {
                ret = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
        } catch (Exception e) {
        }
        return ret;
    }

    public void updateTotalPoint(int point) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DB_COL_TOTALPOINT, point);
            db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?", new String[] { "1" });
        } catch (Exception e) {
            ;
        }
    }

    public int getUserWeekTopScore(int mode, int[] scores) {
        String dbname = (mode == Config.MODE_HARD) ? DB_TABLE_SCORE_HARD : (mode == Config.MODE_MEDIUM ? DB_TABLE_SCORE_MEDIUM : DB_TABLE_SCORE_HARD);
        int size = scores.length;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + dbname + " WHERE date(" + DB_COL_DATE + ") >= DATE('now', '-7 days') ORDER BY " + DB_COL_SCORE + " DESC LIMIT " + size;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null || cursor.getCount() <= 0)
            return 0;
        cursor.moveToFirst();
        int count = cursor.getCount();
        int i = 0;
        while (!cursor.isAfterLast()) {
            scores[i] = cursor.getInt(1);
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        return count;
    }

    public int getUserTopScore(int mode, int[] scores) {
        String dbname = (mode == Config.MODE_HARD) ? DB_TABLE_SCORE_HARD : (mode == Config.MODE_MEDIUM ? DB_TABLE_SCORE_MEDIUM : DB_TABLE_SCORE_HARD);
        int size = scores.length;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbname, new String[] { DB_COL_SCORE }, null, null, null, null, DB_COL_SCORE + " DESC", size + "");
        if (cursor == null || cursor.getCount() <= 0)
            return 0;
        cursor.moveToFirst();
        int count = cursor.getCount();
        int i = 0;
        while (!cursor.isAfterLast()) {
            scores[i] = cursor.getInt(0);
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        return count;
    }

    public boolean isVibrationEnable() {

        if (vibrate_state != -1)
            return vibrate_state == 1;

        boolean ret = true;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT  \"" + DB_COL_VIBRATE + "\" FROM " + DB_TABLE_CONFIG_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor == null) {
                return true;
            }
            if (cursor.moveToFirst()) {
                int v = Integer.parseInt(cursor.getString(0));
                ret = v == 1;
            }
            cursor.close();
            vibrate_state = ret ? 1 : 0;
        } catch (Exception e) {
            ret = true;
        }
        return ret;

    }

    public int setVibrationEnable(int flag) {
        int ret = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DB_COL_VIBRATE, flag);
            vibrate_state = flag;
            ret = db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?", new String[] { "1" });
        } catch (Exception e) {
            ;
        }
        return ret;

    }

    public boolean isSoundEnable() {
        boolean ret = true;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT  \"" + DB_COL_SOUND + "\" FROM " + DB_TABLE_CONFIG_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor == null) {
                return true;
            }
            if (cursor.moveToFirst()) {
                int v = Integer.parseInt(cursor.getString(0));
                ret = v == 1;
            }
            cursor.close();
        } catch (Exception e) {
            ret = true;
        }

        return ret;
    }

    public int setSoundEnable(int flag) {
        int ret = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DB_COL_SOUND, flag);

            ret = db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?", new String[] { "1" });
        } catch (Exception e) {
            ;
        }
        return ret;

    }
}
