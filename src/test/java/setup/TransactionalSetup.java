package setup;

import org.junit.After;
import org.junit.Before;

/**
 * Test Abstraction that controls a transaction which is started at the beginning of the test and rolled back afterwards
 */
public abstract class TransactionalSetup extends Setup {

    /**
     * ensure a transaction is started before each test
     */
    @Before
    final public void beginTransaction() {
        em.getTransaction().begin();
    }

    /**
     * ensure a transaction is rolled back after each test
     */
    @After
    final public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

}
