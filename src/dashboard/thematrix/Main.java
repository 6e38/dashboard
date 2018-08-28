
package dashboard.thematrix;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main
{
  public static void main(String args[])
  {
    String file = "special.txt";

    if (args.length > 0)
    {
      file = args[0];
    }

    new Main(file);
  }

  private String specialFile;

  public Main(String theSpecialFile)
  {
    specialFile = theSpecialFile;

    SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run()
        {
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          GraphicsDevice[] gd = ge.getScreenDevices();

          JFrame frame = new JFrame("");

          frame.setUndecorated(true);
          gd[0].setFullScreenWindow(frame);

          Matrix matrix = new Matrix(frame, specialFile);
          frame.getContentPane().add(matrix);
          frame.getContentPane().setCursor(BlankCursor.getCursor());
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);

          Thread thread = new Engine(matrix);
          thread.start();
        }
    });
  }
}

