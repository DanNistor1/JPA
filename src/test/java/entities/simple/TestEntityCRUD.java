package entities.simple;

import org.junit.Assert;
import org.junit.Test;
import setup.TransactionalSetup;

import java.util.List;

public class TestEntityCRUD extends TransactionalSetup {

    @Test
    public void testCreateReadUpdateReadRemoveRead() {

        // select by id => expect none
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        // create I record
        {
            Entity entity = new Entity();
            entity.setId(1);
            entity.setName("xxx");
            entity.setValue(3);
            em.persist(entity);

            // flush sincronizeaza persistent context cu baza de date (copia bazei aflate in memorie). Operatiile insert/update/delete
            // sunt executate dar tranzactia curenta nu este transmisa. Schimbarile nu vor fi vizibile altor istante
            // EntityManager, ci doar pentru tranzactia curenta. Operatia flush goleste memoria cache de la EntityManager la baza de date.
            em.flush();

            // rupe legaturile tuturor entitatilor cu persistent context (persistent context monitorizeaza starea
            // entitatilor). Este echivalent cu em.detach(entity), dar acesta din urma actioneaza numai asupra unei entitati
            em.clear();

        }

        // select by id => expect exactly I
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("xxx", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        // update
        {
            Entity entity = em.find(Entity.class, 1);
            entity.setName("yyy");
            em.flush();
            em.clear();
        }

        // select by id => expect exactly I update
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("yyy", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        // remove
        {
            Entity entity = em.find(Entity.class, 1);
            em.remove(entity);
            em.flush();
        }

        // select by id => expected nothing
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        // select native
        {
            List resultList = em.createNativeQuery("select * from SimpleEntity").getResultList();
            System.out.println(resultList);
        }

    }
}

