package com.chat;

import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor {
    private static int porta = 4444;
    private static Set<ClienteHandler> clientes = Collections.synchronizedSet(new HashSet<>());

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta " + porta);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova conexão estabelecida com " + clientSocket.getRemoteSocketAddress().toString());
                
                ClienteHandler clienteHandler = new ClienteHandler(clientSocket);
                clientes.add(clienteHandler);
                new Thread(clienteHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e);
            System.exit(1);
        }
    }

    static class ClienteHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String nomeUsuario;

        public ClienteHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                nomeUsuario = in.readLine();
                broadcast(nomeUsuario + " entrou no chat!");

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (mensagem.equalsIgnoreCase("sair")) {
                        break;
                    }
                    broadcast(nomeUsuario + ": " + mensagem);
                }

                clientes.remove(this);
                broadcast(nomeUsuario + " saiu do chat!");
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro no handler do cliente: " + e);
            }
        }

        private void broadcast(String mensagem) {
            for (ClienteHandler cliente : clientes) {
                cliente.out.println(mensagem);
            }
        }
    }
} 