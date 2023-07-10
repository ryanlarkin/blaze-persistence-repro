package org.hibernate.tutorial.em;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="string_property")
public class StringProperty implements Property<String> {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`value`")
    private String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringProperty() {}

    public StringProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringProperty{" +
                "value='" + value + '\'' +
                '}';
    }
}
