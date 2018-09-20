/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.starfield;

import java.awt.Graphics2D;

class Star
{
  private double width;
  private double height;
  private double size;
  private double x;
  private double y;
  private double rate;
  private long then;

  Star()
  {
    width = 0;
    height = 0;

    then = System.currentTimeMillis();
  }

  void surfaceSized(double width, double height)
  {
    this.width = width;
    this.height = height;
    reset();
  }

  void reset()
  {
    size = 1;

    double r = Math.random();

    if (r < 0.01)
    {
      size = 3;
    }
    else if (r < 0.10)
    {
      size = 2;
    }

    x = width + size;
    y = Math.random() * height;
    rate = Math.random() * 100 + 10;
  }

  synchronized void update()
  {
    long now = System.currentTimeMillis();

    double dt = (double)(now - then) / 1000;

    x -= dt * rate;

    if (x < -size * 2)
    {
      reset();
    }

    then = now;
  }

  synchronized void draw(Graphics2D g)
  {
    g.fillRect((int)x, (int)y, (int)size, (int)size);
  }
}

