/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.util;

//import com.sun.media.jai.codec.ImageCodec;
//import com.sun.media.jai.codec.ImageEncoder;
//import com.sun.media.jai.codec.PNGEncodeParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A utility class for generating image files and manipulating images.
 *
 * @author Barry Becker
 */
public final class ImageUtil {

    public enum ImageType { PNG, JPG }

    private ImageUtil() {}

    /**
     * @return a BufferedImage from an Image
     */
    public static BufferedImage makeBufferedImage( final Image image ) {

        BufferedImage bImg = new BufferedImage( image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2 = bImg.createGraphics();
        g2.drawImage( image, null, null );
        g2.dispose();
        return bImg;
    }

    /**
     * create an image that is compatible with your hardware
     */
    public static BufferedImage createCompatibleImage( int width, int height ) {

        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = local.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screen.getDefaultConfiguration();
        return configuration.createCompatibleImage( width, height );
    }

    /**
     * return a byte array given an image
     * @param img the image to convert
     * @param type the type of image to create ("jpg" or "png")
     */
    public static byte[] getImageAsByteArray( Image img, ImageType type ) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream( bos );
        writeImage( img, os, type );

        return bos.toByteArray();
    }

    /**
     *  write an image to the given output stream
     *  @param img image to write.
     *  @param out output stream to write to
     *  @param type the type of image to create ("jpg" or "png")
     */
    public static void writeImage( Image img, BufferedOutputStream out, ImageType type ) {

        BufferedImage bi = makeBufferedImage( img );

        if ( type == ImageType.JPG ) {

            ImageWriter encoder = ImageIO.getImageWritersByFormatName("JPEG").next(); //NON-NLS
            JPEGImageWriteParam param = new JPEGImageWriteParam(null);

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            encoder.setOutput(out);

            try {
                encoder.write(null, new IIOImage((RenderedImage)img, null, null), param);
            } catch (IOException fne) {
                throw new IllegalStateException("IOException error:" + fne.getMessage(), fne);
            }
        }
        else { // PNG is the default
            //PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam( bi );
            //ImageEncoder encoder = ImageCodec.createImageEncoder( "PNG", out, param ); //NON-NLS
            try {
                // Writes it to a file as a .png
                ImageIO.write(bi, "png", out);
                //encoder.encode( bi );
            } catch (IOException e) {
                throw new IllegalStateException("IOException error.", e);
            } catch (NullPointerException npe) {
                throw new IllegalStateException("Could not encode buffered image because it was null.", npe);
            }
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException( "IOException error.", e);
        }
    }

    /**
     * Saves an image to a file using the format specified by the type
     * note the filename should not include the extension.
     * this will be added as appropriate.
     * @param fileName the fileName should not have an extension because it gets added based on VizContext.imageFormat
     * @param img the image to save
     * @param type of image ("jpg" or "png" (default))
     */
    public static void saveAsImage( String fileName, Image img, ImageType type ) {

        BufferedOutputStream os = null;
        try {
            String extension = '.' +type.toString().toLowerCase();
            String fn = fileName;
            if (!fn.endsWith(extension))  {
                // if it does not already have the appropriate extension add it.
                fn += extension;
            }

            os = new BufferedOutputStream( new FileOutputStream( fn ) );
        } catch (FileNotFoundException fne) {
            System.out.println( "File " + fileName + " not found: " + fne.getMessage()); //NON-NLS
        }

        writeImage( img, os, type );
    }

    /**
     * @param pixels one dimension array of pixels where a pixel at x and y can be located with
     *   3 *(x * height + y )
     *   Note that there are 4 integers for every pixel (rgb)
     * @param width
     * @param height
     * @return image from the pixel data
     */
    public static Image getImageFromPixelArray(int[] pixels, int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }


    /**
     * Interpolate among 4 colors (corresponding to the 4 points on a square)
     * @return The interpolated color.
     */
    public static Color interpolate( double x, double y, float[]
                                     colorLL, float[] colorLR, float[] colorUL, float[]  colorUR ) {

        float[] rgbaL = new float[4];
        float[] rgbaU = new float[4];

        rgbaL[0] = (float) (colorLL[0] + x * (colorLR[0] - colorLL[0]));
        rgbaL[1] = (float) (colorLL[1] + x * (colorLR[1] - colorLL[1]));
        rgbaL[2] = (float) (colorLL[2] + x * (colorLR[2] - colorLL[2]));
        rgbaL[3] = (float) (colorLL[3] + x * (colorLR[3] - colorLL[3]));

        rgbaU[0] = (float) (colorUL[0] + x * (colorUR[0] - colorUL[0]));
        rgbaU[1] = (float) (colorUL[1] + x * (colorUR[1] - colorUL[1]));
        rgbaU[2] = (float) (colorUL[2] + x * (colorUR[2] - colorUL[2]));
        rgbaU[3] = (float) (colorUL[3] + x * (colorUR[3] - colorUL[3]));

        return new Color( (float) (rgbaL[0] + y * (rgbaU[0] - rgbaL[0])),
                          (float) (rgbaL[1] + y * (rgbaU[1] - rgbaL[1])),
                          (float) (rgbaL[2] + y * (rgbaU[2] - rgbaL[2])),
                          (float) (rgbaL[3] + y * (rgbaU[3] - rgbaL[3])));
    }
}
