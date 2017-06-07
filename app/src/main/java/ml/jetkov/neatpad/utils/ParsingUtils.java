package ml.jetkov.neatpad.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;

import static ml.jetkov.neatpad.utils.FileManager.readStringFromFile;
import static ml.jetkov.neatpad.utils.FileManager.writeStringToFile;

/**
 * Created by Alex on 2017-06-06.
 */

public class ParsingUtils {

    public static void markdownToHTML(File textFile, File htmlFile) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(readStringFromFile(textFile));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        writeStringToFile(renderer.render(document), htmlFile);
    }
}
