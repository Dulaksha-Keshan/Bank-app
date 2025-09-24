# Bank App - OOP Concepts Demonstration

A simple command-line banking system built in Java to demonstrate the four core pillars of Object-Oriented Programming.

## Overview

This project showcases fundamental OOP concepts through a practical banking application. It's designed as an educational tool to understand how encapsulation, inheritance, polymorphism, and abstraction work together in a real-world scenario.

## Features

- Account creation and management
- Deposit and withdrawal operations
- Balance inquiry
- Different account types (Savings, Checking, etc.)
- User-friendly command-line interface

## OOP Concepts Demonstrated

### 🔒 **Encapsulation**
- Private fields for account details (balance, account number, customer info)
- Public getter and setter methods to control access
- Data validation in setter methods

### 🏗️ **Inheritance** 
- Base `Account` class with common properties and methods
- Derived classes like `SavingsAccount`, `CheckingAccount` extending the base class
- Method overriding for account-specific behavior

### 🎭 **Polymorphism**
- Method overloading for different transaction types
- Runtime polymorphism through method overriding
- Interface implementations for different account behaviors

### 🎨 **Abstraction**
- Abstract classes and methods for common account operations
- Interfaces defining contracts for banking operations
- Hiding complex implementation details from the user



## Getting Started

### Prerequisites
- Java 8 or higher
- Any Java IDE or text editor

### How to Run

1. Clone the repository:
```bash
git clone https://github.com/Dulaksha-Keshan/Bank-app.git
cd Bank-app
```

2. Compile the Java files:
```bash
javac *.java
```

3. Run the application:
```bash
java Main
```

## Usage Example

```
Welcome to Simple Bank System
==============================

1. Create Account
2. Deposit Money
3. Withdraw Money
4. Check Balance
5. Exit

Enter your choice: 1

Enter customer name: John Doe
Enter initial deposit: 1000
Select account type:
1. Savings Account
2. Checking Account
Enter choice: 1

Account created successfully!
Account Number: ACC001
```

## Key Learning Outcomes

After exploring this project, you'll understand:

- How **encapsulation** protects data integrity
- How **inheritance** promotes code reusability
- How **polymorphism** enables flexible code design
- How **abstraction** simplifies complex systems
- Real-world application of OOP principles

## Class Relationships

```
Account (Abstract Class)
├── SavingsAccount (Concrete Class)
├── CheckingAccount (Concrete Class)
└── User (Composition)
    └── Transaction (Association)
```



## Educational Value

This project is perfect for:
- Computer Science students learning OOP
- Java beginners understanding class relationships
- Developers reviewing fundamental concepts
- Interview preparation for OOP questions

## Contributing

Feel free to fork this project and add more features like:
- Loan accounts
- GUI interface



## Author

**Dulaksha Keshan**
- GitHub: [@Dulaksha-Keshan](https://github.com/Dulaksha-Keshan)

---

*Built to demonstrate the power and elegance of Object-Oriented Programming in Java* 📚
