package org.example;

import java.util.*;

public class Main {
    static class Node {
        int city;
        int cost;
        List<Integer> path;

        public Node(int city, int cost, List<Integer> path) {
            this.city = city;
            this.cost = cost;
            this.path = new ArrayList<>(path);
        }
    }

    public static int tspBFS(int[][] graph) {
        int n = graph.length;
        int minCost = Integer.MAX_VALUE;
        int iterations = 0;
        List<Integer> bestPath = new ArrayList<>();

        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(0, 0, List.of(0)));

        // BFS
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            iterations++;
//            System.out.println("Ітерація пошуку вшир #" + iterations + ":\n" +
//                    " місто " + current.city + ", вартість " + current.cost + ",\n" +
//                    " шлях " + current.path);

            if (current.path.size() == n) {
                int returnCost = graph[current.city][0];
                if (returnCost > 0) {
                    int totalCost = current.cost + returnCost;
                    if (totalCost < minCost) {
                        minCost = totalCost;
                        bestPath = new ArrayList<>(current.path);
                        bestPath.add(0);
                    }
                }
                continue;
            }

            for (int i = 0; i < n; i++) {
                if (!current.path.contains(i) && graph[current.city][i] > 0) {
                    List<Integer> newPath = new ArrayList<>(current.path);
                    newPath.add(i);
                    queue.offer(new Node(i, current.cost + graph[current.city][i], newPath));
                }
            }
        }

        System.out.println("Загальная к-сть ітерацій пошуку вшир: " + iterations);
        System.out.println("Найдешевший шлях: " + bestPath);
        return minCost == Integer.MAX_VALUE ? -1 : minCost;
    }

    public static int tspNearestNeighbor(int[][] graph) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        int cost = 0;
        int currentCity = 0;
        int iterations = 0;

        visited[0] = true;
        List<Integer> path = new ArrayList<>();
        path.add(0);

        for (int i = 0; i < n - 1; i++) {
            iterations++;
            int nearestCity = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && graph[currentCity][j] > 0 && graph[currentCity][j] < minDistance) {
                    nearestCity = j;
                    minDistance = graph[currentCity][j];
                }
            }

            if (nearestCity != -1) {
//                System.out.println("Ітерація ближ сусіда: " + iterations + ";\n" +
//                        " поточне місто " + currentCity + ", наступне місто " + nearestCity + ",\n" +
//                        " ціна " + minDistance);

                cost += minDistance;
                visited[nearestCity] = true;
                currentCity = nearestCity;
                path.add(nearestCity);
            }
        }

        // Возвращаемся в начальный город
        if (graph[currentCity][0] > 0) {
            cost += graph[currentCity][0];
            path.add(0);
        } else {
            return -1;
        }

        System.out.println("Шлях найближчого міста: " + path);
        System.out.println("Загальна к-сть ітерацій: " + iterations);
        return cost;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть к-сть міст: ");
        int n = scanner.nextInt();

        int[][] graph = new int[n][n];
        System.out.println("Введіть матрицю відстаней:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = scanner.nextInt();
            }
        }

        int bfsResult = tspBFS(graph);
        if (bfsResult != -1) {
            System.out.println("Мінімальна вартість шляху (вшир): " + bfsResult);
        } else {
            System.out.println("Неможливо знайти шлях");
        }

        int nnResult = tspNearestNeighbor(graph);
        if (nnResult != -1) {
            System.out.println("Мінімальна вартість шляху (найближчий сусід): " + nnResult);
        } else {
            System.out.println("Неможливо знайти шлях");
        }

        scanner.close();
    }
}
