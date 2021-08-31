package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable {

      private int port;
    private boolean isRunning = false;
    private ServerSocket serverSocket;
    private Selector selector;
    private ByteBuffer buffer;

    public Server(int port, int bufferSize) {
        this.port = port;
        this.buffer = ByteBuffer.allocate(bufferSize);
    }

    public void start(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning){
            try {
                int client = selector.select();

                if (client == 0) continue;

                Set<SelectionKey> selectionKeys = selector.keys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                while (it.hasNext()){
                    SelectionKey key = it.next();

                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                        Socket socket = serverSocket.accept();

                        System.out.println("From: " + socket);

                        SocketChannel channel = socket.getChannel();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    }else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
                        SocketChannel channel = null;

                        channel = (SocketChannel) key.channel();

                        boolean connection = readData(channel, buffer);

                        if (!connection){
                            key.cancel();
                            Socket socket = null;
                            socket = channel.socket();
                            socket.close();
                        }
                    }
                    selectionKeys.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean readData(SocketChannel channel, ByteBuffer buffer) {

        return true;
    }

    public void open(){
        ServerSocketChannel serverSocketChannel;

        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            serverSocket = serverSocketChannel.socket();

            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server created on port: " + port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
