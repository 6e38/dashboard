/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Toolkit;

public class BlankCursor
{
  public static Cursor getCursor()
  {
    BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    return Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), "blah");
  }
}

