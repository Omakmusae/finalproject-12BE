package com.example.finalproject12be;

import java.util.*;

public class test {

	public static void main(String[] args) {

		int[] array = {1, 5, 2, 6, 3, 7, 4};

		int[][] commands = {{2, 5, 3}, {4, 4, 1}, {1, 7, 3}};


		int[] answer = new int[commands.length];

		for (int i=0; i<commands.length; i++) {
			List<Integer> arrInt = new ArrayList<>();

			for (int j=commands[i][0]-1; j<commands[i][1]; j++) {
				arrInt.add(array[j]);
			}
			Collections.sort(arrInt);
			answer[i] = arrInt.get(commands[i][2]-1);

		}

	}


	public int solution(int[][] sizes) {
		int answer = 0;
		int max_v=0;
		int max_h=0;
		for(int i=0;i<sizes.length;i++){
			int v=Math.max(sizes[i][0],sizes[i][1]);
			int h=Math.min(sizes[i][0],sizes[i][1]);
			max_v=Math.max(max_v,v);
			max_h=Math.max(max_h,h);
		}
		return answer=max_v*max_h;
	}





}
