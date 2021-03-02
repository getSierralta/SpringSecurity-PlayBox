package com.security.demo.auth;

import java.util.Optional;

//DAO = Data Access Object, It’s a design pattern in which a data access object (DAO) is an object that provides an abstract interface to some type of database or other persistence mechanisms.
// By mapping application calls to the persistence layer, DAOs provide some specific data operations without exposing details of the database
public interface ApplicationUserDao {

   Optional<ApplicationUser> selectUserByUsername(String username);
}
