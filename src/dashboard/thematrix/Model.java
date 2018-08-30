/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.thematrix;

public class Model
{
  public static final int Green = 0;
  private static final int Green1 = 1;
  private static final int Green2 = 2;
  private static final int Green3 = 3;
  private static final int Green4 = 4;
  private static final int Green5 = 5;
  private static final int Green6 = 6;
  private static final int Green7 = 7;
  private static final int Green8 = 8;
  private static final int Green9 = 9;
  private static final int MaxGreens = 10;
  public static final int Red = 10;
  private static final int MaxColors = 11;

  public static int getRandomGreen()
  {
    return (int)(MaxGreens * Math.random());
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
        color[y][x] = Green;
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

