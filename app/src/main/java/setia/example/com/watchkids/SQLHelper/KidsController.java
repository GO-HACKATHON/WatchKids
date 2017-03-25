package setia.example.com.watchkids.SQLHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import setia.example.com.watchkids.Model.Kids;

/**
 * Created by My Computer on 3/25/2017.
 */

public class KidsController {

    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public static final String TABLE_NAME = "kids";
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String UPDATED_AT = "updated_at";

    public static final String CREATE_CONTACTS = "CREATE TABLE "+TABLE_NAME+" "+
            "("+ID+" integer primary key, "+ FIRST_NAME +" TEXT, " + LAST_NAME +" TEXT, " + PHONE_NUMBER + " TEXT, " +
            LATITUDE +" TEXT, "+ LONGITUDE + " TEXT, "  + UPDATED_AT + " TEXT)";

    private String[] TABLE_COLUMNS = {ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER, LATITUDE, LONGITUDE, UPDATED_AT};

    public KidsController(DbHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteData (){
        database.delete(TABLE_NAME, null, null);
    }

    public void insertData(int id, String first_name, String last_name, String phone_number, String latitude, String longitude, int updated_at){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(FIRST_NAME, first_name);
        contentValues.put(LAST_NAME, last_name);
        contentValues.put(PHONE_NUMBER, phone_number);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(UPDATED_AT, updated_at);

        database.insert(TABLE_NAME, null, contentValues);
    }

    public void insertData(Kids kids){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, kids.getId());
        contentValues.put(FIRST_NAME, kids.getFirstName());
        contentValues.put(LAST_NAME, kids.getLastName());
        contentValues.put(PHONE_NUMBER, kids.getPhone());
        contentValues.put(LATITUDE, kids.getLatitude());
        contentValues.put(LONGITUDE, kids.getLongitude());
        contentValues.put(UPDATED_AT, kids.getUpdated());

        database.insert(TABLE_NAME, null, contentValues);
    }

    public void updateKids(int id, String latitude, String longitude, String updatedAt){
        ContentValues contentValues = new ContentValues();
        String temp = String.valueOf(id);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(UPDATED_AT, updatedAt);

        database.update(TABLE_NAME, contentValues, ID + "=?", new String[] {temp});
    }

    public void updateContact(Kids kids){
        ContentValues contentValues = new ContentValues();
        String id = String.valueOf(kids.getId());
        contentValues.put(ID, kids.getId());
        contentValues.put(FIRST_NAME, kids.getFirstName());
        contentValues.put(LAST_NAME, kids.getLastName());
        contentValues.put(PHONE_NUMBER, kids.getPhone());
        contentValues.put(LATITUDE, kids.getLatitude());
        contentValues.put(LONGITUDE, kids.getLongitude());
        contentValues.put(UPDATED_AT, kids.getUpdated());

        database.update(TABLE_NAME, contentValues, ID + "=?", new String[] {id});
    }

    public ArrayList<Kids> getData() {
        ArrayList<Kids> allData = new ArrayList<Kids>();
        Cursor cursor = null;

        cursor = database.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, ID + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allData.add(parseData(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return allData;
    }

    private Kids parseData(Cursor cursor){
        Kids curData = new Kids();

        curData.setId(String.valueOf(cursor.getInt(0)));
        curData.setFirstName(cursor.getString(1));
        curData.setLastName(cursor.getString(2));
        curData.setPhone(cursor.getString(3));
        curData.setLatitude(cursor.getString(4));
        curData.setLongitude(cursor.getString(5));
        curData.setUpdated(cursor.getString(6));

        return curData;
    }
}
