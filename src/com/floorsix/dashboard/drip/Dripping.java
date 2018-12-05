/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.drip;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class Dripping implements Component
{
  public static final String Name = "drip";

  private ArrayList<Drip> drips;
  private int width;
  private int height;
  private int x1;
  private int x2;
  private int y;
  private long lastTime;

  private Palette palette;

  private Data data;

  public Dripping(Data data)
  {
    this.data = data;

    paletteChanged(data.getPalette());

    drips = new ArrayList<Drip>();

    for (int i = 0; i < 150; ++i)
    {
      drips.add(new Drip());
    }

    height = 0;
    x1 = 0;
    x2 = 0;
    y = 0;

    lastTime = System.currentTimeMillis();
  }

  @Override
  public String getName()
  {
    return Name;
  }

  @Override
  public void surfaceSized(int width, int height, Graphics g)
  {
    this.width = width;
    this.height = height;

    int w = 400; // magic numbers!
    int h = 300;

    x1 = 0;
    x2 = width;
    y = 0;

    for (Drip drip : drips)
    {
      drip.start(x1, x2, y);
    }
  }

  @Override
  public void update()
  {
    long currentTime = System.currentTimeMillis();
    float dt = (float)(currentTime - lastTime) / 1000f;
    lastTime = currentTime;

    for (Drip drip : drips)
    {
      drip.update(dt);

      if (drip.e.y > height)
      {
        drip.start(x1, x2, y);
      }
    }
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(palette.background);
    g.fillRect(0, 0, width, height);

    g.setColor(palette.primary);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (Drip drip : drips)
    {
      g.draw(drip.e);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

