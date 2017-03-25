package setia.example.com.watchkids.SQLHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by My Computer on 3/25/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "watchkids.db";
    private static final int DBVERSION = 1;
    public DbHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Menjalankan query
        db.execSQL(KidsController.CREATE_CONTACTS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DATABASE_ALTER adalah variable yang menampung query untuk alter table
        //seperti ContactController.CREATE_CONTACT
        //Kosongkan onUpgrade jika tidak ingin melakukan perubahan pada struktur table
        if (oldVersion < 2) {
            db.execSQL(KidsController.CREATE_CONTACTS);
            //db.execSQL(DATABASE_ALTER_1);
        } else if (oldVersion < 3) {
            //db.execSQL(DATABASE_ALTER_2);
        }
    }
}