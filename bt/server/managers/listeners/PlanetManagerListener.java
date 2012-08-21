package bt.server.managers.listeners;

import bt.common.elements.galaxy.InnerSpherePlanet;
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
public interface PlanetManagerListener
{
    public void PlanetChanged(InnerSpherePlanet isp);
    public void PlanetEditRequest(InnerSpherePlanet isp);
}
