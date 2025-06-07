import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField mensagemField;
    private JButton enviarButton;
    private JButton conectarButton;
    private JButton desconectarButton;
    private JTextField nomeField;
    private JTextField servidorField;
    private JTextField portaField;
    private JLabel statusLabel;
    private Cliente cliente;

    public ChatClientGUI() {
        cliente = new Cliente();
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Cliente de Chat");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel de conexão
        JPanel conexaoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        conexaoPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        nomeField = new JTextField(15);
        conexaoPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        conexaoPanel.add(new JLabel("Servidor:"), gbc);
        gbc.gridx = 1;
        servidorField = new JTextField("localhost", 15);
        conexaoPanel.add(servidorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        conexaoPanel.add(new JLabel("Porta:"), gbc);
        gbc.gridx = 1;
        portaField = new JTextField("4444", 15);
        conexaoPanel.add(portaField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        conectarButton = new JButton("Conectar");
        desconectarButton = new JButton("Desconectar");
        desconectarButton.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(conectarButton);
        buttonPanel.add(desconectarButton);
        conexaoPanel.add(buttonPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        statusLabel = new JLabel("Status: Desconectado");
        conexaoPanel.add(statusLabel, gbc);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(580, 300));

        // Painel de mensagem
        JPanel mensagemPanel = new JPanel(new BorderLayout());
        mensagemField = new JTextField();
        enviarButton = new JButton("Enviar");
        enviarButton.setEnabled(false);
        mensagemPanel.add(mensagemField, BorderLayout.CENTER);
        mensagemPanel.add(enviarButton, BorderLayout.EAST);

        // Layout principal
        setLayout(new BorderLayout());
        add(conexaoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(mensagemPanel, BorderLayout.SOUTH);

        // Eventos
        conectarButton.addActionListener(e -> conectar());
        desconectarButton.addActionListener(e -> desconectar());
        enviarButton.addActionListener(e -> enviarMensagem());
        mensagemField.addActionListener(e -> enviarMensagem());
    }

    private void conectar() {
        String nome = nomeField.getText().trim();
        String servidor = servidorField.getText().trim();
        String portaStr = portaField.getText().trim();

        if (nome.isEmpty() || servidor.isEmpty() || portaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int porta = Integer.parseInt(portaStr);
            cliente.conectar(servidor, porta, nome);
            conectarButton.setEnabled(false);
            desconectarButton.setEnabled(true);
            enviarButton.setEnabled(true);
            statusLabel.setText("Status: Conectado");
            adicionarMensagem("Conectado ao servidor");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao conectar: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desconectar() {
        cliente.desconectar();
        conectarButton.setEnabled(true);
        desconectarButton.setEnabled(false);
        enviarButton.setEnabled(false);
        statusLabel.setText("Status: Desconectado");
        adicionarMensagem("Desconectado do servidor");
    }

    private void enviarMensagem() {
        String mensagem = mensagemField.getText().trim();
        if (!mensagem.isEmpty()) {
            cliente.enviarMensagem(mensagem);
            mensagemField.setText("");
        }
    }

    private void adicionarMensagem(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(mensagem + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
} 