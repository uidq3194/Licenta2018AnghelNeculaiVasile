package APK;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.concurrent.CountDownLatch;

public class login extends Dispecer {
    private JButton loginButton;
    private JButton registerButton;
    public JPanel loginPanel;
    private JLabel userLabel;
    private JTextField textField2; //user
    private JLabel passLabel;
    private JTextField textField1; //password
    private JPanel butonPanel;
    private JPanel logPanel;
    private BufferedImage myImage;

    public login(PublicKey pubKey, ObjectInputStream oin,ObjectOutputStream oop) throws IOException {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel background=new JLabel(new ImageIcon("C:\\Users\\necul\\Client\\Serviciul de Ambulante - Dispecer\\resources\\background.jpg"));
        JFrame frame = new JFrame("Serviciul de ambulante - Dispecer");
        frame.setResizable(false);
        frame.setLayout(null);
        dim.height = dim.height - 100;
        dim.width = dim.width - 100;
        frame.setPreferredSize(dim);
        this.loginPanel.setVisible(true);
        frame.setContentPane(this.loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                byte[] cryptedUsername = null;
                byte[] cpryptedPassword = null;
                String clientLoginAnswer = "0";
                PublicKey ServerPubKey = null;
                try {
                        ServerPubKey = (PublicKey) oin.readObject();


                        System.out.println("pubkey : " + ServerPubKey);
                        System.out.println("username : " + textField2.getText());
                        System.out.println("password : " + textField1.getText());

                        cryptedUsername = encrypt(ServerPubKey, textField2.getText());
                        cpryptedPassword = encrypt(ServerPubKey, textField1.getText());
                        textField2.setText("");
                        textField1.setText("");
                        oop.writeObject(cryptedUsername);
                        oop.writeObject(cpryptedPassword);

                        clientLoginAnswer = oin.readObject().toString();

                        if(clientLoginAnswer.equals("1")) {
                            JOptionPane.showMessageDialog(null, "Logare reusita!", "SdA", JOptionPane.INFORMATION_MESSAGE);
                            Dispecer.dispecerLogat = 1;
                            Dispecer.loginSignal.countDown();
                            frame.dispose();
                        }else
                        {
                            JOptionPane.showMessageDialog(null,"Logare esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null,"ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!","-------WARNING------",JOptionPane.INFORMATION_MESSAGE);
                        }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                PublicKey ServerPubKey = null;
                byte[] checkIfRegister = "#".getBytes();

                try {
                    ServerPubKey = (PublicKey) oin.readObject();
                    oop.writeObject(checkIfRegister);
                    register registerComponent = new register(pubKey,oin,oop);

                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


}
