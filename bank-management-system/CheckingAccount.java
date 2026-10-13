/**
 * A checking account. Charges a small monthly fee
 * if the balance drops below the minimum.
 *
 * @author Thomas Sanchez
 */
public class CheckingAccount extends Account {

    public static final double MINIMUM_BALANCE = 100.0;
    public static final double MONTHLY_FEE = 5.0;

    public CheckingAccount(String ownerName, int accountNumber, String pin) {
        super(ownerName, accountNumber, pin);
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public void monthEnd() {
        if (balance < MINIMUM_BALANCE && balance >= MONTHLY_FEE) {
            balance = balance - MONTHLY_FEE;
            addHistory("Low balance fee", MONTHLY_FEE);
        }
    }
}
