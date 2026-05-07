# 🏦 SecureBank — Bank Management System

A desktop banking application built with **Java Swing**, featuring a clean layered architecture and persistent file-based storage. Designed to run seamlessly in **IntelliJ IDEA**.

---

## 📸 Features

| Feature | Description |
|---|---|
| 🔑 **Customer Login** | Secure ID + password authentication |
| ➕ **Open Account** | Register new customers with auto-assigned IDs |
| 💰 **Deposit** | Add funds to your account |
| 💸 **Withdraw** | Withdraw with insufficient-funds protection |
| 📊 **Check Balance** | View current account balance |
| 📋 **View Details** | See full profile: name, phone, CNIC, address, balance |
| 🏠 **Update Address** | Change registered address |
| 🔒 **Change Password** | Update login password securely |
| 🗑️ **Delete Account** | Permanently remove account with confirmation |

---

## 🗂️ Project Structure

```
BankManagementSystem/
├── .idea/                          # IntelliJ project files
│   └── runConfigurations/
│       └── Run_BankApp.xml         # Pre-configured run config
├── data/
│   └── customers.csv               # Persistent data store
├── src/
│   └── main/java/bank/
│       ├── Main.java               # ▶ Entry point
│       ├── model/
│       │   └── Customer.java       # Data model
│       ├── service/
│       │   ├── BankService.java    # Business logic & validation
│       │   └── CustomerRepository.java  # All file I/O
│       ├── ui/
│       │   ├── WelcomeFrame.java   # Home screen
│       │   ├── LoginFrame.java     # Login screen
│       │   ├── OpenAccountFrame.java   # New account form
│       │   └── DashboardFrame.java # Main menu after login
│       └── util/
│           └── UIHelper.java       # Shared UI styles & helpers
└── BankManagementSystem.iml
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 11** or higher ([Download JDK](https://adoptium.net))
- **IntelliJ IDEA** ([Download](https://www.jetbrains.com/idea/download/))

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/BankManagementSystem.git
   cd BankManagementSystem
   ```

2. **Open in IntelliJ IDEA**
   - Go to **File → Open**
   - Select the `BankManagementSystem` folder
   - IntelliJ will auto-detect the `.iml` module file

3. **Set the SDK**
   - Go to **File → Project Structure → Project**
   - Set SDK to **Java 11** or higher

4. **Run the app**
   - Select the **"Run BankApp"** configuration from the top bar
   - Click ▶ or press `Shift + F10`

> ⚠️ **Important:** The working directory must be the **project root** (already set in the run config). This ensures `data/customers.csv` is found correctly.

---

## 🔐 Demo Credentials

A sample customer is pre-loaded for testing:

| Field | Value |
|---|---|
| Customer ID | `1` |
| Password | `12345` |
| Name | Samad |
| Balance | Rs 10,000 |

---

## 🏛️ Architecture

The project follows a clean **3-layer architecture**:

```
UI Layer  (bank.ui)
    ↓  calls
Service Layer  (bank.service.BankService)
    ↓  uses
Repository Layer  (bank.service.CustomerRepository)
    ↓  reads/writes
data/customers.csv
```

**Why this matters:**
- The UI never touches the file directly — all data access goes through the repository
- All validation and business rules live in `BankService` — easy to test or extend
- Swapping the CSV storage for a real database only requires changing `CustomerRepository`

---

## 📦 Data Format

Customer records are stored in `data/customers.csv`:

```
id,name,phone,cnic,password,address,balance
1,Samad,0344-1234567,33201-1234567-7,12345,Loralai,10000
```

| Column | Description |
|---|---|
| `id` | Auto-incremented unique customer ID |
| `name` | Full name |
| `phone` | Phone number |
| `cnic` | National ID number |
| `password` | Plain-text password |
| `address` | Home address |
| `balance` | Account balance (integer, PKR) |

---

## 🐛 Bug Fixes Over Original

The following critical bugs from the original codebase were fixed:

- **Always-true password check** — `pass.equals(pass)` compared a variable to itself, allowing any password to log in
- **`System.exit(0)` before `dispose()`** — killed the entire app instead of navigating to the next screen
- **Balance not saved after deposit/withdraw** — the loop rebuilt the CSV string using the *old* balance value
- **Broken `Existing_Customer.java`** — missing closing brace made it uncompilable
- **Delete account wrote garbage** — used `spaces.getText()` instead of removing the matched record
- **No input validation** — empty fields or non-numeric amounts caused unhandled crashes

---

## 🛠️ Built With

- **Java** — Core language
- **Java Swing** — GUI framework
- **Java NIO** (`java.nio.file`) — File read/write
- **IntelliJ IDEA** — IDE

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 🙋‍♂️ Author

Made with ❤️ — feel free to fork, improve, and submit pull requests!
