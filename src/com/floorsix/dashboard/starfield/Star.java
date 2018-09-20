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
  private boolean named;
  private String name;

  Star()
  {
    width = 0;
    height = 0;

    then = System.currentTimeMillis();

    named = false;
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

    named = false;

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

    x = width + size;
    y = Math.random() * height;

    if (Math.random() < 0.0001)
    {
      final String[] Names = {
        "proxima centauri",
        "polaris",
        "merope",
        "izar",
        "gacrux",
        "enif",
        "diphda",
        "cursa",
        "bellatrix",
        "alula borealis",
        "fulu",
        "ogma",
        "phact",
        "sarin",
        "vega",
        "yed prior",
        "zhang",
      };
      named = true;
      name = Names[(int)(Math.random() * (double)Names.length)];

      if (y < 50) // prevent named stars from running off the top
      {
        y += 50;
      }

      while (rate > 50) // slow down named stars
      {
        rate -= 40;
      }
    }
  }

  synchronized void update()
  {
    long now = System.currentTimeMillis();

    double dt = (double)(now - then) / 1000;

    x -= dt * rate;

    if (x < -size * 2)
    {
      if (!named || x < -400)
      {
        reset();
      }
    }

    then = now;
  }

  synchronized void draw(Graphics2D g)
  {
    g.fillRect((int)x, (int)y, (int)size, (int)size);

    if (named)
    {
      int x2 = (int)x + 18;
      int y2 = (int)y - 18;

      g.drawLine((int)x + 2, (int)y - 2, x2, y2);
      g.drawString(name, x2 + 2, y2 + 2);
    }
  }
}

