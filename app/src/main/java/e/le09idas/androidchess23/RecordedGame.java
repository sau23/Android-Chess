package e.le09idas.androidchess23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import e.le09idas.androidchess23.chess.Replay;
import e.le09idas.androidchess23.chess.ReplayList;


public class RecordedGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_replay);

        ReplayList replays = new ReplayList();
        try {
            replays.importList();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error");

        }
        if(replays.getReplayList().size() == 0){
            System.out.print("empty list");
        }
        String [] list = replays.stringList();
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);

        ListView records = (ListView) findViewById(R.id.records);
        records.setAdapter(adapter);

        records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(RecordedGame.this, selected.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
