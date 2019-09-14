package entities.simple;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * a simple {@link javax.persistence.Entity}<br>
 * JPA offers well defined default mappings<br>
 * GOOD PRACTICE : ensure used database objects are controlled through JPA annotations and JPA defaults are not used<br>
 * this way code refactoring can be used over entities and entities mappings without affecting database used objects
 */
@javax.persistence.Entity
@Table(name = "SimpleEntity")// override default table name
public class Entity {

    @Id
    private Integer id;

    @Basic(optional = false)// these 2 annotations are equivalent, only one could be used
    @Column(nullable = false)// these 2 annotations are equivalent
    private String name;

    @Basic// can use this annotation or @Column, yet @Column is richer in configurations
    @Column(name = "nullableValue")// override default column name
    private Integer value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
