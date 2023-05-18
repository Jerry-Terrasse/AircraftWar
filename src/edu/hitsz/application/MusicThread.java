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

public class MusicThread extends Thread {


    //音频文件名
    private String filename;
    private AudioFormat audioFormat;
    private byte[] samples;

    private volatile boolean paused = false;
    private volatile boolean stopped = false;
    private boolean needLoop = false;

    public MusicThread(String filename, boolean needLoop) {
        //初始化filename
        this.filename = filename;
        this.needLoop = needLoop;
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

    public void play(InputStream source) throws InterruptedException {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
//        System.out.println(audioFormat.getFrameSize() + "," + audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SoureDataLine是可以写入数据的数据行
        SourceDataLine dataLine = null;
        //获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1 && !this.isInterrupted()) {
                synchronized (stoppedLock) {
                    if (stopped) {
                        break;
                    }
                }
//                System.out.println(filename + " 111");
                synchronized (pausedLock) {
                    while (paused && !this.isInterrupted()) {
//                        System.out.println(filename + " waiting");
                        pausedLock.wait();
//                        System.out.println(filename + " waiting end");
                    }
                }
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

        dataLine.drain();
        dataLine.close();

    }

    final Object pausedLock = new Object();
    public void setPaused(boolean paused) {
        synchronized (pausedLock) {
            this.paused = paused;
            pausedLock.notifyAll();
        }
    }

    final Object stoppedLock = new Object();
    public void setStopped() {
        synchronized (stoppedLock) {
            this.stopped = true;
            stoppedLock.notifyAll();
        }
        setPaused(false); // to interrupt the thread if it is in paused state
    }

    @Override
    public void run() {
        InputStream stream = new ByteArrayInputStream(samples);
        try {
            do {
//                System.out.println(filename + " is playing" + this.isInterrupted());
                play(stream);
                stream = new ByteArrayInputStream(samples);
                synchronized (stoppedLock) {
                    if(stopped) {
                        break;
                    }
                }
            } while (!this.isInterrupted() && needLoop);
        } catch (InterruptedException ie) {
            System.out.println(filename + " is interrupted");
        }
    }
}


