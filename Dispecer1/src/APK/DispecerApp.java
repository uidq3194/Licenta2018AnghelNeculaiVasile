package APK;

import java.awt.*;
        import java.awt.image.BufferedImage;
        import java.awt.event.ActionListener;
        import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
        import javax.swing.border.TitledBorder;

class DispecerApp implements Runnable{

    final JFrame frame = new JFrame("Serviciul de Ambulanta - Dispecer");
    final JPanel gui = new JPanel(new BorderLayout(5,5));
    private static String[][] dataP = new String[1000][3];
    private static String[][] dataA = new String[1000][3];
    private static List<String>  urgenteUpdate = new ArrayList<>();
    private static Map<String,String>  msgAmbulanteUpdate = new HashMap<>();
    private int countC = 1;
    private static JButton updatePanouriBTN = new JButton();
    private static JButton updateMsg = new JButton();
    private static JPanel imagePanel = new JPanel();
    private static JPanel msgPan = new JPanel();
    private static String pacientAction = "Monitorizare";
    private static Map<String,String>  SOSinfo = new HashMap<>();
    private static String nume = "-";
    private static String mesaj = "-";

    private void displayDispecer (){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TitledBorder tborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
//      tborder.setTitle("Dispecer");
        Color color = new Color(255,0,0, 110 );
        gui.setBorder( tborder );
        gui.setBackground(color);
        if (Dispecer.dispecerLogat == 0) {
            login();
        }
        if (Dispecer.dispecerLogat == 1)
        {
            app();
        }

        frame.setContentPane(gui);

        frame.pack();

        frame.setLocationRelativeTo(null);
        try {
            // 1.6+
            frame.setLocationByPlatform(true);
            frame.setMinimumSize(frame.getSize());
        } catch (Throwable ignoreAndContinue) {
        }
        frame.setVisible(true);
    }
    @Override
    public void run() {
        displayDispecer();
    }
    private void app(){



        Color color = new Color(150,0,0, 200 );
        Color color1 = new Color(150,0,0 );
        Color color4 = new Color(255,0,0, 60);
        gui.setBackground(color4);
        frame.revalidate();
        frame.repaint();
        //JToolBar tb = new JToolBar();
        JPanel plafComponents = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 3, 3));
        plafComponents.setBorder(
                new TitledBorder("Manipulare baza de date"));

        JPanel plafComponentsSelected = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 3, 3));
        plafComponentsSelected.setBorder(
                new TitledBorder("Select"));
        JPanel plafComponentsAction = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 3, 3));
        plafComponentsAction.setBorder(
                new TitledBorder("Actiune"));

        plafComponents.add(plafComponentsSelected);
        plafComponents.add(plafComponentsAction);

        String[] plafNames = {"Selecteaza..", "Dispecer", "Medic", "Ambulanta", "Pacient"};

        final JComboBox plafChooser = new JComboBox(plafNames);
        plafComponentsSelected.add(plafChooser, BorderLayout.WEST);
        plafChooser.setBackground(color1);
        plafChooser.setForeground(Color.lightGray);
        plafChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = plafChooser.getSelectedIndex();
                if (plafNames[i].equals("Dispecer")) {
                    JButton updateDispecerBTN = new JButton("Update Cont D.");
                    updateDispecerBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Update Cont D.";
                            frame.validate();
                        }
                    });
                    updateDispecerBTN.setBackground(color1);
                    updateDispecerBTN.setForeground(Color.lightGray);
                    updateDispecerBTN.setFocusPainted(false);

                    JButton delogareDispecerBTN = new JButton("Delogare D.");
                    delogareDispecerBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Logout";
                            frame.validate();
                        }
                    });
                    delogareDispecerBTN.setBackground(color1);
                    delogareDispecerBTN.setForeground(Color.lightGray);
                    delogareDispecerBTN.setFocusPainted(false);

                    JButton stergereDispecerBTN = new JButton("Stergere cont D.");
                    stergereDispecerBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Stergere cont D.";
                            frame.validate();
                        }
                    });
                    stergereDispecerBTN.setBackground(color1);
                    stergereDispecerBTN.setForeground(Color.lightGray);
                    stergereDispecerBTN.setFocusPainted(false);

                    plafComponentsAction.removeAll();
//                    plafComponents.add(plafChooser,BorderLayout.WEST);
                    plafComponentsAction.add(updateDispecerBTN, BorderLayout.EAST);
                    plafComponentsAction.add(delogareDispecerBTN, BorderLayout.EAST);
                    plafComponentsAction.add(stergereDispecerBTN, BorderLayout.EAST);
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();

                } else if (plafNames[i].equals("Medic")) {
                    JButton inregistrareMedicBTN = new JButton("Inregistrare M.");
                    inregistrareMedicBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Inregistrare M.";
                            frame.validate();
                        }
                    });
                    inregistrareMedicBTN.setBackground(color1);
                    inregistrareMedicBTN.setForeground(Color.lightGray);
                    inregistrareMedicBTN.setFocusPainted(false);

                    JButton updateMedicBTN = new JButton("Update Cont M.");
                    updateMedicBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Update Cont M.";
                            frame.validate();
                        }
                    });
                    updateMedicBTN.setBackground(color1);
                    updateMedicBTN.setForeground(Color.lightGray);
                    updateMedicBTN.setFocusPainted(false);

                    JButton stergereMedicBTN = new JButton("Stergere cont M.");
                    stergereMedicBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Stergere cont M.";
                            frame.validate();
                        }
                    });
                    stergereMedicBTN.setBackground(color1);
                    stergereMedicBTN.setForeground(Color.lightGray);
                    stergereMedicBTN.setFocusPainted(false);

                    plafComponentsAction.removeAll();
//                    plafComponents.add(plafChooser,BorderLayout.WEST);
                    plafComponentsAction.add(inregistrareMedicBTN, BorderLayout.EAST);
                    plafComponentsAction.add(updateMedicBTN, BorderLayout.EAST);
                    plafComponentsAction.add(stergereMedicBTN, BorderLayout.EAST);
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                } else if (plafNames[i].equals("Ambulanta")) {
                    JButton inregistrareAmbulantaBTN = new JButton("Inregistrare A.");
                    inregistrareAmbulantaBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Inregistrare A.";
                            frame.validate();
                        }
                    });
                    inregistrareAmbulantaBTN.setBackground(color1);
                    inregistrareAmbulantaBTN.setForeground(Color.lightGray);
                    inregistrareAmbulantaBTN.setFocusPainted(false);

                    JButton updateAmbulantaBTN = new JButton("Update Cont A.");
                    updateAmbulantaBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Update Cont A.";
                            frame.validate();
                        }
                    });
                    updateAmbulantaBTN.setBackground(color1);
                    updateAmbulantaBTN.setForeground(Color.lightGray);
                    updateAmbulantaBTN.setFocusPainted(false);

                    JButton stergereAmbulantaBTN = new JButton("Stergere cont A.");
                    stergereAmbulantaBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Stergere cont A.";
                            frame.validate();
                        }
                    });
                    stergereAmbulantaBTN.setBackground(color1);
                    stergereAmbulantaBTN.setForeground(Color.lightGray);
                    stergereAmbulantaBTN.setFocusPainted(false);

                    plafComponentsAction.removeAll();
//                    plafComponents.add(plafChooser,BorderLayout.WEST);
                    plafComponentsAction.add(inregistrareAmbulantaBTN, BorderLayout.EAST);
                    plafComponentsAction.add(updateAmbulantaBTN, BorderLayout.EAST);
                    plafComponentsAction.add(stergereAmbulantaBTN, BorderLayout.EAST);
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                } else if (plafNames[i].equals("Pacient")) {
                    JButton avertizarePacientBTN = new JButton("Avertizare P.");
                    avertizarePacientBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Avertizare P.";
                            frame.validate();
                        }
                    });
                    avertizarePacientBTN.setBackground(color1);
                    avertizarePacientBTN.setForeground(Color.lightGray);
                    avertizarePacientBTN.setFocusPainted(false);

                    JButton banarePacientBTN = new JButton("Banare P.");
                    banarePacientBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Banare P.";
                            frame.validate();
                        }
                    });
                    banarePacientBTN.setBackground(color1);
                    banarePacientBTN.setForeground(Color.lightGray);
                    banarePacientBTN.setFocusPainted(false);

                    JButton raportarePolitieBTN = new JButton("Raportare Politie");
                    raportarePolitieBTN.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            pacientAction = "Raportare Politie";
                            frame.validate();
                        }
                    });
                    raportarePolitieBTN.setBackground(color1);
                    raportarePolitieBTN.setForeground(Color.lightGray);
                    raportarePolitieBTN.setFocusPainted(false);

                    plafComponentsAction.removeAll();
//                    plafComponents.add(plafChooser,BorderLayout.WEST);
                    plafComponentsAction.add(avertizarePacientBTN, BorderLayout.EAST);
                    plafComponentsAction.add(banarePacientBTN, BorderLayout.EAST);
                    plafComponentsAction.add(raportarePolitieBTN, BorderLayout.EAST);
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                } else if (plafNames[i].equals("Selecteaza..")) {
                    plafComponentsAction.removeAll();
//                    plafComponents.add(plafChooser,BorderLayout.WEST);
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                }
            }
        });

        gui.add(plafComponents, BorderLayout.NORTH);

        JPanel dynamicLabels = new JPanel(new BorderLayout(4, 4));
        dynamicLabels.setBorder(
                new TitledBorder("Panou Urgente"));
        gui.add(dynamicLabels, BorderLayout.WEST);

        final JPanel labels = new JPanel(new GridLayout(0, 1, 3, 3));
        labels.setBorder(
                new TitledBorder("Urgente : "));

        JButton infoUrgentaBTN = new JButton("Informatii Urgenta");

        infoUrgentaBTN.setBackground(color1);
        infoUrgentaBTN.setForeground(Color.lightGray);
        infoUrgentaBTN.setFocusPainted(false);
        dynamicLabels.add(infoUrgentaBTN, BorderLayout.NORTH);

        ButtonGroup group = new ButtonGroup();

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton btn = (JRadioButton) e.getSource();
                JOptionPane.showMessageDialog(null,SOSinfo.get(btn.getText()),"SdA",JOptionPane.INFORMATION_MESSAGE);
            }
        };

        JRadioButton jr = new JRadioButton("----------");

        jr.setFocusPainted(false);
        jr.addActionListener(listener);
        SOSinfo.put("----------","--");
        labels.add(jr);
        group.add(jr);
        frame.validate();



        dynamicLabels.add(new JScrollPane(labels), BorderLayout.CENTER);

        String[] headerP = {"Nume", "Locatie", "Numar de telefon"};
        String[] headerA = {"Mumar inmatriculare", "Sofer", "Medic"};

            dataP[0][0] = "--";
            dataP[0][1] = "--";
            dataP[0][2] = "--";


            dataA[0][0] = "--";
            dataA[0][1] = "--";
            dataA[0][2] = "--";

        DefaultTableModel modelP = new DefaultTableModel(dataP, headerP);
        DefaultTableModel modelA = new DefaultTableModel(dataA, headerA);



        JTable table = new JTable(modelP);
        JTable tableAmbulanta = new JTable(modelA);
        try {
            // 1.6+
            table.setAutoCreateRowSorter(true);
            tableAmbulanta.setAutoCreateRowSorter(true);
        } catch (Exception continuewithNoSort) {
        }
        JScrollPane tableScroll = new JScrollPane(table);
        JScrollPane tableScrollAmbulanta = new JScrollPane(tableAmbulanta);
        Dimension tablePreferred = tableScroll.getPreferredSize();
        Dimension tablePreferredAmbulanta = tableScrollAmbulanta.getPreferredSize();
        tableScroll.setPreferredSize(
                new Dimension(tablePreferred.width, tablePreferred.height / 3));
        tableScrollAmbulanta.setPreferredSize(
                new Dimension(tablePreferredAmbulanta.width, tablePreferredAmbulanta.height / 3));
        JPanel panelPacAmb = new JPanel(new GridLayout(0, 2));
        TitledBorder centerBorder = BorderFactory.createTitledBorder("Panou Pacienti - Ambulante");
        centerBorder.setTitleJustification(TitledBorder.CENTER);
        panelPacAmb.setBorder(centerBorder);
        panelPacAmb.add(tableScroll);
        panelPacAmb.add(tableScrollAmbulanta);
        updateMsg = new JButton("updateMsg");
        updateMsg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(!nume.equals("-")&& !mesaj.equals("-"))
                {
                    updateMesaje(nume,mesaj);
                }
                frame.validate();
            }
        });
        imagePanel = new JPanel(new GridLayout(0, 1));
        imagePanel.setBorder(
                new TitledBorder("SdA- Chat Medici/Ambulante"));
        msgPan = new JPanel(new BorderLayout());
        msgPan.add(imagePanel,BorderLayout.NORTH);
        BufferedImage bi = new BufferedImage(
                200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        GradientPaint gp = new GradientPaint(
                20f, 20f, Color.red, 180f, 180f, Color.yellow);
        g.setPaint(gp);
        g.fillRect(0, 0, 200, 200);
        ImageIcon ii = new ImageIcon(bi);
        JLabel imageLabel = new JLabel(ii);
//        imagePanel.add(imageLabel, null);


        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                panelPacAmb,
                new JScrollPane(msgPan));


        updatePanouriBTN = new JButton("Update Panouri");
        updatePanouriBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DefaultTableModel modelPa = new DefaultTableModel(dataP, headerP);
                table.setModel(modelPa);

                DefaultTableModel modelAm = new DefaultTableModel(dataA, headerA);
                tableAmbulanta.setModel(modelAm);



                if(!urgenteUpdate.isEmpty())
                {
                    group.clearSelection();
                    labels.removeAll();
                    for(String urgs:urgenteUpdate) {
                        JRadioButton jr = new JRadioButton(urgs);
                        jr.setFocusPainted(false);
                        jr.addActionListener(listener);
                        labels.add(jr);
                        group.add(jr);
                        frame.validate();
                    }
                }

                labels.revalidate();
                labels.repaint();

//                System.out.print("Date Pac : ");
//                DataPrint(dataP);
//                System.out.println();
//                System.out.print("Date Amb : ");
//                DataPrint(dataA);
//                System.out.println();
            }
        });

        gui.add(splitPane, BorderLayout.CENTER);
    }
    private void login() {
        JPanel loginP = new JPanel(new GridLayout(0, 1, 3, 3));
        ButtonGroup group = new ButtonGroup();
        JRadioButton jr = new JRadioButton("Logare");
        jr.setFocusPainted(false);
        JRadioButton jr1 = new JRadioButton("Inregistrare");
        jr1.setFocusPainted(false);
        group.add(jr);
        group.add(jr1);
        JPanel RadioPanel = new JPanel(new GridLayout(0, 2, 0, 0));
        RadioPanel.add(jr);
        RadioPanel.add(jr1);




        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton btn = (JRadioButton) e.getSource();
                System.out.println("Selected Button = " + btn.getText());

                if (btn.getText().equals("Logare"))
                {
                    btnLogare(loginP, RadioPanel);
                }
                else if (btn.getText().equals("Inregistrare"))
                {
                    btnInregistrare(loginP, RadioPanel, jr);
                }

            }
        };

        if ( Dispecer.dispecerLogat == 0 )
        {
            jr.addActionListener(listener);
            jr1.addActionListener(listener);
            jr.doClick();


            JPanel bagLoginPanel = new JPanel(new GridBagLayout());
            bagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
            Color color = new Color(150,0,0, 0 );
            bagLoginPanel.setBackground(color);
            loginP.add(RadioPanel);
            bagLoginPanel.add(loginP);


            gui.add(bagLoginPanel, BorderLayout.NORTH);
        }
    }
    private void btnLogare (JPanel loginP, JPanel RadioPanel) {
        loginP.removeAll();
        loginP.setBorder(new TitledBorder("Logare"));

        JButton logareAmbulantaBTN = new JButton("Logare");

        final JPanel loginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
        JLabel userLabel = new JLabel("Utilizator: ");
        loginPanel.add(userLabel, BorderLayout.CENTER);
        JTextField userTextFied = new JTextField();
        loginPanel.add(userTextFied, BorderLayout.CENTER);
        JLabel passLabel = new JLabel("Parola :          ");
        loginPanel.add(passLabel, BorderLayout.CENTER);
        JPasswordField passTextFied = new JPasswordField();
        loginPanel.add(passTextFied, BorderLayout.CENTER);

        logareAmbulantaBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
//                            JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                byte[] cryptedUsername = null;
                byte[] cpryptedPassword = null;
                String clientLoginAnswer = "0";
                PublicKey ServerPubKey = null;
                try {
                    ServerPubKey = (PublicKey) Dispecer.oin.readObject();


                    System.out.println("pubkey : " + ServerPubKey);
                    System.out.println("username : " + userTextFied.getText());
                    System.out.println("password : " + passTextFied.getPassword());

                    cryptedUsername = Dispecer.encrypt(ServerPubKey, userTextFied.getText());
                    cpryptedPassword = Dispecer.encrypt(ServerPubKey,new String(passTextFied.getPassword()));
                    userTextFied.setText("");
                    passTextFied.setText("");
                    Dispecer.oop.writeObject(cryptedUsername);
                    Dispecer.oop.writeObject(cpryptedPassword);

                    clientLoginAnswer = Dispecer.oin.readObject().toString();

                    if(clientLoginAnswer.equals("1")) {
                        JOptionPane.showMessageDialog(null, "Logare reusita!", "SdA", JOptionPane.INFORMATION_MESSAGE);
                        Dispecer.dispecerLogat = 1;
                        gui.removeAll();
                        Dispecer.loginSignal.countDown();
                        updatePanouri();
                        app();

//                        frame.dispose();
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
                frame.validate();
            }
        });
        Color color1 = new Color(150,0,0 );
        logareAmbulantaBTN.setBackground(color1);
        logareAmbulantaBTN.setForeground(Color.lightGray);
        logareAmbulantaBTN.setFocusPainted(false);
        JButton InchidereAmbulantaBTN = new JButton("Inchidere");
        InchidereAmbulantaBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.validate();
            }
        });
        InchidereAmbulantaBTN.setBackground(color1);
        InchidereAmbulantaBTN.setForeground(Color.lightGray);
        InchidereAmbulantaBTN.setFocusPainted(false);



        final JPanel loginBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginBtnPanel.add(logareAmbulantaBTN, BorderLayout.CENTER);
        loginBtnPanel.add(InchidereAmbulantaBTN, BorderLayout.CENTER);

        loginP.add(loginPanel, BorderLayout.CENTER);
        loginP.add(loginBtnPanel, BorderLayout.CENTER);
        loginP.add(RadioPanel);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    private void updateDispcerDB(String nume, String parola, PublicKey ServerPubKey) {
        byte[] cryptedNumePrenume = null;
        byte[] cryptedParola = null;

        try {
            cryptedNumePrenume = Dispecer.encrypt(ServerPubKey, nume);
            cryptedParola = Dispecer.encrypt(ServerPubKey, parola);
            Dispecer.oop.writeObject(cryptedNumePrenume);
            Dispecer.oop.writeObject(cryptedParola);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void stergereDispcerDB(String parola, PublicKey ServerPubKey) {
        byte[] cryptedParola = null;
        try {
            cryptedParola = Dispecer.encrypt(ServerPubKey, parola);
            Dispecer.oop.writeObject(cryptedParola);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stergereMedicDB(String CNPMed, PublicKey ServerPubKey) {
        byte[] cryptedCNPMed = null;
        try {
            cryptedCNPMed = Dispecer.encrypt(ServerPubKey, CNPMed);
            Dispecer.oop.writeObject(cryptedCNPMed);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stergereAmbulantaDB(String NrMat, PublicKey ServerPubKey) {
        byte[] cryptedNrMat = null;
        try {
            cryptedNrMat = Dispecer.encrypt(ServerPubKey, NrMat);
            Dispecer.oop.writeObject(cryptedNrMat);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void avertizarePaceintDB(String Uid, PublicKey ServerPubKey) {
        byte[] cryptedUid = null;
        try {
            cryptedUid = Dispecer.encrypt(ServerPubKey, Uid);
            Dispecer.oop.writeObject(cryptedUid);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void banarePaceintDB(String Uid, PublicKey ServerPubKey) {
        byte[] cryptedUid = null;
        try {
            cryptedUid = Dispecer.encrypt(ServerPubKey, Uid);
            Dispecer.oop.writeObject(cryptedUid);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateMedicDB(String uid,String nume,String nrTel, String parola, PublicKey ServerPubKey) {
        byte[] cryptedUid = null;
        byte[] cryptedNumePrenume = null;
        byte[] cryptedNrTel = null;
        byte[] cryptedParola = null;

        try {
            cryptedUid = Dispecer.encrypt(ServerPubKey, uid);
            cryptedNumePrenume = Dispecer.encrypt(ServerPubKey, nume);
            cryptedNrTel = Dispecer.encrypt(ServerPubKey, nrTel);
            cryptedParola = Dispecer.encrypt(ServerPubKey, parola);
            Dispecer.oop.writeObject(cryptedUid);
            Dispecer.oop.writeObject(cryptedNumePrenume);
            Dispecer.oop.writeObject(cryptedNrTel);
            Dispecer.oop.writeObject(cryptedParola);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void updateAmbulantaDB(String uid,String nume,String nrTel, String parola, PublicKey ServerPubKey) {
        byte[] cryptedUid = null;
        byte[] cryptedSofer = null;
        byte[] cryptedMedic = null;
        byte[] cryptedParola = null;

        try {
            cryptedUid = Dispecer.encrypt(ServerPubKey, uid);
            cryptedSofer = Dispecer.encrypt(ServerPubKey, nume);
            cryptedMedic = Dispecer.encrypt(ServerPubKey, nrTel);
            cryptedParola = Dispecer.encrypt(ServerPubKey, parola);
            Dispecer.oop.writeObject(cryptedUid);
            Dispecer.oop.writeObject(cryptedSofer);
            Dispecer.oop.writeObject(cryptedMedic);
            Dispecer.oop.writeObject(cryptedParola);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void inregAmbulantaMedicDB(String NrMat,String uid,String pass,String Sofer, String Medic, PublicKey ServerPubKey) {
        byte[] cryptedNrMat = null;
        byte[] cryptedUid = null;
        byte[] cryptedParola = null;
        byte[] cryptedSofer = null;
        byte[] cryptedMedic = null;


        try {
            cryptedNrMat = Dispecer.encrypt(ServerPubKey, NrMat);
            cryptedUid = Dispecer.encrypt(ServerPubKey, uid);
            cryptedParola = Dispecer.encrypt(ServerPubKey, pass);
            cryptedSofer = Dispecer.encrypt(ServerPubKey, Sofer);
            cryptedMedic = Dispecer.encrypt(ServerPubKey, Medic);

            Dispecer.oop.writeObject(cryptedNrMat);
            Dispecer.oop.writeObject(cryptedUid);
            Dispecer.oop.writeObject(cryptedParola);
            Dispecer.oop.writeObject(cryptedSofer);
            Dispecer.oop.writeObject(cryptedMedic);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void btnInregistrare (JPanel loginP, JPanel RadioPanel, JRadioButton jr) {
        loginP.removeAll();
        loginP.setBorder(new TitledBorder("Inregistrare"));

        final JPanel loginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
        JLabel cnpLabel = new JLabel("CNP : ");
        loginPanel.add(cnpLabel, BorderLayout.CENTER);
        JTextField cnpTextFied = new JTextField();
        loginPanel.add(cnpTextFied, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("Utilizator : ");
        loginPanel.add(userLabel, BorderLayout.CENTER);
        JTextField userTextFied = new JTextField();
        loginPanel.add(userTextFied, BorderLayout.CENTER);

        JLabel passLabel = new JLabel("Parola : ");
        loginPanel.add(passLabel, BorderLayout.CENTER);
        JPasswordField passTextFied = new JPasswordField();
        loginPanel.add(passTextFied, BorderLayout.CENTER);

        JLabel numeLabel = new JLabel("Nume & Prenume : ");
        loginPanel.add(numeLabel, BorderLayout.CENTER);
        JTextField numeTextFied = new JTextField();
        loginPanel.add(numeTextFied, BorderLayout.CENTER);

        JButton inregistrareAmbulantaBTN = new JButton("Inregistrare");
        inregistrareAmbulantaBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                PublicKey ServerPubKey = null;
                byte[] checkIfRegister = "#".getBytes();

                try {
                    ServerPubKey = (PublicKey) Dispecer.oin.readObject();
                    Dispecer.oop.writeObject(checkIfRegister);

                    byte[] cryptedUid = null;
                    byte[] cryptedCnp = null;
                    byte[] cryptedPass =  null;
                    byte[] cryptedName =  null;
                    JOptionPane.showMessageDialog(null,"Hello","Dysplay Message",JOptionPane.INFORMATION_MESSAGE);
                    try {
                        if(countC == 1)
                        {
                            Dispecer.oop.writeObject("0");
                        }
                        if (cnpTextFied.getText().equals("") || userTextFied.getText().equals("") || passTextFied.getPassword().equals("") || numeTextFied.getText().equals("")) {
                            JOptionPane.showMessageDialog(null,"Please fill all the fields below!","Warning",JOptionPane.INFORMATION_MESSAGE);
                            countC --;
                        }
                        else
                        {
                            cryptedCnp = Dispecer.encrypt(ServerPubKey,cnpTextFied.getText());
                            Dispecer.oop.writeObject(cryptedCnp);
                            cryptedUid = Dispecer.encrypt(ServerPubKey,userTextFied.getText());
                            Dispecer.oop.writeObject(cryptedUid);
                            String cnpUsed = null;
                            cnpUsed = (String) Dispecer.oin.readObject();
                            if(cnpUsed.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"CNP already used!","Warning",JOptionPane.INFORMATION_MESSAGE);
                                countC --;
                            }
                            else if(cnpUsed.equals("1"))
                            {

                                cryptedPass = Dispecer.encrypt(ServerPubKey,String.valueOf(passTextFied.getPassword()));
                                cryptedName = Dispecer.encrypt(ServerPubKey,numeTextFied.getText());

                                Dispecer.oop.writeObject(cryptedPass);
                                Dispecer.oop.writeObject(cryptedName);

                                String recordInserted = (String) Dispecer.oin.readObject();

                                if (recordInserted.equals("1"))
                                {
                                    JOptionPane.showMessageDialog(null,"Welcome to SdA!","SdA",JOptionPane.INFORMATION_MESSAGE);
                                }
                                else if (recordInserted.equals("0"))
                                {
                                    JOptionPane.showMessageDialog(null,"Registration failed! \n Please contact the adimn or try again!","SdA",JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                jr.doClick();
                frame.validate();

            }
        });
        Color color1 = new Color(150,0,0 );
        inregistrareAmbulantaBTN.setBackground(color1);
        inregistrareAmbulantaBTN.setForeground(Color.lightGray);
        inregistrareAmbulantaBTN.setFocusPainted(false);

        JButton inchidereBTN = new JButton("Inchidere");
        inchidereBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.validate();
            }
        });
        inchidereBTN.setBackground(color1);
        inchidereBTN.setForeground(Color.lightGray);
        inchidereBTN.setFocusPainted(false);



        final JPanel loginBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginBtnPanel.add(inregistrareAmbulantaBTN, BorderLayout.CENTER);
        loginBtnPanel.add(inchidereBTN, BorderLayout.CENTER);

        loginP.add(loginPanel, BorderLayout.CENTER);
        loginP.add(loginBtnPanel, BorderLayout.CENTER);
        loginP.add(RadioPanel);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    private void updatePanouri () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Dispecer.dispecerLogat == 1) {
                    try {
                        byte[] cryptedPacientAction = null;
                        PublicKey ServerPubKey = null;
                        ServerPubKey = (PublicKey) Dispecer.oin.readObject();
                        cryptedPacientAction = Dispecer.encrypt(ServerPubKey, pacientAction);
                        Dispecer.oop.writeObject(cryptedPacientAction);

                        if (pacientAction.equals("Monitorizare")) {
                            StringBuilder pac = (StringBuilder) Dispecer.oin.readObject();
                            StringBuilder amb = (StringBuilder) Dispecer.oin.readObject();
                            StringBuilder urg = (StringBuilder) Dispecer.oin.readObject();
                            StringBuilder msgAmbulante = (StringBuilder) Dispecer.oin.readObject();
                            StringBuilder msgMedic = (StringBuilder) Dispecer.oin.readObject();

                            dataP = new String[1000][3];
                            dataP = parcurgereLista(pac);

                            dataA = new String[1000][3];
                            dataA = parcurgereLista(amb);

                            urgenteUpdate = parcurgereUrgente(urg);
                            msgAmbulanteUpdate = parcurgereMsgAmbulante(msgAmbulante);
                            System.out.println(msgAmbulanteUpdate.size());
                            if (!msgAmbulanteUpdate.isEmpty()) {
                                for (Map.Entry<String, String> entry : msgAmbulanteUpdate.entrySet()) {
                                    nume = entry.getKey();
                                    mesaj = entry.getValue();
                                    updateMesaje(nume, mesaj);
                                    imagePanel.revalidate();
                                    imagePanel.repaint();
                                }
                            }
                            msgAmbulanteUpdate = parcurgereMsgAmbulante(msgMedic);
                            if (!msgAmbulanteUpdate.isEmpty()) {
                                for (Map.Entry<String, String> entry : msgAmbulanteUpdate.entrySet()) {
                                    nume = entry.getKey();
                                    mesaj = entry.getValue();
                                    updateMesaje(nume, mesaj);
                                    imagePanel.revalidate();
                                    imagePanel.repaint();
                                }
                            }
                            updatePanouriBTN.doClick();
                        } else if (pacientAction.equals("Logout")) {
                            Dispecer.dispecerLogat = 0;

                        }else if (pacientAction.equals("Update Cont D.")) {
                            JFrame auxFrame = new JFrame("Update cont dispecer");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxuserLabel = new JLabel("Nume Prenume: ");
                            auxloginPanel.add(auxuserLabel, BorderLayout.CENTER);
                            JTextField auxuserTextFied = new JTextField();
                            auxloginPanel.add(auxuserTextFied, BorderLayout.CENTER);
                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);

                            JButton updateDispecer = new JButton("Update");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if (auxuserTextFied.getText().isEmpty() || auxpassTextFied.getPassword().toString().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Update anulat! \n Unul dintre campuri nu a fost completat.","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        updateDispcerDB("gol","gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        updateDispcerDB(auxuserTextFied.getText(),new String(auxpassTextFied.getPassword()), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    updateDispcerDB("gol","gol",finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);


                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();

                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                System.out.println("Update anulat!");
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Update realizat cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }

                        }else if (pacientAction.equals("Stergere cont D.")) {

                            JFrame auxFrame = new JFrame("Stergere cont dispecer");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);
                            JButton updateDispecer = new JButton("Stergere");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if ( auxpassTextFied.getPassword().toString().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Stergere anulata! \n Parola nu a fost completata","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        stergereDispcerDB("gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        stergereDispcerDB(new String(auxpassTextFied.getPassword()), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    stergereDispcerDB("gol", finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);


                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Inregistrare M.")) {

                            JFrame auxFrame = new JFrame("Inregistrare Medic");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));

                            JLabel auxCNPLabel = new JLabel("CNP: ");
                            auxloginPanel.add(auxCNPLabel, BorderLayout.CENTER);
                            JTextField auxCNPTextFied = new JTextField();
                            auxloginPanel.add(auxCNPTextFied, BorderLayout.CENTER);

                            JLabel auxuserLabel = new JLabel("Nume Prenume: ");
                            auxloginPanel.add(auxuserLabel, BorderLayout.CENTER);
                            JTextField auxuserTextFied = new JTextField();
                            auxloginPanel.add(auxuserTextFied, BorderLayout.CENTER);

                            JLabel auxUidLabel = new JLabel("Uid: ");
                            auxloginPanel.add(auxUidLabel, BorderLayout.CENTER);
                            JTextField auxUidTextFied = new JTextField();
                            auxloginPanel.add(auxUidTextFied, BorderLayout.CENTER);


                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);

                            JLabel auxNrTelLabel = new JLabel("Numar telefon: ");
                            auxloginPanel.add(auxNrTelLabel, BorderLayout.CENTER);
                            JTextField auxNrTelTextFied = new JTextField();
                            auxloginPanel.add(auxNrTelTextFied, BorderLayout.CENTER);


                            JButton updateDispecer = new JButton("Inregistrare");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if (auxCNPTextFied.getText().isEmpty() || auxuserTextFied.getText().isEmpty() || auxUidTextFied.getText().isEmpty()
                                            || auxpassTextFied.getPassword().toString().isEmpty() || auxNrTelTextFied.getText().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Inregistrare anulata! \n Unul dintre campuri nu a fost completat.","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        inregAmbulantaMedicDB("gol","gol","gol","gol","gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        inregAmbulantaMedicDB(auxCNPTextFied.getText(),auxuserTextFied.getText(),auxUidTextFied.getText(),new String(auxpassTextFied.getPassword()),auxNrTelTextFied.getText(), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    inregAmbulantaMedicDB("gol","gol","gol","gol","gol",finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);




                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Inregistrare anulata! \n Medic existent, CNP invalid sau campuri necompletate.","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Inregistrare realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Update Cont M.")) {
                            JFrame auxFrame = new JFrame("Update cont Medic");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxUidLabel = new JLabel("Uid: ");
                            auxloginPanel.add(auxUidLabel, BorderLayout.CENTER);
                            JTextField auxUidTextFied = new JTextField();
                            auxloginPanel.add(auxUidTextFied, BorderLayout.CENTER);
                            JLabel auxuserLabel = new JLabel("Nume Prenume: ");
                            auxloginPanel.add(auxuserLabel, BorderLayout.CENTER);
                            JTextField auxuserTextFied = new JTextField();
                            auxloginPanel.add(auxuserTextFied, BorderLayout.CENTER);
                            JLabel auxNrTelLabel = new JLabel("Numar telefon: ");
                            auxloginPanel.add(auxNrTelLabel, BorderLayout.CENTER);
                            JTextField auxNrTelTextFied = new JTextField();
                            auxloginPanel.add(auxNrTelTextFied, BorderLayout.CENTER);
                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);

                            JButton updateDispecer = new JButton("Update");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if (auxUidTextFied.getText().isEmpty() ||auxuserTextFied.getText().isEmpty() || auxNrTelTextFied.getText().isEmpty() || auxpassTextFied.getPassword().toString().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Update anulat! \n Unul dintre campuri nu a fost completat.","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        updateMedicDB("gol","gol","gol","gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        updateMedicDB(auxUidTextFied.getText(),auxuserTextFied.getText(),auxNrTelTextFied.getText(),new String(auxpassTextFied.getPassword()), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    updateMedicDB("gol","gol","gol","gol",finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Update anulat! \n Medicul nu a fost gasit.","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Update realizat cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Stergere cont M.")) {


                            JFrame auxFrame = new JFrame("Stergere cont Medic");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxpassLabel = new JLabel("CNP Medic :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JTextField auxpassTextFied = new JTextField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);
                            JButton updateDispecer = new JButton("Stergere");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if ( auxpassTextFied.getText().isEmpty())
                                    {
                                        JOptionPane.showMessageDialog(null,"Stergere anulata! \n Parola nu a fost completata","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        stergereMedicDB("gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        stergereMedicDB(auxpassTextFied.getText(), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    stergereMedicDB("gol", finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);

                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Inregistrare A.")) {

                            JFrame auxFrame = new JFrame("Inregistrare Ambulanta");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxCNPLabel = new JLabel("Numar inmatriculare: ");
                            auxloginPanel.add(auxCNPLabel, BorderLayout.CENTER);
                            JTextField auxCNPTextFied = new JTextField();
                            auxloginPanel.add(auxCNPTextFied, BorderLayout.CENTER);
                            JLabel auxUidLabel = new JLabel("Uid: ");
                            auxloginPanel.add(auxUidLabel, BorderLayout.CENTER);
                            JTextField auxUidTextFied = new JTextField();
                            auxloginPanel.add(auxUidTextFied, BorderLayout.CENTER);
                            JLabel auxuserLabel = new JLabel("Sofer: ");
                            auxloginPanel.add(auxuserLabel, BorderLayout.CENTER);
                            JTextField auxuserTextFied = new JTextField();
                            auxloginPanel.add(auxuserTextFied, BorderLayout.CENTER);
                            JLabel auxNrTelLabel = new JLabel("Medic: ");
                            auxloginPanel.add(auxNrTelLabel, BorderLayout.CENTER);
                            JTextField auxNrTelTextFied = new JTextField();
                            auxloginPanel.add(auxNrTelTextFied, BorderLayout.CENTER);
                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);

                            JButton updateDispecer = new JButton("Inregistrare");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if (auxCNPTextFied.getText().isEmpty() || auxUidTextFied.getText().isEmpty() || auxuserTextFied.getText().isEmpty() ||
                                            auxNrTelTextFied.getText().isEmpty() || auxpassTextFied.getPassword().toString().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Inregistrare anulata! \n Unul dintre campuri nu a fost completat.","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        inregAmbulantaMedicDB("gol","gol","gol","gol","gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        inregAmbulantaMedicDB(auxCNPTextFied.getText(),auxUidTextFied.getText(),auxuserTextFied.getText(),auxNrTelTextFied.getText(),new String(auxpassTextFied.getPassword()), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    inregAmbulantaMedicDB("gol","gol","gol","gol","gol",finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Inregistrare anulate! \n Ambulanta exista deja sau campurile nu sunt complete. ","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Update realizat cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Update Cont A.")) {

                            JFrame auxFrame = new JFrame("Update cont Ambulanta");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxUidLabel = new JLabel("Uid: ");
                            auxloginPanel.add(auxUidLabel, BorderLayout.CENTER);
                            JTextField auxUidTextFied = new JTextField();
                            auxloginPanel.add(auxUidTextFied, BorderLayout.CENTER);
                            JLabel auxuserLabel = new JLabel("Sofer: ");
                            auxloginPanel.add(auxuserLabel, BorderLayout.CENTER);
                            JTextField auxuserTextFied = new JTextField();
                            auxloginPanel.add(auxuserTextFied, BorderLayout.CENTER);
                            JLabel auxNrTelLabel = new JLabel("Medic: ");
                            auxloginPanel.add(auxNrTelLabel, BorderLayout.CENTER);
                            JTextField auxNrTelTextFied = new JTextField();
                            auxloginPanel.add(auxNrTelTextFied, BorderLayout.CENTER);
                            JLabel auxpassLabel = new JLabel("Parola :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JPasswordField auxpassTextFied = new JPasswordField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);

                            JButton updateDispecer = new JButton("Update");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if (auxUidTextFied.getText().isEmpty() ||auxuserTextFied.getText().isEmpty() || auxNrTelTextFied.getText().isEmpty() || auxpassTextFied.getPassword().toString().isEmpty() )
                                    {
                                        JOptionPane.showMessageDialog(null,"Update anulat! \n Unul dintre campuri nu a fost completat.","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        updateAmbulantaDB("gol","gol","gol","gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        updateAmbulantaDB(auxUidTextFied.getText(),auxuserTextFied.getText(),auxNrTelTextFied.getText(),new String(auxpassTextFied.getPassword()), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    updateAmbulantaDB("gol","gol","gol","gol",finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Update anulat! \n Ambulanta nu a fist gasita in baza de date.","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Update realizat cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Stergere cont A.")) {


                            JFrame auxFrame = new JFrame("Stergere cont Ambulanta");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxpassLabel = new JLabel("Numar inmatriculare :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JTextField auxpassTextFied = new JTextField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);
                            JButton updateDispecer = new JButton("Stergere");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if ( auxpassTextFied.getText().isEmpty())
                                    {
                                        JOptionPane.showMessageDialog(null,"Stergere anulata! \n Parola nu a fost completata","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        stergereAmbulantaDB("gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        stergereAmbulantaDB(auxpassTextFied.getText(), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    stergereAmbulantaDB("gol", finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);

                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Stergere realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Avertizare P.")) {

                            JFrame auxFrame = new JFrame("Avertizate Pacient");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxpassLabel = new JLabel("Uid Pacient :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JTextField auxpassTextFied = new JTextField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);
                            JButton updateDispecer = new JButton("Avertizare");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if ( auxpassTextFied.getText().isEmpty())
                                    {
                                        JOptionPane.showMessageDialog(null,"Avertizare anulata! \n Uid-ul nu a fost completat","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        avertizarePaceintDB("gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        avertizarePaceintDB(auxpassTextFied.getText(), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    avertizarePaceintDB("gol", finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Avertizare esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Avertizare realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else if (pacientAction.equals("Banare P.")) {

                            JFrame auxFrame = new JFrame("Banare Pacient");
                            auxFrame.setLocation(frame.getLocation());
                            JPanel auxGui = new JPanel(new BorderLayout(5,5));
                            TitledBorder auxTborder = new TitledBorder(new EmptyBorder(10, 10, 10, 10));
                            auxGui.setBorder( auxTborder );
                            JPanel auxbagLoginPanel = new JPanel(new GridBagLayout());
                            auxbagLoginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 200, 200));
                            JPanel auxloginPanel = new JPanel(new GridLayout(0, 2, 3, 3));
                            JLabel auxpassLabel = new JLabel("Uid Pacient :      ");
                            auxloginPanel.add(auxpassLabel, BorderLayout.CENTER);
                            JTextField auxpassTextFied = new JTextField();
                            auxloginPanel.add(auxpassTextFied, BorderLayout.CENTER);
                            JButton updateDispecer = new JButton("Banare");
                            PublicKey finalServerPubKey = ServerPubKey;
                            updateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {

                                    if ( auxpassTextFied.getText().isEmpty())
                                    {
                                        JOptionPane.showMessageDialog(null,"Banare anulata! \n Uid-ul nu a fost completat","SdA",JOptionPane.INFORMATION_MESSAGE);
                                        banarePaceintDB("gol", finalServerPubKey);
                                    }
                                    else
                                    {
                                        banarePaceintDB(auxpassTextFied.getText(), finalServerPubKey);
                                    }
                                    auxFrame.dispose();
                                }
                            });
                            JButton closeUpdateDispecer = new JButton("Close");
                            closeUpdateDispecer.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    banarePaceintDB("gol", finalServerPubKey);
                                    auxFrame.dispose();
                                }
                            });
                            auxloginPanel.add(updateDispecer);
                            auxloginPanel.add(closeUpdateDispecer);
                            auxbagLoginPanel.add(auxloginPanel);
                            auxGui.add(auxbagLoginPanel, BorderLayout.NORTH);
                            auxFrame.setContentPane(auxGui);
                            auxFrame.pack();
                            auxFrame.setVisible(true);



                            String raspunsUpdateDispecerDB = (String) Dispecer.oin.readObject();
                            if(raspunsUpdateDispecerDB.equals("0"))
                            {
                                JOptionPane.showMessageDialog(null,"Banare esuata!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }else if(raspunsUpdateDispecerDB.equals("1"))
                            {
                                JOptionPane.showMessageDialog(null,"Banare realizata cu succes!","SdA",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        else if (pacientAction.equals("Raportare Politie")) {
                                JOptionPane.showMessageDialog(null,"Raport trimis catre politie","SdA",JOptionPane.INFORMATION_MESSAGE);
                        }
                        pacientAction="Monitorizare";
                    } catch (IOException e) {
//                        e.printStackTrace();
                        System.out.println("Conexiunea cu serverul a fost pierduta!");
                        return;
                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
                        System.out.println("Conexiunea cu serverul a fost pierduta!");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (Dispecer.dispecerLogat == 0)
                {
                    gui.removeAll();

                    displayDispecer();
                    frame.invalidate();
                    frame.revalidate();
                    frame.repaint();
                    pacientAction="Monitorizare";
                }
            }
        }).start();
    }
    private String[][] parcurgereLista(StringBuilder lista) {
        String[][] data = new String[1000][3];

        if(lista.length() == 0)
        {
            data[0][0] = "--";
            data[0][1] = "--";
            data[0][2] = "--";
            return data;
        }
        int count = 0;
        StringBuilder aux = new StringBuilder();
        int idx = 0;
        for(int i=0; i < lista.length() ; i++) {
            if (lista.charAt(i) != ';' && lista.charAt(i) != '.') {
                aux.append(lista.charAt(i));
            }

            else if (lista.charAt(i)==';' && count == 0)
            {
                count = 1;
                data[idx][0] = aux.toString();
//                System.out.println(data[idx][0]);
                aux.setLength(0);
            }
            else if (lista.charAt(i)==';' && count == 1)
            {
                count = 0;
                data[idx][1] = aux.toString();
//                System.out.println(data[idx][1]);
                aux.setLength(0);
            }
            else if (lista.charAt(i)=='.')
            {
                data[idx][2] = aux.toString();
//                System.out.println(data[idx][2]);
                aux.setLength(0);
                idx ++;
            }

        }
        return data;
    }
    private void DataPrint (String[][] dataPac){
        for(int i=0;i<dataPac.length;i++)
        {
            if(dataPac[i][0]!=null && dataPac[i][1]!=null && dataPac[i][2]!=null)
            {
                System.out.println(dataPac[i][0]+"-"+dataPac[i][1]+"-"+dataPac[i][2]);
            }
        }
    }
    private List<String> parcurgereUrgente (StringBuilder urg){
        List<String>  auxURG = new ArrayList<String>();
        if(urg.equals("----------"))
        {
            auxURG.add("----------");
            return auxURG;
        }

        StringBuilder aux = new StringBuilder();
        String copyUrgenta = null;
        for(int i=0; i < urg.length() ; i++){
            if(urg.charAt(i)!=';'&&urg.charAt(i)!='#')
            {
                aux.append(urg.charAt(i));
            }
            else if(urg.charAt(i)=='#')
            {
                auxURG.add(aux.toString());
                SOSinfo.put(aux.toString(),"--");
                copyUrgenta = aux.toString();
                aux.setLength(0);
            }
            else if(urg.charAt(i)==';')
            {
                SOSinfo.replace(copyUrgenta,aux.toString());
                aux.setLength(0);
            }

        }

        return auxURG;
    }
    private void updateMesaje (String nume,String mesaj){
        Color color1 = new Color(165, 151, 153);
        Color color2 = new Color(150,0,0 );
        JPanel mesajPanel = new JPanel(new GridLayout(0, 3));
        JLabel numeLabel = new JLabel(nume);
        JLabel mesajLabel = new JLabel(mesaj);
        JPanel butoanePannel = new JPanel(new GridLayout(0, 2));
        JButton CititBTN = new JButton("Citit");
        CititBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                mesajPanel.setBackground(Color.WHITE);
                frame.validate();
            }
        });
        CititBTN.setBackground(color2);
        CititBTN.setForeground(Color.lightGray);
        CititBTN.setFocusPainted(false);

        Color color = new Color(165, 151, 153);
        JButton NecititBTN = new JButton("Necitit");
        NecititBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                mesajPanel.setBackground(color);
                frame.validate();
            }
        });
        NecititBTN.setBackground(color2);
        NecititBTN.setForeground(Color.lightGray);
        NecititBTN.setFocusPainted(false);

        butoanePannel.add(CititBTN);
        butoanePannel.add(NecititBTN);
        mesajPanel.add(numeLabel);
        mesajPanel.add(mesajLabel);
        mesajPanel.add(butoanePannel);
        imagePanel.add(mesajPanel);
    }
    private Map<String,String>  parcurgereMsgAmbulante(StringBuilder msgAmbulante){
        Map<String,String>  msgAmbulanteUpdateAUX = new HashMap<>();
        if(msgAmbulante.equals("----------"))
        {
            msgAmbulanteUpdateAUX.put("-","-");
            return msgAmbulanteUpdateAUX;
        }
        StringBuilder aux = new StringBuilder();
        StringBuilder expeditor = new StringBuilder();
        StringBuilder mesaj = new StringBuilder();
        for(int i=0; i < msgAmbulante.length() ; i++) {
            if (msgAmbulante.charAt(i) != ',' && msgAmbulante.charAt(i) != ';') {
                aux.append(msgAmbulante.charAt(i));
            }
            else if (msgAmbulante.charAt(i)==',')
            {
                expeditor.append(aux);
                aux.setLength(0);
            }
            else if (msgAmbulante.charAt(i)==';')
            {
                mesaj.append(aux);
                msgAmbulanteUpdateAUX.put(expeditor.toString(),mesaj.toString());
                expeditor.setLength(0);
                mesaj.setLength(0);
                aux.setLength(0);
            }
        }
        return msgAmbulanteUpdateAUX;
    }
}