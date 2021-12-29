/**
 * @author : Joey Huang
 * @since : 12/27/21, Mon
 **/

import edu.princeton.cs.algs4.Picture;
import java.util.Stack;

public class SeamCarver {
    private Picture pic;
    private double[][] energies;
    private boolean transposed;
    private boolean fromHorizontal;
    private int width;
    private int height;

    private void checkNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    private double gradient(int c1, int c2) {
        int c1r = (c1 >> 16) & 0xFF;
        int c1g = (c1 >>  8) & 0xFF;
        int c2r = (c2 >> 16) & 0xFF;
        int c2g = (c2 >>  8) & 0xFF;
        return Math.pow(c1r - c2r, 2) + Math.pow(c1g - c2g, 2) + Math.pow((c1 & 0xFF) - (c2 & 0xFF), 2);
    }

    private void transpose() {
        double[][] tmpEnergies = new double[this.height][this.width];
        for (int j = 0; j < this.height; j++) {
            for (int i = 0; i < this.width; i++) {
                tmpEnergies[j][i] = this.energies[i][j];
            }
        }
        this.energies = tmpEnergies;
        this.transposed = !this.transposed;
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }

    private void validateSeam(int[] seam, boolean isVert) {
        for (int j : seam) {
            if (isVert) if (j < 0 || j >= this.width()) throw new IllegalArgumentException();
            if (!isVert) if (j < 0 || j >= this.height()) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) throw new IllegalArgumentException();
        }
    }

    private void calcEnergy(){
        this.energies = new double[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (i == 0 || j == 0 || i == this.width - 1 || j == this.height - 1) {
                    energies[i][j] = 1000;
                } else {
                    if (this.transposed) { // switch rows & cols if image is transposed
                        energies[i][j] = Math.sqrt(this.gradient(pic.getRGB(j - 1, i), pic.getRGB(j + 1, i)) +
                                this.gradient(pic.getRGB(j, i - 1), pic.getRGB(j, i + 1)));
                    } else {
                        energies[i][j] = Math.sqrt(this.gradient(pic.getRGB(i - 1, j), pic.getRGB(i + 1, j)) +
                                this.gradient(pic.getRGB(i, j - 1), pic.getRGB(i, j + 1)));
                    }
                }
            }
        }
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.checkNull(picture);
        this.pic = new Picture(picture);
        this.width = this.pic.width();
        this.height = this.pic.height();
        this.transposed = false;
        this.fromHorizontal = false;
        this.calcEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(this.pic);
    }

    // width of current picture
    public int width() {
        return this.pic.width();
    }

    // height of current picture
    public int height() {
        return this.pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1) throw new IllegalArgumentException();
        return this.transposed ? this.energies[y][x] : this.energies[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!this.transposed) this.transpose();
        this.fromHorizontal = true;
        int[] arr = this.findVerticalSeam();
        this.fromHorizontal = false;
        return arr;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (this.transposed && !this.fromHorizontal) this.transpose();

        double[][] distTo = new double[this.width][this.height];
        int[][] edgeFrom = new int[this.width][this.height];

        for (int i = 0; i < this.width; i++) {
            distTo[i][0] = energies[i][0];
            edgeFrom[i][0] = i;
        }

        for (int j = 0; j < this.height - 1; j++) {
            for (int i = 0; i < this.width; i++) {
                distTo[i][j + 1] = Double.POSITIVE_INFINITY;
            }

            for (int i = 0; i < this.width; i++) {
                if (i > 0 && distTo[i][j] + this.energies[i - 1][j + 1] < distTo[i - 1][j + 1]) { // left
                    distTo[i - 1][j + 1] = distTo[i][j] + this.energies[i - 1][j + 1];
                    edgeFrom[i - 1][j + 1] = i;
                }

                if (distTo[i][j] + this.energies[i][j + 1] < distTo[i][j + 1]) { // center
                    distTo[i][j + 1] = distTo[i][j] + this.energies[i][j + 1];
                    edgeFrom[i][j + 1] = i;
                }

                if (i < this.width - 1 && distTo[i][j] + this.energies[i + 1][j + 1] < distTo[i + 1][j + 1]) { // right
                    distTo[i + 1][j + 1] = distTo[i][j] + this.energies[i + 1][j + 1];
                    edgeFrom[i + 1][j + 1] = i;
                }
            }
        }

        Stack<Integer> minPath = new Stack<>();

        double minDist = Double.POSITIVE_INFINITY;
        int smallestBot = 0;
        for (int i = 0; i < this.width; i ++) {
            if (distTo[i][this.height - 1] < minDist){
                minDist = distTo[i][this.height - 1];
                smallestBot = i;
            }
        }
        for (int j = this.height - 1; j >= 0; j--) {
            minPath.push(smallestBot);
            smallestBot = edgeFrom[smallestBot][j];
        }

        int[] arr = new int[this.height];
        int i = 0;
        while (!minPath.empty()) arr[i++] = minPath.pop();
        return arr;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        this.checkNull(seam);
        if (seam.length != this.width() || this.height() <= 1) throw new IllegalArgumentException();
        this.validateSeam(seam, false);
        Picture tmpPicture = new Picture(this.width(), this.height() - 1);
        for (int j = 0; j < this.height() - 1; j++) {
            for (int i = 0; i < this.width(); i++) {
                if (j < seam[i]) {
                    tmpPicture.setRGB(i, j, this.pic.getRGB(i, j));
                } else {
                    tmpPicture.setRGB(i, j, this.pic.getRGB(i, j + 1));
                }
            }
        }
        this.pic = tmpPicture;
        if (this.transposed) this.width--;
        else this.height--;
        this.calcEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        this.checkNull(seam);
        if (seam.length != this.height() || this.width() <= 1) throw new IllegalArgumentException();
        this.validateSeam(seam, true);
        Picture tmpPicture = new Picture(this.width() - 1, this.height());
        for (int j = 0; j < this.height(); j++) {
            for (int i = 0; i < this.width() - 1; i++) {
                if (i < seam[j]) {
                    tmpPicture.setRGB(i, j, this.pic.getRGB(i, j));
                } else {
                    tmpPicture.setRGB(i, j, this.pic.getRGB(i + 1, j));
                }
            }
        }
        this.pic = tmpPicture;
        if (this.transposed) this.height--;
        else this.width--;
        this.calcEnergy();
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
