package com.haishi;

import java.util.*;

public class AVL {

    private AVLNode root;

    static class AVLNode<K extends Comparable<K>,V> {
        private AVLNode parent;
        private AVLNode left;
        private AVLNode right;
        private K key;
        private V value;
        public AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public AVLNode() {
        }

        public AVLNode(AVLNode parent,  V value, K key, AVLNode left, AVLNode right) {
            this.parent = parent;
            this.value = value;
            this.key = key;
            this.right = right;
            this.left = left;
        }
    }

    /**
     * 左旋
     * @param p 旋转结点
     *
     *    p                             pr
     *   / \                           / \
     *  pl  pr        ---->           p   rr
     *      / \                      / \
     *     rl  rr                   pl rl
     *
     */
    private void leftRotate(AVLNode p) {
        if (p == null) {
            return;
        }
        AVLNode right = p.right;

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
     * @param p 旋转结点
     */
    private void RightRotate(AVLNode p) {
        if (p == null) {
            return;
        }
        AVLNode left = p.left;


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





