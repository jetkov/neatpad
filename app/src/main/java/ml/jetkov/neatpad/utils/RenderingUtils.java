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

public class RenderingUtils {

    /**
     * Parses a file containing Markdown and renders it to HTML. Saves the HTML to a specified
     * file.
     *
     * @param textFile The text file containing Markdown language
     * @param htmlFile The HTML file to write to (render)
     * @see org.commonmark
     */
    public static void markdownToHTML(File textFile, File htmlFile) {
        writeStringToFile(markdownToHTML(readStringFromTextFile(textFile)), htmlFile);
    }

    /**
     * Parses a file containing Markdown and renders it to HTML.
     *
     * @param markdown A String containing Markdown language
     * @see org.commonmark
     */
    public static String markdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * Injects the necessary HTML tags to include the MathJax configuration and javascript into
     * a given String.
     *
     * @param string The HTML string to prepend
     * @return The newly rendered HTML String with injected scripts.
     */
    public static String injectMathJaxScripts(String string) {
        String injection = "<script type='text/x-mathjax-config'>\n" +
                "   MathJax.Hub.Config({\n" +
                "       extensions: ['tex2jax.js'],\n" +
                "       jax: ['input/TeX','output/HTML-CSS'],\n" +
                "       tex2jax: {inlineMath: [['$$$','$$$'],['\\\\(','\\\\)']]}\n" +
                "   });\n" +
                "   MathJax.Hub.Queue(function () {\n" +
                "       document.getElementById(\"hide_page\").style.visibility = \"\";\n" +
                "   });" +
                "\n" +
                "</script><script type='text/javascript' src='file:///android_asset/mathjax/MathJax.js?config=TeX-AMS_HTML-full'></script> \n";

        StringBuilder builder = new StringBuilder(string);
        builder.insert(0, injection);

        return builder.toString();

    }

}
