
package dashboard;

import java.awt.Graphics;
import java.awt.Graphics2D;

public interface Component
{
  void surfaceSized(int width, int height, Graphics g);
  void update(Data d);
  void draw(Graphics2D g);
}

