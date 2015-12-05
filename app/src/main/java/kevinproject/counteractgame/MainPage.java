package kevinproject.counteractgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainPage extends AppCompatActivity {


    private Timer timeManager;
    private TimerTask timeCount;
    private TimerTask showTimeTask;
    private int tenMSecs;
    private int mode;
    private String showTime;
    private android.os.Handler handler;

    public static final int CLASSIC_MODE = 1;
    public static final int ZEN_MODE = 2;

    private static final int MSG_REFRESH_TIME = 1;

    private TextView showTimeTV;

    private static MainPage mainPage;

    public int getMode() {
        return mode;
    }

    public MainPage() {
        this.mainPage = this;
    }


    public String getShowTime() {
        return showTime;
    }

    public void updateShowTime() {
        showTime = String.format("%d", tenMSecs / 100) + "." + String.format("%02d", tenMSecs % 100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        showTimeTV = (TextView) findViewById(R.id.showTimeTV);
        timeManager = new Timer();
        handler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_REFRESH_TIME:
                        showTimeTV.setText(showTime);
                        break;
                    default:
                        break;
                }
            }
        };
        mode = CLASSIC_MODE;
        initGame();
    }

    public void initGame(){
        resetTenMSec();
        updateShowTime();
        showTimeTV.setText(showTime);
    }
    public void startGame(){
        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                updateShowTime();
                handler.sendEmptyMessage(MSG_REFRESH_TIME);
            }
        };
        timeCount = new TimerTask() {
            @Override
            public void run() {
                switch (mode){
                    case CLASSIC_MODE:
                        tenMSecs++;
                        break;
                    case ZEN_MODE:
                        tenMSecs--;
                        break;
                    default:
                        break;
                }
            }
        };
        resetTenMSec();
        updateShowTime();
        showTimeTV.setText(showTime);
        timeManager.schedule(timeCount, 10, 10);
        timeManager.schedule(showTimeTask, 110, 110);
    }

    public void endGame(){
        timeCount.cancel();
        timeCount = null;
        showTimeTask.cancel();
        showTimeTask = null;
    }

    public void resetTenMSec(){
        switch (mode){
            case ZEN_MODE:
                tenMSecs = 6000;
                break;
            case CLASSIC_MODE:
                tenMSecs = 0;
                break;
            default:
                break;
        }
    }

    public static MainPage getMainPage(){
        return mainPage;
    }
}
