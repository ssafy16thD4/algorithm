package com.ssafy.swb;

import java.io.*;
import java.util.*;

public class 게리멘더링 {

    static int N;
    static int[] population;
    static List<Integer>[] graph;
    static boolean[] selected;

    static int answer = Integer.MAX_VALUE;

    public static void main(String[] args) throws Exception {
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine().trim());

        population = new int[N];
        graph = new ArrayList[N];
        selected = new boolean[N];

        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<>();
        }

        StringTokenizer st =
                new StringTokenizer(br.readLine().trim());

        for (int i = 0; i < N; i++) {
            population[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine().trim());

            int count = Integer.parseInt(st.nextToken());

            for (int j = 0; j < count; j++) {
                int next = Integer.parseInt(st.nextToken()) - 1;
                graph[i].add(next);
            }
        }

        divide(0);

        System.out.println(
                answer == Integer.MAX_VALUE ? -1 : answer
        );
    }

    static void divide(int index) {
        if (index == N) {
            if (!isConnected(true) || !isConnected(false)) {
                return;
            }

            int aPopulation = 0;
            int bPopulation = 0;

            for (int i = 0; i < N; i++) {
                if (selected[i]) {
                    aPopulation += population[i];
                } else {
                    bPopulation += population[i];
                }
            }

            answer = Math.min(
                    answer,
                    Math.abs(aPopulation - bPopulation)
            );

            return;
        }

        selected[index] = true;
        divide(index + 1);

        selected[index] = false;
        divide(index + 1);
    }

    static boolean isConnected(boolean group) {
        int start = -1;
        int groupCount = 0;

        for (int i = 0; i < N; i++) {
            if (selected[i] == group) {
                groupCount++;

                if (start == -1) {
                    start = i;
                }
            }
        }

        if (start == -1) {
            return false;
        }

        boolean[] visited = new boolean[N];
        Queue<Integer> queue = new ArrayDeque<>();

        visited[start] = true;
        queue.offer(start);

        int visitedCount = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitedCount++;

            for (int next : graph[current]) {
                if (!visited[next] && selected[next] == group) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }

        return visitedCount == groupCount;
    }
}