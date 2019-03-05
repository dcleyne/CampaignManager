package bt.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class SwingHelper
{

    public SwingHelper()
    {
    }

    public static Component GetTextField(JTextField TextField, String FieldName, String ToolTipText, boolean SetPreferredSize)
    {
        JLabel Label = new JLabel();
        JPanel Panel = new JPanel();
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.X_AXIS));
        Panel.setToolTipText(ToolTipText);
        Label.setToolTipText(ToolTipText);
        Label.setText(FieldName);
        Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        Label.setAlignmentY(Component.CENTER_ALIGNMENT);
        Label.setMaximumSize(new Dimension(Short.MAX_VALUE, 21));
        TextField.setSelectionEnd(0);
        TextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 21));
        TextField.setText("");
        TextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        TextField.setAlignmentY(Component.CENTER_ALIGNMENT);
        TextField.setToolTipText(ToolTipText);
        Panel.add(Label);
        Panel.add(TextField);
        Panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (SetPreferredSize)
        {
            Label.setPreferredSize(new Dimension(80, 21));
            TextField.setPreferredSize(new Dimension(320, 21));
        }
        return Panel;
    }

    public static Component GetTextFieldWithAction(JTextField TextField, String FieldName, String ToolTipText, boolean SetPreferredSize, String ActionCommand, String ACToolTip, ActionListener al)
    {
        JPanel Panel = (JPanel)GetTextField(TextField,FieldName,ToolTipText,SetPreferredSize);
        JButton elipses = new JButton("...");
        elipses.setAlignmentX(Component.LEFT_ALIGNMENT);
        elipses.setAlignmentY(Component.CENTER_ALIGNMENT);
        elipses.setPreferredSize(new Dimension(25,21));
        elipses.setMaximumSize(new Dimension(25,21));
        elipses.setActionCommand(ActionCommand);
        elipses.setToolTipText(ACToolTip);
        elipses.addActionListener(al);
        Panel.add(elipses);
        return Panel;
    }

    public static Component GetCheckBox(JCheckBox CheckBox, String FieldName, String ToolTipText, boolean SetPreferredSize)
    {
        JPanel Panel = new JPanel();
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.X_AXIS));
        Panel.setToolTipText(ToolTipText);
        CheckBox.setToolTipText(ToolTipText);
        CheckBox.setText(FieldName);
        CheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        CheckBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        CheckBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 21));
        Panel.add(CheckBox);
        Panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (SetPreferredSize)
        {
            CheckBox.setPreferredSize(new Dimension(10, 21));
        }
        return Panel;
    }

    public static Component GetComboBox(JComboBox<?> ComboBox, String FieldName, String ToolTipText, boolean SetPreferredSize)
    {
        JLabel Label = new JLabel();
        JPanel Panel = new JPanel();
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.X_AXIS));
        Label.setToolTipText(ToolTipText);
        Label.setText(FieldName);
        Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        Label.setAlignmentY(Component.CENTER_ALIGNMENT);
        Label.setMaximumSize(new Dimension(Short.MAX_VALUE, 21));
        ComboBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        ComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        ComboBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        ComboBox.setToolTipText(ToolTipText);
        Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 29));
        Panel.add(Label);
        Panel.add(ComboBox);
        Panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (SetPreferredSize)
        {
            Panel.setPreferredSize(new Dimension(60, 25));
            Label.setPreferredSize(new Dimension(80, 21));
            ComboBox.setPreferredSize(new Dimension(320, 25));
        }
        return Panel;
    }

    public static Component GetTextArea(JTextArea TextArea, String FieldName, String ToolTipText, boolean SetPreferredSize)
    {
        JLabel Label = new JLabel();
        JPanel Panel = new JPanel();
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
        Panel.setToolTipText(ToolTipText);
        Label.setToolTipText(ToolTipText);
        Label.setText(FieldName);
        Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        TextArea.setSelectionEnd(0);
        TextArea.setMinimumSize(new Dimension(0, 21));
        TextArea.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
        TextArea.setText("");
        TextArea.setToolTipText(ToolTipText);
        TextArea.setLineWrap(true);
        TextArea.setWrapStyleWord(true);
        TextArea.setDragEnabled(true);

        JScrollPane ScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPane.setViewportView(TextArea);
        ScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        Panel.add(Label);
        Panel.add(ScrollPane);
        Panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (SetPreferredSize)
        {
            TextArea.setPreferredSize(new Dimension(360, 80));
        }
        return Panel;
    }

    public static Component GetEditorPane(JEditorPane EditorPane, String FieldName, String ToolTipText)
    {
        JLabel Label = new JLabel();
        JPanel Panel = new JPanel();
        Panel.setBorder(BorderFactory.createEtchedBorder());
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
        Panel.setToolTipText(ToolTipText);
        Label.setToolTipText(ToolTipText);
        Label.setText(FieldName);
        Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        EditorPane.setSelectionEnd(0);
        EditorPane.setMinimumSize(new Dimension(0, 21));
        EditorPane.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
        EditorPane.setText("");
        EditorPane.setToolTipText(ToolTipText);


        Panel.add(Label);
        Panel.add(EditorPane);
        Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return Panel;
    }

    public static String FormatDate(LocalDate d)
    {
    	if (d == null) 
    		return "";
    	
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(d);
    }

    public static String FormatDateTime(Date d)
    {
    	if (d == null) 
    		return "";
    	
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(d);
    }

    public static LocalDate GetDateFromString(String Value) throws java.text.ParseException
    {
    	if (Value.isEmpty())
    		return null;
    	
        return LocalDate.parse(Value);
    }

    public static Date GetDateTimeFromString(String Value) throws java.text.ParseException
    {
    	if (Value.isEmpty())
    		return null;
    	
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(Value);
    }

}
