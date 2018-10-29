package at.ac.tuwien.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static at.ac.tuwien.commons.utils.FileUtils.getResourcePath;

public class Props {

    private static final String PATH = "config/app.properties";
    private static final Properties props;

    // static initializer expression
    // called when the class is loaded
    static {
        try( FileInputStream fis = new FileInputStream( getResourcePath( PATH ))) {
            props = new Properties();
            props.load( fis );
        } catch( IOException e ) {
            throw new RuntimeException( e.getMessage() );
        }
    }

    // class cannot be instantiated
    private Props() {}

    // static method to query the props
    public static String getProp( String key ) {
        return props.getProperty( key );
    }
}
