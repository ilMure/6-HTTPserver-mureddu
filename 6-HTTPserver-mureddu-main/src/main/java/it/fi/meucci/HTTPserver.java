package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class HTTPserver extends Thread{
    Socket client = null;
    BufferedReader in = null;
    DataOutputStream out = null;

    public HTTPserver(Socket client){
        this.client = client;
    }

    public void run(){
        try{
            this.communicate();
        }catch(Exception e){
            System.out.println("Errore nella comunicazione");
            e.printStackTrace();
        }
    }

    public void communicate() throws Exception{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new DataOutputStream(client.getOutputStream());
            // Leggi la richiesta del browser
            String request = in.readLine();
            System.out.println("Richiesta ricevuta: " + request);

            String[] requestParts = request.split(" ");
            String filePath = requestParts[1].substring(0);
            
            // Semplice controllo per aggiungere "index.html" nel caso in cui la richiesta del browser sia "/" o una cartella
            if (filePath.endsWith("/") || filePath.equals("/")) {
                filePath += "index.html";
            }
            
            filePath = "6-HTTPserver-mureddu-main/src/resources"+filePath;
            System.out.println(filePath);
                
            File file = new File(filePath);

            sendFile(file, out);

            client.close();
    }

    private void sendFile(File file, DataOutputStream out) throws Exception{
        // Verifica se il file richiesto esiste e non è una cartella
        if (file.exists() && !file.isDirectory()) {
            System.out.println("Il file richiesto esiste, quindi invio la pagina");
            // Invia gli headers HTTP al client
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write(("Content-Type: "+getContentType(file)+"\r\n").getBytes());
            out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes()); // Riga vuota tra gli headers e il body

            // Contenuto del file
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Invia il contenuto del file al client
            out.write(fileContent);

        } else {
            System.out.println("Il file richiesto non esiste, quindi invio errore");
            File fileErr = new File("HTTPserver-mureddu-main/src/resources/errorPage/error404.html");

            // Se il file non esiste, invia la pagina per mostrare error 404
            out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
            out.write("Content-Type: text/html\r\n".getBytes());
            out.write(("Content-Length: " + fileErr.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes()); // Riga vuota tra gli headers e il body
                
            // Contenuto del file
            byte[] fileContent = Files.readAllBytes(fileErr.toPath());

            // Invia il contenuto del file al client
            out.write(fileContent);
        }
    }

    private String getContentType(File file){
        //devo distinguere i vari tipi di content-type in base al file html, css, js e image
        String contType = "";
        System.out.println(file.getName());
        String[] ar = file.getName().split("[.]");           
        String extension = ar[1];
        System.out.println("estensione: "+ extension);
        if (extension.equals("css")){
            contType = "text/css";
        }else if (extension.equals("js")){
            contType = "text/javascript";
        }else if (extension.equals("html")){
            contType = "text/html";
        }else{
            contType = "image/"+extension;
        }
        return contType;
    }

    public static class MultiServer{
        public void serverStart(){
            try {
                ServerSocket server = new ServerSocket(8000);
                for (;;){
                    System.out.println("Server in attesa...");
                    Socket socket = server.accept();
                    HTTPserver serverHTTPMultiThread = new HTTPserver(socket);
                    serverHTTPMultiThread.start();
                }
            } catch (Exception e) {
                System.out.println("Errore durante istanza del server");
            }
        }
    }
}
