package kevinproject.counteractgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class GameView extends SurfaceView implements View.OnTouchListener{
    private static int WIDTH = 30;
    private static int OFFSETY = 30;
    private static final int COLS = 6;
    private static final int ROWS = 6;
    private Slot[][] matrix;
    private ArrayList<Integer> content;
    private LinkedList<Slot> uncoveredSlot;
    private Thread timeCount;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(callback);
        setOnTouchListener(this);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(callback);
        setOnTouchListener(this);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(callback);
        setOnTouchListener(this);
        initGame();
    }

    public void initGame(){
        matrix = new Slot[ROWS][COLS];
        uncoveredSlot = new LinkedList<Slot>();
        Random random = new Random();
        content = new ArrayList<Integer>();
        for(int i = 0; i < ROWS * COLS / 2; i++){
            int key = random.nextInt(Slot.KINDS) + 1;
            content.add(key);
            content.add(key);
        }
        Collections.shuffle(content);
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                matrix[i][j] = new Slot(content.get(i * COLS + j));
            }
        }
    }

    public void redraw(){
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                Bitmap bm = null;
                switch(matrix[i][j].getStatus()){
                    case Slot.COVERED:
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.coveredpic);
                        break;
                    case Slot.UNCOVERED:
                        switch(matrix[i][j].getMark()){
                            case 1:
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
                                break;
                            case 2:
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);
                                break;
                            case 3:
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic3);
                                break;
                            case 4:
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic4);
                                break;
                            case 5:
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic5);
                                break;
                            default:
                                break;
                        }

                        break;
                    default:
                        break;
                }
                if(bm != null){
                    bm = Bitmap.createScaledBitmap(bm, WIDTH, WIDTH, true);
                    if(i % 2 == 0) {
                        canvas.drawBitmap(bm, 1.0f * j * WIDTH, 1.0f * i * WIDTH + OFFSETY, paint);
                    }else{
                        canvas.drawBitmap(bm, 1.0f * WIDTH + j * WIDTH, 1.0f * i * WIDTH + OFFSETY, paint);
                    }
                }
            }
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            int x, y;
            y = (int)((event.getY() - OFFSETY) / WIDTH);
            if(y % 2 == 0) {
                x = (int) (event.getX() / WIDTH);
            }else{
                x = (int) ((event.getX() - WIDTH) / WIDTH);
            }
            if(x >= 0 && x < COLS && y >= 0 && y < ROWS){
                if(matrix[y][x].getStatus() == Slot.COVERED){
                    matrix[y][x].setStatus(Slot.UNCOVERED);
                    uncoveredSlot.offer(matrix[y][x]);
                    change();
                    redraw();
                }
            }
        }
        return true;
    }

    private boolean change(){
        boolean counteractFlag = false;
        if(uncoveredSlot.size() == 2) {
            if (uncoveredSlot.getFirst().getMark() == uncoveredSlot.getLast().getMark()) {
                waitShow();
                while (!uncoveredSlot.isEmpty()) {
                    Slot counteract = uncoveredSlot.poll();
                    counteract.setStatus(Slot.COUNTERACTED);
                }
                counteractFlag = true;
            } else {
                waitShow();
                while (!uncoveredSlot.isEmpty()) {
                    Slot cover = uncoveredSlot.poll();
                    cover.setStatus(Slot.COVERED);
                }
            }
        }

        if(checkWin()){
            waitShow();
            new AlertDialog.Builder(getContext()).setTitle("Congratulations !!").setMessage("You Win !!").setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initGame();
                    redraw();
                }
            }).show();
        }
        return counteractFlag;
    }

    private void waitShow(){
        redraw();
        try{
            Thread.sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkWin(){
        int i = 0, j = 0;
        Loop:
        for(i = 0; i < ROWS; i++){
            for(j = 0; j < COLS; j++){
                if(matrix[i][j].getStatus() != Slot.COUNTERACTED){
                    break Loop;
                }
            }
        }
        if(j == COLS && i == ROWS){
            return true;
        }
        return false;
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback(){
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            WIDTH = width / (COLS + 1);
            OFFSETY = (height - ROWS * WIDTH) / 2;
            redraw();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
}
