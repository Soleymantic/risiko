package at.ac.tuwien.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileUtils {

    // execute around pattern
    // demostrated on loading a file
    public static void processFileByLine( String absolutePath, FileProcessesor p ) {

        // Boilerplate code (BEFORE) ---
        try( BufferedReader br = Files.newBufferedReader( Paths.get( absolutePath ))) {
        // -----------------------------

            // the actual problem ---
            br.lines().forEach( line -> p.process( line ) );
            // ----------------------

        // Boilerplate code (AFTER) ---
        } catch ( IOException e ) {
            // convert checked into unchecked exception
            throw new RuntimeException( e.getMessage() );
        }
        // ----------------------------
    }


    public static void processFile( String absolutePath, Consumer<BufferedReader> callback ) {

        try( BufferedReader br = Files.newBufferedReader( Paths.get( absolutePath ))) {

            // call accept from Consumer interface
            callback.accept( br );

        } catch ( IOException e ) {
            throw new RuntimeException( e.getMessage() );
        }
    }


    public static String getResourcePath( String relativePath ) {
        try {
            // if getResourcePath cannot find the file it returns NULL
            URL resource = FileUtils.class.getClassLoader().getResource( relativePath );
            if ( resource != null )
                return new File( resource.toURI() ).getAbsolutePath();
            else
                throw new IllegalArgumentException( "bad resource location: " + relativePath );
        } catch ( URISyntaxException e ) {
            throw new RuntimeException( e.getMessage() );
        }
    }


    @FunctionalInterface
    // see Consumer Interface
    public interface FileProcessesor {
        void process(String line);
    }
}
