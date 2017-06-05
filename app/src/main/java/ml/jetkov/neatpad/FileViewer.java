package ml.jetkov.neatpad;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FileViewer extends AppCompatActivity implements TextEditor.OnFragmentInteractionListener, HTMLViewer.OnFragmentInteractionListener {

    private FragmentManager fragManager = getSupportFragmentManager();

    private TextEditor textEditor = new TextEditor();
    private HTMLViewer htmlViewer = new HTMLViewer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragManager.beginTransaction().add(R.id.frag_container, textEditor).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchViewMode();
            }
        });
    }

    protected void switchViewMode() {
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        if (findViewById(R.id.text_editor) == null) {
            fragTransaction.replace(R.id.frag_container, textEditor);
        } else {
            fragTransaction.replace(R.id.frag_container, htmlViewer);
        }
        fragTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
