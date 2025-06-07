import java.net.*;
import java.io.*;
import java.util.*;
/*
 *  Servidor TCP - versão simplificada para o laboratório de redes
 *
 *  (C) Prof. Dr. Ulisses Rodrigues Afonseca
 *
 *  Este é um programa servidor simples, sem thread, para atender a um
 *  protocolo genérico implementado na classe Protocolo. O código original
 *  foi tirado do SUN Java Tutorial.
 *
 */
 
public class Servidor {
    private int porta;
    private Set<ClientHandler> clientes;
    private ServerSocket serverSocket;
    private boolean executando;

    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new HashSet<>();
        this.executando = false;
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(porta);
            executando = true;
            System.out.println("Servidor iniciado na porta " + porta);
            
            while(executando) {
                Socket clientSocket = serverSocket.accept();
                String endereco = clientSocket.getRemoteSocketAddress().toString();
                System.out.println("Nova conexão estabelecida com " + endereco);
                
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientes.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        } finally {
            parar();
        }
    }

    public void parar() {
        executando = false;
        try {
            for (ClientHandler cliente : clientes) {
                cliente.desconectar();
            }
            clientes.clear();
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("Servidor parado");
        } catch (IOException e) {
            System.out.println("Erro ao parar servidor: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String nomeUsuario;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Erro ao criar streams: " + e.getMessage());
            }
        }

        public void run() {
            try {
                nomeUsuario = in.readLine();
                broadcast(nomeUsuario + " entrou no chat");

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (mensagem.equalsIgnoreCase("sair")) {
                        break;
                    }
                    broadcast(nomeUsuario + ": " + mensagem);
                }
            } catch (IOException e) {
                System.out.println("Erro na conexão com cliente: " + e.getMessage());
            } finally {
                desconectar();
            }
        }

        public void enviarMensagem(String mensagem) {
            out.println(mensagem);
        }

        public void desconectar() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
                clientes.remove(this);
                if (nomeUsuario != null) {
                    broadcast(nomeUsuario + " saiu do chat");
                }
            } catch (IOException e) {
                System.out.println("Erro ao desconectar cliente: " + e.getMessage());
            }
        }
    }

    private void broadcast(String mensagem) {
        System.out.println(mensagem);
        for (ClientHandler cliente : clientes) {
            cliente.enviarMensagem(mensagem);
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor(4444);
        servidor.iniciar();
    }
}

