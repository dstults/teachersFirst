package org.funteachers.teachersfirst.daos;

import java.util.*;

// Generic Data Access Object (DAO) Interface

public interface DAO<T> {

    // Life Cycle ----------------------------------------
    //boolean initialize(Connection conn);			// Handled by the ConnectionPackage now.
    //void terminate();								// Handled by the ConnectionPackage now.

    // Create --------------------------------------------
    int insert(T item);

    // Retrieve ------------------------------------------
    // ...one at a time
    T retrieveByID(int id);
    T retrieveByIndex(int index);
    // ...all at once
    List<T> retrieveAll();
    List<Integer> retrieveAllIDs();
    // ...some at a time
    List<T> search(String keyword);

    // Update ---------------------------------------------
    boolean update(T item);
    
    // Delete ---------------------------------------------
    void delete(int id);

    // Miscellaneous -------------------------------------
    int size();
}
