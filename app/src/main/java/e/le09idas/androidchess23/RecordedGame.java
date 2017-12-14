package e.le09idas.androidchess23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;

import e.le09idas.androidchess23.chess.Replay;
import e.le09idas.androidchess23.chess.ReplayList;
import e.le09idas.androidchess23.chess.SortByDate;
import e.le09idas.androidchess23.chess.SortByName;

public class RecordedGame extends AppCompatActivity {

    private ListView records;

    private ArrayAdapter<Replay> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_replay);
        ReplayList.readFromData();
        records = (ListView)findViewById(R.id.records);
        adapter = new ArrayAdapter<Replay>(this, android.R.layout.simple_expandable_list_item_1, ReplayList.getReplayList());
        records.setAdapter(adapter);

        records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Replay selected = (Replay) adapterView.getItemAtPosition(i);
                ReplayList.setSelected(selected);
                showGame();
            }
        });

        Button backToMain = (Button)findViewById(R.id.backToMain);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        Button sortDate = (Button)findViewById(R.id.sortDate);
        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sort(new SortByDate());
                adapter.notifyDataSetChanged();
                records.setAdapter(adapter);
            }
        });

        Button sortName = (Button)findViewById(R.id.sortName);
        sortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sort(new SortByName());
                adapter.notifyDataSetChanged();
                records.setAdapter(adapter);
            }
        });

    }

    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void showGame(){
        Intent rep = new Intent(this, Game.class);
        finish();
        startActivity(rep);
    }
}
