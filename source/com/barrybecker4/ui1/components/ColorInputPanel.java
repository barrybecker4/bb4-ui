// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel that allows the user to enter a color for something using the standard color chooser.
 *
 * @author Barry Becker
 */
public class ColorInputPanel extends JPanel
{

    private ActionListener actionListener_;

    /**
     * @param label  the label for this panel entry
     * @param toolTip  the tooltip for the color button giving the user instructions.
     * @param colorButton  the button to click to bring up the chooser. This button's background is maintains the color.
     */
    public ColorInputPanel(final String label, String toolTip, final JButton colorButton)
    {
        this.setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        this.setAlignmentX( Component.LEFT_ALIGNMENT );

        JLabel colorLabel = new JLabel( label );
        this.add( colorLabel );

        colorButton.setToolTipText( toolTip );

        colorButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed( ActionEvent evt ) {
                    Object source = evt.getSource();
                    if ( source == colorButton ) {
                        Color selectedColor = JColorChooser.showDialog( colorButton, label, colorButton.getBackground() );
                        colorButton.setBackground( selectedColor );
                        if (actionListener_ !=null) {
                            actionListener_.actionPerformed(evt);
                        }
                    }
                }
            } );
        this.add(colorButton);
    }

    /**
     * an alternative constructor which gives a hook for calling back to the client when
     * the color has actually been selected
     */
    public ColorInputPanel(final String label, String toolTip, final JButton colorButton, ActionListener actionListener) {
          this(label, toolTip, colorButton);
          actionListener_ = actionListener;
    }
}
