package org.hibernate.tutorial.em;

public interface Property<T> {
    String getName();
    T getValue();
}
