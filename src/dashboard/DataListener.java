/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

interface DataListener
{
  void stateChanged(Data.State state);
  void paletteChanged(Palette palette);
}

