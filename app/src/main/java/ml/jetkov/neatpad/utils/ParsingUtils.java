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

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;

import static ml.jetkov.neatpad.utils.FileManager.readStringFromTextFile;
import static ml.jetkov.neatpad.utils.FileManager.writeStringToFile;

/**
 * A set of static utility methods for parsing text files.
 * <p>
 * Created by jetkov (Alex Petkovic) on 2017-06-06.
 */

public class ParsingUtils {

    /**
     * Parses a file containing Markdown and renders it to HTML. Saves the HTML to a specified
     * file.
     *
     * @param textFile The text file containing Markdown language
     * @param htmlFile The HTML file to write to (render)
     * @see org.commonmark
     */
    public static void markdownToHTML(File textFile, File htmlFile) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(readStringFromTextFile(textFile));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        writeStringToFile(renderer.render(document), htmlFile);
    }
}
