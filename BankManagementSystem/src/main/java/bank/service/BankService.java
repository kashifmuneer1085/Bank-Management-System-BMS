package bank.service;

import bank.model.Customer;

import java.io.IOException;
import java.util.Optional;

/**
 * Business-logic layer for all banking operations.
 * All validation lives here so the UI classes stay thin.
 */
public class BankService {

    private final CustomerRepository repo;

    public BankService(CustomerRepository repo) {
        this.repo = repo;
    }

    // -------------------------------------------------------------------------
    // Authentication
    // -------------------------------------------------------------------------

    /**
     * Attempt login.  Returns the Customer on success.
     * @throws AuthException if credentials are wrong.
     */
    public Customer login(String id, String password) throws AuthException {
        Optional<Customer> customer = repo.findByIdAndPassword(id, password);
        if (customer.isEmpty()) throw new AuthException("Invalid ID or Password.");
        return customer.get();
    }

    // -------------------------------------------------------------------------
    // Account management
    // -------------------------------------------------------------------------

    /**
     * Open a new account.
     * @return the auto-assigned customer ID
     */
    public int openAccount(String name, String phone, String cnic,
                            String password, String address, double initialBalance) throws ServiceException {
        validate(name, "Name");
        validate(phone, "Phone");
        validate(cnic, "CNIC");
        validate(password, "Password");
        if (initialBalance < 0) throw new ServiceException("Initial balance cannot be negative.");
        try {
            return repo.save(name, phone, cnic, password, address, initialBalance);
        } catch (IOException e) {
            throw new ServiceException("Could not save account: " + e.getMessage());
        }
    }

    /**
     * Delete the account of the currently logged-in customer.
     */
    public void deleteAccount(Customer customer) throws ServiceException {
        try {
            boolean deleted = repo.delete(customer.getId());
            if (!deleted) throw new ServiceException("Account not found.");
        } catch (IOException e) {
            throw new ServiceException("Could not delete account: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Transactions
    // -------------------------------------------------------------------------

    /**
     * Deposit amount into customer's account.
     * @return new balance
     */
    public double deposit(Customer customer, double amount) throws ServiceException {
        if (amount <= 0) throw new ServiceException("Deposit amount must be positive.");
        customer.setBalance(customer.getBalance() + amount);
        persist(customer);
        return customer.getBalance();
    }

    /**
     * Withdraw amount from customer's account.
     * @return new balance
     */
    public double withdraw(Customer customer, double amount) throws ServiceException {
        if (amount <= 0) throw new ServiceException("Withdrawal amount must be positive.");
        if (amount > customer.getBalance())
            throw new ServiceException("Insufficient funds. Available: " + (int) customer.getBalance());
        customer.setBalance(customer.getBalance() - amount);
        persist(customer);
        return customer.getBalance();
    }

    // -------------------------------------------------------------------------
    // Profile management
    // -------------------------------------------------------------------------

    public void updateAddress(Customer customer, String newAddress) throws ServiceException {
        validate(newAddress, "Address");
        customer.setAddress(newAddress);
        persist(customer);
    }

    public void updatePassword(Customer customer, String newPassword) throws ServiceException {
        validate(newPassword, "Password");
        customer.setPassword(newPassword);
        persist(customer);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void persist(Customer customer) throws ServiceException {
        try {
            repo.update(customer);
        } catch (IOException e) {
            throw new ServiceException("Could not save changes: " + e.getMessage());
        }
    }

    private void validate(String value, String fieldName) throws ServiceException {
        if (value == null || value.isBlank())
            throw new ServiceException(fieldName + " cannot be empty.");
        if (value.contains(","))
            throw new ServiceException(fieldName + " must not contain commas.");
    }

    // -------------------------------------------------------------------------
    // Custom exceptions
    // -------------------------------------------------------------------------

    public static class AuthException extends Exception {
        public AuthException(String message) { super(message); }
    }

    public static class ServiceException extends Exception {
        public ServiceException(String message) { super(message); }
    }
}
