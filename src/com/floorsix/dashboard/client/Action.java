/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

interface Action
{
  String getCommandName();
  String getUsage();
  String getDescription();
  void doCommand(String[] args);
}

