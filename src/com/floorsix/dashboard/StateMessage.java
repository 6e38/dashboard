/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

public class StateMessage implements Component
{
  public static final String Name = "statemessage";
  public static final long OpaqueDuration = 250;
  public static final long Duration = 2500;
  private static final int Padding = 10;

  private int fontSize;
  private long startTime;
  private String state;
  private int width;
  private int height;
  private Color background;
  private Color foreground;
  private Font font;
  private int textX;
  private int textY;

  public StateMessage()
  {
    fontSize = 9;
    state = "None";
    width = 1;
    height = 1;
    background = new Color(50, 50, 50, 128);
    foreground = new Color(225, 225, 225);

    try
    {
      InputStream stream = getClass().getResourceAsStream("/fonts/Rubik-Light.ttf");
      font = Font.createFont(Font.TRUETYPE_FONT, stream);
    }
    catch (FontFormatException fontFormatException)
    {
      font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
    }
    catch (IOException ioException)
    {
      font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
    }
  }

  void setState(String state)
  {
    startTime = System.currentTimeMillis();
    this.state = state;
  }

  @Override
  public String getName()
  {
    return Name;
  }

  @Override
  public void surfaceSized(int width, int height, Graphics g)
  {
    if (width > 4000)
    {
      fontSize = 80;
    }
    else if (width > 1000)
    {
      fontSize = 25;
    }
    else if (width > 600)
    {
      fontSize = 16;
    }
    else
    {
      fontSize = 9;
    }

    font = font.deriveFont(Font.PLAIN, fontSize);
  }

  private void calculateSizes(Graphics g)
  {
    FontMetrics metrics = g.getFontMetrics(font);

    width = metrics.stringWidth(state) + Padding * 2;
    height = metrics.getAscent() + metrics.getDescent() + Padding * 2;

    textX = Padding * 2;
    textY = Padding + height - Padding - metrics.getDescent();
  }

  @Override
  public void update()
  {
  }

  @Override
  public void draw(Graphics2D g)
  {
    if (System.currentTimeMillis() - startTime < Duration)
    {
      calculateSizes(g);

      g.setColor(background);
      g.fillRect(Padding, Padding, width, height);

      g.setColor(foreground);
      g.setFont(font);
      g.drawString(state, textX, textY);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
  }
}

