package GUI.Animations;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Portrait extends ImageView {




    public static ImageView getChibi(String portrait){
        return new ImageView(new Image("file:Resources/mugshots/"+portrait+".png",128*6,112*6,false,false)){{
            setViewport(new Rectangle2D(96*6,16*6,32*6,32*6));
            setFitHeight(32*3);
            setFitWidth(32*3);
            }};
    }
    public static ImageView getPortrait(String portrait){
        return new ImageView(new Image("file:Resources/mugshots/"+portrait+".png",128*6,112*6,false,false)){{
            setViewport(new Rectangle2D(0,0,96*6,80*6));
            setFitHeight(80*3);
            setFitWidth(96*3);
        }};
    }
    public static String ELIWOOD_YOUNG = "000";
    public static String ELIWOOD_YOUNG_ANGRY = "001";
    public static String ELIWOOD_YOUNG_SAD = "002";
    public static String ELIWOOD_YOUNG_BROW_cap = "003";
    public static String ELIWOOD_NORMAL = "004";
    public static String ELIWOOD_BROWN_CAP = "005";
    public static String ELIWOOD_WHITE_CAP_SIDE = "006";
    public static String ELIWOOD_WHITE_CAP_FRONT = "007";
    public static String ELIWOOD_OLD = "010";
    public static String HECTOR_NORMAL = "011";
    public static String HECTOR_ANGRY = "012";
    public static String HECTOR_YOUNG_SAD = "013";
    public static String HECTOR_ANGRY_FRONT = "014";
    public static String HECTOR_BROWN_CAP = "015";
    public static String HECTOR_BROWN_CAP_FRONT = "016";
    public static String HECTOR_BROWN_CAP_ANGRY = "020";
    public static String HECTOR_RED_CAP_SIDE = "021";
    public static String HECTOR_RED_CAP_FRONT = "022";
    public static String HECTOR_FRONT = "023";
    public static String HECTOR_OLD = "024";
    public static String LYN_NORMAL = "025";
    public static String LYN_SIDE = "026";
    public static String LYN_SAD = "030";
    public static String LYN_BROWN_CAP = "031";
    public static String LYN_FRONT = "032";
    public static String KENT = "060";
    public static String SAIN = "061";
    public static String ZUGU = "182";


}
