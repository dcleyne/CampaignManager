/**
 * Created on 27/11/2005
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
package bt.servermanager.ui;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

public class ConnectionDialog extends JDialog
{
    static final long serialVersionUID = 1;

    private String m_PlayerName;
    private String m_Password;
    private String m_ServerName;
    private int m_ServerPort;
    private boolean m_DoConnect;

    // Visual Members
    private JPanel connectionDialogPanel = null;
    private JPanel serverPanel = null;
    private JLabel passwordLabel = null;
    private JPasswordField passwordField = null;
    private JLabel serverNameLabel = null;
    private JTextField serverNameTextField = null;
    private JLabel serverPortLabel = null;
    private JTextField serverPortTextField = null;
    private JButton cancelButton = null;
    private JButton okButton = null;

    /**
     * This method initializes 
     * 
     */
    public ConnectionDialog(Frame owner) 
    {
    	super(owner);
    	initialize();
    }
    
    public String getPlayerName()
    { return m_PlayerName; }
    
    public String getPassword()
    { return m_Password; }
    
    public String getServerName()
    { return m_ServerName; }
    
    public int getServerPort()
    { return m_ServerPort; }

    public boolean doConnect()
    { return m_DoConnect; }
    
    public void setPassword(String Password)
    { 
        m_Password = Password; 
        passwordField.setText(m_Password);
    }
    
    public void setServerName(String ServerName)
    {
        m_ServerName = ServerName; 
        serverNameTextField.setText(m_ServerName);
    }
    
    public void setServerPort(int Port)
    { 
        m_ServerPort = Port; 
        serverPortTextField.setText(Integer.toString(m_ServerPort));
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(200, 200));
        this.setContentPane(getConnectionDialogPanel());
        this.setTitle("Connection Dialog");
        this.setResizable(false);
        this.setName("Connection Dialog");
        this.setModal(true);
    		
    }

    /**
     * This method initializes ConnectionDialogPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getConnectionDialogPanel()
    {
        if (connectionDialogPanel == null)
        {
            passwordLabel = new JLabel();
            passwordLabel.setText("Password");
            connectionDialogPanel = new JPanel();
            connectionDialogPanel.setLayout(new BoxLayout(connectionDialogPanel,BoxLayout.Y_AXIS));
            connectionDialogPanel.add(passwordLabel, null);
            connectionDialogPanel.add(getPlayerPasswordField(), null);
            connectionDialogPanel.add(getServerPanel(), null);
            
            JPanel buttonPanel = new JPanel();
            FlowLayout fl = new FlowLayout();
            fl.setAlignment(FlowLayout.RIGHT);
            buttonPanel.setLayout(fl);
            
            buttonPanel.add(getOKButton(), null);
            buttonPanel.add(getCancelButton(), null);
            connectionDialogPanel.add(buttonPanel);
        }
        return connectionDialogPanel;
    }

    /**
     * This method initializes ServerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getServerPanel()
    {
        if (serverPanel == null)
        {
            serverPortLabel = new JLabel("Server Port");
            serverNameLabel = new JLabel("Server DNS Name or IP");
            serverPanel = new JPanel();
            serverPanel.setLayout(new BoxLayout(serverPanel,BoxLayout.Y_AXIS));
            serverPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Server", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            serverPanel.add(serverNameLabel, null);
            serverPanel.add(getServerNameTextField(), null);
            serverPanel.add(serverPortLabel, null);
            serverPanel.add(getServerPortTextField(), null);
        }
        return serverPanel;
    }

    /**
     * This method initializes PlayerPasswordField	
     * 	
     * @return javax.swing.JPasswordField	
     */
    private JPasswordField getPlayerPasswordField()
    {
        if (passwordField == null)
        {
            passwordField = new JPasswordField();
            passwordField.setPreferredSize(new Dimension(120,20));
        }
        return passwordField;
    }

    /**
     * This method initializes ServerNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getServerNameTextField()
    {
        if (serverNameTextField == null)
        {
            serverNameTextField = new JTextField();
            serverNameTextField.setPreferredSize(new Dimension(120,20));
        }
        return serverNameTextField;
    }

    /**
     * This method initializes ServerPortTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getServerPortTextField()
    {
        if (serverPortTextField == null)
        {
            serverPortTextField = new JTextField();
            serverPortTextField.setPreferredSize(new Dimension(120,20));
        }
        return serverPortTextField;
    }

    /**
     * This method initializes CancelButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCancelButton()
    {
        if (cancelButton == null)
        {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    m_DoConnect = false;
                    ConnectionDialog.this.setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOKButton()
    {
        if (okButton == null)
        {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    if (fillMembers())
                    {
                        m_DoConnect = true;
                        ConnectionDialog.this.setVisible(false);
                    }
                }
            });
        }
        return okButton;
    }

    private boolean fillMembers()
    {
        boolean Valid = true;
        String ValidityProblems = "";
        
        m_Password = new String(passwordField.getPassword());
        m_ServerName = serverNameTextField.getText();
        m_ServerPort = Integer.parseInt(serverPortTextField.getText());
        
        if (m_ServerName.equals(""))
        {
            Valid = false;
            ValidityProblems += "You must supply a valid Server Name or IP Address!\r\n";
        }
        if (m_ServerPort == 0)
        {
            Valid = false;
            ValidityProblems += "You must supply a valid Server Port Number!\r\n";
        }
        if (!Valid)
        {
            JOptionPane.showMessageDialog(this,ValidityProblems);
        }
        
        return Valid;
    }

}  //  @jve:decl-index=0:visual-constraint="4,9"
