/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package top.nowandfuture.jmediapipe.math;

/**
 * A single precision floating point 3 by 3 matrix.
 * Primarily to support 3D rotations.
 */
public class Matrix3d {

    /**
     * The first matrix element in the first row.
     */
    public double m00;
    /**
     * The second matrix element in the first row.
     */
    public double m01;
    /**
     * The third matrix element in the first row.
     */
    public double m02;
    /**
     * The first matrix element in the second row.
     */
    public double m10;
    /**
     * The second matrix element in the second row.
     */
    public double m11;
    /**
     * The third matrix element in the second row.
     */
    public double m12;
    /**
     * The first matrix element in the third row.
     */
    public double m20;
    /**
     * The second matrix element in the third row.
     */
    public double m21;
    /**
     * The third matrix element in the third row.
     */
    public double m22;

    /**
     * Constructs and initializes a Matrix3f from the specified nine values.
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     */
    public Matrix3d(double m00, double m01, double m02,
                    double m10, double m11, double m12,
                    double m20, double m21, double m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;

        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;

        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;

    }

    /**
     * Constructs and initializes a Matrix3f from the specified
     * nine-element array.   this.m00 =v[0], this.m01=v[1], etc.
     * @param v the array of length 9 containing in order
     */
    public Matrix3d(double[] v) {
        this.m00 = v[0];
        this.m01 = v[1];
        this.m02 = v[2];

        this.m10 = v[3];
        this.m11 = v[4];
        this.m12 = v[5];

        this.m20 = v[6];
        this.m21 = v[7];
        this.m22 = v[8];

    }



    /**
     *  Constructs a new matrix with the same values as the
     *  Matrix3f parameter.
     *  @param m1  the source matrix
     */
    public Matrix3d(Matrix3d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;

        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;

        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;

    }

    /**
     * Constructs and initializes a Matrix3f to all zeros.
     */
    public Matrix3d() {
        this.m00 = (double) 1.0;
        this.m01 = (double) 0.0;
        this.m02 = (double) 0.0;

        this.m10 = (double) 0.0;
        this.m11 = (double) 1.0;
        this.m12 = (double) 0.0;

        this.m20 = (double) 0.0;
        this.m21 = (double) 0.0;
        this.m22 = (double) 1.0;

    }

    /**
     * Returns a string that contains the values of this Matrix3f.
     * @return the String representation
     */
    @Override
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n"
                + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n"
                + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
    }

    /**
     * Sets this Matrix3f to identity.
     */
    public final void setIdentity() {
        this.m00 = (double) 1.0;
        this.m01 = (double) 0.0;
        this.m02 = (double) 0.0;

        this.m10 = (double) 0.0;
        this.m11 = (double) 1.0;
        this.m12 = (double) 0.0;

        this.m20 = (double) 0.0;
        this.m21 = (double) 0.0;
        this.m22 = (double) 1.0;
    }

    /**
     * Sets the specified row of this matrix3f to the three values provided.
     * @param row the row number to be modified (zero indexed)
     * @param v the replacement row
     */
    public final void setRow(int row, double[] v) {
        switch (row) {
            case 0:
                this.m00 = v[0];
                this.m01 = v[1];
                this.m02 = v[2];
                break;

            case 1:
                this.m10 = v[0];
                this.m11 = v[1];
                this.m12 = v[2];
                break;

            case 2:
                this.m20 = v[0];
                this.m21 = v[1];
                this.m22 = v[2];
                break;

            default:
                throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    /**
     * Sets the specified row of this matrix3f to the Vector provided.
     * @param row the row number to be modified (zero indexed)
     * @param v the replacement row
     */
    public final void setRow(int row, Vec3d v) {
        switch (row) {
            case 0:
                this.m00 = v.x;
                this.m01 = v.y;
                this.m02 = v.z;
                break;

            case 1:
                this.m10 = v.x;
                this.m11 = v.y;
                this.m12 = v.z;
                break;

            case 2:
                this.m20 = v.x;
                this.m21 = v.y;
                this.m22 = v.z;
                break;

            default:
                throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    /**
     * Copies the matrix values in the specified row into the vector parameter.
     * @param row  the matrix row
     * @param v    the vector into which the matrix row values will be copied
     */
    public final void getRow(int row, Vec3d v) {
        if (row == 0) {
            v.x = m00;
            v.y = m01;
            v.z = m02;
        } else if (row == 1) {
            v.x = m10;
            v.y = m11;
            v.z = m12;
        } else if (row == 2) {
            v.x = m20;
            v.y = m21;
            v.z = m22;
        } else {
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    /**
     * Copies the matrix values in the specified row into the array parameter.
     * @param row  the matrix row
     * @param v    the array into which the matrix row values will be copied
     */
    public final void getRow(int row, double[] v) {
        if (row == 0) {
            v[0] = m00;
            v[1] = m01;
            v[2] = m02;
        } else if (row == 1) {
            v[0] = m10;
            v[1] = m11;
            v[2] = m12;
        } else if (row == 2) {
            v[0] = m20;
            v[1] = m21;
            v[2] = m22;
        } else {
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }
}


