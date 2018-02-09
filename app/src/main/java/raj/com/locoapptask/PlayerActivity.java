package raj.com.locoapptask;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1001;
    private static final String TAG = "SurfaceSwitch";
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mFirstSurface;
    private SurfaceHolder mSecondSurface;
    //private CircleSurface mSecondSurface;
    private SurfaceHolder mActiveSurface;
    private Uri mVideoUri;
    CircleSurface second;
    SurfaceView first;
    int i=0;

    ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

       // String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
       // mVideoUri = Uri.parse(vidAddress);

        Intent in=getIntent();
        Bundle bn=in.getExtras();
        String uri=bn.getString("uri");
        mVideoUri=Uri.parse(uri);

        first = (SurfaceView) findViewById(R.id.firstSurface);
        first.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "First surface created!");

                mFirstSurface = surfaceHolder;

                if (mVideoUri != null && i==0) {
                    mMediaPlayer = MediaPlayer.create(PlayerActivity.this,
                            mVideoUri, mFirstSurface);
                    mActiveSurface = mFirstSurface;
                    mMediaPlayer.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "First surface destroyed!");
            }
        });

        second = (CircleSurface) findViewById(R.id.secondSurface);
        second.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "Second surface created!");
                mSecondSurface = surfaceHolder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "Second surface destroyed!");
            }
        });
        second.setFitsSystemWindows(true);


        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        /*scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                PlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        },0,5, TimeUnit.SECONDS);*/

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                PlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i == 0) {
                            Log.d("surfaceDetails", "first");
                            doSwitchSurface();
                            second.setVisibility(View.VISIBLE);
                            first.setVisibility(View.INVISIBLE);
                            i++;
                        } else if (i == 1) {
                            Log.d("surfaceDetails", "second");

                            first.setVisibility(View.VISIBLE);
                            second.setVisibility(View.INVISIBLE);


//                          Toast.makeText(MainActivity.this, "Times-up Again", Toast.LENGTH_SHORT).show();
                            doSwitchSurface();
                            i++;


                        }

                    }
                });

            }},
                10, 10,
                TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoUri = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {
            Log.d(TAG, "Got video " + data.getData());
            mVideoUri = data.getData();
        }
    }

    public void doStartStop(View view) {
        if (mMediaPlayer == null) {
            Intent pickVideo = new Intent(Intent.ACTION_PICK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                pickVideo.setTypeAndNormalize("video/*");
                startActivityForResult(pickVideo, PICK_VIDEO_REQUEST);
            }

        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void doSwitchSurface() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mActiveSurface = mFirstSurface == mActiveSurface ? mSecondSurface : mFirstSurface;
            mMediaPlayer.setDisplay(mActiveSurface);
        }
    }
}
