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

package ml.jetkov.neatpad.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jetkov on 08/06/17.
 */
public class FileListAdapter extends ArrayAdapter<File> {

    private Context context;
    private ArrayList<File> fileList;
    private LayoutInflater layoutInflater;

    FileListAdapter(Context context, File[] files) {
        super(context, -1, files);
    }


}
