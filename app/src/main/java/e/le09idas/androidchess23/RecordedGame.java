package e.le09idas.androidchess23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import e.le09idas.androidchess23.chess.Replay;
import e.le09idas.androidchess23.chess.ReplayList;
import e.le09idas.androidchess23.NewGame;


public class RecordedGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_replay);

        ReplayList replays = new ReplayList();
        try {
            replays = importList();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error IO Exception", Toast.LENGTH_SHORT).show();;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error class not found", Toast.LENGTH_SHORT).show();

        }
        if(replays.getReplayList().size() == 0){
            Toast.makeText(this, "No replays in ReplayList", Toast.LENGTH_SHORT).show();;
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

    public ReplayList importList() throws IOException, ClassNotFoundException {

        File file = getFilesDir();
        ReplayList replays = new ReplayList();
        if (!(file.exists() && file.isDirectory())) {
            file.mkdir();
            return replays;
        }
        try {
            FileInputStream fin = openFileInput("replays.ser");
            ObjectInputStream in = new ObjectInputStream(fin);
            replays = (ReplayList)in.readObject();
            in.close();
            fin.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Successfully read from" + file.toString()+ "/replays.ser", Toast.LENGTH_SHORT).show();
        return replays;

    }

    public void exportList(ReplayList replays, Replay replay) {
        File file = getFilesDir();
        try {
            FileOutputStream fout = openFileOutput("replays.ser", MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(replays);
            out.close();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Successfully saved to" + file.toString()+ "/replays.ser", Toast.LENGTH_SHORT).show();
    }
}
