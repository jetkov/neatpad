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
import ml.jetkov.neatpad.utils.ParsingUtils;

public class FileInterface extends AppCompatActivity implements TextEditor.OnFragmentInteractionListener, HTMLViewer.OnFragmentInteractionListener {

    private FragmentManager fragManager = getSupportFragmentManager();

    private TextEditor textEditorFrag = new TextEditor();
    private HTMLViewer htmlViewerFrag = new HTMLViewer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_interface);
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

    private void switchViewMode() {
        EditText textEditor = (EditText) findViewById(R.id.text_editor);
        WebView htmlViewer = (WebView) findViewById(R.id.html_viewer);

        File textFile = new File(FileManager.getExternalAppDir("Text Files"), "Test Note.txt");
        File htmlFile = new File(FileManager.getExternalAppDir("HTML Files"), "Test Note.html");

        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        if (textEditor == null) {
            fragTransaction.replace(R.id.frag_container, textEditorFrag).commit();
        } else if (htmlViewer == null) {
            FileManager.writeStringToFile(textEditor.getText().toString(), textFile);
            ParsingUtils.markdownToHTML(textFile, htmlFile);
            fragTransaction.replace(R.id.frag_container, htmlViewerFrag).commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
