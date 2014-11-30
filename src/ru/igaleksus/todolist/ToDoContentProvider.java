package ru.igaleksus.todolist;

import android.content.*;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by mac on 25.11.14.
 */
public class ToDoContentProvider extends ContentProvider {

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("ru.igaleksus.todoprovider", "todoitems", ALLROWS);
        uriMatcher.addURI("ru.igaleksus.todoprovider", "todoitems/#", SINGLE_ROW);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://ru.igaleksus.todoprovider/todoitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
//    public static final String KEY_IS_DONE = "done";
    public static final String KEY_CREATION_DATE = "creation_date";

    private MySQLLiteOpenHelper myOpenHelper;

    @Override
    public boolean onCreate() {
        myOpenHelper = new MySQLLiteOpenHelper(getContext(), MySQLLiteOpenHelper.DATABASE_NAME,
                null, MySQLLiteOpenHelper.DATABASE_VERSION);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLLiteOpenHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default: break;
        }

        Cursor cursor = queryBuilder.query(db, strings, s, strings1, groupBy, having, s1);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case ALLROWS: return "vnd.android.cursor.dir/vnd.igaleksus.todos";
            case SINGLE_ROW: return "vnd.android.cursor.item/vnd.igaleksus.todos";
                default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id = db.insert(MySQLLiteOpenHelper.DATABASE_TABLE, nullColumnHack, contentValues);

        if (id > -1){
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        }
        else return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                s = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : "");
            default: break;
        }

        if (s == null)
            s = "1";

        int deleteCount = db.delete(MySQLLiteOpenHelper.DATABASE_TABLE, s, strings);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;


    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                s = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : "");

            default: break;
        }

        int updateCount = db.update(MySQLLiteOpenHelper.DATABASE_TABLE, contentValues, s, strings);

        return updateCount;
    }

    private static class MySQLLiteOpenHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "todoDatabase.db";
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_TABLE = "todoItemTable";

        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID +
        " integer primary key autoincrement, " + KEY_TASK + " text not null, " + KEY_CREATION_DATE + "long);";

        public MySQLLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public MySQLLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w("TaskDBAAdapter", "Upgrading from version " + i + " to " + i1 + ", which will destroy all old data");

            sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);

            onCreate(sqLiteDatabase);
        }
    }
}
