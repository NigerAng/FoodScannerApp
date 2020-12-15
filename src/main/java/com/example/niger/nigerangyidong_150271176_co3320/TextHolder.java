package com.example.niger.nigerangyidong_150271176_co3320;
//Bug fix https://github.com/googlesamples/android-vision/issues/299

/**Solution for solving text forming in random order
 * Create own list of blocks and assign coordinates to each block
 */
class Textholder implements Comparable<Textholder> {

    private float Y;
    private String text;

    Textholder(float Y, String text) {
        this.Y = Y;
        this.text = text;
    }

    String getText() {
        return text;
    }

    float getY() {
        return Y;
    }

    public int compareTo(Textholder th) {
        if (Y == th.getY()) return 0;
        else if (Y > th.getY()) return 1;
        else return -1;
    }
}
