package be.ehb.democamera2;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Uri fotoUri;
    private ImageView fotoIv;
    private final int REQUEST_CODE = 100;

    private View.OnClickListener fotoClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            //fotoUri = Uri.parse(getFotoFile().getPath());
            //sinds Android 24 moet uri naar filesystem via provider
            //https://developer.android.com/reference/android/support/v4/content/FileProvider.html
            fotoUri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID + ".provider", getFotoFile());

            mIntent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);

            startActivityForResult(mIntent,REQUEST_CODE);


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Picasso.with(this).load(fotoUri).into(fotoIv);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoIv = findViewById(R.id.iv_foto);
        fotoIv.setOnClickListener(fotoClickedListener);
    }

    private File getFotoFile(){
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmmss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        File fotoPath = new File(storageDir.getPath() + File.separator + "img" + timestamp + ".jpeg");
        Log.e("Path", fotoPath.getPath());

        return fotoPath;
    }
}
