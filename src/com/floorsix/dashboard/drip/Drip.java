/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.drip;

import java.awt.geom.Ellipse2D;

public class Drip
{
  Ellipse2D.Float e;
  private float vel;
  private static final float accel = 100;
  private float delay;
  private static final float diameter = 12;

  public Drip()
  {
    e = new Ellipse2D.Float(0, 0, diameter, diameter);
    vel = 0;
    delay = 0;
  }

  public void start(float x1, float x2, float y)
  {
    e.x = (float)Math.random() * (x2 - x1 - e.width) + x1;
    e.y = y - e.height * 0.80f;
    vel = 0;
    delay = (float)Math.random() * 4f; // Magic seconds number
  }

  public void update(float timeDelta)
  {
    if (delay < 0)
    {
      vel += accel * timeDelta;
      e.y += vel * timeDelta;
    }
    else
    {
      delay -= timeDelta;
    }
  }
}

