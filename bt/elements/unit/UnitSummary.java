package bt.elements.unit;

import java.io.Serializable;
import java.util.Date;

public class UnitSummary implements Serializable 
{
	private static final long serialVersionUID = -5274629497132314349L;
	
	private String _UnitName;
	private String _UnitPlayer;
	private Date _EstablishDate;
	private String _Notes;
	
	public UnitSummary()
	{
		
	}
	
	public UnitSummary(String playerName, Unit u)
	{
		_UnitName = u.getName();
		_UnitPlayer = playerName;
		_EstablishDate = u.getEstablishDate();
		_Notes = u.getNotes();
	}
	
	public void setUnitName(String unitName) {
		this._UnitName = unitName;
	}
	public String getUnitName() {
		return _UnitName;
	}
	public void setUnitPlayer(String unitPlayer) {
		this._UnitPlayer = unitPlayer;
	}
	public String getUnitPlayer() {
		return _UnitPlayer;
	}
	public void setEstablishDate(Date establishDate) {
		this._EstablishDate = establishDate;
	}
	public Date getEstablishDate() {
		return _EstablishDate;
	}
	public void setNotes(String notes) {
		this._Notes = notes;
	}
	public String getNotes() {
		return _Notes;
	}
	
	
}
