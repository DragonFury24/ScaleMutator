import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

//Rev. Dr. Douglas R Oberle - Washington, DC
public class scaleMutator {
    //pre:  a and b are valid index #s of list, which is not null
    //post: swaps list[a] with list[b]  - modifies the array that is sent
    //i.e., given list:[1,2,3,4], a:1, b:3 -> changes list to [1,4,3,2]
    public static void swap(int[] list, int a, int b) {
        int temp = list[a];
        list[a] = list[b];
        list[b] = temp;
    }

    //pre:  list is not null
    //post: nums is sorted in ascending order - modifies the array that is sent
    //i.e., given list:[4,2,7,5] -> changes list to [2,4,5,7]
    //needs to work with an array of any size
    public static void selSort(int[] list) {
        for (int i = 0; i < list.length; i++) {
            int minIndex = i;

            for (int j = i + 1; j < list.length; j++)
                if (list[j] < list[minIndex])
                    minIndex = j;
            if (list[minIndex] < list[i])
                swap(list, minIndex, i);
        }
    }

    //pre:   list is not null
    //post:  returns a new array with the same elements of list, but scrambled (put in random order)
    //needs to work with an array of any size
    public static int[] scramble(int[] list) {

        //Swap version
//        int[] scramble = list.clone();
//        Random gen = new Random(121);
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; i < scramble.length; j++) {
//                swap(scramble, j, gen.nextInt(scramble.length));
//            }
//        }
//
//        return scramble;

        ArrayList<Integer> scramble = new ArrayList<Integer>();

        Arrays.stream(list).forEach(scramble::add);
        Collections.shuffle(scramble);

        return scramble.stream()
                       .mapToInt(Integer::intValue)
                       .toArray();
    }

    //pre:  list is not null
    //post: returns a new array with elements in list, but in reverse order
    //i.e., given list:[1,2,3,4] -> returns [4,3,2,1]
    //needs to work with an array of any size
    public static int[] reverse(int[] list) {
        //ArrayList version
//        ArrayList<Integer> reverse = new ArrayList<>();
//
//        for (int i = list.length - 1; i >= 0; i--)
//            reverse.add(list[i]);
//
//        return reverse.stream().mapToInt(Integer::intValue).toArray();

        int[] reverse = Arrays.copyOf(list, list.length);

        IntStream.range(0, reverse.length / 2)
                 .forEach(i -> swap(reverse, i, reverse.length - i - 1));

        return reverse;
    }

    //pre:  list is not null
    //post: returns a new array (almost double the size of list) with the elements of list as a palindrome.
    //i.e., given list:[1,2,3,4] -> returns [1,2,3,4,3,2,1]
    //needs to work with an array of any size
    public static int[] makePalindrome(int[] list) {
        //ArrayList Version - Test reverse, remove loop

//        ArrayList<Integer> palindrome = new ArrayList<>();
//        Arrays.stream(list).forEach(palindrome::add);
//
//        for (int i = list.length - 2; i >= 0; i--)
//            palindrome.add(list[i]);
//
//        return palindrome.stream().mapToInt(Integer::intValue).toArray();


        int[] palindrome = new int[(list.length * 2) - 1];

        System.arraycopy(list, 0, palindrome, 0, list.length);
        System.arraycopy(reverse(list), 1, palindrome, list.length, palindrome.length - list.length);

        return palindrome;
    }

    //pre:  neither a nor b are null, assume a.length == b.length
    //post: returns a new array with elements shuffles in from two lists,
    //alternating between advancing elements of lists a and b
    //[a0, b0, a1, b1, a2, b2, a3, b3, a4, b4,...]
    //i.e., given a:[0,1,2,3], b:[9,8,7,6] -> returns [0,9,1,8,2,7,3,6]
    //needs to work with arrays of any size
    public static int[] shuffle(int[] a, int[] b) {
        int[] shuffle = new int[a.length + b.length];

        //Copy all elements of @a to even numbered indexes in @shuffle
        IntStream.range(0, a.length)
                 .forEach(i -> shuffle[2 * i] = a[i]);
        //Copy all elements of @b to odd numbered indexes in @shuffle
        IntStream.rangeClosed(1, b.length)
                 .forEach(i -> shuffle[(2 * i) - 1] = b[i - 1]);

        return shuffle;
    }

    //pre:  list is not null
    //post: returns a new array with elements of list, but shuffled-
    //alternating between elements from the beginning moving right and elements from the end moving left
    //[1st elem, Last elem, 2nd elem, 2nd to last elem, 3rd elem, 3rd to last elem, 4th elem, 4th to last elem,...]
    //i.e., given list:[0,1,2,3,4,5,6,7] -> returns [0,7,1,6,2,5,3,4]
    //needs to work with an array of any size
    public static int[] shuffle(int[] list) {
        int[] shuffle          = new int[list.length];
        int[] rightHalfReverse = reverse(Arrays.copyOfRange(list, list.length / 2, list.length)); //Takes the right half of @list and reverses it

        //Copy left half of @list to even numbered indexes of @shuffle
        IntStream.range(0, (list.length & 1) == 0 ? list.length / 2 : (list.length / 2) + 1)
                 .forEach(i -> shuffle[2 * i] = list[i]);
        //Copy reversed right half of @list to off numbered indexes of @shuffle
        IntStream.range(1, (list.length & 1) == 0 ? rightHalfReverse.length + 1 : rightHalfReverse.length)
                 .forEach(i -> shuffle[(2 * i) - 1] = rightHalfReverse[i - 1]);

        return shuffle;
    }

    //pre:  list is not null, note is between 22 and 108
    //post:  makes a new array containing each element of list, but where every other element is note
    //[list0, note, list1, note, list2, note, list3, note, list4, note,...]
    //if list:[2, 4, 6, 8] and note:1 -> returns [1,2,1,4,1,6,1,8]
    //needs to work with an array of any size
    public static int[] mixWithNote(int[] list, int note) {
        //Note to self: Create array only version(No ArrayList usage)

        ArrayList<Integer> mixed = new ArrayList<>();

        //Add all elements of @list to @mixed
        Arrays.stream(list)
              .forEach(mixed::add);
        //Adds @note to even numbered indexes of @mixed
        IntStream.range(0, list.length + 6)
                 .filter(i -> (i & 1) == 0)
                 .forEach(i -> mixed.add(i, note));

        return mixed.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
    }

    //pre:  list is at least of length 3
    //post:  makes a new array containing triads (groups of 3 consecutive notes in the scale), each starting with successive elements of list
    //[1st elem, 2nd elem, 3rd elem, 2nd elem, 3rd elem, 4th elem, 3rd elem, 4th elem, 5th elem,...]
    //if list:[1,2,3,4,5,6] -> returns [1,2,3,  2,3,4,  3,4,5, 4,5,6]
    public static int[] triads(int[] list) {
        ArrayList<Integer> triads = new ArrayList<>();

        //Adds the value at the current index and 2 consecutive indexes to @triads
        for (int i = 0; i < list.length; i++) {
            if (i + 3 < list.length) {
                for (int j = i; j < i + 3; j++) {
                    triads.add(list[j]);
                }
            }
        }

        return triads.stream()
                     .mapToInt(Integer::intValue)
                     .toArray();
    }

    //post:  student invents their own mutation to the scale
    public static int[] extraCredit(int[] list) {
        List<Integer> modify = new ArrayList<>();

        //Filter out even numbers in @list, multiply each number by 1.05, and add to @modify
        Arrays.stream(list)
              .filter(note -> (note & 1) == 0)
              .map(note -> (int) (note * 1.05))
              .forEach(modify::add);
        //Generate random ints between 65 and 70 and add to @modify to make it the same length as @list
        new Random().ints(65, 70)
                    .limit(list.length - modify.size())
                    .forEach(modify::add);
        //Randomize the ordering
        Collections.shuffle(modify);

        return modify.stream()
                     .mapToInt(Integer::intValue)
                     .toArray();
    }
} 	

