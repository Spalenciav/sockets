

package chatsockets;


import java.io.*;
import java.net.*;
import javax.swing.*;

public class Socket2 extends JFrame {
    private JTextField texto;
    private JButton boton;
    private JTextArea chatArea;
    private ServerSocket server;

    public Socket2() {
        setTitle("Socket 2");
        setBounds(500, 100, 400, 300);
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
            Socket socket = new Socket("127.0.0.1", 5000);
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
            server = new ServerSocket(6000);
            while (true) {
                Socket socket = server.accept();
                DataInputStream flujo = new DataInputStream(socket.getInputStream());
                String mensaje = flujo.readUTF();
                chatArea.append("Socket1: " + mensaje + "\n");
                flujo.close();
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
