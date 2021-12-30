package com.umarabdul.jmailer;

/**
 * Custom exception class for JMailer.
 * @author Umar Abdul
 * @version 1.0
 * Date: 27/Dec/2021
 */

public class JMailerError extends Exception {
  
  private String message;

  /**
   * Constructor.
   * @param message The error message.
   */
  public JMailerError(String message){
    this.message = message;
  }

  @Override
  public String getMessage(){
    return message;
  }
}
