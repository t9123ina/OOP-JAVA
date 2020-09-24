import java.io.*;
import java.net.*;

class DBServer{
    final static char EOT = 4;

    public static void main(String args[])
    {
        new DBServer(8888);
    }

    public DBServer(int portNumber)
    {
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            DatabaseList databaseList = new DatabaseList();
            while(true){
                processnextCommand(in,out,databaseList);
            }
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }
    private void processnextCommand(BufferedReader in,BufferedWriter out, DatabaseList databaseList) throws IOException {
        String command = in.readLine();
        new Executecommand(command,out,databaseList);
        out.write("\n");
        out.write("" + EOT +"\n");
        out.flush();
    }

}