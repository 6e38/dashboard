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
  private double dx;
  private double sineMag;
  private double sineTime;
  private double sinePeriod;

  Snow()
  {
    width = 0;
    height = 0;

    dx = 0;
    sineMag = 2;
    sinePeriod = .5 + (Math.random() * .2 - .1);
    sineTime = Math.random() * Math.PI * 2;

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
    rate = Math.random() * 100 + 10;

    if (rate < 40)
    {
      size = 1;
    }
    else if (rate < 80)
    {
      size = 2;
    }
    else if (rate < 100)
    {
      size = 3;
    }
    else
    {
      size = 4;
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

    sineTime += dt;
    if (sineTime > 1 / sinePeriod)
    {
      sineTime -= 1 / sinePeriod;
    }
    dx = sineMag * Math.sin(sineTime * Math.PI * 2 * sinePeriod);

    then = now;
  }

  synchronized void draw(Graphics2D g)
  {
    g.fillRect((int)(x + dx), (int)y, (int)size, (int)size);
  }
}

