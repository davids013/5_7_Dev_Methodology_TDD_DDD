import task1.CreditCalc;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\tМодуль 5. Многопоточное и функциональное программирование\n" +
                "\tДомашнее задание к лекции 7.Методология разработки. TDD. DDD\n");

        System.out.println("\tЗадача 1. Разработка через тестирование (Test Driven Development)");
        final double loanAmount = 750_000d;
        final float rate = 16.7f;
        final int months = 18;
        final CreditCalc credit = new CreditCalc(loanAmount, rate, months);
        System.out.printf("Оформлен кредит на сумму %.1f руб. со ставкой %.1f%% годовых на %d месяцев\n",
                loanAmount, rate, months);
        credit.getOverpayment();
    }
}
