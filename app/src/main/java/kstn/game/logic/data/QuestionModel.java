package kstn.game.logic.data;

public class QuestionModel {
    int id;
    private String cauhoi;
    private boolean used = false;
    private String CauTraLoi;

    public QuestionModel(int id, String cauhoi, String cauTraLoi) {
        this.id = id;
        this.cauhoi = cauhoi;
        this.CauTraLoi = cauTraLoi;
    }

    public void use() {
        used = true;
    }

    public boolean isUsed() {
        return used;
    }

    public String getQuestion() {
        return cauhoi;
    }

    public String getAnswer() {
        return CauTraLoi;
    }

    public int getId() {
        return id;
    }

    public QuestionModel(String cauhoi, String cauTraLoi) {
        this.cauhoi = cauhoi;
        this.CauTraLoi = cauTraLoi;
    }
}
