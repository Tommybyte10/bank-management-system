import java.util.ArrayList;

/**
 * The parent class for every type of bank account.
 * It is abstract, so you can't create a plain Account,
 * only a CheckingAccount, SavingsAccount, or BusinessAccount.
 *
 * @author Thomas Sanchez
 */
public abstract class Account {

    public static final double SUSPICIOUS_PERCENT = 0.50;
    public static final int MAX_PIN_TRIES = 3;

    private String ownerName;
    private int accountNumber;
    private String pin;
    private int failedPinTries;
    private boolean locked;
    protected double balance;
    private ArrayList<String> history;

    public Account(String ownerName, int accountNumber, String pin) {
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.failedPinTries = 0;
        this.locked = false;
        this.balance = 0.0;
        this.history = new ArrayList<String>();
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getTriesLeft() {
        return MAX_PIN_TRIES - failedPinTries;
    }

    public boolean checkPin(String attempt) {
        if (locked) {
            return false;
        }
        if (pin.equals(attempt)) {
            failedPinTries = 0;
            return true;
        }
        failedPinTries++;
        if (failedPinTries >= MAX_PIN_TRIES) {
            locked = true;
        }
        return false;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance = balance + amount;
        addHistory("Deposit", amount);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        boolean flagged = isSuspicious(amount);
        balance = balance - amount;
        if (flagged) {
            addHistory("Withdrawal (FLAGGED)", amount);
        } else {
            addHistory("Withdrawal", amount);
        }
        return true;
    }

    public boolean transferTo(Account other, double amount) {
        if (other == this || amount <= 0 || amount > balance) {
            return false;
        }
        boolean flagged = isSuspicious(amount);
        balance = balance - amount;
        other.balance = other.balance + amount;
        if (flagged) {
            addHistory("Transfer to #" + other.getAccountNumber() + " (FLAGGED)", amount);
        } else {
            addHistory("Transfer to #" + other.getAccountNumber(), amount);
        }
        other.addHistory("Transfer from #" + accountNumber, amount);
        return true;
    }

    public boolean isSuspicious(double amount) {
        return amount > balance * SUSPICIOUS_PERCENT;
    }

    public int countFlagged() {
        int count = 0;
        for (String entry : history) {
            if (entry.contains("FLAGGED")) {
                count++;
            }
        }
        return count;
    }

    protected void addHistory(String type, double amount) {
        history.add(String.format("%s: $%.2f (balance: $%.2f)", type, amount, balance));
    }

    public abstract String getAccountType();

    public abstract void monthEnd();

    @Override
    public String toString() {
        String status = "";
        if (locked) {
            status = "  [LOCKED]";
        }
        return String.format("#%d  %s  (%s)  $%.2f%s",
                accountNumber, ownerName, getAccountType(), balance, status);
    }
}
