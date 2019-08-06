import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;

public class BlobDriver extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField inputField;
    private JLabel modelPicLabel;
    private JLabel textLabel;
    private JPanel buttonPanel;
    private JPanel centerPanel;
    private Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    public BigBlob blob;

    public BlobDriver() {

        contentPane = new JPanel();
        buttonCancel = new JButton("Cancel");
        buttonOK = new JButton("Submit");

        inputField = new JTextField();
        inputField.setPreferredSize( new Dimension(150,20));

        modelPicLabel = new JLabel("The picture will show up here  ");
        modelPicLabel.setPreferredSize( new Dimension(250,200));
        modelPicLabel.setBorder(loweredEtched);

        textLabel= new JLabel("Please enter the name of the picture, i.e. Corvette");
     //   textLabel.setPreferredSize(new Dimension(300,300));
        buttonPanel = new JPanel();
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(200,200));

        this.add(contentPane);
        this.setPreferredSize(new Dimension(550,250));
        this.setTitle("Big Blob");
       rootPane.setDefaultButton(buttonOK);


        contentPane.setLayout(new BorderLayout(5,5));
       // contentPane.add(textArea, BorderLayout.NORTH);
        contentPane.add(modelPicLabel, BorderLayout.EAST);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new BorderLayout(5,5));
       // contentPane.add(textLabel, BorderLayout.NORTH);
        centerPanel.add(textLabel, BorderLayout.CENTER);
        centerPanel.add(inputField, BorderLayout.SOUTH);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCancel);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        try {
            blob = new BigBlob(inputField.getText());

            modelPicLabel.setIcon(blob.blobIcon);
            modelPicLabel.setText("");
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
    }

    private void onCancel() {

        dispose();
    }

    public static void main(String[] args) {
        BlobDriver driver = new BlobDriver();
        driver.pack();
        driver.setVisible(true);
        driver.setLocationRelativeTo(null);
        driver.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
