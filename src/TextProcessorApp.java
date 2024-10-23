import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextProcessorApp extends JFrame {
    private JTextField filePathField;
    private JTextArea outputArea;

    public static class EmptyWordException extends ArithmeticException {
        public EmptyWordException(String message) {
            super(message);
        }
    }

    public TextProcessorApp() {
        setTitle("Text Processor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        filePathField = new JTextField(20);
        JButton processButton = new JButton("Process Text");
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        
        processButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            try {
                String fileContent = readFile(filePath);
                String processedText = processText(fileContent);
                outputArea.setText(processedText);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "File not found: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "IO Exception: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (EmptyWordException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Enter file name:"));
        panel.add(filePathField);
        panel.add(processButton);
        panel.add(new JScrollPane(outputArea));

        add(panel);
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private String processText(String text) throws EmptyWordException {
        String[] words = text.split("\\W+");
        //words[1] = "";
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                throw new EmptyWordException("Word is empty!");
            }
            if (word.length() % 2 != 0) {
                int middleIndex = word.length() / 2;
                word = word.substring(0, middleIndex) + word.substring(middleIndex + 1);
            }
            result.append(word).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextProcessorApp().setVisible(true));
    }
}
