package com.security.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.security.demo.security.UserPermission.*;

public enum UserRole {
    //This is where you define the roles of the application
    STUDENT(Sets.newHashSet()), // newHashSet() Creates a mutable, initially empty HashSet instance.
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)), //Creates a mutable HashSet instance initially containing the given elements.
    ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<UserPermission> permissions;

    //Set<E> A collection that contains no duplicate elements
    UserRole(Set<UserPermission> permissions){
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }


    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        //SimpleGrantedAuthority is a basic concrete implementation of a GrantedAuthority must either represent itself as a String or be specifically supported by an AccessDecisionManager.
        //Represents an authority granted to an Authentication object.
        //Stream map(Function mapper) returns a stream consisting of the results of applying the given function to the elements of this stream
        //the Stream API is used to process collections of objects

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
