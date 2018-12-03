/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import com.floorsix.preferences.Preferences;

public class StatePreferences
{
  private static final String BackgroundKey = "background_";
  private static final String ClockKey = "clock_";
  private static final String PaletteKey = "palette_";
  private Preferences prefs;

  StatePreferences()
  {
    prefs = Preferences.getUserPreferences(App.class);
  }

  String getBackground(Data.State state)
  {
    String key = BackgroundKey + state;
    return prefs.getString(key, "blank");
  }

  String getClock(Data.State state)
  {
    String key = ClockKey + state;
    return prefs.getString(key, "clock");
  }

  String getPalette(Data.State state)
  {
    String key = PaletteKey + state;
    return prefs.getString(key, "terminal");
  }

  void saveBackground(Data.State state, String background)
  {
    String key = BackgroundKey + state;
    prefs.setString(key, background);
    prefs.commit();
  }

  void saveClock(Data.State state, String clock)
  {
    String key = ClockKey + state;
    prefs.setString(key, clock);
    prefs.commit();
  }

  void savePalette(Data.State state, String palette)
  {
    String key = PaletteKey + state;
    prefs.setString(key, palette);
    prefs.commit();
  }
}

