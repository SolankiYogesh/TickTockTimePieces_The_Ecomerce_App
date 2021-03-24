package com.syd.ticktocktimepieces.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.syd.ticktocktimepieces.models.WatchModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler<List> extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABSE_NAME="TickTockTimePiecesDb";
    private static final String TABLE_CART="cart";
    private static final String KEY_ID="id";
    private static final String KEY_CATEGORY="category";
    private static final String KEY_NAME="name";
    private static final String KEY_COMPANY="Company";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_IMAGE="image";
    private static final String KEY_PUBLISHED_YEAR="published_year";
    private static final String KEY_PRICE="price";
    private static final String KEY_QUANTITY="quantity";


    public DatabaseHandler(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE "+ TABLE_CART + "(" + KEY_ID + " TEXT PRIMARY KEY, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_NAME + " TEXT, "
                + KEY_COMPANY + " TEXT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_IMAGE + " TEXT, "
                + KEY_PUBLISHED_YEAR+ " TEXT, "
                + KEY_PRICE + " TEXT, "
                + KEY_QUANTITY + " TEXT ) ";

        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CART);
        onCreate(db);
    }
    public void AddToCart(WatchModel watchModel){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID, watchModel.getId());
        contentValue.put(KEY_CATEGORY, watchModel.getCategory());
        contentValue.put(KEY_NAME, watchModel.getName());
        contentValue.put(KEY_COMPANY, watchModel.getCOmpany());
        contentValue.put(KEY_DESCRIPTION, watchModel.getDescription());
        contentValue.put(KEY_IMAGE, watchModel.getImage());
        contentValue.put(KEY_PUBLISHED_YEAR, watchModel.getPublishedYear());
        contentValue.put(KEY_PRICE, watchModel.getPrice());

        sqLiteDatabase.insert(TABLE_CART,null, contentValue);
        sqLiteDatabase.close();
    }
     public java.util.List<WatchModel> getcartitems(){
        String selectQuery = "SELECT * FROM " +TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        java.util.List<WatchModel> cartitems = new ArrayList<WatchModel>();

         Cursor cursor= sqLiteDatabase.rawQuery(selectQuery,null);

         if(cursor.moveToFirst()){
             do {
                 WatchModel watchModel = new WatchModel();
                 watchModel.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                 watchModel.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                 watchModel.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                 watchModel.setCOmpany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                 watchModel.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                 watchModel.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));
                 watchModel.setPublishedYear(cursor.getString(cursor.getColumnIndex(KEY_PUBLISHED_YEAR)));
                 watchModel.setPrice(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
                 cartitems.add(watchModel);
             }while (cursor.moveToNext());
         }
         cursor.close();
         return cartitems;
    }
    public  void deletecart(){
        String selectQuery = "DELETE FROM " +TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       sqLiteDatabase.execSQL(selectQuery);

    }    public void removeItem(String id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART, KEY_ID + "= ?", new String[]{id});
        sqLiteDatabase.close();
    }
}
