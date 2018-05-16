package org.diiage.amassey.partiel2018massey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReleaseDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DB_NAME = "releases.db";

    // Release
    public static final String TABLE_RELEASE = "'release'";
    public static final String TABLE_RELEASE_ID = "id";
    public static final String TABLE_RELEASE_ARTIST = "artist";
    public static final String TABLE_RELEASE_RESSOURCE_URL = "ressource_url";
    public static final String TABLE_RELEASE_YEAR = "year";
    public static final String TABLE_RELEASE_CATNO = "catno";
    public static final String TABLE_RELEASE_TITLE = "title";
    public static final String TABLE_RELEASE_FORMAT = "format";
    public static final String TABLE_RELEASE_THUMB = "thumb";
    public static final String TABLE_RELEASE_STATUS = "status";
    public static final String TABLE_RELEASE_ID_ARTIST = "idArtist";

    // Artist
    public static final String TABLE_ARTIST = "artist";
    public static final String TABLE_ARTIST_ID = "id";
    public static final String TABLE_ARTIST_NOM = "nom";

    // LinkReleaseArtist
    public static final String TABLE_LINK_RELEASE_ARTIST = "linkReleaseArtist";
    public static final String TABLE_LINK_RELEASE_ARTIST_ID_RELEASE = "idRelease";
    public static final String TABLE_LINK_RELEASE_ARTIST_ID_ARTIST = "idArtist";

    public ReleaseDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RELEASE +" ( " +
                TABLE_RELEASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_RELEASE_ARTIST + " TEXT, " +
                TABLE_RELEASE_RESSOURCE_URL + " TEXT, " +
                TABLE_RELEASE_YEAR + " INTEGER, " +
                TABLE_RELEASE_CATNO + " TEXT, " +
                TABLE_RELEASE_TITLE + " TEXT, " +
                TABLE_RELEASE_FORMAT + " TEXT, " +
                TABLE_RELEASE_THUMB + " TEXT, " +
                TABLE_RELEASE_STATUS + " TEXT )");

    }

    //Méthode apellé lorsqu'il est nécéssaire de mettre a jour la structure de la base de données
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ARTIST +" ( " +
                    TABLE_ARTIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_ARTIST_NOM  + " TEXT )");

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_LINK_RELEASE_ARTIST +" ( " +
                    TABLE_LINK_RELEASE_ARTIST_ID_RELEASE + " INTEGER, " +
                    TABLE_LINK_RELEASE_ARTIST_ID_ARTIST + " INTEGER )");

            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_RELEASE + " ADD " + TABLE_RELEASE_ID_ARTIST + " INTEGER");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
