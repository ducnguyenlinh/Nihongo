package com.example.duclinh1610.nihongo.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "accountManager";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Accounts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_RE_PASSWORD = "re_password";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_accounts = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT," +
                "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_NAME,
                KEY_ADDRESS, KEY_EMAIL, KEY_MOBILE, KEY_PASSWORD, KEY_RE_PASSWORD);
        db.execSQL(create_accounts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_accounts = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_accounts);

        onCreate(db);
    }

    // Adding new Contact
    void addContact(Account contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_MOBILE, contact.getMobile());
        values.put(KEY_PASSWORD, contact.getPassword());
        values.put(KEY_RE_PASSWORD, contact.getRe_password());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Getting single contact
    private Account getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_MOBILE},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Account contact = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return contact;
    }

    //  Getting All Contact
    public ArrayList<Account> getAllContacts(){
        ArrayList<Account> contactList = new ArrayList<Account>();
        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                Account contact = new Account();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setAddress(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setMobile(cursor.getString(4));
                // adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contactList
        return contactList;
    }

    // Updating single contact
    public int updateContact(Account contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_MOBILE, contact.getMobile());
        values.put(KEY_PASSWORD, contact.getPassword());
        values.put(KEY_RE_PASSWORD, contact.getRe_password());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + "=?",
                new String[]{ String.valueOf(contact.getId())});
    }

    // Deleting single contact
    public void  deleteContact(Account contact){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?",
                new String[] { String.valueOf(contact.getId())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
