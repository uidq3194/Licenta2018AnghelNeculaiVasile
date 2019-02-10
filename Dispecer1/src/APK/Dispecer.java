package APK;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import javafx.scene.layout.Border;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CyclicBarrier;

public class Dispecer{
	
//	public  JPasswordField passwordField;
////	public  JTextField textField;
////	public  JButton blogin;
////	public  JButton btnNewUser;

	private int authKey_flag = -1;
	public  PublicKey serverPubKey = null ;
	private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    public static ObjectInputStream oin = null;
    public static ObjectOutputStream oop = null;
    private Preferences preferences = Preferences.userNodeForPackage(Dispecer.class);
    public  static int dispecerLogat = 0;
    public  static CountDownLatch loginSignal = new CountDownLatch(1);


    public static void main(String[] args) throws IOException {
        int productionMode = 1;
        Dispecer dispecer = new Dispecer();

        if(productionMode == 1)
        {
            dispecer.productionMode();
        }
        else
        {
            dispecer.mentenanceMode();
        }
    }
	private void instantiateMembers(){
        try {
            this.socket = new Socket(getHostName(), getPortNumber());
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.oin = new ObjectInputStream(socket.getInputStream());
            this.oop = new ObjectOutputStream(socket.getOutputStream());
            String hostName = "localhost";
            int portNumber = 9555;
            setHostPort(hostName,portNumber);
            setPassword("a");
            setUsername("a");
        } catch (IOException e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Serverul este offline.","SdA",JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }

    }


    public static byte[] encrypt(PublicKey publicKey, @NotNull String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes());
    }
    public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }


	private void   setCredentials(String authentificationKey, String apkType) {
		    preferences.put("storred_authentificationKey", authentificationKey);
		    preferences.put("type", apkType);
		  }
	private String getStorred_authentificationKey() {
		    return preferences.get("storred_authentificationKey", null);
		  }
	private String getStorred_apkType() {
	    return preferences.get("type", null);
	  }
	private int    getPortNumber(){
		return Integer.parseInt(preferences.get("portNumber", null));
	}
    private String getHostName(){
        return preferences.get("hostName", null);
    }
	private void   setHostPort(String hostName, int portNumber) {
		preferences.put("hostName", hostName);
		preferences.put("portNumber", Integer.toString(portNumber));
	}
    private void   setUsername(String sessionKey_login){
        preferences.put("username", sessionKey_login);
    }
    private void   setPassword(String sessionKey_login){
        preferences.put("password", sessionKey_login);
    }
    private String getUsername(){
        return preferences.get("username", null);
    }
    private String getPassword(){
        return preferences.get("password", null);
    }


    public void mentenanceMode() {
        this.setCredentials("255 255 255","Dispecer");
        System.out.println("Preferences were saved.");
    }
    public void productionMode() throws IOException {

        instantiateMembers();

        apkAuthentification();
        int userLoggedInt = userLogged();
        if(this.authKey_flag == 1)
        {
            System.out.println("Aplicatia s-a autentificat cu succes!");

            if( userLoggedInt == 0)
            {
                apkDispecer_login();
//                System.out.println("Logare esuata");

            }
            else
            {
                apkDispecer_login();
//                System.out.println("Clientul s-a logat cu succes ");
            }

        }
        else
        {
            System.out.println("Aplicatia nu s-a autentificat cu succes!");
            System.out.println("ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!");
        }
        System.out.println("Final Dispecer");

    }


    public void apkAuthentification() {
        String apkType;
        PublicKey pubKey = null;
        byte[] cpryptedAuthkey = null;
        byte[] cpryptedApkType = null;

        try {
            apkType = getStorred_apkType();
            pubKey = (PublicKey) this.oin.readObject();
            this.serverPubKey = pubKey;
            cpryptedAuthkey = encrypt(pubKey,getStorred_authentificationKey());
            cpryptedApkType = encrypt(pubKey,apkType);
            this.oop.writeObject(cpryptedAuthkey);
            this.oop.writeObject(cpryptedApkType);
            String authKey_flag_str = (String) this.oin.readObject();
            System.out.println(authKey_flag_str);
            authKey_flag = Integer.parseInt(authKey_flag_str);

        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
	private int userLogged() {
		try {

			byte[] cryptedUsername = null;
			byte[] cpryptedPassword = null;
			String clientLoginAnswer = null;

			PublicKey ServerPubKey = (PublicKey) this.oin.readObject();

			System.out.println("PublicKEY : " + ServerPubKey);
			System.out.println("username : " + getUsername());
			System.out.println("password : " + getPassword());
			cryptedUsername = encrypt(ServerPubKey,getUsername());
			cpryptedPassword = encrypt(ServerPubKey,getPassword());

			this.oop.writeObject(cryptedUsername);
			this.oop.writeObject(cpryptedPassword);
			clientLoginAnswer = this.oin.readObject().toString();
			System.out.println("clientLoginAnswer : " + clientLoginAnswer);

			if (clientLoginAnswer.equals("1")) {
			    dispecerLogat = 1;
				return 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
    private void apkDispecer_login(){
        DispecerApp dispecerApp = new DispecerApp();
        SwingUtilities.invokeLater(dispecerApp);
    }
//	private void apkDispecer_login() throws IOException   {
//        login loginComponent = new login(serverPubKey,oin,oop);
//        try {
//            loginSignal.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if(this.dispecerLogat == 1){
//            apkDispecer();
//        }
//	}
//
//    public  void apkDispecer(){
//        DispecerGUI dispecerAPK =  new DispecerGUI(serverPubKey,oin,oop);
//        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
//    }
}
