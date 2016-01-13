package com.bminor.officebarkaroake;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SongRequestActivity extends AppCompatActivity {

    private static final String TAG = "songRequestActivity";

    private static String artist = "";
    private static String song = "";
    private static String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_request);

        Intent intent = getIntent();
        artist = intent.getStringExtra(PerformSearchActivity.artistName);
        song = intent.getStringExtra(PerformSearchActivity.songTitle);

        TextView songField = (TextView)findViewById(R.id.selectedSong);
        TextView artistField = (TextView) findViewById(R.id.selectedArtist);

        songField.setText( song );
        artistField.setText( artist );
    }

    public void sendMessage( View v ){

        EditText nameField = (EditText)findViewById(R.id.nameField);
        name = nameField.getText().toString();

        name.trim();

        if( name.length() > 0 )
            new RequestSong( song , artist, name ).execute();
    }

    private class RequestSong extends AsyncTask<Void, Void, Boolean> {

        private String _song = "";
        private String _artist = "";
        private String _name = "";

        public RequestSong( String song, String artist, String name ){
            _song = song;
            _artist = artist;
            _name = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return new SubmitRequest().fetchResults( _song, _artist, _name );
        }

        @Override
        protected void onPostExecute( Boolean result){
            if(result){
                Log.i(TAG, "Received succcess" );
                alertUser(true);
            } else {
                Log.i(TAG, "Received bad result");
                alertUser(false);
            }
        }

        protected void alertUser( boolean success ){
            String title = "", message = "";

            if( success ){
                title = getString(R.string.success_title);
                message = getString(R.string.success_message);
            } else {
                title = getString(R.string.error_title);
                message = getString(R.string.error_message );
            }

            showAlert(title, message);
        }

        private void showAlert( String title, String message ){
            AlertDialog alertDialog = new AlertDialog.Builder( SongRequestActivity.this).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent homeIntent = new Intent( SongRequestActivity.this, MainActivity.class );
                    startActivity( homeIntent );
                }
            });

            alertDialog.show();
        }
    }
}