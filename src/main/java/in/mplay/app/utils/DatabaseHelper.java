package in.mplay.app.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.mplay.app.json.JsonMovies;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String database_name = "megaplay.db";
    private static final String table_movies = "table_movies";
    private static final String table_categories = "table_categories";

    private static final String categories = "categories";
    private static final String cid = "cid";

    private static final String id_row = "id";
    private static final String title_row = "title";
    private static final String description_row = "description";
    private static final String quality_row = "quality";
    private static final String language_row = "language";
    private static final String channel_row = "channel";
    private static final String category_row = "category";
    private static final String img_link_row = "img_link";
    private static final String d_link_row = "d_link";
    private static final String date_row = "date";

    private String getlinks;
    public DatabaseHelper(Context context) {
        super(context, database_name, null, 3);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_categories + " (cid integer primary key,categories text)");
        db.execSQL("create table " + table_movies + " (id integer primary key,title text,description text,quality text,language text,channel text,category text,img_link text,d_link text,date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_categories);
        db.execSQL("DROP TABLE IF EXISTS " + table_movies);
        onCreate(db);
    }

    public void refreshDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table_movies);
    }

    @SuppressLint("Recycle")
    public void deleteCat(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_categories,cid+"=?",  new String[]{id});
    }

    public boolean setCategory(String i, String category) {
        long result = 0;
        SQLiteDatabase read = this.getReadableDatabase();
        SQLiteDatabase write = getWritableDatabase();
        String countQuery = "select  * from " + table_categories + " where cid='" + i + "'";
        Cursor cursor = read.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        ContentValues contentValues = new ContentValues();
        if(count == 0) {
            contentValues.put(cid, i);
            contentValues.put(categories, category);
            result = write.insert(table_categories, null, contentValues);
        }else {
            contentValues.put(categories, category);
            result = write.update(table_categories, contentValues, cid+"=?",  new String[]{i});
        }
        return result != -1;
    }

    public List < String > getCategory() {
        SQLiteDatabase db = getWritableDatabase();
        List < String > list = new ArrayList < > ();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select  * from " + table_categories + " order by cid asc", null);

        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }

        return list;
    }

    public boolean addMovies(JsonMovies model) {
        long result = 0;
        SQLiteDatabase read = this.getReadableDatabase();
        SQLiteDatabase write = getWritableDatabase();
        String countQuery = "select  * from " + table_movies + " where id='" + model.getId() + "'";
        Cursor cursor = read.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put(id_row, model.getId());
        contentValues.put(title_row, model.getTitle());
        contentValues.put(description_row, model.getDescription());
        contentValues.put(quality_row, model.getQuality());
        contentValues.put(language_row, model.getLanguage());
        contentValues.put(channel_row, model.getChannel());
        contentValues.put(category_row, model.getCategory());
        contentValues.put(img_link_row, model.getImg_link());
        contentValues.put(d_link_row, model.getD_link());
        contentValues.put(date_row, model.getDate());

        if(count == 0) {
            result = write.insert(table_movies, null, contentValues);
        }else {
            result = write.update(table_movies, contentValues, id_row+"=?",  new String[]{model.getId()});
        }
        return result != -1;
    }
    public int getCount(String category) {
        String countQuery = "select  * from " + table_movies + " where category='" + category + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getRows() {
        String countQuery = "select  * from " + table_movies;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public String getlinks(String title) {
        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select  * from " + table_movies + " where title='" + title + "'", null);

        while (cursor.moveToNext()) {
            getlinks = cursor.getString(8);

        }
        return getlinks;
    }
    public List <JsonMovies> allmovies(String category) {
        SQLiteDatabase db = getWritableDatabase();
        List < JsonMovies > list = new ArrayList < > ();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select  * from " + table_movies + " where category='" + category+"' order by id desc", null);

        while (cursor.moveToNext()) {
            JsonMovies add = new JsonMovies(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            list.add(add);
        }

        return list;

    }

    public List <JsonMovies> getSlider_list(String category) {
        SQLiteDatabase db = getWritableDatabase();
        List < JsonMovies > list = new ArrayList < > ();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select  * from " + table_movies + " where category='" + category+"' order by id desc limit 10", null);

        while (cursor.moveToNext()) {
            JsonMovies add = new JsonMovies(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            list.add(add);
        }

        return list;

    }

    public List <JsonMovies> RelatedVideos(String category, int limit) {
        SQLiteDatabase db = getWritableDatabase();
        List < JsonMovies > list = new ArrayList < > ();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select  * from " + table_movies + " where category='" + category+"' order by id desc limit "+limit, null);

        while (cursor.moveToNext()) {

            String string = cursor.getString(8);
            string.replace("http://", "https://");
            String newString = string.replace("http://", "https://");
            String[] urlslist = newString.split("https://");
            String url = Verify("https://" + urlslist[1]);
            if (!url.equals("Other")){
                JsonMovies add = new JsonMovies(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                list.add(add);
            }
        }

        return list;

    }

    private String Verify(String youTubeUrl) {
        String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }else{
            return "Other";
        }
    }

    public List < JsonMovies > search(String keyword) {
        String words = keyword.toLowerCase();
        List < JsonMovies > list = new ArrayList < > ();
        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from " + table_movies + " where " + title_row + " like ?" + " or " + description_row + " like ?" + " or " + category_row + " like ?" + " or " + language_row + " like ?", new String[] {
                "%" + words + "%", "%" + words + "%", "%" + words + "%", "%" + words + "%"
        });

        while (cursor.moveToNext()) {
            JsonMovies add = new JsonMovies(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            list.add(add);
        }

        return list;

    }

    public List <JsonMovies> channel_movies(String channel) {
        List <JsonMovies> list = new ArrayList <> ();
        SQLiteDatabase db = getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from " + table_movies + " where " + channel_row + " like ?" , new String[] {
                "%" + channel + "%"
        });

        while (cursor.moveToNext()) {
            JsonMovies add = new JsonMovies(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            list.add(add);
        }

        return list;

    }
}