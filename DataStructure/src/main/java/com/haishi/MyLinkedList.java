package com.haishi;

import org.w3c.dom.Node;

import java.util.*;

public class MyLinkedList<E> {

    int size=0;

    Node<E> head;

    Node<E> tail;

    public MyLinkedList() {
    }

    public MyLinkedList(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    private static class Node<E> {
        E item;
        MyLinkedList.Node<E> next;
        MyLinkedList.Node<E> prev;

        Node(MyLinkedList.Node<E> prev, E element, MyLinkedList.Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    public void addFirst(E e) {
        final Node<E> f=head;
        final Node<E> newNode = new Node<E>(null, e, f);
        head = newNode;
        if(f==null){
            tail = newNode;
        }else{
            f.prev = newNode;
        }
        size++;
    }


    public void addLast(E e) {
        final Node<E> l=tail;
        final Node<E> newNode = new Node<E>(l, e, null);
        tail = newNode;
        if(l==null){
            head = newNode;
        }else{
            l.next = newNode;
        }
        size++;
    }



    public E removeFirst() {
        if(head==null){
            return null;
        }
        final E e=head.item;
        final Node<E> next=head.next;
        head.item=null;
        head.next=null;
        head.prev=null;
        if(next==null){
            tail=null;
        }else{
            next.prev=null;
        }
        size--;
        return e;
    }


    public E removeLast() {
        if(tail==null)
            return null;
        final E e=tail.item;
        final Node<E> r=tail.prev;
        tail.item=null;
        tail.prev=null;
        tail.next=null;
        tail=r;
        if(r==null){
            head=null;
        }else{
            r.next=null;
        }
        size--;
        return e;
    }




    public E getFirst() {
        return head==null?null:head.item;
    }


    public E getLast() {
        return tail==null?null:tail.item;
    }




    public E remove() {
        return removeFirst();
    }




    public E peek() {
        return getFirst();
    }



    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size==0;
    }


    public boolean contains(Object o) {
        return indexOf(o)!=-1;
    }



    public Object[] toArray() {
        Object[] a=new Object[size];
        int i=0;
        for(Node<E> node=head;node!=null;node=node.next){
            a[i++]=node.item;
        }
        return a;
    }



    public <T> T[] toArray(T[] a) {
        if(a==null || a.length<size){
            a=(T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(),size);
        }
        int i=0;

        Object[] o=a;
        for(Node<E> node=head;node!=null;node=node.next){
            o[i++]=node.item;
        }
        if(a.length>size){
            while(i<a.length){
                a[i++]=null;
            }
        }
        return a;
    }


    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
       if(!checkIndex(index)) return null;
       Node<E> oldNode=find(index);
       final E oldItem=oldNode.item;
       final Node<E> prev=oldNode.prev;
       final Node<E> next=oldNode.next;

       if(prev==null){
           head=next;
       }else {
           prev.next = next;
       }

       if(next==null){
           tail=prev;
       }else{
           next.prev=prev;
       }

       oldNode.prev=null;
       oldNode.next=null;
       oldNode.item=null;

       size--;

       return oldItem;
    }

    private Node<E> find(int index) {
        if(index<(size>>1)){
            Node<E> node=head;
            for(int i=0;i<index;i++){
                node=node.next;
            }
            return node;
        }else {
            Node<E> node=tail;
            for(int i=size-1;i>index;i--){
                node=node.prev;
            }
            return node;
        }
    }


    private boolean checkIndex(int index) {
        return index>=0 && index<size;
    }

    public boolean remove(Object o) {
        remove(indexOf(o));
        return true;
    }




    public boolean addAll(Collection<? extends E> c) {
        Object[] array = c.toArray();
        if(array.length==0){
            return false;
        }

        for (Object o : array) {
            Node<E> node=new Node<>(tail,(E)o,null);
            if(tail==null){
                head=node;
            }else {
                tail.next=node;
            }
            tail=node;
        }
        size+=array.length;
        return true;
    }






    public void clear() {
        //帮助gc
        for(Node<E> e=head;e!=null;e=e.next){
            Node<E> next=e.next;
            e.item=null;
            e.next=null;
            e.prev=null;
            e=next;
        }
        head=null;
        tail=null;
        size=0;
    }


    public E get(int index) {
        if(!checkIndex(index)) return null;
        return find(index).item;
    }


    public E set(int index, E element) {
        if(!checkIndex(index)) return null;
        Node<E> oldNode=find(index);
        final E oldItem=oldNode.item;
        oldNode.item=element;
        return oldItem;
    }


    public void add(int index, E element) {
        if(!checkIndex(index)) return;
        if(index==size){
            addLast(element);
        }else {
            final Node<E> oldNode=find(index);
            final Node<E> prev=oldNode.prev;
            final Node<E> newNode=new Node<E>(prev,element,oldNode);
            if(prev==null){
                head=newNode;
            }else{
                prev.next = newNode;
            }
            size++;
        }

    }





    public int indexOf(Object o) {
       int index=0;
       if(o==null){
           for(Node<E> node=head;node!=null;node=node.next){
               if(node.item==null){
                   return index;
               }
               index++;
           }
       }else{
           for(Node<E> node=head;node!=null;node=node.next){
               if(o.equals(node.item)){
                   return index;
               }
               index++;
           }
       }
       return -1;
    }


    public int lastIndexOf(Object o) {
        int index=size-1;
        if(o==null){
            for(Node<E> node=tail;node!=null;node=node.prev){
                if(node.item==null){
                    return index;
                }
                index--;
            }
        }else{
            for(Node<E> node=tail;node!=null;node=node.prev){
                if(o.equals(node.item)){
                    return index;
                }
                index--;
            }
        }
        return -1;
    }


}
