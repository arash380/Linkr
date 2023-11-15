package com.linkr.access;

import com.linkr.models.Credentials;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


/**
 * Accesses the Credential related tables in the database to read,
 * write, and update entries.
 *
 * @author Robert Roe A00817290
 * @version 1.0
 */
@Stateless
public class CredentialsAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    @PersistenceContext(unitName = "linkr-jpa")
    EntityManager em;

    /**
     * Default constructor.
     */
    public CredentialsAccessor() {
    }


    /**
     * Finds and returns a credential based on employee id.
     *
     * @param id the employeeId of the desired credential
     * @return credentials the Credentials
     */
    public Credentials find(int id) {
        return em.find(Credentials.class, id);
    }

    /**
     * Finds and returns a credential based on username.
     *
     * @param username the username of the desired credential
     * @return credentials the Credentials
     */
    public Credentials find(String username) throws NoResultException {

        try {
            return em.createQuery(
                "SELECT c FROM Credentials c WHERE c.userName LIKE :userName",
                Credentials.class).
                setParameter("userName", username).
                getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * Saves a credential to the database.
     *
     * @param credentials the Credentials to save
     */
    public void persist(Credentials credentials) {
        em.persist(credentials);
    }

    /**
     * Updates a credential in the database.
     *
     * @param credentials the Credentials to update
     */
    public void merge(Credentials credentials) throws IllegalArgumentException {
        em.merge(credentials);
    }

    /**
     * Removes a credential from the database.
     *
     * @param credentials the Credentials to remove
     */
    public void remove(Credentials credentials) {
        em.remove(credentials);
    }
}
