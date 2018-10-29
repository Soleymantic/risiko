package at.ac.tuwien.commons;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXView {

    public static void load( String path, Object controller ) {
        try {
            FXMLLoader loader = new FXMLLoader( FXView.class.getClassLoader().getResource( path ));
            loader.setController( controller );
            loader.load(); // the fxml view injects @FXML dependencies into the controller
        } catch ( IOException e ) {
            throw new RuntimeException( e.getMessage() );
        }
    }
}
