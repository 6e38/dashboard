/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

public interface Action
{
  String getCommandName();
  void doCommand();
}

