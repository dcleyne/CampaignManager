package bt.elements.unit;

/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public enum UnitDesignation
{
	REGIMENT,
	BATTALION,
	COMPANY,
	LANCE,
	REGIMENTALCOMBATTEAM,
	PLATOON,
	HQ;
	
	private static final String[] _Names = {
		"Regiment",
		"Battalion",
		"Company",
		"Lance",
		"Regimental Combat Team",
		"Platoon",
		"Head Quarters"
	};
	
	private static final boolean[] _ElementsAllowed = {
		false,
		false,
		false,
		true,
		false,
		true,
		true,
		false,
		true
	};

	private static final boolean[] _HQAllowed = {
		true,
		true,
		false,
		false,
		true,
		false,
		false,
		false,
		false
	};
	
	public String toString()
	{ return _Names[ordinal()]; }
	

	public boolean elementsAllowed()
	{ return _ElementsAllowed[ordinal()]; }
	
	public boolean hqAllowed()
	{ return _HQAllowed[ordinal()]; }
}
