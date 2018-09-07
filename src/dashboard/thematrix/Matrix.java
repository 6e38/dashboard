/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.thematrix;

import dashboard.Component;
import dashboard.Data;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Matrix implements CollisionAvoidance, Component
{
  private static final Color[] ColorMap = {
    new Color(0xff00ff00),
    new Color(0xff00ee00),
    new Color(0xff00dd00),
    new Color(0xff00cc00),
    new Color(0xff00bb00),
    new Color(0xff00aa00),
    new Color(0xff009900),
    new Color(0xff008800),
    new Color(0xff007700),
    new Color(0xff006600),
    new Color(0xffee0000),
  };

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

  public Matrix(String specialFile)
  {
    this.specialFile = specialFile;
  }

  @Override
  public String getName()
  {
    return "matrix";
  }

  @Override
  public void surfaceSized(int width, int height, Graphics g)
  {
    this.width = width;
    this.height = height;

    mainFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    g.setFont(mainFont);
    FontMetrics metrics = g.getFontMetrics();
    charWidth = metrics.charWidth('A');
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
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);

    int lastColor = Model.Green;
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

  @Override
  public void update(Data d)
  {
    data = d;

    for (int i = 0; i < drop.length; i++)
    {
      if (drop[i].update())
      {
        drop[i] = new Drop(cols, rows, model, this, null);
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
}

