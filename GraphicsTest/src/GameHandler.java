import java.awt.*;
import java.util.Arrays;

//Maybe package everything to make stuff easier

public class GameHandler implements Runnable {
    Color[][] screen;
    GraphicsTest gt;

    //Whatever
    public GameHandler() {
        screen = new Color[24][36];
        gt = new GraphicsTest(500, 500);
    }

    public GameHandler(GraphicsTest graphicsTest) {
        screen = new Color[24][36];
        gt = graphicsTest;
    }

    //keys do stuff whatever whatever
    public Color[][] getScreen() {
        return screen;
    }

    public void graphicsTester() throws InterruptedException {
        for (Color[] c : screen) {
            Arrays.fill(c, Color.RED);
        }
        /*for(int i=0; i<1000; i++){
            screen[rand(0, screen.length-1)][rand(0, screen[0].length-1)] = Color.GREEN;
            gt.setScreen(screen);
            Thread.sleep(5);
        }*/
        gt.setScreen(Assets.Sprites.LINK.applyPalette(Assets.Palettes.LINK_PALETTE));
    }

    /**
     * gets a random number from ant integer range, inclusive on both endpoints
     *
     * @param a, one endpoint to get a random number from
     * @param b, another endpoint to get a random number from
     * @return the int value of ( (the range of a to b) + 1 ) times (random double from 0 to 1) + the smaller # of a and b
     */
    public int rand(int a, int b) {
        return (int) (Math.random() * (Math.abs(a - b) + 1)) + Math.min(a, b);
    }

    @Override
    public void run() {
        try {
            this.graphicsTester();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
