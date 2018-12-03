/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

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
  private int componentIndex;
  private Data data;
  private Profiler profiler;

  public Surface(String specialFile)
  {
    super();

    isSized = false;

    data = new Data();
    data.setDataListener(this);

    components = new ArrayList<Component>();
    addComponent(new com.floorsix.dashboard.clock.Clock(data));
    addComponent(new com.floorsix.dashboard.clock.CornerClock(data));

    backgrounds = new ArrayList<Component>();
    addBackground(new Background(data));
    addBackground(new com.floorsix.dashboard.thematrix.Matrix(data, specialFile));
    addBackground(new com.floorsix.dashboard.drip.Dripping(data));
    addBackground(new com.floorsix.dashboard.starfield.Starfield(data));
    addBackground(new com.floorsix.dashboard.snow.Snowfield(data));

    backgroundIndex = 0;
    componentIndex = 0;

    addComponentListener(this);

    com.floorsix.dashboard.server.Server.launchServer(data);

    profiler = new Profiler("Frames", 200);
  }

  public void addComponent(Component c)
  {
    components.add(c);
  }

  public void addBackground(Component c)
  {
    backgrounds.add(c);
  }

  public void update()
  {
    data.update();

    backgrounds.get(backgroundIndex).update();

    components.get(componentIndex).update();
  }

  private void draw(Graphics2D g)
  {
    backgrounds.get(backgroundIndex).draw(g);

    components.get(componentIndex).draw(g);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    if (isSized)
    {
      Graphics2D g2d = (Graphics2D) g.create();
      draw(g2d);
      g2d.dispose();
    }
    else
    {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
    }

    profiler.update();
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

      case 'C':
        componentIndex = (componentIndex + 1) % components.size();
        data.setComponent(components.get(componentIndex).getName());
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

    String s;

    switch (state)
    {
      case WorkingHours:
        s = com.floorsix.dashboard.thematrix.Matrix.Name;
        break;

      case AfterWork:
        s = com.floorsix.dashboard.drip.Dripping.Name;
        break;

      case BeforeWork:
        s = Background.Name;
        break;

      default:
      case Weekend:
        s = Background.Name;
        break;

      case Nighttime:
        s = Background.Name;
        break;

      case Morning:
        s = Background.Name;
        break;
    }

    data.setBackground(s);
  }

  @Override
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

  @Override
  public void backgroundChanged(String background)
  {
    backgroundIndex = 0;

    for (Component c : backgrounds)
    {
      if (c.getName().equals(background))
      {
        return;
      }

      ++backgroundIndex;
    }

    backgroundIndex = 0;
  }

  @Override
  public void componentChanged(String component)
  {
    componentIndex = 0;

    for (Component c : components)
    {
      if (c.getName().equals(component))
      {
        return;
      }

      ++componentIndex;
    }

    componentIndex = 0;
  }
}

