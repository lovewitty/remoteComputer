import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class main {
	private static int SERVER_PORT = 9999, SERVER_PORT_DHCP = 9994, SERVER_PORT1 = 9998,  portInit = 9950;;
	static Socket socket1;

    private static ServerSocket server1;
    static DatagramSocket socket, socket_DHCP;
    static DatagramSocket[] sockets;
    static Thread[] threads;
    static BufferedImage bi;
    private static ObjectInputStream ois;
    private static float[] point;
    private static JFrame frame;
    private static JFrame pan;
    private static Robot robot;
    private static float x1, y1;
    private static int TAILLE_MAX = 20000, frequence=50;
    
    static byte[] receiveData, sendData;
    static DatagramPacket paquetRetour, paquetRecu;
    static String requete;
    static InetAddress IPAddress;
    static byte[] by;
    static int nbre;
    static  byte[][] fragments;
	/**
	 * @param args
	 */
public static void main(String[] args) {
		// TODO Auto-generated method stub
		//final panneau_transparent pan = new panneau_transparent();
	    GraphicsEnvironment ge = 
	            GraphicsEnvironment.getLocalGraphicsEnvironment();
	        GraphicsDevice gd = ge.getDefaultScreenDevice();
	        boolean isPerPixelTranslucencySupported = 
	            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
	        try {
				robot = new Robot();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        //If translucent windows aren't supported, exit.
	        if (!isPerPixelTranslucencySupported) {
	            System.out.println(
	                "Per-pixel translucency is not supported");
	               pan = new panneau_transparent(); 
	        }

	        JFrame.setDefaultLookAndFeelDecorated(true);
	         pan = new translucent();
	        ((JFrame) pan).setVisible(true);
		final JFrame frame = new JFrame();
		JLabel label = new JLabel("");
		frame.setSize(((JFrame) pan).getSize());
		frame.setTitle("Rendu de la capture");
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setVisible(true);
//		frame.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent arg0) {
//				
//				
//				
//				
//			}
//		});
		
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

				while(true){
					try {
						socket = null;
						try
						{
							// On essaye de cr�er notre socket serveur UDP
							socket = new DatagramSocket(SERVER_PORT);
							System.out.println("Start the server at port " + SERVER_PORT
									+ " and waiting for clients...");
							sendFile(SERVER_PORT);
						}
						catch (SocketException se) 
						{
							se.printStackTrace();
						}
						//				


					}


					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	//	System.out.println("valeur: "+conversionto256base(3147).length);
       t1.start();
       
       
       Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
				 try {
						server1 = new ServerSocket(SERVER_PORT1);
						  System.out.println("Start the server at port " + SERVER_PORT1
				                  + " and waiting for clients...");
				
			           while(true){
		                 socket1 = server1.accept();
		               System.out.println("Accept socket connection: "+socket1.getInetAddress());
		             
		                   // The file name must be a fully qualified path
		                ois = new ObjectInputStream(socket1.getInputStream());
		                point = (float[])ois.readObject();
		                if(point.length==1){
		                	if((int)point[0]==0){
		                		robot.keyPress(KeyEvent.VK_BACK_SPACE);
			                	robot.keyRelease(KeyEvent.VK_BACK_SPACE);
		                	}
//		                	else if((int)point[0]==-2){
//		                		robot.keyPress(KeyEvent.VK_ENTER);
//			                	robot.keyRelease(KeyEvent.VK_ENTER);
//		                	}
//		                	
//		                	else if((int)point[0]==-3){
//		                		robot.keyPress(KeyEvent.VK_QUOTE);
//			                	robot.keyRelease(KeyEvent.VK_QUOTE);
//		                	}
//		                	else if((int)point[0]==-4){
//		                		robot.keyPress(KeyEvent.VK_MINUS);
//			                	robot.keyRelease(KeyEvent.VK_MINUS);
//		                	}
//		                	else if((int)point[0]==-5){
//		                		robot.keyPress(KeyEvent.VK_SEMICOLON);
//			                	robot.keyRelease(KeyEvent.VK_SEMICOLON);
//		                	}
//		                	
//		                	else if((int)point[0]==-6){
//		                		robot.keyPress(KeyEvent.VK_SLASH);
//			                	robot.keyRelease(KeyEvent.VK_SLASH);
//		                	}
//		                	
//		                	else if((int)point[0]==-7){
//		                		robot.keyPress(KeyEvent.VK_SPACE);
//			                	robot.keyRelease(KeyEvent.VK_SPACE);
//		                	}
//		                	
//		                	else if((int)point[0]==-8){
//		                		robot.keyPress(KeyEvent.VK_PERIOD);
//			                	robot.keyRelease(KeyEvent.VK_PERIOD);
//		                	}
		                	else{
		                	robot.keyPress((char)point[0]);
		                	robot.keyRelease((char)point[0]);
		                	}
		                	//JOptionPane.showMessageDialog(frame,  "Touche saisie: "+(char)point[0], "touche saisie", JOptionPane.INFORMATION_MESSAGE);
		                }
		                else{
		                try{
		                	 x1 = (pan.getWidth()/point[2])*point[0]+pan.getX(); y1 = ( pan.getHeight()/point[3])*point[1]+pan.getY();
		                	robot.mouseMove((int)x1, (int)y1);
		                	robot.mousePress(InputEvent.BUTTON1_MASK);
		                	robot.mouseRelease(InputEvent.BUTTON1_MASK);
			                //JOptionPane.showMessageDialog(frame,  "x1 = "+x1+" et y1= "+y1, "Nouvelles coordonnees", JOptionPane.INFORMATION_MESSAGE);

		                }catch(ArithmeticException e){
		                	e.printStackTrace();
		                }
		                }
		                    
			           }
		             
		            }
		         catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            
		            try {
						server1.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }

				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 try {
							server1.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
				}
				
			}
		});
	//	System.out.println("valeur: "+conversionto256base(3147).length);
      t2.start();
      
      
      
      Thread t4 = new Thread(new Runnable() {
 			
 			@Override
 			public void run() {
 				while(true){
 				 try {
 						
 					 attendreRequete(SERVER_PORT_DHCP);
 		            }
 		        

 				 catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 				}
 				
 			}
 		});
 	//	System.out.println("valeur: "+conversionto256base(3147).length);
     t4.start();
       
       
       
       
      while(true){
			try{
				bi = ScreenShotFactory.screenShot(((JFrame) pan).getBounds(), frame.getSize(), "image.png", ScreenShotFactory.IMAGE_TYPE_PNG);
			label.setIcon(new ImageIcon(bi));
			frame.setSize(((JFrame) pan).getSize());
			
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			}catch(NullPointerException e){
				System.out.println("Acces refuse intercepte.... Continuit� du programme...");
			}
			
		}
		
	}

public static void attendreRequete(int port)
{
	socket_DHCP = null;
	try
	{
		// On essaye de cr�er notre socket serveur UDP
		socket_DHCP = new DatagramSocket(port);
		System.out.println("Start the server at port " + SERVER_PORT_DHCP
                  + " and waiting for clients...");
	}
	catch (SocketException se) 
	{
		se.printStackTrace();
	}
	// On initialise les trames qui vont servir � recevoir et envoyer les paquets
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	// Tant qu'on est connect�, on attend une requ�te et on y r�pond
	while (socket_DHCP != null && !socket_DHCP.isClosed())
	{
		try
		{
			DatagramPacket paquetRecu = new DatagramPacket(receiveData, receiveData.length);
			socket_DHCP.receive(paquetRecu);
			String requete = new String(paquetRecu.getData());
			InetAddress IPAddress = paquetRecu.getAddress();
			if (requete.contains("je recherche un serveur remoteComputer"))
			{
				sendData = "Je suis un serveur remoteComputer".getBytes();
				DatagramPacket paquetRetour = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				socket_DHCP.send(paquetRetour);
				//socket_DHCP.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}



public static void sendFile(int port)
{
	
	// On initialise les trames qui vont servir � recevoir et envoyer les paquets
	receiveData = new byte[1024];
	
	// Tant qu'on est connect�, on attend une requ�te et on y r�pond
	while (socket != null && !socket.isClosed())
	{

		try{

			 paquetRecu = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(paquetRecu);  //j'attends qu'il m'envoit une demande d'image
			 requete = new String(paquetRecu.getData());
			 IPAddress = paquetRecu.getAddress();
//			if (requete.contains("oh serveur, envoit moi une image"))
//			{
//				byte[] by = imageToByteArray(bi);
//
//				char[] valeurs = conversionto256base(by.length);
//
//				sendData  =new Integer(valeurs.length).toString().getBytes();
//				DatagramPacket paquetRetour = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				socket.send(paquetRetour);
//				for(char c: valeurs){
//					sendData  =new Integer((int)c).toString().getBytes();
//					paquetRetour = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//					socket.send(paquetRetour);
//				}
//
//				paquetRetour = new DatagramPacket(by, by.length, IPAddress, port);
//				socket.send(paquetRetour);
//				System.out.println("Image envoy�...");
//			}
			
			if (requete.contains("oh serveur, envoit moi une image"))
			{
				 by = imageToByteArray(bi);
				
				 nbre = (int) Math.ceil((double)by.length/TAILLE_MAX);
				
				
				sendData  =new byte[nbre];
				paquetRetour = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				socket.send(paquetRetour);
				fragment(by, nbre);
				
				if(nbre>1){
					threads = new Thread[nbre-1];
					sockets = new DatagramSocket[nbre-1];
					
					for(int i=0; i<nbre-1; i++){
						final int j = i;
						threads[i] = new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								// TODO Auto-generated method stub
								try {
									sockets[j] = new DatagramSocket(portInit+j);
									sockets[j].send(new DatagramPacket(fragments[j+1], fragments[j+1].length, IPAddress, portInit+j));
									sockets[j].close();
								} catch (SocketException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						threads[i].start();
					}
				}
				
				
				
			
				
					 socket.send(new DatagramPacket(fragments[0], fragments[0].length, IPAddress, port));
					
				
				try {
					Thread.sleep(frequence);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			

				
				System.out.println("Image envoy�..."+nbre+"....."+by.length);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}


	}
}

public static void fragment(byte[] tab, int nbre){
	
	 fragments = new byte[nbre][];
	int i=0, lue = 0;
	byte[] temp;
	while(tab.length-lue>TAILLE_MAX){
		temp = Arrays.copyOfRange(tab, lue, lue+TAILLE_MAX);
		fragments[i] = Arrays.copyOf(temp, temp.length+1);
		fragments[i][TAILLE_MAX] = new Integer(i).byteValue();
		lue +=TAILLE_MAX;
		i++;
	}
	if(i<nbre){
		temp = Arrays.copyOfRange(tab, lue, lue+TAILLE_MAX);
		fragments[i] = Arrays.copyOf(temp, TAILLE_MAX+1);
		fragments[i][TAILLE_MAX] = new Integer(i).byteValue();
		i++;
	}
}
 

	
	
	public static byte[] imageToByteArray(BufferedImage image) throws IOException
	{
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, ScreenShotFactory.IMAGE_TYPE_PNG, baos);
	    return baos.toByteArray();
	}
	
	public static char[] conversionto256base(int nombre){
		int nomb = nombre;
		int i=0;
		while(nomb>0){
			i++;
			nomb/=256;
		}
		
		 nomb = nombre;
		 char result[] = new char[i];
		 
		while(nomb>0){
			
			result[i-1]=(char) (nomb%256);
			nomb/=256;
			i--;
		}
		
		return result;
	}
}



