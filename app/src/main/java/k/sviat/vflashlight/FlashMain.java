package k.sviat.vflashlight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class FlashMain extends Activity {

    private final String TAG = "FlashMain";

    private Button mButtonScreen;
    private Button mButtonBoth;
    private Button mButtonFlash;

    private Camera mCamera;
    private SeekBar mSeekBrightness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_main);

        mButtonBoth = (Button) findViewById(R.id.buttonEnableBoth);
        mButtonScreen = (Button) findViewById(R.id.buttonEnableScreen);
        mButtonFlash = (Button) findViewById(R.id.buttonEnableLed);
        mSeekBrightness = (SeekBar) findViewById(R.id.seekBarScreenBrightness);

        tryToInitCamera();

        mButtonFlash.setOnClickListener(new View.OnClickListener() {
            private boolean flashEnabled = false;

            @Override
            public void onClick(View v) {
                if (flashEnabled) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }

                flashEnabled = !flashEnabled;
            }
        });

        mButtonScreen.setOnClickListener(new View.OnClickListener() {
            private boolean screenEnabled = false;
            private float brightness;// = 0.3F;

            @Override
            public void onClick(View v) {
                if (screenEnabled) {
                    turnOffScreen(brightness);
                } else {
                    brightness = mSeekBrightness.getProgress();
                    if (brightness < 10) {
                        brightness = 0.1F;
                    } else {
                        brightness = brightness / 100.0F;
                    }

                    brightness = turnOnScreen(brightness);
                }

                screenEnabled = !screenEnabled;
            }
        });

        mButtonBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonFlash.performClick();
                mButtonScreen.performClick();
            }
        });

        mSeekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 10) progress = 10;
                turnOnScreen(progress / 100.0F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void tryToInitCamera() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Your device has no flash.", Toast.LENGTH_SHORT).show();
            mButtonFlash.setEnabled(false);
        } else {
            Toast.makeText(this, "Your device has flash.", Toast.LENGTH_SHORT).show();

           try {
               mCamera = Camera.open();
           }
           catch (Exception e) {
               Log.e(TAG, "Camera init failed", e);
           }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void turnOnFlash() {
        Camera.Parameters p = mCamera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        mCamera.setParameters(p);
        mCamera.startPreview();
    }

    private void turnOffFlash() {
        Camera.Parameters p = mCamera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        mCamera.setParameters(p);
        mCamera.stopPreview();
    }

    private float turnOnScreen(float brightness) {
        int brightnessMode = 0;

        try {
            brightnessMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        float prevBrightness = layoutParams.screenBrightness;

        layoutParams.screenBrightness = brightness;
        getWindow().setAttributes(layoutParams);

        return prevBrightness;
    }

    private void turnOffScreen(float previousBrightness) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = previousBrightness;
        getWindow().setAttributes(layoutParams);
    }
}