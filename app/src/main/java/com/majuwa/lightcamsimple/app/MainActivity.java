package com.majuwa.lightcamsimple.app;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {
    private static boolean statusLight=false;
    private Camera cam;
    private SurfaceHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView preview = (SurfaceView) findViewById(R.id.PREVIEW);
        this.holder = preview.getHolder();
        this.holder.addCallback(this);
        cam = Camera.open();
        try{
            cam.setPreviewDisplay(this.holder);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        try{
            cam.setPreviewDisplay(holder);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        cam.stopPreview();
        this.holder = null;
    }
    public void switchLight(View view){
        if(statusLight){
            Parameters p = cam.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.stopPreview();
            statusLight = false;
        }
        else{
            Parameters p = cam.getParameters();
            Log.v("test",p.getFlashMode());
            List<String> list = p.getSupportedFlashModes();

            if(list.contains(Camera.Parameters.FLASH_MODE_TORCH)){
                Log.v("test","Kamera");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                cam.setParameters(p);
                cam.startPreview();
                Log.v("test",cam.toString());
                cam.startPreview();
                statusLight = true;
            }

        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if(cam!=null){
            cam.release();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
