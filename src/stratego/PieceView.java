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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.*;
import javafx.event.EventHandler;

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
    private final int SIZE = 70;
    private final String[] RANK_IMAGES = {
            "ranks_flag.png","ranks_bomb.png", "ranks_1-spy.png", "ranks_2-scout.png",
            "ranks_3-Miner.png", "ranks_4-SGT.png", "ranks_5-LT.png", "ranks_6-CPT.png",
            "ranks_7-MAJ.png", "ranks_8-COL.png", "ranks_9-GEN.png", "ranks_10-Marshall.png"
            };
    
    private Color color;
    private int pieceIndex;
    private Color borderColor;
    private int borderWidth;
    private Piece piece;
    private boolean dragEnabled;
    private boolean dropEnabled;
    
    private int row;
    private int col;
    
    /**
     * Constructor.
     * 
     * @param pieceIndex - index of the piece or empty square 
     * (lake = -2, land = -1, flag = 0, bomb = 1, spy = 2, scout = 3, miner = 4, SGT = 5, LT = 6, CPT = 7,
     * MAJ = 8, COL = 9, GEN = 10, Marshall = 11)
     * @param borderColor - {@link Color} of the border
     * @param borderWidth - an integer representing the border width
     */  
    public PieceView (int pieceIndex, Color borderColor, int borderWidth) {
        super();
        this.row = -1; // 'null' value
        this.col = -1; // 'null' value
        this.pieceIndex = pieceIndex;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        dragEnabled = true;
        dropEnabled = true;
        
        setPiece();
        initBackground(pieceIndex);
        
        Rectangle r = new Rectangle(SIZE, SIZE, TRANSPARENT);
        this.getChildren().add(r);
        setSquareBorder(borderColor, borderWidth);
        
        this.setPrefHeight(SIZE);
        this.setPrefWidth(SIZE);
        
        this.setOnDragDetected( e-> {
            if(this.dragEnabled) {
                System.out.print("Drag started on ");
                testPrintPiece();
                Dragboard db = startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(toString());
                db.setContent(content);
            }
            e.consume();
        });
        
        // Mouse dragover event handler
        this.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        
        // Drag over entered event ( mouse is dragging over this)
        this.setOnDragEntered(e -> {
            if(dropEnabled) {
                System.out.print("Drag entered ");
                testPrintPiece();
                // setting color to "highlight" square mouse is over
                //TODO check for own pieces and not highlight
                //TODO check for invalid move square and not highlight
                Color c = Color.MAGENTA;
                setBorderColor(c);
            }
        });

        // Drag over exited event ( mouse is no longer dragging over this
        this.setOnDragExited(e -> {
            if(dropEnabled) {
                System.out.print("Drag exited ");
                testPrintPiece();
                // reseting to stored color value (mouse drag no longer over)
                setBorderColor(this.borderColor);
                e.consume();
            }
        });
        
        // Drag over dropped event
        this.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            
            if(db.hasString() && this.dropEnabled) {
                
                //TODO check with controller for valid position
                
                String rawInfo = db.getString();
                String[] info = rawInfo.split(" ");
                setBorderWidth(Integer.parseInt(info[2]));
                initBackground(Integer.parseInt(info[0]));
                
                // Changing border color value (drag over exit event will actually set the border color)
                this.borderColor = Color.web(info[1]);

                success = true;
                

                System.out.print("Drag ended on ");
                testPrintPiece();
                System.out.printf("String: %s %s %s\n", info[0], info[1], info[2]);
                System.out.printf("Dropped on row %d and col %d\n", row, col);
                //System.out.println(color.toString());
                
            }
            e.setDropCompleted(success);
            e.consume();
            
        });
    }

    public PieceView(int pieceIndex, Color borderColor) { 
        this(pieceIndex, borderColor, 3);  // default border width is 3
    }
    
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
            BackgroundImage bgi = new BackgroundImage(new Image(imagePath), 
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
     */
    public void setSquareBorder(Color borderColor, int borderWidth) {
        setBorderColor(borderColor);
        setBorderWidth(borderWidth);
    }
    
    private void setBorderColor(Color color) {
        Rectangle r = (Rectangle) this.getChildren().get(0);
        r.setStroke(color);
    }
    
    private void setBorderWidth(int width) {
        this.borderWidth = width;
        Rectangle r = (Rectangle) this.getChildren().get(0);
        r.setStrokeWidth(width);
    }
    
    public void setDragEnabled(boolean enabled) { this.dragEnabled = enabled; }
    
    public void setDropEnabled(boolean enabled) { this.dropEnabled = enabled; }
    
    private void setPiece() {
        switch(pieceIndex) {
        case -2:
            piece = Piece.LAKE;
            break;
        case -1:
            piece = Piece.EMPTY;
            break;
        case 0:
            piece = Piece.FLAG;
            break;
        case 1:
            piece = Piece.BOMB;
            break;
        case 2:
            piece = Piece.SPY;
            break;
        case 3:
            piece = Piece.SCOUT;
            break;
        case 4:
            piece = Piece.MINER;
            break;
        case 5:
            piece = Piece.SERGEANT;
            break;
        case 6:
            piece = Piece.LIEUTENANT;
            break;
        case 7:
            piece = Piece.CAPTAIN;
            break;
        case 8:
            piece = Piece.MAJOR;
            break;
        case 9:
            piece = Piece.COLONEL;
            break;
        case 10:
            piece = Piece.GENERAL;
            break;
        case 11:
            piece = Piece.MARSHAL;
            break;
        }
    }
    
    public Piece getPiece() {
        return this.piece;
    }
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    
    public String toString() {
        String str = String.valueOf(pieceIndex) + " " 
                   + String.valueOf(borderColor) + " " 
                   + String.valueOf(borderWidth);
        return str;
    }
    
    private void testPrintPiece() {
                         //  -2      -1       0        1     2      3       4         5     6       7     8      9      10      11
        String[] pieces = {"Lake", "Land", "Flag", "Bomb", "Spy", "Scout", "Miner", "SGT", "LT", "CPT", "MAJ", "COL", "GEN", "Marshal"};
        System.out.printf("%s\n", pieces[pieceIndex + 2]);
    }
}
