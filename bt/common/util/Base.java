package bt.common.util;

/**
 * Title:        Space Conquest Game
 * Description:  The same old Game
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Daniel Cleyne
 * @version 1.0
 */

public class Base
{

    public static double AU = 1.496E8;
    public static double LYkm = 9.46E12;
    public static double LYau = 63240.0;
    public static double PCkm = 3.09E13;
    public static double PCau = PCkm / AU;
    public static double PCly = 3.26;
    public static double SunDiameter = 1.39E6;
    public static double EarthDiameter = 12756.0;
    public static double EarthDensity = 5.5;

    public static double fudgeVal = 3.53155146; // =(6 / LOG10(50)) I dreamed this up. It looks right but...

//public static String climatestrings[] = {"None","Regulated","Temperate","Cold","Hot"};
    public static String CityStrings[] =
        {
        "None", "Sealed Dome", "Aerial", "Subterranium", "Surface", "Mobile", "Orbital"};
    public static String LifeStrings[] =
        {
        "None", "Vary Rare", "Rare", "Common", "Abundant", "Rich", "Very Rich"};

    public static String PlanetStrings[] =
        {
        "None", "Primary", "Secondary", "Tertiary"};
    public static String PortStrings[] =
        {
        "None", "Commercial", "Industrial", "Military", "Private", "Pirate"};
    public static String GalaxyStrings[] =
        {
        "None", "Spiral", "Barred Spiral", "Elliptical", "Irregular"};

    public static String[] WorldTypeStrings =
        {
        "Rockball", "Icy Rockball", "Desert", "Hostile", "Greenhouse", "Ocean", "Hostile Subgiant", "Giant"};
    public static String[] SizeClassStrings =
        {
        "Tiny", "Very Small", "Small", "Standard", "Large", "Giant"};

    public static String[] AtmosphereStrings =
        {
        "None", "Trace", "Very Thin", "Thin", "Standard", "Dense", "Very Dense", "Superdense"};
    public static String[] AtmosphereTypeStrings =
        {
        "None", "Reducing", "Exotic", "Oxygen-Nitrogen", "Polluted", "Corrosive"};
    public static String[] AtmosphereContaminantStrings =
        {
        "None", "Chlorine/Flourine", "Sulfur Compounds", "Nitrogen Compounds", "Low Oxygen", "Pollutants", "High Carbon Dioxide", "High Oxygen", "Inert Gases", "Water Vapour"};
    public static String[] ClimateStrings =
        {
        "None", "Very Hot", "Tropical", "Hot", "Warm", "Earth Normal", "Cool", "Chilly", "Cold", "Very Cold", "Frozen"};
    public static String[] EcosphereStrings =
        {
        "No Life", "Protoorganisms", "Lower Plants", "Higher Plants", "Lower Animals", "Higher Animals", "Near-Intelligence", "Intelligence"};
    public static String[] TerrainTypeStrings =
        {
        "None", "Icy Barren", "Desert", "Hilly/Rough", "Mountainous/Volcanic", "Plains/Steppes", "Forest/Jungle", "Marsh/Swamp"};

    public static int Ia = 0;
    public static int Ib = 1;
    public static int II = 2;
    public static int III = 3;
    public static int IV = 4;
    public static int V = 5;
    public static int D = 6;

    public static String StarClassStrings[] =
        {
        "O", "B", "A", "F", "G", "K", "M"};
    public static String StarMagnitudeStrings[] =
        {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static String LuminosityClassStrings[] =
        {
        "Ia", "Ib", "II", "III", "IV", "V", "D"};
    public static String BodeConstantStrings[] =
        {
        "0.2", "0.3", "0.35", "0.4"};

    public static String GreekStrings[] =
        {
        "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi",
        "Omega"};

    public static String AnglicStrings[] =
        {
        "Ay", "Bee", "See", "Dee", "Ee", "Eff", "Gee", "Aitch", "Eye", "Jay", "Kay", "Ell", "Em", "En", "Oh", "Pee", "Cue", "Are", "Ess", "Tee", "You", "Vee", "Double-You", "Eks", "Wye", "Zee"};

    public static short HexNeighbours[][] =
        {
        /*   1 */
        {
        2, 100, 198, 296, 394, 2},
        /*   2 */
        {
        1, 100, 4, 3, 396, 394},
        /*   3 */
        {
        2, 4, 6, 5, 399, 396},
        /*   4 */
        {
        100, 101, 7, 6, 3, 2},
        /*   5 */
        {
        3, 6, 9, 8, 403, 399},
        /*   6 */
        {
        4, 7, 10, 9, 5, 3},
        /*   7 */
        {
        101, 103, 11, 10, 6, 4},
        /*   8 */
        {
        5, 9, 13, 12, 408, 403},
        /*   9 */
        {
        6, 10, 14, 13, 8, 5},
        /*  10 */
        {
        7, 11, 15, 14, 9, 6},
        /*  11 */
        {
        103, 106, 16, 15, 10, 7},
        /*  12 */
        {
        8, 13, 18, 17, 414, 408},
        /*  13 */
        {
        9, 14, 19, 18, 12, 8},
        /*  14 */
        {
        10, 15, 20, 19, 13, 9}
    };

    public Base()
    {
    }

    public static double log10(double n)
    {
        return Math.log(n) / Math.log(10);
    }

    public static int[] AbundanceMods =
        {
         -3, -2, -2, -1, +1, +3, 0};
    public static String[] MineralNames =
        {
        "Gemstones", "Rare Minerals", "Radioactives", "Heavy Metals", "Industrial Metals", "Light Metals", "Organics"};
    public static String[] AbundanceNames =
        {
        "Absent", "Scarce", "Ample", "Plentiful", "Extremely Plentiful"};

    public static String DetermineMineralValues()
    {
        int roll = Dice.d6(3);
        int Mod = 0;
        switch (roll)
        {
            case 3:
                Mod = -5;
                break;
            case 4:
                Mod = -4;
                break;
            case 5:
                Mod = -3;
                break;
            case 6:
            case 7:
                Mod = -2;
                break;
            case 8:
            case 9:
                Mod = -1;
                break;
            case 10:
            case 11:
                Mod = 0;
                break;
            case 12:
            case 13:
                Mod = 1;
                break;
            case 14:
            case 15:
                Mod = 2;
                break;
            case 16:
                Mod = 3;
                break;
            case 17:
                Mod = 4;
                break;
            case 18:
                Mod = 5;
                break;
        }
        return DetermineMineralValues(Mod);
    }

    public static String DetermineMineralValues(int Mod)
    {
        String Minerals = "";

        for (int Count = 0; Count < 7; Count++)
        {
            int roll = Dice.d6(3) + AbundanceMods[Count] + Mod;
            if (roll < 7)
            {
                Minerals += MineralNames[Count] + "=" + AbundanceNames[0] + ";"; //0
            }
            else if (roll < 9)
            {
                Minerals += MineralNames[Count] + "=" + AbundanceNames[1] + ";"; //1
            }
            else if (roll < 10)
            {
                Minerals += MineralNames[Count] + "=" + AbundanceNames[2] + ";"; //2
            }
            else if (roll < 12)
            {
                Minerals += MineralNames[Count] + "=" + AbundanceNames[3] + ";"; //3
            }
            else
            {
                Minerals += MineralNames[Count] + "=" + AbundanceNames[4] + ";"; //4
            }
        }
        return Minerals;
    }

    public static double GetDistance(double XRef, double YRef, double ZRef, double X, double Y, double Z)
    {
        double bd_x = Math.abs(XRef - X);
        double bd_y = Math.abs(YRef - Y);
        double bd_z = Math.abs(ZRef - Z);

        double bd_xy = Math.sqrt(Math.pow(bd_x, 2) + Math.pow(bd_y, 2));
        double bd_xyz = Math.sqrt(Math.pow(bd_xy, 2) + Math.pow(bd_z, 2));

        return bd_xyz;
    }

}
