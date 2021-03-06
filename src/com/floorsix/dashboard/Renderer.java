/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

public class Renderer extends Thread
{
  private Surface surface;
  private boolean running;

  public Renderer(Surface s)
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
        sleep(16); // Attempt 62.5 fps
      }
      catch (InterruptedException e)
      {
        System.out.println("Interrupted");
      }
    }
  }

  public void end()
  {
    running = false;
  }
}

