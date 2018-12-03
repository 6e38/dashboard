/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.thematrix;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Matrix implements CollisionAvoidance, Component
{
  public static final String Name = "matrix";

  private Color[] ColorMap;

  private Palette palette;

  private boolean hasSurface;
  private Data data;
  private String specialFile;
  private int width;
  private int height;
  private int charWidth;
  private int charHeight;
  private int offsetX;
  private int offsetY;
  private int rows;
  private int cols;

  private Model model;
  private Drop[] drop;

  private Font mainFont;

  public Matrix(Data data, String specialFile)
  {
    hasSurface = false;

    this.data = data;
    this.specialFile = specialFile;

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

    mainFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    g.setFont(mainFont);
    FontMetrics metrics = g.getFontMetrics();
    charWidth = metrics.charWidth('\u4e00');
    charHeight = metrics.getHeight();

    cols = width / charWidth;
    rows = height / charHeight;
    offsetX = (width % charWidth) / 2;
    offsetY = (height % charHeight) / 2 + metrics.getAscent();

    model = new Model(cols, rows);

    drop = new Drop[cols / 3];
    for (int i = 0; i < drop.length; i++)
    {
      drop[i] = new Drop(cols, rows, model, this, specialFile);
    }

    hasSurface = true;
  }

  @Override
  public void draw(Graphics2D g)
  {
    if (hasSurface)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, width, height);

      int lastColor = Model.Color;
      g.setColor(ColorMap[lastColor]);
      g.setFont(mainFont);

      char[][] data = model.getData();
      int[][] color = model.getColors();
      int cols = data[0].length;
      int rows = data.length;

      for (int y = 0; y < rows; y++)
      {
        for (int x = 0; x < cols; x++)
        {
          if (color[y][x] != lastColor)
          {
            lastColor = color[y][x];
            g.setColor(ColorMap[lastColor]);
          }

          g.drawChars(data[y], x, 1, x * charWidth + offsetX, y * charHeight + offsetY);
        }
      }
    }
  }

  @Override
  public void update()
  {
    if (hasSurface)
    {
      for (int i = 0; i < drop.length; i++)
      {
        if (drop[i].update())
        {
          drop[i] = new Drop(cols, rows, model, this, null);
        }
      }
    }
  }

  @Override
  public boolean collides(int x, int y1, int y2)
  {
    for (Drop d : drop)
    {
      if (d != null && d.collides(x, y1, y2))
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;

    ColorMap = new Color[Model.MaxColors];

    int red = palette.primary.getRed();
    int green = palette.primary.getGreen();
    int blue = palette.primary.getBlue();

    final int divisions = 16;
    int rsub = red > 0 ? red / divisions : 0;
    int gsub = green > 0 ? green / divisions : 0;
    int bsub = blue > 0 ? blue / divisions : 0;

    for (int i = 0; i < Model.MaxShades; ++i)
    {
      ColorMap[i] = new Color(red - rsub * i, green - gsub * i, blue - bsub *i);
    }

    ColorMap[Model.MaxShades] = palette.secondary;
  }
}

