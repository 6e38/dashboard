/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

import dashboard.Component;
import dashboard.Data;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Background implements Component
{
  public static final String Name = "blank";

  private int width;
  private int height;

  public Background()
  {
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
  }

  @Override
  public void update(Data d)
  {
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);
  }
}

