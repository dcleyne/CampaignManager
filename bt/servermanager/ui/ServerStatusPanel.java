/**
 * Created on 07/12/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
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
package bt.servermanager.ui;

import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.net.ServerStatus;
import bt.common.ui.models.TableSorter;
import bt.common.util.ExceptionUtil;
import bt.servermanager.ServerManager;
import bt.servermanager.ServerManagerListener;

public class ServerStatusPanel extends JPanel implements ServerManagerListener, ActionListener
{
    private static Log log = LogFactory.getLog(ServerStatusPanel.class);
    static final long serialVersionUID = 1;
    
    private ServerManager m_Manager;
    protected TableSorter m_Sorter;
    protected JScrollPane m_ScrollPane;
    
    private JPanel m_StatusPanel;
    private JLabel m_ServerNameLabel = null;
    private JLabel m_ServerNameText = null;
    private JLabel m_ServerAddressLabel = null;
    private JLabel m_ServerAddressText = null;
    private JLabel m_ServerStartTimeLabel = null;
    private JLabel m_ConnectionsLabel = null;
    private JLabel m_ServerStartTimeText = null;
    private JLabel m_ConnectionsText = null;
    private JButton m_DisconnectButton = null;
    private JPanel m_DisconnectButtonPanel = null;
    
    public ServerStatusPanel(ServerManager manager)
    {
    	m_Manager = manager;
    	m_Manager.addListener(this);
		initialize();
    	
    }
     
    /**
     * This method initializes this
     * 
     */
    private void initialize() 
    {
    	setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    	m_ConnectionsLabel = new JLabel();
        m_ConnectionsLabel.setText("Connections");
        m_ServerStartTimeLabel = new JLabel();
        m_ServerStartTimeLabel.setText("Server Start Time");
        m_ServerAddressLabel = new JLabel();
        m_ServerAddressLabel.setText("ServerAddress");
        m_ServerNameLabel = new JLabel();
        m_ServerNameLabel.setText("Server Name");
        
        m_StatusPanel = new JPanel();
        m_StatusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.add(m_ServerNameLabel);
        p1.add(getM_ServerNameText());        
        m_StatusPanel.add(p1, null);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(m_ServerAddressLabel);
        p2.add(getM_ServerAddressText());        
        m_StatusPanel.add(p2, null);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.add(m_ServerStartTimeLabel);
        p3.add(getM_ServerStartTimeText());
        m_StatusPanel.add(p3, null);
        
        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        p4.add(m_ConnectionsLabel);
        p4.add(getM_ConnectionsText());
        m_StatusPanel.add(p4, null);
        
        this.add(m_StatusPanel);
        this.add(getM_DisconnectButtonPanel());
    }
    
    public void connected() 
    {
    	m_DisconnectButton.setEnabled(true);
    }
    
    public void disconnected() 
    {
        clearFields();
    	m_DisconnectButton.setEnabled(false);
    }

    public void connectionFailed(String errorMessage) {}   
    public void error(String Error){}
    public void serverShuttingDown() {}
    public void PlayerInfoRequested(){}
    
    
    public void serverStatus(ServerStatus status)
    {
    	if (status == null)
    	{
    		m_Manager.askServerStatus();
    		return;
    	}
    	
        log.info(status.toString());
        
        m_ServerNameText.setText(status.getServerName());
        m_ServerAddressText.setText(status.getServerAddress());
        Date StartDate = new Date(status.getServerStartTime());
        m_ServerStartTimeText.setText(StartDate.toString());
        m_ConnectionsText.setText(Long.toString(status.getNumberOfConnections()));
        
        repaint();
    }
        
    public void GameAdministrator(String GameName)
    {
        
    }

    /**
     * This method initializes m_ServerNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JLabel getM_ServerNameText()
    {
        if (m_ServerNameText == null)
        {
            m_ServerNameText = new JLabel();
            m_ServerNameText.setBorder(BorderFactory.createEtchedBorder());
            m_ServerNameText.setPreferredSize(new Dimension(180,20));
        }
        return m_ServerNameText;
    }

    /**
     * This method initializes m_ServerAddressTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JLabel getM_ServerAddressText()
    {
        if (m_ServerAddressText == null)
        {
            m_ServerAddressText = new JLabel();
            m_ServerAddressText.setBorder(BorderFactory.createEtchedBorder());
            m_ServerAddressText.setPreferredSize(new Dimension(120,20));
        }
        return m_ServerAddressText;
    }

    /**
     * This method initializes m_ServerStartTimeTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JLabel getM_ServerStartTimeText()
    {
        if (m_ServerStartTimeText == null)
        {
            m_ServerStartTimeText = new JLabel();
            m_ServerStartTimeText.setBorder(BorderFactory.createEtchedBorder());
            m_ServerStartTimeText.setPreferredSize(new Dimension(220,20));
        }
        return m_ServerStartTimeText;
    }

    /**
     * This method initializes m_ConnectionsTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JLabel getM_ConnectionsText()
    {
        if (m_ConnectionsText == null)
        {
            m_ConnectionsText = new JLabel();
            m_ConnectionsText.setBorder(BorderFactory.createEtchedBorder());
            m_ConnectionsText.setPreferredSize(new Dimension(40,20));
        }
        return m_ConnectionsText;
    }

    /**
     * This method initializes m_DisconnectButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JPanel getM_DisconnectButtonPanel()
    {
        if (m_DisconnectButtonPanel == null)
        {
        	m_DisconnectButtonPanel = new JPanel();
        	FlowLayout fl = new FlowLayout();
        	fl.setAlignment(FlowLayout.RIGHT);
        	m_DisconnectButtonPanel.setLayout(fl);
        	
        	
            m_DisconnectButton = new JButton();
            m_DisconnectButton.setText("Disconnect");
            m_DisconnectButton.setActionCommand("Disconnect");
        	m_DisconnectButton.setEnabled(false);

            m_DisconnectButton.addActionListener(this);
            
            m_DisconnectButtonPanel.add(m_DisconnectButton);
        }
        return m_DisconnectButtonPanel;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            if (ae.getActionCommand().equalsIgnoreCase("Disconnect"))
            {
                int reply = JOptionPane.showConfirmDialog(this,
                        "Do you really want to disconnect?","Disconnect",
                        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (reply == JOptionPane.YES_OPTION)
                {
                    m_Manager.disconnect();
                }
            }
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }
    
    private void clearFields()
    {
        m_ServerNameText.setText("");
        m_ServerAddressText.setText("");
        m_ServerStartTimeText.setText("");
        m_ConnectionsText.setText("");
        
    }

	@Override
	public void solarSystemDetails(Long id, SolarSystemDetails ssd) {}

	@Override
	public void messageStart(String message) {}

	@Override
	public void messageEnd() {}

	@Override
	public void unitList(Vector<UnitSummary> unitNames) {}

	@Override
	public void unitDetails(String unitName, Unit unit) {}

	@Override
	public void playerList(Vector<PlayerSummary> playerSummaries) {}

	@Override
	public void playerDetails(String playerName, Player unit) {}
}
