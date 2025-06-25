import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//key listener is used so that i can listen to key presses(typing)
public class MorseCodeTranslatorGUI extends JFrame implements KeyListener {
    private JTextArea textInputArea, morseCodeArea;
    private MorseCodeController morseCodeController;

    public MorseCodeTranslatorGUI() {
        super("Morse Code Translator");

        setSize(new Dimension(540, 760));

        setResizable(false);

        setLayout(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.darkGray);

        //places GUI in the centre of screen when ran
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();
        addGuiComponents();
    }

    private void addGuiComponents() {
        //title label
        JLabel titleLabel = new JLabel("Morse Code Translator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 540, 100);

        JLabel textInputLabel = new JLabel("Text:");
        textInputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        textInputLabel.setForeground(Color.white);
        textInputLabel.setBounds(20, 100, 200, 30);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.BOLD, 18));

        //so that we are listening to the key pressed when we are typing
        textInputArea.addKeyListener(this);

        //simulates padding of 10px in the text area
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // makes it such that the text goes to next line once the end of text area is reached
        textInputArea.setLineWrap(true);

        // so that when the text wraps ,it doesnt split off
        textInputArea.setWrapStyleWord(true);

        //adding scroll to the text area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20, 132, 484, 236);

        //morse code input
        JLabel morseCodeInputLabel = new JLabel("Morse Code:");
        morseCodeInputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        morseCodeInputLabel.setForeground(Color.white);
        morseCodeInputLabel.setBounds(20, 390, 200, 30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog", Font.BOLD, 18));
        morseCodeArea.setEditable(false);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);

        //scroll for morse code
        JScrollPane morseInputScroll = new JScrollPane(morseCodeArea);
        morseInputScroll.setBounds(20, 430, 484, 236);


        //play sound button
        JButton playSoundButton=new JButton("Play Sound");
        playSoundButton.setBounds(210,680,100,30);
        playSoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //disable the button so the sound doesnt get interrupted
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //attempt to play the morse code sound
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");

                            morseCodeController.playsound(morseCodeMessage);
                        } catch (LineUnavailableException lineUnavailableException) {
                        lineUnavailableException.printStackTrace();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        finally {
                            playSoundButton.setEnabled(true);
                        }
                    }

                });
                playMorseCodeThread.start();
            }
        });


        //add to GUI
        add(titleLabel);
        add(textInputLabel);
        add(textInputScroll);
        add(morseCodeInputLabel);
        add(morseInputScroll);
        add(playSoundButton);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //ignore shift
        if (e.getKeyCode() != KeyEvent.VK_SHIFT) {
            String inputText=textInputArea.getText();

            //update the GUI with the translated text
            morseCodeArea.setText(morseCodeController.translateToMorse(inputText));
        }
    }
}
