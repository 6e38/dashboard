
package dashboard.clock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class ClockPanel extends JPanel implements ComponentListener
{
  private Font clockFont;
  private int paints;

  public ClockPanel()
  {
    super();

    clockFont = new Font(Font.SANS_SERIF, Font.PLAIN, 80);

    addComponentListener(this);
    paints = 0;
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    if (paints < 10)
    {
      ++paints;
      System.out.println("paintComponent");
    }

    Graphics2D g2d = (Graphics2D) g.create();
    update();
    draw(g2d);
    g2d.dispose();
  }

  private void update()
  {
  }

  private void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    g.setColor(Color.GREEN);
    g.setFont(clockFont);
    g.drawString("8:00", 40, 80);
  }

  /*
  @Override
  public void setSize(Dimension d)
  {
    System.out.println(String.format("setSize(%f,%f)", d.getWidth(), d.getHeight()));
  }

  @Override
  public Dimension getSize()
  {
    System.out.println("getSize()");
    return new Dimension(100, 100);
  }
  */

  @Override
  public boolean isMaximumSizeSet()
  {
    System.out.println("isMaximumSizeSet()");
    return true;
  }

  @Override
  public boolean isMinimumSizeSet()
  {
    System.out.println("isMinimumSizeSet()");
    return true;
  }

  @Override
  public boolean isPreferredSizeSet()
  {
    System.out.println("isPreferredSizeSet()");
    return true;
  }

  @Override
  public Dimension getMaximumSize()
  {
    System.out.println("getMaximumSize()");
    return new Dimension(300, 65635);
  }

  @Override
  public Dimension getMinimumSize()
  {
    System.out.println("getMinimumSize()");
    return new Dimension(300, 65635);
  }

  @Override
  public Dimension getPreferredSize()
  {
    System.out.println("getPreferredSize()");
    return new Dimension(300, 65535);
  }

  /*
  @Override
  public void setMaximumSize(Dimension d)
  {
    System.out.println(String.format("setMaximumSize(%f,%f)", d.getWidth(), d.getHeight()));
  }

  @Override
  public void setMinimumSize(Dimension d)
  {
    System.out.println(String.format("setMinimumSize(%f,%f)", d.getWidth(), d.getHeight()));
  }

  @Override
  public void setPreferredSize(Dimension d)
  {
    System.out.println(String.format("setPreferredSize(%f,%f)", d.getWidth(), d.getHeight()));
  }
  */

  @Override
  public void componentHidden(ComponentEvent e)
  {
    System.out.println("componentHidden");
  }

  @Override
  public void componentMoved(ComponentEvent e)
  {
    System.out.println("componentMoved");
  }

  @Override
  public void componentResized(ComponentEvent e)
  {
    System.out.println(String.format("componentResized %d,%d", getWidth(), getHeight()));
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
    System.out.println("componentShown");
  }
}

