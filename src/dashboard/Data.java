/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class Data
{
  private enum State {
    WorkingHours,
    AfterWork,
    BeforeWork,
    Weekend,
    Nighttime,
    Morning,
  };

  private State state;
  private int overrideState;
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

  private int dayOfYear;

  private String dateString;
  private String timeString;
  private String ampmString;
  private String remainingString;

  private String background;
  private int primaryColor;
  private int secondaryColor;
  private int backgroundColor;

  public Data()
  {
    dayOfYear = -1;

    overrideState = -1;

    dfs = new DateFormatSymbols();

    setBackground(Background.Name);
    setColors(0xff00ff00, 0xff00ee00, 0xff000000);
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

  public void setBackground(String bg)
  {
    background = bg;
  }

  public int[] getColors()
  {
    int[] array = new int[3];
    array[0] = primaryColor;
    array[1] = secondaryColor;
    array[2] = backgroundColor;
    return array;
  }

  public void setColors(int primary, int secondary, int background)
  {
    primaryColor = primary;
    secondaryColor = secondary;
    backgroundColor = background;
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
    if (overrideState == -1)
    {
      for (int i = 0; i < stateMap.length; ++i)
      {
        if (stateMap[i] == state)
        {
          overrideState = i;
          break;
        }
      }
    }

    overrideState = (overrideState + 1) % stateMap.length;

    if (stateMap[overrideState] == state)
    {
      overrideState = -1;
    }
  }

  private boolean isState(State x)
  {
    if (overrideState != -1)
    {
      return x == stateMap[overrideState];
    }
    else
    {
      return x == state;
    }
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

  private void updateDayOfYear()
  {
    if (calendar.get(Calendar.DAY_OF_YEAR) != dayOfYear)
    {
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
      nighttime.set(Calendar.HOUR_OF_DAY, 20);
      nighttime.set(Calendar.MINUTE, 0);
      nighttime.set(Calendar.SECOND, 0);
      nighttime.set(Calendar.MILLISECOND, 0);

      morningtime = (Calendar)calendar.clone();
      morningtime.set(Calendar.HOUR_OF_DAY, 6);
      morningtime.set(Calendar.MINUTE, 0);
      morningtime.set(Calendar.SECOND, 0);
      morningtime.set(Calendar.MILLISECOND, 0);
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
    if (calendar.before(morningtime))
    {
      state = State.Morning;
    }
    else if (calendar.after(nighttime))
    {
      state = State.Nighttime;
    }
    else if (calendar.before(eightam))
    {
      state = State.BeforeWork;
    }
    else if (calendar.after(fivepm))
    {
      state = State.AfterWork;
    }
    else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
    {
      state = State.Weekend;
    }
    else
    {
      state = State.WorkingHours;
    }
  }
}

