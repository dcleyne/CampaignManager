package bt.ui.panels;

import java.awt.Dimension;


import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import bt.elements.personnel.Rating;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;

public class NewOpponentPanel extends JPanel 
{
	private static final long serialVersionUID = -4059960782857330761L;

	private JComboBox<Rating> _OpponentRatingComboBox;
	private JComboBox<QualityRating> _OpponentQualityRatingComboBox;
	private JComboBox<TechRating> _OpponentTechRatingComboBox;
	
	public NewOpponentPanel(Unit u) 
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		_OpponentRatingComboBox = new JComboBox<Rating>(Rating.values());
		add(_OpponentRatingComboBox);
		
		_OpponentQualityRatingComboBox = new JComboBox<QualityRating>(QualityRating.values());
		add(_OpponentQualityRatingComboBox);
		
		_OpponentTechRatingComboBox = new JComboBox<TechRating>(TechRating.values());
		add(_OpponentTechRatingComboBox);
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		_OpponentRatingComboBox.setSelectedItem(Rating.REGULAR);
		_OpponentQualityRatingComboBox.setSelectedItem(u.getQualityRating());
		_OpponentTechRatingComboBox.setSelectedItem(u.getTechRating());
	}
	

	public Rating getSelectedRating()
	{
		return (Rating) _OpponentRatingComboBox.getSelectedItem();
	}
	
	public QualityRating getSelectedQualityRating()
	{
		return (QualityRating) _OpponentQualityRatingComboBox.getSelectedItem();
	}
	
	public TechRating getSelectedTechRating()
	{
		return (TechRating) _OpponentTechRatingComboBox.getSelectedItem();
	}
	
	
}
