package kstn.game.view.thang.data;

/**
 * Created by thang on 9/12/2017.
 */
import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class QuestionManagerDatabase extends SQLiteAssetHelper {
    public QuestionManagerDatabase(Context context) {
        super(context, "CauHoiDataBase1.sqlite", null, 1);
    }
}
