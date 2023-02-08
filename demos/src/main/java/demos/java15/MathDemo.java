package demos.java15;

public class MathDemo {

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Math.abs(Integer.MAX_VALUE));
        System.out.println(Math.abs(Integer.MIN_VALUE));

        System.out.println(Math.absExact(Integer.MIN_VALUE));
    }
}
