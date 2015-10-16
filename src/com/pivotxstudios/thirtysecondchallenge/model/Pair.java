package com.pivotxstudios.thirtysecondchallenge.model;

import java.util.Comparator;


public class Pair {
        
        public static class CustomComparator implements Comparator<Pair> {
            @Override
            public int compare(Pair o1, Pair o2) {
                int d1 = o1.value;
                int d2 = o2.value;
                
                if (d1 > d2)
                        return 1;
                if (d1 < d2)
                        return -1;
                return 0;
            }
        }

        
        public String key;
        public int value;
        
        public Pair(String key, int value) {
                this.key = key;
                this.value = value;
        }
}
