/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

import javax.swing.JFrame;

public class Engine extends Thread
{
  private JFrame frame;
  private boolean running;

  public Engine(JFrame f)
  {
    frame = f;
    running = false;
  }

  public void run()
  {
    running = true;

    while (running)
    {
      frame.repaint();

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

