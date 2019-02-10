package APK;

import java.io.*;
import java.net.*;
import java.security.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.prefs.Preferences;

import javax.crypto.Cipher;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.stream.Location;
import javax.mail.Session;
public class Client extends Server implements Runnable{
	
	private Socket clientSocket = null;
    private String serverText =  null;
    private Preferences preferences = Preferences.userNodeForPackage(Client.class);
    private int aux = 1;
    private String regCloseORReg = null;
    public  String clientType = null;
    private BufferedReader input = null;
    private PrintWriter output = null;
    public ObjectOutputStream oop = null;
    public ObjectInputStream oin = null;
    public boolean astept = true;
    private boolean AmbulantaDisponibila;
    private double DistantaDeParcursInPlus = 0;
    private boolean AmbulantaDisponibilaSOS = true;
    private int dispecerLogat = 0;
    private static List<String> mesajePacient= new ArrayList<>();
    private static Map<Client,String> mesajeAmbulanta= new HashMap<>();
    private static Map<Client,String> mesajeMedic= new HashMap<>();
    private static Map<Client,Client> urgenteInAsteptare= new HashMap<>();
    private boolean pacientPreluatInstant = false;
    private boolean InfoBufferTrimis = false;

    public  StringBuffer BufferSOSinfoPacinet = null;
    public  StringBuffer BufferSOSinfoAmbulanta = null;
    private StringBuffer SosInfoStr;
    private StringBuffer SosInfoStrAmbulanta = new StringBuffer();
    private int cnt = 0;
    private int cnt1 = 0;
    private double latAmb = 0;
    private double lonAmb = 0;

    private  String NumePrenume = null;
    private  String NumePrenumeMed = null;
    private  String uid = null;
    private  String Adresa = null;
    private  boolean blocat;
    private  boolean banat;
    private  boolean avertizat;
    private  int varsta;
    private  String sex = null;
    private  String boli = null;
    private  String numarDeTelefon = null;
    private  String numarDeTelefonMed = null;

    private  String numarInmatriculare = null;
    private  String Sofer = null;
    private  String Medic = null;
    private boolean lock = false;
    private boolean ambulantaLibera = true;
    private byte[] pacientLocationBytes = null;
    private String dispecerUid = null;



    private int authentification = -1;

    private DatabaseConnection mysqlConnect = new DatabaseConnection();

    private boolean cnpValid(String CNP){
        if(CNP.length() != 13 ||!CNP.matches("\\d+"))
        {
            return false;
        }
        else
        {
            String[] Scheck = {"1","2","3","4","5","6","7","8"};

            int suma = Character.getNumericValue(CNP.charAt(0))*2+Character.getNumericValue(CNP.charAt(1))*7+Character.getNumericValue(CNP.charAt(2))*9+Character.getNumericValue(CNP.charAt(3))*1 +
                    Character.getNumericValue(CNP.charAt(4))*4+Character.getNumericValue(CNP.charAt(5))*6+Character.getNumericValue(CNP.charAt(6))*3+Character.getNumericValue(CNP.charAt(7))*5+
                    Character.getNumericValue(CNP.charAt(8))*8+Character.getNumericValue(CNP.charAt(9))*2+Character.getNumericValue(CNP.charAt(10))*7+Character.getNumericValue(CNP.charAt(11))*9;
            suma = suma % 11;
            if (suma == 10)
            {
                suma = 1;
            }
            if (suma != Character.getNumericValue(CNP.charAt(12)))
            {
                return false;
            }
            String S =CNP.substring(0,1);
            if (!Arrays.asList(Scheck).contains(S))
            {
                return false;
            }
            String[] JJcheck = {"01","02","03","04","05","06","07","08","09","10",
                    "11","12","13","14","15","16","17","18","19","20",
                    "21","22","23","24","25","26","27","28","29","30",
                    "31","32","33","34","35","36","37","38","39","40",
                    "41","42","43","44","45","46","47","48","49","50",
                    "51","52"};
            String JJ = CNP.substring(7,9);
            if (!Arrays.asList(JJcheck).contains(JJ))
            {
                return false;
            }
            return true;
        }
    }


    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            oop = new ObjectOutputStream(clientSocket.getOutputStream());
            oin = new ObjectInputStream(clientSocket.getInputStream());
            //String fromDispecer;
            //long time = System.currentTimeMillis();
            KeyPair keyPair = null;
            PublicKey pubKey = null;
            PrivateKey privateKey = null;
            setCredentials("Dispecer","Ambulanta","Pacient","Medic");
            try {
                keyPair = buildKeyPair();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Eroare la generarea cheilor.");
//                e.printStackTrace();
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;

            }
            if(keyPair != null)
            {
                pubKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();
            }
            else
            {
                System.out.println("Eroare la generarea cheilor.");
                return;
            }
//            System.out.println("cheie privata " + privateKey);

            apkAuthentification(pubKey,privateKey);

//			output.write(this.authentification);

//			System.out.println(this.authentification);

            String auth_copy_str = Integer.toString(this.authentification);

//			System.out.println(auth_copy_str);

            oop.writeObject(auth_copy_str);
            System.out.println("Varianta client : " + this.authentification);


            if(this.authentification == 1)
            {
                System.out.println("Aplicatia1 s-a autentificat cu succes!");
                apkDispecer_login(pubKey,privateKey);

            }
            else if (this.authentification == 2)
            {
                System.out.println("Aplicatia2 s-a autentificat cu succes!");
                apkAmbulanta_login(pubKey,privateKey);

            }
            else if (this.authentification == 3)
            {
                System.out.println("Aplicatia3 s-a autentificat cu succes!");
                apkPacient_login(pubKey,privateKey);

            }
            else if (this.authentification == 4)
            {
                System.out.println("Aplicatia3 s-a autentificat cu succes!");
                apkMedic_login(pubKey,privateKey);

            }
            else
            {
                System.out.println("Aplicatia nu s-a autentificat cu succes!");
                System.out.println("ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!");
            }

            //output.println((this.serverText + time + ""));
            //System.out.println(this.serverText +"( " + time + " )");

            //if((fromDispecer = input.readLine()) != null) {
            //	System.out.println("Dispecer: " + fromDispecer);
            //}
            System.out.println("AICI-1");
//			output.close();
//			input.close();
        } catch(IOException e) {
//            e.printStackTrace();
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;

        }
        System.out.println("RUN terminat");
    }
	public Client(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }
    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  

        return cipher.doFinal(message.getBytes());  
    }
    public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
       
        return cipher.doFinal(encrypted);
    }


    public  void   setCredentials(String dispecerType, String ambulantaType, String pacientType, String medicType) {
        preferences.put("dispecerType", dispecerType);
        preferences.put("ambulantaType", ambulantaType);
        preferences.put("pacientType", pacientType);
        preferences.put("medicType", medicType);
    }
	private String get_pacient_type() {
		return preferences.get("pacientType", null);
	}
    private String get_medic_type() {
        return preferences.get("medicType", null);
    }
	private String get_ambulanta_type() {
		return preferences.get("ambulantaType", null);
	}
	private String get_dispecer_type() {
		return preferences.get("dispecerType", null);
	}
	private void   setSessionKey_login(String sessionKey_login){
		preferences.put("sessionKey_login", sessionKey_login);
	}
	private String getSessionKey_login(){
		return preferences.get("sessionKey_login", null);
	}
    private double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // distance in Kilometers

        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public  void apkAuthentification(PublicKey pubkey,PrivateKey privateKey) throws IOException {
        /* ------Autentificarea tipului de aplicatie------
         * Serverul trimite cheia publica clientului
         * Clientul cripteaza cheia de autentificare cu cheia publica primita
         * Serverul primeste cheia de autentificare si o decripteaza cu cheia privata
         * Se apeleaza metoda de logare dupa autentificarea aplicatiei.
         * -----------------------------------------------
         */
        try{
            String apkType_fromServer;
            //byte[] fromDispecer_byte;
            byte[] authKeyFromClient = null;
            byte[] apkTypeFromClient = null;
            String DecryptedAuthKey = null;
            String DecryptedApkType = null;
            oop.writeObject(pubkey);
//            System.out.println(pubkey);

//            System.out.println((PublicKey) oin.readObject());
            byte[] cryptedAuthKeyFromClient;
            byte[] cpryptedApkTypeFromClient;
            cryptedAuthKeyFromClient = (byte[]) oin.readObject();
            cpryptedApkTypeFromClient = (byte[]) oin.readObject();
//            System.out.println(new String(cryptedAuthKeyFromClient,"UTF-8"));
//            System.out.println(new String(cpryptedApkTypeFromClient,"UTF-8"));

            authKeyFromClient = decrypt(privateKey,cryptedAuthKeyFromClient);
            apkTypeFromClient = decrypt(privateKey,cpryptedApkTypeFromClient);
            DecryptedAuthKey = 	new String(authKeyFromClient,"UTF-8");
            apkType_fromServer = 	new String(apkTypeFromClient,"UTF-8");
            clientType = apkType_fromServer;

            System.out.println("Client cheie de autentificare: " + DecryptedAuthKey);
            String sql = "SELECT cheie_de_autentificare FROM autentificare_apk WHERE tip_aplicatie = ? ";
//            DatabaseConnection mysqlConnect = new DatabaseConnection();
            try {
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, apkType_fromServer);
                ResultSet rs = statement.executeQuery();
                String userid = null;
                while (rs.next()) {
                    userid = rs.getString("cheie_de_autentificare");
                }
                if (userid != null)
                {
                    if (userid.equals(DecryptedAuthKey) && apkType_fromServer.equals(get_dispecer_type()) )
                    {
                        this.authentification = 1;
                    }
                    else if (userid.equals(DecryptedAuthKey) && apkType_fromServer.equals(get_ambulanta_type()) )
                    {
                        this.authentification = 2;
                    }
                    else if (userid.equals(DecryptedAuthKey) && apkType_fromServer.equals(get_pacient_type()) )
                    {
                        this.authentification = 3;
                    }
                    else if (userid.equals(DecryptedAuthKey) && apkType_fromServer.equals(get_medic_type()))
                    {
                        this.authentification = 4;
                    }
                }
                else
                {
                    this.authentification = 0;
                }
                System.out.println(userid);
                System.out.println(get_medic_type());
                System.out.println(apkType_fromServer);
                System.out.println(userid.equals(DecryptedApkType));
                System.out.println(apkType_fromServer.equals(get_medic_type()));

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } finally {
                mysqlConnect.disconnect();
            }

        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
//            e1.printStackTrace();
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (Exception e) {
            System.out.println("Procesul de decriptare a cheii de autentificare a esuat.");
            e.printStackTrace();
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        }
    }

	private void apkPacient_login(PublicKey pubkey,PrivateKey privateKey) {
        int userLogat = 0;
        String checkIfRegister = null;
        while (userLogat == 0) {
            String uid = new String();
            String pass = new String();
            byte[] cryptedUidFromClient;
            byte[] cpryptedPassFromClient;
            try {
                oop.writeObject(pubkey);
                System.out.println("pubkey (apkPacient_login): " + pubkey);
                cryptedUidFromClient = (byte[]) oin.readObject();
                checkIfRegister = new String(cryptedUidFromClient, "UTF-8");
                System.out.println("DIEZ : " + checkIfRegister);
                System.out.println("NU Am primit register");
                if(checkIfRegister.equals("#"))
                {
                    System.out.println("Am primit register");
                    registerPacient(pubkey, privateKey);
                    System.out.println("AAAAAAAAAA");
                    userLogat = 0;
                    break;
                }
                cpryptedPassFromClient = (byte[]) oin.readObject();
                byte[] byte_uid = null;
                byte[] byte_pass = null;

                byte_uid = decrypt(privateKey, cryptedUidFromClient);
                byte_pass = decrypt(privateKey, cpryptedPassFromClient);
                uid = new String(byte_uid, "UTF-8");
                pass = new String(byte_pass, "UTF-8");

                System.out.println("uid : " + uid + "pass : " + pass);

            } catch (IOException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            }

            System.out.println("Am intrat aici!!!!!!!!!!");
            String sql = "SELECT uid, pass, NumePrenume, blocat,banat,avertizat,varsta,sex,boli,Adresa,nr_tel FROM pacient WHERE uid = ? and pass = ? ";
//            DatabaseConnection mysqlConnect = new DatabaseConnection();
            try {
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, uid);
                statement.setString(2, pass);
                ResultSet rs = statement.executeQuery();
                String UserFromDB = null;
                String PassFormDB = null;

                while (rs.next()) {
                    UserFromDB = rs.getString("uid");
                    PassFormDB = rs.getString("pass");
                    NumePrenume = rs.getString("NumePrenume");
                    uid = rs.getString("uid");
                    Adresa = rs.getString("Adresa");
                    blocat = rs.getBoolean("blocat");
                    banat = rs.getBoolean("banat");
                    avertizat = rs.getBoolean("avertizat");
                    varsta = rs.getInt("varsta");
                    sex = rs.getString("sex");
                    boli = rs.getString("boli");
                    numarDeTelefon = rs.getString("nr_tel");
                }
                System.out.println("UserFromDB : " + UserFromDB);
                System.out.println("PassFormDB : " + PassFormDB);
                if (UserFromDB != null && PassFormDB != null && banat == false) {
                    userLogat = 1;
                    clientsList.add(this);
                    if (avertizat == true)
                    {
                        oop.writeObject("2");
                    } else
                    {
                        oop.writeObject("1");
                    }

                    System.out.println("PassFormDB : " + PassFormDB);
                    apkPacient(pubkey, privateKey);


                } else {
                    System.out.println("PassFormDB : " + PassFormDB);
                    oop.writeObject("0");

                }
            } catch (SQLException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } finally {
                mysqlConnect.disconnect();
            }
        }
        if(userLogat == 0 && !checkIfRegister.equals("#")) {
            apkPacient_login(pubkey, privateKey);
        }
	}
    public  void registerPacient(PublicKey pubkey,PrivateKey privateKey){
        String regCloseORReg = null;
        PreparedStatement statement = null;
//        DatabaseConnection mysqlConnect = new DatabaseConnection();
        try {
            regCloseORReg = (String) oin.readObject();
            if (regCloseORReg.equals("0"))
            {
                System.out.println("Procedura de inregistrare");
                byte[] cryptedCnp = null;
                byte[] cryptedUid = null;
                byte[] cryptedPass =  null;
                byte[] cryptedName =  null;
                byte[] decryptedUid = null;
                byte[] decryptedPass =  null;
                byte[] decryptedName =  null;
                byte[] decryptedCnp = null;
                cryptedCnp = (byte[]) oin.readObject();
                decryptedCnp = decrypt(privateKey,cryptedCnp);
                String decryptedCnpStr = new String(decryptedCnp,"UTF-8");
                cryptedUid = (byte[]) oin.readObject();
                decryptedUid = decrypt(privateKey,cryptedUid);
                String decryptedUidStr = new String(decryptedUid,"UTF-8");
                System.out.println(decryptedCnpStr);
                String sql = "SELECT cnp,uid FROM pacient WHERE cnp = ? or uid = ?";

                statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, decryptedCnpStr);
                statement.setString(2, decryptedUidStr);
                ResultSet rs = statement.executeQuery();
                String CnpFromDB = null;
                String UidFromDB = null;
                while (rs.next()) {
                    CnpFromDB = rs.getString("cnp");
                    UidFromDB = rs.getString("uid");
                }
                if (CnpFromDB != null || UidFromDB != null)
                {
                    oop.writeObject("0");
                    System.out.println("CNP : " + CnpFromDB + "OR username : "+ UidFromDB + "already used! : ");
                    registerPacient(pubkey, privateKey);
                }else {
                    oop.writeObject("1");
                    cryptedPass = (byte[]) oin.readObject();
                    cryptedName = (byte[]) oin.readObject();
                    decryptedPass = decrypt(privateKey,cryptedPass);
                    decryptedName = decrypt(privateKey,cryptedName);
                    String decryptedPassStr = new String(decryptedPass,"UTF-8");
                    String decryptedNameStr = new String(decryptedName,"UTF-8");
                    System.out.println(decryptedUidStr+decryptedPassStr+decryptedNameStr);
                    String insertSql = "INSERT INTO pacient"
                            + "(cnp,uid,pass,numeprenume) VALUES"
                            + "(?,?,?,?)";
                    statement = mysqlConnect.connect().prepareStatement(insertSql);
                    statement.setString(1, decryptedCnpStr);
                    statement.setString(2, decryptedUidStr);
                    statement.setString(3, decryptedPassStr);
                    statement.setString(4, decryptedNameStr);
                    int updateSt = statement.executeUpdate();
                    if (updateSt>0) {
                        System.out.println("Record is inserted into PACIENT table!");
                        oop.writeObject("1");
                    }else
                    {
                        oop.writeObject("0");
                        System.out.println("Record was not inserted into PACIENT table!");
                    }
                }
                apkPacient_login(pubkey, privateKey);
            }
            else if (regCloseORReg.equals("1"))
            {
                System.out.println("Procedura de inchidere");
                apkPacient_login(pubkey, privateKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        }
        System.out.println("Inregistrare terminata!");


    }
    private void apkPacient(PublicKey pubkey,PrivateKey privateKey) throws Exception {
        System.out.println("Pacientul s-a logat cu succes ");
        int userlogat=1;
        byte[] cryptedPacientAction = null;
        byte[] decryptedPacientAction = null;
        String decryptedPacientActionStr = null;
        oop.writeObject(pubkey);
        cryptedPacientAction = (byte[]) oin.readObject();
        decryptedPacientAction = decrypt(privateKey, cryptedPacientAction);
        decryptedPacientActionStr = new String(decryptedPacientAction, "UTF-8");

        if (decryptedPacientActionStr.equals("lockAccount")) {
            byte[] cryptedCnpLock = null;
            byte[] cryptedUidLock = null;
            byte[] cryptedPasswordLock = null;

            byte[] decryptedCnpLock = null;
            byte[] decryptedUidLock = null;
            byte[] decryptedPasswordLock = null;

            String decryptedCnpLockStr = null;
            String decryptedUidLockStr = null;
            String decryptedPasswordLockStr = null;

            cryptedCnpLock = (byte[]) oin.readObject();
            cryptedUidLock = (byte[]) oin.readObject();
            cryptedPasswordLock = (byte[]) oin.readObject();

            decryptedCnpLock = decrypt(privateKey, cryptedCnpLock);
            decryptedUidLock = decrypt(privateKey, cryptedUidLock);
            decryptedPasswordLock = decrypt(privateKey, cryptedPasswordLock);

            decryptedCnpLockStr = new String(decryptedCnpLock, "UTF-8");
            decryptedUidLockStr = new String(decryptedUidLock, "UTF-8");
            decryptedPasswordLockStr = new String(decryptedPasswordLock, "UTF-8");

            String sql = "SELECT uid FROM pacient WHERE cnp = ? and uid = ? and pass = ? ";
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.setString(1, decryptedCnpLockStr);
            statement.setString(2, decryptedUidLockStr);
            statement.setString(3, decryptedPasswordLockStr);
            ResultSet rs = statement.executeQuery();
            String UidFromDB_lock = null;
            while (rs.next()) {
                UidFromDB_lock = rs.getString("uid");
            }
            if (UidFromDB_lock != null) {
                String sqlLock = "UPDATE pacient SET blocat = ? WHERE cnp = ? and uid = ? and pass = ? ";
                PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                statementLock.setBoolean(1, true);
                statementLock.setString(2, decryptedCnpLockStr);
                statementLock.setString(3, decryptedUidLockStr);
                statementLock.setString(4, decryptedPasswordLockStr);
                statementLock.executeUpdate();

                System.out.println("Pacientul a fost blocat!");
                oop.writeObject("1");
            } else {
                System.out.println("Pacientul NU a fost blocat!");
                oop.writeObject("0");
            }
        } else if (decryptedPacientActionStr.equals("changePass")) {
            byte[] cryptedCnpChPass = null;
            byte[] cryptedUidChPass = null;
            byte[] cryptedPasswordOldChPass = null;
            byte[] cryptedPasswordNewChPass = null;

            byte[] decryptedCnpChPass = null;
            byte[] decryptedUidChPass = null;
            byte[] decryptedPasswordOldChPass = null;
            byte[] decryptedPasswordNewChPass = null;

            String decryptedCnpChPassStr = null;
            String decryptedUidChPassStr = null;
            String decryptedPasswordOldChPassStr = null;
            String decryptedPasswordNewChPassStr = null;

            cryptedCnpChPass = (byte[]) oin.readObject();
            cryptedUidChPass = (byte[]) oin.readObject();
            cryptedPasswordOldChPass = (byte[]) oin.readObject();
            cryptedPasswordNewChPass = (byte[]) oin.readObject();

            decryptedCnpChPass = decrypt(privateKey, cryptedCnpChPass);
            decryptedUidChPass = decrypt(privateKey, cryptedUidChPass);
            decryptedPasswordOldChPass = decrypt(privateKey, cryptedPasswordOldChPass);
            decryptedPasswordNewChPass = decrypt(privateKey, cryptedPasswordNewChPass);

            decryptedCnpChPassStr = new String(decryptedCnpChPass, "UTF-8");
            decryptedUidChPassStr = new String(decryptedUidChPass, "UTF-8");
            decryptedPasswordOldChPassStr = new String(decryptedPasswordOldChPass, "UTF-8");
            decryptedPasswordNewChPassStr = new String(decryptedPasswordNewChPass, "UTF-8");

            String sql = "SELECT uid FROM pacient WHERE cnp = ? and uid = ? and pass = ? ";
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.setString(1, decryptedCnpChPassStr);
            statement.setString(2, decryptedUidChPassStr);
            statement.setString(3, decryptedPasswordOldChPassStr);
            ResultSet rs = statement.executeQuery();
            String UidFromDB_lock = null;
            while (rs.next()) {
                UidFromDB_lock = rs.getString("uid");
            }
            if (UidFromDB_lock != null) {
                String sqlLock = "UPDATE pacient SET pass = ? WHERE cnp = ? and uid = ? and pass = ? ";
                PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                statementLock.setString(1, decryptedPasswordNewChPassStr);
                statementLock.setString(2, decryptedCnpChPassStr);
                statementLock.setString(3, decryptedUidChPassStr);
                statementLock.setString(4, decryptedPasswordOldChPassStr);
                statementLock.executeUpdate();

                System.out.println("Parola a fost schimbata!");
                oop.writeObject("1");
            } else {
                System.out.println("Parola NU a fost schimbata!");
                oop.writeObject("0");
            }
        } else if (decryptedPacientActionStr.equals("SOS")) {

            pacientLocationBytes = null;
            pacientLocationBytes = (byte[]) oin.readObject();
            System.out.println(pacientLocationBytes);
            double latPac = (double) oin.readObject();
            double lonPac = (double) oin.readObject();
            System.out.println(Server.clientsList.size());

            boolean preluat = false;

            for (Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
                Client Pacient = entry.getKey();
                Client Ambulanta = entry.getValue();
                if (this.equals(Pacient)) {
                    preluat = true;
                    break;
                }
            }

            if (preluat == false) {

                double min = 0;
                for (Client auxCli : Server.clientsList) {
                    if (auxCli.clientType.equals("Ambulanta") && auxCli.latAmb != 0 && auxCli.lonAmb != 0) {
                        double dist = auxCli.DistantaDeParcursInPlus + distance(auxCli.latAmb, auxCli.lonAmb, latPac, lonPac);
                        if (min == 0) {
                            min = dist;
                        } else if (min != 0 && dist < min) {
                            min = dist;
                        }
                    }
                }
                oop.writeObject(min);
                for (Client auxCli : Server.clientsList) {
                    if (auxCli.clientType.equals("Ambulanta")) {
                        double dist = auxCli.DistantaDeParcursInPlus + distance(auxCli.latAmb, auxCli.lonAmb, latPac, lonPac);
                        if (dist == min) {

                            if (auxCli.ambulantaLibera == true) {
                                System.out.println("Am gasit o ambulanta");

                                auxCli.AmbulantaDisponibila = false;
                                urgentePreluate.put(this, auxCli);
                                auxCli.oop.writeObject("SOS");
                                auxCli.oop.writeObject(pacientLocationBytes);
                                System.out.println("lat amb = " + auxCli.latAmb + "lon amb = " + auxCli.lonAmb);
                                auxCli.AmbulantaDisponibila = true;
                                auxCli.lock = true;
                                System.out.println(pacientLocationBytes);
                                pacientPreluatInstant = true;
                                break;
                            } else if (auxCli.ambulantaLibera == false) {
                                urgenteInAsteptare.put(this, auxCli);
                                pacientPreluatInstant = false;
                                System.out.println("Am pus urgenta in asteptare.");

                                break;
                            }

                        }
                    } else {
                        System.out.println("NU am gasit o ambulanta");
                    }
                }


                int sosSend = 1;
                if (sosSend == 1) {
                    System.out.println("SOS trimis");
                    oop.writeObject("1");
                } else {
                    System.out.println("SOS netrimis");
                    oop.writeObject("0");
                }
            }
            else
            {
                System.out.println("Pacient preluat deja.");
                oop.writeObject("0");
            }
        }else if (decryptedPacientActionStr.equals("SOSinfoSend")) {


            SosInfoStr = (StringBuffer) oin.readObject();

            System.out.println(Server.urgentePreluate.size());

            for(Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
                Client Pacient = entry.getKey();
                Client Ambulanta = entry.getValue();

                if (Pacient.equals(this))
                {
                    if (Pacient.pacientPreluatInstant == true)
                    {
                        System.out.println("Am gasit ambulanta asignata ");

                        Ambulanta.AmbulantaDisponibilaSOS = false;
                        Ambulanta.oop.writeObject(pubkey);
                        Ambulanta.oop.writeObject(SosInfoStr);
//                    BufferSOSinfoPacinet.setLength(0);
//                    BufferSOSinfoPacinet.append(SosInfoStr);
                        System.out.println(SosInfoStr);
                        Ambulanta.AmbulantaDisponibilaSOS = true;
                        Ambulanta.lock = false;
                        Pacient.InfoBufferTrimis = false;
                        break;
                    }
                    else
                    {
                        Ambulanta.lock = false;
                        Pacient.InfoBufferTrimis = true;
                    }
                }
                else
                {
                    System.out.println("NU am gasit ambulanta asignata");
                }

            }
            int sosSend = 1;
            if ( sosSend == 1) {
                System.out.println("SOS INFO trimis");
                oop.writeObject("1");
            } else {
                System.out.println("SOS INFO netrimis");
                oop.writeObject("0");
            }
        }else if (decryptedPacientActionStr.equals("changeAddress")) {
            byte[] cryptedCnpChAddr = null;
            byte[] cryptedUidChAddr = null;
            byte[] cryptedPasswordChAddr = null;
            byte[] cryptedAddrChAddr = null;

            byte[] decryptedCnpChAddr = null;
            byte[] decryptedUidChAddr = null;
            byte[] decryptedPasswordChAddr = null;
            byte[] decryptedAddrChAddr = null;

            String decryptedCnpChAddrStr = null;
            String decryptedUidChAddrStr = null;
            String decryptedPasswordChAddrStr = null;
            String decryptedAddrChAddrStr = null;

            cryptedCnpChAddr = (byte[]) oin.readObject();
            cryptedUidChAddr = (byte[]) oin.readObject();
            cryptedPasswordChAddr = (byte[]) oin.readObject();
            cryptedAddrChAddr = (byte[]) oin.readObject();

            decryptedCnpChAddr = decrypt(privateKey, cryptedCnpChAddr);
            decryptedUidChAddr = decrypt(privateKey, cryptedUidChAddr);
            decryptedPasswordChAddr = decrypt(privateKey, cryptedPasswordChAddr);
            decryptedAddrChAddr = decrypt(privateKey, cryptedAddrChAddr);

            decryptedCnpChAddrStr = new String(decryptedCnpChAddr, "UTF-8");
            decryptedUidChAddrStr = new String(decryptedUidChAddr, "UTF-8");
            decryptedPasswordChAddrStr = new String(decryptedPasswordChAddr, "UTF-8");
            decryptedAddrChAddrStr = new String(decryptedAddrChAddr, "UTF-8");

            String sql = "SELECT uid FROM pacient WHERE cnp = ? and uid = ? and pass = ? ";
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.setString(1, decryptedCnpChAddrStr);
            statement.setString(2, decryptedUidChAddrStr);
            statement.setString(3, decryptedPasswordChAddrStr);
            ResultSet rs = statement.executeQuery();
            String UidFromDB_lock = null;
            while (rs.next()) {
                UidFromDB_lock = rs.getString("uid");
            }
            if (UidFromDB_lock != null) {
                String sqlLock = "UPDATE pacient SET Adresa = ? WHERE cnp = ? and uid = ? and pass = ? ";
                PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                statementLock.setString(1, decryptedAddrChAddrStr);
                statementLock.setString(2, decryptedCnpChAddrStr);
                statementLock.setString(3, decryptedUidChAddrStr);
                statementLock.setString(4, decryptedPasswordChAddrStr);
                statementLock.executeUpdate();

                System.out.println("Adresa a fost schimbata!");
                oop.writeObject("1");
            } else {
                System.out.println("Adresa NU a fost schimbata!");
                oop.writeObject("0");
            }
        } else if (decryptedPacientActionStr.equals("changeMedicalHistory")) {
            byte[] cryptedCnpChMed = null;
            byte[] cryptedUidChMed = null;
            byte[] cryptedPasswordChMed = null;
            byte[] cryptedMedChMed = null;

            byte[] decryptedCnpChMed = null;
            byte[] decryptedUidChMed = null;
            byte[] decryptedPasswordChMed = null;
            byte[] decryptedMedChMed = null;

            String decryptedCnpChMedStr = null;
            String decryptedUidChMedStr = null;
            String decryptedPasswordChMedStr = null;
            String decryptedMedChMedStr = null;

            cryptedCnpChMed = (byte[]) oin.readObject();
            cryptedUidChMed = (byte[]) oin.readObject();
            cryptedPasswordChMed = (byte[]) oin.readObject();
            cryptedMedChMed = (byte[]) oin.readObject();

            decryptedCnpChMed = decrypt(privateKey, cryptedCnpChMed);
            decryptedUidChMed = decrypt(privateKey, cryptedUidChMed);
            decryptedPasswordChMed = decrypt(privateKey, cryptedPasswordChMed);
            decryptedMedChMed = decrypt(privateKey, cryptedMedChMed);

            decryptedCnpChMedStr = new String(decryptedCnpChMed, "UTF-8");
            decryptedUidChMedStr = new String(decryptedUidChMed, "UTF-8");
            decryptedPasswordChMedStr = new String(decryptedPasswordChMed, "UTF-8");
            decryptedMedChMedStr = new String(decryptedMedChMed, "UTF-8");

            String sql = "SELECT uid FROM pacient WHERE cnp = ? and uid = ? and pass = ? ";
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.setString(1, decryptedCnpChMedStr);
            statement.setString(2, decryptedUidChMedStr);
            statement.setString(3, decryptedPasswordChMedStr);
            ResultSet rs = statement.executeQuery();
            String UidFromDB_lock = null;
            while (rs.next()) {
                UidFromDB_lock = rs.getString("uid");
            }
            if (UidFromDB_lock != null) {
                String sqlLock = "UPDATE pacient SET boli = ? WHERE cnp = ? and uid = ? and pass = ? ";
                PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                statementLock.setString(1, decryptedMedChMedStr);
                statementLock.setString(2, decryptedCnpChMedStr);
                statementLock.setString(3, decryptedUidChMedStr);
                statementLock.setString(4, decryptedPasswordChMedStr);
                statementLock.executeUpdate();

                System.out.println("Istoricul medical a fost schimbat!");
                oop.writeObject("1");
            } else {
                System.out.println("Istoricul medical NU a fost schimbat!");
                oop.writeObject("0");
            }
        } else if (decryptedPacientActionStr.equals("Logout")) {

            System.out.println("Delogare cu succes!");
            userlogat = 0;
            oop.writeObject("1");
        }
        if (userlogat == 1)
        {
            apkPacient(pubkey, privateKey);
        }
        else if (userlogat == 0)
        {
            apkPacient_login(pubkey, privateKey);
        }
    }


	private void apkAmbulanta_login(PublicKey pubkey,PrivateKey privateKey) {
        int userLogat = 0;
        String checkIfRegister = null;
        while (userLogat == 0) {
            String uid = new String();
            String pass = new String();
            byte[] cryptedUidFromClient;
            byte[] cpryptedPassFromClient;
            try {
                oop.writeObject(pubkey);
                System.out.println("pubkey (apkAmbulanta_login): " + pubkey);
                cryptedUidFromClient = (byte[]) oin.readObject();
                checkIfRegister = new String(cryptedUidFromClient, "UTF-8");
                System.out.println("DIEZ : " + checkIfRegister);
                System.out.println("NU Am primit register");
                if(checkIfRegister.equals("#"))
                {
                    System.out.println("Am primit register");
                    registerAmbulanta(pubkey, privateKey);
                    System.out.println("AAAAAAAAAA");
                    userLogat = 0;
                    break;
                }
                cpryptedPassFromClient = (byte[]) oin.readObject();
                byte[] byte_uid = null;
                byte[] byte_pass = null;

                byte_uid = decrypt(privateKey, cryptedUidFromClient);
                byte_pass = decrypt(privateKey, cpryptedPassFromClient);
                uid = new String(byte_uid, "UTF-8");
                pass = new String(byte_pass, "UTF-8");

                System.out.println("uid : " + uid + "pass : " + pass);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            }

            System.out.println("Am intrat aici!!!!!!!!!!");
            String sql = "SELECT uid, pass, NrInmatriculare, Sofer, Medic FROM ambulanta WHERE uid = ? and pass = ? ";
//            DatabaseConnection mysqlConnect = new DatabaseConnection();
            try {
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, uid);
                statement.setString(2, pass);
                ResultSet rs = statement.executeQuery();
                String UserFromDB = null;
                String PassFormDB = null;
                while (rs.next()) {
                    UserFromDB = rs.getString("uid");
                    PassFormDB = rs.getString("pass");
                    numarInmatriculare = rs.getString("NrInmatriculare");
                    Sofer = rs.getString("Sofer");
                    Medic = rs.getString("Medic");
//                    System.out.println(numarInmatriculare + Sofer + Medic);
                }
                System.out.println("UserFromDB :  " + UserFromDB);
                System.out.println("PassFormDB :  " + PassFormDB);
                if (UserFromDB != null && PassFormDB != null) {
                    userLogat = 1;
                    clientsList.add(this);
                    oop.writeObject("1");
                    System.out.println("PassFormDB : " + PassFormDB);
                    apkAmbulanta(pubkey, privateKey);

                } else {
                    oop.writeObject("0");
                    System.out.println("PassFormDB : " + PassFormDB);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } finally {
                mysqlConnect.disconnect();
            }
        }
        if(userLogat == 0 && !checkIfRegister.equals("#")) {
            apkAmbulanta_login(pubkey, privateKey);
        }
		
	}
    public  void registerAmbulanta(PublicKey pubkey,PrivateKey privateKey){
        String regCloseORReg = null;
        PreparedStatement statement = null;
//        DatabaseConnection mysqlConnect = new DatabaseConnection();
        try {
            regCloseORReg = (String) oin.readObject();
            if (regCloseORReg.equals("0"))
            {
                System.out.println("Procedura de inregistrare");
                byte[] cryptedCnp = null;
                byte[] cryptedUid = null;
                byte[] cryptedPass =  null;
                byte[] cryptedName =  null;
                byte[] decryptedUid = null;
                byte[] decryptedPass =  null;
                byte[] decryptedName =  null;
                byte[] decryptedCnp = null;
                cryptedCnp = (byte[]) oin.readObject();
                decryptedCnp = decrypt(privateKey,cryptedCnp);

                String decryptedCnpStr = new String(decryptedCnp,"UTF-8");
                System.out.println(decryptedCnpStr);
                String sql = "SELECT cnp FROM ambulanta WHERE cnp = ?";

                statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, decryptedCnpStr);
                ResultSet rs = statement.executeQuery();
                String CnpFromDB = null;
                while (rs.next()) {
                    CnpFromDB = rs.getString("cnp");
                }
                if (CnpFromDB != null)
                {
                    oop.writeObject("0");
                    System.out.println("CNP already used! : " + CnpFromDB);
                    registerAmbulanta(pubkey, privateKey);
                }else {

                    oop.writeObject("1");

                    cryptedUid = (byte[]) oin.readObject();
                    System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMM");
                    cryptedPass = (byte[]) oin.readObject();
                    cryptedName = (byte[]) oin.readObject();
                    decryptedUid = decrypt(privateKey,cryptedUid);
                    decryptedPass = decrypt(privateKey,cryptedPass);
                    decryptedName = decrypt(privateKey,cryptedName);
                    String decryptedUidStr = new String(decryptedUid,"UTF-8");
                    String decryptedPassStr = new String(decryptedPass,"UTF-8");
                    String decryptedNameStr = new String(decryptedName,"UTF-8");
                    System.out.println(decryptedUidStr+decryptedPassStr+decryptedNameStr);
                    String insertSql = "INSERT INTO ambulanta"
                            + "(cnp,uid,pass,numeprenume) VALUES"
                            + "(?,?,?,?)";
                    statement = mysqlConnect.connect().prepareStatement(insertSql);
                    statement.setString(1, decryptedCnpStr);
                    statement.setString(2, decryptedUidStr);
                    statement.setString(3, decryptedPassStr);
                    statement.setString(4, decryptedNameStr);
                    int updateSt = statement.executeUpdate();
                    if (updateSt>0) {
                        System.out.println("Record is inserted into DISPECER table!");
                        oop.writeObject("1");
                    }else
                    {
                        oop.writeObject("0");
                        System.out.println("Record was not inserted into DISPECER table!");
                    }
                }
                apkAmbulanta_login(pubkey, privateKey);
            }
            else if (regCloseORReg.equals("1"))
            {
                System.out.println("Procedura de inchidere");
                apkAmbulanta_login(pubkey, privateKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        }
        System.out.println("Inregistrare terminata!");


    }
	private void apkAmbulanta(PublicKey pubkey,PrivateKey privateKey) throws Exception {
	    System.out.println("Clientul s-a logat cu succes ");
        int ambulantaLogata=1;
        byte[] cryptedAmbulantaAction = null;
        byte[] decryptedAmbulantaAction = null;
        String decryptedAmbulantaActionStr = null;
        if(cnt == 0)
        {
            oop.writeObject(pubkey);
            cnt=1;
        }
        System.out.println("cccccccccccccccccccccccccccc");
        while(this.lock == true){
            System.out.println("Lock");
        }

//        for(Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
//            Client Pacient = entry.getKey();
//            Client Ambulanta = entry.getValue();
//
//            if (Ambulanta.equals(this)) {
////                if (Pacient.pacientPreluatInstant == false && Pacient.InfoBufferTrimis == true) {
//                    Ambulanta.oop.writeObject(pubkey);
//                    Ambulanta.oop.writeObject(Pacient.SosInfoStr);
//                    System.out.println(Pacient.SosInfoStr);
////                    break;
////                }
//            }
//        }

        cryptedAmbulantaAction = (byte[]) oin.readObject();
        System.out.println(cryptedAmbulantaAction);
        decryptedAmbulantaAction = decrypt(privateKey, cryptedAmbulantaAction);
        System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        decryptedAmbulantaActionStr = new String(decryptedAmbulantaAction, "UTF-8");
        if (decryptedAmbulantaActionStr.equals("Logout")) {
            System.out.println("Delogare cu succes!");
            ambulantaLogata = 0;
            ambulantaLibera = true;
            oop.writeObject("1");
        }else if (decryptedAmbulantaActionStr.equals("SOSprimit")) {

            this.AmbulantaDisponibila = false;
            for (Map.Entry<Client, Client> entry : urgenteInAsteptare.entrySet()) {
                Client Pacient = entry.getKey();
                Client Ambulanta = entry.getValue();

                if (this.equals(Ambulanta)) {
                    this.AmbulantaDisponibila = false;
                    urgentePreluate.put(Pacient, this);
                    this.oop.writeObject("SOS");
                    this.oop.writeObject(Pacient.pacientLocationBytes);
                    System.out.println("lat amb = " + this.latAmb + "lon amb = " + this.lonAmb);
                    this.AmbulantaDisponibila = true;
                    this.lock = false;
//                    urgentaAsignata = true;
                    System.out.println(Pacient.pacientLocationBytes);
                    urgenteInAsteptare.remove(Pacient,Ambulanta);
//                    if (pacientPreluatInstant == false)
//                    {
////                        System.out.println("Am gasit ambulanta asignata ");
//
//                        Ambulanta.AmbulantaDisponibilaSOS = false;
//                        Ambulanta.oop.writeObject(pubkey);
//                        Ambulanta.oop.writeObject(Ambulanta.SosInfoStr);
////                    BufferSOSinfoPacinet.setLength(0);
////                    BufferSOSinfoPacinet.append(SosInfoStr);
//                        System.out.println(Ambulanta.SosInfoStr);
//                        Ambulanta.AmbulantaDisponibilaSOS = true;
//                        Ambulanta.lock = false;
//
//                    }
                    break;
                }
            }
            this.latAmb = (double) oin.readObject();
            this.lonAmb = (double) oin.readObject();
            while(this.AmbulantaDisponibila == false){
                System.out.println("AStept");
            }
            System.out.println("rrrrrr");
            ambulantaLibera = false;



        }else if (decryptedAmbulantaActionStr.equals("Simptome")) {
            ambulantaLibera = true;
            double  auxDistanta = (double) oin.readObject();

            byte[] cryptedSosinfoAmbulanta = null;
            byte[] decryptedSosinfoAmbulanta = null;
            String decryptedSosinfoAmbulantaStr = null;


            cryptedSosinfoAmbulanta = (byte[]) oin.readObject();
            decryptedSosinfoAmbulanta = decrypt(privateKey, cryptedSosinfoAmbulanta);
            decryptedSosinfoAmbulantaStr = new String(decryptedSosinfoAmbulanta, "UTF-8");
            System.out.println("SosinfoAmbulanta :" + decryptedSosinfoAmbulantaStr);
            SosInfoStrAmbulanta.setLength(0);
            SosInfoStrAmbulanta.append(decryptedSosinfoAmbulantaStr);
            DistantaDeParcursInPlus =  auxDistanta * 2 ;
            System.out.println("Distanta ambulanta :" + auxDistanta);
            cnt = 0;
        } else if (decryptedAmbulantaActionStr.equals("Ocupat")) {
            AmbulantaDisponibila = false;
            ambulantaLibera = false;
        }
        else if (decryptedAmbulantaActionStr.equals("Disponibil")) {
            AmbulantaDisponibila = true;
            ambulantaLibera = true;
        }
        else if (decryptedAmbulantaActionStr.equals("Anulare")) {
            AmbulantaDisponibila = true;
//            Client pac = urgentePreluate.get(this);
            String  detaliiCancel = (String) oin.readObject();

            for(Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
                Client Pacient = entry.getKey();
                Client Ambulanta = entry.getValue();
                if(this.equals(Ambulanta))
                {
                    mesajeAmbulanta.put(this,"Urgenta anulata (Paceint : "+ Pacient.NumePrenume + " - UID : " + Pacient.uid  + " - Detalii : " + detaliiCancel + ")");
                    urgentePreluate.remove(this);
                    break;
                }
            }


            ambulantaLibera = true;
        }
        else if (decryptedAmbulantaActionStr.equals("CerinteDispecer")) {
            ambulantaLibera = true;
            byte[] cryptedMsg = null;
            byte[] decryptedMsg = null;
            String decryptedMsgStr = null;
            cryptedMsg = (byte[]) oin.readObject();
            decryptedMsg = decrypt(privateKey, cryptedMsg);
            decryptedMsgStr = new String(decryptedMsg, "UTF-8");
            mesajeAmbulanta.put(this,decryptedMsgStr);
            oop.writeObject("1");
        }
        if (ambulantaLogata == 1)
        {
            System.out.println("ssssssssssssssssssssssssssssssssssss");
            apkAmbulanta(pubkey, privateKey);
        }
        else if (ambulantaLogata == 0)
        {
            apkAmbulanta_login(pubkey, privateKey);
        }
    }

    private void apkMedic_login(PublicKey pubkey,PrivateKey privateKey) {
        int userLogat = 0;
        String checkIfRegister = null;
        while (userLogat == 0) {
            String uid = new String();
            String pass = new String();
            byte[] cryptedUidFromClient;
            byte[] cpryptedPassFromClient;
            try {
                oop.writeObject(pubkey);
                System.out.println("pubkey (apkMedic_login): " + pubkey);
                cryptedUidFromClient = (byte[]) oin.readObject();
                checkIfRegister = new String(cryptedUidFromClient, "UTF-8");
                System.out.println("DIEZ : " + checkIfRegister);
                System.out.println("NU Am primit register");
                if(checkIfRegister.equals("#"))
                {
                    System.out.println("Am primit register");
//                    registerMedic(pubkey, privateKey);
                    System.out.println("AAAAAAAAAA");
                    userLogat = 0;
                    break;
                }
                cpryptedPassFromClient = (byte[]) oin.readObject();
                byte[] byte_uid = null;
                byte[] byte_pass = null;

                byte_uid = decrypt(privateKey, cryptedUidFromClient);
                byte_pass = decrypt(privateKey, cpryptedPassFromClient);
                uid = new String(byte_uid, "UTF-8");
                pass = new String(byte_pass, "UTF-8");

                System.out.println("uid : " + uid + "pass : " + pass);

            } catch (IOException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            }

            System.out.println("Am intrat aici!!!!!!!!!!");
            String sql = "SELECT uid, pass, NumePrenume,nr_tel FROM medic WHERE uid = ? and pass = ? ";
//            DatabaseConnection mysqlConnect = new DatabaseConnection();
            try {
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, uid);
                statement.setString(2, pass);
                ResultSet rs = statement.executeQuery();
                String UserFromDB = null;
                String PassFormDB = null;

                while (rs.next()) {
                    UserFromDB = rs.getString("uid");
                    PassFormDB = rs.getString("pass");
                    NumePrenumeMed = rs.getString("NumePrenume");
                    numarDeTelefonMed = rs.getString("nr_tel");
                }
                System.out.println("UserFromDB : " + UserFromDB);
                System.out.println("PassFormDB : " + PassFormDB);
                if (UserFromDB != null && PassFormDB != null) {
                    userLogat = 1;
                    clientsList.add(this);
                    oop.writeObject("1");
                    System.out.println("PassFormDB : " + PassFormDB);
                    apkMedic(pubkey, privateKey);


                } else {
                    System.out.println("PassFormDB : " + PassFormDB);
                    oop.writeObject("0");

                }
            } catch (SQLException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } finally {
                mysqlConnect.disconnect();
            }
        }
        if(userLogat == 0 && !checkIfRegister.equals("#")) {
            apkMedic_login(pubkey, privateKey);
        }
    }
    public  void registerMedic(PublicKey pubkey,PrivateKey privateKey){
        String regCloseORReg = null;
        PreparedStatement statement = null;
//        DatabaseConnection mysqlConnect = new DatabaseConnection();
        try {
            regCloseORReg = (String) oin.readObject();
            if (regCloseORReg.equals("0"))
            {
                System.out.println("Procedura de inregistrare");
                byte[] cryptedCnp = null;
                byte[] cryptedUid = null;
                byte[] cryptedPass =  null;
                byte[] cryptedName =  null;
                byte[] decryptedUid = null;
                byte[] decryptedPass =  null;
                byte[] decryptedName =  null;
                byte[] decryptedCnp = null;
                cryptedCnp = (byte[]) oin.readObject();
                decryptedCnp = decrypt(privateKey,cryptedCnp);
                String decryptedCnpStr = new String(decryptedCnp,"UTF-8");
                cryptedUid = (byte[]) oin.readObject();
                decryptedUid = decrypt(privateKey,cryptedUid);
                String decryptedUidStr = new String(decryptedUid,"UTF-8");
                System.out.println(decryptedCnpStr);
                String sql = "SELECT cnp,uid FROM medic WHERE cnp = ? or uid = ?";

                statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, decryptedCnpStr);
                statement.setString(2, decryptedUidStr);
                ResultSet rs = statement.executeQuery();
                String CnpFromDB = null;
                String UidFromDB = null;
                while (rs.next()) {
                    CnpFromDB = rs.getString("cnp");
                    UidFromDB = rs.getString("uid");
                }
                if (CnpFromDB != null || UidFromDB != null)
                {
                    oop.writeObject("0");
                    System.out.println("CNP : " + CnpFromDB + "OR username : "+ UidFromDB + "already used! : ");
                    registerMedic(pubkey, privateKey);
                }else {
                    oop.writeObject("1");
                    cryptedPass = (byte[]) oin.readObject();
                    cryptedName = (byte[]) oin.readObject();
                    decryptedPass = decrypt(privateKey,cryptedPass);
                    decryptedName = decrypt(privateKey,cryptedName);
                    String decryptedPassStr = new String(decryptedPass,"UTF-8");
                    String decryptedNameStr = new String(decryptedName,"UTF-8");
                    System.out.println(decryptedUidStr+decryptedPassStr+decryptedNameStr);
                    String insertSql = "INSERT INTO medic"
                            + "(cnp,uid,pass,numeprenume) VALUES"
                            + "(?,?,?,?)";
                    statement = mysqlConnect.connect().prepareStatement(insertSql);
                    statement.setString(1, decryptedCnpStr);
                    statement.setString(2, decryptedUidStr);
                    statement.setString(3, decryptedPassStr);
                    statement.setString(4, decryptedNameStr);
                    int updateSt = statement.executeUpdate();
                    if (updateSt>0) {
                        System.out.println("Record is inserted into PACIENT table!");
                        oop.writeObject("1");
                    }else
                    {
                        oop.writeObject("0");
                        System.out.println("Record was not inserted into PACIENT table!");
                    }
                }
                apkMedic_login(pubkey, privateKey);
            }
            else if (regCloseORReg.equals("1"))
            {
                System.out.println("Procedura de inchidere");
                apkMedic_login(pubkey, privateKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        }
        System.out.println("Inregistrare terminata!");


    }
    private void apkMedic(PublicKey pubkey,PrivateKey privateKey) throws Exception {
//        System.out.println("Pacientul s-a logat cu succes ");
        int userlogat=1;
        while (userlogat == 1) {
            byte[] cryptedPacientAction = null;
            byte[] decryptedPacientAction = null;
            String decryptedPacientActionStr = null;
            StringBuilder UrgenteM = new StringBuilder();
            oop.writeObject(pubkey);
            cryptedPacientAction = (byte[]) oin.readObject();
            decryptedPacientAction = decrypt(privateKey, cryptedPacientAction);
            decryptedPacientActionStr = new String(decryptedPacientAction, "UTF-8");
            if (decryptedPacientActionStr.equals("Logout")) {

                System.out.println("Delogare cu succes!");
                userlogat = 0;
                oop.writeObject("1");
            }
            else if (decryptedPacientActionStr.equals("CerinteDispecer")) {
                byte[] cryptedMsg = null;
                byte[] decryptedMsg = null;
                String decryptedMsgStr = null;
                cryptedMsg = (byte[]) oin.readObject();
                decryptedMsg = decrypt(privateKey, cryptedMsg);
                decryptedMsgStr = new String(decryptedMsg, "UTF-8");
                mesajeMedic.put(this,decryptedMsgStr);
                oop.writeObject("1");
            }
            else if (decryptedPacientActionStr.equals("MoniotrURG")) {
                if (urgentePreluate.isEmpty()) {
                    UrgenteM.append("----------");
                } else {
                    for (Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
                        Client Pacient = entry.getKey();
                        Client Ambulanta = entry.getValue();
                        if (Pacient.NumePrenume == null) {
                            UrgenteM.append("Pacient : ");
                            UrgenteM.append("NumeNecunoscut");
                            UrgenteM.append("\n");
                        } else {
                            UrgenteM.append("Pacient : ");
                            UrgenteM.append(Pacient.NumePrenume);
                            UrgenteM.append("\n");
                        }
                        if (Ambulanta.numarInmatriculare == null) {
                            UrgenteM.append(" - Ambulanta : ");
                            UrgenteM.append(" -- ");
                            UrgenteM.append("\n");
                        } else {
                            UrgenteM.append(" - Ambulanta : ");
                            UrgenteM.append(Ambulanta.numarInmatriculare);
                            UrgenteM.append("\n");
                        }
                        if (Ambulanta.Medic == null) {
                            UrgenteM.append(" - Medic : ");
                            UrgenteM.append(" -- ");
                            UrgenteM.append("\n");
                        } else {
                            UrgenteM.append(" - Medic : ");
                            UrgenteM.append(Ambulanta.Medic);
                            UrgenteM.append("\n");
                        }
                        if (Pacient.SosInfoStr == null) {
                            UrgenteM.append("#");
                            UrgenteM.append(" -- ");
                            UrgenteM.append("\n");
                        } else {
                            UrgenteM.append("#");
                            UrgenteM.append(Pacient.SosInfoStr);
                            UrgenteM.append("\n");
                        }
                        if (Ambulanta.SosInfoStrAmbulanta == null) {
                            UrgenteM.append(" -- ");
                            UrgenteM.append("\n");
                        } else {
                            UrgenteM.append(Ambulanta.SosInfoStrAmbulanta);
                            UrgenteM.append("\n");
                        }
                        UrgenteM.append(";");

                    }
                }
//                System.out.println(UrgenteM);
                oop.writeObject(UrgenteM);
                oop.writeObject("1");
                Thread.sleep(3000);
            }
        }
        if (userlogat == 0)
        {
            apkMedic_login(pubkey, privateKey);
        }
    }

	private void apkDispecer_login(PublicKey pubkey,PrivateKey privateKey) {
        int userLogat = 0;
        String checkIfRegister = null;
        while (userLogat == 0) {

            String uid = new String();
            String pass = new String();
            byte[] cryptedUidFromClient;
            byte[] cpryptedPassFromClient;
            try {

                oop.writeObject(pubkey);
                System.out.println("pubkey (apkDispecer_login): " + pubkey);
                cryptedUidFromClient = (byte[]) oin.readObject();
                checkIfRegister = new String(cryptedUidFromClient, "UTF-8");
                if(checkIfRegister.equals("#"))
                {
					registerDispecer(pubkey, privateKey);
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    userLogat = 0;
                    break;
                }
                cpryptedPassFromClient = (byte[]) oin.readObject();
                byte[] byte_uid = null;
                byte[] byte_pass = null;

                byte_uid = decrypt(privateKey, cryptedUidFromClient);
                byte_pass = decrypt(privateKey, cpryptedPassFromClient);
                uid = new String(byte_uid, "UTF-8");
                pass = new String(byte_pass, "UTF-8");

                System.out.println("uid : " + uid + "pass : " + pass);

            } catch (IOException e) {
                e.printStackTrace();
//                closeConnection();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
//                closeConnection();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
//                closeConnection();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            }
            // aici primesc
            System.out.println("Am intrat aici!!!!!!!!!!");
            String sql = "SELECT uid, pass FROM dispecer WHERE uid = ? and pass = ? ";
//            DatabaseConnection mysqlConnect = new DatabaseConnection();
            try {
                PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, uid);
                statement.setString(2, pass);
                ResultSet rs = statement.executeQuery();
                String UserFromDB = null;
                String PassFormDB = null;
                while (rs.next()) {
                    UserFromDB = rs.getString("uid");
                    dispecerUid = UserFromDB;
                    PassFormDB = rs.getString("pass");
                }
                System.out.println("UserFromDB : " + UserFromDB);
                System.out.println("PassFormDB : " + PassFormDB);
                if (UserFromDB != null && PassFormDB != null) {
                    userLogat = 1;
                    dispecerLogat = 1;
//                    apkDispecer();
                    //trimitem user logged = 1
                    oop.writeObject("1");
                    apkDispecer(pubkey,privateKey);
                    System.out.println("PassFormDB : " + PassFormDB);

                } else {
                    oop.writeObject("0");
                    System.out.println("PassFormDB : " + PassFormDB);
                }
            } catch (SQLException e) {
                e.printStackTrace();
//                closeConnection();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
//                closeConnection();
                clientsList.remove(this);
                System.out.println("Conexiunea cu clientul a fost pierduta!");
                return;
            } finally {
                mysqlConnect.disconnect();
//                closeConnection();
            }
        }

        if(userLogat == 0 && !checkIfRegister.equals("#")) {
			apkDispecer_login(pubkey, privateKey);
		}
    }
    public  void registerDispecer(PublicKey pubkey,PrivateKey privateKey){
//        String regCloseORReg = null;
        PreparedStatement statement = null;

//        DatabaseConnection mysqlConnect = new DatabaseConnection();
        try {
            if (aux == 1) {
                regCloseORReg = (String) oin.readObject();
            }
            if (regCloseORReg.equals("0"))
            {
                System.out.println("Procedura de inregistrare");
                byte[] cryptedCnp = null;
                byte[] cryptedUid = null;
                byte[] cryptedPass =  null;
                byte[] cryptedName =  null;
                byte[] decryptedUid = null;
                byte[] decryptedPass =  null;
                byte[] decryptedName =  null;
                byte[] decryptedCnp = null;
                cryptedCnp = (byte[]) oin.readObject();
                decryptedCnp = decrypt(privateKey,cryptedCnp);
                cryptedUid = (byte[]) oin.readObject();
                decryptedUid = decrypt(privateKey,cryptedUid);


                String decryptedCnpStr = new String(decryptedCnp,"UTF-8");
                String decryptedUidStr = new String(decryptedUid,"UTF-8");
                System.out.println(decryptedCnpStr);
                String sql = "SELECT cnp,uid FROM dispecer WHERE cnp = ? or uid = ?";

                statement = mysqlConnect.connect().prepareStatement(sql);
                statement.setString(1, decryptedCnpStr);
                statement.setString(2, decryptedUidStr);
                ResultSet rs = statement.executeQuery();
                String CnpFromDB = null;
                String UidFromDB = null;
                while (rs.next()) {
                    CnpFromDB = rs.getString("cnp");
                    UidFromDB = rs.getString("uid");
                }
                if (CnpFromDB != null || UidFromDB != null)
                {
                    oop.writeObject("0");
                    System.out.println("CNP : " + CnpFromDB + "OR username : "+ UidFromDB + "already used! : ");
                    aux --;
                    apkDispecer_login(pubkey, privateKey);
                }else {

                    oop.writeObject("1");


                    cryptedPass = (byte[]) oin.readObject();
                    cryptedName = (byte[]) oin.readObject();

                    decryptedPass = decrypt(privateKey,cryptedPass);
                    decryptedName = decrypt(privateKey,cryptedName);

                    String decryptedPassStr = new String(decryptedPass,"UTF-8");
                    String decryptedNameStr = new String(decryptedName,"UTF-8");
                    System.out.println(decryptedUidStr+decryptedPassStr+decryptedNameStr);
                    String insertSql = "INSERT INTO dispecer"
                            + "(cnp,uid,pass,numeprenume) VALUES"
                            + "(?,?,?,?)";
                    statement = mysqlConnect.connect().prepareStatement(insertSql);
                    statement.setString(1, decryptedCnpStr);
                    statement.setString(2, decryptedUidStr);
                    statement.setString(3, decryptedPassStr);
                    statement.setString(4, decryptedNameStr);
                    int updateSt = statement.executeUpdate();
                    if (updateSt>0) {
                        System.out.println("Record  is inserted into DISPECER table!");
                        oop.writeObject("1");
                        apkDispecer_login(pubkey, privateKey);
                    }else
                    {
                        oop.writeObject("0");
                        System.out.println("Record was not inserted into DISPECER table!");

                    }
                }
            }
            else if (regCloseORReg.equals("1"))
            {
                System.out.println("Procedura de inchidere");
                apkDispecer_login(pubkey, privateKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            closeConnection();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
//            closeConnection();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
//            closeConnection();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        }
        System.out.println("Inregistrare terminata!");


    }
	private void apkDispecer(PublicKey pubkey,PrivateKey privateKey) {
        System.out.println("Clientul s-a logat cu succes ");
        try {
            while (dispecerLogat == 1) {
                byte[] cryptedPacientAction = null;
                byte[] decryptedPacientAction = null;
                String decryptedPacientActionStr = null;
                StringBuilder UrgenteM = new StringBuilder();
                oop.writeObject(pubkey);
                cryptedPacientAction = (byte[]) oin.readObject();
                decryptedPacientAction = decrypt(privateKey, cryptedPacientAction);
                decryptedPacientActionStr = new String(decryptedPacientAction, "UTF-8");
                if (decryptedPacientActionStr.equals("Monitorizare")) {
                    StringBuilder pacientiActivi = new StringBuilder();
                    StringBuilder ambulanteActive = new StringBuilder();
                    StringBuilder Urgente = new StringBuilder();
                    StringBuilder msgAmbulante = new StringBuilder();
                    StringBuilder msgMedic = new StringBuilder();
                    for (Client auxCli : Server.clientsList) {
                        if (auxCli.clientType.equals("Pacient")) {
                            if (auxCli.NumePrenume == null) {
                                pacientiActivi.append("--");
                            } else {
                                pacientiActivi.append(auxCli.NumePrenume);
                                pacientiActivi.append(";");
                            }
                            if (auxCli.Adresa == null) {
                                pacientiActivi.append("--");
                            } else {
                                pacientiActivi.append(auxCli.Adresa);
                                pacientiActivi.append(";");
                            }
                            if (auxCli.numarDeTelefon == null) {
                                pacientiActivi.append("--");
                            } else {
                                pacientiActivi.append(auxCli.numarDeTelefon);
                                pacientiActivi.append(".");
                            }
                        }
                        System.out.print(1);
                    }
                    for (Client auxCli : Server.clientsList) {
                        if (auxCli.clientType.equals("Ambulanta")) {
                            if (auxCli.numarInmatriculare == null) {
                                ambulanteActive.append("--");
                            } else {
                                ambulanteActive.append(auxCli.numarInmatriculare);
                                ambulanteActive.append(";");
                            }
                            if (auxCli.Sofer == null) {
                                ambulanteActive.append("--");
                            } else {
                                ambulanteActive.append(auxCli.Sofer);
                                ambulanteActive.append(";");
                            }
                            if (auxCli.Medic == null) {
                                ambulanteActive.append("--");
                            } else {
                                ambulanteActive.append(auxCli.Medic);
                                ambulanteActive.append(".");
                            }
                        }
                    }

                    if (urgentePreluate.isEmpty()) {
                        Urgente.append("----------");
                    } else {
                        for (Map.Entry<Client, Client> entry : urgentePreluate.entrySet()) {
                            Client Pacient = entry.getKey();
                            Client Ambulanta = entry.getValue();
                            if (Pacient.NumePrenume == null) {
                                Urgente.append("Pacient : ");
                                Urgente.append("NumeNecunoscut");
                                Urgente.append("\n");
                            } else {
                                Urgente.append("Pacient : ");
                                Urgente.append(Pacient.NumePrenume);
                                Urgente.append("\n");
                            }
                            if (Ambulanta.numarInmatriculare == null) {
                                Urgente.append(" - Ambulanta : ");
                                Urgente.append(" -- ");
                                Urgente.append("\n");
                            } else {
                                Urgente.append(" - Ambulanta : ");
                                Urgente.append(Ambulanta.numarInmatriculare);
                                Urgente.append("\n");
                            }
                            if (Ambulanta.Medic == null) {
                                Urgente.append(" - Medic : ");
                                Urgente.append(" -- ");
                                Urgente.append("\n");
                            } else {
                                Urgente.append(" - Medic : ");
                                Urgente.append(Ambulanta.Medic);
                                Urgente.append("\n");
                            }
                            if (Pacient.SosInfoStr == null) {
                                Urgente.append("#");
                                Urgente.append(" -- ");
                                Urgente.append("\n");
                            } else {
                                Urgente.append("#");
                                Urgente.append(Pacient.SosInfoStr);
                                Urgente.append("\n");
                            }
                            if (Ambulanta.SosInfoStrAmbulanta == null) {
                                Urgente.append(" -- ");
                                Urgente.append("\n");
                            } else {
                                Urgente.append(Ambulanta.SosInfoStrAmbulanta);
                                Urgente.append("\n");
                            }
//                        if(Ambulanta.BufferSOSinfoAmbulanta == null)
//                        {
//                            Urgente.append("Informatii Urgenta - Ambulanta : ");
//                            Urgente.append(" -- ");
//                            Urgente.append("\n");
//                        }
//                        else
//                        {
//                            Urgente.append("Informatii Urgenta - Ambulanta : ");
//                            Urgente.append(Ambulanta.BufferSOSinfoAmbulanta);
//                            Urgente.append("\n");
//                        }
//                        if(Ambulanta.BufferSOSinfoPacinet == null)
//                        {
//                            Urgente.append("Informatii Urgenta - Pacient : ");
//                            Urgente.append(" -- ");
//                            Urgente.append("\n");
//                        }
//                        else
//                        {
//                            Urgente.append("Informatii Urgenta - Pacient : ");
//                            Urgente.append(Pacient.BufferSOSinfoPacinet);
//                            Urgente.append("\n");
//                        }
                            Urgente.append(";");

                        }
                    }

                    if (mesajeAmbulanta.isEmpty()) {
                        msgAmbulante.append("----------");
                    } else {
                        for (Map.Entry<Client, String> entry : mesajeAmbulanta.entrySet()) {
                            Client Ambulanta = entry.getKey();
                            String Mesajul = entry.getValue();
                            msgAmbulante.append(Ambulanta.numarInmatriculare);
                            msgAmbulante.append(",");
                            msgAmbulante.append(Mesajul);
                            msgAmbulante.append(";");
                        }
                        mesajeAmbulanta.clear();
                    }

                    if (mesajeMedic.isEmpty()) {
                        msgMedic.append("----------");
                    } else {
                        for (Map.Entry<Client, String> entry : mesajeMedic.entrySet()) {
                            Client Medic = entry.getKey();
                            String Mesajul = entry.getValue();
                            msgMedic.append(Medic.NumePrenumeMed);
                            msgMedic.append(",");
                            msgMedic.append(Mesajul);
                            msgMedic.append(";");
                        }
                        mesajeMedic.clear();
                    }


//                System.out.println(pacientiActivi);
//                System.out.println(ambulanteActive);
//                System.out.println(Urgente);
                    System.out.println(msgAmbulante);
                    oop.writeObject(pacientiActivi);
                    oop.writeObject(ambulanteActive);
                    oop.writeObject(Urgente);
                    oop.writeObject(msgAmbulante);
                    oop.writeObject(msgMedic);

                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Logout")) {

                    dispecerLogat = 0;
                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Update Cont D.")) {
                    byte[] cryptedNume = null;
                    byte[] decryptedNume = null;
                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    String decryptedNumeStr = null;
                    String decryptedParolaStr = null;
                    cryptedNume = (byte[]) oin.readObject();
                    cryptedParola = (byte[]) oin.readObject();
                    decryptedNume = decrypt(privateKey, cryptedNume);
                    decryptedNumeStr = new String(decryptedNume, "UTF-8");
                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedParolaStr = new String(decryptedParola, "UTF-8");

                    if (decryptedNumeStr.equals("gol")||decryptedParolaStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sqlLock = "UPDATE dispecer SET NumePrenume = ? , pass = ? WHERE uid = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                        statementLock.setString(1, decryptedNumeStr);
                        statementLock.setString(2, decryptedParolaStr);
                        statementLock.setString(3, dispecerUid);
                        statementLock.executeUpdate();
                        oop.writeObject("1");
                    }

                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Stergere cont D.")) {

                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    String decryptedParolaStr = null;
                    cryptedParola = (byte[]) oin.readObject();
                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedParolaStr = new String(decryptedParola, "UTF-8");
                    if (decryptedParolaStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT uid FROM dispecer WHERE uid = ? and pass = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, dispecerUid);
                        statementLock.setString(2, decryptedParolaStr);
                        ResultSet rs = statementLock.executeQuery();
                        String UidFromDB = null;
                        while (rs.next()) {
                            UidFromDB = rs.getString("uid");
                        }
                        if (UidFromDB != null)
                        {
                            String sqlLock = "DELETE FROM dispecer WHERE uid = ? and pass = ?";
                            statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                            statementLock.setString(1, dispecerUid);
                            statementLock.setString(2, decryptedParolaStr);
                            statementLock.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }

                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Inregistrare M.")) {

                    byte[] cryptedNrMat = null;
                    byte[] decryptedNrMat = null;
                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    byte[] cryptedSofer = null;
                    byte[] decryptedSofer = null;
                    byte[] cryptedMedic = null;
                    byte[] decryptedMedic = null;

                    String decryptedCNPStr = null;
                    String decryptedNumeStr = null;
                    String decryptedUidStr = null;
                    String decryptedPassStr = null;
                    String decryptedNrTelStr = null;


                    cryptedNrMat = (byte[]) oin.readObject();
                    cryptedUid = (byte[]) oin.readObject();
                    cryptedParola = (byte[]) oin.readObject();
                    cryptedSofer = (byte[]) oin.readObject();
                    cryptedMedic = (byte[]) oin.readObject();

                    decryptedNrMat = decrypt(privateKey, cryptedNrMat);
                    decryptedCNPStr = new String(decryptedNrMat, "UTF-8");

                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedNumeStr = new String(decryptedUid, "UTF-8");

                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedUidStr = new String(decryptedParola, "UTF-8");

                    decryptedSofer = decrypt(privateKey, cryptedSofer);
                    decryptedPassStr = new String(decryptedSofer, "UTF-8");

                    decryptedMedic = decrypt(privateKey, cryptedMedic);
                    decryptedNrTelStr = new String(decryptedMedic, "UTF-8");



                    if (decryptedCNPStr.equals("gol")||decryptedNumeStr.equals("gol")||decryptedUidStr.equals("gol")||
                            decryptedPassStr.equals("gol")||decryptedNrTelStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {

                        String sql = "SELECT CNP FROM medic WHERE CNP = ?";
                        PreparedStatement stm = mysqlConnect.connect().prepareStatement(sql);
                        stm.setString(1, decryptedCNPStr);
                        ResultSet rs = stm.executeQuery();
                        String NrMatFromDB = null;
                        while (rs.next()) {
                            NrMatFromDB = rs.getString("CNP");
                        }
                        if (NrMatFromDB == null && cnpValid(decryptedCNPStr) )
                        {
                            String sqlLock = "INSERT  INTO medic (CNP, NumePrenume, Uid, pass, Nr_tel)"
                                    + " VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                            st.setString(1, decryptedCNPStr);
                            st.setString(2, decryptedNumeStr);
                            st.setString(3, decryptedUidStr);
                            st.setString(4, decryptedPassStr);
                            st.setString(5, decryptedNrTelStr);
                            st.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }



                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Update Cont M.")) {
                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    byte[] cryptedNume = null;
                    byte[] decryptedNume = null;
                    byte[] cryptedNrTel = null;
                    byte[] decryptedNrTel = null;
                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    String decryptedUidStr = null;
                    String decryptedNumeStr = null;
                    String decryptedNrTelStr = null;
                    String decryptedParolaStr = null;

                    cryptedUid = (byte[]) oin.readObject();
                    cryptedNume = (byte[]) oin.readObject();
                    cryptedNrTel = (byte[]) oin.readObject();
                    cryptedParola = (byte[]) oin.readObject();

                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedUidStr = new String(decryptedUid, "UTF-8");

                    decryptedNume = decrypt(privateKey, cryptedNume);
                    decryptedNumeStr = new String(decryptedNume, "UTF-8");

                    decryptedNrTel = decrypt(privateKey, cryptedNrTel);
                    decryptedNrTelStr = new String(decryptedNrTel, "UTF-8");

                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedParolaStr = new String(decryptedParola, "UTF-8");

                    if (decryptedUidStr.equals("gol")||decryptedNumeStr.equals("gol")||decryptedNrTelStr.equals("gol")||decryptedParolaStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT Uid FROM medic WHERE Uid = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedUidStr);
                        ResultSet rs = statementLock.executeQuery();
                        String UidFromDB = null;
                        while (rs.next()) {
                            UidFromDB = rs.getString("Uid");
                        }
                        if (UidFromDB != null) {
                            String sqlLock = "UPDATE medic SET NumePrenume = ? , Nr_tel = ?, pass = ?  WHERE uid = ?";
                            PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                            st.setString(1, decryptedNumeStr);
                            st.setString(2, decryptedNrTelStr);
                            st.setString(3, decryptedParolaStr);
                            st.setString(4, decryptedUidStr);
                            st.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }
                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Stergere cont M.")) {
                    byte[] cryptedCNP = null;
                    byte[] decryptedCNP = null;
                    String decryptedCNPStr = null;
                    cryptedCNP = (byte[]) oin.readObject();
                    decryptedCNP = decrypt(privateKey, cryptedCNP);
                    decryptedCNPStr = new String(decryptedCNP, "UTF-8");
                    if (decryptedCNPStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT CNP FROM medic WHERE CNP = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedCNPStr);
                        ResultSet rs = statementLock.executeQuery();
                        String CNPFromDB = null;
                        while (rs.next()) {
                            CNPFromDB = rs.getString("CNP");
                        }
                        if (CNPFromDB != null)
                        {
                            String sqlLock = "DELETE FROM medic WHERE CNP = ?";
                            statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                            statementLock.setString(1, decryptedCNPStr);
                            statementLock.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }


                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Inregistrare A.")) {

                    byte[] cryptedNrMat = null;
                    byte[] decryptedNrMat = null;
                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    byte[] cryptedSofer = null;
                    byte[] decryptedSofer = null;
                    byte[] cryptedMedic = null;
                    byte[] decryptedMedic = null;

                    String decryptedNrMatStr = null;
                    String decryptedUidStr = null;
                    String decryptedParolaStr = null;
                    String decryptedSoferStr = null;
                    String decryptedMedicStr = null;


                    cryptedNrMat = (byte[]) oin.readObject();
                    cryptedUid = (byte[]) oin.readObject();
                    cryptedParola = (byte[]) oin.readObject();
                    cryptedSofer = (byte[]) oin.readObject();
                    cryptedMedic = (byte[]) oin.readObject();

                    decryptedNrMat = decrypt(privateKey, cryptedNrMat);
                    decryptedNrMatStr = new String(decryptedNrMat, "UTF-8");

                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedUidStr = new String(decryptedUid, "UTF-8");

                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedParolaStr = new String(decryptedParola, "UTF-8");

                    decryptedSofer = decrypt(privateKey, cryptedSofer);
                    decryptedSoferStr = new String(decryptedSofer, "UTF-8");

                    decryptedMedic = decrypt(privateKey, cryptedMedic);
                    decryptedMedicStr = new String(decryptedMedic, "UTF-8");



                    if (decryptedNrMatStr.equals("gol")||decryptedUidStr.equals("gol")||decryptedParolaStr.equals("gol")||
                            decryptedSoferStr.equals("gol")||decryptedMedicStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {

                        String sql = "SELECT NrInmatriculare FROM ambulanta WHERE NrInmatriculare = ?";
                        PreparedStatement stm = mysqlConnect.connect().prepareStatement(sql);
                        stm.setString(1, decryptedNrMatStr);
                        ResultSet rs = stm.executeQuery();
                        String NrMatFromDB = null;
                        while (rs.next()) {
                            NrMatFromDB = rs.getString("NrInmatriculare");
                        }
                        if (NrMatFromDB == null)
                        {
                            String sqlLock = "INSERT  INTO ambulanta (NrInmatriculare, uid, pass, Sofer, Medic)"
                                    + " VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                            st.setString(1, decryptedNrMatStr);
                            st.setString(2, decryptedUidStr);
                            st.setString(3, decryptedParolaStr);
                            st.setString(4, decryptedSoferStr);
                            st.setString(5, decryptedMedicStr);
                            st.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }

                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Update Cont A.")) {
                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    byte[] cryptedNume = null;
                    byte[] decryptedNume = null;
                    byte[] cryptedNrTel = null;
                    byte[] decryptedNrTel = null;
                    byte[] cryptedParola = null;
                    byte[] decryptedParola = null;
                    String decryptedUidStr = null;
                    String decryptedNumeStr = null;
                    String decryptedNrTelStr = null;
                    String decryptedParolaStr = null;

                    cryptedUid = (byte[]) oin.readObject();
                    cryptedNume = (byte[]) oin.readObject();
                    cryptedNrTel = (byte[]) oin.readObject();
                    cryptedParola = (byte[]) oin.readObject();

                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedUidStr = new String(decryptedUid, "UTF-8");

                    decryptedNume = decrypt(privateKey, cryptedNume);
                    decryptedNumeStr = new String(decryptedNume, "UTF-8");

                    decryptedNrTel = decrypt(privateKey, cryptedNrTel);
                    decryptedNrTelStr = new String(decryptedNrTel, "UTF-8");

                    decryptedParola = decrypt(privateKey, cryptedParola);
                    decryptedParolaStr = new String(decryptedParola, "UTF-8");

                    if (decryptedUidStr.equals("gol")||decryptedNumeStr.equals("gol")||decryptedNrTelStr.equals("gol")||decryptedParolaStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT Uid FROM ambulanta WHERE Uid = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedUidStr);
                        ResultSet rs = statementLock.executeQuery();
                        String UidFromDB = null;
                        while (rs.next()) {
                            UidFromDB = rs.getString("Uid");
                        }
                        if (UidFromDB != null) {
                            String sqlLock = "UPDATE ambulanta SET Sofer = ? , Medic = ?, pass = ?  WHERE uid = ?";
                            PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                            st.setString(1, decryptedNumeStr);
                            st.setString(2, decryptedNrTelStr);
                            st.setString(3, decryptedParolaStr);
                            st.setString(4, decryptedUidStr);
                            st.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }


                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Stergere cont A.")) {
                    byte[] cryptedNrMat = null;
                    byte[] decryptedNrMat = null;
                    String decryptedNrMatStr = null;
                    cryptedNrMat = (byte[]) oin.readObject();
                    decryptedNrMat = decrypt(privateKey, cryptedNrMat);
                    decryptedNrMatStr = new String(decryptedNrMat, "UTF-8");
                    if (decryptedNrMatStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT NrInmatriculare FROM ambulanta WHERE NrInmatriculare = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedNrMatStr);
                        ResultSet rs = statementLock.executeQuery();
                        String CNPFromDB = null;
                        while (rs.next()) {
                            CNPFromDB = rs.getString("NrInmatriculare");
                        }
                        if (CNPFromDB != null)
                        {
                            String sqlLock = "DELETE FROM ambulanta WHERE NrInmatriculare = ?";
                            statementLock = mysqlConnect.connect().prepareStatement(sqlLock);
                            statementLock.setString(1, decryptedNrMatStr);
                            statementLock.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }


                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Avertizare P.")) {

                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    String decryptedUidStr = null;
                    cryptedUid = (byte[]) oin.readObject();
                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedUidStr = new String(decryptedUid, "UTF-8");
                    if (decryptedUidStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT avertizat FROM pacient WHERE uid = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedUidStr);
                        ResultSet rs = statementLock.executeQuery();
                        Boolean CNPFromDB = null;
                        while (rs.next()) {
                            CNPFromDB = rs.getBoolean("avertizat");
                        }
                        if (CNPFromDB != null)
                        {

                            if (CNPFromDB == true ) {

                                String sqlLock = "UPDATE pacient SET avertizat = ? , banat = ? WHERE uid = ?";
                                PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                                st.setBoolean(1, true);
                                st.setBoolean(2, true);
                                st.setString(3, decryptedUidStr);
                                st.executeUpdate();

                                oop.writeObject("1");
                            }
                            else
                            {
                                String sqlLock = "UPDATE pacient SET avertizat = ?  WHERE uid = ?";
                                PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                                st.setBoolean(1, true);
                                st.setString(2, decryptedUidStr);
                                st.executeUpdate();

                                oop.writeObject("1");
                            }

                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }

                    Thread.sleep(2000);
                }else if (decryptedPacientActionStr.equals("Banare P.")) {

                    byte[] cryptedUid = null;
                    byte[] decryptedUid = null;
                    String decryptedUidStr = null;
                    cryptedUid = (byte[]) oin.readObject();
                    decryptedUid = decrypt(privateKey, cryptedUid);
                    decryptedUidStr = new String(decryptedUid, "UTF-8");
                    if (decryptedUidStr.equals("gol"))
                    {
                        oop.writeObject("0");
                    }
                    else
                    {
                        String sql = "SELECT uid FROM pacient WHERE uid = ?";
                        PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                        statementLock.setString(1, decryptedUidStr);
                        ResultSet rs = statementLock.executeQuery();
                        String UIDFromDB = null;
                        while (rs.next()) {
                            UIDFromDB = rs.getString("uid");
                        }
                        if (UIDFromDB != null)
                        {
                            String sqlLock = "UPDATE pacient SET banat = ?  WHERE uid = ?";
                            PreparedStatement st = mysqlConnect.connect().prepareStatement(sqlLock);
                            st.setBoolean(1, true);
                            st.setString(2, decryptedUidStr);
                            st.executeUpdate();
                            oop.writeObject("1");
                        }
                        else
                        {
                            oop.writeObject("0");
                        }
                    }



                    Thread.sleep(2000);
                }
                else if (decryptedPacientActionStr.equals("Raportare Politie")) {

                    StringBuilder Email = new StringBuilder();
                    String sql = "SELECT NumePrenume, CNP FROM pacient WHERE banat = 1";
                    PreparedStatement statementLock = mysqlConnect.connect().prepareStatement(sql);
                    ResultSet rs = statementLock.executeQuery();
                    String NumePrenumeFromDB = null;
                    String CNPFromDB = null;
                    while (rs.next()) {
                        NumePrenumeFromDB = rs.getString("NumePrenume");
                        CNPFromDB = rs.getString("CNP");
                        if(NumePrenumeFromDB != null && CNPFromDB != null)
                        {
                            Email.append(NumePrenumeFromDB);
                            Email.append(" - ");
                            Email.append(CNPFromDB);
                            Email.append("\n");
                        }
                    }
                    if (Email != null)
                    {
                        String to = "neculai.anghel@outlook.com";
                        String from = "nicushor09@gmail.com";
                        String host = "localhost";
//                        Properties properties = System.getProperties();
//                        properties.setProperty("mail.smtp.host", host);
//                        Session session = Session.getDefaultInstance(properties);
//
//                        MimeMessage message = new MimeMessage(session);
//                        message.setFrom(new InternetAddress(from));
//                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//                        message.setSubject("Raport lunar Serviciul S.D.A. - infractiuni");
//                        message.setText(Email.toString());
//                        Transport.send(message);
                        System.out.println("Sent message successfully....");
                    }

                    Thread.sleep(2000);
                }
            }
            if (dispecerLogat == 0)
            {
                apkDispecer_login(pubkey,privateKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            clientsList.remove(this);
            System.out.println("Conexiunea cu clientul a fost pierduta!");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
