
package dashboard;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Surface extends JPanel implements DataListener, ComponentListener, KeyListener
{
  private boolean isSized;
  private ArrayList<Component> components;
  private ArrayList<Component> backgrounds;
  private int backgroundIndex;
  private Data data;

  public Surface(String specialFile)
  {
    super();

    isSized = false;

    data = new Data();
    data.setDataListener(this);

    components = new ArrayList<Component>();
    addComponent(new dashboard.clock.Clock(data));

    backgrounds = new ArrayList<Component>();
    addBackground(new Background(data));
    addBackground(new dashboard.thematrix.Matrix(data, specialFile));
    addBackground(new dashboard.drip.Dripping(data));

    backgroundIndex = 0;

    addComponentListener(this);
  }

  public void addComponent(Component c)
  {
    components.add(c);
  }

  public void addBackground(Component c)
  {
    backgrounds.add(c);
  }

  private void update()
  {
    data.update();

    backgrounds.get(backgroundIndex).update();

    for (Component c : components)
    {
      c.update();
    }
  }

  private void draw(Graphics2D g)
  {
    backgrounds.get(backgroundIndex).draw(g);

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

    for (Component c : backgrounds)
    {
      c.surfaceSized(w, h, g);
    }

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

  @Override
  public void keyPressed(KeyEvent e)
  {
    switch (e.getKeyChar())
    {
      case '>':
        data.cycleState();
        break;

      case 'b':
        backgroundIndex = (backgroundIndex + 1) % backgrounds.size();
        data.setBackground(backgrounds.get(backgroundIndex).getName());
        break;

      case 'c':
        Palette p = PaletteFactory.getNext();
        data.setPalette(p);
        break;

      default:
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void stateChanged(Data.State state)
  {
    Palette p = PaletteFactory.get(state);
    data.setPalette(p);
  }

  public void paletteChanged(Palette palette)
  {
    for (Component c : backgrounds)
    {
      c.paletteChanged(palette);
    }

    for (Component c : components)
    {
      c.paletteChanged(palette);
    }
  }
}

