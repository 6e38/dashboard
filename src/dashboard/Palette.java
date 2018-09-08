
package dashboard;

import java.awt.Color;

public class Palette
{
  public Color primary;
  public Color secondary;
  public Color background;

  Palette(int primary, int secondary, int background)
  {
    this.primary = new Color(primary);
    this.secondary = new Color(secondary);
    this.background = new Color(background);
  }
}

