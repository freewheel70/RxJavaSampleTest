package com.hong.app.freegank.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hong.app.freegank.R;
import com.hong.app.freegank.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class AvatarResultActivity extends AppCompatActivity {

    private static final String TAG = "AvatarResultActivity";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;
    private static final String AVATAR_IMAGE_PATH = "/FreeGank/Avatar/";
    ;;

    @Bind(R.id.image_view_preview)
    ImageView imagePreview;

    @Bind(R.id.save_button)
    Button saveButton;

    @Bind(R.id.cancel_button)
    Button cancelButton;

    private String avatarPath;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_result);
        ButterKnife.bind(this);

        imagePreview.setImageURI(getIntent().getData());

//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);
//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCroppedImage();
                Intent intent = new Intent();
                intent.putExtra(LoginActivity.EXTRA_CONFIRM_SAVE, true);
                intent.putExtra(LoginActivity.EXTRA_FILE_PATH, avatarPath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(LoginActivity.EXTRA_CONFIRM_SAVE, false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private void saveCroppedImage() {
        Uri imageUri = getIntent().getData();
        if (imageUri != null && imageUri.getScheme().equals("file")) {
            try {
                saveFile(imageUri);
            } catch (Exception e) {
                Toast.makeText(AvatarResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, imageUri.toString(), e);
            }
        } else {
            Toast.makeText(AvatarResultActivity.this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile(Uri croppedFileUri) throws Exception {

        avatarPath = Constants.AVATAR_PATH;
        Log.d(TAG, "saveFile: avatarPath " + avatarPath);
        File saveFile = new File(avatarPath);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);

        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_avatar)
                .setMessage(R.string.cancel_save_avatar_warning)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelButton.performClick();
                    }
                }).setNegativeButton(R.string.alert_cancel, null)
                .setCancelable(false)
                .show();
    }
}
