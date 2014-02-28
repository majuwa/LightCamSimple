package com.majuwa.lightcamsimple.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;

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
        this.initialzie();
    }
    public void initialzie(){
        Button test = (Button)findViewById(R.id.button);
        test.setText("ON");
        test.setBackgroundColor(Color.GREEN);
        SurfaceView preview = (SurfaceView) findViewById(R.id.PREVIEW);
        cam = Camera.open();
        this.holder = preview.getHolder();
        this.holder.addCallback(this);
        statusLight=false;
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
            if(cam!=null)
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
            Button test = (Button)findViewById(R.id.button);
            test.setText("ON");
            test.setBackgroundColor(Color.GREEN);
        }
        else{
            Parameters p = cam.getParameters();
            List<String> list = p.getSupportedFlashModes();

            if(list.contains(Camera.Parameters.FLASH_MODE_TORCH)){
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                cam.setParameters(p);
                cam.startPreview();
                cam.startPreview();
                statusLight = true;
                Button test = (Button)findViewById(R.id.button);
                test.setText("OFF");
                test.setBackgroundColor(Color.RED);
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(cam==null){
            this.initialzie();
        }
    }
        @Override
    public void onStop(){
        super.onStop();
        if(cam!=null){
            cam.release();
            cam = null;
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
        switch (id){
            case R.id.about_menu:
                String licence = "This program is free software: you can redistribute it and/or modify \n"
                        + "it under the terms of the GNU General Public License as published by \n"
                        + "the Free Software Foundation, either version 3 of the License. \n"
                        + "\n"
                        + "This program is distributed in the hope that it will be useful, \n"
                        + "but WITHOUT ANY WARRANTY; without even the implied warranty of \n"
                        + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the \n"
                        + "GNU General Public License for more details. \n"
                        + " \n"
                        + "You should have received a copy of the GNU General Public License\n"
                        + "along with this program.  If not, see http://www.gnu.org/licenses/.\n";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(this.getResources().getString(R.string.app_name) + "\n(C) 2014 majuwa\n" + licence)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setTitle(this.getResources().getString(R.string.action_settings)).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
