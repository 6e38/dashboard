
package dashboard;

class PaletteFactory
{
  static final Palette[] palettes = {
    new Palette(0xff00ff00, 0xff00ee00, 0xff000000),
    new Palette(0xff900000, 0xff700000, 0xff000000),
    new Palette(0xf00000aa, 0xff000080, 0xff333333),
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

