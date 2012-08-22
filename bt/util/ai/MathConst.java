package bt.util.ai;

/**
 * Utility class, keeping various mathematical constants and tests.
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

public class MathConst
{
	/**
	 * Private constructor to lock-out any attempts to make an object from this class.
	 */
	private MathConst() {}

	static public final boolean isUndefined( double a ) { return Double.isNaN( a ); }
	static public final boolean isDefined( double a ) { return !Double.isNaN( a ); }
	static public final boolean isZero( double a ) { return (Math.abs(a) < SMALL); }
	static public final boolean isZero( double a, double tol ) { return (Math.abs(a) < tol); }
	static public final boolean isOne( double a ) { return (Math.abs(a-1.0) < SMALL); }
	static public final boolean isOne( double a, double tol ) { return (Math.abs(a-1.0) < tol); }
	static public final boolean isNegative( double a ) { return (a < -SMALL); }
	static public final boolean isNegative( double a, double tol ) { return (a < -tol); }
	static public final boolean isPositive( double a ) { return (a > -SMALL); }
	static public final boolean isPositive( double a, double tol ) { return (a > -tol); }
	static public final boolean isEqual( double a, double b ) { return (Math.abs(a-b) < SMALL); }
	static public final boolean isEqual( double a, double b, double tol ) { return (Math.abs(a-b) < tol); }
	static public final boolean isGreater( double a, double b ) { return (a > (b + SMALL)); }
	static public final boolean isGreater( double a, double b, double tol ) { return (a > (b+tol)); }
	static public final boolean isLesser( double a, double b ) { return (a < (b - SMALL)); }
	static public final boolean isLesser( double a, double b, double tol ) { return (a < (b-tol)); }
	static public final boolean inRange( double a, double b, double c ) { return !isGreater(a, b) && !isGreater(b, c); }
	static public final boolean inRange( double a, double b, double c, double tol ) { return !isGreater(a, b, tol) && !isGreater(b, c, tol); }

	static public final boolean isOdd( int a ) { return ((a & 0x01) == 1); }
	static public final boolean isEven( int a ) { return ((a & 0x01) == 0); }

	static public final int  Sign( double a ) { return (isZero(a)?0:((a<0.0)?-1:1)); }
	static public final int  Sign( int a ) { return (isZero(a)?0:((a<0)?-1:1)); }

	// Note that the rounding place must be >1
	static public final double Round( double a ) { return Math.round(a*SIGNIFICANT)/SIGNIFICANT; }
	static public final double Round( double a, double place ) { return Math.round(a*place)/place; }

	static public final double Degrees( double a )	{ return (a*360.0) / TWOPI; }
	static public final double Radians( double a )	{ return (a*TWOPI) / 360.0; }

	static public final double PI = Math.PI;
	static public final double HALFPI = (Math.PI/2.0);
	static public final double QUARTERPI = (Math.PI/4.0);
	static public final double TWOPI = (Math.PI*2.0);
	static public final double SQRT2 = Math.sqrt(2.0);

	static public final double UNDEFINED = Double.NaN;

	static public final double SIGNIFICANT = 1e+7;
	static public final double SMALL = 1e-6;
	static public final double VSMALL = 1e-9;
}
