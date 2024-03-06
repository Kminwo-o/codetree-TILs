import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int l, n, q;
    static int[][] knightArr;
    static int[][] arr;
    static Knight[] knights;
    static int[][] command;
    static int[][] dir = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    static class Knight {
        int x;
        int y;
        int h;
        int w;
        int health;
        int damage;
        boolean isDamage;

        public Knight(int x, int y, int h, int w, int health, int damage, boolean isDamage) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.health = health;
            this.damage = damage;
            this.isDamage = isDamage;
        }
    }
    public static void moveKnight(int knight, int direction) {
        for (int i = knights[knight].x ; i < knights[knight].x + knights[knight].h; i++) {
            for (int j = knights[knight].y; j < knights[knight].y + knights[knight].w; j++) {
                int nextX = i + dir[direction][0];
                int nextY = j + dir[direction][1];

                if (knightArr[nextX][nextY] == 0 || knightArr[nextX][nextY] == knight) continue;

                moveKnight(knightArr[nextX][nextY], direction);
            }
        }

        switch (direction) {
            case 0:
                for (int i = knights[knight].x; i < knights[knight].x + knights[knight].h; i++) {
                    for (int j = knights[knight].y; j < knights[knight].y + knights[knight].w; j++) {
                        int nextX = i + dir[direction][0];
                        int nextY = j + dir[direction][1];

                        knightArr[i][j] = 0;
                        knightArr[nextX][nextY] = knight;
                    }
                }
                break;
            case 1:
                for (int i = knights[knight].y + knights[knight].w - 1; i >= knights[knight].y; i--) {
                    for (int j = knights[knight].x; j < knights[knight].x + knights[knight].h; j++) {
                        int nextX = j + dir[direction][0];
                        int nextY = i + dir[direction][1];

                        knightArr[j][i] = 0;
                        knightArr[nextX][nextY] = knight;
                    }
                }
                break;
            case 2:
                for (int i = knights[knight].x + knights[knight].h - 1; i >= knights[knight].x ; i--) {
                    for (int j = knights[knight].y; j < knights[knight].y + knights[knight].w; j++) {
                        int nextX = i + dir[direction][0];
                        int nextY = j + dir[direction][1];

                        knightArr[i][j] = 0;
                        knightArr[nextX][nextY] = knight;
                    }
                }
                break;
            case 3:
                for (int i = knights[knight].y; i < knights[knight].y + knights[knight].w; i++) {
                    for (int j = knights[knight].x; j < knights[knight].x + knights[knight].h; j++) {
                        int nextX = j + dir[direction][0];
                        int nextY = i + dir[direction][1];

                        knightArr[j][i] = 0;
                        knightArr[nextX][nextY] = knight;
                    }
                }
                break;
        }

        knights[knight].isDamage = true;
        knights[knight].x += dir[direction][0];
        knights[knight].y += dir[direction][1];
    }

    public static boolean isWall(int x, int y) {
        return 0 > x || x >= l || 0 > y || y >= l || arr[x][y] == 2;
    }


    private static boolean checkedAround(int knight, int direction) {
        for (int i = knights[knight].x; i < knights[knight].x + knights[knight].h; i++) {
            for (int j = knights[knight].y; j < knights[knight].y + knights[knight].w; j++) {
                int nextX = i + dir[direction][0];
                int nextY = j + dir[direction][1];

                if (isWall(nextX, nextY)) return false;

                if (knightArr[nextX][nextY] == 0 || knightArr[nextX][nextY] == knight) continue;

                if (!checkedAround(knightArr[nextX][nextY], direction)) return false;
            }
        }
        return true;
    }

    public static void checkedDamage() {
        for (int i = 1; i <= n; i++) {
            if (!knights[i].isDamage) continue;

            int damage = checkedTrap(i);

            knights[i].health -= damage;
            knights[i].damage += damage;

            if (knights[i].health <= 0) {
                for (int j = knights[i].x; j < knights[i].x + knights[i].h; j++) {
                    for (int k = knights[i].y; k < knights[i].y + knights[i].w; k++) {
                        knightArr[j][k] = 0;
                    }
                }
            }
        }
    }

    public static int checkedTrap(int knight) {
        int trapCnt = 0;

        for (int i = knights[knight].x; i < knights[knight].x + knights[knight].h; i++) {
            for (int j = knights[knight].y; j < knights[knight].y + knights[knight].w; j++) {
                if (arr[i][j] == 1) {
                    trapCnt++;
                }
            }
        }

        return trapCnt;
    }

    public static int totalDamage() {
        int damage = 0;
        for (int i = 1; i <= n; i++) {
            if (knights[i].health <= 0) continue;

            damage += knights[i].damage;
        }

        return damage;
    }

    public static void commandOfKing(int knight, int direction) {
        if (knights[knight].health <= 0) return;
        if (!checkedAround(knight, direction)) return;

        moveKnight(knight, direction);
        knights[knight].isDamage = false;
        checkedDamage();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        l = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        arr = new int[l][l];
        for (int i = 0; i < l; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < l; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[n+1];
        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int health = Integer.parseInt(st.nextToken());

            knights[i] = new Knight(r, c, h, w, health, 0, false);
        }

        knightArr = new int[l][l];
        for (int i = 1; i <= n; i++) {
            Knight knight = knights[i];

            for (int j = 0; j < knight.h; j++) {
                for (int k = 0; k < knight.w; k++) {
                    knightArr[knight.x + j][knight.y + k] = i;
                }
            }
        }

        command = new int[q][2];
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());

            command[i][0] = Integer.parseInt(st.nextToken());
            command[i][1] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < q; i++) {
            commandOfKing(command[i][0], command[i][1]);

            for (int j = 1; j <= n; j++) {
                knights[j].isDamage = false;
            }
        }

        System.out.println(totalDamage());
        br.close();
    }
}