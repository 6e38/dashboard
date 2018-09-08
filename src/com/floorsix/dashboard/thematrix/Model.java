/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.thematrix;

public class Model
{
  public static final int Color = 0;
  private static final int Shade1 = 1;
  private static final int Shade2 = 2;
  private static final int Shade3 = 3;
  private static final int Shade4 = 4;
  private static final int Shade5 = 5;
  private static final int Shade6 = 6;
  private static final int Shade7 = 7;
  private static final int Shade8 = 8;
  private static final int Shade9 = 9;
  public static final int MaxShades = 10;
  public static final int AltColor = 10;
  public static final int MaxColors = 11;

  public static int getRandomShade()
  {
    return (int)(MaxShades * Math.random());
  }

  private int cols;
  private int rows;
  private char[][] data;
  private int[][] color;

  public Model(int theCols, int theRows)
  {
    cols = theCols;
    rows = theRows;

    data = new char[rows][cols];
    color = new int[rows][cols];
    for (int y = 0; y < rows; y++)
    {
      for (int x = 0; x < cols; x++)
      {
        data[y][x] = ' ';
        color[y][x] = Color;
      }
    }
  }

  public void setChar(char c, int x, int y, int theColor)
  {
    if (y >= rows)
    {
      y %= rows;
    }
    data[y][x] = c;
    color[y][x] = theColor % MaxColors;
  }

  public char[][] getData()
  {
    return data;
  }

  public int[][] getColors()
  {
    return color;
  }
}

