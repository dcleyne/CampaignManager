package bt.common.elements.design;

public class HeatSinkDesign extends DesignItem
{
    private String _HeatSinkType = "Single";
    
 	public void setHeatSinkType(String heatSinkType)
	{
		_HeatSinkType = heatSinkType;
	}


	public String getHeatSinkType()
	{
		return _HeatSinkType;
	}
   
	@Override
	public String getType()
	{
		return "Heat Sink";
	}

}
