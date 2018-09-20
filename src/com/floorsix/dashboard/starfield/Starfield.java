/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.starfield;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Starfield implements Component
{
  public static final String Name = "starfield";

  private Palette palette;

  private int width;
  private int height;
  private Star[] stars;

  public Starfield(Data data)
  {
    paletteChanged(data.getPalette());

    stars = new Star[1000];

    for (int i = 0; i < stars.length; ++i)
    {
      stars[i] = new Star();
    }
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

    for (Star star : stars)
    {
      star.surfaceSized(width, height);
    }
  }

  @Override
  public void update()
  {
    for (Star star : stars)
    {
      star.update();
    }
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(palette.background);
    g.fillRect(0, 0, width, height);

    g.setColor(palette.primary);

    for (Star star : stars)
    {
      star.draw(g);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

