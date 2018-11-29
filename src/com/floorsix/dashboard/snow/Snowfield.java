/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.snow;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Snowfield implements Component
{
  public static final String Name = "snow";

  private Palette palette;

  private int width;
  private int height;
  private Snow[] snow;
  private Color snowColor;

  public Snowfield(Data data)
  {
    paletteChanged(data.getPalette());

    snow = new Snow[1000];

    for (int i = 0; i < snow.length; ++i)
    {
      snow[i] = new Snow();
    }

    snowColor = new Color(255, 255, 255);
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

    for (Snow s : snow)
    {
      s.surfaceSized(width, height);
    }
  }

  @Override
  public void update()
  {
    for (Snow s : snow)
    {
      s.update();
    }
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(palette.background);
    g.fillRect(0, 0, width, height);

    g.setColor(snowColor);

    for (Snow s : snow)
    {
      s.draw(g);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

