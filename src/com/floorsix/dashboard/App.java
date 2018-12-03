/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App
{
  public static void main(String args[])
  {
    String file = "special.txt";

    if (args.length > 0)
    {
      file = args[0];
    }

    new App(file);
  }

  private String specialFile;

  public App(String theSpecialFile)
  {
    specialFile = theSpecialFile;

    SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run()
        {
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          GraphicsDevice[] gd = ge.getScreenDevices();

          JFrame frame = new JFrame("");

          frame.setUndecorated(true);
          gd[0].setFullScreenWindow(frame);

          frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

          Surface surface = new Surface(specialFile);

          frame.getContentPane().add(surface);
          frame.addKeyListener(surface);

          frame.getContentPane().setCursor(BlankCursor.getCursor());
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);

          Thread thread = new Renderer(surface);
          thread.start();
        }
    });
  }
}

