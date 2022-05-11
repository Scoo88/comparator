package com.price.comparator.check.enums;

//formatter:off
public enum CategoryLevel {
    FIRST_LEVEL(0),
    SECOND_LEVEL(1),
    THIRD_LEVEL(2),
    FOURTH_LEVEL(3),
    FIFTH_LEVEL(4);

    int level;

    CategoryLevel (int level){
        this.level = level;
    }

//    public int getLevel() {
//        return level;
//    }
}
