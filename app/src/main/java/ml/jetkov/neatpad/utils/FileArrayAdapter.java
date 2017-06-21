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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ml.jetkov.neatpad.R;

/**
 * Returns a list item View for each file in an array of files, allowing the files in a given array
 * to be displayed as a list.
 */
public class FileArrayAdapter extends ArrayAdapter<File> {
    private final Context context;
    private final File[] files;

    /**
     * Creates a new FileArrayAdapter
     *
     * @param context The current context
     * @param files   The array of files to scan and return views for each element
     */
    public FileArrayAdapter(Context context, File[] files) {
        super(context, -1, files);
        this.context = context;
        this.files = files;
    }

    /**
     * @return The array of files
     */
    public File[] getFiles() {
        return files;
    }

    /**
     * Generates a View for the the file list item for the file at the given position.
     * @param position The file position in the list/array.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The list item View generated for the file at the given list/array position.
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ListItemViewHolder itemViewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.file_browser_list_item, parent, false);
            itemViewHolder = new ListItemViewHolder(rowView);

            rowView.setTag(rowView);
        } else if (convertView.getTag() instanceof ListItemViewHolder) {
            itemViewHolder = (ListItemViewHolder) convertView.getTag();
        }

        if (itemViewHolder != null) {

            if (files[position].isFile()) {
                itemViewHolder.fileIcon.setImageResource(R.drawable.ic_file);
            } else {
                itemViewHolder.fileIcon.setImageResource(R.drawable.ic_folder);
            }

            itemViewHolder.fileName.setText(files[position].getName());

            String items = String.valueOf(files[position].listFiles() != null ? files[position].listFiles().length + " files" : "File");
            itemViewHolder.fileInfo.setText(items);

            return rowView;
        }

        return convertView;
    }

    /**
     * A view holder for the each file list item. Holds the file icon, file name, and file
     * information views found with the IDs file_icon, file_name, and file_info.
     * The specific layout these views can be found in is file_browser_list_item.xml
     */
    private class ListItemViewHolder {
        final ImageView fileIcon;
        final TextView fileName;
        final TextView fileInfo;

        public ListItemViewHolder(View view) {
            fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            fileName = (TextView) view.findViewById(R.id.file_name);
            fileInfo = (TextView) view.findViewById(R.id.file_info);
        }
    }
}