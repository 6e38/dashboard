/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.clock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Calendar;
import javax.swing.JPanel;

public class ClockPanel extends JPanel implements ComponentListener
{
  private static final String[] DaysOfWeek = {
    "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
  };
  private static final String[] Months = {
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  };
  private Font clockFont;
  private Font dateFont;
  private Font remainingFont;
  private Dimension preferredSize;
  private boolean isSized;
  private String timeString;
  private String dateString;
  private String ampmString;
  private String remainingString;
  private Point timePoint;
  private Point datePoint;

  public ClockPanel()
  {
    super();

    final float clockFontSize = 100f;
    final float dateFontSize = 25f;
    final float remainingFontSize = 15f;

    try
    {
      InputStream stream = getClass().getResourceAsStream("/fonts/LANENAR_.ttf");
      Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
      clockFont = font.deriveFont(clockFontSize);

      stream = getClass().getResourceAsStream("/fonts/Rubik-Light.ttf");
      font = Font.createFont(Font.TRUETYPE_FONT, stream);
      dateFont = font.deriveFont(dateFontSize);

      remainingFont = font.deriveFont(remainingFontSize);
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

    preferredSize = new Dimension(400, 65535);

    isSized = false;

    timeString = null;
    dateString = null;
    ampmString = null;
    remainingString = null;

    addComponentListener(this);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    update();
    draw(g2d);
    g2d.dispose();
  }

  private void update()
  {
    Calendar c = Calendar.getInstance();

    int hour = c.get(Calendar.HOUR);
    if (hour == 0)
    {
      hour = 12;
    }
    timeString = String.format("%d:%02d", hour, c.get(Calendar.MINUTE));
    dateString = String.format("%s %d %s %d",
        DaysOfWeek[c.get(Calendar.DAY_OF_WEEK)],
        c.get(Calendar.DAY_OF_MONTH),
        Months[c.get(Calendar.MONTH)],
        c.get(Calendar.YEAR));
    ampmString = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

    Calendar five = (Calendar)c.clone();
    five.set(Calendar.HOUR_OF_DAY, 17);
    five.set(Calendar.MINUTE, 0);
    five.set(Calendar.SECOND, 0);
    five.set(Calendar.MILLISECOND, 0);

    long ms = five.getTime().getTime() - c.getTime().getTime();
    int hours = (int)(ms / 1000 / 60 / 60);
    int mins = (int)(ms / 1000 / 60) % 60;
    int secs = (int)(ms / 1000) % 60;
    //remainingString = String.format("%d:%02d:%02d remaining", hours, mins, secs);
    remainingString = String.format("%d:%02d to go", hours, mins);
  }

  private void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    if (isSized)
    {
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

  @Override
  public boolean isMaximumSizeSet()
  {
    return true;
  }

  @Override
  public boolean isMinimumSizeSet()
  {
    return true;
  }

  @Override
  public boolean isPreferredSizeSet()
  {
    return true;
  }

  @Override
  public Dimension getMaximumSize()
  {
    return preferredSize;
  }

  @Override
  public Dimension getMinimumSize()
  {
    return preferredSize;
  }

  @Override
  public Dimension getPreferredSize()
  {
    return preferredSize;
  }

  @Override
  public void componentHidden(ComponentEvent e)
  {
  }

  @Override
  public void componentMoved(ComponentEvent e)
  {
  }

  @Override
  public void componentResized(ComponentEvent e)
  {
    isSized = true;
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
  }
}

