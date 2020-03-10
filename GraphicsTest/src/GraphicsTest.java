import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class GraphicsTest extends Frame implements Runnable {
    static Color[][] screen;
    static int width;
    static int height;
    static int xScale;
    static int yScale;
    static final int xPx = 24;
    static final int yPx = 36;

    public GraphicsTest(int screenWidth, int screenHeight) {
        super("Graphics Test");
        width = screenWidth;
        height = screenHeight;
        xScale = (int) ((double) width / (double) xPx);
        yScale = (int) ((double) height / (double) yPx);
        System.out.println(xScale + " " + yScale);
        prepareGUI();
        screen = new Color[yPx][xPx];
    }

    private void prepareGUI() {
        setSize(width, height);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int yInOriginal = 0; yInOriginal < screen.length; yInOriginal++) { //builds t -> b
            for (int yScaleIterator = 0; yScaleIterator < yScale; yScaleIterator++) {
                for (int xInOriginal = 0; xInOriginal < screen[0].length; xInOriginal++) { //builds l -> r
                    for (int xScaleIterator = 0; xScaleIterator < xScale; xScaleIterator++) { //builds one px scale times
                        bi.setRGB((xInOriginal * xScale) + xScaleIterator, (yInOriginal * yScale) + yScaleIterator, screen[yInOriginal][xInOriginal].getRGB());
                    }
                }
            }
        }
        g2.drawImage(bi, 0, 0, this);

    }

    public void update(Graphics g) {
        paint(g);
    }

    public void setScreen(Color[][] newScreen) {
        screen = newScreen;
        this.repaint();
    }

    public BufferedImage scale2(BufferedImage before, double xFactor, double yFactor) {
        int w = before.getWidth();
        int h = before.getHeight();
        // Create a new image of the proper size
        int w2 = (int) (w * xFactor);
        int h2 = (int) (h * yFactor);
        BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_RGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(xFactor, yFactor);
        AffineTransformOp scaleOp
                = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);

        Graphics2D g2 = (Graphics2D) after.getGraphics();
        // Here, you may draw anything you want into the new image, but we're
        // drawing a scaled version of the original image.
        g2.drawImage(before, scaleOp, 0, 0);
        g2.dispose();
        return after;
    }

    @Override
    public void run() {
        this.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.repaint();
    }
}
