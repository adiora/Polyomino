package com.adioracreations.polyominoes;

import java.util.ArrayList;
import java.util.Stack;

class Polyomino {

    private final ArrayList<boolean[][]> patterns = new ArrayList<>();

    private final Stack<boolean[][]> rawPatterns = new Stack<>();

    private final int order;

    Polyomino(int order) {
        this.order = order;
        initialize();
    }

    private void initialize() {
        boolean[][] pattern = new boolean[order][order/2+1];

        for(int i = 0; i < order; i++)
            pattern[i][0] = true;

        patterns.add(getTrimmed(pattern));
        rawPatterns.add(pattern);
    }
    
    public void generate() {
        boolean[][] pattern;
        while (!rawPatterns.isEmpty()) {
            pattern = rawPatterns.pop();

            for (int i = 0; i < pattern.length; i++) {
                for (int j = 0; j < pattern[0].length; j++) {

                    if (pattern[i][j] && getBounds(pattern, i, j) == 1) {
                        for (int m = 0; m < pattern.length; m++) {
                            for (int n = 0; n < pattern[0].length; n++) {

                                pattern[i][j] = false;
                                if ((m != i || n != j) && isValid(pattern, m, n)) {

                                    boolean[][] nPattern = getDuplicate(pattern);
                                    nPattern[m][n] = true;

                                    boolean[][] trimmed = getTrimmed(nPattern);

                                    if (isNotSame(trimmed)) {
                                        patterns.add(trimmed);
                                        rawPatterns.push(nPattern);
                                    }
                                }
                                pattern[i][j] = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isNotSame(boolean[][] nPattern) {

        for(boolean[][] pattern : patterns) {

            if(pattern.length == nPattern.length && pattern[0].length == nPattern[0].length
                    && (isEqual(pattern, nPattern) ||
                    isEqualHorizontally(pattern, nPattern) ||
                    isEqualVertically(pattern, nPattern) ||
                    isEqualVerticallyHorizontally(pattern, nPattern))) return false;

            else if(pattern.length == nPattern[0].length && pattern[0].length == nPattern.length) {
                boolean[][] rotated = getRotated(nPattern);
                if(isEqual(pattern, rotated) ||
                        isEqualHorizontally(pattern, rotated) ||
                        isEqualVertically(pattern, rotated) ||
                        isEqualVerticallyHorizontally(pattern, rotated)) return false;
            }
        }
        return true;
    }

    private static boolean[][] getDuplicate(boolean[][] pattern) {
        boolean[][] duplicate = new boolean[pattern.length][pattern[0].length];
        for(int k = 0; k < pattern.length; k++)
            System.arraycopy(pattern[k], 0, duplicate[k], 0, pattern[0].length);
        return duplicate;
    }

    private static boolean[][] getTrimmed(boolean[][] pattern) {
        int startX = pattern[0].length -1, endX = 0, startY = pattern.length -1, endY = 0;

        for(int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                if (pattern[i][j]) {
                    if(startX > j)
                        startX = j;
                    if(startY > i)
                        startY = i;
                    if(endX < j)
                        endX = j;
                    if(endY < i)
                        endY = i;
                }
            }
        }

        endX++;
        endY++;

        boolean[][] trimmed = new boolean[endY - startY][endX - startX];

        for (int m = 0; startY < endY; m++, startY++)
            for (int n = 0, l = startX; l < endX; n++, l++)
                trimmed[m][n] = pattern[startY][l];

        return trimmed;
    }

    private static boolean[][] getRotated(boolean[][] pattern) {
        boolean[][] rotated = new boolean[pattern[0].length][pattern.length];

        for (int i = 0; i < pattern[0].length; i++)
            for (int j = 0; j < pattern.length; j++)
                rotated[i][j] = pattern[pattern.length - j -1][i];

        return rotated;
    }

    private static boolean isEqual(boolean[][] pattern1, boolean[][] pattern2) {
        if(pattern1.length == pattern2.length && pattern1[0].length == pattern2[0].length) {
            for (int i = 0; i < pattern1.length; i++)
                for (int j = 0; j < pattern1[0].length; j++)
                    if (pattern1[i][j] != pattern2[i][j]) return false;
            return true;
        }
        return false;
    }

    private static boolean isEqualHorizontally(boolean[][] pattern1, boolean[][] pattern2) {
        for(int i = 0; i < pattern1.length; i++)
            for(int j = 0; j < pattern1[0].length; j++)
                if(pattern1[i][j] != pattern2[i][pattern1[0].length - j - 1]) return false;

        return true;
    }

    private static boolean isEqualVertically(boolean[][] pattern1, boolean[][] pattern2) {
        for(int i = 0; i < pattern1.length; i++)
            for(int j = 0; j < pattern1[0].length; j++)
                if(pattern1[i][j] != pattern2[pattern1.length - i - 1][j]) return false;

        return true;
    }

    private static boolean isEqualVerticallyHorizontally(boolean[][] pattern1, boolean[][] pattern2) {
        for(int i = 0; i < pattern1.length; i++)
            for(int j = 0; j < pattern1[0].length; j++)
                if(pattern1[i][j] != pattern2[pattern1.length - i - 1][pattern1[0].length - j - 1]) return false;

        return true;
    }

    private static int getBounds(boolean[][] pattern, int y, int x) {
        int bound = 0;

        if(isBound(pattern, y, x-1)) bound++;
        if(isBound(pattern, y, x+1)) bound++;
        if(isBound(pattern, y-1, x)) bound++;
        if(isBound(pattern, y+1, x)) bound++;

        return bound;
    }

    private static boolean exists(boolean[][] pattern, int y, int x) {
        return x > -1 && x < pattern[0].length && y > -1 && y < pattern.length;
    }

    private static boolean isBound(boolean[][] pattern, int y, int x) {
        return exists(pattern, y, x) && pattern[y][x];
    }

    private static boolean isValid(boolean[][] pattern, int y, int x) {
        return !pattern[y][x] && getBounds(pattern, y, x) > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("All patterns: \n");
        int i = 0;
        for (boolean[][] pattern : patterns) {
            sb.append("pattern ");
            sb.append(++i);
            sb.append('\n');
            for (boolean[] rBoolean : pattern) {
                for (boolean cBoolean : rBoolean)
                    sb.append(cBoolean? "1" : " ");
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public void print(boolean[][] pattern) {
        for (boolean[] booleans : pattern) {
            for (boolean aBoolean : booleans) System.out.print(aBoolean ? "1" : " ");
            System.out.println();
        }
    }
}
