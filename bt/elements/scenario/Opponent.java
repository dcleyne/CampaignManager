package bt.elements.scenario;

import bt.elements.Faction;
import bt.elements.personnel.Rating;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;

public class Opponent
{
	private Faction _Faction;
	private Rating _Rating;
	private QualityRating _QualityRating;
	private TechRating _TechRating;
	
	public Faction getFaction()
	{
		return _Faction;
	}
	public void setFaction(Faction faction)
	{
		_Faction = faction;
	}
	public Rating getRating()
	{
		return _Rating;
	}
	public void setRating(Rating rating)
	{
		_Rating = rating;
	}
	public QualityRating getQualityRating()
	{
		return _QualityRating;
	}
	public void setQualityRating(QualityRating qualityRating)
	{
		_QualityRating = qualityRating;
	}
	public TechRating getTechRating()
	{
		return _TechRating;
	}
	public void setTechRating(TechRating techRating)
	{
		_TechRating = techRating;
	}
	
	public Opponent()
	{
		
	}
	
	public Opponent(Faction f, Rating r, QualityRating qr, TechRating tr)
	{
		_Faction = f;
		_Rating = r;
		_QualityRating = qr;
		_TechRating = tr;
	}
}
