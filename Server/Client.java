package client;
/*
 * Client part of the client-server relationship.
 * Most of this code was written by George Farcasiu
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable{
    private volatile boolean terminated = false;
    
    private String ipAddress;

    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream ois;

    // INITIALIZATION BLOCK
    {
        try {
            socket = new Socket(ipAddress, MultiServer.PORT);
            os = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // NOTE : Cannot be called before the thread starts or run is called
    public void executeAction(Object ... args) {
        try {
            os.writeObject(args);
        } catch (IOException e) {
            System.err.println("<IOException encountered when initializing resources/>");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ipAddress = findServerIpAddress();

        try {
            try {
                while (!terminated) {
                        //Do client stuff
                }
            } catch (Exception e) {
                System.err.println("<Exception encountered during client execution/>");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                os.close();
                ois.close();
            } catch (IOException e) {
                // DO NOTHING
            }
        }
    }

    public void terminate() {
        terminated = true;
    }

    public String findServerIpAddress() {
        return "192.168.1.6";
    }
}//end class


