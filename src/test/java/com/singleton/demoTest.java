package com.singleton;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Author Liwei
 * @Date 2021/8/23 23:52
 */
public class demoTest {
    @Test
    public void test01() {
        int[] arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (99 - 10 + 1) + 10);
            System.out.println(arr[i]);
        }
        System.out.println("---------");
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        System.out.println(max);
        System.out.println("---------");
        Arrays.stream(arr).max().ifPresent(System.out::println);
    }

    @Test
    public void test02() {
        int[] arr = {1, 3, 5, 7, 9, 11};
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test03() {
        int[] arr = {1, 3, 5, 7, 9, 11};
//        int index = getIndex3(arr, 7);
//        System.out.println(index);

        int[] arr2 = {11, 9, 5, 7, 3, 1};
        bubbleSort(arr2);
        System.out.println(Arrays.toString(arr2));
    }

    public static int getIndex(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (value == arr[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndex2(int[] arr, int value) {
        int min = 0;
        int max = arr.length - 1;
        int mid = (max + min) / 2;
        while (arr[mid] != value) {
            if (arr[mid] > value) {
                max = mid - 1;
            } else if (arr[mid] < value) {
                min = mid + 1;
            }
            if (min > max) {
                return -1;
            }
            mid = (max + min) / 2;
        }
        return mid;
    }

    public static int getIndex3(int[] arr, int value) {

        int head = 0;
        int end = arr.length - 1;
        while (head <= end) {
            int mid = (head + end) / 2;
            if (arr[mid] == value) {
                return mid;
            } else if (arr[mid] > value) {
                end = mid - 1;
            } else if (arr[mid] < value) {
                head = mid + 1;
            }
        }
        return -1;
    }
    public static void bubbleSort(int[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = 0; j < arr.length-1-i; j++) {
                if (arr[j]>arr[j+1]){
                    int temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }
        }
    }
    @Test
    public void recursive(){
        System.out.println(sum(100));
        System.out.println("---");
        System.out.println(f(10));
    }
    public int sum(int num){
        if (num==1){
            return 1;
        }else {
            return num+sum(num-1);
        }
    }
    public int f(int num){
        if (num==0){
            return 1;
        }else if(num==1){
            return 4;
        }else {
            return 2*f(num-1)+f(num-2);
        }
    }
}
