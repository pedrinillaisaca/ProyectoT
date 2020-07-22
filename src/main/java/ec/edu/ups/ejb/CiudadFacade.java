package ec.edu.ups.ejb;

import ec.edu.ups.entidad.Ciudad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CiudadFacade extends AbstractFacade<Ciudad> {
    @PersistenceContext(unitName = "Practica03.EJB.JSF.JPA")
    private EntityManager entityManager;

    public CiudadFacade(){
        super(Ciudad.class);
    }

    @Override
    protected EntityManager getEntityManager(){
        return entityManager;
    }
}
