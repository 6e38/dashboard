/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

import java.awt.Graphics2D;
import java.awt.Graphics;

public class Background implements Component
{
  public static final String Name = "blank";

  private Palette palette;

  private int width;
  private int height;

  public Background(Data data)
  {
    paletteChanged(data.getPalette());
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
  public void update()
  {
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(palette.background);
    g.fillRect(0, 0, width, height);
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

