/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.thematrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Drop
{
  private static ArrayList<String> specialStrings;
  private static boolean hasSpecial = false;

  private int updates;
  private int x;
  private int y;
  private int startY;
  private int endY;
  private int cols;
  private int rows;
  private int index;
  private int duration;
  private char[] message;
  private Model model;
  private boolean isSpecial;
  private int color;

  public Drop(int theCols, int theRows, Model theModel, CollisionAvoidance ca, String specialFile)
  {
    readSpecialStrings(specialFile);

    updates = 0;
    cols = theCols;
    rows = theRows;
    model = theModel;
    index = 0;

    if (!hasSpecial && specialStrings.size() > 0 && Math.random() < 0.01)
    {
      hasSpecial = true;
      isSpecial = true;
      color = Model.AltColor;

      message = specialStrings.get((int)(specialStrings.size() * Math.random())).toCharArray();

      duration = (int)(20 * 5 * Math.random() + 20 * 5) + message.length;
    }
    else
    {
      isSpecial = false;
      color = Model.getRandomShade();
      boolean blank = Math.random() < 0.50 ? true : false;

      int length = (int)((rows - 3) * Math.random()) + 3;
      message = new char[length];

      if (blank)
      {
        makeBlankMessage(message);
      }
      else
      {
        makeRandomMessage(message);
      }

      duration = (int)(20 * 5 * Math.random()) + message.length;
    }

    x = (int)(cols * Math.random());
    y = (int)(rows * Math.random());

    while (ca.collides(x, y, y + message.length))
    {
      x = (int)(cols * Math.random());
      y = (int)(rows * Math.random());
    }

    startY = y;
    endY = y + message.length;
  }

  private char getRandomChar()
  {
    /* Kanji range from 0x4e00 to 0x9fff */
    final char first = 0x4e00;
    final char last = 0x4f8f;
    return (char)(Math.random() * (last + 1 - first) + first);
  }

  private void makeRandomMessage(char[] m)
  {
    for (int i = 0; i < m.length; ++i)
    {
      m[i] = getRandomChar();
    }
  }

  private void makeBlankMessage(char[] m)
  {
    for (int i = 0; i < m.length; ++i)
    {
      m[i] = ' ';
    }
  }

  public boolean update()
  {
    boolean complete = false;

    if (updates < message.length)
    {
      model.setChar(message[index++], x, y++, color);
    }
    else if (updates < duration)
    {
      if (!isSpecial)
      {
        model.setChar(getRandomChar(), x, y, color);
      }
    }
    else
    {
      if (isSpecial)
      {
        isSpecial = false;
        hasSpecial = false;
        index = 0;
        y = startY;
        color = Model.getRandomShade();
        makeRandomMessage(message);
        duration = message.length;
        updates = -1;
      }
      else
      {
        complete = true;
      }
    }

    ++updates;

    return complete;
  }

  private static void readSpecialStrings(String filename)
  {
    if (filename != null)
    {
      if (specialStrings == null)
      {
        specialStrings = new ArrayList<String>();

        try
        {
          File file = new File(filename);
          BufferedReader reader = new BufferedReader(new FileReader(file));

          for (String line = reader.readLine(); line != null; line = reader.readLine())
          {
            specialStrings.add(line);
          }
        }
        catch (Exception e)
        {
          // Ignore all
        }
      }
    }
  }

  public boolean collides(int theX, int y1, int y2)
  {
    if (x == theX)
    {
      if (y1 > endY || y2 < startY)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    else
    {
      return false;
    }
  }
}

