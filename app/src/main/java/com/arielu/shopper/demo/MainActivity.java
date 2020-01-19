package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {


    private VideoView videoBG ;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hook up the VideoView to our UI
        videoBG = (VideoView) findViewById(R.id.videoView) ;

        //Build your video Uri
        Uri uri = Uri.parse("android.resource://com.arielu.shopper.demo/"  + R.raw.supermarket);


        //set the new Uri to our VideView
        videoBG.setVideoURI(uri);

        //Start the videoView
        videoBG.start();
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer ;
                //We want our video to play over and over so we set our looping to true
                mMediaPlayer.setLooping(true);
                //We seek to the current position if it has been set and play the video
                if(mCurrentVideoPosition != 0){
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }

            }
        });
    }

    public void exitClick(View view)
    {
        System.exit(0);
    }
    public void continueClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


    }


@Override
protected void onPause() {
    super.onPause();
    mCurrentVideoPosition = mMediaPlayer.getCurrentPosition() ;
    videoBG.pause();
}

//@Override
//protected void onResume() {
//    super.onResume();
//    videoBG.start();
//}

@Override
protected void onDestroy() {

    super.onDestroy();
    mMediaPlayer.release();
    mMediaPlayer = null;

}




}