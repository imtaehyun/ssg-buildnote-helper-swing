package util;

import model.Author;

import javax.swing.*;
import java.awt.*;

/**
 * Created by 140179 on 2015-06-12.
 */
public class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(((Author) value).getName());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);

        return this;
    }
}
