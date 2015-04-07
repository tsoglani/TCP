/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;


import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Dhmioyrgia ths klashs TCPServer. Prokeitai gia enan server poy leitoyrgei symfwna me to protokolo TCP kai 
 * boh8aei sthn antallagh mhnhmatwn twn clients poy syndeontai se ayton.
 */
public class TCPServer {

    
static Hashtable<String, Socket> users;
    //H porta sthn opoia afoygrazetai o server aithmata gia nees syndeseis
    private int serverPort = 15000;
    //Dhlwsh enos ServerSocket(eksidikeymeno socket gia servers sto TCP).
    private ServerSocket serverSocket;
    
PrintWriter outputWriter;

    //O kataskeyasths toy TCP Server.
    public TCPServer() {
      
        users= new Hashtable<String, Socket>();
        //Xrhsh enos handler gia socket exceptions(p.x. mh epityxh dhmiourgia syndeshs).
        try {
            
            //Dhmiourgia enos ServerSocket se sygkekrimenh porta.
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Waiting...");
            //Epanalhptikos broxos ston opoio to serverSocket afoygrazetai aithmata gia syndeseis kai 
            //dhmioyrgei syndesh me enan client se ksexwristo socket.
            while (true) {
                //Apodoxh aithshs gia syndesh toy client me ton server kai dhmioyrgia syndeshs se ksexwristo socket.
                Socket socket = serverSocket.accept();
            
                
             outputWriter = new PrintWriter(socket.getOutputStream(), true);
               



ConnectionService service= new  ConnectionService(socket,outputWriter);
service.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TCPServer();
    }
}
