// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.ui.application.ApplicationFrame;
import com.barrybecker4.ui.components.SplashScreen;
import com.barrybecker4.ui.util.GUIUtil;
import com.barrybecker4.ui.util.Log;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * An app that tries to demonstrate the use of most of the UI components in this package.
 *
 * @author Barry Becker
 */
public class UberApp extends ApplicationFrame {

    public static final String IMAGE_ROOT = "com/barrybecker4/ui/uber/images/";   // NON-NLS

    public UberApp() {
        super("UberApp Demo");   // NON-NLS

    }

    protected void createUI() {
        AppContext.initialize("ENGLISH", Collections.singletonList("com.barrybecker4.ui.uber.message"), new Log());
        Icon image = GUIUtil.getIcon(IMAGE_ROOT + "pool_pennies_small.jpg");
        new SplashScreen(image, this, 2000);

        //JPanel mainPanel = new JPanel();
        JTabbedPane mainPanel = new JTabbedPane();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.addTab("Input Elements", new MainTexturePanel());
        mainPanel.addTab("Histogram", new HistogramTestPanel());
        mainPanel.addTab("Multi-Function", new MultiFunctionTestPanel());

        this.add(mainPanel);

        super.createUI();
    }

    public static void main(String[] args) throws Exception {
        new UberApp();
    }

}