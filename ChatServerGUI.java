import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServerGUI extends JFrame {
    private JTextArea chatArea;
    private JButton iniciarButton;
    private JButton pararButton;
    private JLabel statusLabel;
    private Servidor servidor;

    public ChatServerGUI() {
        servidor = new Servidor(4444);
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Servidor de Chat");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(580, 350));

        // Painel de controle
        JPanel controlPanel = new JPanel();
        iniciarButton = new JButton("Iniciar Servidor");
        pararButton = new JButton("Parar Servidor");
        statusLabel = new JLabel("Status: Desconectado");

        iniciarButton.addActionListener(e -> iniciarServidor());
        pararButton.addActionListener(e -> pararServidor());
        pararButton.setEnabled(false);

        controlPanel.add(iniciarButton);
        controlPanel.add(pararButton);
        controlPanel.add(statusLabel);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void iniciarServidor() {
        try {
            new Thread(() -> servidor.iniciar()).start();
            iniciarButton.setEnabled(false);
            pararButton.setEnabled(true);
            statusLabel.setText("Status: Conectado");
            adicionarMensagem("Servidor iniciado na porta 4444");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao iniciar servidor: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararServidor() {
        servidor.parar();
        iniciarButton.setEnabled(true);
        pararButton.setEnabled(false);
        statusLabel.setText("Status: Desconectado");
        adicionarMensagem("Servidor parado");
    }

    private void adicionarMensagem(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(mensagem + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatServerGUI().setVisible(true);
        });
    }
} 