package task1;

import org.junit.jupiter.api.*;
import test_resources.TestInit;

public class CreditCalcTest {
    final float REL_ERR = 1E-7f;
    final double loanAmount = 4_000_000d;
    final float rate = 12.6f;
    final int months = 24;
    final CreditCalc creditCalc = new CreditCalc(loanAmount, rate, months);

    @BeforeAll
    static void beforeAll() { TestInit.beforeAll(); }

    @AfterAll
    static void afterAll() { TestInit.afterAll(); }

    @BeforeEach
    public void beforeEach() { TestInit.beforeEach(); }

    @AfterEach
    public void afterEach() { TestInit.afterEach(); }

    @Test
    void getMonthlyPaymentTest() {
        final double expected = 189_416.63;         // Рассчитано кредитным онлайн-калькулятором
        final double result = creditCalc.getMonthlyPayment();

        Assertions.assertTrue(Math.abs(result - expected) <= REL_ERR * expected);
    }

    @Test
    void getOverallDebtTest() {
        final double expected = 4_545_999.12;       // Рассчитано кредитным онлайн-калькулятором
        final double result = creditCalc.getOverallDebt();

        Assertions.assertTrue(Math.abs(result - expected) <= REL_ERR * expected);
    }

    @Test
    void getOverpaymentTest() {
        final double expected = 545_999.12;         // Рассчитано кредитным онлайн-калькулятором
        final double result = creditCalc.getOverpayment();

        Assertions.assertTrue(Math.abs(result - expected) <= REL_ERR * expected);
    }
}
