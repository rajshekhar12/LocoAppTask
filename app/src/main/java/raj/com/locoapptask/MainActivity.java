package raj.com.locoapptask;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    AppCompatButton button;
    String vidAddress;
    Toolbar toolbar;
    TextView toolbarTitle;

    private static final int PICK_VIDEO_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(AppCompatButton)findViewById(R.id.play);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Loco task");

//        vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
//        Uri vidUri = Uri.parse(vidAddress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
                intent.putExtra("uri",vidAddress);
                startActivity(intent);*/

               doStartStop();
            }
        });


    }

    public void doStartStop() {

            Intent pickVideo = new Intent(Intent.ACTION_PICK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                pickVideo.setTypeAndNormalize("video/*");
                startActivityForResult(pickVideo, PICK_VIDEO_REQUEST);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {
//            Log.d(TAG, "Got video " + data.getData());
            vidAddress = data.getData().toString();
            Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
            intent.putExtra("uri",vidAddress);
            startActivity(intent);
        }
    }
}
