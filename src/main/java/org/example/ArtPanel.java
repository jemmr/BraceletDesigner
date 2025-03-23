package org.example;
import synesketch.emotion.SynesthetiatorEmotionSemantic;
import synesketch.gui.EmpathyPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class ArtPanel extends JPanel implements PropertyChangeListener {
    private JFrame frame;
    private JPanel content;
    private EmpathyPanel ep;
    private BufferedImage image;
    private ImgProcessor imgProcessor = new ImgProcessor();
    private SimAnnealer simAnnealer = new SimAnnealer();

    public ArtPanel( JFrame f ) {
        super();
        frame = f;
        setPreferredSize( new Dimension( 500, 500 ) );
        content = new JPanel();
        content.setBackground( new Color( 16, 8, 32 ));
        content.setPreferredSize( new Dimension( 500, 500 ) );
        add( content );
        try {
            ep = new EmpathyPanel(
                    500,
                    "SynesketchPApplet",
                    "synesketch.emotion.SynesthetiatorEmotionSemantic"
            );
            if ( ep == null ) throw new RuntimeException( "Unknown EmpathyPanel error." );
        } catch ( Exception e ) {
            System.out.println("Synesthetiator failed to load.");
            if ( e.getCause() != null ) {
                System.out.println(e.getCause().toString());
            } else {
                throw new RuntimeException( e );
            }
        }
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt ) {
        if ( Objects.equals( evt.getPropertyName(), "text" ) ) {
            String text = evt.getNewValue().toString();
            System.out.print("  received at art: ");
            System.out.println(text);
            if ( ep != null ) {
                try {
                    ep.fireSynesthesiator(text);
                    removeAll();
                    add(ep);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else if ( Objects.equals( evt.getPropertyName(), "image" ) ) {
            System.out.println("  image received at art.");
            try {
                BufferedImage img = (BufferedImage) evt.getNewValue();
                content.removeAll();
                content.add( new JLabel( new ImageIcon( img ) ) );
                removeAll();
                add( content );
                image = img;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ( Objects.equals( evt.getPropertyName(), "start" ) ) {
            System.out.print("  beginning image processing. image type is ");
            System.out.println( evt.getNewValue().toString() );
            if ( evt.getNewValue() == InputType.processing ) {
                try {
                    int w = frame.getWidth(), h = frame.getHeight();
                    if ( w > 500 ) w = 500;
                    if ( h > 500 ) h = 500;
                    image = new Robot().createScreenCapture( new Rectangle(
                            getLocationOnScreen().x,
                            getLocationOnScreen().y,
                            w,
                            h ) );
                } catch (AWTException ex) {
                    return;
                }
                content.removeAll();
                content.add( new JLabel( new ImageIcon( image ) ) );
                removeAll();
                add( content );
                revalidate();
                repaint();
                frame.revalidate();
                frame.repaint();
            }
            BufferedImage oldImage = image;
            image = imgProcessor.process( oldImage );
            content.removeAll();
            content.add( new JLabel( new ImageIcon( image ) ) );
            removeAll();
            add( content );
            simAnnealer.setArgs( image, imgProcessor.getColorSet() );
            firePropertyChange("simann", null, simAnnealer );
        }
        revalidate();
        repaint();
        frame.revalidate();
        frame.repaint();
    }
//
//    public ArtPanel() throws Exception {
//    }
}