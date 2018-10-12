package bt.ui.renderers;

import java.util.Vector;
import java.awt.Rectangle;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemRoute;
import bt.elements.unit.Faction;
import bt.managers.SolarSystemManager;

import java.awt.*;

/**
 * Title:        Space Conquest Game
 * Description:  The same old Game
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Daniel Cleyne
 * @version 1.0
 */

public class FlatStarMapRenderer
{
    protected int MAPSPAN = 1400;
    protected double MAPSCALE = 1.0;
    protected int MAPGUTTER = 30;
    protected int MAPGRID;
    protected int MAPWIDTH;

    protected int m_CurrentSolarSystem;
    protected Image m_Image[];
    protected Polygon m_ConvexHull[];
    protected Rectangle m_BoundingBox;
    protected Vector<Integer> m_SelectedSystems = new Vector<Integer>();

    protected double m_Zoom;
    protected double m_MaxZoom = 3.0;
    protected double m_MinZoom = 0.0;

    private double m_Width;
    private double m_Height;
    private double m_MinX;
    private double m_MinY;

    private int m_MapYear;
    private final int ZOOMS = 4;

    public FlatStarMapRenderer()
    {
        m_CurrentSolarSystem = -1;
        m_Image = new Image[ZOOMS];
        m_ConvexHull = new Polygon[4];
        m_MapYear = 3025;

        SetZoom(0.0);
    }

    public void SetYear(int Year)
    {
        if (Year < 2750)
            return;
        if (Year > 3081)
            return;
        m_MapYear = Year;

        for (int i = 0; i < 4; i++)
             m_Image[i] = null;
    }

    public Rectangle getBoundingBox()
    { return new Rectangle(0,0,MAPWIDTH+(MAPGUTTER*2),MAPWIDTH+(MAPGUTTER*2)); }

    public Dimension getMapSize()
    {
        Dimension d = new Dimension(MAPWIDTH+(MAPGUTTER*2),MAPWIDTH+(MAPGUTTER*2));
        return d;
    }

    public void SetZoom(double Zoom)
    {
        m_Zoom = Zoom;
        if (m_Zoom < m_MinZoom)
            m_Zoom = m_MinZoom;
        if (m_Zoom > m_MaxZoom)
            m_Zoom = m_MaxZoom;

        MAPWIDTH = MAPSPAN * (int)(MAPSCALE + m_Zoom);
        MAPGRID = (int)(MAPSCALE + m_Zoom) * 20;
    }

    public double GetZoom()
    { return m_Zoom; }


    public void draw(Graphics comp, Component wnd)
    {
        if (m_Image[(int)m_Zoom] == null)
        {
            Cursor OldCur = wnd.getCursor();
            wnd.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            m_Image[(int)m_Zoom] = wnd.createImage(MAPWIDTH+(MAPGUTTER*2),MAPWIDTH+(MAPGUTTER*2));

            Graphics2D comp2D = (Graphics2D)m_Image[(int)m_Zoom].getGraphics();

//                comp2D.setClip(null);
//                Rectangle r = wnd.getBounds();
//                comp2D.fillRect(r.x,r.y,r.width,r.height);
//                comp2D.clipRect(0,0,MAPWIDTH+(MAPGUTTER*2),MAPWIDTH+(MAPGUTTER*2));

            comp2D.setColor(Color.lightGray);
            comp2D.fillRect(0,0,MAPWIDTH+(MAPGUTTER*2),MAPWIDTH+(MAPGUTTER*2));

            comp2D.setColor(Color.black);
            comp2D.fillRect(MAPGUTTER,MAPGUTTER,MAPWIDTH,MAPWIDTH);
            comp2D.setColor(Color.darkGray);
            comp2D.drawRect(MAPGUTTER,MAPGUTTER,MAPWIDTH,MAPWIDTH);

            int Offset = (MAPWIDTH / 2) + MAPGUTTER;
            int Offset1 = (MAPWIDTH / 2) + MAPGUTTER;
            int Scale = (int)(MAPWIDTH / MAPGRID);
            for (int i = 0; i < (Scale / 2); i++)
            {
                comp2D.drawLine(Offset,MAPGUTTER,Offset,MAPGUTTER+MAPWIDTH);
                comp2D.drawLine(MAPGUTTER,Offset,MAPGUTTER+MAPWIDTH,Offset);
                comp2D.drawLine(Offset1,MAPGUTTER,Offset1,MAPGUTTER+MAPWIDTH);
                comp2D.drawLine(MAPGUTTER,Offset1,MAPGUTTER+MAPWIDTH,Offset1);
                Offset += MAPGRID;
                Offset1 -= MAPGRID;
            }

            Font f = new Font("Arial",Font.PLAIN,8 + (int)m_Zoom);
            comp2D.setFont(f);
            for (int j = 0; j < SolarSystemManager.getPlanetCount(); j++)
            {
                InnerSpherePlanet p = SolarSystemManager.getPlanet(j);

                int xoff = GetXOffset(p.getXCoord());
                int yoff = GetYOffset(p.getYCoord());

                Faction fac = Faction.getFromAbbreviation(p.getOwner(m_MapYear));
                if (fac != null) {
                    comp2D.setColor(Color.decode(fac.getColour()));
                } else {
                    comp2D.setColor(Color.darkGray);
                }
                comp2D.fillArc(xoff-5,yoff-5,7 + (int)m_Zoom,7 + (int)m_Zoom,0,360);

//                comp2D.drawArc(xoff-7,yoff-7,9 + (int)m_Zoom,9 + (int)m_Zoom,0,360);

                comp2D.setColor(Color.lightGray);
                comp2D.drawString(p.getSystem(),xoff + 10,yoff);

                //Lets try and be polite to the system
                Thread.yield();
            }

            comp2D.setColor(Color.black);
            int height = comp2D.getFontMetrics().getHeight();
            Offset = (MAPWIDTH / 2) + MAPGUTTER;
            Offset1 = (MAPWIDTH / 2) + MAPGUTTER;
            for (int k = 0; k < (Scale/2); k++)
            {
                String xLabel = String.valueOf((int)(k * (MAPGRID / (MAPSCALE + m_Zoom))));
                String yLabel = xLabel;
                comp2D.drawString(xLabel,Offset-10,height);
                comp2D.drawString(yLabel,0,Offset + (height / 2));
                comp2D.drawString(xLabel,Offset1-10,height);
                comp2D.drawString(yLabel,0,Offset1 + (height / 2));
                Offset += MAPGRID;
                Offset1 -= MAPGRID;
            }

            //m_ConvexHull[(int)m_Zoom] = ConvexHull();
            wnd.setCursor(OldCur);

            //Lets be nice to the system
            Thread.yield();
        }
        comp.drawImage(m_Image[(int)m_Zoom],0,0,Color.black,null);

        //comp.setColor(Color.white);
        //comp.drawPolygon(m_ConvexHull[(int)m_Zoom]);

        for (int i = 0; i < m_SelectedSystems.size(); i++)
        {
            InnerSpherePlanet p = SolarSystemManager.getPlanet(((Integer)m_SelectedSystems.elementAt(i)).intValue());
            int xoff = GetXOffset(p.getXCoord());
            int yoff = GetYOffset(p.getYCoord());
            comp.setColor(Color.green);
            //comp.drawArc(xoff-5,yoff-5,7 + (int)m_Zoom,7 + (int)m_Zoom,0,360);
            comp.drawRect(xoff-5,yoff-5,7 + (int)m_Zoom,7 + (int)m_Zoom);

            comp.setColor(Color.CYAN);
            Vector<SolarSystemRoute> v = SolarSystemManager.getRoutesForSystem(p);
            for (int j = 0; j < v.size(); j++)
            {
                SolarSystemRoute ssr = v.elementAt(j);
                InnerSpherePlanet dest = SolarSystemManager.getPlanetFromID(ssr.getDestinationSystem(p.getID()));

                int xoffdest = GetXOffset(dest.getXCoord());
                int yoffdest = GetYOffset(dest.getYCoord());

                comp.drawLine(xoff,yoff,xoffdest,yoffdest);
            }
        }

/*        if (m_CurrentSolarSystem != -1)
        {
            SolarSystemProxy s = m_Systems.GetSystem((int)m_CurrentSolarSystem);
            int xoff = GetXOffset(s.GetX());
            int yoff = GetYOffset(s.GetY());
            comp.setColor(Color.white);
            comp.drawArc(xoff-5,yoff-5,7 + (int)m_Zoom,7 + (int)m_Zoom,0,360);
        }*/
    }

    protected double GetWidth()
    {
        double minX = 0;
        double maxX = 0;

        if (m_Width == 0.0)
        {
            for (int i = 0; i < SolarSystemManager.getPlanetCount(); i++)
            {
                InnerSpherePlanet m = SolarSystemManager.getPlanet(i);
                if (m.getXCoord() < minX)
                    minX = m.getXCoord();
                if (m.getXCoord() > maxX)
                    maxX = m.getXCoord();
            }
            m_Width = maxX - minX;
            m_MinX = minX;
        }
        return m_Width;
    }

    protected double GetHeight()
    {
        double minY = 0;
        double maxY = 0;

        if (m_Height == 0.0)
        {
            for (int i = 0; i < SolarSystemManager.getPlanetCount(); i++)
            {
                InnerSpherePlanet m = SolarSystemManager.getPlanet(i);
                if (m.getYCoord() < minY)
                    minY = m.getYCoord();
                if (m.getYCoord() > maxY)
                    maxY = m.getYCoord();
            }
            m_Height = maxY - minY;
            m_MinY = minY;
        }
        return m_Height;
    }

    protected java.awt.Rectangle GetBoundingBox()
    {
        if (m_BoundingBox == null)
        {
            GetWidth();
            GetHeight();
            m_BoundingBox = new java.awt.Rectangle((int)m_MinX,(int)m_MinY,(int)m_Width,(int)m_Height);
        }
        return new java.awt.Rectangle(m_BoundingBox);
    }


    protected int GetXOffset(double X)
    {
        return (int)(X * (MAPSCALE + m_Zoom)) + (MAPWIDTH / 2) + MAPGUTTER;
    }

    protected int GetYOffset(double Y)
    {
        return (MAPWIDTH / 2) - (int)(Y * (MAPSCALE + m_Zoom)) + MAPGUTTER;
    }

    public void selectSolarSystem(Point p)
    {
        m_CurrentSolarSystem = GetSystemIndex(p);
        m_SelectedSystems.clear();
        if (m_CurrentSolarSystem != -1)
            m_SelectedSystems.add(m_CurrentSolarSystem);
    }

    public void toggleSolarSystem(Point p)
    {
        m_CurrentSolarSystem = GetSystemIndex(p);
        if (m_CurrentSolarSystem != -1)
        {
            Integer I = m_CurrentSolarSystem;
            if (m_SelectedSystems.contains(I))
                m_SelectedSystems.remove(I);
            else
                m_SelectedSystems.add(I);
        }
    }

    public void FindSystems(String SearchText)
    {
        m_SelectedSystems.clear();
        for (int i = 0; i < SolarSystemManager.getPlanetCount(); i++)
        {
            InnerSpherePlanet isp = SolarSystemManager.getPlanet(i);
            if (isp.getSystem().equals(SearchText))
                m_SelectedSystems.add(i);
        }
    }


    protected Color GetSystemColor(String Owner)
    {
        if (Owner.compareTo("Cappellan Confederation") == 0)
            return Color.green;
        if (Owner.compareTo("Draconis Combine") == 0)
            return Color.red;
        if (Owner.compareTo("Free Worlds League") == 0)
            return Color.magenta;
        if (Owner.compareTo("Federated Suns") == 0)
            return Color.yellow;
        if (Owner.compareTo("Lyran Commonwealth") == 0)
            return Color.blue;
        if (Owner.compareTo("???") == 0)
            return Color.white;
        if (Owner.compareTo("N/A") == 0)
            return Color.darkGray;
        if (Owner.compareTo("P") == 0)
            return Color.lightGray;
        if (Owner.indexOf("/") > 0)
            return Color.orange;

        return Color.lightGray;
    }

    public int GetCurrentSystemIndex()
    {
        return m_CurrentSolarSystem;
    }

    public int GetSystemIndex(Point p)
    {
        int Size = (int)(4.0 + MAPSCALE + m_Zoom);
        Rectangle r = new Rectangle(p.x-Size,p.y-Size,Size * 2,Size * 2);
        for (int j = 0; j < SolarSystemManager.getPlanetCount(); j++)
        {
            InnerSpherePlanet isp = SolarSystemManager.getPlanet(j);
            int xoff = GetXOffset(isp.getXCoord());
            int yoff = GetYOffset(isp.getYCoord());
            if (r.contains(xoff,yoff))
            {
                return j;
            }
        }
        return -1;
    }

    public double CalculateDistance(int System1, int System2)
    {
        return SolarSystemManager.getDistance(System1,System2);
    }

    protected Polygon ConvexHull()
    {
        Polygon p = new Polygon();
        Point points[] = new Point[SolarSystemManager.getPlanetCount()];
        for (int i = 0; i < SolarSystemManager.getPlanetCount(); i++)
        {
            InnerSpherePlanet isp = SolarSystemManager.getPlanet(i);
            int x = GetXOffset(isp.getXCoord());
            int y = GetYOffset(isp.getYCoord());
            points[i] = new Point(x,y);
        }
//            int M = GrahamScan(points,m_Systems.GetSystemCount()-1);
        int M = wrap(points,SolarSystemManager.getPlanetCount()-1);
        for (int i = 0; i <= M; i++)
            p.addPoint(points[i].x,points[i].y);

        return p;
    }

    protected void swap(Point a[], int i, int j)
    {
        Point t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    protected double theta(Point p1, Point p2)
    {
        int dx, dy, ax, ay;
        double t;
        dx = p2.x - p1.x;
        ax = Math.abs(dx);
        dy = p2.y - p1.y;
        ay = Math.abs(dy);
        t = (ax + ay == 0) ? 0 : (double)dy/(ax + ay);
        if (dx < 0)
            t = 2-t;
        else
            if (dy < 0)
                t = 4+t;
        return t * 90.0;
    }

    protected void shellsort(Point a[], int N)
    {
        int i, j, h;
        Point v;
        for (h = 1; h <= N/9; h = 3*h+1);

        for (; h > 0; h /= 3)
        {
            for (i = h+1; i<= N; i+= 1)
            {
                v = a[i];
                j = 1;
                while (j > h && theta(a[1],a[j-h]) > theta(a[1],v))
                {
                    a[j] = a[j-h];
                    j -= h;
                }
                a[j] = v;
            }
        }
    }

    protected int ccw(Point p0, Point p1, Point p2)
    {
        int dx1, dx2, dy1, dy2;
        dx1 = p1.x - p0.x;
        dy1 = p1.y - p0.y;
        dx2 = p2.x - p0.x;
        dy2 = p2.y - p0.y;
        if (dx1*dy2 > dy1*dx2) return +1;
        if (dx1+dy2 < dy1*dx2) return -1;
        if ((dx1*dx2 < 0) || (dy1*dy2 < 0)) return -1;
        if ((dx1*dx1+dy1*dy1) < (dx2*dx2+dy2*dy2)) return +1;
        return 0;
    }

    protected int wrap(Point p[], int N)
    {
        int i, min, M;
        double th, v;
        for (min = 0, i = 1; i < N; i++)
            if (p[i].y < p[min].y) min = i;
        p[N] = p[min];
        th = 0.0;
        for (M = 0; M < N; M++)
        {
            swap(p, M, min);
            min = N;
            v = th;
            th = 360.0;
            for (i = M+1; i <= N; i++)
            {
                if (theta(p[M], p[i]) > v)
                {
                    if (theta(p[M], p[i]) < th)
                    {
                        min = i;
                        th = theta(p[M], p[min]);
                    }
                }
            }
            if (min == N) return M;
        }
        return 0;
    }

    protected int GrahamScan(Point p[], int N)
    {
        int i, min, M;
        for (min = 1, i = 2; i <= N; i++)
            if (p[i].y < p[min].y) min = i;

        for (i = 1; i <= N; i++)
            if (p[i].y == p[min].y)
                if (p[i].x > p[min].x) min = 1;

        swap(p, 1, min);
        shellsort(p, N);
        p[0] = p[N];
        for (M = 3, i = 4; i <= N; i++)
        {
            while (ccw(p[M], p[M-1], p[i]) >= 0) M--;
            M++;
            swap(p,i,M);
        }
        return M;
    }

    protected void ResetMap()
    {
        for (int i = 0; i < ZOOMS; i++)
        {
            m_Image[i] = null;
        }
    }
}
