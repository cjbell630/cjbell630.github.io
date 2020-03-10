public class Main {
    public static void main(String[] args) {
        GraphicsTest gt = new GraphicsTest(600, 900);
        Thread rendering = new Thread(gt);
        GameHandler gh = new GameHandler(gt);
        Thread game = new Thread(gh);
        game.start();
        rendering.start();
    }
}
