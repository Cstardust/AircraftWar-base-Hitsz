package edu.hitsz.application;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;

public class MusicThread extends Thread {

    //音频文件名
    private String filename;
    private AudioFormat audioFormat;
    private byte[] samples;

    private boolean isLoop = false;     //  是否循环
    private boolean isForEnd = false;   //  结束音乐的特殊标志
    private boolean isLapse = false;    //  发出音乐的物体是否已经失效
    private static boolean isEnd = false;   //  游戏是否结束：结束则所有音乐都该停止
    private static boolean isMusic = false; //  游戏是否开启音效

    public boolean getIsLapse() {return isLapse;}
    public void setLapse(boolean flag) {isLapse = flag;}
    public boolean getIsForEnd() {return isForEnd;}
    public  boolean getIsLoop() {
        return isLoop;
    }
    public void setIsLoop(boolean flag) {isLoop = flag;}
    public static boolean getIsMusic() {
        return isMusic;
    }
    public static void setIsMusic(boolean flag) {
        MusicThread.isMusic = flag;
    }
    public static void setIsEnd(boolean flag)
    {
        isEnd = flag;
    }
    public static boolean getIsEnd()
    {
        return isEnd;
    }

    public MusicThread(String filename,boolean isloop,boolean isforend) {
        //初始化filename
        this.filename = filename;
        this.isLoop = isloop;
        this.isForEnd = isforend;
        reverseMusic();
    }

    public void reverseMusic() {
        try {
            //定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            //用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SoureDataLine是可以写入数据的数据行
        SourceDataLine dataLine = null;
        //获取受数据行支持的音频格式DataLine.info
        Info info = new Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataLine.start();
        do
        {
            try {
                int numBytesRead = 0;
                while (numBytesRead != -1&&( ((!getIsLapse())&&(!getIsEnd())) || getIsForEnd())) {
                    //从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                    numBytesRead =
                            source.read(buffer, 0, buffer.length);
                    //通过此源数据行将数据写入混频器
                    if (numBytesRead != -1) {
                        dataLine.write(buffer, 0, numBytesRead);
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                source.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(!getIsEnd()&&getIsLoop()&&(!getIsLapse()));   //  没结束 且 要求循环
        dataLine.drain();
        dataLine.close();
    }

    @Override
    public void run() {
        InputStream stream = new ByteArrayInputStream(samples);
        play(stream);
    }
}


