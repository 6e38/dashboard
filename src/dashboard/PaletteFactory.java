
package dashboard;

class PaletteFactory
{
  static final Palette[] palettes = {
    new Palette(0x00ff00, 0x00ee00, 0x000000),
    new Palette(0x900000, 0x700000, 0x000000),
    new Palette(0x0000aa, 0x000080, 0x333333),
  };

  static int index;

  static Palette get()
  {
    return palettes[index];
  }

  static Palette getNext()
  {
    index = (index + 1) % palettes.length;
    return palettes[index];
  }
}

