package kevinproject.counteractgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainPage extends AppCompatActivity {


    private Timer timeManager;
    private TimerTask timeCount;
    private int tenMSecs;
    private boolean justStartFlag;
    private int mode;
    private String showTime;

    private static final int CLASSIC_MODE = 1;
    private static final int ZEN_MODE = 2;


    private GameView gameView;
    private TextView showTimeTV;

    private static MainPage mainPage;

    public MainPage() {
        this.mainPage = this;
    }


    public String getShowTime() {
        return showTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        gameView = (GameView) findViewById(R.id.gameView);
        showTimeTV = (TextView) findViewById(R.id.showTimeTV);
        mode = CLASSIC_MODE;
        if(mode == CLASSIC_MODE){
            showTime = "0.00";
        }
    }

    public static MainPage getMainPage(){
        return mainPage;
    }
}
