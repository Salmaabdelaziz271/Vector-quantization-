import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SelectImage {
    private JPanel panel1;
    private JButton compressButton;
    private JButton selectImageButton;
    private JButton newImageButton;
    private JLabel selectedPath;
    private JLabel newPath;
    private ImageIcon selectedImageIcon;
    private JTextField blockWidthField;
    private JTextField blockHeightField;
    private JTextField blocksNumField;

    private ImageLoad imageLoad ;
    private String imagePath;



    JPanel getPanel1(){
        return panel1;
    }
    public SelectImage(JTextField blockWidthField, JTextField blockHeightField , JTextField blocksNumField) {
        this.blockWidthField = blockWidthField;
        this.blockHeightField= blockHeightField;
        this.blocksNumField = blocksNumField;

        imageLoad = new ImageLoad();

        selectImageButton.setPreferredSize(new java.awt.Dimension(500, 400));
        newImageButton.setPreferredSize(new java.awt.Dimension(500, 400));


        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });

        compressButton.setEnabled(false);
        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canCompress()){
                    VectorQuantization v = new VectorQuantization();
                    try {
                        int blocksNum = Integer.parseInt(blocksNumField.getText());
                        int blockWidth = Integer.parseInt(blockWidthField.getText());
                        int blockHeight = Integer.parseInt(blockHeightField.getText());

                        imageLoad.newImage(
                                imageLoad.replaceImage(
                                        v.getFinalBlocks(blocksNum, blockWidth, blockHeight, imagePath),
                                        imageLoad.divideIntoBlocks(blockWidth, blockHeight, imagePath)
                                ),
                                "newImage123.jpg"
                        );

                        newPath.setText("newImage123.jpg");
                        imagePath = "newImage123.jpg";

                        newImageButton.setText("");
                        ImageIcon imageIcon = new ImageIcon(imagePath);
                        Image image = imageIcon.getImage().getScaledInstance(
                                newImageButton.getWidth(),
                                newImageButton.getHeight(),
                                Image.SCALE_SMOOTH
                        );

                        newImageButton.setIcon(new ImageIcon(image));
                        newPath.setText(imagePath);
                    } catch (NumberFormatException a) {
                        // Handle the case where parsing the integers fails
                        a.printStackTrace();
                    }

                }
                else{
                    JFrame frame = new JFrame("BlockInfo");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    BlockInfo blockInfo = new BlockInfo();
                    frame.setContentPane(blockInfo.getPanel1());
                    frame.setSize(700, 400);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
                    int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
                    frame.setLocation(centerX, centerY);
                    frame.setVisible(true);

                    blockInfo.getBlockWidth().setText(blockWidthField.getText());
                    blockInfo.getBlockHeight().setText(blockHeightField.getText());
                    blockInfo.getBlocksNum().setText(blocksNumField.getText());
                }
            }
        });
    }


    boolean canCompress(){


        try {
            int blockWidth = Integer.parseInt(blockWidthField.getText());
            int blockHeight = Integer.parseInt(blockHeightField.getText());
            int imageWidth;
            int imageHeight;

            imageLoad.getImagePixels(imagePath);
            imageWidth = imageLoad.width;
            imageHeight = imageLoad.height;

            return imageWidth % blockWidth == 0 && imageHeight % blockHeight == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(panel1);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String selectedImagePath = selectedFile.getAbsolutePath();
            selectedPath.setText(selectedImagePath);
            imagePath = selectedImagePath;

            selectImageButton.setText("");
            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage().getScaledInstance(
                    selectImageButton.getWidth(), selectImageButton.getHeight(), Image.SCALE_SMOOTH);

            selectImageButton.setIcon(new ImageIcon(image));
            compressButton.setEnabled(true);
        }
    }
}
