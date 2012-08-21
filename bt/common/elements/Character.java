package bt.common.elements;

import java.util.Vector;

public class Character extends Personnel 
{
    private CharacterOrigin m_Origin = CharacterOrigin.INNER_SPHERE;
    private Vector<CharacterTraining> m_Training = new Vector<CharacterTraining>();
    private Vector<CharacterCombatExperience> m_CombatExperience = new Vector<CharacterCombatExperience>();
    
    private int[] m_PersonnelModifiers = new int[PersonnelModifier.values().length];
    private int[] m_PointPoolModifiers = new int[PointPoolModifier.values().length];

    
    public void addCombatExperience(CharacterCombatExperience cms)
    { m_CombatExperience.add(cms); }

    public void addTraining(CharacterTraining ct)
    {
    	m_Training.add(ct);
    }
    
    public void setOrigin(CharacterOrigin co)
    {
    	m_Origin = co;
    }

    public Vector<CharacterTraining> getTraining()
    { return m_Training; }
    
    public Vector<CharacterCombatExperience> getCombatExperience()
    { return m_CombatExperience; }
    
    public CharacterOrigin getOrigin()
    { return m_Origin; }

    public int getPersonnelModifier(PersonnelModifier pm)
    { return m_PersonnelModifiers[pm.ordinal()]; }
    
    public int getPointPoolModifier(PointPoolModifier ppm)
    { return m_PointPoolModifiers[ppm.ordinal()]; }
    
    public void setPersonnelModifier(PersonnelModifier pm, int mod)
    { m_PersonnelModifiers[pm.ordinal()] = mod; }
   
    public void setPointPoolModifier(PointPoolModifier ppm, int mod)
    { m_PersonnelModifiers[ppm.ordinal()] = mod; }
   
    
    public Character()
    {
    	
    }
    
    public Vector<CharacterTraining> getAvailableTraining()
    {
    	Vector<CharacterTraining> availableTraining = new Vector<CharacterTraining>();
    	
    	if (m_Origin == CharacterOrigin.CLAN)
    	{
    		if (m_Training.size() > 0)
    			return availableTraining;
    			
    		availableTraining.add(CharacterTraining.CLAN_DROPOUT);
    		availableTraining.add(CharacterTraining.CLAN_GRADUATE);
    		availableTraining.add(CharacterTraining.CLAN_HONORS_GRADUATE);
    	}
    	else
    	{
    		if (m_Training.size() > 1)
    			return availableTraining;
    		
    		if (m_Training.size() == 0)
    			availableTraining.add(CharacterTraining.FAMILY_TRAINED);
    		
    		if (m_Training.size() > 0 && !m_Training.contains(CharacterTraining.FAMILY_TRAINED))
    		{
    			availableTraining.add(CharacterTraining.FAMILY_TRAINED);
    		} else if (m_Training.size() == 0 || m_Training.contains(CharacterTraining.FAMILY_TRAINED))
    		{
    			availableTraining.add(CharacterTraining.ACADEMY_DROPOUT);
    			availableTraining.add(CharacterTraining.ACADEMY_GRADUATE);
    			availableTraining.add(CharacterTraining.ACADEMY_HONORS_GRADUATE);
    		}
    		
    	}
    	
    	return availableTraining;
    }
    
    public Vector<CharacterCombatExperience> getAvailableCombatExperience()
    {
    	Vector<CharacterCombatExperience> availableCombatService = new Vector<CharacterCombatExperience>();

    	if (m_CombatExperience.size() > 2)
    		return availableCombatService;
    	
    	int todCount = 0;
    	int coCount = 0;
    	int tbCount = 0;
    	for (CharacterCombatExperience ccb: m_CombatExperience)
    	{
    		switch (ccb)
    		{
    			case COVERT_OPS:
    				coCount++;
    				break;
    			case TRAINING_BATTALION:
    				tbCount++;
    				break;
    			case TOUR_OF_DUTY:
    				todCount++;
    		}
    	}
    	if (coCount == 0)
    		availableCombatService.add(CharacterCombatExperience.COVERT_OPS);
    	if (tbCount == 0)
    		availableCombatService.add(CharacterCombatExperience.TRAINING_BATTALION);
    	if (todCount < 3)
    		availableCombatService.add(CharacterCombatExperience.TOUR_OF_DUTY);
    	
    	return availableCombatService;
    }

    public void saveToElement(org.jdom.Element e)
    {
    	super.saveToElement(e);
    	
    	org.jdom.Element pme = new org.jdom.Element("PersonnelModifiers");
    	for (PersonnelModifier pm: PersonnelModifier.values())
    		pme.addContent(new org.jdom.Element(pm.toString()).setText(Integer.toString(m_PersonnelModifiers[pm.ordinal()])));
    	
    	e.addContent(pme);
    	
    	org.jdom.Element ppme = new org.jdom.Element("PointPoolModifiers");
    	for (PointPoolModifier ppm: PointPoolModifier.values())
    		ppme.addContent(new org.jdom.Element(ppm.toString()).setText(Integer.toString(m_PointPoolModifiers[ppm.ordinal()])));
    	
    	e.addContent(ppme);
    	
     	e.addContent(new org.jdom.Element("Origin").setText(m_Origin.toString()));
     	if (m_Training.size() > 0)
     	{
     		org.jdom.Element cte = new org.jdom.Element("CharacterTraining");
     		for (CharacterTraining ct : m_Training)
 			{
     			cte.addContent(new org.jdom.Element("Training").setText(ct.toString()));
 			}
     		e.addContent(cte);
     	}
     	
     	if (m_CombatExperience.size() > 0)
     	{
     		org.jdom.Element cae = new org.jdom.Element("CombatExperience");
     		for (CharacterCombatExperience ccb : m_CombatExperience)
 			{
     			cae.addContent(new org.jdom.Element("Experience").setText(ccb.toString()));
 			}
     		e.addContent(cae);
     	}
    }
    
    public void loadFromElement(org.jdom.Element e)
    {
    	super.loadFromElement(e);

    	org.jdom.Element pme = e.getChild("PersonnelModifiers");
    	for (PersonnelModifier pm: PersonnelModifier.values())
    		m_PersonnelModifiers[pm.ordinal()] = Integer.parseInt(pme.getChildText(pm.toString()));
    	
    	org.jdom.Element ppme = e.getChild("PointPoolModifiers");
    	for (PointPoolModifier ppm: PointPoolModifier.values())
    		m_PointPoolModifiers[ppm.ordinal()] = Integer.parseInt(ppme.getChildText(ppm.toString()));
    	    	
 		m_Origin = CharacterOrigin.fromString(e.getChildText("Origin"));

 		m_Training.clear();
     	org.jdom.Element TrainingElement = e.getChild("CharacterTraining");
     	if (TrainingElement != null)
     	{
     		for (Object eto: TrainingElement.getChildren("Training"))
     		{
     			m_Training.add(CharacterTraining.fromString(((org.jdom.Element)eto).getText()));
     		}
     	}

 		m_CombatExperience.clear();
     	org.jdom.Element AdvantageElement = e.getChild("CombatExperience");
     	if (AdvantageElement != null)
     	{
     		for (Object ato: AdvantageElement.getChildren("Experience"))
     		{
     			m_CombatExperience.add(CharacterCombatExperience.fromString(((org.jdom.Element)ato).getText()));
     		}
     		
     	}
    }    
}
