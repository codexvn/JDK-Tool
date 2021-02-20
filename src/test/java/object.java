import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Generated;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class object {
    public static ArrayList<Thread> threadArrayList = new ArrayList<Thread>();

    @Test
    public void testObject() throws IOException {
        Stream<Path> list = Files.walk(Paths.get("D:/ss"));
        list.forEach(System.out::println);
    }

    @Test
    void testDirStream() throws IOException {
        DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("D:/ss"));
        Path path = Files.walkFileTree(Paths.get("D:/ss"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(attrs.creationTime());
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(attrs.creationTime());
                return super.visitFile(file, attrs);
            }
        });
    }

    @Test
    void zipFileSystem() throws IOException {
        FileSystem fileSystem = FileSystems.newFileSystem(Paths.get("D:/ss.zip"));
        Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        System.out.println(dir);
                        return super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        System.out.println(file);
                        return super.visitFile(file, attrs);
                    }
                }
        );
    }

    @Test
    void readFile() {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(Paths.get("D:/123.txt").toFile()))) {
            FileChannel fileChannel = FileChannel.open(Paths.get("D:/lock.txt"));
            fileChannel.tryLock();
            while (dataInputStream.available() != 0)
                System.out.println(dataInputStream.read());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void inetAddress() throws UnknownHostException {
        InetAddress[] inetAddress = InetAddress.getAllByName("time-a.nist.gov");
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost.toString());
    }

    @Test
    void serverSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(12800);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try (
                            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                            Scanner scanner = new Scanner(socket.getInputStream())) {
                        object.threadArrayList.add(Thread.currentThread());
                        System.out.println("服务器启动:" + Thread.currentThread().getId() + "    " + threadArrayList.size());
//                        while (scanner.hasNextLine())
                        System.out.println(scanner.nextLine());
                        System.out.println("服务器关闭");
                        object.threadArrayList.remove(Thread.currentThread());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Test
    void socketChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 12800));
       Scanner scanner = new Scanner(socketChannel);
       while (scanner.hasNextLine())
           System.out.println(scanner.next());

    }

    @Test
    void tmpServer() throws IOException {
            ServerSocket serverSocket = new ServerSocket(12800);
            while (true){
                System.out.println("服务器启动");
                try (Socket socket = serverSocket.accept();
                     PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true)){
                    while (true){
                        for (int i = 0; i < 10; i++)
                            printWriter.println(String.format("%02d",i));
                        while (true);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("服务器关闭");
            }

    }
    public static class tstt{
        public tstt() {

        }

        public static void ts(){}
        public void sdfsdfd(){};
    }

    @Test
    void javaConnectSocket() throws IOException {
        System.out.println("客户端连接服务器");
        try (Socket socket = new Socket("localhost", 12800)) {
            Scanner  scanner = new Scanner(socket.getInputStream());
            while (scanner.hasNextLine())
                System.out.println(scanner.nextLine());
        }
    }
}