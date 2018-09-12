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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.io.IOException;
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
  }

  private void drawPresence(Graphics2D g, int y)
  {
    List<PresenceEvent> list = data.getPresenceEvents();
    final int hoursInBar = 11;

    int barWidth = getWidth() * 9 / 10;
    int barHeight = 10;
    int barX = getWidth() / 2 - barWidth / 2;
    int barY = y;

    g.setColor(palette.secondary);

    long startOfDay = data.getStartOfDay();
    long sevenam = startOfDay + 5 * 60 * 60 * 1000;
    long sixpm = startOfDay + 7 * 60 * 60 * 1000;
    /*
    long sevenam = startOfDay + 7 * 60 * 60 * 1000;
    long sixpm = startOfDay + 18 * 60 * 60 * 1000;
    */
    long duration = sixpm - sevenam;

    PresenceEvent last = null;

    for (PresenceEvent e : list)
    {
      if (last != null)
      {
        if (e.type == Type.Lock)
        {
          if (last.type == Type.Unlock)
          {
            float p = (float)(last.timestamp - sevenam) / (float)duration;
            float xf = p * (float)barWidth;
            int x = barX + (int)xf;

            p = (float)(e.timestamp - last.timestamp) / (float)duration;
            float wf = p * (float)barWidth;
            int w = (int)wf;
            if (w == 0)
            {
              w = 1;
            }

            g.fillRect(x, barY, w, barHeight);
          }
        }
      }

      last = e;
    }

    if (last != null && last.type == Type.Unlock)
    {
      float p = (float)(last.timestamp - sevenam) / (float)duration;
      float xf = p * (float)barWidth;
      int x = barX + (int)xf;

      p = (float)(Calendar.getInstance().getTime().getTime() - last.timestamp) / (float)duration;
      float wf = p * (float)barWidth;
      int w = (int)wf;
      if (w == 0)
      {
        w = 1;
      }

      g.fillRect(x, barY, w, barHeight);
    }

    g.setColor(palette.primary);
    g.drawRect(barX, barY, barWidth, barHeight);
    int x1 = barX + barWidth / hoursInBar;
    g.drawLine(x1, barY + barHeight, x1, barY + barHeight * 2); // 8am
    x1 = barX + barWidth * 10 / hoursInBar;
    g.drawLine(x1, barY + barHeight, x1, barY + barHeight * 2); // 5pm
    x1 = barX + barWidth * 5 / hoursInBar;
    g.drawLine(x1, barY + barHeight, x1, barY + barHeight + barHeight / 2); // noon
    x1 = barX + barWidth * 6 / hoursInBar;
    g.drawLine(x1, barY + barHeight, x1, barY + barHeight + barHeight / 2); // 1pm
  }

  @Override
  public void paletteChanged(Palette palette)
  {
    this.palette = palette;
  }
}

