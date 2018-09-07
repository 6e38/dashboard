
package dashboard;

import java.awt.Color;

public class Palette
{
  public Color primary;
  public Color secondary;
  public Color background;

  Palette(int primary, int secondary, int background)
  {
    this.primary = new Color(primary, true);
    this.secondary = new Color(secondary, true);
    this.background = new Color(background, true);
  }
}

