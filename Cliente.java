import java.io.*;
import java.net.*;
import java.util.Scanner;
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

    public Cliente() {
        this.conectado = false;
    }

    public void conectar(String endereco, int porta, String nomeUsuario) throws IOException {
        this.nomeUsuario = nomeUsuario;
        socket = new Socket(endereco, porta);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Enviar nome de usuário
        out.println(nomeUsuario);
        conectado = true;
        System.out.println("Conectado ao servidor!");

        // Thread para receber mensagens
        new Thread(() -> {
            try {
                String mensagem;
                while (conectado && (mensagem = in.readLine()) != null) {
                    System.out.println(mensagem);
                }
            } catch (IOException e) {
                if (conectado) {
                    System.out.println("Erro na conexão: " + e.getMessage());
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
            System.out.println("Desconectado do servidor");
        } catch (IOException e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public void enviarMensagem(String mensagem) {
        if (conectado) {
            out.println(mensagem);
        }
    }

    public boolean estaConectado() {
        return conectado;
    }

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Cliente cliente = new Cliente();
        
        System.out.print("Digite seu nome: ");
        String nomeUsuario = teclado.nextLine();
        
        System.out.print("Endereço do servidor: ");
        String enderecoServidor = teclado.nextLine();
        
        System.out.print("Porta do servidor: ");
        int portaServidor = teclado.nextInt();
        teclado.nextLine(); // Limpar o buffer
        
        try {
            cliente.conectar(enderecoServidor, portaServidor, nomeUsuario);
            
            String mensagem;
            while (cliente.estaConectado()) {
                mensagem = teclado.nextLine();
                if (mensagem.equalsIgnoreCase("sair")) {
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
