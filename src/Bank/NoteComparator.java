package Bank;

import Banknote.Note;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import static Utils.Utils.*;
// import static Utils.Utils.getXOR; 


/**
 * Created by msokol on 4/2/16.
 */
public final class NoteComparator {

    /**
     * Method that makes all the comparison and gives answer whether all the notes are correctly made
     *
     * @param n
     */
    public static boolean noteCompare(ArrayList<Note> n, ArrayList<byte[]> l,
                                      ArrayList<byte[]> r, ArrayList<Note> noteArrayList)
            throws UnsupportedEncodingException,
            NoSuchAlgorithmException {

        boolean a = checkAmounts(n);
        boolean b = checkIDs(n);
        boolean c = checkBitCommitments(n, noteArrayList, l, r);
        SysOut("Amounts check : " + a);
        SysOut("IDs check : " + b);
        SysOut("Bit Commitments check : " + c);

        //////////////////////////////////////////////////
        if (a && b && c) return true;
        else return false;
    }

    /**
     * Check if amounts of all <code>Note</code>s in the <code>ArrayList</code> are the same
     *
     * @param n
     * @return
     */
    private static boolean checkAmounts(ArrayList<Note> n) {
        int s = 0;
        for (int i = 0; i < n.size() - 1; i++
                ) {
            if (Arrays.equals(n.get(i).getAmount(), n.get(i + 1).getAmount())) {
                s = s + 1;
            }
        }

        if (s == n.size() - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if IDs of all <code>Note</code>s in the <code>ArrayList</code> are different
     *
     * @param n
     * @return
     */
    private static boolean checkIDs(ArrayList<Note> n) {
        int s = 0;
        for (int i = 0; i < n.size() - 1; i++
                ) {
            if (Arrays.equals(n.get(i).getId(), n.get(i + 1).getId())) {
                s = s + 1;
            }
        }
        if (s != 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check Bit Commitments from the <code>Notes</code>
     *
     * @param unblindedNotes - Unblinded Notes <code>ArrayList</code>
     * @param l              - Left Safe Strings <code>ArrayList</code>
     * @param r              - right Safe Strings <code>ArrayList</code>
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static boolean checkBitCommitments(ArrayList<Note> unblindedNotes, ArrayList<Note> originalNotes, ArrayList<byte[]> l,
                                               ArrayList<byte[]> r)
            throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        //Sprawdzenie lewego zobowiązania
        int left = 0, right = 0;
        if (unblindedNotes.size() == originalNotes.size()) {
            //SysOut(n.size() + " , " + original.size());
            for (int j = 0; j < unblindedNotes.size(); j++) {
                int s = 0;
                for (int i = 0; i < 100; i++) {
                    //FIXME
                    String Uprime = ArrayBytesToString(unblindedNotes.get(j).getLeftOut(i))
                            + ArrayBytesToString(l.get(i))
                            + ArrayBytesToString(originalNotes.get(j).getLeftMystery(i));
                    byte[] U = getHash(Uprime).getBytes();
                    if (Arrays.equals(unblindedNotes.get(j).getLeftHash(i), U)) {
                        s = s + 1;
                    } else {
                        SysOut("Bit Commitment fault. Aborting protocol!");
                        return false;
                    }
                }
                if (s == 100) {
                    left = left + 1;
                }
            }
            //Sprawdzenie prawego zobowiązania
            for (int j = 0; j < unblindedNotes.size(); j++) {
                int s = 0;
                for (int i = 0; i < 100; i++) {
                    //FIXME
                    String Uprime = ArrayBytesToString(unblindedNotes.get(j).getRightOut(i)) + ArrayBytesToString(r.get(i))
                            + ArrayBytesToString(originalNotes.get(j).getRightMystery(i));
                    byte[] U = getHash(Uprime).getBytes();
                    if (Arrays.equals(unblindedNotes.get(j).getRightHash(i), U)) {
                        s = s + 1;
                    } else {
                        SysOut("Bit Commitment fault. Aborting protocol!");
                        return false;
                    }
                }
                if (s == 100) {
                    right = right + 1;
                }
            }
        }

        if ((right == unblindedNotes.size()) && (left == unblindedNotes.size())) {
            return true;
        } else {
            return false;
        }

    }

	/*
     * private static void checkXOR (ArrayList<Note> n){ for(Note a : n){
	 * for(int i = 0; i < 100; i++){
	 * SysOut(Utils.ArrayBytesToString((a.getLeftMystery(i))) + " xor " +
	 * Utils.ArrayBytesToString(a.getRightMystery(i)) + " = " +
	 * Utils.ArrayBytesToString(getXOR(a.getLeftMystery(i),
	 * a.getRightMystery(i)))); } } }
	 */
}
