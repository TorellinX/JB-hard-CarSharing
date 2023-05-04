package carsharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputManager {

  static String getInput() {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    while (true) {
      try {
        line = reader.readLine().strip();
        if (line == null) {
          System.out.println("Error!");
          continue;
        }
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
      return line;
    }
    return null;
  }

  static int getIntInput() {
    int intInput;
    while (true) {
      String line = getInput();
      try {
        assert line != null;
        intInput = Integer.parseInt(line);
        return intInput;
      } catch (NumberFormatException e) {
        System.out.println("Wrong format. Please enter only a number");
      }
    }
  }

}
