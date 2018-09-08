/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

interface DataListener
{
  void stateChanged(Data.State state);
  void paletteChanged(Palette palette);
  void backgroundChanged(String background);
}

