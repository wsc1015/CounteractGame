package kevinproject.counteractgame;


/**
 * Created by Kevin on 11/24/15.
 */
public class Slot{
    private int mark;
    private int status;


    public static final int COVERED = 1;
    public static final int UNCOVERED = 2;
    public static final int COUNTERACTED = 3;

    public static int KINDS = 5;



    public Slot(int mark) {
        this.mark = mark;
        this.status = COVERED;
    }

    public int getMark() {
        return mark;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Slot{" +
                "mark=" + mark +
                ", status=" + status +
                '}';
    }
}
