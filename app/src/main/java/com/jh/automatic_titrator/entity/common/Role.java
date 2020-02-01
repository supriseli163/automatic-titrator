package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 16/9/15.
 */
public enum Role {
    admin, manager, operator, factory;

    public static Role getRole(int i) {
        return Role.values()[i];
    }

    public static int getRoleIndex(Role role) {
        switch (role) {
            case admin:
                return 0;
            case manager:
                return 1;
            case operator:
                return 2;
            case factory:
                return 3;
        }
        return 2;
    }
}
