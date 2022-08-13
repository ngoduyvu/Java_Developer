package service;

import model.Customer.Customer;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerService {
    private static CustomerService SINGLETON = new CustomerService();
    private Collection<Customer> customers;

    private CustomerService() {
        this.customers = new ArrayList<>();
    }

    public static CustomerService getSINGLETON() {
        return SINGLETON;
    }

    public void addCustomer(String firstName, String lastName, String email) {
        customers.add(new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String email) {
        for (Customer customer: customers) {
            if(customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomer() {
        return customers;
    }

}
