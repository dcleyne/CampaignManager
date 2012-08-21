/**
 * Created on 29/11/2005
 * <p>Title: RenegadeLegion</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2005</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.server;

import bt.common.elements.unit.Player;

public interface ParticipantRegistrationFactoryListener
{
    void ParticipantAdded(Player p);
    void ParticipantRemoved(Player p);
}
