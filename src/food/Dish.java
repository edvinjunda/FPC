package food;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Dish {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @ManyToMany
        @LazyCollection(LazyCollectionOption.FALSE)
        private List<Food> ingredients;
        @ElementCollection(fetch = FetchType.EAGER)
        private List<Integer> amount;
        private String name;
        private Status status;
        private PortionSize portionSize;

    public Dish(List<Food> ingredients, List<Integer> amount, Status status) {
        this.ingredients = ingredients;
        this.amount = amount;
        this.status = status;
    }

    public Dish(List<Food> ingredients, List<Integer> amount) {
        this.ingredients = ingredients;
        this.amount = amount;
    }

    @Override
    public String toString(){
        return id + ". Status: " +
                status;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Dish)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Dish d = (Dish) o;

        // Compare the data members and return accordingly
        return Double.compare(id, d.id) == 0;
    }

    @Override
    public int hashCode()
    {
        // we can return some
        // other calculated value or may
        // be memory address of the
        // Object on which it is invoked.
        // it depends on how you implement
        // hashCode() method.
        return this.id;
    }
}
