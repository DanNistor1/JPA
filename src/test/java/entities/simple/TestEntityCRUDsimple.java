package entities.simple;

import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class TestEntityCRUDsimple {

    @Test
    public void testCreateReadUpdateReadRemoveRead() {

        String PERSISTENCE_UNIT_NAME = "examplePersistenceUnit";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        // select by id => expect none
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        // create I record
        {
            Entity entity = new Entity();
            entity.setId(1);
            entity.setName("xxx");
            entity.setValue(3);
            entityManager.persist(entity);

            // flush sincronizeaza persistent context cu baza de date (copia bazei aflate in memorie). Operatiile insert/update/delete
            // sunt executate dar tranzactia curenta nu este transmisa. Schimbarile nu vor fi vizibile altor istante
            // EntityManager, ci doar pentru tranzactia curenta. Operatia flush goleste memoria cache de la EntityManager la baza de date.
            entityManager.flush();

            // rupe legaturile tuturor entitatilor cu persistent context (persistent context monitorizeaza starea
            // entitatilor). Este echivalent cu entityManager.detach(entity), dar acesta din urma actioneaza numai asupra unei entitati
            entityManager.clear();

        }

        // select by id => expect exactly I
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("xxx", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        // update
        {
            Entity entity = entityManager.find(Entity.class, 1);
            entity.setName("yyy");
            entityManager.flush();
            entityManager.clear();
        }

        // select by id => expect exactly I update
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("yyy", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

/*
        // remove
        {
            Entity entity = entityManager.find(Entity.class, 1);
            entityManager.remove(entity);
            entityManager.flush();
        }

        // select by id => expected nothing
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNull(entity);
        }
*/

        // select native
        {
            List resultList = entityManager.createNativeQuery("select * from SimpleEntity").getResultList();
            System.out.println(resultList);
        }

//        entityManager.getTransaction().rollback();
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

    }
}
