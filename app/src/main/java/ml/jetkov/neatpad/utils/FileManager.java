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

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A set of utility methods for the management of files and directories on the android system,
 * with reference to the app.
 * <p>
 * Created by jetkov (Alex Petkovic) on 05/06/17.
 */

public class FileManager {

    public static final String appFolderName = "NeatPad";
    private static final String LOG_TAG = "File Manager";

    /**
     * @return True if 'external' storage is available for read and write.
     */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e("File Manager", "External Storage is not writable.");
        return false;
    }

    /**
     * @return True if 'external' storage is available to at least read.
     */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        Log.e("File Manager", "External Storage is not readable.");
        return false;
    }

    /**
     * Gets the 'external' app folder. Usually this is actually on internal
     * storage, but is entirely accessible by the user and other apps. Creates a 'NeatPad' folder
     * if it does not already exist.
     */
    public static File getExternalAppDir() {
        File file = Environment.getExternalStoragePublicDirectory(appFolderName);

        if (!isExternalStorageWritable() || !file.mkdirs()) {
            Log.i(LOG_TAG, "Directory " + file.getName() + "not created");
        }

        return file;
    }

    /**
     * Gets a subdirectory of the 'external' app folder. Usually this is actually on internal
     * storage, but is entirely accessible by the user and other apps. Creates a 'NeatPad' folder
     * and subdirectory with specified name.
     *
     * @param dirName The name of the subdirectory to create/return
     */
    public static File getExternalAppDir(String dirName) {
        File file = new File(getExternalAppDir(), dirName);

        if (!isExternalStorageWritable() || !file.mkdirs()) {
            Log.i(LOG_TAG, "Directory " + file.getName() + "not created");
        }

        return file;
    }

    /**
     * Checks if 'external' storage write permissions are granted. If they are not, requests
     * permission from user. On SDK versions < 23, this permission is automatically granted
     * upon installation.
     *
     * @param activity The activity to request permission from.
     * @return Whether or not 'external' storage write permission was granted.
     */
    public static boolean isExtStorageWritePermGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "External write permission is granted");
                return true;
            } else {

                Log.v(LOG_TAG, "External write permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { // permission is automatically granted on SDK < 23 upon installation
            Log.v(LOG_TAG, "External write permission is granted");
            return true;
        }
    }

    /**
     * Writes a String to a File. If the file exists, it will be overwritten.
     *
     * @param string The string to be written to the file
     * @param file   A file to write to
     * @return True if the write is a success, otherwise false.
     */
    public static boolean writeStringToFile(String string, File file) {
        try {
            if (file.delete())
                Log.i(LOG_TAG, "File" + file.getName() + "exists. Deleted to overwrite.");
            if (file.createNewFile()) Log.i(LOG_TAG, "New file" + file.getName() + " created.");
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(string);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            return true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Writing String to file failed: " + e.toString());
            return false;
        }
    }

    /**
     * Attempts to read a text file into a String.
     *
     * @param textFile The text file to read
     * @return A String containing the contents of the read text file
     */
    public static String readStringFromTextFile(File textFile) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Reading String from file failed: " + e.getMessage());
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * Copies a file using the abstract file path File object.
     *
     * @param src    Source File
     * @param target Target File
     * @return True if the file has successfully been copied.
     */
    public static boolean copyFile(File src, File target) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(target);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to copy file " + src.getPath() + " to " + target.getPath());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Copies a file using the given input and output file streams.
     *
     * @param in  The source InputStream
     * @param out The destination OutputStream
     * @throws IOException On an IO error
     */
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Copies an asset from the app's internal 'assets' directory to a specified destination.
     * @param assetManager The application's AssetManager.
     * @param fromAssetPath The path of the asset to copy (ex. "samples/CommonmarkSpec.txt").
     * @param destination The destination for the asset, specified by the File object.
     * @return True if the asset was successfully copied.
     */
    public static boolean copyAsset(AssetManager assetManager, String fromAssetPath, File destination) {
        InputStream in;
        OutputStream out;

        try {
            in = assetManager.open(fromAssetPath);
            destination.delete();
            destination.createNewFile();
            out = new FileOutputStream(destination);

            copyFile(in, out);

            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not copy asset: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
