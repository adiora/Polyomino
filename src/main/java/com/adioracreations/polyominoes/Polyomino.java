package com.adioracreations.polyominoes;

import java.util.ArrayList;

class Polyomino {

    private final ArrayList<boolean[][]> combinations = new ArrayList<>();

    private final int order;

    Polyomino(int order) {
        this.order = order;
        initialize();
    }

    private void initialize() {
        boolean[][] combination = new boolean[order][order/2+1];

        for(int i = 0; i < order; i++)
            combination[i][0] = true;

        combinations.add(combination);
    }

    public void generate() {
        boolean[][] combination = combinations.get(0);
        combinations.set(0, getTrimmed(combination));

        generate(combination);
    }

    private void generate(boolean[][] combination) {
        for(int i = 0; i < combination.length; i++) {
            for(int j = 0; j < combination[0].length; j++) {

                if(combination[i][j] && getBounds(combination, i, j) == 1) {
                    for(int m = 0; m < combination.length; m++) {
                        for(int n = 0; n < combination[0].length; n++) {

                            combination[i][j] = false;
                            if(isValid(combination, m, n) && m != i && n != j) {

                                boolean[][] sub_combination = getDuplicate(combination);
                                sub_combination[m][n] = true;

                                boolean[][] trimmed = getTrimmed(sub_combination);

                                if(!isSame(trimmed)) {
                                    combinations.add(trimmed);
                                    generate(sub_combination);
                                }
                            }
                            combination[i][j] = true;
                        }
                    }
                }
            }
        }
    }

    private boolean isSame(boolean[][] combination) {

        for(boolean[][] sub_combination : combinations) {

            if(sub_combination.length == combination.length && sub_combination[0].length == combination[0].length
                    && (isEqual(sub_combination, combination) ||
                    isEqualHorizontally(sub_combination, combination) ||
                    isEqualVertically(sub_combination, combination) ||
                    isEqualVerticallyHorizontally(sub_combination, combination))) return true;

            else if(sub_combination.length == combination[0].length && sub_combination[0].length == combination.length) {
                boolean[][] rotated = getRotated(combination);
                if(isEqual(sub_combination, rotated) ||
                        isEqualHorizontally(sub_combination, rotated) ||
                        isEqualVertically(sub_combination, rotated) ||
                        isEqualVerticallyHorizontally(sub_combination, rotated)) return true;
            }
        }
        return false;
    }

    private static boolean[][] getDuplicate(boolean[][] combination) {
        boolean[][] duplicate = new boolean[combination.length][combination[0].length];
        for(int k = 0; k < combination.length; k++)
            System.arraycopy(combination[k], 0, duplicate[k], 0, combination[0].length);
        return duplicate;
    }

    private static boolean[][] getTrimmed(boolean[][] combination) {
        int startX = combination[0].length -1, endX = 0, startY = combination.length -1, endY = 0;

        for(int i = 0; i < combination.length; i++) {
            for (int j = 0; j < combination[0].length; j++) {
                if (combination[i][j]) {
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
                trimmed[m][n] = combination[startY][l];

        return trimmed;
    }

    private static boolean[][] getRotated(boolean[][] combination) {
        boolean[][] rotated = new boolean[combination[0].length][combination.length];

        for (int i = 0; i < combination[0].length; i++)
            for (int j = 0; j < combination.length; j++)
                rotated[i][j] = combination[combination.length - j -1][i];

        return rotated;
    }

    private static boolean isEqual(boolean[][] combination1, boolean[][] combination2) {
        if(combination1.length == combination2.length && combination1[0].length == combination2[0].length) {
            for (int i = 0; i < combination1.length; i++)
                for (int j = 0; j < combination1[0].length; j++)
                    if (combination1[i][j] != combination2[i][j]) return false;
            return true;
        }
        return false;
    }

    private static boolean isEqualHorizontally(boolean[][] combination1, boolean[][] combination2) {
        for(int i = 0; i < combination1.length; i++)
            for(int j = 0; j < combination1[0].length; j++)
                if(combination1[i][j] != combination2[i][combination1[0].length - j - 1]) return false;

        return true;
    }

    private static boolean isEqualVertically(boolean[][] combination1, boolean[][] combination2) {
        for(int i = 0; i < combination1.length; i++)
            for(int j = 0; j < combination1[0].length; j++)
                if(combination1[i][j] != combination2[combination1.length - i - 1][j]) return false;

        return true;
    }

    private static boolean isEqualVerticallyHorizontally(boolean[][] combination1, boolean[][] combination2) {
        for(int i = 0; i < combination1.length; i++)
            for(int j = 0; j < combination1[0].length; j++)
                if(combination1[i][j] != combination2[combination1.length - i - 1][combination1[0].length - j - 1]) return false;

        return true;
    }

    private static int getBounds(boolean[][] combination, int y, int x) {
        int bound = 0;

        if(isBound(combination, y, x-1)) bound++;
        if(isBound(combination, y, x+1)) bound++;
        if(isBound(combination, y-1, x)) bound++;
        if(isBound(combination, y+1, x)) bound++;

        return bound;
    }

    private static boolean exists(boolean[][] combination, int y, int x) {
        return x > -1 && x < combination[0].length && y > -1 && y < combination.length;
    }

    private static boolean isBound(boolean[][] combination, int y, int x) {
        return exists(combination, y, x) && combination[y][x];
    }

    private static boolean isValid(boolean[][] combination, int y, int x) {
        return !combination[y][x] && getBounds(combination, y, x) > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("All Combinations: \n");
        int i = 0;
        for (boolean[][] combination : combinations) {
            sb.append("Combination ");
            sb.append(++i);
            sb.append('\n');
            for (boolean[] rBoolean : combination) {
                for (boolean cBoolean : rBoolean)
                    sb.append(cBoolean? "1" : " ");
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}