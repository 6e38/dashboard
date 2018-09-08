/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

public class Engine extends Thread
{
  private Surface surface;
  private boolean running;

  public Engine(Surface s)
  {
    surface = s;
    running = false;
  }

  public void run()
  {
    running = true;

    while (running)
    {
      surface.repaint();

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
