/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GAMER
 */
/*File AudioRecorder02.java
Copyright 2003, Richard G. Baldwin

This program demonstrates the capture of audio
data from a microphone into an audio file.

A GUI appears on the screen containing the
following buttons:
  Capture
  Stop
Data capture stops and the output file is closed
when the user clicks the Stop button.


************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import java.rmi.*;
import java.awt.*;
import java.io.*;
import java.io.File;
import java.rmi.server.*;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;



public class Voice_mail extends UnicastRemoteObject
{
	  JFrame f1=new JFrame();
	  AudioFormat audioFormat;
	  String soundfile = "VoiceMail.wav"; 
	  TargetDataLine targetDataLine;
	  final JButton start_rec =new JButton("Start Recording");
	  final JButton stop_rec = new JButton("Stop Recording");
	  final JButton send_mail=new JButton("Send Voice Mail");
	  final JPanel main_p = new JPanel();
	  final JPanel main_2p = new JPanel();
	  static BroadcastServer bs;
	  int selectedIndex = 0;
	 /*
	 public static void main( String args[])
	{
		new Voice_mail();
	}//end main
	*/
	
  public Voice_mail(final BroadcastServer bs, int Index) throws RemoteException
  {
    start_rec.setEnabled(true);
    stop_rec.setEnabled(false);
	try
	{
	this.bs = bs;
	this.selectedIndex = Index;
	}
	catch(Exception ex)
	{
	
	}
    //Register anonymous listeners
    start_rec.addActionListener
	(
      new ActionListener()
	  {
        public void actionPerformed(ActionEvent e)
		{
          start_rec.setEnabled(false);
          stop_rec.setEnabled(true);
          //Capture input data from the
          // microphone until the Stop button is
          // clicked.
          recording_voice();
        }//end actionPerformed
      }//end ActionListener
    );//end addActionListener()

	send_mail.addActionListener
	(
      new ActionListener()
	  {
        public void actionPerformed(ActionEvent e)
		{	
		  byte buffer[] = null;
          start_rec.setEnabled(false);
          stop_rec.setEnabled(false);
          try
			{	
				FileInputStream fis = new FileInputStream(soundfile);	// open the file
				if (fis!=null)
				{
					System.out.println("\n\tFILE FOUND Successfully\n\tReady to Send!!!!!");
					buffer = new byte[fis.available()];		// create the buffer of size of file
					fis.read(buffer);		// read the file in bytes and store the bytes in buffer
					try
					{
						bs.sendsound(soundfile, buffer, selectedIndex);		// calling the remote mehtod used to send the file to the other connected user.
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(null,"Error Occured during send  the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		
			catch(IOException ioe)
			{
				String ACK = "FAILED";
				buffer = ACK.getBytes();
				System.out.println("\n\tFILE NOT FOUND ERROR\n\tTRY Again!!!!!");
			}
          
        }//end actionPerformed
      }//end ActionListener
    );//end addActionListener()
	
	
	
    stop_rec.addActionListener
	(
      new ActionListener()
	  {
        public void actionPerformed(ActionEvent e)
		{
          start_rec.setEnabled(true);
          stop_rec.setEnabled(false);
          //Terminate the capturing of input data
          // from the microphone.
          targetDataLine.stop();
          targetDataLine.close();
        }//end actionPerformed
      }//end ActionListener
    );//end addActionListener()
    main_p.setBackground(Color.orange);
    main_2p.setBackground(Color.orange);
     main_p.add(start_rec);
     main_p.add(stop_rec);
     main_2p.add(send_mail);
    //Put the buttons in the JFrame
   // getContentPane().add(start_rec);
    f1.getContentPane().setBackground(Color.LIGHT_GRAY);
    f1.getContentPane().add(main_p);
    f1.getContentPane().add(main_2p);
    //Finish the GUI and make visible
    f1.getContentPane().setLayout(new FlowLayout());
    f1.setTitle("Voice Mail");
    f1.setDefaultCloseOperation(f1.DISPOSE_ON_CLOSE);
    f1.setBounds(600,300,300,120);
  //  setSize(300,120);
    f1.setVisible(true);
  }//end constructor

  //This method captures audio input from a
  // microphone and saves it in an audio file.
  private void recording_voice()
  {
    try
	{
      //Get things set up for capture
      audioFormat = sound_format();
      DataLine.Info voice_data =new DataLine.Info(TargetDataLine.class,audioFormat);
      targetDataLine = (TargetDataLine)AudioSystem.getLine(voice_data);

      //Create a thread to capture the microphone
      // data into an audio file and start the
      // thread running.  It will run until the
      // Stop button is clicked.  This method
      // will return after starting the thread.
      new CaptureThread().start();
    }
	catch (Exception e) 
	{
      e.printStackTrace();
      System.exit(0);
    }//end catch
  }//end captureAudio method

  //This method creates and returns an
  // AudioFormat object for a given set of format
  // parameters.  If these parameters don't work
  // well for you, try some of the other
  // allowable parameter values, which are shown
  // in comments following the declarations.
  private AudioFormat sound_format(){
    float sampleRate = 8000.0F; // can be any of these 8000,11025,16000,22050,44100
    int sampleSizeInBits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = false;
    return new AudioFormat(sampleRate,sampleSizeInBits, channels,signed,bigEndian);
  }

//Inner class to capture data from microphone
// and write it to an output audio file.
class CaptureThread extends Thread
{
  public void run()
  {
    AudioFileFormat.Type file_format = null; //declaring format
    File voice_file =new File(soundfile);
    //Set the file type and the file extension
      file_format = AudioFileFormat.Type.WAVE;
      
    try
	{
      targetDataLine.open(audioFormat);
      targetDataLine.start();
      AudioSystem.write(new AudioInputStream(targetDataLine),file_format,voice_file);
    }
    catch (Exception e)
	{
          {

          System.out.println("!!! Voice cannot be send !!!");
          }
	}//end catch

   }//end run
  }//end inner class CaptureThread
}//end outer class AudioRecorder02.java
