package bfs8;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K;
	static int[][] map;
	static int[][][] check;
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());
			map = new int[N][M];
			check = new int[N][M][K + 1];
			
			for(int i = 0; i < N; i++) {
				String input = br.readLine();
				for(int j = 0; j < M; j++) {
					map[i][j] = input.charAt(j) - '0';
				}
			}
			
			bfs();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void bfs() {
		Queue<Result> queue = new LinkedList<>();
		queue.add(new Result(0, 0, 0));
		check[0][0][0] = 1;
		while(!queue.isEmpty()) {
			Result cur = queue.remove();
			if(cur.i == N - 1 && cur.j == M - 1) {
				System.out.println(check[cur.i][cur.j][cur.broken]);
				return;
			}
			for(int i = 0; i < 4; i++) {
				int next_i = cur.i + dy[i];
				int next_j = cur.j + dx[i];
				if(next_i < 0 || next_j < 0 || next_i >= N || next_j >= M)
					continue;
				if(map[next_i][next_j] == 1) {
					if(cur.broken < K) {
						if(check[next_i][next_j][cur.broken + 1] != 0)
							continue;
						queue.add(new Result(next_i, next_j, cur.broken + 1));
						check[next_i][next_j][cur.broken + 1] = check[cur.i][cur.j][cur.broken] + 1;
					}
				}
				else {
					if(check[next_i][next_j][cur.broken] != 0)
						continue;
					queue.add(new Result(next_i, next_j, cur.broken));
					check[next_i][next_j][cur.broken] = check[cur.i][cur.j][cur.broken] + 1;
				}
				
			}
		}
		System.out.println(-1);
	}

}

class Result {
	int i, j;
	int broken;
	Result(int i, int j, int b){
		this.i = i;
		this.j = j;
		this.broken = b;
	}
}
