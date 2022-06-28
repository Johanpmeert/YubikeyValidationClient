# YubikeyValidationClient
Tests a Yubikey push for validity

This support library can test if a String is in fact a valid Yubikey push

Use of the test library:
```
import com.johanpmeert.YubikeyValidationClient;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("YubiKey test");
        System.out.println("------------");
        Scanner input = new Scanner(System.in);
        System.out.print("Push Yubikey: ");
        String OTP = input.nextLine();
        YubikeyValidationClient client = new YubikeyValidationClient(OTP);
        System.out.print("Yubikey serial " + client.getSerial() + " reports ");
        if (client.isValidResponse()) {
            System.out.println("OTP valid");
        } else {
            System.out.println("OTP invalid");
        }
    }
}
```
