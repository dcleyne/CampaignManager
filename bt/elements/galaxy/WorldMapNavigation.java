package bt.elements.galaxy;

import java.util.Vector;

import bt.util.Dice;
import bt.util.Logger;

/**
 * Title:        Ant
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */

public class WorldMapNavigation
{

    private int   m_FaceLength;
    private int   m_MapSize;
    private int   m_NumRows;
    private int   m_MapSectorCount;
    private int[] m_RowStarts;
    private int[] m_RowEnds;
    private int[] m_HexNeighbours;
    private int m_SegmentSize;
    private int m_SegmentRows;
    private int m_MapRowHeight;


    public WorldMapNavigation()
    {
		m_FaceLength = 0;
		m_MapSize = 0;
		m_NumRows = 0;
    }

    public void initialiseMap(int faceLength)
    {
		m_FaceLength = faceLength;
		m_MapSize = 10 * (faceLength * faceLength);
	        m_MapSectorCount = m_MapSize + 2;
	        m_NumRows = (3 * faceLength) + 2;
	        m_SegmentSize = m_FaceLength * m_FaceLength;
	        m_SegmentRows = (m_FaceLength * 2) - 1;
	        m_MapRowHeight = (m_FaceLength * 3) + 1;
	
		initialiseRows();
		initialiseHexNeighbours();
    }

    public int getHexNeighbour(int hex, int direction)
    {
    	while (direction < 1)
    		direction += 6;
    	
    	while (direction > 6) 
    		direction -= 6;
    	
		int Offset = (hex - 1) * 6;
		Offset += direction - 1;
		if (Offset < 0) return hex;
		return m_HexNeighbours[Offset];
    }

    public int[] getHexNeighbours(int hex)
    {
        int[] Neighbours = new int[6];
        for (int i = 0; i < 6; i++)
            Neighbours[i] = getHexNeighbour(hex,i + 1);

        return Neighbours;
    }

    public int getMapSize()
    {
    	return m_MapSize;
    }
    
    public int getSegmentSize()
    {
        return m_SegmentSize;
    }
    
    public int getSegmentCentre(int segment)
    {
    	int offset = ((segment - 1) * m_SegmentSize) + 1;
        return offset + (m_SegmentSize / 2);
    }

    public int getSegmentStart(int segment)
    {
        return ((segment - 1) * m_SegmentSize) + 1;
    }
    
    public int getSegmentEnd(int segment)
    {
        return (segment * m_SegmentSize) + 1;
    }

    public int getSegmentRows()
    {
        return m_SegmentRows;
    }

    public int getMapRowHeight()
    {
        return m_MapRowHeight;
    }

    public int getMapFaceLength()
    {
        return m_FaceLength;
    }

    private void initialiseRows( )
    {
		m_RowStarts = new int[m_NumRows];
		m_RowEnds = new int[m_NumRows];
	
		int RowLen = 1;
		int CurEnd = 2;
		for (int i = 0; i < m_SegmentRows; i++)
		{
	            m_RowEnds[i] = CurEnd;
	            m_RowStarts[i] = m_RowEnds[i] - RowLen + 1;
	
	            if (i < m_FaceLength-1)
	                RowLen++;
	            else
	                RowLen--;
	
	            CurEnd += RowLen;
		}
		RowLen = m_FaceLength - 1;
		CurEnd += Dice.HexRowContent(m_FaceLength);  
		for (int i = m_SegmentRows + 1; i < m_NumRows; i++)
		{
            m_RowEnds[i] = CurEnd;
            m_RowStarts[i] = m_RowEnds[i] - RowLen + 1;

            RowLen--;

            CurEnd += RowLen;
		}
    }

    private void initialiseHexNeighbours( )
    {
		m_HexNeighbours = new int[(m_MapSize + 2) * 6];
	
		int FaceSize = m_FaceLength * m_FaceLength;
		int Diagonal = (FaceSize * 2);
	
		initialiseTopLeft(FaceSize,Diagonal);
		initialiseBottomLeft(FaceSize,Diagonal);
		fillHexSpaces(Diagonal);
	
		calculatePoles(Diagonal);
    }

    private void initialiseTopLeft( int faceSize, int diagonal )
    {
		//Fill In Top Left Face
		int CurRow = 0;
		int Offset = 0;
		int i = 0;
		for (i = 0; i < faceSize; i++)
		{
            CurRow = getRow(i + 2);
            Offset = (i + 1) * 6;

            //Direction 1
            if (CurRow == 1)
                m_HexNeighbours[Offset] = 1;
            else if (i + 2 == getRowEnds(CurRow))
            {
                if (CurRow <= m_FaceLength)
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + diagonal;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow)) + 1;
            }
            else
            {
                m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow));
                if (CurRow > m_FaceLength) m_HexNeighbours[Offset]++;
            }

            Offset++;
            //Direction 2
            if (CurRow == 1)
                m_HexNeighbours[Offset] = i + diagonal + 2;
            else if (i + 2 == getRowEnds(CurRow))
            {
                if (CurRow <= m_FaceLength)
                    m_HexNeighbours[Offset] = (i + 2) + diagonal - CurRow + 1;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - m_FaceLength) + faceSize;
            }
            else
                m_HexNeighbours[Offset] = i + 2 + 1;

            Offset++;
            //Direction 3
            if (CurRow >= m_FaceLength)
                if (i + 2 == getRowEnds(CurRow))
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - m_FaceLength + 1) + faceSize;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow));
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow)) + 1;

            Offset++;
            //Direction 4
            if (CurRow >= m_FaceLength)
                if (i + 2 == getRowStarts(CurRow))
                    m_HexNeighbours[Offset] = getRowEnds(CurRow - m_FaceLength + 1) + (faceSize * 9);
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow)) - 1;
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow));

            Offset++;
            //Direction 5
            if (CurRow == 1)
                m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) - (diagonal - CurRow);
            else if (i + 2 == getRowStarts(CurRow))
            {
                if (CurRow < m_FaceLength)
                	m_HexNeighbours[Offset] = CurRow + getRowEnds(CurRow) - diagonal + 1;
                else
                    if (CurRow == m_FaceLength)
                        m_HexNeighbours[Offset] = getRowStarts(CurRow) - (diagonal - CurRow + 1);
                    else
                        m_HexNeighbours[Offset] = getRowEnds(CurRow - m_FaceLength) - faceSize;
            }
            else
                m_HexNeighbours[Offset] = i + 2 - 1;

            if (m_HexNeighbours[Offset] < 2)
                m_HexNeighbours[Offset] += m_MapSize;

            Offset++;
            //Direction 6
            if (CurRow == 1)
                m_HexNeighbours[Offset] = getRowStarts(CurRow) - (diagonal - CurRow + 1);
            else if (i + 2 == getRowStarts(CurRow))
            {
                if (CurRow <= m_FaceLength)
                    if (CurRow == m_FaceLength)
                        m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow));
                    else
                        m_HexNeighbours[Offset] = getRowStarts(CurRow) - (diagonal - CurRow + 1);
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) - (i + 2 - getRowStarts(CurRow));
            }
            else if (CurRow <= m_FaceLength)
                m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow)) - 1;
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow));

            if (m_HexNeighbours[Offset] < 2)
                m_HexNeighbours[Offset] += m_MapSize;
		}
    }

    private void initialiseBottomLeft( int faceSize, int diagonal )
    {
	 	//Fill In Bottom Left Face
		int CurRow = 0;
		int Offset = 0;
		int i = 0;
		for (i = 0; i < faceSize; i++)
		{
            CurRow = getRow(i + 2);
            Offset = (i + faceSize + 1) * 6;

            //Direction 1
            if (CurRow <= m_FaceLength)
                if (i + 2 == getRowEnds(CurRow))
                    m_HexNeighbours[Offset] = getRowStarts(m_FaceLength + (CurRow - 1)) + faceSize;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow));
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow)) + 1;

            m_HexNeighbours[Offset] += faceSize;


            Offset++;
            //Direction 2
            if (i + 2 == getRowEnds(CurRow))
                if (CurRow < m_FaceLength)
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + m_FaceLength) + faceSize;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow) + diagonal;
            else
                m_HexNeighbours[Offset] = i + 2 + 1;

            m_HexNeighbours[Offset] += faceSize;


            Offset++;
            //Direction 3
            if (CurRow == m_SegmentRows)
                m_HexNeighbours[Offset] = m_MapSize + 2 - faceSize;
            else if (CurRow >= m_FaceLength)
                if (i + 2 == getRowEnds(CurRow))
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + diagonal;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow));
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow)) + 1;

            m_HexNeighbours[Offset] += faceSize;


            Offset++;
            //Direction 4
            if (CurRow == m_NumRows)
                m_HexNeighbours[Offset] = getRowStarts(CurRow) - diagonal;
            else if (CurRow >= m_FaceLength)
                if (i + 2 == getRowStarts(CurRow))
                    if (CurRow == m_FaceLength)
                        m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow));
                    else
                        m_HexNeighbours[Offset] = getRowEnds(CurRow) - diagonal;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow)) - 1;
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow + 1) + (i + 2 - getRowStarts(CurRow));

            m_HexNeighbours[Offset] += faceSize;

            if (m_HexNeighbours[Offset] < 2)
                m_HexNeighbours[Offset] += m_MapSize;


            Offset++;
            //Direction 5
            if (i + 2 == getRowStarts(CurRow))
                if (CurRow < m_FaceLength)
                    m_HexNeighbours[Offset] = getRowEnds(m_FaceLength + CurRow) - faceSize;
                else if (CurRow == m_FaceLength)
                    m_HexNeighbours[Offset] = getRowEnds(CurRow) - diagonal;
                else
                    m_HexNeighbours[Offset] = getRowEnds(CurRow - 1) - diagonal;
            else
                m_HexNeighbours[Offset] = i + 2 - 1;

            m_HexNeighbours[Offset] += faceSize;

            if (m_HexNeighbours[Offset] < 2)
                m_HexNeighbours[Offset] += m_MapSize;


            Offset++;
            //Direction 6
            if (CurRow <= m_FaceLength)
                if (i + 2 == getRowStarts(CurRow))
                    m_HexNeighbours[Offset] = getRowEnds(m_FaceLength + (CurRow - 1)) - faceSize;
                else
                    m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow)) - 1;
            else
                m_HexNeighbours[Offset] = getRowStarts(CurRow - 1) + (i + 2 - getRowStarts(CurRow));

            m_HexNeighbours[Offset] += faceSize;
		}
   }

    private void fillHexSpaces( int diagonal )
    {
        //Fill in Remaining 4 diagonals
        int Offset = 6 + (diagonal * 6);
        while (Offset < ((m_MapSize + 2) * 6) - 6)
        {
            if (m_HexNeighbours[Offset - (diagonal * 6)] != 1 && m_HexNeighbours[Offset - (diagonal * 6)] != m_MapSize + 2 )
            {
                m_HexNeighbours[Offset] = m_HexNeighbours[Offset - (diagonal * 6)] + diagonal;
                //Now fix up those that need to wrap
                while (m_HexNeighbours[Offset] >= m_MapSize + 2)
                    m_HexNeighbours[Offset] -= m_MapSize;
            }
            else
                m_HexNeighbours[Offset] = m_HexNeighbours[Offset - (diagonal * 6)];
            Offset++;
        }
    }

    private void calculatePoles( int diagonal )
    {
        int Offset = 0;
        m_HexNeighbours[Offset++] = 2 + (2 * diagonal);
        m_HexNeighbours[Offset++] = 2 + diagonal;
        m_HexNeighbours[Offset++] = 2;
        m_HexNeighbours[Offset++] = 2 + (4 * diagonal);
        m_HexNeighbours[Offset++] = 2 + (3 * diagonal);
        m_HexNeighbours[Offset++] = 2 + (2 * diagonal);

        Offset = ((m_MapSize + 2) * 6) - 6;
        m_HexNeighbours[Offset++] = m_MapSize - diagonal + 1;
        m_HexNeighbours[Offset++] = m_MapSize - (2 * diagonal) + 1;
        m_HexNeighbours[Offset++] = m_MapSize - (3 * diagonal) + 1;
        m_HexNeighbours[Offset++] = m_MapSize - (3 * diagonal) + 1;
        m_HexNeighbours[Offset++] = m_MapSize - (4 * diagonal) + 1;
        m_HexNeighbours[Offset++] = m_MapSize + 1;
    }

    private int getRow(int hex)
    {
		int CurRow = 1;
		while (hex > m_RowEnds[CurRow-1])
	            CurRow++;
	
		return CurRow;
    }

    public int getRowStarts(int row)
    {
		return m_RowStarts[row-1];
    }

    public int getRowEnds(int row)
    {
    	return m_RowEnds[row-1];
    }

    public int getMapSectorCount()
    {
        return m_MapSize + 2;
    }

    public int getSectorPathLength(int startSector, int endSector)
    {
        int[] Path = getSectorPath(startSector,endSector);
        int PathLength = Path.length;
        Path = null;
        return PathLength;
    }

    public int[] getSectorPath(int startSector, int endSector)
    {
        try
        {
            int[] RightPath = getShortestSectorPath(startSector, endSector, false);
            int[] LeftPath = getShortestSectorPath(startSector, endSector, true);

            if (RightPath.length <= LeftPath.length)
            {
                LeftPath = null;
                return RightPath;
            }
            else
            {
                RightPath = null;
                return LeftPath;
            }
        }
        catch (Exception e)
        {
            Logger.Log(Logger.LEVEL_CRITICAL,e.toString());
            Logger.Log(Logger.LEVEL_CRITICAL,"GetSectorPath FAILED : StartSector(" + Integer.toString(startSector) + ") EndSector(" + Integer.toString(endSector) + ")");
            return null;
        }
    }


    public int[] getShortestSectorPath(int startSector, int endSector, boolean goLeft)
    {
        if (startSector < 1 || endSector < 1) return null;

        Vector<Integer> vPath = new Vector<Integer>();
        int EndSegment = getSegment(endSector);
        int EndSegmentRow = getSegmentRow(endSector);
        int EndHexMapRow = getMapRow(endSector);
        int CurrentHex = startSector;
        int CurrentSegment = getSegment(CurrentHex);
        int CurrentSegmentRow = getSegmentRow(CurrentHex);
        int CurrentHexMapRow = getMapRow(CurrentHex);

        /*
        int EndQuadrant = GetSegmentQuadrant(EndSector);
        boolean EndSegNorthHemi = IsSegmentNorthern(EndSegment);
        int CurrentQuadrant = GetSegmentQuadrant(CurrentHex);
        boolean CurrentSegNorthHemi = IsSegmentNorthern(CurrentSegment);
*/
        vPath.add(startSector);

        while (CurrentHex != endSector)
        {
            int NextHex = CurrentHex;

            if (NextHex != endSector)
            {
                if (CurrentSegment == EndSegment)
                {
                    //Ok so we're in the same segment now
                    if (CurrentSegmentRow == EndSegmentRow) // Go east or west
                    {
                        if (CurrentHex < endSector)
                            NextHex = getHexNeighbour(CurrentHex,2); //Go east young man
                        else
                            NextHex = getHexNeighbour(CurrentHex,5); //Go west young man
                    }
                    else
                    { //Choose between direction 6 and 1, 4 and 3
                        if (CurrentHex < endSector)
                        { //We're choosing between 4 and 3
                            if (isToRightOf(CurrentHex,endSector))
                                NextHex = getHexNeighbour(CurrentHex,3);
                            else
                                NextHex = getHexNeighbour(CurrentHex,4);
                        }
                        else
                        { //We're choosing between 6 and 1
                            if (isToRightOf(CurrentHex,endSector))
                                NextHex = getHexNeighbour(CurrentHex,1);
                            else
                                NextHex = getHexNeighbour(CurrentHex,6);
                        }
                    }
                }
                else
                {
                    boolean found = false;
                    if (EndSegment == 0)
                    {
                        for (int i = 1; i < 7; i++)
                            if (getHexNeighbour(CurrentHex, i) == endSector)
                            {
                                NextHex = endSector;
                                found = true;
                            }
                        if (!found)
                            NextHex = getHexNeighbour(CurrentHex, 1);
                    } else if (EndSegment == 11)
                    {
                        for (int i = 1; i < 7; i++)
                            if (getHexNeighbour(CurrentHex, i) == endSector)
                            {
                                NextHex = endSector;
                                found = true;
                            }
                        if (!found)
                            NextHex = getHexNeighbour(CurrentHex, 4);
                    }
                    else
                    {
//                        if ((CurrentSegment > EndSegment && (CurrentSegment - EndSegment) < 5) ||
//                            (CurrentSegment < EndSegment && (EndSegment - CurrentSegment) > 5))
                        if (goLeft)
                        {
                            if (CurrentHexMapRow < EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 4);

                            if (CurrentHexMapRow > EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 6);

                            if (CurrentHexMapRow == EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 5);
                        }
                        else
                        {
                            if (CurrentHexMapRow < EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 3);

                            if (CurrentHexMapRow > EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 1);

                            if (CurrentHexMapRow == EndHexMapRow)
                                NextHex = getHexNeighbour(CurrentHex, 2);
                        }
                    }
                }
            }
            vPath.add(NextHex);


            CurrentHex = NextHex;
            CurrentSegment = getSegment(CurrentHex);
            CurrentSegmentRow = getSegmentRow(CurrentHex);
            /*
            CurrentQuadrant = GetSegmentQuadrant(CurrentHex);
            CurrentSegNorthHemi = IsSegmentNorthern(CurrentSegment);
            */
            CurrentHexMapRow = getMapRow(CurrentHex);


        }

        int[] Path = new int[vPath.size()];
        for (int i = 0; i < Path.length; i++)
            Path[i] = ((Integer)vPath.elementAt(i)).intValue();

        vPath.clear();
        vPath = null;
        return Path;
    }

    public boolean isToRightOf(int sector1, int sector2)
    {
//        boolean value = false;
        int Sec1Row = getSegmentRow(sector1);
        int Sec2Row = getSegmentRow(sector2);

        if (Sec1Row == Sec2Row) return (sector2 > sector1);

        int Offset1 = getSegmentRowOffset(sector1) + getRowOffset(Sec1Row);
        int Offset2 = getSegmentRowOffset(sector2) + getRowOffset(Sec2Row);

        if (Offset1 == Offset2)
            if (Sec1Row > m_FaceLength)
                return (Sec1Row > Sec2Row);
            else if (Sec1Row < m_FaceLength)
                return (Sec1Row < Sec2Row);
            else
            {
                if (Sec2Row < m_FaceLength)
                    return (Sec1Row < Sec2Row);
                else
                    return (Sec1Row > Sec2Row);
            }

        else
            return (Offset2 > Offset1);

    }

    public boolean isToLeftOf(int sector1, int sector2)
    {
        return !isToRightOf(sector1,sector2);
    }


    public int getSegment(int hexIndex)
    {
        if (hexIndex == 1) return 0; //North Pole
        if (hexIndex == m_MapSectorCount) return 11; //South Pole

        int Segment = (hexIndex-1) / m_SegmentSize;
        if ((hexIndex-1) % m_SegmentSize > 0) Segment++;

        return Segment;
    }

    public int getSegmentRow(int hex)
    {
        int Row = 1;
        if (hex == 1 || hex == m_MapSectorCount) return Row;

        int StartHex = ((getSegment(hex) - 1) * m_SegmentSize) + 1;
        int Offset = hex - StartHex;
        int RowLength = 1;
        boolean found = false;
        while (!found)
        {
            if (Offset <= RowLength)
            {
                found = true;
                break;
            }

            Offset -= RowLength;
            if (Row < m_FaceLength)
                RowLength++;
            else
                RowLength--;

            Row++;
        }
        return Row;
    }

    public int getSegmentRowOffset(int hex)
    {
        int Row = 1;
        if (hex == 1 || hex == m_MapSectorCount) return Row;

        int StartHex = ((getSegment(hex) - 1) * m_SegmentSize) + 1;
        int Offset = hex - StartHex;
        int RowLength = 1;
        boolean found = false;
        while (!found)
        {
            if (Offset <= RowLength)
            {
                found = true;
                break;
            }

            Offset -= RowLength;
            if (Row < m_FaceLength)
                RowLength++;
            else
                RowLength--;

            Row++;
        }
        return Offset;
    }

    public int getSegmentQuadrant(int hex)
    {
        int Row = 1;
        if (hex == 1 || hex == m_MapSectorCount) return Row;

        int StartHex = ((getSegment(hex) - 1) * m_SegmentSize);
        int Offset = hex - StartHex;
        int RowLength = 1;
        boolean found = false;
        while (!found)
        {
            if (Offset < RowLength)
            {
                found = true;
                break;
            }

            Offset -= RowLength;
            if (Row <= m_FaceLength)
                RowLength++;
            else
                RowLength--;

            Row++;
        }
        if (Row <= m_FaceLength)
        {
            if (Offset < (RowLength / 2))
                return 1;
            else
                return 2;
        }
        else
        {
            if (Offset < (RowLength / 2))
                return 3;
            else
                return 4;
        }
    }

    private int getRowOffset(int row)
    {
        if (row == m_FaceLength) return 0;

        int Offset = 0;
        if (row < m_FaceLength)
            Offset = m_FaceLength - row;
        else
            Offset = row - m_FaceLength;

        return Offset;
    }
    
    public boolean isHexNorthern(int hex)
    {
    	return isSegmentNorthern(getSegment(hex));
    }

    public boolean isHexSouthern(int hex)
    {
    	return isSegmentSouthern(getSegment(hex));
    }

    
    public boolean isSegmentNorthern(int segment)
    {
        if (segment == 0) return true;
        if (segment == 11) return false;

        int res = (segment % 2);
        return (res != 0);
    }

    public boolean isSegmentSouthern(int segment)
    {
        return !isSegmentNorthern(segment);
    }

    public int getMapRow(int hex)
    {
        if (hex == 1) return 1;
        if (hex == m_MapSectorCount) return m_MapRowHeight + 1;

        int SegmentRow = getSegmentRow(hex) + 1;
        if (isSegmentSouthern(getSegment(hex)))
            SegmentRow += m_FaceLength;

        return SegmentRow;
    }
    
    public boolean isRowStart(int sector)
    {
    	int segment = getSegment(sector);
    	if (segment == 0)
    		return true;
    	if (segment == 11)
    		return true;

    	int segmentOffset = (segment - 1) * m_SegmentSize;
    	
    	for (int i = 0; i < m_RowStarts.length; i++ )
    		if (m_RowStarts[i] + segmentOffset == sector)
    			return true;
    	
    	return false;
    }

    public boolean isRowEnd(int sector)
    {
    	int segment = getSegment(sector);
    	if (segment == 0)
    		return true;
    	if (segment == 11)
    		return true;

    	int segmentOffset = (segment - 1) * m_SegmentSize;
    	
    	for (int i = 0; i < m_RowEnds.length; i++ )
    		if (m_RowEnds[i] + segmentOffset == sector)
    			return true;
    	
    	return false;
    }
    
    public boolean isClockwiseFrom(int centerHex, int referenceHex, int checkHex)
    {
        int CenterRow = getMapRow(centerHex);
        int ReferenceRow = getMapRow(referenceHex);
        int CheckRow = getMapRow(checkHex);

//        int CenterSegment = GetSegment(CenterHex);
//        int ReferenceSegment = GetSegment(ReferenceHex);
//        int CheckSegment = GetSegment(CheckHex);

        boolean Clockwise = false;
        if (CenterRow == ReferenceRow)
        {
            if (isToRightOf(centerHex,referenceHex))
            {
                if (CheckRow >= ReferenceRow)
                    Clockwise = true;
                else
                    Clockwise = false;
            }
            else
            {
                if (CheckRow <= ReferenceRow)
                    Clockwise = true;
                else
                    Clockwise = false;
            }
        }
        else if (CenterRow > ReferenceRow)
            if (isToRightOf(centerHex,referenceHex))
            {
                if (isToLeftOf(referenceHex,checkHex) && CheckRow < ReferenceRow)
                    Clockwise = false;
                else
                    Clockwise = true;
            }
            else
            {
                if (isToRightOf(referenceHex,checkHex) && CheckRow > ReferenceRow)
                    Clockwise = false;
                else
                    Clockwise = true;
            }
        else
        {
            if (isToRightOf(centerHex,referenceHex))
            {
                if (isToRightOf(referenceHex,checkHex) && CheckRow < ReferenceRow)
                    Clockwise = false;
                else
                    Clockwise = true;
            }
            else
            {
                if (isToLeftOf(referenceHex,checkHex) && CheckRow > ReferenceRow)
                    Clockwise = false;
                else
                    Clockwise = true;
            }
        }
        return Clockwise;
    }
    
    public int getHexOffsetByColsRows(int hex, int cols, int rows)
    {
    	int newHex = hex;
    	//Translate in 'x' direction
    	int xDir = cols >= 0 ? 2 : 5;
    	for (int i = 0; i < Math.abs(cols); i ++)
    		newHex = getHexNeighbour(newHex,xDir);
    	
    	//Translate in 'y' direction
    	for (int j = 0; j < Math.abs(rows); j ++)
    	{
    		int yDir = 0;
    		boolean evenRow = (getMapRow(newHex) % 2) == 0;
    		if (rows < 0)
				yDir = evenRow ? 1 : 6;
    		else
    			yDir = evenRow ? 4 : 3;
    		
    		newHex = getHexNeighbour(newHex,yDir);
    	}
    	
    	return newHex;
    }
    
    public Vector<Integer> getSurroundingRing(int sector, int distance)
    {    	
    	Vector<Integer> core = new Vector<Integer>();
    	Vector<Integer> outerRing = new Vector<Integer>();
    	outerRing.add(sector);
    	for (int ring = 0; ring < distance; ring++)
    	{
    		Vector<Integer> nextRing = new Vector<Integer>();
    		for (Integer sec: outerRing)
    		{
    			int[] neighbours = getHexNeighbours(sec);
    			for (int i = 0; i < 6; i++)
    				if (!core.contains(neighbours[i]) && !outerRing.contains(neighbours[i]))
    					nextRing.add(neighbours[i]);    			
    		}
    		core.addAll(outerRing);
    		outerRing = new Vector<Integer>(nextRing);
    	}
    	
    	return outerRing;
    }
    
    public Vector<Integer> getSurroundingSectors(int sector, int distance)
    {    	
    	Vector<Integer> core = new Vector<Integer>();
    	Vector<Integer> outerRing = new Vector<Integer>();
    	outerRing.add(sector);
    	for (int ring = 0; ring < distance; ring++)
    	{
    		Vector<Integer> nextRing = new Vector<Integer>();
    		for (Integer sec: outerRing)
    		{
    			int[] neighbours = getHexNeighbours(sec);
    			for (int i = 0; i < 6; i++)
    				if (!core.contains(neighbours[i]) && !outerRing.contains(neighbours[i]))
    					nextRing.add(neighbours[i]);    			
    		}
    		core.addAll(outerRing);
    		outerRing = new Vector<Integer>(nextRing);
    	}
    	
		core.addAll(outerRing);
		Integer centre = sector;
		while (core.contains(centre))
			core.remove(centre);
    	return core;
    }
    
    public int getDirectionOfNeighbour(int hex, int neighbour)
    {
    	for (int i = 1; i < 7; i++)
    		if (getHexNeighbour(hex,i) == neighbour)
    			return i;
    	
    	return -1;
    }
    
    public int getOppositeDirection(int dir)
    {
    	int newDir = dir - 3;
    	if (newDir < 1)
    		newDir += 6;
    	return newDir;
    }
}
