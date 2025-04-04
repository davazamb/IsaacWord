import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class IsaacWordsApp {
    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame frame = new JFrame("Aplicación de selección de letras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Etiqueta para mostrar las letras seleccionadas
        JLabel label = new JLabel("");
        label.setFont(new Font("Arial", Font.PLAIN, 40)); // Tamaño de fuente
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.CENTER);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 7, 6, 6)); // Espaciado entre botones

        // Botones de letras
        String[] letters = {"1","2","3","4","5","6","7","8","9","0","A",
                "B","C", "D","E","F","G","H","I","J","K","L","M","N",
                "Ñ","O","P","R","S","T","U","V","W","X","Y","Z"};
        for (String letter : letters) {
            JButton button = new JButton(letter);
            button.setFont(new Font("Arial", Font.BOLD, 18)); // Ampliar tamaño del texto

            // Asignar colores según criterio: rojo para vocales, azul para consonantes
            if ("1234567890".contains(letter)) {
                button.setBackground(Color.cyan); // Vocales
                button.setForeground(Color.black); // Texto blanco para contraste
            } else {
                button.setBackground(Color.PINK); // Consonantes
                button.setForeground(Color.black); // Texto blanco para contraste
            }

            button.addActionListener((ActionEvent e) -> {
                // Agregar la letra seleccionada a la etiqueta
                label.setText(label.getText() + ((JButton)e.getSource()).getText());
            });
            buttonPanel.add(button);
        }



        // Botón para dar espacio
        JButton spaceButton = new JButton("Espacio");
        spaceButton.setFont(new Font("Arial", Font.BOLD, 18)); // Ampliar tamaño del texto
        spaceButton.setPreferredSize(new Dimension(190, 50)); // Ampliar tamaño del botón
        spaceButton.addActionListener((ActionEvent e) -> {
            // Agregar un espacio en el texto
            label.setText(label.getText() + " ");
        });
        buttonPanel.add(spaceButton);

        // Botón para borrar la última letra o espacio
        JButton backspaceButton = new JButton("Borrar última");
        backspaceButton.setFont(new Font("Arial", Font.BOLD, 18)); // Ampliar tamaño del texto
        backspaceButton.setPreferredSize(new Dimension(190, 50)); // Ampliar tamaño del botón
        backspaceButton.addActionListener((ActionEvent e) -> {
            String currentText = label.getText();
            if (!currentText.isEmpty()) {
                // Remover el último carácter
                label.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        buttonPanel.add(backspaceButton);

        // Añadir el panel de botones al panel principal
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Configurar y mostrar la ventana
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}