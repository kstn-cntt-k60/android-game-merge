package kstn.game.logic.cone;

public class ConeResult {
    public static final int _100 = 0;
    public static final int _200 = 1;
    public static final int _300 = 2;
    public static final int _400 = 3;
    public static final int _500 = 4;
    public static final int _600 = 5;
    public static final int _700 = 6;
    public static final int _800 = 7;
    public static final int _900 = 8;
    public static final int DIV_2 = 9;
    public static final int MUL_2 = 10;
    public static final int LOST_LIFE = 11;
    public static final int BONUS = 12;
    public static final int BONUS_LIFE = 13;
    public static final int LUCKY = 14;
    public static final int LOST_SCORE = 15;

    public static boolean isScore(int result) {
        if (result >= _100 && result <= _900)
            return true;
        else
            return false;
    }

    public static int getScore(int result) {
        assert (isScore(result));
        return (result - _100 + 1) * 100;
    }

    public static String getString(int result) {
        if (isScore(result)) {
            return Integer.toString(getScore(result));
        }

        switch (result) {
            case DIV_2:
                return "Chia 2";
            case MUL_2:
                return "Nhân 2";
            case LOST_LIFE:
                return "Mất Lượt";
            case BONUS:
                return "Thưởng";
            case BONUS_LIFE:
                return "Thêm Lượt";
            case LUCKY:
                return "May Mắn";
            case LOST_SCORE:
                return "Mất điểm";
            default:
                assert (false);
                return "";
        }
    }
}
