package model.Customer;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private final String emailPattern = "^(.+)@(.+).(.+)$";

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = validateEmail(email);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }
    public String validateEmail(String email) {
        Pattern pattern = Pattern.compile(emailPattern);
        if(!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("This is an invalid email!");
        }
        return email;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n" + "Email: " + this.email;
    }
}
