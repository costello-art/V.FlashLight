package k.sviat.vflashlight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class FlashMain extends Activity {

    private Button mButtonScreen;
    private Button mButtonBoth;
    private Button mButtonFlash;

    private Camera mCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_main);

        mButtonBoth = (Button) findViewById(R.id.buttonEnableBoth);
        mButtonScreen = (Button) findViewById(R.id.buttonEnableScreen);
        mButtonFlash = (Button) findViewById(R.id.buttonEnableLed);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Your device has no flash.", Toast.LENGTH_SHORT).show();
            mButtonFlash.setEnabled(false);
        } else {
            Toast.makeText(this, "Your device has flash.", Toast.LENGTH_SHORT).show();
            mCamera = Camera.open();
        }

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

            @Override
            public void onClick(View v) {
                if (screenEnabled) {
                    turnOffScreen();
                } else {
                    turnOnScreen();
                }

                screenEnabled = !screenEnabled;
            }
        });
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

    private void turnOnScreen() {

    }

    private void turnOffScreen() {

    }

    private void turnOnBoth() {

    }

    private void turnOffBoth() {

    }
}
