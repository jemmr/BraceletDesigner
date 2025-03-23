package org.example;

import org.apache.jena.tdb.store.Hash;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.*;

public class ImgProcessor {
    private BufferedImage image;
    private final Color[] colorSet = new Color[4];

    public ImgProcessor() {
        super();
    }

    public void setImage( BufferedImage image ) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color[] getColorSet() {
        return colorSet;
    }

    public BufferedImage scale(BufferedImage image, int width, int height ) {
        int w = height*image.getWidth()/image.getHeight(), x = 0;
        System.out.println( STR."    \{w} -> \{width}, \{height}" );
        Image scaled = image.getScaledInstance( w, height, Image.SCALE_FAST );
        BufferedImage newImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = newImage.createGraphics();
        while ( x < width ) {
            g.drawImage( scaled, x, 0, null );
            x += w;
        }
        g.dispose();
        return newImage;
    }

    public void medianCut( ArrayList< Color > in, ArrayList< Color > out1, ArrayList< Color > out2 ) {
        int i, maxRange, split;
        int minR = 256, maxR = 0, minG = 256, maxG = 0, minB = 256, maxB = 256;
        for ( Color c : in ) {
            if ( c.getRed() < minR ) minR = c.getRed();
            if ( c.getRed() > maxR ) maxR = c.getRed();
            if ( c.getGreen() < minG ) minG = c.getGreen();
            if ( c.getGreen() > maxG ) maxG = c.getGreen();
            if ( c.getBlue() < minB ) minB = c.getBlue();
            if ( c.getBlue() > maxB ) maxB = c.getBlue();
        }
        i = 0;
        maxRange = maxR-minR;
        if ( maxG - minG > maxRange ) {
            maxRange = maxG - minG;
            i = 1;
        }
        if ( maxB - minB > maxRange ) {
            i = 2;
        }
        final int channel = i;
        in.sort( Comparator.comparingInt( ( Color c ) -> getChannel( c, channel ) ) );
        split = getChannel( in.get(in.size() / 2), channel );
        split += getChannel( in.get((in.size() + 1) / 2), channel );
        split = split / 2;
        for ( Color c : in ) {
            if ( getChannel( c, channel) < split ) out1.add( c );
            else out2.add( c );
        }
    }

    public int getChannel( Color c, int i ) {
        if ( i == 0 ) return c.getRed();
        if ( i == 1 ) return c.getGreen();
        return c.getBlue();
    }

    public BufferedImage process( BufferedImage image ) {
        BufferedImage before = scale( image, 500, 100 );

        ArrayList< Color > colors = new ArrayList< Color >();
        for ( int x = 0; x < before.getWidth(); x++ ) {
            for ( int y = 0; y < before.getHeight(); y++ ) {
                Color c = new Color( before.getRGB( x, y ) );
                colors.add( c );
            }
        }

        ArrayList< Color > split0 = new ArrayList< Color >(),
                split1 = new ArrayList< Color >();
        medianCut( colors, split0, split1 );
        ArrayList< Color > split00 = new ArrayList< Color >(),
                split01 = new ArrayList< Color >(),
                split10 = new ArrayList< Color >(),
                split11 = new ArrayList< Color >();
        medianCut( split0, split00, split01);
        medianCut( split1, split10, split11);
        HashSet< Color > split00Set = new HashSet< Color >(),
                split01Set = new HashSet< Color >(),
                split10Set = new HashSet< Color >(),
                split11Set = new HashSet< Color >();
        int[] mean = new int[]{ 0, 0, 0 };
        colorSet[0] = createLookup( split00, split00Set );
        colorSet[1] = createLookup( split01, split01Set );
        colorSet[2] = createLookup( split10, split10Set );
        colorSet[3] = createLookup( split11, split11Set );

        int c00 = colorSet[0].getRGB();
        int c01 = colorSet[1].getRGB();
        int c10 = colorSet[2].getRGB();
        int c11 = colorSet[3].getRGB();

        BufferedImage output = new BufferedImage( before.getWidth(), before.getHeight(), BufferedImage.TYPE_INT_RGB );
        for ( int x = 0; x < before.getWidth(); x++ ) {
            for ( int y = 0; y < before.getHeight(); y++ ) {
                Color pixel = new Color( before.getRGB( x, y ) );
                int newpixel;
                if ( split00Set.contains( pixel ) ) newpixel = c00;
                else if ( split01Set.contains( pixel ) ) newpixel = c01;
                else if ( split10Set.contains( pixel ) ) newpixel = c10;
                else newpixel = c11;
                output.setRGB( x, y,  newpixel );
            }
        }

        setImage( output );
        return output;
    }

    private Color createLookup( ArrayList< Color > colorList, HashSet< Color > colorSet ) {
        int r = 0, g = 0, b = 0;
        for (Color c : colorList) {
            r += c.getRed();
            g += c.getGreen();
            b += c.getBlue();
            colorSet.add(c);
        }
        r = r / colorList.size();
        g = g / colorList.size();
        b = b / colorList.size();
        System.out.println( STR."  \{r},\{g},\{b}");
        return new Color( r, g, b );
    }
}
