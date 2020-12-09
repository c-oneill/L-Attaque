package stratego;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * This class serves as the Stage for the color picker dialog box.
 *
 * <p> The main purpose of this class is to facilitate user-selected chat
 * box color selection.
 *  
 * @author Kristopher Rangel
 *
 */
public class ChatColorPicker extends Stage{

    private final ColorPicker fontColor;
    private final ColorPicker fontBGColor;
    private final int PADDING = 20; 
    private Scene scene;
    private boolean hitOK;
    
    // final colors
    private Color textColor;
    private Color bgColor;
    
    // Starting colors
    private Color defaultTextColor;
    private Color defaultBGColor; 
    
    /**
     * <ul><b><i>ColorPicker</i></b></ul>
     * <ul><ul><p><code>public ColorPicker (Color colorText, Color colorBG)</code></p></ul>
     * 
     * Constructor.
     * 
     * @param currentTextColor - current chat font color
     * @param currentBGColor - current chat background color
     * 
     * @author Kristopher Rangel
     */
    public ChatColorPicker(Color currentTextColor, Color currentBGColor){
        this.defaultBGColor = currentBGColor;
        this.defaultTextColor = currentTextColor;
        this.hitOK = false;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Color Selection");
        this.setResizable(false);
        
        // Font color picker
        Label fontLabel = new Label("Chat Font Color");
        fontColor = new ColorPicker(currentTextColor);
        fontColor.getStyleClass().add("button");
        fontColor.setOnAction(e -> {
            textColor = fontColor.getValue();
        });
        
        // Background color picker
        Label bgLabel = new Label("Chat Background Color");
        fontBGColor = new ColorPicker(currentBGColor);
        fontBGColor.getStyleClass().add("button");
        fontBGColor.setOnAction(e -> {
            bgColor = fontBGColor.getValue();
        });

        // buttons
        Button okay = new Button("OK");
        okay.setOnAction(e -> { hitOK = true; this.close(); } ); 
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> { this.close(); } );

        // Adding elements to GridPane
        GridPane box = new GridPane();
        Insets boxInsets = new Insets(PADDING);
        box.add(fontLabel, 0, 0);
        box.add(fontColor, 1, 0);
        box.add(bgLabel, 0, 1);
        box.add(fontBGColor, 1, 1);
        box.add(okay, 0, 2);
        box.add(cancel, 1, 2);
        GridPane.setConstraints((Node) fontLabel, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        GridPane.setConstraints((Node) fontColor, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        GridPane.setConstraints((Node) bgLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        GridPane.setConstraints((Node) fontBGColor, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        GridPane.setConstraints((Node) okay, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        GridPane.setConstraints((Node) cancel, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, boxInsets);
        
        // Setting scene
        this.scene = new Scene(box);
        this.setScene(scene);
    }
    
    /**
     * <ul><b><i>userHitOK</i></b></ul>
     * <ul><ul><p><code>protected boolean userHitOK () </code></p></ul>
     *
     * Gets whether the user hit the okay button or the cancel button (or clicked the X to exit)
     *
     * @return <li><code>True</code> if the Okay button was clicked,
     * <li><code>False</code> if Cancel button was clicked, or the window was exited with the 'X' box
     * 
     * @author Kristopher Rangel 
     */
    protected boolean userHitOK() {
        return hitOK;
    }
    
    /**
     * <ul><b><i>getTextColor</i></b></ul>
     * <ul><ul><p><code>protected Color getTextColor () </code></p></ul>
     *
     * Returns the chat text color selected.
     *
     * @return {@link Color} selected by user, if no color was selected, returns the current color.
     * 
     * @author Kristopher Rangel
     */
    protected Color getTextColor() {
        Color color = textColor;
        if(color == null)
            color = defaultTextColor;
        return color;
    }


    /**
     * <ul><b><i>getBgColor</i></b></ul>
     * <ul><ul><p><code> Color getBgColor () </code></p></ul>
     *
     * Returns the chat background color selected.
     *
     * @return {@link Color} selected by user, if no color was selected, returns the current color.
     * 
     * @author Kristopher Rangel
     */
    protected Color getBgColor() {
        Color color = bgColor;
        if(color == null)
            color = defaultBGColor;
        return color;
    }
}