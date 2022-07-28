package me.whiteship.restapi;

import java.util.*;

public class CodeTest {

    public static int[] solution(int[] arr) {
        int[] answer = {};
        ArrayList<Integer> list = new ArrayList<>();

        int compareNum = 10;

        for(int num : arr){
            if(compareNum != num){
                list.add(num);
            }

            compareNum = num;
        }

        answer = new int[list.size()];

        for(int i=0; i< answer.length;i++){
          answer[i] =  list.get(i).intValue();
        }
        return answer;
    }

    public static void main(String[] args) {
        int[] list = {1,1,3,3, 0, 1,1};
        solution(list);
    }
}
