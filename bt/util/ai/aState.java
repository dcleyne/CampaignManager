
package bt.util.ai;

import java.util.Iterator;

/**
 * This base class defines the State part of the A* search.  It represents a
 * position in a map or a state in a process.
 *
 * The state will have a key (identifier) used for access plus
 * several "distance" scores that indicate how far from the start state
 * this state is and an estimate of how far to the goal state is left.
 *
 * The state knows about its neighbors and can provide an iterator that
 * lists these neighbors.
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
abstract public class aState
{
	/// @name Construction
	//@{
	/**
	 * Generic, empty constructor.
	 */
	public aState()
	{}
	//@}

	/// @name Path
	//@{
	/**
	 * Return the state that led to this one (its parent).
	 */
	public aState getPrevious()
	{ return m_prev; }

	/**
	 * Set the state that leads to this one.
	 */
	public void setPrevious(
		aState prev )		///< Previous state
	{ m_prev = prev; }

	/**
	 * Return an iterator of all neighbors.  This method
	 * should create these neighbor states and infuse them
	 * with the distance we have moved so far.
	 */
	abstract public Iterator<aState> neighbors();
	//@}

	/// @name Scoring
	//@{
	/**
	 * Get the distance from the start.  Note that this is set by the
	 * neighbors() call and may be re-set during the aStar search.
	 */
	abstract public double getDistFromStart();

	/**
	 * Return or calculate an estimated distance to the goal state.
	 * This should under-estimate this distance if you want a good,
	 * efficient path.
	 */
	abstract public double getDistToGoal();

	/**
	 * Get the current, estimated, total cost this state represents.
	 */
	public double getCost()
	{ return getDistFromStart() + getDistToGoal(); }

	//@}

	/// @name State
	//@{
	/**
	 * Returns true when we have reached the goal state.
	 */
	abstract public boolean done();

	/**
	 * The key that identifies the state.
	 */
	abstract public Object getKey();

	/**
	 * Determine if two states are the same.
	 */
	abstract public boolean equals(aState state);
	//@}

	// ========================================================================

	private aState m_prev;			///< Previous state in this path
}

