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
  private Dimension preferredSize;
  private boolean isSized;
  private String timeString;
  private String dateString;
  private Point timePoint;
  private Point datePoint;

  public ClockPanel()
  {
    super();

    try
    {
      InputStream stream = getClass().getResourceAsStream("/fonts/LANEUP__.ttf");
      Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
      clockFont = font.deriveFont(100f);
      dateFont = font.deriveFont(30f);
    }
    catch (FontFormatException fontFormatException)
    {
      System.out.println("Failed to load font");
      clockFont = new Font(Font.SANS_SERIF, Font.PLAIN, 80);
      dateFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    }
    catch (IOException ioException)
    {
      System.out.println("IO failure while reading font");
      clockFont = new Font(Font.SANS_SERIF, Font.PLAIN, 80);
      dateFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    }

    preferredSize = new Dimension(400, 65535);

    isSized = false;

    timeString = "8:00";
    dateString = "Monday, August 24, 2018";

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

    timeString = String.format("%d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    dateString = String.format("%s %d %s %d",
        DaysOfWeek[c.get(Calendar.DAY_OF_WEEK)],
        c.get(Calendar.DAY_OF_MONTH),
        Months[c.get(Calendar.MONTH)],
        c.get(Calendar.YEAR));
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

      metrics = g.getFontMetrics(dateFont);
      y += metrics.getAscent();
      w = metrics.stringWidth(dateString);
      x = getWidth() / 2 - w / 2;

      g.setFont(dateFont);
      g.drawString(dateString, x, y);
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

