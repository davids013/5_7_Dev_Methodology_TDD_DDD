package task1;

public class CreditCalc {
    private final double loanAmount;
    private final float rate;
    private final int months;
    
    public CreditCalc(double loanAmount, float rate, int months) {
        this.loanAmount = loanAmount;
        this.rate = rate;
        this.months = months;
    }

    public double getMonthlyPayment() {
        double monthRelRate = rate / 12 / 100;
        double temp = Math.pow(monthRelRate + 1, months);
        double result = loanAmount * (monthRelRate * temp / (temp - 1));
        System.out.println("Monthly payment = " + result);
        return result;
    }

    public double getOverallDebt() {
        double result = getMonthlyPayment() * months;
        System.out.println("Overall debt = " + result);
        return result;
    }

    public double getOverpayment() {
        double result = getOverallDebt() - loanAmount;
        System.out.println("Overpayment = " + result);
        return result;
    }
}
