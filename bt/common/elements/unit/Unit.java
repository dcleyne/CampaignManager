package bt.common.elements.unit;

import java.io.Serializable;

import java.util.Date;
import java.util.Vector;

import bt.common.elements.Ammunition;
import bt.common.elements.Asset;
import bt.common.elements.Battlemech;
import bt.common.elements.ElementType;
import bt.common.elements.Item;
import bt.common.elements.personnel.Personnel;

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
public class Unit implements Serializable
{
	private static final long serialVersionUID = 1;
	
    private String _Name;
    private Player _Player;
    
    private Date _EstablishDate;
    private String _Notes;

    private Vector<Personnel> _Personnel = new Vector<Personnel>();
    private Vector<Asset> _Assets = new Vector<Asset>();

    private Vector<Ammunition> _SpareAmmunition = new Vector<Ammunition>();
    private Vector<Item> _SpareParts = new Vector<Item>();

    private Vector<Asset> _SalvagedAssets = new Vector<Asset>();
    
    private Vector<String> _AssignedMissions = new Vector<String>();

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public Player getPlayer()
	{
		return _Player;
	}

	public void setPlayer(Player player)
	{
		_Player = player;
	}

	public Date getEstablishDate() {
		return _EstablishDate;
	}

	public void setEstablishDate(Date establishDate) {
		_EstablishDate = establishDate;
	}

	public String getNotes() {
		return _Notes;
	}

	public void setNotes(String notes) {
		_Notes = notes;
	}

	public Vector<Personnel> getPersonnel()
	{
		return _Personnel;
	}

	public Vector<Asset> getAssets()
	{
		return _Assets;
	}

	public Vector<Battlemech> getBattlemechs()
	{
		Vector<Battlemech> mechs = new Vector<Battlemech>();
		for (Asset a : _Assets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech)a);
		
		return mechs;
	}

	public Vector<Ammunition> getSpareAmmunition()
	{
		return _SpareAmmunition;
	}

	public Vector<Item> getSpareParts()
	{
		return _SpareParts;
	}

	public Vector<Battlemech> getSalvagedMechs()
	{
		Vector<Battlemech> mechs = new Vector<Battlemech>();
		for (Asset a : _SalvagedAssets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech)a);

		return mechs;
	}

	public Vector<Asset> getSalvagedAssets()
	{
		return _SalvagedAssets;
	}

	public Vector<String> getAssignedMissions()
	{
		return _AssignedMissions;
	}
	
	public int getUnitStrength()
	{
		// TODO This will have to be expanded to include other unit types.
		return _Assets.size();
	}
	
	public int getUnitBV()
	{
		// TODO This will have to be expanded to include other unit types.
		int totalBV = 0;
		for (Asset bm : _Assets)
			totalBV += bm.getAdjustedBV();
		
		return totalBV;
	}
    
    
}
