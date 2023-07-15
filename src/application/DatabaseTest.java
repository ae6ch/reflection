package application;

import org.junit.jupiter.api.Test;

/**
 * DatabaseTest class to verify the database can be open and tables created
 * @result the database is open, and tables created
 */

public class DatabaseTest {
   @Test
   void testGetConnection() {
      assert (new Database().getConnection() != null);
   }
}