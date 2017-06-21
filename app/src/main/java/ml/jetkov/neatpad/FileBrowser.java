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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import ml.jetkov.neatpad.utils.FileArrayAdapter;
import ml.jetkov.neatpad.utils.FileManager;

public class FileBrowser extends AppCompatActivity {
    //private static final String LOG_TAG = "File Browser";
    private File currentDirectory = FileManager.getExternalAppDir("Text Files");

    private ListView fileList;
    private FileArrayAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNewFileDialog(currentDirectory);
            }
        });

        fileList = (ListView) findViewById(R.id.file_list);

        if (FileManager.isExtStorageWritePermGranted(this)) {
            generateDefaultHierarchy();
            updateList(currentDirectory.listFiles());
        }

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selectedFile = fileAdapter.getFiles()[position];
                if (selectedFile.isDirectory()) {
                    currentDirectory = selectedFile;
                    updateList(currentDirectory.listFiles());
                } else launchFileInterface(selectedFile.getAbsolutePath());
            }
        });

        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        generateDefaultHierarchy();
        updateList(FileManager.getExternalAppDir().listFiles());
    }

    @Override
    public void onBackPressed() {
        if (!currentDirectory.equals(FileManager.getExternalAppDir())) {
            File relativeParent = currentDirectory.getParentFile();
            if (updateList(relativeParent.listFiles())) currentDirectory = relativeParent;
            //Log.e(LOG_TAG, currentDirectory.getAbsolutePath());
        } else {
            super.onBackPressed();
        }
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

    private void newNewFileDialog(final File parentDirectory) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        dialogBuilder.setTitle("New File");
        dialogBuilder.setMessage("Enter the name of the new file: ");

        final EditText input = new EditText(this);
        input.setSingleLine(true);
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                if (!Objects.equals(input.getText().toString(), "")) {
                    File newFile = new File(parentDirectory, input.getText().toString() + ".txt");

                    try {
                        if (!newFile.createNewFile()) {
                            newOverwriteFileDialog(newFile);
                        }
                    } catch (IOException e) {
                        //Log.e(LOG_TAG, "Could not create new file: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                updateList(currentDirectory.listFiles());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                // cancelled
            }
        });

        dialog = dialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void newOverwriteFileDialog(final File fileToOverwrite) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Overwrite file '" + fileToOverwrite.getName() + "' ?");

        final TextView input = new TextView(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                fileToOverwrite.delete();
                try {
                    fileToOverwrite.createNewFile();
                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Could not create new file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
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
