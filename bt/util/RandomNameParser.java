package bt.util;

import java.util.ArrayList;

import bt.elements.unit.RandomName;

public abstract class RandomNameParser
{
	public abstract ArrayList<RandomName> parseRandomNames(String nameStream);
}
