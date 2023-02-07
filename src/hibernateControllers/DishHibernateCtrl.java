package hibernateControllers;

import food.Dish;
import food.PortionSize;
import food.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class DishHibernateCtrl {
    private EntityManagerFactory emf = null;

    public DishHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEm() {
        return emf.createEntityManager();
    }

    public void createDish(Dish dish) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.persist(dish); //INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void updateDish(Dish dish) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(dish); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void removeDish(int id) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            Dish dish = null;
            try {
                dish = em.getReference(Dish.class, id);
                dish.getId();
            } catch (Exception e) {
                System.out.println("Dish doesn't exist");
            }

            //book = em.find(Food.class, id);

            em.remove(dish);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            alertMsg("This dish is unavailable.","It was already removed.");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List getAllDishes() {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Dish.class));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Dish getDishById(int id) {
        EntityManager em = null;
        Dish dish = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            dish = em.find(Dish.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Dish with entered Id doesn't exist");///////////////////////
        }
        return dish;
    }

    public List getFilteredDishes(String name, Status status, PortionSize portionSize) {
        EntityManager em = getEm();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Dish> query = cb.createQuery(Dish.class);
            Root<Dish> root = query.from(Dish.class);

            if(name.isEmpty()&&status==null&&portionSize==null){
                query.select(root);
            }
            else if(status==null&&portionSize==null)
            {
                query.select(root).where(
                        cb.like(root.get("name"),"%" + name + "%"));
            }
            else if(status==null){
                query.select(root).where(cb.and(
                        cb.like(root.get("name"),"%" + name + "%"),
                        cb.equal(root.get("portionSize"),portionSize)));
            }
            else if(portionSize==null){
                query.select(root).where(cb.and(
                        cb.like(root.get("name"),"%" + name + "%"),
                        cb.equal(root.get("status"), status)));
            }
            else{
                query.select(root).where(cb.and(
                        cb.like(root.get("name"),"%" + name + "%"),
                        cb.equal(root.get("status"), status),
                        cb.equal(root.get("portionSize"),portionSize)));
            }

            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
}
