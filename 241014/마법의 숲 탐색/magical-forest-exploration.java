import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int R, C, K;
    static int[][] forest;
    static boolean[][] visited;
    static Pos cur;
    static int[] DX = {0, 1, 0, -1};
    static int[] DY = {-1, 0, 1, 0};

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        forest = new int[R + 1][C + 1];
        visited = new boolean[R + 1][C + 1];
        int score = 0;
        for(int k = 0; k< K; k++) {
            st = new StringTokenizer(br.readLine());
            int c = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            cur = new Pos(0, c, d);
            locate();
            score += calcuateScore();

        }


        System.out.println(score);
    }

    private static int calcuateScore() {
        if(cur.r <= 1) {
            return 0;
        }
        for(int i=0; i<=R; i++) {
            Arrays.fill(visited[i], false);
        }
        int maxR = cur.r;
        Queue<Pos> q = new ArrayDeque<>();
        q.offer(cur);
        while(!q.isEmpty()) {
            Pos pos = q.poll();
            visited[pos.r][pos.c] = true;
            for(int i=0; i<4; i++) {
                int nr = pos.r + DY[i];
                int nc = pos.c + DX[i];
                if (0 <= nr && nr <= R && 0 < nc && nc <= C) {
                    visited[nr][nc] = true;
                    maxR = Math.max(maxR, nr);
                }
            }
            // 출구 찾기
            int r = pos.r + DY[pos.d];
            int c = pos.c + DX[pos.d];

            // 중심 찾기
            for(int i=0; i<4; i++) {
                int nr = r + DY[i];
                int nc = c + DX[i];
                if (0 <= nr && nr <= R && 0 < nc && nc <= C) {
                    if(!visited[nr][nc] && forest[nr][nc] > 0) {
                        for(int j=0; j<4; j++) {
                            int _nr = nr + DY[j];
                            int _nc = nc + DX[j];
                            if (0 <= _nr && _nr <= R && 0 < _nc && _nc <= C) {
                                if(!visited[_nr][_nc] && forest[_nr][_nc] < 0) {
                                    q.offer(new Pos(_nr, _nc, -forest[_nr][_nc] - 1));
                                }
                            }
                        }
                    }
                }
            }
        }

        return maxR;
    }

    private static void clearForest() {
        for(int i=0; i<=R; i++) {
            Arrays.fill(forest[i], 0);
        }
    }

    private static void locate() {
        while(true) {
            if (!moveDown()) {
                if (!rotateLeft()) {
                    if (!rotateRight()) {
                        break;
                    }
                }
            }
        }
        if(cur.r == 0) {
            clearForest();
        } else {
            forest[cur.r][cur.c] = -cur.d - 1;
            for(int i=0; i<4; i++) {
                int nr = cur.r + DY[i];
                int nc = cur.c + DX[i];
                forest[nr][nc] = 1;
            }
            int nr = cur.r + DY[cur.d];
            int nc = cur.c + DX[cur.d];
            forest[nr][nc] = 2;
        }
    }

    private static boolean rotateRight() {
        int r = cur.r;
        int c = cur.c;
        int[] dc = {1, 2, 1};
        int[] dr = {-1, 0, 1};
        for(int i=0; i<3; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if(0 <= nr && nr <= R && 0 < nc && nc <= C) {
                if (forest[nr][nc] != 0) {
                    return false;
                }
            } else {
                return false;
            }
        }
        cur.c++;
        if(moveDown()) {
            cur.d = (cur.d + 1) % 4;
            return true;
        } else {
            cur.c--; // 복구
            return false;
        }
    }

    private static boolean rotateLeft() {
        int r = cur.r;
        int c = cur.c;
        int[] dc = {-1, -2, -1};
        int[] dr = {-1, 0, 1};
        for(int i=0; i<3; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if(0 <= nr && nr <= R && 0 < nc && nc <= C) {
                if (forest[nr][nc] != 0) {
                    return false;
                }
            } else {
                return false;
            }
        }
        cur.c--;
        if(moveDown()) {
            cur.d = (cur.d + 3) % 4;
            return true;
        } else {
            cur.c++; // 복구
            return false;
        }
    }

    private static boolean moveDown() {
        int r = cur.r;
        int c = cur.c;
        int[] dc = {-1, 0, 1};
        int[] dr = {1, 2, 1};
        for(int i=0; i<3; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if(0 <= nr && nr <= R && 0 < nc && nc <= C) {
                if(forest[nr][nc] != 0) {
                    return false;
                }
            } else {
                return false;
            }
        }
        cur.r++;
        return true;
    }

    static class Pos {
        int r;
        int c;
        int d;

        Pos(int r, int c, int d) {
            this.r = r;
            this.c = c;
            this.d = d;
        }

        @Override
        public String toString() {
            return "Pos: (" + r + ", " + c + "), d=" + d;
        }
    }
}