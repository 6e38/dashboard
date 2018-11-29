/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.clock;

import com.floorsix.dashboard.Component;
import com.floorsix.dashboard.Data;
import com.floorsix.dashboard.Palette;
import com.floorsix.dashboard.PresenceEvent;
import com.floorsix.dashboard.PresenceEvent.Type;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class Clock implements Component
{
  private Palette palette;
  private Rectangle bounds;
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private Data data;

  public Clock(Data data)
  {
    this.data = data;

    paletteChanged(data.getPalette());
  }

  private void setSize(int width, int height)
  {
    int w = 400;
    int h = 300;

    bounds = new Rectangle(width / 2 - w / 2, height / 2 - h / 2, w, h);
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
    return "clock";
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
  }

  @Override
  public void update()
  {
  }

  @Override
  public void draw(Graphics2D g)
  {
    AffineTransform save = g.getTransform();

    if (data.isBeforeWork() || data.isAfterWork() || data.isWorkingHours())
    {
      g.setColor(palette.background);
      g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    g.setColor(palette.primary);

    if (data.isBeforeWork() || data.isAfterWork() || data.isWorkingHours())
    {
      g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

    g.translate(bounds.x, bounds.y);

    FontMetrics metrics;
    metrics = g.getFontMetrics(clockFont);
    int y = metrics.getAscent();
    int w = metrics.stringWidth(data.getTimeString());
    int x = getWidth() / 2 - w / 2;

    g.setFont(clockFont);
    g.drawString(data.getTimeString(), x, y);
    g.setFont(dateFont);
    g.drawString(data.getAmPmString(), x + w, y);

    y += metrics.getDescent();
    metrics = g.getFontMetrics(dateFont);
    y += metrics.getAscent();
    w = metrics.stringWidth(data.getDateString());
    x = getWidth() / 2 - w / 2;

    g.setFont(dateFont);
    g.drawString(data.getDateString(), x, y);

    if (data.isWorkingHours())
    {
      y += metrics.getDescent();
      metrics = g.getFontMetrics(remainingFont);
      y += metrics.getAscent();
      w = metrics.stringWidth(data.getRemainingString());
      x = getWidth() / 2 - w / 2;

      g.setFont(remainingFont);
      g.drawString(data.getRemainingString(), x, y);
    }

    y += metrics.getDescent();

    drawPresence(g, y);

    g.setTransform(save);
  }

  private void drawPresence(Graphics2D g, int y)
  {
    List<PresenceEvent> list = data.getPresenceEvents();
    final int hoursInBar = 11;

    float barWidth = getWidth() * 9 / 10;
    float barHeight = 10;
    float barX = getWidth() / 2 - barWidth / 2;
    float barY = y;

    g.setClip((int)barX, (int)barY, (int)barWidth, (int)barHeight);

    g.setColor(palette.secondary);

    long startOfDay = data.getStartOfDay();
    long sevenam = startOfDay + 7 * 60 * 60 * 1000;
    long sixpm = startOfDay + 18 * 60 * 60 * 1000;
    long duration = sixpm - sevenam;

    PresenceEvent last = null;

    for (PresenceEvent e : list)
    {
      if (last != null)
      {
        if (last.type == Type.Unlock)
        {
          float x = (float)(last.timestamp - sevenam) / (float)duration * barWidth + barX;
          float w = (float)(e.timestamp - last.timestamp) / (float)duration * barWidth;
          w = w < 1 ? 1 : w;

          g.fillRect((int)x, (int)barY, (int)w, (int)barHeight);
        }
      }

      last = e;
    }

    if (last != null && last.type == Type.Unlock)
    {
      float x = (float)(last.timestamp - sevenam) / (float)duration * barWidth + barX;
      float w = (float)(Calendar.getInstance().getTime().getTime() - last.timestamp) / (float)duration * barWidth;
      w = w < 1 ? 1 : w;

      g.fillRect((int)x, (int)barY, (int)w, (int)barHeight);
    }

    g.setClip(null);

    g.drawRect((int)barX, (int)barY, (int)barWidth, (int)barHeight);
    float x = barX + barWidth / hoursInBar;
    g.drawLine((int)x, (int)(barY + barHeight), (int)x, (int)(barY + barHeight * 2));
    x = barX + barWidth * 10 / hoursInBar;
    g.drawLine((int)x, (int)(barY + barHeight), (int)x, (int)(barY + barHeight * 2));
    x = barX + barWidth * 5 / hoursInBar;
    g.drawLine((int)x, (int)(barY + barHeight), (int)x, (int)(barY + barHeight * 1.3));
    x = barX + barWidth * 6 / hoursInBar;
    g.drawLine((int)x, (int)(barY + barHeight), (int)x, (int)(barY + barHeight * 1.3));
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

