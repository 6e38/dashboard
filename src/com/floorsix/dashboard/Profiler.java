/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

public class Profiler
{
  private long lastTime;
  private double n;
  private double threshold;
  private String name;

  Profiler(String name, double threshold)
  {
    this.threshold = threshold;
    this.name = name;
    restart();
  }

  void restart()
  {
    n = 0;
    lastTime = System.currentTimeMillis();
  }

  void update()
  {
    if (++n >= threshold)
    {
      long currentTime = System.currentTimeMillis();
      double dt = (double)(currentTime - lastTime) / 1000.0;
      double xps = n / dt;
      System.out.println(String.format("%s per second: %.1f, %.0f/%d", name, xps, n, currentTime - lastTime));
      lastTime = currentTime;
      n = 0;
    }
  }
}

