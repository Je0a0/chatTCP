package src;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
/*
 *  Cliente TCP - versão simplificada para o laboratório de redes
 *  
 *  (C) Prof. Dr. Ulisses Rodrigues Afonseca
 *  
 *  Este é um programa cliente simples que ler uma string via teclado e 
 *  envia o texto como requisição para um servidor. O código foi
 *  adaptador do SUN Java Tutorial.
 *  
 */

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String nomeUsuario;
    private boolean conectado;
    private List<MensagemListener> listeners;

    public interface MensagemListener {
        void onMensagemRecebida(String mensagem);
    }

    public Cliente() {
        this.conectado = false;
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

    public void conectar(String endereco, int porta, String nomeUsuario) throws IOException {
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            throw new IOException("Nome de usuário inválido");
        }
        
        this.nomeUsuario = nomeUsuario;
        socket = new Socket(endereco, porta);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Enviar nome de usuário
        out.println(nomeUsuario);
        conectado = true;
        notificarListeners("Conectado ao servidor!");

        // Thread para receber mensagens
        new Thread(() -> {
            try {
                String mensagem;
                while (conectado && (mensagem = in.readLine()) != null) {
                    notificarListeners(mensagem);
                    if (mensagem.equals("goodbye")) {
                        desconectar();
                        break;
                    }
                }
            } catch (IOException e) {
                if (conectado) {
                    notificarListeners("Erro na conexão: " + e.getMessage());
                    desconectar();
                }
            }
        }).start();
    }

    public void desconectar() {
        conectado = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            notificarListeners("Desconectado do servidor");
        } catch (IOException e) {
            notificarListeners("Erro ao desconectar: " + e.getMessage());
        }
    }

    public void enviarMensagem(String mensagem) {
        if (conectado && out != null) {
            out.println(mensagem);
        }
    }

    public boolean estaConectado() {
        return conectado;
    }

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Cliente cliente = new Cliente();
        
        System.out.print("Endereço do servidor: ");
        String enderecoServidor = teclado.nextLine();
        
        System.out.print("Porta do servidor: ");
        int portaServidor = teclado.nextInt();
        teclado.nextLine(); // Limpar o buffer
        
        System.out.print("Seu nome: ");
        String nomeUsuario = teclado.nextLine();
        
        try {
            cliente.conectar(enderecoServidor, portaServidor, nomeUsuario);
            
            String mensagem;
            while (cliente.estaConectado()) {
                System.out.print("Mensagem a enviar para o servidor: ");
                mensagem = teclado.nextLine();
                if (mensagem.equalsIgnoreCase("end")) {
                    cliente.enviarMensagem("end");
                    break;
                }
                cliente.enviarMensagem(mensagem);
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            cliente.desconectar();
            teclado.close();
        }
    }
}
