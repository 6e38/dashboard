/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.clock;

import dashboard.Component;
import dashboard.Data;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.io.IOException;

public class ClockPanel implements Component
{
  private Rectangle bounds;
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private Data data;

  public ClockPanel()
  {
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

  @Override
  public void update(Data d)
  {
    data = d;
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
  }
}

