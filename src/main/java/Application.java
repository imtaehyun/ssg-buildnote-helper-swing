import javax.swing.*;

/**
 * Created by 140179 on 2015-06-09.
 */
public class Application {

    public static void main(String[] args) {
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//            JFrame.setDefaultLookAndFeelDecorated(true);

            JFrame frame = new JFrame("ssg buildnote helper ver 1.1.4");
            frame.setContentPane(new BuildnoteForm().getRootPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
