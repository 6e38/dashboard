/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.clock;

import dashboard.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Calendar;

public class ClockPanel implements Component
{
  private static final String[] DaysOfWeek = {
    "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
  };
  private static final String[] Months = {
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  };

  private Rectangle bounds;
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private String timeString;
  private String dateString;
  private String ampmString;
  private String remainingString;

  public ClockPanel()
  {
    timeString = null;
    dateString = null;
    ampmString = null;
    remainingString = null;
  }

  private void setSize(int width, int height)
  {
    int w = width / 5;
    int h = height / 4;

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

  private void calculateRemaining(Calendar c)
  {
    if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
        c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
    {
      Calendar five = (Calendar)c.clone();
      five.set(Calendar.HOUR_OF_DAY, 17);
      five.set(Calendar.MINUTE, 0);
      five.set(Calendar.SECOND, 0);
      five.set(Calendar.MILLISECOND, 0);

      long ms = five.getTime().getTime() - c.getTime().getTime();
      int hours = (int)(ms / 1000 / 60 / 60);
      int mins = (int)(ms / 1000 / 60) % 60;
      int secs = (int)(ms / 1000) % 60;
      remainingString = String.format("%d:%02d to go", hours, mins);
    }
    else
    {
      remainingString = "";
    }
  }

  @Override
  public void update()
  {
    Calendar c = Calendar.getInstance();

    int hour = c.get(Calendar.HOUR);
    if (hour == 0)
    {
      hour = 12;
    }
    timeString = String.format("%d:%02d", hour, c.get(Calendar.MINUTE));
    dateString = String.format("%s, %s %d, %d",
        DaysOfWeek[c.get(Calendar.DAY_OF_WEEK)],
        Months[c.get(Calendar.MONTH)],
        c.get(Calendar.DAY_OF_MONTH),
        c.get(Calendar.YEAR));
    ampmString = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

    calculateRemaining(c);
  }

  @Override
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    g.setColor(Color.GREEN);
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    g.translate(bounds.x, bounds.y);

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    g.setColor(Color.GREEN);

    FontMetrics metrics;
    metrics = g.getFontMetrics(clockFont);
    int y = metrics.getAscent();
    int w = metrics.stringWidth(timeString);
    int x = getWidth() / 2 - w / 2;

    g.setFont(clockFont);
    g.drawString(timeString, x, y);
    g.setFont(dateFont);
    g.drawString(ampmString, x + w, y);

    y += metrics.getDescent();
    metrics = g.getFontMetrics(dateFont);
    y += metrics.getAscent();
    w = metrics.stringWidth(dateString);
    x = getWidth() / 2 - w / 2;

    g.setFont(dateFont);
    g.drawString(dateString, x, y);

    y += metrics.getDescent();
    metrics = g.getFontMetrics(remainingFont);
    y += metrics.getAscent();
    w = metrics.stringWidth(remainingString);
    x = getWidth() / 2 - w / 2;

    g.setFont(remainingFont);
    g.drawString(remainingString, x, y);
  }
}

