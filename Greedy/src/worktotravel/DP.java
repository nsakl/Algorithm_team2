package worktotravel;

import java.util.*;
import java.io.*;

public class DP {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int totalDays = Integer.parseInt(br.readLine()); // A씨가 여행을 떠나기까지의 총 일수
		
		String[] workNames = new String[totalDays + 1];
		int[] workDays = new int[totalDays + 1];
		int[] payment = new int[totalDays + 1];
		int[] maxPayment = new int[totalDays + 2];
		
		for(int i = 1; i <= totalDays; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			workNames[i] = st.nextToken(); // 알바 이름
			workDays[i] = Integer.parseInt(st.nextToken()); // 알바하는데 걸리는 일 수
			payment[i] = Integer.parseInt(st.nextToken()); // 받을 수 있는 알바비
		}
		
		for(int i = totalDays; i > 0; i--) {
			int nextDay = i + workDays[i]; // 다음 날짜
			
			if(nextDay > totalDays + 1) { // 일할 수 있는 날짜를 넘어가는 경우
				// 일을 하지 못하므로 바로 다음 maxPayment값(더 앞쪽의 날짜)로 설정
				maxPayment[i] = maxPayment[i + 1];
			} else { // 일할 수 있는 날짜인 경우
				// 1. 일하지 않았을 때(maxPayment[i + 1])
				// 2. 일 했을 때 총 벌 수 있는 알바비(payment[i] + maxPayment[nextDay])
				// 위 두 경우 중 더 큰 값을 maxPayment에 넣어준다.
				maxPayment[i] = Math.max(maxPayment[i + 1], payment[i] + maxPayment[nextDay]);
			}
		}
		
		System.out.println("Optimal Work Sequence:");
		findOptimalWorkSequence(workNames, workDays, payment, maxPayment, 1);
		System.out.println("\nMaximum Payment: " + maxPayment[1]);
	}

	private static void findOptimalWorkSequence(String[] workNames, int[] workDays, int[] payment, int[] maxPayment, int currentDay) {
        if (currentDay >= maxPayment.length - 1) {
            return;
        }

        if (maxPayment[currentDay] != maxPayment[currentDay + 1]) {
            System.out.println(workNames[currentDay] + " - Work Days: " + workDays[currentDay] + ", Payment: " + payment[currentDay]);
            findOptimalWorkSequence(workNames, workDays, payment, maxPayment, currentDay + workDays[currentDay]);
        } else {
            findOptimalWorkSequence(workNames, workDays, payment, maxPayment, currentDay + 1);
        }
    }
}
