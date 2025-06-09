import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServerGUI extends JFrame {
    private JTextArea chatArea;
    private JButton iniciarButton;
    private JButton pararButton;
    private JLabel statusLabel;
    private JTextField ipField;
    private JTextField portaField;
    private Servidor servidor;

    public ChatServerGUI() {
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Servidor de Chat");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel de configuração
        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(new JLabel("IP:"), gbc);
        gbc.gridx = 1;
        ipField = new JTextField("localhost", 15);
        configPanel.add(ipField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        configPanel.add(new JLabel("Porta:"), gbc);
        gbc.gridx = 1;
        portaField = new JTextField("4444", 15);
        configPanel.add(portaField, gbc);

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
        add(configPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void iniciarServidor() {
        try {
            String ip = ipField.getText().trim();
            String portaStr = portaField.getText().trim();

            if (ip.isEmpty() || portaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int porta = Integer.parseInt(portaStr);
            servidor = new Servidor(porta);
            new Thread(() -> servidor.iniciar()).start();
            iniciarButton.setEnabled(false);
            pararButton.setEnabled(true);
            statusLabel.setText("Status: Conectado");
            adicionarMensagem("Servidor iniciado em " + ip + ":" + porta);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Porta inválida. Digite um número válido.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao iniciar servidor: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararServidor() {
        if (servidor != null) {
            servidor.parar();
            iniciarButton.setEnabled(true);
            pararButton.setEnabled(false);
            statusLabel.setText("Status: Desconectado");
            adicionarMensagem("Servidor parado");
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
            new ChatServerGUI().setVisible(true);
        });
    }
} 