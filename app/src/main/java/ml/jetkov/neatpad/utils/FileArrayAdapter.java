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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ml.jetkov.neatpad.R;

public class FileArrayAdapter extends ArrayAdapter<File> {
    private final Context context;
    private final File[] files;

    public FileArrayAdapter(Context context, File[] files) {
        super(context, -1, files);
        this.context = context;
        this.files = files;
    }

    public File[] getFiles() {
        return files;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ListItemViewHolder itemViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.list_item_file, parent, false);
            itemViewHolder = new ListItemViewHolder(rowView);

            rowView.setTag(rowView);
        } else itemViewHolder = (ListItemViewHolder) convertView.getTag();

        if (files[position].isFile()) {
            itemViewHolder.fileIcon.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            itemViewHolder.fileIcon.setImageResource(R.mipmap.ic_launcher);
        }

        itemViewHolder.fileName.setText(files[position].getName());
        itemViewHolder.fileModDate.setText(String.valueOf(files[position].lastModified()));

        return rowView;
    }

    private class ListItemViewHolder {
        ImageView fileIcon;
        TextView fileName;
        TextView fileModDate;

        public ListItemViewHolder(View view) {
            fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            fileName = (TextView) view.findViewById(R.id.file_name);
            fileModDate = (TextView) view.findViewById(R.id.file_mod_date);
        }
    }
}