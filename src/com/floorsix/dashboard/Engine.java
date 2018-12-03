/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

public class Engine extends Thread
{
  private Surface surface;
  private boolean running;
  private Profiler profiler;

  public Engine(Surface s)
  {
    surface = s;
    running = false;
    profiler = new Profiler("Updates", 1000);
  }

  public void run()
  {
    running = true;

    profiler.restart();

    while (running)
    {
      surface.update();

      try
      {
        sleep(2);
      }
      catch (InterruptedException e)
      {
        running = false;
      }

      profiler.update();
    }
  }

  public void end()
  {
    running = false;
  }
}

