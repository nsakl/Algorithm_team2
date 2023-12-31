package worktotravel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Job {
    int day;
    int duration;
    int pay;

    public Job(int day, int duration, int pay) {
        this.day = day;
        this.duration = duration;
        this.pay = pay;
    }
}

public class Greedy {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("알바 일수를 입력하세요: ");
        int n = scanner.nextInt();

        Job[] jobs = new Job[n];

        // 알바 일수와 알바비 입력 받기
        for (int i = 0; i < n; i++) {
            System.out.print((i + 1) + "일차 알바 일수: ");
            int duration = scanner.nextInt();

            System.out.print((i + 1) + "일차 알바비: ");
            int pay = scanner.nextInt();

            jobs[i] = new Job(i + 1, duration, pay);
        }

        // 알바를 알바비가 높은 순서대로 정렬
        Arrays.sort(jobs, Comparator.comparingInt(job -> job.pay));

        int[] schedule = new int[n + 1];
        int maxProfit = 0;
        StringBuilder selectedDays = new StringBuilder();

        for (int i = 0; i < n; i++) {
            for (int j = Math.min(n, jobs[i].day + jobs[i].duration - 1); j >= jobs[i].day; j--) {
                // 해당 일에 알바를 할 수 있는 경우
                if (schedule[j] == 0) {
                    schedule[j] = jobs[i].pay;
                    maxProfit += jobs[i].pay;
                    selectedDays.append(jobs[i].day).append(" ");
                    break;
                }
            }
        }

        System.out.println("얻을 수 있는 최대 수익: " + maxProfit);
        System.out.println("선택한 알바의 날짜: " + selectedDays.toString().trim());
    }
}
