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
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class ReverseCornerClock implements Component
{
  private static final double padding = 10;

  private Palette palette;
  private Rectangle bounds;
  private RoundRectangle2D.Double box;
  private boolean dirty;
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private Data data;
  private boolean hasSurface;

  public ReverseCornerClock(Data data)
  {
    this.data = data;

    paletteChanged(data.getPalette());

    hasSurface = false;

    dirty = false;
  }

  private void setSize(int width, int height)
  {
    bounds = new Rectangle(0, 0, width, height);
  }

  @Override
  public String getName()
  {
    return "reversecornerclock";
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

    FontMetrics fm = g.getFontMetrics(clockFont);
    int clockWidth = fm.stringWidth("00\u202200"); // '0' is probably widest number character
    int clockHeight = fm.getAscent();
    fm = g.getFontMetrics(dateFont);
    int dateWidth = fm.stringWidth("September, Wednesday 30, 2000");
    int dateHeight = fm.getAscent();

    int maxWidth = clockWidth > dateWidth ? clockWidth : dateWidth;

    double w = maxWidth + padding * 2;
    double h = clockHeight + dateHeight + padding * 3;
    double x = width - w - padding;
    double y = height - h - padding;
    box = new RoundRectangle2D.Double(x, y, w, h, 50, 50);
    /*
    January
    February
    March
    April
    May
    June
    July
    August
    September 9 chars long
    October
    November
    December
    Sunday
    Monday
    Tuesday
    Wednesday 9 chars long
    Thursday
    Friday
    Saturday
    */

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
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

      g.setColor(palette.primary);
      g.fill(box);

      double x;
      double y;
      double w;
      FontMetrics metrics;

      metrics = g.getFontMetrics(dateFont);
      y = box.getY() + box.getHeight() - metrics.getDescent() - padding;
      w = metrics.stringWidth(data.getDateString());
      x = box.getX() + box.getWidth() / 2 - w / 2;
      g.setColor(palette.secondary);
      g.setFont(dateFont);
      g.drawString(data.getDateString(), (int)x, (int)y);
      y -= metrics.getAscent() + padding;

      metrics = g.getFontMetrics(clockFont);
      //y -= metrics.getDescent();
      w = metrics.stringWidth(data.getTimeString2());
      x = box.getX() + box.getWidth() / 2 - w / 2;
      g.setFont(clockFont);
      g.drawString(data.getTimeString2(), (int)x, (int)y);
    }
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

