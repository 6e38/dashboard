
package com.floorsix.dashboard;

class PaletteFactory
{
  private static final Palette[] palettes = {
    new Palette("dim", 0x555555, 0x555555, 0x000000),
    new Palette("darkblue", 0x5555ff, 0x333388, 0x000044),
    new Palette("terminal", 0x00ff00, 0x008800, 0x000000),
    new Palette("danger", 0xdd0000, 0xbb0000, 0x440000),
    new Palette("faint", 0x333333, 0x333333, 0x000000),
    new Palette("yellow", 0xffff00, 0x00ff00, 0x000000),
    new Palette("reverse", 0x000000, 0xbbbbbb, 0xffffff),
    new Palette("snow", 0xffffff, 0xbbbbbb, 0x222222),
  };

  static int index = 0;

  static String getNext()
  {
    index = (index + 1) % palettes.length;
    return palettes[index].name;
  }

  static Palette get(String name)
  {
    index = 0;

    for (int i = 0; i < palettes.length; i++)
    {
      if (palettes[i].name.equals(name))
      {
        index = i;
        break;
      }
    }

    return palettes[index];
  }
}

