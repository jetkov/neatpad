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

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import java.io.File;

import ml.jetkov.neatpad.utils.FileManager;
import ml.jetkov.neatpad.utils.RenderingUtils;

public class FileInterface extends AppCompatActivity implements TextEditor.OnFragmentInteractionListener, HTMLViewer.OnFragmentInteractionListener {

    private final TextEditor textEditorFrag = new TextEditor();
    private final HTMLViewer htmlViewerFrag = new HTMLViewer();
    private final FragmentManager fragManager = getSupportFragmentManager();
    private EditText textEditor;
    private WebView htmlViewer;

    private File textFile = new File(FileManager.getExternalAppDir("Text Files"), "Note.txt");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_interface_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragManager.beginTransaction().add(R.id.frag_container, textEditorFrag).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchViewMode();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String filePath = getIntent().getExtras().getString("file_path");
        if (filePath != null) {
            textFile = new File(filePath);
        }
        loadTextFile(textFile);
    }

    private void switchViewMode() {
        updateElements();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        if (textEditor == null) {
            fragTransaction.replace(R.id.frag_container, textEditorFrag).commit();
            updateElements();
        } else if (htmlViewer == null) {
            File htmlFile = new File(FileManager.getExternalAppDir("HTML Files"), textFile.getName().replace(".txt", "") + ".html");
            String textInEditor = textEditor.getText().toString();
            String html;

            FileManager.writeStringToFile(textInEditor, textFile);
            html = RenderingUtils.markdownToHTML(textInEditor);
            html = RenderingUtils.injectMathJaxScripts(html);
            FileManager.writeStringToFile(html, htmlFile);

            fragTransaction.replace(R.id.frag_container, htmlViewerFrag).commit();
            updateElements();
            htmlViewer.loadUrl("file://" + htmlFile.getAbsolutePath());
        }
    }

    private void updateElements() {
        getSupportFragmentManager().executePendingTransactions();
        textEditor = (EditText) findViewById(R.id.text_editor);
        htmlViewer = (WebView) findViewById(R.id.html_viewer);
    }

    private void loadTextFile(File file) {
        updateElements();
        if (textEditor == null) switchViewMode();
        textEditor.setText(FileManager.readStringFromTextFile(file));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
