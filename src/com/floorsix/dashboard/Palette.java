
package com.floorsix.dashboard;

import java.awt.Color;

public class Palette
{
  public String name;
  public Color primary;
  public Color secondary;
  public Color background;

  Palette(String name, int primary, int secondary, int background)
  {
    this.name = name;
    this.primary = new Color(primary);
    this.secondary = new Color(secondary);
    this.background = new Color(background);
  }
}

