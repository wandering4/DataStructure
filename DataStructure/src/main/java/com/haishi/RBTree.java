package com.haishi;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 红黑树
 */
public class RBTree<K extends Comparable<K>,V>{

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private RBNode root;

    private int size;

    public RBTree() {

    }

    /**
     * 红黑树结点
     */
    public static class RBNode<K extends Comparable<K>,V> {
        private RBNode parent;
        private RBNode left;
        private RBNode right;
        private K key;
        private V value;
        private boolean color;
        public RBNode(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }

        public RBNode() {
        }

        public RBNode(RBNode parent, boolean color, V value, K key, RBNode left, RBNode right) {
            this.parent = parent;
            this.color = color;
            this.value = value;
            this.key = key;
            this.right = right;
            this.left = left;
        }

        public RBNode getParent() {
            return parent;
        }

        public void setParent(RBNode parent) {
            this.parent = parent;
        }

        public RBNode getRight() {
            return right;
        }

        public void setRight(RBNode right) {
            this.right = right;
        }

        public RBNode getLeft() {
            return left;
        }

        public void setLeft(RBNode left) {
            this.left = left;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public boolean getColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

    }


    /**
     * 左旋
     * @param p
     *
     *    p                             pr
     *   / \                           / \
     *  pl  pr        ---->           p   rr
     *      / \                      / \
     *     rl  rr                   pl rl
     *
     */
    private void leftRotate(RBNode p) {
        if (p == null) {
            return;
        }
        RBNode right = p.right;

        //设置p和right.left的关系
        //如果right有左节点，需要挂载到left上
        if(right.left!=null){
            right.left.parent = p;
        }
        p.right=right.left;

        //如果p上面还有结点，需要处理parent与p之间的关系
        right.parent=p.parent;
        if(right.parent==null){
            this.root=right;
        }else if(p.parent.left==p){
            p.parent.left=right;
        }else{
            p.parent.right=right;
        }

        //设置p和right的关系
        p.parent=right;
        right.left=p;

    }

    /**
     * 右旋
     * @param p
     */
    private void RightRotate(RBNode p) {
        if (p == null) {
            return;
        }
        RBNode left = p.left;


        //如果left有右节点，需要挂载到left上
        if(left.right!=null){
            left.right.parent = p;
        }
        p.left=left.right;

        //如果p上面还有结点，需要处理parent与p之间的关系
        left.parent=p.parent;
        if(left.parent==null){
            this.root=left;
        }else if(p.parent.left==p){
            p.parent.left=left;
        }else{
            p.parent.left=left;
        }

        //设置p和left的关系
        p.parent=left;
        left.right=p;
    }


    /**
     * 新增结点
     * 如果键值一样则弹出原来的值
     * @param key
     * @param val
     * @return 弹出的值
     */
    public V put(K key ,V val){
        //1.找到插入的位置
        if(this.root==null){
            this.root=new RBNode(key,val==null?key:val,BLACK);
            size++;
            return null;
        }

        //不允许key为空
        if(key==null){
            throw new NullPointerException();
        }

        //需要插入结点的父节点
        RBNode p=null;
        RBNode cur=this.root;
        int i=0;
        while(cur!=null){
            i=cur.key.compareTo(key);
            p=cur;
            if(i==0){
                V old= (V) cur.getValue();
                cur.setValue(val==null?key:val);
                return old;
            }else if(i<0){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }
        //找到了需要插入的位置
        cur=new RBNode<K,Object>(key,val==null?key:val,BLACK);
        cur.parent=p;
        if(i<0){
            //插入左侧
            p.left=cur;
        }else{
            //插入右侧
            p.right=cur;
        }


        //2.调整平衡 变色和旋转
        fixAfterPut(cur);
        size++;
        return null;
    }

    /**
     * 调整结点平衡
     * @param node
     */
    private void fixAfterPut(RBNode<K,Object> node) {
        //1.插入结点设置为红色
        node.setColor(RED);

        //2.旋转和变色
        RBNode parent;
        while (node!=null&&node!=root&&node.parent.color==RED){
            parent=getParent(node);
            if(parent==leftOf(getParent(parent))){
                //当前结点的父节点是爷结点的左结点
                RBNode uncle=rightOf(getParent(parent));
                if(getColor(uncle)==RED){
                    //四结点
                    setColor(uncle,BLACK);
                    setColor(parent,BLACK);
                    setColor(getParent(parent),RED);
                    //需要一个递归的处理
                    node=getParent(getParent(node));
                }else{
                    //三结点
                    if(node==rightOf(parent)){
                        //做一次左旋
                        node=parent;
                        leftRotate(node);
                    }
                    //根据爷爷结点做一次右旋
                    setColor(getParent(node),BLACK);
                    setColor(getParent(getParent(node)),RED);
                    RightRotate(getParent(getParent(node)));
                }

            }else{
                //当前结点的父节点是爷结点的右结点
                RBNode uncle=leftOf(getParent(parent));
                if(getColor(uncle)==RED){
                    //四结点
                    setColor(uncle,BLACK);
                    setColor(parent,BLACK);
                    setColor(getParent(parent),RED);
                    //需要一个递归的处理
                    node=getParent(getParent(node));
                }else{
                    //三结点
                    if(node==parent.left){
                        //做一次右旋
                        node=parent;
                        RightRotate(node);
                    }
                    //根据爷爷结点做一次右旋
                    setColor(getParent(node),BLACK);
                    setColor(getParent(getParent(node)),RED);
                    leftRotate(getParent(getParent(node)));
                }

            }
        }

        //3.根节点为黑色
        root.setColor(BLACK);
    }

    private boolean getColor(RBNode n){
        return n==null?BLACK:n.color;
    }

    private RBNode getParent(RBNode n){
        return n==null?null:n.parent;
    }

    private RBNode leftOf(RBNode n){
        return n==null?null:n.left;
    }

    private RBNode rightOf(RBNode n){
        return n==null?null:n.right;
    }

    private void setColor(RBNode n, boolean color){
        if(n!=null)
            n.setColor(color);
    }


    public RBNode getRoot() {
        return root;
    }

    public int size(){
        return size;
    }



    public V remove(K key){

    }


}


