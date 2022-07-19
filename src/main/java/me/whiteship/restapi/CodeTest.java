package me.whiteship.restapi;

import java.util.*;

public class CodeTest {

    public static int solution(int[][] sizes){
        int answer = 0;

        // 작은수 중 큰 수 * 큰수중 큰수
        List<Integer> min = new ArrayList<>();
        List<Integer> max = new ArrayList<>();

        for(int i=0; i< sizes.length; i++){
            if(sizes[i][0] < sizes[i][1]){
                min.add(sizes[i][0]);
                max.add(sizes[i][1]);
            }else{
                min.add(sizes[i][1]);
                max.add(sizes[i][0]);
            }
        }

        answer = Collections.max(min) * Collections.max(max);
        return answer;
    }

    public static void main(String[] args) {
        int[][] num = {{60, 50}, {30, 70}, {60, 30}, {80, 40}};
        solution(num);
    }
}
