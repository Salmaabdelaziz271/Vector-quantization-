import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlockInfo {
    private JPanel panel1;
    private JButton doneButton;
    private JTextField BlocksNum;
    private JTextField BlockWidth;
    private JTextField BlockHeight;

    JPanel getPanel1(){
        return panel1;
    }

    public JTextField getBlocksNum() {
        return BlocksNum;
    }

    public JTextField getBlockWidth() {
        return BlockWidth;
    }

    public JTextField getBlockHeight() {
        return BlockHeight;
    }

    public BlockInfo() {
        BlocksNum.getDocument().addDocumentListener(new TextFieldDocumentListener());
        BlockWidth.getDocument().addDocumentListener(new TextFieldDocumentListener());
        BlockHeight.getDocument().addDocumentListener(new TextFieldDocumentListener());
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectImage selectImage = new SelectImage(BlockWidth , BlockHeight , BlocksNum);
                JFrame frame = new JFrame("Select Image");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(selectImage.getPanel1());
                frame.setSize(1200, 800);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
                int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
                frame.setLocation(centerX, centerY);
                frame.setVisible(true);
            }
        });
    }



    private void updateButtonState() {
        boolean enableButton = isValidNumber(BlocksNum.getText())
                && isValidNumber(BlockWidth.getText())
                && isValidNumber(BlockHeight.getText());

        doneButton.setEnabled(enableButton);
    }

    private boolean isValidNumber(String text) {
        if (text.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private class TextFieldDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateButtonState();
        }
    }

}
