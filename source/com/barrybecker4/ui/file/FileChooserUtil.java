/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.file;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.util.FileUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Miscellaneous commonly used file chooser related utility methods.
 * @author Barry Becker
 */
public final class FileChooserUtil {

    /**
     * For opening files.
     * don't create this here or applets using this class will have a security exception
     * instead we create a singleton when needed.
     */
    private static JFileChooser chooser_ = null;

    /**
     * cannot instantiate static class.
     */
    private FileChooserUtil() {}

    /**
     * @return a generic file chooser.
     */
    public static JFileChooser getFileChooser() {
        return getFileChooser(null);
    }

    /**
     * get a singleton file chooser.
     * @param filter optional file filter
     * @return file chooser with specified filter.
     */
    public static JFileChooser getFileChooser(FileFilter filter) {
        if (chooser_ == null) {
            chooser_ = new JFileChooser();
        }
        chooser_.setCurrentDirectory( new File( FileUtil.getHomeDir() ) );
        chooser_.setFileFilter(filter);
        return chooser_;
    }


    public static File getSelectedFileToSave(String extension, File defaultDir) {
        return getSelectedFile(AppContext.getLabel("SAVE"), extension, defaultDir);
    }

    public static File getSelectedFileToOpen(String extension, File defaultDir) {
         return getSelectedFile(AppContext.getLabel("OPEN"), extension, defaultDir);
    }

    private static File getSelectedFile(String action, String extension, File defaultDir) {

        JFileChooser chooser = FileChooserUtil.getFileChooser(new ExtensionFileFilter(extension));
         chooser.setDialogTitle(action);
         chooser.setApproveButtonText(action);
         chooser.setApproveButtonToolTipText(AppContext.getLabel("ACTION_FILE", new Object[] {action}));

         if (defaultDir != null) {
             chooser.setCurrentDirectory(defaultDir);
         }

         int state = chooser.showOpenDialog( null );
         File file = chooser.getSelectedFile();
         if ( file != null && state == JFileChooser.APPROVE_OPTION )  {
             return file;
         }
         else return null;
    }
}