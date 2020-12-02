package stratego;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class encapsulates the network functions of the Stratego program.
 * 
 * <p>Functions include starting a network connection as a server or client, 
 * sending and receiving information between connected a
 * connected server or client, and closing the network connection.</p>
 * <p>Additionally error information can be retrieved in the by invoking the
 * {@link #getErrorMessage()} method.</p>
 * 
 * @author Kristopher Rangel
 *
 */

public class StrategoNetwork {

    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    private boolean startedWithoutError; // true if connection started without error
    private String errorMessage; // error message associated with the last error occurring
    
    /**
     * Constructor.
     * <p>This constructor starts a server connections or client connection as
     * appropriate on the given port.
     * 
     * @param isServer - whether a server connection should be established
     *                   <li>if True, a server connection will be established
     *                   <li>if False, a client connection will be established
     * @param server - the hostname
     * @param port - the port number
     * 
     * @author Kristopher Rangel
     * 
     */
    public StrategoNetwork(boolean isServer, String server, int port) {
        
        if(isServer) {
            startedWithoutError = startServer(port);
        }else {
            startedWithoutError = startClient(server, port);
        }
    }
    
    /**
     * <ul><b><i>startServer</i></b></ul>
     * <ul><ul><p><code>private boolean startServer () </code></p></ul>
     *
     * Creates a server connecting accepting clients.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     */
    private boolean startServer(int port) {
        boolean hasNoException = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            connection = serverSocket.accept();
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
 
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occurred while trying to establish server.";
        }
        return hasNoException;
    }

    /**
     * <ul><b><i>startClient</i></b></ul>
     * <ul><ul><p><code>private boolean startClient (String server, int port) </code></p></ul>
     *
     * Creates a client connection and attempts to connect to the specified server.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @param server - the host name, or null for the loopback address.
     * @param port - the port number
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     * 
     */

    private boolean startClient(String server, int port) {
        boolean hasNoException = true;
        try {
            connection = new Socket(server, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occurred while trying to establish connection to server.";
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>closeConnection</i></b></ul>
     * <ul><ul><p><code>public boolean closeConnection () </code></p></ul>
     *
     * Closes the connection created by this class.
     * 
     * <p>If an exception occurred or an attempt was made to close a
     * null connection, false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     */
    public boolean closeConnection() {
        boolean hasNoException = true;
        try { 
            
            if(connection != null)
                connection.close();
            else {
                hasNoException = false;
                errorMessage = "Attempted to close a null connection.";
            }
                
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occurred while trying to close connection.";
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>writeMessage</i></b></ul>
     * <ul><ul><p><code>public boolean writeMessage (String message) </code></p></ul>
     *
     * Writes a                  to the output buffer of this connection.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @param message - the                  to transmit
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     */
    public boolean writeMessage(String message) { //TODO decide method of sending information (wrapper class?)
        boolean hasNoException = true;
        errorMessage = "No error occurred";
        try {
            output.writeObject(message);
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occured while writing message.";
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>readMessage</i></b></ul>
     * <ul><ul><p><code>public Connect4MoveMessage readMessage () </code></p></ul>
     *
     * Reads a                   from the input buffer of this connection.
     * 
     * <p>If an exception occurred while trying to read the message, null will be returned.
     * In that event, the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return - 
     * 
     * @author Kristopher Rangel
     * @author Caroline O'Neill
     */
    public String readMessage() {
        String message = null;   // need to decide how we are sending information (wrapper class?)
        errorMessage = "No error occurred.";
        try {
            message = (String) input.readObject(); // Change class here to match above
            errorMessage = "No error message.";
        } catch(SocketException | EOFException e) {
            errorMessage = "Connection Closed.";
            closeConnection();
        }catch(IOException e) {
            errorMessage = "IOException occured while trying to read message.";
        }catch(ClassNotFoundException e) {
            errorMessage = "ClassNotFoundException occured while trying to read message.";
        }
        return message;
    }
    
    /**
     * <ul><b><i>getStartError</i></b></ul>
     * <ul><ul><p><code>public boolean getStartError () </code></p></ul>
     *
     * Returns error status of network startup.
     *
     * @return True if there was an error during startup, false otherwise
     * 
     * @author Kristopher Rangel
     */
    public boolean getStartError() { return !startedWithoutError; }
    
    /**
     * <ul><b><i>getErrorMessage</i></b></ul>
     * <ul><ul><p><code>public String getErrorMessage () </code></p></ul>
     *
     * Returns the network error message associated with the last network error.
     *
     * @return a <code>String</code> representing the message associated with the last occurring network error.
     */
    public String getErrorMessage() { return errorMessage; }
}
