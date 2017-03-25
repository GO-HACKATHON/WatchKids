package setia.example.com.watchkids.SQLHelper;

import android.content.Context;
import android.database.SQLException;

import java.util.List;

import setia.example.com.watchkids.Model.Kids;

/**
 * Created by My Computer on 3/25/2017.
 */

public class SQLManager {
    private static KidsController KC;

    public static KidsController getKC() {
        return KC;
    }

    public static void setKC(KidsController KC) {
        SQLManager.KC = KC;
    }

    public static void getReady(Context context){
        DbHelper dbHelper = new DbHelper(context);
        setKC(new KidsController(dbHelper));
    }

    public static void openKC() throws SQLException {
        KC.open();
    }

    public static void closeKC() {
        KC.close();
    }

    public static void deleteDataKC (){
        KC.deleteData();
    }

    public static void insertDataKC(List<Kids> kidsList) {
        for(int loop = 0; loop < kidsList.size(); loop++){
            KC.insertData(kidsList.get(loop));
        }
    }

    public static void updateDataKC(Kids kids) {
        KC.updateKids(Integer.valueOf(kids.getId()), kids.getLatitude(), kids.getLongitude(), kids.getUpdated());
    }

    public static void updateDataKC(List<Kids> kidsList) {
        for(int loop = 0; loop < kidsList.size(); loop++){
            updateDataKC(kidsList.get(loop));
        }
    }

    public static List<Kids> getDataKC() {
        return KC.getData();
    }

}
