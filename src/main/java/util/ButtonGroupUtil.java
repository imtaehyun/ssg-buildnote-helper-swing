package util;

import javax.swing.*;
import java.util.Enumeration;

/**
 * Created by 140179 on 2015-06-17.
 */
public class ButtonGroupUtil {
    public static String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
}
