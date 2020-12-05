package stratego;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.*;
import javafx.event.EventHandler;
import stratego.Piece.PieceType;

/**
 * This class represents a single square in the UI for the {@link StrategoView}.
 * 
 * <p>This class servers as a wrapper to encapsulate the display and UI functions of
 * a square on the board or a piece, to include mouse interaction.
 * </p>
 * 
 * @author Kristopher Rangel
 *
 */

public class PieceView extends VBox {
    
    private final Color LAKE = Color.AQUA;
    private final Color LAND = Color.WHEAT;
    private final Color TRANSPARENT = Color.TRANSPARENT;
    
    //private final int SIZE = 70;
    private final double SIZE = (StrategoView.STANDARD - 90/825D) * 70;
    
    private final String[] RANK_IMAGES = {
            "ranks_flag.png","ranks_bomb.png", "ranks_1-spy.png", "ranks_2-scout.png",
            "ranks_3-Miner.png", "ranks_4-SGT.png", "ranks_5-LT.png", "ranks_6-CPT.png",
            "ranks_7-MAJ.png", "ranks_8-COL.png", "ranks_9-GEN.png", "ranks_10-Marshall.png"
            };
    
    private Color color;
    private int pieceIndex;
    private Color borderColor;
    private int borderWidth;
    private PieceType piece;
    private boolean dragEnabled;
    private boolean dropEnabled;
    private boolean isOnBoard;
    private Label label;
    private int row;
    private int col;
    
    //private StrategoController controller;
    
    /**
     * Constructor.
     * 
     * @param pieceIndex - index of the piece or empty square 
     * (lake = -2, land = -1, flag = 0, bomb = 1, spy = 2, scout = 3, miner = 4, SGT = 5, LT = 6, CPT = 7,
     * MAJ = 8, COL = 9, GEN = 10, Marshall = 11)
     * @param borderColor - {@link Color} of the border
     * @param borderWidth - an integer representing the border width
     * 
     * @author Kristopher Rangel
     */  
    public PieceView (StrategoController controller, int pieceIndex, Color borderColor, int borderWidth) {
        super();
        this.row = -1; // 'null' value
        this.col = -1; // 'null' value
        this.pieceIndex = pieceIndex;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.setPrefHeight(SIZE);
        this.setPrefWidth(SIZE);
        
        dragEnabled = true;
        dropEnabled = true;
        
        label = new Label();
        setPieceType();
        initBackground(pieceIndex);
        initBorder();
        setEvents();
        
    }
    
    public void setLabelText(String text) {
        label.setText(text);
    }
    public Label getLabel() {
        return this.label;
    }
    
    /**
     * Overloaded Constructor.
     * 
     * <p>Loads the constructor with a default border width of 3.
     * 
     * @param pieceIndex - index of the piece or empty square 
     * (lake = -2, land = -1, flag = 0, bomb = 1, spy = 2, scout = 3, miner = 4, SGT = 5, LT = 6, CPT = 7,
     * MAJ = 8, COL = 9, GEN = 10, Marshall = 11)
     * @param borderColor - {@link Color} of the border
     * 
     * @author Kristopher Rangel
     */
    public PieceView(int pieceIndex, Color borderColor) { 
        this(null, pieceIndex, borderColor, 3);  // default border width is 3
    }
    
    /**
     * <ul><b><i>initBorder</i></b></ul>
     * <ul><ul><p><code>private void initBorder () </code></p></ul>
     *
     * Initializes the border for this object. 
     * 
     * <p>It does this via a transparent {@link Rectangle} object.
     *
     * @author Kristopher Rangel
     */
    private void initBorder() {
        Rectangle r = new Rectangle(SIZE, SIZE, TRANSPARENT);
        this.getChildren().add(r);
        setSquareBorder(borderColor, borderWidth); 
    }
    
    /**
     * <ul><b><i>setEvents</i></b></ul>
     * <ul><ul><p><code>private void setEvents () </code></p></ul>
     *
     * This sets the drag and drop events associated with this object.
     * 
     * <p>The events are inherited from VBox. This function just specifies related actions.
     * 
     * @see VBox
     *
     * @author Kristopher Rangel
     */
    private void setEvents() {
        
        // On drag detected event (the start of a drag and drop)
        this.setOnDragDetected( e-> {
            if(this.dragEnabled) {
                Dragboard db;
                
                // if on board transfer mode is move, if from the pieces box, copy
                if(this.isOnBoard) { db = startDragAndDrop(TransferMode.MOVE); }
                else { db = startDragAndDrop(TransferMode.COPY); }
                
                ClipboardContent content = new ClipboardContent();
                content.putString(toString());
                db.setContent(content);
                
                if(StrategoView.ENABLE_CONSOLE_DEBUG) {
                    System.out.print("Drag started on ");
                    testPrintPiece();
                }
                this.startFullDrag();
            }
            e.consume();
        });
        
        // Mouse dragover event handler (for accepting a drag and drop)
        this.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(dropEnabled) {
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
            }
        });
        
        // Drag over entered event ( mouse is dragging over this)
        this.setOnDragEntered(e -> {
            if(dropEnabled) {
                //TODO check for own pieces and not highlight
                //TODO check for invalid move square and not highlight
                
                
                // setting color to "highlight" square mouse is over
                Color c = Color.MAGENTA;
                setBorderColor(c);
                
                if(StrategoView.ENABLE_CONSOLE_DEBUG) {
                    System.out.print("Drag entered ");
                    testPrintPiece();
                }
                
            }
        });

        // Drag over exited event ( mouse is no longer dragging over this)
        this.setOnDragExited(e -> {
            if(dropEnabled) {

                // reseting to stored color value (mouse drag no longer over)
                setBorderColor(this.borderColor);
                e.consume();
                
                if(StrategoView.ENABLE_CONSOLE_DEBUG) {
                    System.out.print("Drag exited ");
                    testPrintPiece();
                }
            }
        });
        
        // Drag over dropped event (the drag and drop event is completed)
        this.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            int oldPiece = pieceIndex; // saving previous piece
            if(db.hasString() && this.dropEnabled) {
                
                //TODO check with controller for valid position
                
                
                String rawInfo = db.getString();
                String[] info = rawInfo.split(" ");
                this.pieceIndex = Integer.parseInt(info[0]);
                setBorderWidth(Integer.parseInt(info[2]));
                
                update();
                //setPieceType();
                //initBackground(pieceIndex);
                
                // getting whether the moved piece came from the board (true) or the pieces box (false)
                boolean fromBoard = (info[3].substring(0, 4).equals("true"));
                if(fromBoard) {
                    //System.out.println("From Board!");
                    
                        
                    
                    
                    
                }else { // Not from board (placed during setup)
                   
                    
                    if(oldPiece != pieceIndex){ // if pieced placed is not the same piece already there
                        StrategoView.updateLabels(pieceIndex, -1); // decrement count of new piece
                        
                        if(oldPiece >= 0) {  // If position on board was occupied by another piece
                            StrategoView.updateLabels(oldPiece, 1); // increment count of old piece
                        }
                    }


                }
                
                // Changing border color value (drag over exit event will actually set the border color)
                this.borderColor = Color.web(info[1]);

                success = true;
                //int colorInt = (this.color == Color.BLUE) ? 0 : 1;
                //controller.addToSetup(this.row, this.col, this.piece, colorInt); 
                
                if(StrategoView.ENABLE_CONSOLE_DEBUG) {
                    System.out.print("Drag ended on ");
                    testPrintPiece();
                    System.out.printf("String: %s %s %s\n", info[0], info[1], info[2]);
                    System.out.printf("Dropped on row %d and col %d\n", row, col);
                    //System.out.println(color.toString());
                }
                
            }
            e.setDropCompleted(success);
           
            e.consume();
            
        });
    }
    
    /**
     * <ul><b><i>initBackground</i></b></ul>
     * <ul><ul><p><code>private void initBackground (pieceIndex) </code></p></ul>
     *
     * Initialized the background.
     * 
     * <p> The background is set based on the given piece index. A lake is colored with
     * the value of {@link #LAKE} and land is colored with the value of {@link #LAND}.
     * For the remaining pieces the applicable background image is used.
     *
     * @param pieceIndex - the index of the piece 
     * 
     * @author Kristopher Rangel
     */
    private void initBackground(int pieceIndex) {
        Background bg;
        
        if(pieceIndex == -2) { // lake
            this.color = LAKE;
            dragEnabled = false;
            dropEnabled = false;
        }else if(pieceIndex == -1) { // empty land
            this.color = LAND;
            dragEnabled = false;
        }else {
            this.color = TRANSPARENT;   
        }
        
        if(pieceIndex >= 0 && pieceIndex < RANK_IMAGES.length) {
            String imagePath = RANK_IMAGES[pieceIndex];
            BackgroundImage bgi = new BackgroundImage(new Image(imagePath, SIZE, SIZE, true, true), 
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            bg = new Background(bgi);
            
        }else {
            BackgroundFill bgfill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            bg = new Background(bgfill);
        }
        this.setBackground(bg);
    }
    
    
    /**
     * <ul><b><i>setSquareBorder</i></b></ul>
     * <ul><ul><p><code>public void setSquareBorder (Color borderColor, int borderWidth) </code></p></ul>
     *
     * Sets the border color and with of this object.
     *
     * @param borderColor - the {@link Color} to set the border to
     * @param borderWidth - the integer value to set the width of the border to
     * 
     * @author Kristopher Rangel
     */
    public void setSquareBorder(Color borderColor, int borderWidth) {
        setBorderColor(borderColor);
        setBorderWidth(borderWidth);
    }
    
    /**
     * <ul><b><i>setBorderColor</i></b></ul>
     * <ul><ul><p><code>private void setBorderColor (Color color) </code></p></ul>
     *
     * Sets the border color to the given color.
     *
     * @param color - the {@link Color} object to apply to the border
     * 
     * @author Kristopher Rangel
     */
    private void setBorderColor(Color color) {
        Rectangle r = (Rectangle) this.getChildren().get(0);
        r.setStroke(color);
    }
    
    /**
     * <ul><b><i>setBorderWidth</i></b></ul>
     * <ul><ul><p><code>private void setBorderWidth (int width) </code></p></ul>
     *
     * Sets the border width the the specified value.
     *
     * @param width - an integer representing the width to set the border to
     * 
     * @author Kristopher Rangel
     */
    private void setBorderWidth(int width) {
        this.borderWidth = width;
        Rectangle r = (Rectangle) this.getChildren().get(0);
        r.setStrokeWidth(width);
    }
    
    /**
     * <ul><b><i>setDragEnabled</i></b></ul>
     * <ul><ul><p><code>public void setDragEnabled (boolean enabled) </code></p></ul>
     *
     * Sets the drag enabled flag.
     * 
     * <p>If set to true, this object is dragable, otherwise, it is not dragable.
     *
     * @param enabled - true to enable dragging, false otherwise
     * 
     * @author Kristopher Rangel
     */
    public void setDragEnabled(boolean enabled) { this.dragEnabled = enabled; }
    
    /**
     * <ul><b><i>setDropEnabled</i></b></ul>
     * <ul><ul><p><code>public void setDropEnabled (boolean enabled) </code></p></ul>
     *
     * Sets the drop enabled flag.
     * 
     * <p>If set to true, other objects can be dropped on this object, otherwise, it is not a 
     * valid target for a drag and drop event.
     *
     * @param enabled - true to enable dropping, false otherwise.
     * 
     * @author Kristopher Rangel
     */
    public void setDropEnabled(boolean enabled) { this.dropEnabled = enabled; }
    
    /**
     * <ul><b><i>setPiece</i></b></ul>
     * <ul><ul><p><code>private void setPiece () </code></p></ul>
     *
     * Stores or updates a reference to a {@link Piece} object that this {@link PieceView}
     * represents.
     * 
     * @author Kristopher Rangel
     *
     */
    private void setPieceType() {
        piece = convertPieceIndexToType(pieceIndex);
    }
    
    /**
     * <ul><b><i>getPiece</i></b></ul>
     * <ul><ul><p><code>private Piece getPiece () </code></p></ul>
     *
     * Returns the {@link Piece} object that this {@link PieceView} object
     * represents. 
     *
     * @return the <code>Piece</code> object represented by this object
     * 
     * @author Kristopher Rangel
     */
    public PieceType getPieceType() {
        return this.piece;
    }
    
    /**
     * <ul><b><i>setPosition</i></b></ul>
     * <ul><ul><p><code>public void setPosition (int row, int col) </code></p></ul>
     *
     * Sets the position on the board that this object is present in.
     *
     * @param row - the row on the board
     * @param col - the column on the board
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public void setIsOnBoard(boolean isOnBoard) {
        this.isOnBoard = isOnBoard;
    }
    
    /**
     * <ul><b><i>toString</i></b></ul>
     * <ul><ul><p><code>public String toString () </code></p></ul>
     *
     * Converts this object into a string value representing the 
     * piece index, the border color, and the border width.
     *
     * @return - the string represtation of this object
     * 
     * @author Kristopher Rangel
     */
    public String toString() {
        String str = String.valueOf(pieceIndex) + " " 
                   + String.valueOf(borderColor) + " " 
                   + String.valueOf(borderWidth) + " "
                   + String.valueOf(isOnBoard);
        return str;
    }
    
    /**
     * <ul><b><i>testPrintPiece</i></b></ul>
     * <ul><ul><p><code> void testPrintPiece () </code></p></ul>
     *
     * A test function to print the text equivalent of this object's piece index
     * 
     * @author Kristopher Rangel
     *
     */
    private void testPrintPiece() {
                         //  -2      -1       0        1     2      3       4         5     6       7     8      9      10      11
        String[] pieces = {"Lake", "Land", "Flag", "Bomb", "Spy", "Scout", "Miner", "SGT", "LT", "CPT", "MAJ", "COL", "GEN", "Marshal"};
        System.out.printf("%s\n", pieces[pieceIndex + 2]);
    }
    
    /**
     * <ul><b><i>convertPieceIndexToType</i></b></ul>
     * <ul><ul><p><code> PieceType convertPieceIndexToType (int pieceIndex) </code></p></ul>
     *
     *Converts a piece index number to the equivalent {@link PieceType}. 
     *
     * @param pieceIndex - the piece index integer value to covert
     * @return the associated PieceType
     * 
     * @author Kristopher Rangel
     */
    public static PieceType convertPieceIndexToType(int pieceIndex) {
        PieceType pieceType = null;
        switch(pieceIndex) {
        case -2:
            pieceType = PieceType.LAKE;
            break;
        case -1:
            pieceType = PieceType.EMPTY;
            break;
        case 0:
            pieceType = PieceType.FLAG;
            break;
        case 1:
            pieceType = PieceType.BOMB;
            break;
        case 2:
            pieceType = PieceType.SPY;
            break;
        case 3:
            pieceType = PieceType.SCOUT;
            break;
        case 4:
            pieceType = PieceType.MINER;
            break;
        case 5:
            pieceType = PieceType.SERGEANT;
            break;
        case 6:
            pieceType = PieceType.LIEUTENANT;
            break;
        case 7:
            pieceType = PieceType.CAPTAIN;
            break;
        case 8:
            pieceType = PieceType.MAJOR;
            break;
        case 9:
            pieceType = PieceType.COLONEL;
            break;
        case 10:
            pieceType = PieceType.GENERAL;
            break;
        case 11:
            pieceType = PieceType.MARSHAL;
            break;
        }
        return pieceType;
    }
    
    /**
     * <ul><b><i>convertPieceTypeToIndex</i></b></ul>
     * <ul><ul><p><code>public int convertPieceTypeToIndex (PieceType pieceType) </code></p></ul>
     *
     * Converts a {@link PieceType} to the equivalent piece index number.
     *
     * @param pieceType - the PieceType value to convert
     * @return the associated piece index value
     * 
     * @author Kristopher Rangel
     */
    public int convertPieceTypeToIndex(PieceType pieceType) {
        int pieceIndex = -1;
        switch(pieceType) {
        case LAKE:
            pieceIndex = -2;
            break;
        case EMPTY:
            pieceIndex = -1;
            break;
        case FLAG:
            pieceIndex = 0;
            break;
        case BOMB:
            pieceIndex = 1;
            break;
        case SPY:
            pieceIndex = 2;
            break;
        case SCOUT:
            pieceIndex = 3;
            break;
        case MINER:
            pieceIndex = 4;
            break;
        case SERGEANT:
            pieceIndex = 5;
            break;
        case LIEUTENANT:
            pieceIndex = 6;
            break;
        case CAPTAIN:
            pieceIndex = 7;
            break;
        case MAJOR:
            pieceIndex = 8;
            break;
        case COLONEL:
            pieceIndex = 9;
            break;
        case GENERAL:
            pieceIndex = 10;
            break;
        case MARSHAL:
            pieceIndex = 11;
            break;
        }
        return pieceIndex;
    }
    
    /**
     * <ul><b><i>update</i></b></ul>
     * <ul><ul><p><code>private void update () </code></p></ul>
     *
     * Updates the display to reflect the currently stored piece.
     * 
     * @author Kristopher Rangel
     *
     */
    private void update() {
        setPieceType();
        initBackground(pieceIndex);
    }
    
    /**
     * <ul><b><i>update</i></b></ul>
     * <ul><ul><p><code>private void update (Piece piece) </code></p></ul>
     *
     * Updates this PieceView object to store the given {@link Piece} and
     * display the associate background image.
     *
     * @param piece - the <code>Piece</code> to update this PieceView with
     * 
     * @author Kristopher Rangel
     */
    public void update(Piece piece) {
        this.piece = piece.type;
        initBackground(convertPieceTypeToIndex(this.piece));
    }
}
