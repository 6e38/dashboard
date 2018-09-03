
package dashboard;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Surface extends JPanel implements ComponentListener
{
  private boolean isSized;
  private ArrayList<Component> components;
  private Data data;

  public Surface()
  {
    super();

    isSized = false;
    components = new ArrayList<Component>();
    data = new Data();

    addComponentListener(this);
  }

  public void addComponent(Component c)
  {
    components.add(c);
  }

  private void update()
  {
    data.update();

    for (Component c : components)
    {
      c.update(data);
    }
  }

  private void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    for (Component c : components)
    {
      c.draw(g);
    }
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    if (isSized)
    {
      Graphics2D g2d = (Graphics2D) g.create();
      update();
      draw(g2d);
      g2d.dispose();
    }
    else
    {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
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
    int w = getWidth();
    int h = getHeight();
    Graphics g = getGraphics();

    for (Component c : components)
    {
      c.surfaceSized(w, h, g);
    }

    isSized = true;
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
  }
}

