package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResizableImage extends Image {
    public ResizableImage(String s) {
        super(s);
    }
    public ResizableImage(String s, double width, double height){
        super(s, 6*width,6*height ,false,false);
    }
    public ResizableImage(InputStream inputStream) throws IOException {
        super(inputStream);
    }
    public ResizableImage getSubimage(int x, int y, int w, int h) throws IOException {
        PixelReader reader = this.getPixelReader();
        WritableImage image = new WritableImage(reader, x, y, w, h);
        return new ResizableImage(getImgAsInputStream(image));
    }
    public Image resize(int targetWidth, int targetHeight, boolean preserveRatio) throws IOException {
        ImageView imageView = new ImageView(this);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return new ResizableImage(getImgAsInputStream(imageView.snapshot(null, null)));
    }


    private InputStream getImgAsInputStream(Image image){
        try {
            ImageView view = new ImageView(image);
            BufferedImage bImage = SwingFXUtils.fromFXImage(view.getImage(), null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", outputStream);
            byte[] res  = outputStream.toByteArray();
            return new ByteArrayInputStream(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws IOException {
        new ResizableImage("file:MapGeneration/Tilesets/0a000b0c.png").getSubimage(0,0,16,16);
    }
}
