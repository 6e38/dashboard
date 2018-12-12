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
  private StateMessage stateMessage;
  private int backgroundIndex;
  private int componentIndex;
  private Data data;
  private StatePreferences statePrefs;
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
    addComponent(new com.floorsix.dashboard.clock.ReverseCornerClock(data));

    backgrounds = new ArrayList<Component>();
    addBackground(new Background(data));
    addBackground(new com.floorsix.dashboard.thematrix.Matrix(data, specialFile));
    addBackground(new com.floorsix.dashboard.drip.Dripping(data));
    addBackground(new com.floorsix.dashboard.starfield.Starfield(data));
    addBackground(new com.floorsix.dashboard.snow.Snowfield(data));

    backgroundIndex = 0;
    componentIndex = 0;

    addComponentListener(this);

    stateMessage = new StateMessage();

    com.floorsix.dashboard.server.Server.launchServer(data);

    profiler = new Profiler("Frames", 200);

    statePrefs = new StatePreferences();
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

    stateMessage.update();
  }

  private void draw(Graphics2D g)
  {
    backgrounds.get(backgroundIndex).draw(g);

    components.get(componentIndex).draw(g);

    stateMessage.draw(g);
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

    stateMessage.surfaceSized(w, h, g);

    isSized = true;
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    String name;

    switch (e.getKeyChar())
    {
      case '>':
        data.cycleState();
        break;

      case 'b':
        backgroundIndex = (backgroundIndex + 1) % backgrounds.size();
        name = backgrounds.get(backgroundIndex).getName();
        data.setBackground(name);
        statePrefs.saveBackground(data.getState(), name);
        break;

      case 'c':
        name = PaletteFactory.getNext();
        data.setPalette(name);
        statePrefs.savePalette(data.getState(), name);
        break;

      case 'C':
        componentIndex = (componentIndex + 1) % components.size();
        name = components.get(componentIndex).getName();
        data.setComponent(name);
        statePrefs.saveClock(data.getState(), name);
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
    data.setPalette(statePrefs.getPalette(state));
    data.setBackground(statePrefs.getBackground(state));
    data.setComponent(statePrefs.getClock(state));

    stateMessage.setState(state.toString());
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

    stateMessage.setState(palette.name);
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

