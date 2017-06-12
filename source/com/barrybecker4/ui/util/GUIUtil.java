// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.util;

import com.barrybecker4.common.app.ClassLoaderSingleton;
import com.barrybecker4.ui.components.SplashScreen;
import com.barrybecker4.ui.file.FileChooserUtil;
import com.barrybecker4.ui.themes.BarryTheme;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * This class implements a number of static utility functions that are useful when creating UIs.
 * I used to support running as applet or webstart separately from running as an application, but
 * now I just run the applet as an application and it seems to work.
 *
 * @author Barry Becker
 */
public final class GUIUtil {

    private GUIUtil() {}

    /**
     * Some other interesting fonts: "Ænigma Scrawl 4 BRK"; "Nyala"; "Raavi";
     * Verdana is nice, but it does not support japanese or vietnamese character.
     * Only Serif and SansSerif seem to support everything.
     */
    public static final String DEFAULT_FONT_FAMILY = "SansSerif";  // NON-NLS

    /** webstart services  */
    private static BasicService basicService_ = null;

    /**
     *  Set the ui look and feel to my very own.
     */
    public static void setCustomLookAndFeel()  {
        BarryTheme theme = new BarryTheme();
        MetalLookAndFeel.setCurrentTheme( theme );

        // for java 1.4 and later
        //JFrame.setDefaultLookAndFeelDecorated(true);
        //JDialog.setDefaultLookAndFeelDecorated(true);

        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // for windows
            //java look and feel is customizable with themes
            UIManager.setLookAndFeel( "javax.swing.plaf.metal.MetalLookAndFeel" );

            // a cool experimental look and feel. see http://www.oyoaha.com/
            //OyoahaLookAndFeel lnf = new OyoahaLookAndFeel();
            //UIManager.setLookAndFeel(lnf);

            //GTK look and feel for Linux.
            //UIManager.setLookAndFeel( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );

            // MacIntosh Look and feel
            // there is supposed to be some trick to getting this to wowk, but I can't find it right now.
            //UIManager.setLookAndFeel( new it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel() );

            //UIManager.setLookAndFeel( new WindowsLookAndFeel() );

            // turn on auditory cues.
            // @@ can't do this under linux until I upgrade java or get the right sound card driver.
            UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.allAuditoryCues"));

            theme.setUIManagerProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return the image icon given the full path to the image.
     */
    public static ImageIcon getIcon(String sPath) {
        return getIcon(sPath, true);
    }

    /**
     * @return the image icon given the full path to the image.
     */
    public static ImageIcon getIcon(String sPath, boolean failIfNotFound) {
        ImageIcon icon = null;

        URL url = ClassLoaderSingleton.getClassLoader().getResource(sPath);
        if (url != null) {
            icon = new ImageIcon( url );
        }
        else if (failIfNotFound) {
            throw new IllegalArgumentException("Invalid file or url path:"+ sPath);
        }

        return icon;
    }

    /**
     * Load a buffered image from a file or resource.
     * @return loaded image or null if not found.
     */
    public static BufferedImage getBufferedImage(String path) {

         ImageIcon img = GUIUtil.getIcon(path, false);
         BufferedImage image = null;
         if (img != null && img.getIconWidth() > 0) {
             image = ImageUtil.makeBufferedImage(img.getImage());
         }
         return image;
    }

    /**
     * Displays a splash screen while the application is busy starting up.
     * @return the window containing the splash screen image.
     */
    public static JWindow showSplashScreen( int waitMillis, String imagePath) {
        // show a splash screen initially (if we are running through web start)
        // so the user knows something is happening
        ImageIcon splash;
        URL url = ClassLoaderSingleton.getClassLoader().getResource(imagePath);
        if (url == null) {
            // use a default
            splash = new ImageIcon( new BufferedImage( 300, 300, BufferedImage.TYPE_INT_RGB ) );
        } else  {
            splash = new ImageIcon( url );
        }
        return new SplashScreen( splash, null, waitMillis );
    }


    /**
     * This method is useful for turning Applets into applications.
     * For thread safety, this method should be invoked from the event-dispatching thread.
     * @param applet the applet to show
     * @return frame containing the applet.
     */
    public static JFrame showApplet(final JApplet applet) {
        JFrame baseFrame = new JFrame();
        /* not needed since java 1.6? */
        baseFrame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        baseFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed( WindowEvent e ) {
                System.exit( 0 );
            }
        });

        baseFrame.setContentPane(applet.getContentPane());

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (2.0 * d.getHeight()  / 3.0) ;
        int width = (int) Math.min(height * 1.5, 2.0 * d.getWidth() / 3);
        baseFrame.setLocation( (d.width - width) >> 2, (d.height - height) >> 2 );

        Dimension dim = applet.getSize();
        if (dim.width == 0) {
            baseFrame.setSize( width, height);
        } else {
            baseFrame.setSize( applet.getSize() );
        }
        applet.init();
        baseFrame.setTitle(applet.getName());
        baseFrame.repaint();
        baseFrame.setVisible( true );

        applet.start();
        return baseFrame;
    }

    /**
     * Paint with specified texture.
     */
    public static void paintComponentWithTexture(ImageIcon texture, Component c, Graphics g) {
        if (texture == null) {
            System.out.println( "warning no texture to tile with" ); // NON-NLS
            return;
        }
        Dimension size = c.getSize();

        int textureWidth = texture.getIconWidth();
        int textureHeight = texture.getIconHeight();
        assert textureWidth > 0 && textureHeight > 0;

        g.setColor(c.getBackground());
        g.fillRect(0,0,size.width, size.height);
        for (int row=0; row<size.height; row+=textureHeight) {
            for (int col=0; col<size.width; col+=textureWidth) {
                texture.paintIcon(c, g, col, row);
            }
        }
    }

    public static void saveSnapshot(JComponent component, String directory) {

        JFileChooser chooser = FileChooserUtil.getFileChooser();
        chooser.setCurrentDirectory( new File( directory ) );
        int state = chooser.showSaveDialog( null );
        File file = chooser.getSelectedFile();
        if ( file != null && state == JFileChooser.APPROVE_OPTION ) {

            BufferedImage img = getSnapshot(component);
            ImageUtil.saveAsImage(file.getAbsolutePath(), img, ImageUtil.ImageType.PNG);
        }
    }

    public static BufferedImage getSnapshot(JComponent component) {
        BufferedImage img =
            (BufferedImage) component.createImage(component.getWidth(), component.getHeight());
        component.paint(img.createGraphics());
        return img;
    }

    /**
     * Get the suffix of a file name.
     * The part after the "." typically used by FileFilters.
     * @return the file suffix.
    */
    public static String getFileSuffix(File f) {
        String s = f.getPath(), suffix = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length()-1)  {
            suffix = s.substring(i+1).toLowerCase();
        }
        return suffix;
    }

    /** @return true if running through webstart */
    public static boolean hasBasicService() {
        return getBasicService() != null;
    }

    /**
     * @return the basic jnlp service or null if it is not available.
     */
    public static BasicService getBasicService() {
        if (basicService_ == null) {
            try {
                basicService_ = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
            }
            catch (Exception e) {
                System.out.println("Not running through webstart: "+e.getMessage());  // NON-NLS
                return null;
            }
            catch (NoClassDefFoundError ncde) {
                System.out.println("jnlp BasicService not available: "+ncde.getMessage()); // NON-NLS
                return null;
            }
        }
        return basicService_;
    }
}
