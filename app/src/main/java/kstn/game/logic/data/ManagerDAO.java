package kstn.game.logic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kstn.game.logic.state.multiplayer.ScoreDb;

/**
 * Created by thang on 9/12/2017.
 */

public class ManagerDAO {
    private ManagerDatabase questionManagerDatabase;
    private SQLiteDatabase database;
    private Random random = new Random();

    public ManagerDAO(Context context){
        questionManagerDatabase = new ManagerDatabase(context);
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
    public boolean SaveScore(String name, int score){
        ContentValues tmp= new ContentValues(); // bien dua du lie vao trong database
        tmp.put("NAME",name);
        tmp.put("SCORE",score);
        long check= database.insert("RANK",null,tmp);
        if(check!=0){
            return true;
        }
        return false;

    }
    // xem( lay) du lieu
    private ArrayList<ScoreDb> getListScore(){
        ArrayList<ScoreDb> data = new ArrayList<>();
        String sql ="SELECT * FROM RANK";
        Cursor contro =  database.rawQuery(sql,null);
        contro.moveToFirst(); // con tro o hag dau tien
        while(!contro.isAfterLast()){
            // doc den khi nao het du lieu thi thoi
            int id = contro.getInt(contro.getColumnIndex("ID"));
            String name = contro.getString(contro.getColumnIndex("NAME"));
            int score  = contro.getInt(contro.getColumnIndex("SCORE"));
            ScoreDb s = new ScoreDb(id,name,score);
            data.add(s);
            contro.moveToNext();
        }
        return data;
    }

    public ArrayList<ScoreDb> getRanking(){
        ArrayList<ScoreDb> data = getListScore();
        for(int i=0;i<data.size()-1;i++){
            for(int j=i+1;j<data.size();j++){
                if(data.get(i).getScore()<data.get(j).getScore()){
                    ScoreDb temp = data.get(i);
                    data.set(i,data.get(j));
                    data.set(j,temp);
                }
            }
        }
        return data;
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
