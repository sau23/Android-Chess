package e.le09idas.androidchess23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button newGame = (Button) findViewById(R.id.button3);
        newGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gotToNewGame();
            }
        });

        Button replayGame = (Button) findViewById(R.id.button4);
        replayGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gotToReplayGame();
            }
        });
    }

    private void gotToNewGame(){
        Intent intent = new Intent(this, NewGame.class);
        startActivity(intent);
    }

    private void gotToReplayGame(){
        Intent intent = new Intent(this, RecordedGame.class);
        startActivity(intent);
    }
}
