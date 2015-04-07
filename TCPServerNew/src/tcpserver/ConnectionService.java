/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LabUser
 */
public class ConnectionService extends Thread {

    Iterator iterator2;
    boolean isStoped = false;
    String username;
    Socket socket;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    Iterator iterator;
    TCPServer server;
    private boolean isSignedOut;

    public ConnectionService(Socket socket, PrintWriter outputWriter) {
        try {
            this.server = server;
            this.socket = socket;
            this.outputWriter = outputWriter;






            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//outputWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(outputWriter.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void run() {


        try {    //Epanalhptikh anagnvsh mhmhmatwn apo thn eiserxomenh roh ths syndeshs.
            while (true) {
                System.out.println(server.users);

                String userAddress = Integer.toString(socket.getLocalPort());
                //Eiserxomeno mhnhma(anagnwsh apo thn roh mesw tou reader).
                String receivedMessage = inputReader.readLine();
                System.out.println(receivedMessage);

                //String usersName;

                if (receivedMessage.startsWith("SignIn")) {
                    isSignedOut = false;
                    username = receivedMessage.substring(7);
                    if (server.users.containsKey(username)) {

                        outputWriter.println(" choose another nickname");

                        return;
                    } else {



                        outputWriter.println(username + " SignIn ");
                        server.users.put(username, socket);
                        iterator = server.users.keySet().iterator();
                        while (iterator.hasNext()) {
                            String userNames = (String) iterator.next();
                            Socket usersSockets = server.users.get(userNames);
                            if (userNames != username) {
                                PrintWriter AllOutputWriters = new PrintWriter(usersSockets.getOutputStream(), true);
                                AllOutputWriters.println(username + " just sign in ");
                                outputWriter.println(userNames + "  isAlredyIn ");
                            }



                        }

                    }
                } else if (isSignedOut) {
                    outputWriter.flush();
                } else if (receivedMessage.startsWith("MessageTo")) {
                    String goToUser = receivedMessage.substring(10);
                    iterator = TCPServer.users.keySet().iterator();
                    while (iterator.hasNext()) {
                        String usersNames = (String) iterator.next();
                        Socket usersSocket = TCPServer.users.get(usersNames);
                        if (goToUser.startsWith(usersNames)) {
                            PrintWriter AllOutputWriters = new PrintWriter(usersSocket.getOutputStream(), true);
                            String privateMessage = goToUser.substring(usersNames.length());
                            AllOutputWriters.println(privateMessage);

                        } else {

                            outputWriter.println(" no users with this nickname ");

                        }
                        return;


                    }

                } else if (receivedMessage.startsWith("Message")) {

                    iterator = server.users.keySet().iterator();

                    while (iterator.hasNext()) {
                        String userNames = (String) iterator.next();
                        Socket usersSockets = server.users.get(userNames);



                        if (userNames == username) {
                            outputWriter.println(" I say: " + receivedMessage.substring(7));



                        } else {
                            PrintWriter AllOutputWriters = new PrintWriter(usersSockets.getOutputStream(), true);
                            AllOutputWriters.println(username + " says: " + receivedMessage.substring(7));

                        }


                    }
                } else if (receivedMessage.startsWith("Signout")) {


                    server.users.remove(username);
                    server.users.remove(this);
                    outputWriter.println("SignedOut");

                    iterator = server.users.keySet().iterator();

                    while (iterator.hasNext()) {
                        String userNames = (String) iterator.next();
                        Socket usersSockets = server.users.get(userNames);

                        PrintWriter AllOutputWriters = new PrintWriter(usersSockets.getOutputStream(), true);

                        AllOutputWriters.println(username + " SignedOut ");

                    }



                    outputWriter.flush();
                    // outputWriter.close();

                    isSignedOut = true;


                }
            }


        } catch (IOException e) {
            System.out.println("IOExeption throws");
        } catch (Exception e) {
            System.out.println("exeption throws");
        }
    }
}
