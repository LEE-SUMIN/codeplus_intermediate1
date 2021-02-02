package bf11;
import java.io.*;
import java.util.StringTokenizer;

public class Main {
	static Marble red, blue;
	static int bi, bj; //���� ��ġ �ʱⰪ
	static int ri, rj;
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			char[][] board = new char[N][M];
			
			for(int i = 0; i < N; i++) {
				String input = br.readLine();
				for(int j = 0; j < M; j++) {
					board[i][j] = input.charAt(j);
					if(board[i][j] == 'B') {
						blue = new Marble(i, j);
						bi = i;
						bj = j;
					}
					else if(board[i][j] == 'R') {
						red = new Marble(i, j);
						ri = i;
						rj = j;
					}
				}
			}
			//���� ������ �Ķ� ���� ��ü�� ���� ���带 ����ȭ�����ֱ� ���� static������ ����
			Marble.board = new char[N][M];
			//�迭 deepcopy �Լ��� �̿��Ͽ� ���� ����
			red.set_board(board);
			
			//��Ʈ����ũ�� ���� ���� ������ �迭�� ��ȯ
			int[] dir = new int[10]; 
			int num = -1;
			for(int i = 0; i < (1 << 20); i++) {
				dir = set_dir(i);
				int ans = go(dir, 0, 0);
				if(num == -1 || (ans != -1 && ans < num)) {
					num = ans;
				}
				//�ٽ� �ʱ�ȭ
				blue = new Marble(bi, bj);
				red = new Marble(ri, rj);
				red.set_board(board);
			}
			
			System.out.println(num);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//��Ʈ����ũ�� ���� ���� ������ �迭�� ��ȯ
	public static int[] set_dir(int num) {
		int[] dir = new int[10];
		for(int i = 0; i < 10; i++) {
			dir[i] = (num & 3);
			num >>= 2;
		}
		return dir;
	}
	
	//�Է¹��� ���� �迭�� ������ �������� count���� ��ȯ�Ѵ�.
	public static int go(int[] dir, int idx, int count) {
		if(blue.isHole)
			return -1;
		if(red.isHole)
			return count;
		if(idx >= 10)
			return -1;
		//up-0, down-3, right-1, left-2
		if(idx != 0 && dir[idx] == dir[idx - 1]) //������ �������� ����� ���� ���
			return -1;
		if(idx != 0 && dir[idx] == ~dir[idx - 1]) //������ �������� ����� �ݴ��� ���
			return -1;
		
		boolean rmove = true, bmove = true;
		
		while(true) {
			rmove = red.move(dir[idx]);
			bmove = blue.move(dir[idx]);
			if(!rmove && !bmove) {
				break;
			}
		}
		
		int res = go(dir, idx + 1, count + 1);
		return res;
	}
	
	
}

class Marble {
	int r, c;
	boolean isHole = false;
	static char[][] board;
			
	Marble(int r, int c){
		this.r = r;
		this.c = c;
	}
	
	public void set_board(char[][] board) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				Marble.board[i][j] = board[i][j];
			}
		}
	}
	
	public boolean move(int dir) {
		int ni = r, nj = c;
		
		if(isHole) {
			return false;
		}
		switch(dir) {
		case 0: //up
			ni = r - 1;
			break;
		case 1: //right
			nj = c + 1;
			break;
		case 2: //left
			nj = c - 1;
			break;
		case 3: //down
			ni = r + 1;
			break;
		}
		if(board[ni][nj] == '.') { //���� ������ ���� ��ĭ�� ���,
			swap(r, c, ni, nj); //���忡�� ������ ��ġ�� �ű��.
			r = ni;
			c = nj;
			return true;
		}
		else if(board[ni][nj] == 'O') { //���� ������ ���� ������ ���,
			board[r][c] = '.'; //���� ���� ��ġ�� ��ĭ���� �����
			r = ni;
			c = nj;
			isHole = true; //�ش� ������ isHole ������ true�� �ٲ��ش�.
			return true;
		}
		return false;
	}
	
	public void swap(int i, int j, int ni, int nj) {
		char temp = board[i][j];
		board[i][j] = board[ni][nj];
		board[ni][nj] = temp;
	}
}