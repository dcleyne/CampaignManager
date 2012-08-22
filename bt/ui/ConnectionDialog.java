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
package bt.ui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;

public class ConnectionDialog extends JDialog
{
    static final long serialVersionUID = 1;

    private String m_PlayerName;
    private String m_Password;
    private String m_Nickname;
    private String m_EmailAddress;
    private String m_ServerName;
    private int m_ServerPort;
    private boolean m_DoConnect;

    // Visual Members
    private JPanel ConnectionDialogPanel = null;
    private JLabel PlayerNameLabel = null;
    private JTextField PlayerNameTextField = null;
    private JPanel ServerPanel = null;
    private JLabel PasswordLabel = null;
    private JPasswordField PlayerPasswordField = null;
    private JLabel ServerNameLabel = null;
    private JTextField ServerNameTextField = null;
    private JLabel ServerPortLabel = null;
    private JTextField ServerPortTextField = null;
    private JButton CancelButton = null;
    private JButton jButton = null;
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
    
    public String getNickname()
    { return m_Nickname; }
    
    public String getEmailAddress()
    { return m_EmailAddress; }
    
    public String getServerName()
    { return m_ServerName; }
    
    public int getServerPort()
    { return m_ServerPort; }

    public boolean doConnect()
    { return m_DoConnect; }
    
    public void setPlayerName(String Name)
    { 
        m_PlayerName = Name;
        PlayerNameTextField.setText(m_PlayerName);
    }
    
    public void setPassword(String Password)
    { 
        m_Password = Password; 
        PlayerPasswordField.setText(m_Password);
    }
    
    public void setServerName(String ServerName)
    {
        m_ServerName = ServerName; 
        ServerNameTextField.setText(m_ServerName);
    }
    
    public void setServerPort(int Port)
    { 
        m_ServerPort = Port; 
        ServerPortTextField.setText(Integer.toString(m_ServerPort));
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(337, 232));
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
        if (ConnectionDialogPanel == null)
        {
            PasswordLabel = new JLabel();
            PasswordLabel.setBounds(new Rectangle(15, 45, 84, 16));
            PasswordLabel.setText("Password");
            PlayerNameLabel = new JLabel();
            PlayerNameLabel.setBounds(new Rectangle(15, 15, 86, 16));
            PlayerNameLabel.setText("Player Name");
            ConnectionDialogPanel = new JPanel();
            ConnectionDialogPanel.setLayout(null);
            ConnectionDialogPanel.add(PlayerNameLabel, null);
            ConnectionDialogPanel.add(getPlayerNameTextField(), null);
            ConnectionDialogPanel.add(getServerPanel(), null);
            ConnectionDialogPanel.add(PasswordLabel, null);
            ConnectionDialogPanel.add(getPlayerPasswordField(), null);
            ConnectionDialogPanel.add(getCancelButton(), null);
            ConnectionDialogPanel.add(getJButton(), null);
        }
        return ConnectionDialogPanel;
    }

    /**
     * This method initializes PlayerNamejTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPlayerNameTextField()
    {
        if (PlayerNameTextField == null)
        {
            PlayerNameTextField = new JTextField();
            PlayerNameTextField.setBounds(new java.awt.Rectangle(105,15,211,20));
        }
        return PlayerNameTextField;
    }

    /**
     * This method initializes ServerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getServerPanel()
    {
        if (ServerPanel == null)
        {
            ServerPortLabel = new JLabel();
            ServerPortLabel.setBounds(new java.awt.Rectangle(16,62,135,16));
            ServerPortLabel.setText("Server Port");
            ServerNameLabel = new JLabel();
            ServerNameLabel.setText("Server DNS Name or IP");
            ServerNameLabel.setBounds(new Rectangle(15, 30, 148, 16));
            ServerPanel = new JPanel();
            ServerPanel.setLayout(null);
            ServerPanel.setBounds(new Rectangle(14, 73, 301, 91));
            ServerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Server", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ServerPanel.add(ServerNameLabel, null);
            ServerPanel.add(getServerNameTextField(), null);
            ServerPanel.add(ServerPortLabel, null);
            ServerPanel.add(getServerPortTextField(), null);
        }
        return ServerPanel;
    }

    /**
     * This method initializes PlayerPasswordField	
     * 	
     * @return javax.swing.JPasswordField	
     */
    private JPasswordField getPlayerPasswordField()
    {
        if (PlayerPasswordField == null)
        {
            PlayerPasswordField = new JPasswordField();
            PlayerPasswordField.setBounds(new java.awt.Rectangle(105,45,211,20));
        }
        return PlayerPasswordField;
    }

    /**
     * This method initializes ServerNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getServerNameTextField()
    {
        if (ServerNameTextField == null)
        {
            ServerNameTextField = new JTextField();
            ServerNameTextField.setBounds(new java.awt.Rectangle(165,30,121,20));
        }
        return ServerNameTextField;
    }

    /**
     * This method initializes ServerPortTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getServerPortTextField()
    {
        if (ServerPortTextField == null)
        {
            ServerPortTextField = new JTextField();
            ServerPortTextField.setBounds(new java.awt.Rectangle(165,61,121,20));
        }
        return ServerPortTextField;
    }

    /**
     * This method initializes CancelButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCancelButton()
    {
        if (CancelButton == null)
        {
            CancelButton = new JButton();
            CancelButton.setBounds(new Rectangle(223, 171, 91, 26));
            CancelButton.setText("Cancel");
            CancelButton.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    m_DoConnect = false;
                    ConnectionDialog.this.setVisible(false);
                }
            });
        }
        return CancelButton;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton()
    {
        if (jButton == null)
        {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(121, 171, 90, 26));
            jButton.setText("OK");
            jButton.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    if (fillMembers())
                    {
                        m_DoConnect = true;
                        ConnectionDialog.this.setVisible(false);
                    }
                }
            });
        }
        return jButton;
    }

    private boolean fillMembers()
    {
        boolean Valid = true;
        String ValidityProblems = "";
        
        m_PlayerName = PlayerNameTextField.getText();
        m_Password = new String(PlayerPasswordField.getPassword());
        m_ServerName = ServerNameTextField.getText();
        m_ServerPort = Integer.parseInt(ServerPortTextField.getText());
        
        if (m_PlayerName.equals(""))
        {
            Valid = false;
            ValidityProblems += "You must supply a valid Player Name!\r\n";
        }
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
