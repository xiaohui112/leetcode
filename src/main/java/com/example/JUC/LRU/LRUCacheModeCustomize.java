package com.example.JUC.LRU;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义LRU算法，手撸
 * 类似于AQS的数据结构
 * 1.需要定义内部Node存储k,v
 * 2.需要一个双向链表来管理node，更新新鲜度
 * 3.capacity 定义容量大小
 */
public class LRUCacheModeCustomize {

    /**
     * 节点类
     * @param <K,V>
     */
    class Node<K,V>{
         K key;
         V value;
        //前后指针
         Node prev;
         Node next;

        public Node(K key,V value){
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }

        public Node() {
            this.prev = this.next = null;
        }
    }

    class DoubleLinkedList<K,V>{
        //头尾指针
        Node head;
        Node tail;

        //构造方法
        public DoubleLinkedList(){
            this.head = new Node();
            this.tail = new Node();
            //收尾相连
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }

        //增加节点  每次增加到头部
        public void addToHead(Node node){
            //node的前后指针 指向链表头尾
            node.next = head.next;
            node.prev = head;
            //将头节点的下一个节点（插入前的第一个节点）的前指针指向 node
            this.head.next.prev = node;
            //再将头节点的后指针指向node
            this.head.next = node;
            //this.tail.prev = node;//这里不能直接操作尾结点tail，多个节点时，head.next ！= tail.prev
        }
        //和删除节点方法
        public void removeNode(Node node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }

        //获取最后一个节点
        public Node getLastNode(){
            return this.tail.prev;
        }
    }


    //自定义类属性
    private int capacity;
    Map<Integer,Node<Integer,Integer>> map; //真正存储K,V的地方，查找快
    DoubleLinkedList<Integer,Integer> doubleLinkedList;//管理新鲜度的 双向链表，不需要管理K,V的值

    public LRUCacheModeCustomize(int capacity){
        this.capacity = capacity;
        map = new HashMap<>();
        doubleLinkedList = new DoubleLinkedList<>();
    }

    //查找key的value，然后用doubleLinkList管理新鲜度
    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        Node<Integer, Integer> node = map.get(key);
        //更新doubleLinkedList中的位置,不关心原来在哪，先删除再插入到头部
        doubleLinkedList.removeNode(node);
        doubleLinkedList.addToHead(node);

        return node.value;
    }

    public void put(int key,int value){
        if (map.containsKey(key)){//更新value值
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addToHead(node);
        }else{//不存在
            if(map.size() == capacity){//达到最大容量
                //获取doubleLinkedList尾部节点 删除，再插入新节点
                Node lastNode = doubleLinkedList.getLastNode();
                map.remove(lastNode.key);//不要忘记删除map中的数据
                doubleLinkedList.removeNode(lastNode);
            }
            //新增节点
            Node node = new Node(key, value);
            map.put(key,node);
            doubleLinkedList.addToHead(node);

        }
    }

    public static void printDoubleLinkedList(Node tail){
        Node node = tail.prev;
        StringBuffer stringBuffer = new StringBuffer("[");
        while(node!=null){
            stringBuffer.append(node.key);
            if (node.prev.key!=null){
                stringBuffer.append(",");
            }else{
                stringBuffer.append("]");
                break;
            }
            node = node.prev;
        }
        System.out.println(stringBuffer.toString());
    }

    public static void main(String[] args) {
        LRUCacheModeCustomize mode = new LRUCacheModeCustomize(3);
        mode.put(1,1);
        mode.put(2,2);
        mode.put(3,3);

        printDoubleLinkedList(mode.doubleLinkedList.tail);
        //新插入4，容量已满时，最先插入的 1会被删除
        mode.put(4,4);
        printDoubleLinkedList(mode.doubleLinkedList.tail);
        //访问一个key，根据accessOrder刷新key的新鲜度
        mode.get(3);
        printDoubleLinkedList(mode.doubleLinkedList.tail);
        mode.put(3,1000);
        printDoubleLinkedList(mode.doubleLinkedList.tail);
        mode.put(3,100);
        printDoubleLinkedList(mode.doubleLinkedList.tail);

        //在插入新key，
        mode.put(5,5);
        printDoubleLinkedList(mode.doubleLinkedList.tail);

    }

    /**
     * [1, 2, 3]
     * [2, 3, 4]
     * [2, 4, 3]
     * [2, 4, 3]
     * [2, 4, 3]
     * [4, 3, 5]
     */
}
