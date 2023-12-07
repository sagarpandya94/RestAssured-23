package com.rest;

public class MethodChaining {

    public static void main(String[] args){
        a1().a2().a3();
    }

    public static MethodChaining a1(){
        System.out.println("Hi, this is a1");
        return new MethodChaining();
    }

    public MethodChaining a2(){
        System.out.println("Hello, this is a2");
        return this;
    }

    public MethodChaining a3(){
        System.out.println("Hi, a3 here");
        return this;
    }
}
