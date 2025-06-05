package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.File;

public class InputPanel extends JPanel {
    InputPanel inputPanel = this;
    JTextField textField = new JTextField( 24 );
    JButton uploadButton = new JButton();
    JButton continueButton = new JButton();
    InputType type = InputType.none;

    public InputPanel() {
        super();
        Action textAction = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
//                System.out.print( "input: " );
//                System.out.println( textField.getText() );
                type = InputType.processing;
                inputPanel.firePropertyChange( "text", null, textField.getText() );
            }
        };
        Action uploadAction = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                if ( f != null ) {
                    String filename = f.getAbsolutePath();
//                    System.out.print( "image chosen in input: ");
//                    System.out.println( filename );
                    try {
                        BufferedImage img = ImageIO.read(new File( filename ));//get the image from file chooser and scale it to match JLabel size
                        type = InputType.upload;
                        inputPanel.firePropertyChange("image", null, img);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        Action continueAction = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( type != InputType.none ) {
                    inputPanel.firePropertyChange("start", null, type);
                    if ( type == InputType.processing ) type = InputType.upload;
                }
//                else System.out.println( "- no image chosen" );
            }
        };
//        textField.setPreferredSize( new Dimension( 450, 30 ) );
        textField.addActionListener( textAction );
        uploadButton.setText( "Upload...");
        uploadButton.setPreferredSize( new Dimension( 87, 30 ) );
        uploadButton.addActionListener( uploadAction );
        continueButton.setText( "Continue");
        continueButton.setPreferredSize( new Dimension( 89, 30 ) );
        continueButton.addActionListener( continueAction );
        add(textField);
        add(uploadButton);
        add(continueButton);

        revalidate();
        repaint();
    }
}
