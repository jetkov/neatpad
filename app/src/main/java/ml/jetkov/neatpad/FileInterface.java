package ml.jetkov.neatpad;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import static ml.jetkov.neatpad.utils.FileManager.getExternalAppDir;
import static ml.jetkov.neatpad.utils.FileManager.writeStringToFile;

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

    protected void switchViewMode() {
        EditText textEditor = (EditText) findViewById(R.id.text_editor);

        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        if (textEditor == null) {
            fragTransaction.replace(R.id.frag_container, textEditorFrag);
        } else if (findViewById(R.id.html_viewer) == null){
            writeStringToFile(textEditor.getText().toString(), new File(getExternalAppDir("Text Files"), "Test Note.txt"));
            fragTransaction.replace(R.id.frag_container, htmlViewerFrag);
        }
        fragTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
