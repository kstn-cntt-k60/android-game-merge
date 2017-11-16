package kstn.game.logic.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kstn.game.logic.model.QuestionModel;

/**
 * Created by thang on 9/12/2017.
 */

public class QuestionManagerDAO {
    private QuestionManagerDatabase questionManagerDatabase;
    private SQLiteDatabase database;
    private Random random = new Random();

    public QuestionManagerDAO(Context context){
        questionManagerDatabase = new QuestionManagerDatabase(context);
    }

    public void open() {
        database = questionManagerDatabase.getWritableDatabase();
    }

    public  void close() {
        database.close();
    }

    List<QuestionModel> questionModels = null;

    public QuestionModel getRandomQuestion() {
        if (questionModels == null)
            questionModels = getData();
        int index;
        do {
            index = random.nextInt(questionModels.size());
        } while (questionModels.get(index).isUsed());
        questionModels.get(index).use();
        return questionModels.get(index);
    }

    // xem( lay) du lieu
    private ArrayList<QuestionModel> getData(){
        ArrayList<QuestionModel> data = new ArrayList<>();
        String sql ="SELECT * FROM question";

        Cursor contro =  database.rawQuery(sql,null);
        contro.moveToFirst(); // con tro o hag dau tien
        while(!contro.isAfterLast()){
            // doc den khi nao het du lieu thi thoi
            int id = contro.getInt(contro.getColumnIndex("id"));
            String tencauhoi = contro.getString(contro.getColumnIndex("tencauhoi"));
            String cautraloi  = contro.getString(contro.getColumnIndex("cautraloi"));
            QuestionModel sv = new QuestionModel(id,tencauhoi,cautraloi);
            data.add(sv);
            contro.moveToNext();
        }

        return data;
    }
}
