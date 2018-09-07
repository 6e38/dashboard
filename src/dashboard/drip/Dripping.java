/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.drip;

import dashboard.Component;
import dashboard.Data;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class Dripping implements Component
{
  private ArrayList<Drip> drips;
  private int width;
  private int height;
  private int x1;
  private int x2;
  private int y;

  private Color primaryColor;
  private Color secondaryColor;
  private Color backgroundColor;

  private Data data;

  public Dripping(Data data)
  {
    this.data = data;

    colorsChanged(data.getColors());

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
    return "drip";
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
    g.setColor(backgroundColor);
    g.fillRect(0, 0, width, height);

    g.setColor(primaryColor);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (Drip drip : drips)
    {
      g.draw(drip.e);
    }
  }

  @Override
  public void colorsChanged(int[] colors)
  {
    primaryColor = new Color(colors[0], true);
    secondaryColor = new Color(colors[1], true);
    backgroundColor = new Color(colors[2], true);
  }
}

