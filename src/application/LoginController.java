package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * javafx controller for for login.fxml
 * 
 * @author Steve Rubin
 */
public class LoginController {
   Database db;
   boolean firstlaunch = false;
   
   @FXML
   TextField passwordField;
   
	@FXML
	private SceneController control = new SceneController();

   public LoginController() {
      System.out.println("LoginController constructor called");
      db = new Database();

      try {
         Statement stmt = db.getConnection().createStatement();
         String sql = "SELECT key,value FROM config";
         ResultSet rs = stmt.executeQuery(sql);

         while (rs.next()) {
            String key = rs.getString("key");
            String value = rs.getString("value");
            System.out.println("key = " + key);
            System.out.println("value = " + value);
            if (key.equals("firstlaunch") && value.equals("1")) { // This firstlaunch thing is a bit hackish, sorry
               System.out.println("firstlaunch = " + firstlaunch);
               firstlaunch = true;
            }
            System.out.println("firstlaunch = " + firstlaunch);
         }
         stmt.close();

         if (firstlaunch) {
            System.out.println("First launch detected, please change your password");
         } else {
            System.out.println("Not our first time launching, please enter your password");

         }
      } catch (SQLException e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }

   }

   /**
    * Event handler for the Login button*
    * 
    * @param e event
    */

   public void loginButtonPressed(Event e) {
      System.out.printf("Login button pressed, with Password of %s\n", passwordField.getText());
      // get the password from the database table config, key=password
      try {

         PreparedStatement pstmt = db.getConnection()
               .prepareStatement("SELECT value FROM config where key='password' and value = ?");
         pstmt.setString(1, passwordField.getText());

         ResultSet rs = pstmt.executeQuery();

         if (rs.next()) {
            System.out.println("Password is correct");
            // see if firstlaunch is set to 1
            // if so changeScene to resetpw.fxml
            // else changeScene to mainmenu.fxml
            if (firstlaunch) {
               System.out.println("First launch detected, please change your password");
               pstmt.close();
               control.changeScene(e, "resetpw.fxml");
            } else {
               System.out.println("Not our first time launching, please enter your password");
               pstmt.close();
               control.changeScene(e, "mainmenu.fxml");
            }
         } else {
            System.out.println("Password is incorrect");
         }

      } catch (SQLException sqle) {
         System.err.println(sqle.getClass().getName() + ": " + sqle.getMessage());
         System.exit(0);
      }
   }

   /**
    * Event handler for the Login button*
    * 
    * @param e event
    */
   public void resetButtonPressed(Event e) {
      System.out.println("Reset button pressed");
   }
}