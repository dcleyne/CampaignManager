
package bt.util.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * The heart of the A* search process.  It doesn't care what it
 * processes, so long as it conforms to the aState base class.
 *
 * Copyright &copy; 2003 Simulated Reality Systems, LLC.
 * http://www.simreal.com
 * Distributed under the terms of the GNU General Public License v3
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @nosubgrouping
 */
public class aStar
{
	/// @name Construction
	//@{
	/**
	 * The A* search requires both a starting state
	 * and a goal state.  The goal state, however, is embedded
	 * in the start state, possibly as a static common to all
	 * of the states.
	 */
	public aStar(aState start)	///< Starting state, with goal embedded
	{
		m_open = new HashMap<Object,aState>();
		m_closed = new HashMap<Object,aState>();

		m_open.put(start.getKey(), start);
	}
	//@}

	/// @name A* Search
	//@{
	/**
	 * Conduct the search from start to goal, returning an array
	 * that holds the path, or null if there is no path.
	 */
	public ArrayList<aState> search()
	{
		aState active;

		m_steps = 0;

		while (m_open.size() > 0)
		{
			// Work with the cheapest state in the open set
			//
			active = nextOpen();
			m_steps++;
			//
			// Are we there yet?
			//
			if (active.done())
			{ return buildPath(active); }
			//
			// No? Then add untouched neighbors to the
			// open list.  Touched neighbors get co-opted into
			// this path only if the scoring is better now.
			//
			// Note that neighbors() needs to set the true current
			// cost of the nodes it creates.
			//
			Iterator<aState> iter = active.neighbors();
			while ( (iter != null) && iter.hasNext())
			{
				aState next = (aState)iter.next();
				Object key = next.getKey();
				//
				// If this neighbor is already in the closed list,
				// see if the current path through it is cheaper and,
				// hence, useful.
				//
				if (m_closed.containsKey(key))
				{
					aState closed = (aState)m_closed.get(key);
					if (next.getCost() < closed.getCost())
					{ m_closed.remove(key); }
					else
					{ next = null; }
				}
				else // Not in closed; repeat test for open
				if (m_open.containsKey(next.getKey()))
				{
					aState open = (aState)m_open.get(key);
					if (next.getCost() < open.getCost())
					{ m_open.remove(key); }
					else
					{ next = null; }
				}
				//
				// If we haven't rejected this state because it is better
				// in one the open or closed list, put it on the open list.
				//
				if (next != null)
				{
					next.setPrevious(active);
					m_open.put(next.getKey(), next);
				}
			}
			//
			// We have squeezed this state dry, so put it on the closed list
			//
			m_closed.put(active.getKey(), active);
		}
		return null;
	}

	/**
	 * Get the next, cheapest, state from the open list.
	 * A second list sorted by cost would make this faster.
	 *
	 * Assumes that there be at least one state in the m_open
	 * list.
	 */
	private aState nextOpen()
	{
		Iterator<aState> iter = m_open.values().iterator();
		//
		// Seed our scan
		//
		aState best_state = (aState)iter.next();
		double best_cost = best_state.getCost();
		//
		// Now see if there are any cheaper states.
		//
		aState test;
		while (iter.hasNext())
		{
			test = (aState)iter.next();
			if (MathConst.isLesser(test.getCost(), best_cost))
			{
				best_state = test;
				best_cost = test.getCost();
			}
		}
		//
		// Now remove the best state from the open list and return it.
		//
		return (aState)m_open.remove(best_state.getKey());
	}

	/**
	 * Create a list of states that leads from the starting state
	 * up to and including the goal state.  This requires that we
	 * reverse the links that run from the goal to the start.
	 */
	private ArrayList<aState> buildPath(aState state)		///< End of the trail.
	{
		ArrayList<aState> list = new ArrayList<aState>();

		while (state != null)
		{
			list.add(0, state);
			state = state.getPrevious();
		}

		return list;
	}

	/**
	 * Report how many steps we have taken (how many states we have examined)
	 * in our search.
	 */
	public int getStepCount()
	{ return m_steps; }
	//@}

	// ========================================================================

	private HashMap<Object,aState> m_closed;	///< States already visited
	private HashMap<Object,aState> m_open;		///< States not yet visited

	private int m_steps;		///< A counter of how many states we touch
}

