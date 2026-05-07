package bank.model;

/**
 * Represents a bank customer with all their account details.
 * Data format: id,name,phone,cnic,password,address,balance
 */
public class Customer {
    private int id;
    private String name;
    private String phone;
    private String cnic;
    private String password;
    private String address;
    private double balance;

    public Customer(int id, String name, String phone, String cnic,
                    String password, String address, double balance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.cnic = cnic;
        this.password = password;
        this.address = address;
        this.balance = balance;
    }

    /** Parse a CSV line into a Customer object. */
    public static Customer fromCsv(String line) {
        String[] parts = line.trim().split(",", 7);
        if (parts.length < 7) throw new IllegalArgumentException("Invalid record: " + line);
        return new Customer(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            parts[4].trim(),
            parts[5].trim(),
            Double.parseDouble(parts[6].trim())
        );
    }

    /** Serialize to CSV line. */
    public String toCsv() {
        return id + "," + name + "," + phone + "," + cnic + "," +
               password + "," + address + "," + (int) balance;
    }

    // --- Getters & Setters ---
    public int getId()                       { return id; }
    public String getName()                  { return name; }
    public String getPhone()                 { return phone; }
    public String getCnic()                  { return cnic; }
    public String getPassword()              { return password; }
    public String getAddress()               { return address; }
    public double getBalance()               { return balance; }
    public void setAddress(String address)   { this.address = address; }
    public void setBalance(double balance)   { this.balance = balance; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + name + "'}";
    }
}
