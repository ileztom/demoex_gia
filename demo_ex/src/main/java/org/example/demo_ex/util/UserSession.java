package org.example.demo_ex.util;

import org.example.demo_ex.model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isAdmin() {
        return currentUser != null && "Администратор".equals(currentUser.getRole());
    }

    public boolean isManager() {
        return currentUser != null && "Менеджер".equals(currentUser.getRole());
    }

    public boolean isClient() {
        return currentUser != null && "Клиент".equals(currentUser.getRole());
    }

    public boolean isGuest() {
        return currentUser == null;
    }

    public void logout() {
        currentUser = null;
    }
}
