package bf8;
import java.io.*;
import java.util.StringTokenizer;

public class Main {
	static int[][] sudoku;
	static boolean[][] domino;
	static boolean[][] check_row; //i��° row�� j�� �����ϴ���
	static boolean[][] check_col; //i���� column�� j�� �����ϴ���
	static boolean[][] check_sq; //i��° square�� j�� �����ϴ���
	static StringBuilder sb = new StringBuilder();
	static boolean state; //������ ã�Ҵ���
	
	public static void main(String[] args) {
		int test = 1; //testcase��ȣ
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st;
		
		try {
			int N = Integer.parseInt(br.readLine());
			while(N != 0) {
				sb.append("Puzzle ").append(test).append('\n');
				test++;
				
				check_row = new boolean[9][9];
				check_col = new boolean[9][9];
				check_sq = new boolean[9][9];
				sudoku = new int[9][9];
				domino = new boolean[9][9];
				
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9; j++) {
						sudoku[i][j] = -1;
					}
					domino[i][i] = true; //���� ���ڷ� �̷���� ���̳�� �������� �����Ƿ� �̹� ����ߴٰ� ����
				}
				//���� ���ִ� ���̳��
				for(int i = 0; i < N; i++) {
					st = new StringTokenizer(br.readLine());
					int num1 = Integer.parseInt(st.nextToken()) - 1;
					String loc1 = st.nextToken();
					int r1 = loc1.charAt(0) - 'A';
					int c1 = loc1.charAt(1) - '1';
					sudoku[r1][c1] = num1;
					check_row[r1][num1] = true;
					check_col[c1][num1] = true;
					check_sq[r1 / 3 * 3 + c1 / 3][num1] = true;
					
					int num2 = Integer.parseInt(st.nextToken()) - 1;
					String loc2 = st.nextToken();
					int r2 = loc2.charAt(0) - 'A';
					int c2 = loc2.charAt(1) - '1';
					sudoku[r2][c2] = num2;
					check_row[r2][num2] = true;
					check_col[c2][num2] = true;
					check_sq[r2 / 3 * 3 + c2 / 3][num2] = true;
					
					domino[num2][num1] = true;
					domino[num1][num2] = true;
				}
				//���� �ԷµǾ� �ִ� ���ڸ� ���ڵ�
				st = new StringTokenizer(br.readLine());
				for(int i = 0; i < 9; i++) {
					String loc = st.nextToken();
					int r = loc.charAt(0) - 'A';
					int c = loc.charAt(1) - '1';
					sudoku[loc.charAt(0) - 'A'][loc.charAt(1) - '1'] = i;
					check_row[r][i] = true;
					check_col[c][i] = true;
					check_sq[r / 3 * 3 + (c / 3)][i] = true;
				}
				
				go(0, 0);
				//'������ ã�Ҵ���'�� ���� ������ �ٽ� false�� �ʱ�ȭ
				state = false;
				
				N = Integer.parseInt(br.readLine());
			}
			
			bw.write(sb.toString());
			bw.close();
			br.close();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void go(int r, int c) {
		if(state) { //������ ã������ ���̻� ������� �ʾƵ� �ǹǷ� �ٷ� ����
			return;
		}
		if(r == 9) { //ó�� ������ ã�� ���
			print();
			state = true;
			return;
		}
		if(sudoku[r][c] != -1) { //�̹� ���ڰ� ���ִ� ĭ�� ���
			c++; //��ĭ���� �̵��Ѵ�.
			go(r + c / 9, c % 9); //c�� 8�� �Ѿ�� ���� �ٷ� �̵��ϵ���
			return;
		}
		
		for(int k = 0; k < 9; k++) { //��ĭ�� ��� 0~8���� �־��.
			if(!check(r, c, k)) { //�ش� row/col/square�� ���ڰ� �ִ� ���->�Ѿ��
				continue;
			}
			for(int l = 0; l < 9; l++) { //���̳��� �ٸ� ĭ���� 0~8���� �־��.
				if(domino[k][l]) //�̹� ����� ���̳��� ���->�Ѿ��.
					continue;
				//���̳븦 ���η� ���� ���
				if(c + 1 < 9) {
					if(sudoku[r][c + 1] == -1 && check(r, c + 1, l)) {
						sudoku[r][c] = k;
						sudoku[r][c + 1] = l;
						domino[k][l] = true;
						domino[l][k] = true;
						check_row[r][k] = true;
						check_col[c][k] = true;
						check_sq[r / 3 * 3 + c / 3][k] = true;
						check_row[r][l] = true;
						check_col[c + 1][l] = true;
						check_sq[r / 3 * 3 + (c + 1) / 3][l] = true;
						c++;
						go(r + c / 9, c % 9);
						c--;
						sudoku[r][c] = -1;
						sudoku[r][c + 1] = -1;
						domino[k][l] = false;
						domino[l][k] = false;
						check_row[r][k] = false;
						check_col[c][k] = false;
						check_sq[r / 3 * 3 + c / 3][k] = false;
						check_row[r][l] = false;
						check_col[c + 1][l] = false;
						check_sq[r / 3 * 3 + (c + 1) / 3][l] = false;
					}
				}
				//���̳븦 ���η� ���� ���
				if(r + 1 < 9) {
					if(sudoku[r + 1][c] == -1 && check(r + 1, c, l)) {
						sudoku[r][c] = k;
						sudoku[r + 1][c] = l;
						check_row[r][k] = true;
						check_col[c][k] = true;
						check_sq[r / 3 * 3 + c / 3][k] = true;
						check_row[r + 1][l] = true;
						check_col[c][l] = true;
						check_sq[(r + 1) / 3 * 3 + c / 3][l] = true;
						c++;
						go(r + c / 9, c % 9);
						c--;
						sudoku[r][c] = -1;
						sudoku[r + 1][c] = -1;
						check_row[r][k] = false;
						check_col[c][k] = false;
						check_sq[r / 3 * 3 + c / 3][k] = false;
						check_row[r + 1][l] = false;
						check_col[c][l] = false;
						check_sq[(r + 1) / 3 * 3 + c / 3][l] = false;
					}
				}
				
			}
		}
	}
	//�ش� row/col/square�� k��� ���ڰ� �̹� �����ϴ��� �Ǵ�
	public static boolean check(int r, int c, int k) {
		if(check_row[r][k] || check_col[c][k] || check_sq[r / 3 * 3 + c / 3][k]) {
			return false;
		}
		return true;
	}
	
	public static void print() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				sb.append(sudoku[i][j] + 1);
			}
			sb.append('\n');
		}
	}
}
