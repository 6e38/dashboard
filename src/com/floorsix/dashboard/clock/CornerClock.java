/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.clock;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import com.floorsix.dashboard.PresenceEvent.Type;
import com.floorsix.dashboard.PresenceEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class CornerClock implements Component
{
  private Palette palette;
  private Rectangle bounds;
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private Data data;
  private boolean hasSurface;

  public CornerClock(Data data)
  {
    this.data = data;

    paletteChanged(data.getPalette());

    hasSurface = false;
  }

  private void setSize(int width, int height)
  {
    bounds = new Rectangle(0, 0, width, height);
  }

  private int getWidth()
  {
    return bounds.width;
  }

  private int getHeight()
  {
    return bounds.height;
  }

  @Override
  public String getName()
  {
    return "cornerclock";
  }

  @Override
  public void surfaceSized(int width, int height, Graphics g)
  {
    setSize(width, height);

    final int clockFontSize = 100;
    final int dateFontSize = 25;
    final int remainingFontSize = 15;

    try
    {
      InputStream stream = getClass().getResourceAsStream("/fonts/LANENAR_.ttf");
      Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
      clockFont = font.deriveFont(Font.PLAIN, clockFontSize);

      stream = getClass().getResourceAsStream("/fonts/Rubik-Light.ttf");
      font = Font.createFont(Font.TRUETYPE_FONT, stream);
      dateFont = font.deriveFont(Font.PLAIN, dateFontSize);

      remainingFont = font.deriveFont(Font.PLAIN, remainingFontSize);
    }
    catch (FontFormatException fontFormatException)
    {
      System.err.println("Failed to load font");
      clockFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)clockFontSize);
      dateFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)dateFontSize);
      remainingFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)remainingFontSize);
    }
    catch (IOException ioException)
    {
      System.err.println("IO failure while reading font");
      clockFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)clockFontSize);
      dateFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)dateFontSize);
      remainingFont = new Font(Font.SANS_SERIF, Font.PLAIN, (int)remainingFontSize);
    }

    hasSurface = true;
  }

  @Override
  public void update()
  {
  }

  @Override
  public void draw(Graphics2D g)
  {
    if (hasSurface)
    {
      int padding = 10;

      g.setColor(palette.secondary);

      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

      int x;
      int y;
      int w;
      FontMetrics metrics;

      metrics = g.getFontMetrics(dateFont);
      y = bounds.height - metrics.getDescent() - padding;
      w = metrics.stringWidth(data.getDateString());
      x = bounds.width - w - padding;
      g.setFont(dateFont);
      g.drawString(data.getDateString(), x, y);
      y -= metrics.getAscent();

      metrics = g.getFontMetrics(clockFont);
      //y -= metrics.getDescent();
      w = metrics.stringWidth(data.getTimeString());
      x = bounds.width - w - padding;
      g.setFont(clockFont);
      g.drawString(data.getTimeString(), x, y);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

