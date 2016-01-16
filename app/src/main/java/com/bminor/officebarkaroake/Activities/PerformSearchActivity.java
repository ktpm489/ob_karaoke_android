package com.bminor.officebarkaroake.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bminor.officebarkaroake.Messaging.QueryFetcher;
import com.bminor.officebarkaroake.R;
import com.bminor.officebarkaroake.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformSearchActivity extends AppCompatActivity {

    public final static String songTitle = "com.bminor.officebarkaraoke.MESSAGE";
    public final static String artistName = "com.bminor.officebarkaraoke.MESSAGE2";

    private final static String TAG = "PerformSearchActivity";

    ListView listView;
    private List<SongInfo> sItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_search);

        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.SEARCH_STRING);
        String type = intent.getStringExtra(MainActivity.SEARCH_TYPE);

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent requestIntent = new Intent( PerformSearchActivity.this, SongRequestActivity.class );
                //SongInfo selected = (SongInfo)parent.getItemAtPosition(position);

                SongInfo selected = sItems.get(position);
                Log.i(TAG, selected.toString() );

                requestIntent.putExtra(songTitle, selected.get_song() );
                requestIntent.putExtra(artistName, selected.get_artist() );
                startActivity( requestIntent );
            }
        });

        new PerformSearch( type, query ).execute();
    }

    private class PerformSearch extends AsyncTask<Void, Void, List<SongInfo>> {

        private String _query = "";
        private String _type = "all";

        public PerformSearch( String type, String query ){
            _query = query;
            _type = type;
        }

        @Override
        protected List<SongInfo> doInBackground(Void... params) {
            return new QueryFetcher().fetchResults( _type, _query );
        }

        @Override
        protected void onPostExecute(List<SongInfo> songs){
            sItems = songs;
            setupAdapter();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    private void setupAdapter(){

        List<Map<String, String>> songList = new ArrayList<Map<String, String>>();
        for( int i = 0; i < sItems.size(); i++ ){
            Map<String, String> data = new HashMap<String, String>(2);
            data.put("song", sItems.get(i).get_song() );
            data.put("artist", sItems.get(i).get_artist() );
            songList.add(data);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, songList,
                android.R.layout.simple_list_item_2,
                new String[] {"song","artist"},
                new int[] { android.R.id.text1, android.R.id.text2});

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }
}