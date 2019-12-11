import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BasicGUI {
    final int DEF_WIDTH = 400;
    final int DEF_HEIGHT = 400;
    JFrame frame;

    public BasicGUI(String name, int width, int height, String[][] menu, JPanel[] panels) {
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        JMenuBar menuBar = new JMenuBar();
        for (String[] item : menu) {
            menuBar.add(createJMenu(item[0], subArray(item, 1, item.length)));
        }

        JPanel panelOfPanels = new JPanel();
        for (JPanel p : panels) {
            panelOfPanels.add(p);
        }

        frame.getContentPane().add(BorderLayout.CENTER, panelOfPanels);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.setVisible(true);
    }

    public JMenu createJMenu(String headerName, String... itemNames) {
        JMenu menu = new JMenu(headerName);
        for (String s : itemNames) {
            menu.add(new JMenuItem(s));
        }
        return menu;
    }

    public <T> T[] subArray(T[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end);
    }

    public void show() {
        frame.setVisible(true);
    }
}
