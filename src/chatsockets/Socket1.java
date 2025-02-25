

package chatsockets;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Socket1 extends JFrame {
    private JTextField texto;
    private JButton boton;
    private JTextArea chatArea;
    private ServerSocket server;
    
    public Socket1() {
        setTitle("Socket 1");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        chatArea = new JTextArea();
        chatArea.setBounds(20, 20, 340, 150);
        chatArea.setEditable(false);
        add(chatArea);

        texto = new JTextField();
        texto.setBounds(20, 180, 240, 30);
        add(texto);

        boton = new JButton("Enviar");
        boton.setBounds(270, 180, 90, 30);
        boton.addActionListener(e -> enviarMensaje());
        add(boton);

        setVisible(true);
        
        // Iniciar servidor en un hilo separado
        new Thread(this::recibirMensajes).start();
    }

    private void enviarMensaje() {
        try {
            Socket socket = new Socket("127.0.0.1", 6000);
            DataOutputStream flujo = new DataOutputStream(socket.getOutputStream());
            flujo.writeUTF(texto.getText());
            flujo.close();
            socket.close();
            chatArea.append("Tú: " + texto.getText() + "\n");
            texto.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void recibirMensajes() {
        try {
            server = new ServerSocket(5000);
            while (true) {
                Socket socket = server.accept();
                DataInputStream flujo = new DataInputStream(socket.getInputStream());
                String mensaje = flujo.readUTF();
                chatArea.append("Socket2: " + mensaje + "\n");
                flujo.close();
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
