package src;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static int porta = 4444;
    private static final String versao = "1.0";
    private static final String discoPrincipal = "/";
    private Set<ClientHandler> clientes;
    private ServerSocket serverSocket;
    private boolean executando;
    private List<MensagemListener> listeners;

    public interface MensagemListener {
        void onMensagemRecebida(String mensagem);
    }

    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new HashSet<>();
        this.executando = false;
        this.listeners = new ArrayList<>();
    }

    public void addMensagemListener(MensagemListener listener) {
        listeners.add(listener);
    }

    public void removeMensagemListener(MensagemListener listener) {
        listeners.remove(listener);
    }

    private void notificarListeners(String mensagem) {
        for (MensagemListener listener : listeners) {
            listener.onMensagemRecebida(mensagem);
        }
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(porta);
            executando = true;
            notificarListeners("Servidor iniciado na porta " + porta);
            
            while(executando) {
                Socket clientSocket = serverSocket.accept();
                String endereco = clientSocket.getRemoteSocketAddress().toString();
                notificarListeners("Nova conexão estabelecida com " + endereco);
                
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientes.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            notificarListeners("Erro no servidor: " + e.getMessage());
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
            notificarListeners("Servidor parado");
        } catch (IOException e) {
            notificarListeners("Erro ao parar servidor: " + e.getMessage());
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
                if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
                    throw new IOException("Nome de usuário inválido");
                }
                broadcast(nomeUsuario + " entrou no chat");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String outputLine;
                    
                    // Verifica se é um comando do protocolo original
                    switch (inputLine) {
                        case "getVersion":
                            outputLine = versao;
                            break;
                        case "getServerTime":
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date hora = Calendar.getInstance().getTime();
                            outputLine = sdf.format(hora);
                            break;
                        case "getFreeSpace":
                            File principal = new File(discoPrincipal);
                            long freeespace = principal.getFreeSpace();
                            outputLine = "Espaço disponível = " + freeespace;
                            break;
                        case "end":
                            outputLine = "goodbye";
                            break;
                        default:
                            // Se não for um comando, trata como mensagem de chat
                            broadcast(nomeUsuario + ": " + inputLine);
                            continue;
                    }
                    
                    out.println(outputLine);
                    if (outputLine.equals("goodbye")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro na conexão com cliente: " + e.getMessage());
            } finally {
                desconectar();
            }
        }

        public void enviarMensagem(String mensagem) {
            if (out != null) {
                out.println(mensagem);
            }
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
        notificarListeners(mensagem);
        for (ClientHandler cliente : clientes) {
            cliente.enviarMensagem(mensagem);
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor(porta);
        servidor.iniciar();
    }
}

