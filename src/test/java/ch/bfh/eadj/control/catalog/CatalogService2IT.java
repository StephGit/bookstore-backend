package ch.bfh.eadj.control.catalog;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(Arquillian.class)
public class CatalogService2IT {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(CatalogService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Inject
    CatalogService catalogService;



}
