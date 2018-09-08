
package dashboard;

import java.awt.Graphics;
import java.awt.Graphics2D;

public interface Component
{
  String getName();
  void surfaceSized(int width, int height, Graphics g);
  void update();
  void draw(Graphics2D g);
  void paletteChanged(Palette palette);
}

