package bank.service;

import bank.model.Customer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles all persistence for Customer records.
 * Reads/writes a simple CSV file: id,name,phone,cnic,password,address,balance
 *
 * Thread-safety: synchronized on the instance — suitable for a single-user
 * desktop app. For multi-user use, swap this with a proper database.
 */
public class CustomerRepository {

    private final Path dataFile;

    public CustomerRepository(Path dataFile) {
        this.dataFile = dataFile;
        ensureFileExists();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Return all customers. */
    public synchronized List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(dataFile)) {
                if (!line.isBlank()) {
                    try {
                        list.add(Customer.fromCsv(line));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
        }
        return list;
    }

    /** Find a customer by ID and password (login check). */
    public synchronized Optional<Customer> findByIdAndPassword(String id, String password) {
        try {
            int numId = Integer.parseInt(id.trim());
            return findAll().stream()
                    .filter(c -> c.getId() == numId && c.getPassword().equals(password.trim()))
                    .findFirst();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /** Find a customer by ID only. */
    public synchronized Optional<Customer> findById(int id) {
        return findAll().stream().filter(c -> c.getId() == id).findFirst();
    }

    /**
     * Add a new customer.  Auto-assigns the next available ID.
     * @return the newly assigned ID
     */
    public synchronized int save(String name, String phone, String cnic,
                                  String password, String address, double balance) throws IOException {
        int nextId = findAll().stream().mapToInt(Customer::getId).max().orElse(0) + 1;
        Customer c = new Customer(nextId, name, phone, cnic, password, address, balance);
        Files.writeString(dataFile, c.toCsv() + System.lineSeparator(), StandardOpenOption.APPEND);
        return nextId;
    }

    /** Persist a modified customer back to disk. */
    public synchronized void update(Customer updated) throws IOException {
        List<Customer> all = findAll();
        StringBuilder sb = new StringBuilder();
        for (Customer c : all) {
            if (c.getId() == updated.getId()) {
                sb.append(updated.toCsv());
            } else {
                sb.append(c.toCsv());
            }
            sb.append(System.lineSeparator());
        }
        Files.writeString(dataFile, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    /** Remove a customer by ID.  Returns true if found and deleted. */
    public synchronized boolean delete(int id) throws IOException {
        List<Customer> all = findAll();
        boolean found = all.removeIf(c -> c.getId() == id);
        if (found) {
            StringBuilder sb = new StringBuilder();
            for (Customer c : all) sb.append(c.toCsv()).append(System.lineSeparator());
            Files.writeString(dataFile, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        return found;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void ensureFileExists() {
        try {
            if (!Files.exists(dataFile)) {
                Files.createDirectories(dataFile.getParent());
                Files.createFile(dataFile);
            }
        } catch (IOException e) {
            System.err.println("Could not create data file: " + e.getMessage());
        }
    }
}
