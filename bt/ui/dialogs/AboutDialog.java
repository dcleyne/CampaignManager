/**
 * Created on 4/01/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
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
package bt.ui.dialogs;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.Rectangle;

public class AboutDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 1;

	private JPanel m_AboutPanel = null;
	private JPanel m_HeadingPanel = null;
	private JLabel jLabel = null;
	private JButton jButton = null;

	private JTextPane m_TextPane = null;

	/**
	 * This method initializes 
	 * 
	 */
	public AboutDialog(Frame owner) 
	{
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
        this.setSize(new Dimension(318, 450));
        this.setTitle("About");
        this.setContentPane(getM_AboutPanel());
			
	}

	/**
	 * This method initializes m_AboutPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getM_AboutPanel()
	{
		if (m_AboutPanel == null)
		{
			m_AboutPanel = new JPanel();
			m_AboutPanel.setLayout(new BorderLayout());
			m_AboutPanel.setBackground(java.awt.Color.black);
			m_AboutPanel.add(getM_HeadingPanel(), BorderLayout.NORTH);
			m_AboutPanel.add(getJButton(), BorderLayout.SOUTH);
			m_AboutPanel.add(getM_TextPane(), BorderLayout.CENTER);
		}
		return m_AboutPanel;
	}

	/**
	 * This method initializes m_HeadingPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getM_HeadingPanel()
	{
		if (m_HeadingPanel == null)
		{
			jLabel = new JLabel();
			jLabel.setIcon(new ImageIcon(getClass().getResource("/data/images/Legatus Heading.gif")));
			jLabel.setBackground(Color.BLACK);	
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setText("");
			m_HeadingPanel = new JPanel();
			m_HeadingPanel.setLayout(new BorderLayout());
			m_HeadingPanel.setBackground(Color.BLACK);			
			m_HeadingPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
			m_HeadingPanel.setBounds(new java.awt.Rectangle(5,4,290,62));
			m_HeadingPanel.add(jLabel, java.awt.BorderLayout.CENTER);
		}
		return m_HeadingPanel;
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
			jButton.setMaximumSize(new Dimension(100, 25));
			jButton.setPreferredSize(new java.awt.Dimension(100,25));
			jButton.setText("Ok");
			jButton.setBounds(new Rectangle(96, 365, 100, 25));
			jButton.setMinimumSize(new Dimension(100, 25));
			jButton.addActionListener(this);
		}
		return jButton;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		setVisible(false);
	}

	/**
	 * This method initializes m_TextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextPane getM_TextPane()
	{
		if (m_TextPane == null)
		{
			m_TextPane = new JTextPane();
			SimpleAttributeSet set= new SimpleAttributeSet();
			StyleConstants.setAlignment(set,StyleConstants.ALIGN_CENTER);
			StyleConstants.setBackground(set, Color.black);
			StyleConstants.setForeground(set, Color.LIGHT_GRAY);
			StyleConstants.setFontFamily(set, "SansSerif");
			StyleConstants.setFontSize(set, 13);
			m_TextPane.setParagraphAttributes(set,true);
			m_TextPane.setBounds(new Rectangle(11, 78, 283, 260));
			m_TextPane.setForeground(java.awt.Color.lightGray);
			m_TextPane.setText("Copyright: Daniel Cleyne (c) 2004-2007 \n\n This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version. \n\nThis program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n\n\n Dedicated to the memory of Horza.\nFriend of friends.\n");
			m_TextPane.setToolTipText("Copyright: Copyright Daniel Cleyne (c) 2004-2007");
			m_TextPane.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			m_TextPane.setEditable(false);
			m_TextPane.setBackground(java.awt.Color.black);

		}
		return m_TextPane;
	}
	
}  //  @jve:decl-index=0:visual-constraint="12,9"
