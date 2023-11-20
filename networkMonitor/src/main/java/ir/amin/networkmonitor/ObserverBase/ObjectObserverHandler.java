package ir.amin.networkmonitor.ObserverBase;//package com.paraxco.commontools.ObserverBase;
//
//
//import java.util.LinkedList;
//
///**
// * Created by Amin on 10/14/2017.
// */
//
//public class ObjectObserverHandler<T> {
//
//    LinkedList<ObjectObserver<T>> observerList = new LinkedList();
//
//    public void addObserver(ObjectObserver<T> observer) {
//        observerList.add(observer);
//    }
//
//    public void removeObserver(ObjectObserver<T> observer) {
//        observerList.add(observer);
//    }
//
//    public void informObservers(T data) {
//        for (ObjectObserver<T> observer : observerList) {
//            observer.observe(data);
//        }
//    }
//
//    interface ObjectObserver<T> {
//        void observe(T observable);
//    }
//
//}
//
//
