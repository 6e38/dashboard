/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.drip;

import dashboard.Component;
import dashboard.Data;
import dashboard.Palette;
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

    x1 = width / 2 - w / 2;
    x2 = x1 + w;
    y = height / 2 + h / 2;

    for (Drip drip : drips)
    {
      drip.start(x1, x2, y);
    }
  }

  @Override
  public void update()
  {
    for (Drip drip : drips)
    {
      drip.update(0.050f); // magic number!

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

