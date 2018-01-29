package d.kh.fun.string;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
    public static void main(String[] args) {
        String s = "dima";
        List<String> permutations = permutation(s.toCharArray(), new char[s.length()], 0, new ArrayList<>());
        System.out.println(permutations);
    }

    private static List<String> permutation(char[] source, char[] dest, int pos, List<String> result) {
        if (pos == dest.length) {
            result.add(new String(dest));
        } else {
            for (int i = 0; i < source.length; i++) {
                dest[pos] = source[i];
                permutation(cutOutChar(source, i), dest, pos + 1, result);
            }
        }
        return result;
    }

    private static char[] cutOutChar(char[] chars, int pos) {
        char[] result = new char[chars.length - 1];
        System.arraycopy(chars, 0, result, 0, pos);
        System.arraycopy(chars, pos + 1, result, pos, result.length - pos);
        return result;
    }
}
