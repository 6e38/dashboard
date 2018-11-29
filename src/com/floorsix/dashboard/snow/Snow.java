/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.snow;

import java.awt.Graphics2D;

class Snow
{
  private double width;
  private double height;
  private double size;
  private double x;
  private double y;
  private double rate;
  private long then;

  Snow()
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
    rate = Math.random() * 50 + 10;

    double r = Math.random();

    size = 1;

    if (r < 0.01)
    {
      size = 3;
    }
    else if (r < 0.10)
    {
      size = 2;
    }

    x = Math.random() * (width * 1.5) - (width * .25);
    y = -size;
  }

  synchronized void update()
  {
    long now = System.currentTimeMillis();

    double dt = (double)(now - then) / 1000;

    y += dt * rate;

    if (y > height + size * 2)
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

