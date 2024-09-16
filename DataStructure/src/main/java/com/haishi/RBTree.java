package com.haishi;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 红黑树
 */
public class RBTree {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private RBNode root;

    private int size;

    public RBTree() {

    }

    /**
     * 红黑树结点
     */
    static class RBNode<K extends Comparable<K>,V> {
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



}


