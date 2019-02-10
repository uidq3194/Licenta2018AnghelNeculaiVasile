package APK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;

public class register {
    private JTextField cnpText;
    private JTextField userText;
    private JPasswordField passwordText;
    private JTextField nameText;
    private JLabel cnp;
    private JLabel uid;
    private JLabel pass;
    private JLabel numeprenume;
    private JButton register;
    private JButton close;
    private JPanel registerPanel;
    private JPanel regPanel;
    private int countC = 1;

    public register(PublicKey pubKey, ObjectInputStream oin, ObjectOutputStream oop) throws IOException {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel background=new JLabel(new ImageIcon("C:\\Users\\necul\\Client\\Serviciul de Ambulante - Dispecer\\resources\\background.jpg"));
        JFrame registerFrame = new JFrame("---Register Dispecer---");
        registerFrame.setResizable(false);
        registerFrame.setLayout(null);
        dim.height = dim.height - 100;
        dim.width = dim.width - 100;
        registerFrame.setPreferredSize(dim);
        this.registerPanel.setVisible(true);
        registerFrame.setContentPane(this.registerPanel);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.pack();
        registerFrame.setVisible(true);

        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                byte[] cryptedUid = null;
                byte[] cryptedCnp = null;
                byte[] cryptedPass =  null;
                byte[] cryptedName =  null;
                JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                try {
                    if(countC == 1)
                    {
                        oop.writeObject("0");
                    }
                    if (cnpText.getText().equals("") || userText.getText().equals("") || passwordText.getPassword().equals("") || nameText.getText().equals("")) {
                        JOptionPane.showMessageDialog(null,"Please fill all the fields below!","Warning",JOptionPane.INFORMATION_MESSAGE);
                        countC --;
                    }
                    else
                    {
                        cryptedCnp = Dispecer.encrypt(pubKey,cnpText.getText());
                        oop.writeObject(cryptedCnp);
                        cryptedUid = Dispecer.encrypt(pubKey,userText.getText());
                        oop.writeObject(cryptedUid);
                        String cnpUsed = null;
                        cnpUsed = (String) oin.readObject();
                        if(cnpUsed.equals("0"))
                        {
                            JOptionPane.showMessageDialog(null,"CNP already used!","Warning",JOptionPane.INFORMATION_MESSAGE);
                            countC --;
                        }
                        else if(cnpUsed.equals("1"))
                        {

                            cryptedPass = Dispecer.encrypt(pubKey,String.valueOf(passwordText.getPassword()));
                            cryptedName = Dispecer.encrypt(pubKey,nameText.getText());

                            oop.writeObject(cryptedPass);
                            oop.writeObject(cryptedName);

                            String recordInserted = (String) oin.readObject();

                            if (recordInserted.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Welcome to SdA!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                            else if (recordInserted.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Registration failed! \n Please contact the adimn or try again!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                            registerFrame.dispose();
                        }


                    }


                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                try {
                    oop.writeObject("1");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                registerFrame.dispose();
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
