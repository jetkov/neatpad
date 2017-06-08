package ml.jetkov.neatpad.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.File;

/**
 * Created by jetkov on 08/06/17.
 */

public class FileAdapter extends ArrayAdapter<File> {

    FileAdapter(Context context, File[] files) {
        super(context, -1, files);
    }


}
