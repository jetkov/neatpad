/*
 * Copyright (C) 2017  Alex Petkovic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ml.jetkov.neatpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import ml.jetkov.neatpad.utils.FileArrayAdapter;
import ml.jetkov.neatpad.utils.FileManager;

public class FileBrowser extends AppCompatActivity {
    private static final String LOG_TAG = "File Browser";

    private ListView fileList;
    private FileArrayAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getExternalAppFile(view.getContext(), "test");
//                Snackbar.make(view, "Test directory created!", Snackbar.LENGTH_LONG).show();
            }
        });

        fileList = (ListView) findViewById(R.id.file_list);

        if (FileManager.isExtStorageWritePermGranted(this)) {
            generateDefaultHierarchy();
            updateList(FileManager.getExternalAppDir().listFiles());
        }

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selectedFile = fileAdapter.getFiles()[position];
                if (selectedFile.isDirectory()) updateList(selectedFile.listFiles());
                else launchFileInterface(selectedFile.getAbsolutePath());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        generateDefaultHierarchy();
        updateList(FileManager.getExternalAppDir().listFiles());
    }

    private void generateDefaultHierarchy() {
        File dest = new File(FileManager.getExternalAppDir("Text Files"), "CommonmarkSpec.txt");
        FileManager.copyAsset(getAssets(), "samples/CommonmarkSpec.txt", dest);
    }

    private boolean updateList(File[] files) {
        if (fileList != null) {
            fileAdapter = new FileArrayAdapter(this, files);
            fileList.setAdapter(fileAdapter);
            return true;
        }
        return false;
    }

    private void newFileDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title");
        alert.setMessage("Message");

        final TextView input = new TextView(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                input.setText("hi");
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void launchFileInterface(String filePath) {
        Intent intent = new Intent(this, FileInterface.class);
        intent.putExtra("file_path", filePath);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_browser, menu);
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
}
