package food;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@Entity
public class Food implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private int partsAmount;
    private String notes;
    private Status status;
    @ManyToMany(mappedBy = "ingredients", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dish> inDishes;

    public Food() {
    }

    public Food(String name, double price, int partsAmount, String notes, Status status) {
        this.name = name;
        this.price = price;
        this.partsAmount = partsAmount;
        this.notes = notes;
        this.status = status;
    }

    public Food(String name, double price, int partsAmount, String notes, Status status, List<Dish> inDishes) {
        this.name = name;
        this.price = price;
        this.partsAmount = partsAmount;
        this.notes = notes;
        this.status = status;
        this.inDishes = inDishes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPartsAmount() {
        return partsAmount;
    }

    public void setPartsAmount(int partsAmount) {
        this.partsAmount = partsAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Dish> getInDishes() {
        return inDishes;
    }

    public void setInDishes(List<Dish> inDishes) {
        this.inDishes = inDishes;
    }

    @Override
    public String toString(){
        return id + "." +
                name + ", " +
                price + ", amount: " +
                partsAmount + ", " +
                status + ". Notes: " +
                notes;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Food)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Food f = (Food) o;

        // Compare the data members and return accordingly
        return Double.compare(id, f.id) == 0;
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
