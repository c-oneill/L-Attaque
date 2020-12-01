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

public class PieceView extends VBox {

    //                        Player colors             Dirt            Lake            Has Piece
    private Color[] colors = {Color.BLUE, Color.RED,    Color.WHEAT,    Color.AQUA,     Color.TRANSPARENT}; 
    
    private String[] rank_images = {"ranks_flag.png","ranks_bomb.png", "ranks_1-spy.png", "ranks_2-scout.png",
            "ranks_3-Miner.png", "ranks_4-SGT.png", "ranks_5-LT.png", "ranks_6-CPT.png",
            "ranks_7-MAJ.png", "ranks_8-COL.png", "ranks_9-GEN.png", "ranks_10-Marshall.png"};
    
    private Piece piece;
    private Color color;
    private int pieceIndex;

    private final int SIZE = 70;
    
    public PieceView (int pieceIndex, Color borderColor, int borderWidth) {
        super();
        
        this.pieceIndex = pieceIndex;
        
        Background bg;
        
        if(pieceIndex == -2) { // lake
            this.color = colors[3];
        }else if(pieceIndex == -1) { // empty land
            this.color = colors[2];
        }else {
            this.color = colors[4];   
        }
        
        
        
        if(pieceIndex >= 0) {
         
            BackgroundImage bgi = new BackgroundImage(new Image(getImagePath(pieceIndex)), 
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            bg = new Background(bgi);
            
        }else {
            BackgroundFill bgfill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            bg = new Background(bgfill);
        }
        this.setBackground(bg);
        
        Rectangle r = new Rectangle(SIZE, SIZE, color);
        this.getChildren().add(r);
        setSquareBorder(borderColor, borderWidth);
        
        this.setPrefHeight(SIZE);
        this.setPrefWidth(SIZE);
        
        this.setOnDragDetected( e-> {
            testPrintPiece();
            //System.out.println("Drag started");
            //System.out.println(e.getTarget().toString());
            e.consume();
        });
    }
    
    private void testPrintPiece() {
                         //  -2      -1       0        1     2      3       4         5     6       7     8      9      10      11
        String[] pieces = {"Lake", "Land", "Flag", "Bomb", "Spy", "Scout", "Miner", "SGT", "LT", "CPT", "MAJ", "COL", "GEN", "Marshal"};
        System.out.printf("Drag started on %s\n", pieces[pieceIndex + 2]);
    }
    
    private String getImagePath(int pieceIndex) {      
        return rank_images[pieceIndex];
    }
    
    public void setSquareBorder(Color borderColor, int borderWidth) {
        Rectangle r = (Rectangle) this.getChildren().get(0);
        r.setStroke(borderColor);
        r.setStrokeWidth(borderWidth);
    }
    
}
