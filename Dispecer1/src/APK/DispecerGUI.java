package APK;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;

public class DispecerGUI {
    public JPanel dispecerAPKPanel;
    private JTextArea textArea1;
    private JButton OK;

    public DispecerGUI(PublicKey pubKey, ObjectInputStream oin, ObjectOutputStream oop) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel background=new JLabel(new ImageIcon("C:\\Users\\necul\\Client\\Serviciul de Ambulante - Dispecer\\resources\\background.jpg"));
        JFrame frame = new JFrame("Serviciul de ambulante - Dispecer");
        frame.setResizable(false);
        frame.setLayout(null);
        dim.height = dim.height - 100;
        dim.width = dim.width - 100;
        frame.setPreferredSize(dim);
        this.dispecerAPKPanel.setVisible(true);
        frame.setContentPane(this.dispecerAPKPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
