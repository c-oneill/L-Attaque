package stratego;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * This class serves as the Stage for the new game menu dialog box.
 * 
 * <p>This class presents options for the user to select whether to create a game
 * as a Server or Client, as well as enter the Server and Port number to connect to.</p>
 * 
 * <p>User selections can be retrieved via accessor methods included.</p>
 * 
 * @author Kristopher Rangel
 *
 */
public class StrategoNewGameMenu extends Stage{

    
    private final int PADDING = 10; 
    private final String DEFAULT_SERVER = "localhost";
    private final String DEFAULT_PORT = "4000";
    
    private Scene scene;
    private RadioButton serverRB;
    private RadioButton clientRB;
    private TextField serverTF;
    private TextField portTF;
    private boolean hitOK;
    
    /**
     * <ul><b><i>Connect4NetworkSetup</i></b></ul>
     * <ul><ul><p><code>public Connect4NetworkSetup ()</code></p></ul>
     * 
     * Constructor.
     * 
     * @author Kristopher Rangel
     */
    public StrategoNewGameMenu(){
        this.hitOK = false;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("New Game Options");
        this.setResizable(false);
        setScene();
    }
        
    /**
     * <ul><b><i>getCreateToggleRow</i></b></ul>
     * <ul><ul><p><code>private HBox getCreateToggleRow () </code></p></ul>
     *
     * Sets up the 'Create:' row of the Network Setup options.
     * 
     * <p>These elements are added to in an HBox for uniformity of display. The elements
     * include a Label and two toggle options: Server and Client.</p>
     *
     * @return - the <code>HBox</code> containing the elements of the 'Create' row of options
     * 
     * @author Kristopher Rangel
     */
    private HBox getCreateServerClientRow() {
        Label createLabel = new Label("Create: ");
        ToggleGroup createTG = new ToggleGroup();
        serverRB = new RadioButton("Server");
        serverRB.setToggleGroup(createTG);
        serverRB.setSelected(true);
        clientRB = new RadioButton("Client");
        clientRB.setToggleGroup(createTG);
        
        HBox row = new HBox(createLabel, serverRB, clientRB);
        row.setSpacing(PADDING);
        return row;
    }
    
    /**
     * <ul><b><i>setScene</i></b></ul>
     * <ul><ul><p><code>private void setScene () </code></p></ul>
     *
     * Sets up the elements on the scene.
     * 
     * <p>The basic structure of the scene is a {@link VBox} with four rows. Each row is a
     * {@link HBox} that contains all elements on that row.</p>
     * 
     * <p>The default server is {@value #DEFAULT_SERVER} and the default port is {@value #DEFAULT_PORT}.</p>
     *
     * @author Kristopher Rangel
     */
    private void setScene() {

        HBox row1 = getCreateServerClientRow();
        
        // row2 (server/port row)
        Label serverLabel = new Label("Server");
        serverTF = new TextField(DEFAULT_SERVER);
        Label portLabel = new Label("Port");
        portTF = new TextField(DEFAULT_PORT);
        
        HBox row2 = new HBox(serverLabel, serverTF, portLabel, portTF);
        row2.setSpacing(PADDING);
        
        
        // row4 (button row)
        Button okay = new Button("OK");
        okay.setOnAction(e -> { hitOK = true; this.close(); } ); 
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> { this.close(); } );
        
        HBox row3 = new HBox(okay, cancel);
        row3.setSpacing(PADDING);
  
        
        // Adding rows to Vbox
        VBox box = new VBox(row1, row2, row3);;
        
        // Setting padding for Vbox
        Insets boxInsets = new Insets(PADDING);
        VBox.setMargin(row1, boxInsets);
        VBox.setMargin(row2, boxInsets);
        VBox.setMargin(row3, boxInsets);
        box.setSpacing(PADDING);
        
        // Adding VBox to scene
        this.scene = new Scene(box);
        this.setScene(scene);
    }
    
    /**
     * <ul><b><i>createModeSelection</i></b></ul>
     * <ul><ul><p><code>public boolean createModeSelection () </code></p></ul>
     *
     * Getter for user Server/Client selection during network setup.
     *
     * @return <li><code>True</code> if 'Server' was selected,<li><code>False</code> if 'Client' was selected
     * 
     * @author Kristopher Rangel
     */
    public boolean getCreateModeSelection() {
        return serverRB.isSelected();
    }    
    
    /**
     * <ul><b><i>getServer</i></b></ul>
     * <ul><ul><p><code>public String getServer () </code></p></ul>
     *
     * Gets the user-entered Server.
     *
     * @return - the <code>String</code> value the user entered into the server text box
     * 
     * @author Kristopher Rangel
     */
    public String getServer() {
        return serverTF.getText();
    }
    
    /**
     * <ul><b><i>getPort</i></b></ul>
     * <ul><ul><p><code>public int getPort () </code></p></ul>
     *
     * Gets the user-entered port number.
     *
     * @return - the <code>Integer</code> representation of the user entered value in the port text box
     *
     * @author Kristopher Rangel
     */
    public int getPort() {
        return Integer.valueOf(portTF.getText());
    }
    
    /**
     * <ul><b><i>userHitOK</i></b></ul>
     * <ul><ul><p><code> boolean userHitOK () </code></p></ul>
     *
     * Gets whether the user hit the okay button or the cancel button (or clicked the X to exit)
     *
     * @return <li><code>True</code> if the Okay button was clicked,
     * <li><code>False</code> if Cancel button was clicked, or the window was exited with the 'X' box
     * 
     * @author Kristopher Rangel 
     */
    public boolean userHitOK() {
        return hitOK;
    }
    
    
}
