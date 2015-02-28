package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer implements Runnable {
    public static final int PORT = 19257;
    private static final int MAX_THREADS = 10;

    private boolean terminated = false;

    public MultiServer() {
        //Initialization stuff
    }

    public void run() { // NOTE : does not handle port collisions
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!terminated && MultiServerThread.getThreadCount() < MAX_THREADS) {
                new MultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            System.exit(-1);
        }
    }

    public void terminate() {
        terminated = true;
    }

    // TESTING ONLY
    public static void main(String[] args) {
        MultiServer server = new MultiServer();
        new Thread(server).start();
    }
}

class MultiServerThread extends Thread {
    private static int threadCount = 0;

    private Socket socket = null;
    private volatile boolean terminated = false;

    public MultiServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        threadCount++;

        try (
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ) {
            try {
                while (!terminated) {
                    //Do server stuff
                    
                    // Execute request
                    try {

                    } catch (IllegalArgumentException e) {
                        System.err.println("<Invalid request: " + e.getMessage() + "/>");
                        continue;
                    }
                }
            } catch (Exception e) {
                System.err.println("<Exception encountered during client execution/>");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("<Exception encountered initializing resources/>");
            e.printStackTrace();
        }
    }

    // GETTERS & SETTERS

    public void terminate() {
        terminated = true;
    }

    public static int getThreadCount() {
        return threadCount;
    }
}
