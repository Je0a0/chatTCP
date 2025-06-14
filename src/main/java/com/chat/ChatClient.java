package com.chat;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame {
    private JTextField serverAddress;
    private JTextField serverPort;
    private JTextField username;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton connectButton;
    private JButton sendButton;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;

    public ChatClient() {
        setTitle("Chat Multi-usuários");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Configurar Look and Feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Painel principal com padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Painel de conexão
        JPanel connectionPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        connectionPanel.setBackground(Color.WHITE);
        connectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        serverAddress = new JTextField("localhost");
        serverPort = new JTextField("4444");
        username = new JTextField();
        connectButton = new JButton("Conectar");

        connectionPanel.add(createInputPanel("Servidor:", serverAddress));
        connectionPanel.add(createInputPanel("Porta:", serverPort));
        connectionPanel.add(createInputPanel("Nome:", username));
        connectionPanel.add(connectButton);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Painel de mensagem
        JPanel messagePanel = new JPanel(new BorderLayout(5, 0));
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        messageField = new JTextField();
        sendButton = new JButton("Enviar");
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        // Adicionar componentes ao painel principal
        mainPanel.add(connectionPanel, BorderLayout.NORTH);
        mainPanel.add(chatScroll, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        // Configurar ações
        connectButton.addActionListener(e -> handleConnect());
        sendButton.addActionListener(e -> handleSendMessage());
        messageField.addActionListener(e -> handleSendMessage());

        // Configurar estado inicial
        sendButton.setEnabled(false);
        messageField.setEnabled(false);

        // Estilizar botões
        styleButton(connectButton);
        styleButton(sendButton);

        add(mainPanel);
    }

    private JPanel createInputPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleConnect() {
        if (!connected) {
            try {
                String address = serverAddress.getText();
                int port = Integer.parseInt(serverPort.getText());
                String name = username.getText();

                if (name.trim().isEmpty()) {
                    showError("Por favor, insira seu nome.");
                    return;
                }

                socket = new Socket(address, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(name);

                new Thread(this::receiveMessages).start();

                connected = true;
                connectButton.setText("Desconectar");
                sendButton.setEnabled(true);
                messageField.setEnabled(true);
                serverAddress.setEnabled(false);
                serverPort.setEnabled(false);
                username.setEnabled(false);

            } catch (Exception e) {
                showError("Erro ao conectar: " + e.getMessage());
            }
        } else {
            disconnect();
        }
    }

    private void handleSendMessage() {
        if (connected && !messageField.getText().trim().isEmpty()) {
            out.println(messageField.getText());
            messageField.setText("");
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String finalMessage = message;
                SwingUtilities.invokeLater(() -> chatArea.append(finalMessage + "\n"));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                showError("Conexão perdida com o servidor.");
                disconnect();
            });
        }
    }

    private void disconnect() {
        try {
            if (out != null) out.println("sair");
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connected = false;
        connectButton.setText("Conectar");
        sendButton.setEnabled(false);
        messageField.setEnabled(false);
        serverAddress.setEnabled(true);
        serverPort.setEnabled(true);
        username.setEnabled(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClient().setVisible(true);
        });
    }
} 