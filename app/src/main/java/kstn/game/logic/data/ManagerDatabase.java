package kstn.game.logic.data;

/**
 * Created by thang on 9/12/2017.
 */
import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class ManagerDatabase extends SQLiteAssetHelper {
    public ManagerDatabase(Context context) {
        super(context,"database.sqlite", null, 1);
    }
}
