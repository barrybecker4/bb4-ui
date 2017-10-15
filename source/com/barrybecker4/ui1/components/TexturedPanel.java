// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.components;

import com.barrybecker4.ui1.util.GUIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * A panel with a textured background.
 * The background gets tiled with the image that is passed in.
 */
public class TexturedPanel extends JPanel {

    private static final long serialVersionUID = 0L;

    private ImageIcon texture = null;

    public TexturedPanel( ImageIcon texture ) {
        setTexture(texture);
    }

    public void setTexture( ImageIcon texture ) {
        this.texture = texture;
    }

    @Override
    public void paintComponent(Graphics g) {
        GUIUtil.paintComponentWithTexture(texture, this, g);
    }

}