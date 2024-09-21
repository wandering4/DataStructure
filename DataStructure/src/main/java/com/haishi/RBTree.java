package com.haishi;

import java.awt.*;
import java.util.*;

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
            }else if(i>0){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }
        //找到了需要插入的位置
        cur=new RBNode<K,Object>(key,val==null?key:val,BLACK);
        cur.parent=p;
        if(i>0){
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


    /**
     * 删除节点
     *   节点删除(普通的二叉树删除)
     */
    public V remove(K key){
        //1.找到对应结点
        RBNode p=getNode(key);

        if(p==null){
            return null;
        }

        V oldVal=(V) p.getValue();
        deleteNode(p);
        return oldVal;
    }


    private RBNode getNode(K key){
        RBNode p=root;
        while(p!=null){
            if(p.key.equals(key)){
                return p;
            }else if(key.compareTo((K)p.key)<0){
                p=p.left;
            }else{
                p=p.right;
            }
        }
        return null;
    }

    /**
     * 删除节点
     * 1.删除节点(普通的二叉树删除)
     *    1.删除叶子节点 直接删除
     *    2.删除节点有一个子节点，那么用子节点来替代
     *    3.删除节点右两个子节点，这时我们需要找到删除节点的前驱节点或者后继节点来替换
     *          将情况3转换为情况1或2
     * 2.调整
     * @param node
     */
    private void deleteNode(RBNode node){
        //1.删除的结点有两个子节点
        while (leftOf(node)!=null&&rightOf(node)!=null){
            //找到node的前驱结点或后继结点
            RBNode predecessor=predecessor(node);
//            RBNode successor=successor(node);
            //通过前驱或后继结点的信息覆盖
            node.setKey(predecessor.key);
            node.setValue(predecessor.value);
            node=predecessor;
        }

        RBNode replace=leftOf(node)!=null?leftOf(node):rightOf(node);
        //2.删除的结点有一个子结点
        if(replace!=null){
            replace.parent = node.parent;
            if(node.parent == null){
                root = replace;
            }else if(leftOf(getParent(node)) == node){
                getParent(node).left = replace;
            }else{
                getParent(node).right = replace;
            }
            // 要删除的node节点 GC
            node.left = node.right = node.parent = null;
            if(getColor(node) == BLACK){
                // 做调整操作
                fixAfterRemove(replace);
            }
        }
        //2.删除的结点是叶子结点
        else if (node == root) {
            root = null;

        } else {

            if (getColor(node) == BLACK) {
                // 做调整操作
                fixAfterRemove(node);
            }

            RBNode parent=getParent(node);
            if(node==parent.left){
                parent.left=null;
            }else{
                parent.right=null;
            }
            node.parent=null;
            node=null;
        }
    }

    /**
     * 删除结点后的平衡处理
     * 1. 情况1：自己能搞定
     * 2. 情况2： 找兄弟结点借    父结点下移，兄弟结点上移  变色+旋转
     * 3. 情况3： 兄弟结点不借    兄弟结点变红  递归处理
     * @param e
     */
    private void fixAfterRemove(RBNode e) {
        //处理情况2，3
        while (e!=root&&getColor(e)==BLACK){
            if(e==leftOf(getParent(e))){
                RBNode brother=getParent(e).right;
                //不是真正的兄弟结点
                if(getColor(brother)==RED){
                    setColor(brother,BLACK);
                    setColor(getParent(e),RED);
                    leftRotate(getParent(e));
                    //找到真正兄弟结点
                    brother=rightOf(getParent(e));
                }
                //情况3：兄弟结点没有子节点
                if(getColor(leftOf(brother))==BLACK&&getColor(rightOf(brother))==BLACK){
                    //变色
                    setColor(brother,RED);
                    e=getParent(e);
                }else {
                    //兄弟结点借
                    //兄弟结点右侧为空，左侧肯定不为空
                    if (getColor(rightOf(brother)) == BLACK) {
                        setColor(brother, RED);
                        setColor(leftOf(brother), BLACK);
                        RightRotate(brother);
                        //兄弟结点变化
                        brother = rightOf(getParent(e));
                    }
                    //左旋加变色
                    setColor(brother, getColor(getParent(e)));
                    setColor(getParent(e), BLACK);
                    setColor(rightOf(brother), BLACK);
                    leftRotate(getParent(e));
                    //结束循环
                    e=root;
                }
            }else{
                RBNode brother=getParent(e).left;
                //不是真正的兄弟结点
                if(getColor(brother)==RED){
                    setColor(brother,BLACK);
                    setColor(getParent(e),RED);
                    RightRotate(getParent(e));
                    //找到真正兄弟结点
                    brother=leftOf(getParent(e));
                }
                //情况3：兄弟结点没有子节点
                if(getColor(leftOf(brother))==BLACK&&getColor(rightOf(brother))==BLACK){
                    //变色
                    setColor(brother,RED);
                    e=getParent(e);
                }else {
                    //兄弟结点借
                    //兄弟结点右侧为空，左侧肯定不为空
                    if (getColor(leftOf(brother)) == BLACK) {
                        setColor(brother, RED);
                        setColor(leftOf(brother), BLACK);
                        leftRotate(brother);
                        //兄弟结点变化
                        brother = leftOf(getParent(e));
                    }
                    //左旋加变色
                    setColor(brother, getColor(getParent(e)));
                    setColor(getParent(e), BLACK);
                    setColor(leftOf(brother), BLACK);
                    RightRotate(getParent(e));
                    //结束循环
                    e=root;
                }
            }

        }
        //处理情况1
        setColor(e,BLACK);
    }


    /**
     * 查询当前结点的前驱结点
     * @param node
     * @return
     */
    private RBNode predecessor(RBNode node) {
        RBNode p;
        if (node == null) {
            return null;
        } else if (leftOf(node) != null) {
            p = leftOf(node);
            while (p.right != null) {
                p = p.right;
            }
            return p;
        } else {
            p = getParent(node);
            RBNode pre = node;
            while (p != null && pre == p.left) {
                pre = p;
                p = getParent(p);
            }
            return p;
        }
    }

    /**
     * 目标结点的后继结点
     * @param node
     * @return
     */
    private RBNode successor(RBNode node) {
        RBNode p;
        if (node == null) {
            return null;
        } else if (rightOf(node) != null) {
            p = rightOf(node);
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            p = getParent(node);
            RBNode pre = node;
            while (p != null && pre == p.right) {
                pre = p;
                p = getParent(p);
            }
            return p;
        }
    }

}


