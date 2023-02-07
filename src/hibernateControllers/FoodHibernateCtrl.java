package hibernateControllers;

import food.Food;
import food.PortionSize;
import food.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class FoodHibernateCtrl {

    private EntityManagerFactory emf = null;

    public FoodHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEm() {
        return emf.createEntityManager();
    }


    public void createFood(Food food) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.persist(food); //INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void updateFood(Food food) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(food); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void removeFood(int id) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            Food food = null;
            try {
                food = em.getReference(Food.class, id);
                food.getId();
            } catch (Exception e) {
                System.out.println("Food with entered Id doesn't exist");
            }

            //food = em.find(Food.class, id);

            em.remove(food);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            alertMsg("This food can't be removed.","It was already removed or is contained in some dish.");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /*public List getAllFoods(int inStock) {
        EntityManager em = getEm();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Food> query = cb.createQuery(Food.class);
            Root<Food> root = query.from(Food.class);

            if (inStock==0)
                query.select(root).where(cb.gt(root.get("inStock"), inStock));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return new ArrayList();
    }*/

    public Food getFoodById(int id) {
        EntityManager em = null;
        Food food = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            food = em.find(Food.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Food with entered Id doesn't exist");
        }
        return food;
    }

    public List getFilteredFoods(String name, Status status) {
        EntityManager em = getEm();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Food> query = cb.createQuery(Food.class);
            Root<Food> root = query.from(Food.class);

            if(name.isEmpty()&&status==null){
                query.select(root);
            }
            else if(status==null){
                query.select(root).where(cb.like(root.get("name"),"%"+name+"%"));
            }
            else{
                query.select(root).where(cb.and(cb.like(root.get("name"), "%" + name + "%")), cb.equal(root.get("status"), status));
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
