package homework;

import javax.swing.Action;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;

/**
 * 
 * @author jhg95693
 */
public class JButton extends javax.swing.JButton {
    
    public void setStyling(boolean b) {
        this.setContentAreaFilled(b);
        this.setFocusPainted(b);
    }
    
    public JButton() {
        super(null, null);
    }
    
    public JButton(String text) {
        super(text, null);
    }
    
    public JButton(Action a) {
        super();
        setAction(a);
    }
    
    public JButton(String text, Icon icon) {
        // Create the model
        setModel(new DefaultButtonModel());

        // initialize
        init(text, icon);
    }
    
}
