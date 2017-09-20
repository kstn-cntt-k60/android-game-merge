package kstn.game.view.thang.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kstn.game.view.thang.model.CauHoiModel;

import java.util.ArrayList;

/**
 * Created by thang on 9/12/2017.
 */

public class QuestionManagerDAO {
    private QuestionManagerDatabase questionManagerDatabase;
    private SQLiteDatabase database;
    public QuestionManagerDAO(Context context){
        questionManagerDatabase = new QuestionManagerDatabase(context);
    }
    public void open() {
        database = questionManagerDatabase.getWritableDatabase();
    }
    public  void close() {
        database.close();
    }

    // xem( lay) du lieu
    public ArrayList<CauHoiModel> getData(){
        ArrayList<CauHoiModel> data = new ArrayList<>();
        String sql ="SELECT * FROM question";

        Cursor contro =  database.rawQuery(sql,null);
        contro.moveToFirst(); // con tro o hag dau tien
        while(!contro.isAfterLast()){
            // doc den khi nao het du lieu thi thoi
            int id = contro.getInt(contro.getColumnIndex("id"));
            String tencauhoi = contro.getString(contro.getColumnIndex("tencauhoi"));
            String cautraloi  = contro.getString(contro.getColumnIndex("cautraloi"));
            CauHoiModel sv = new CauHoiModel(id,tencauhoi,cautraloi);
            data.add(sv);
            contro.moveToNext();
        }

        return data;
    }
}
