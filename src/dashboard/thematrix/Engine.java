
package dashboard.thematrix;

public class Engine extends Thread
{
  private Matrix matrix;
  private boolean running;

  public Engine(Matrix m)
  {
    matrix = m;
    running = false;
  }

  public void run()
  {
    running = true;

    while (running)
    {
      matrix.repaint();

      try
      {
        sleep(50);
      }
      catch (InterruptedException e)
      {
        running = false;
      }
    }
  }

  public void end()
  {
    running = false;
  }
}

