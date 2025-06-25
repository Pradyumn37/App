import javax.sound.sampled.*;
import java.util.*;

//this class will handle the logic for our GUI
public class MorseCodeController {
    //we will use hashmap to translate user input into morse code
    // we will use the letter as the key and the morse code as the value so we can look up the value of letter easily

    private HashMap<Character,String> morseCodeMap;

    public MorseCodeController()
    {   morseCodeMap=new HashMap<>();
        // Uppercase letters
        morseCodeMap.put('A', ".-");
        morseCodeMap.put('B', "-...");
        morseCodeMap.put('C', "-.-.");
        morseCodeMap.put('D', "-..");
        morseCodeMap.put('E', ".");
        morseCodeMap.put('F', "..-.");
        morseCodeMap.put('G', "--.");
        morseCodeMap.put('H', "....");
        morseCodeMap.put('I', "..");
        morseCodeMap.put('J', ".---");
        morseCodeMap.put('K', "-.-");
        morseCodeMap.put('L', ".-..");
        morseCodeMap.put('M', "--");
        morseCodeMap.put('N', "-.");
        morseCodeMap.put('O', "---");
        morseCodeMap.put('P', ".--.");
        morseCodeMap.put('Q', "--.-");
        morseCodeMap.put('R', ".-.");
        morseCodeMap.put('S', "...");
        morseCodeMap.put('T', "-");
        morseCodeMap.put('U', "..-");
        morseCodeMap.put('V', "...-");
        morseCodeMap.put('W', ".--");
        morseCodeMap.put('X', "-..-");
        morseCodeMap.put('Y', "-.--");
        morseCodeMap.put('Z', "--..");

        // Digits
        morseCodeMap.put('0', "-----");
        morseCodeMap.put('1', ".----");
        morseCodeMap.put('2', "..---");
        morseCodeMap.put('3', "...--");
        morseCodeMap.put('4', "....-");
        morseCodeMap.put('5', ".....");
        morseCodeMap.put('6', "-....");
        morseCodeMap.put('7', "--...");
        morseCodeMap.put('8', "---..");
        morseCodeMap.put('9', "----.");

        // Punctuation
        morseCodeMap.put('.', ".-.-.-");
        morseCodeMap.put(',', "--..--");
        morseCodeMap.put('?', "..--..");
        morseCodeMap.put('\'', ".----.");
        morseCodeMap.put('!', "-.-.--");
        morseCodeMap.put('/', "-..-.");
        morseCodeMap.put('(', "-.--.");
        morseCodeMap.put(')', "-.--.-");
        morseCodeMap.put('&', ".-...");
        morseCodeMap.put(':', "---...");
        morseCodeMap.put(';', "-.-.-.");
        morseCodeMap.put('=', "-...-");
        morseCodeMap.put('+', ".-.-.");
        morseCodeMap.put('-', "-....-");
        morseCodeMap.put('_', "..--.-");
        morseCodeMap.put('"', ".-..-.");
        morseCodeMap.put('$', "...-..-");
        morseCodeMap.put('@', ".--.-.");
        morseCodeMap.put(' ', "/");

        // Lowercase letters (reuse uppercase mappings)
        for (char c = 'A'; c <= 'Z'; c++) {
            morseCodeMap.put(Character.toLowerCase(c), morseCodeMap.get(c));
        }
    }
    public String translateToMorse(String textToTranslate)
    {
        StringBuilder translatedText=new StringBuilder();
        //translate the letter to morse
        for (Character letter: textToTranslate.toCharArray())
        {
            translatedText.append(morseCodeMap.get(letter)+" ");
        }
        return translatedText.toString();
    }
    //morseMessage - contains the text after converting to morse code
    public void playsound(String[] morseMessage) throws LineUnavailableException, InterruptedException {
        //audio format specifies the sound properties
        AudioFormat audioFormat=new AudioFormat(44100,16,1,true,false);

        //create the data line(to play incoming audio data)
        DataLine.Info dataLineInfo=new DataLine.Info(SourceDataLine.class,audioFormat);
        SourceDataLine sourceDataLine=(SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        //duration of the sound to be played
        int dotDuration=200;
        int dashDuration=(int)(1.5*dotDuration);
        int slashDuration=2*dashDuration;

        for(String pattern:morseMessage)
        {
            //play the letter sound
            for(char c:pattern.toCharArray())
            {
                if(c =='.')
                {
                    playBeep(sourceDataLine,dotDuration);
                    Thread.sleep(dotDuration);

                }
                else if(c=='-')
                {
                    playBeep(sourceDataLine,dashDuration);
                    Thread.sleep(dotDuration);

                } else if(c=='/')
                {

                    Thread.sleep(slashDuration);

                }
            }

            //wait a bit before playing the next sequence
            Thread.sleep(dotDuration);
        }
        //close output audio line
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    //send audio data to be played to the data line
    private void playBeep(SourceDataLine line,int duration)
    {
        //create audio data
        byte[] data=new byte[duration * 44100/1000];
        for(int i=0;i<data.length;i++)
        {
            //calculate the angle of the sine wave for the current sample based on the sample rate and frequency
            double angle=i / (44100.0/440) * 2 * Math.PI;

            //calculate the amplitude of the sine wave at the current angle and scale it to fit within the range of a signed byte (-128,-127)
            //also in the content of audio processing , a signed bytes is often used to represent audio data because it can represent both positive and negative amplitudes of sound waves
            data[i]=(byte)(Math.sin(angle)*127);

        }
        //write the audio data in teh data line to be played
        line.write(data,0,data.length);
    }

}
