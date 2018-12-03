/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class Data
{
  enum State {
    WorkingHours,
    AfterWork,
    BeforeWork,
    Weekend,
    Nighttime,
    Morning,
  };

  private DataListener listener;
  private State state;
  private State actual;
  private static final State[] stateMap = {
    State.Morning,
    State.BeforeWork,
    State.WorkingHours,
    State.AfterWork,
    State.Nighttime,
    State.Weekend,
  };

  private Calendar calendar;
  private Calendar fivepm;
  private Calendar eightam;
  private Calendar nighttime;
  private Calendar morningtime;
  private DateFormatSymbols dfs;
  private long startOfDay;

  private int dayOfYear;

  private String dateString;
  private String timeString;
  private String ampmString;
  private String remainingString;

  private String background;
  private String component;
  private Palette palette;

  private PresenceData presenceData;

  public Data()
  {
    dayOfYear = -1;

    dfs = new DateFormatSymbols();

    setBackground(Background.Name);
    setComponent("clock");
    setPalette(PaletteFactory.get());

    state = State.Morning;
    actual = state;

    presenceData = new PresenceData();
  }

  public void update()
  {
    calendar = Calendar.getInstance();

    updateDayOfYear();
    updateState();

    dateString = String.format("%s, %s %d, %d",
        dfs.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)],
        dfs.getMonths()[calendar.get(Calendar.MONTH)],
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.YEAR));

    int hour = calendar.get(Calendar.HOUR);
    if (hour == 0)
    {
      hour = 12;
    }
    timeString = String.format("%d:%02d", hour, calendar.get(Calendar.MINUTE));

    ampmString = dfs.getAmPmStrings()[calendar.get(Calendar.AM_PM)];

    calculateRemaining();
  }

  public String getBackground()
  {
    return background;
  }

  public String getComponent()
  {
    return component;
  }

  public void setBackground(String bg)
  {
    background = bg;

    if (listener != null)
    {
      listener.backgroundChanged(background);
    }
  }

  public void setComponent(String component)
  {
    this.component = component;

    if (listener != null)
    {
      listener.componentChanged(component);
    }
  }

  public Palette getPalette()
  {
    return palette;
  }

  public void setPalette(Palette palette)
  {
    this.palette = palette;

    if (listener != null)
    {
      listener.paletteChanged(palette);
    }
  }

  public String getDateString()
  {
    return dateString;
  }

  public String getTimeString()
  {
    return timeString;
  }

  public String getAmPmString()
  {
    return ampmString;
  }

  public String getRemainingString()
  {
    return remainingString;
  }

  public void cycleState()
  {
    int index = 0;

    for (int i = 0; i < stateMap.length; ++i)
    {
      if (stateMap[i] == state)
      {
        index = i;
        break;
      }
    }

    index = (index + 1) % stateMap.length;

    state = stateMap[index];

    if (listener != null)
    {
      listener.stateChanged(state);
    }
  }

  private boolean isState(State x)
  {
    return x == state;
  }

  public boolean isWorkingHours()
  {
    return isState(State.WorkingHours);
  }

  public boolean isWeekend()
  {
    return isState(State.Weekend);
  }

  public boolean isAfterWork()
  {
    return isState(State.AfterWork);
  }

  public boolean isBeforeWork()
  {
    return isState(State.BeforeWork);
  }

  public boolean isMorning()
  {
    return isState(State.Morning);
  }

  public boolean isNighttime()
  {
    return isState(State.Nighttime);
  }

  public long getStartOfDay()
  {
    return startOfDay;
  }

  private void updateDayOfYear()
  {
    if (calendar.get(Calendar.DAY_OF_YEAR) != dayOfYear)
    {
      dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

      Calendar c = (Calendar)calendar.clone();
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      startOfDay = c.getTime().getTime();

      fivepm = (Calendar)calendar.clone();
      fivepm.set(Calendar.HOUR_OF_DAY, 17);
      fivepm.set(Calendar.MINUTE, 0);
      fivepm.set(Calendar.SECOND, 0);
      fivepm.set(Calendar.MILLISECOND, 0);

      eightam = (Calendar)calendar.clone();
      eightam.set(Calendar.HOUR_OF_DAY, 8);
      eightam.set(Calendar.MINUTE, 0);
      eightam.set(Calendar.SECOND, 0);
      eightam.set(Calendar.MILLISECOND, 0);

      nighttime = (Calendar)calendar.clone();
      nighttime.set(Calendar.HOUR_OF_DAY, 17);
      nighttime.set(Calendar.MINUTE, 30);
      nighttime.set(Calendar.SECOND, 0);
      nighttime.set(Calendar.MILLISECOND, 0);

      morningtime = (Calendar)calendar.clone();
      morningtime.set(Calendar.HOUR_OF_DAY, 6);
      morningtime.set(Calendar.MINUTE, 0);
      morningtime.set(Calendar.SECOND, 0);
      morningtime.set(Calendar.MILLISECOND, 0);

      presenceData.dayOfYearChanged();
    }
  }

  private void calculateRemaining()
  {
    if (isWorkingHours())
    {
      long ms = fivepm.getTime().getTime() - calendar.getTime().getTime();
      int hours = (int)(ms / 1000 / 60 / 60);
      int mins = (int)(ms / 1000 / 60) % 60;

      remainingString = String.format("%d:%02d to go", hours, mins);
    }
  }

  private void updateState()
  {
    State current = actual;

    if (calendar.before(morningtime))
    {
      current = State.Morning;
    }
    else if (calendar.after(nighttime))
    {
      current = State.Nighttime;
    }
    else if (calendar.before(eightam))
    {
      current = State.BeforeWork;
    }
    else if (calendar.after(fivepm))
    {
      current = State.AfterWork;
    }
    else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
    {
      current = State.Weekend;
    }
    else
    {
      current = State.WorkingHours;
    }

    if (current != actual)
    {
      state = current;
      actual = current;
      if (listener != null)
      {
        listener.stateChanged(state);
      }
    }
  }

  void setDataListener(DataListener listener)
  {
    this.listener = listener;
  }

  public void lockEvent(long timestamp)
  {
    presenceData.lockEvent(timestamp);
  }

  public void unlockEvent(long timestamp)
  {
    presenceData.unlockEvent(timestamp);
  }

  public List<PresenceEvent> getPresenceEvents()
  {
    return presenceData.getPresenceEvents();
  }

  public State getState()
  {
    return state;
  }
}

