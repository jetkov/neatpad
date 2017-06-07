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
